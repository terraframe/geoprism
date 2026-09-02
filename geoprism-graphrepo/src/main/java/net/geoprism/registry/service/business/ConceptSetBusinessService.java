/**
 * CopyrighConceptSet (c) 2023 TerraFrame, Inc. All rights reserved.
 *
 * This file is parConceptSet of Geoprism(tm).
 *
 * Geoprism(tm) is free software: you can redistribute iConceptSet and/or modify
 * iConceptSet under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the License,
 * or (aConceptSet your option) any later version.
 *
 * Geoprism(tm) is distributed in the hope thaConceptSet iConceptSet will be
 * useful, but WITHOUConceptSet ANY WARRANTY; withouConceptSet even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Geoprism(tm). If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism.registry.service.business;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.commongeoregistry.adapter.constants.DefaultAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.runwaysdk.business.graph.EdgeObject;
import com.runwaysdk.business.graph.GraphQuery;
import com.runwaysdk.business.graph.VertexObject;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.session.Session;

import net.geoprism.registry.cache.TransactionLRUCache;
import net.geoprism.registry.conversion.RegistryLocalizedValueConverter;
import net.geoprism.registry.graph.ConceptClass;
import net.geoprism.registry.graph.ConceptEdgeType;
import net.geoprism.registry.graph.ConceptSet;
import net.geoprism.registry.graph.ConceptVertex;
import net.geoprism.registry.model.EdgeConstant;
import net.geoprism.registry.model.GeoObjectMetadata;
import net.geoprism.registry.service.permission.PermissionServiceIF;
import net.geoprism.registry.view.ConceptSetDTO;
import net.geoprism.registry.view.DiscreteType;

@Service
public class ConceptSetBusinessService implements ConceptSetBusinessServiceIF
{
  @Autowired
  protected PermissionServiceIF                         permissions;

  @Autowired
  protected ConceptClassBusinessServiceIF               cClassService;

  @Autowired
  protected ConceptEdgeTypeBusinessServiceIF            cEdgeTypeService;

  @Autowired
  protected ConceptObjectBusinessServiceIF              cObjectService;

  private final TransactionLRUCache<String, ConceptSet> cache;

  public ConceptSetBusinessService()
  {
    this(UUID.randomUUID().toString());
  }

  public ConceptSetBusinessService(String cacheName)
  {
    this.cache = new TransactionLRUCache<String, ConceptSet>(cacheName, (v) -> {

      return new String[] { v.getCode(), v.getOid() };
    }, 20);

  }

  private TransactionLRUCache<String, ConceptSet> getCache()
  {
    return cache;
  }

  @Override
  @Transaction
  public void delete(ConceptSet type)
  {
    type.delete();

    this.getCache().remove(type);
  }

  @Override
  @Transaction
  public ConceptSet apply(ConceptSetDTO dto)
  {
    ConceptSet type = this.getByCode(dto.getCode()).orElseGet(() -> {
      ConceptSet t = new ConceptSet();
      t.setCode(dto.getCode());

      return t;
    });

    this.fromDTO(type, dto);

    boolean isNew = type.isNew();

    if (isNew && StringUtils.isNotBlank(dto.getRootTerm()))
    {
      MdVertexDAOIF mdVertex = MdVertexDAO.getMdVertexDAO(ConceptVertex.CLASS);

      StringBuilder statement = new StringBuilder();
      statement.append("  SELECT FROM " + mdVertex.getDBClassName());
      statement.append("  WHERE code = :code");

      GraphQuery<VertexObject> query = new GraphQuery<VertexObject>(statement.toString());
      query.setParameter("code", dto.getRootTerm());

      Optional.ofNullable(query.getSingleResult()).ifPresent(root -> {

        // TODO: Validate that the root is part of the valid concept classes
        type.setRootTerm(root);
      });
    }

    type.apply();

    // Add all of concept classes and concept edge types
    if (isNew)
    {
      dto.getConceptClasses().forEach(code -> {
        this.addConceptClass(type, this.cClassService.getByCodeOrThrow(code));
      });

      dto.getConceptEdgeTypes().forEach(code -> {
        this.addConceptEdgeType(type, this.cEdgeTypeService.getByCodeOrThrow(code));
      });
    }

    this.getCache().put(type);

    return type;
  }

  protected void fromDTO(ConceptSet type, ConceptSetDTO dto)
  {
    type.setDiscreteType(dto.getDiscreteType().name());

    RegistryLocalizedValueConverter.populate(type, ConceptSet.DISPLAYLABEL, dto.getDisplayLabel());
    RegistryLocalizedValueConverter.populate(type, ConceptSet.DESCRIPTION, dto.getDescription());
  }

  @Override
  public Optional<ConceptSet> get(String oid)
  {
    return this.cache.get(oid, () -> {

      MdVertexDAOIF mdVertex = MdVertexDAO.getMdVertexDAO(ConceptSet.CLASS);

      StringBuilder statement = new StringBuilder();
      statement.append("SELECT FROM " + mdVertex.getDBClassName());
      statement.append(" WHERE oid = :oid");

      GraphQuery<ConceptSet> query = new GraphQuery<ConceptSet>(statement.toString());
      query.setParameter("oid", oid);

      return Optional.ofNullable(query.getSingleResult());
    });
  }

  @Override
  public Optional<ConceptSet> getByCode(String code)
  {
    return this.cache.get(code, () -> {

      MdVertexDAOIF mdVertex = MdVertexDAO.getMdVertexDAO(ConceptSet.CLASS);

      StringBuilder statement = new StringBuilder();
      statement.append("SELECT FROM " + mdVertex.getDBClassName());
      statement.append(" WHERE code = :code");

      GraphQuery<ConceptSet> query = new GraphQuery<ConceptSet>(statement.toString());
      query.setParameter("code", code);

      return Optional.ofNullable(query.getSingleResult());
    });
  }

  @Override
  public ConceptSet getByCodeOrThrow(String code)
  {
    return this.getByCode(code).orElseThrow(() -> {
      MdVertexDAOIF mdVertex = MdVertexDAO.getMdVertexDAO(ConceptSet.CLASS);

      net.geoprism.registry.DataNotFoundException ex = new net.geoprism.registry.DataNotFoundException();
      ex.setTypeLabel(mdVertex.getDisplayLabel(Session.getCurrentLocale()));
      ex.setDataIdentifier(code);
      ex.setAttributeLabel(GeoObjectMetadata.get().getAttributeDisplayLabel(DefaultAttribute.CODE.getName()));

      return ex;
    });
  }

  @Override
  public List<ConceptSet> getAll()
  {
    MdVertexDAOIF mdVertex = MdVertexDAO.getMdVertexDAO(ConceptSet.CLASS);

    StringBuilder statement = new StringBuilder();
    statement.append("SELECT FROM " + mdVertex.getDBClassName());
    statement.append(" ORDER BY code DESC");

    GraphQuery<ConceptSet> query = new GraphQuery<ConceptSet>(statement.toString());

    return query.getResults().stream() //
        .sorted((a, b) -> a.getLabel().getValue().compareTo(b.getLabel().getValue())) //
        .toList(); //
  }

  @Override
  public ConceptSetDTO toDTO(ConceptSet type)
  {
    ConceptSetDTO dto = new ConceptSetDTO();

    this.toDTO(dto, type);

    return dto;
  }

  protected void toDTO(ConceptSetDTO dto, ConceptSet type)
  {
    dto.setCode(type.getCode());
    dto.setDisplayLabel(type.getLabel());
    dto.setDiscreteType(DiscreteType.valueOf(type.getDiscreteType()));

    this.getConceptClasses(type).forEach(cClass -> dto.getConceptClasses().add(cClass.getCode()));
    this.getConceptEdgeTypes(type).forEach(cEdge -> dto.getConceptEdgeTypes().add(cEdge.getCode()));

    if (StringUtils.isNotBlank(type.getRootTerm()))
    {
      this.cObjectService.getByOid(type.getRootTerm()).ifPresent(root -> dto.setRootTerm(root.getCode()));
    }
  }

  @Override
  public EdgeObject addConceptClass(ConceptSet type, ConceptClass conceptClass)
  {
    if (type.getDiscreteType().equals(DiscreteType.TAXONOMY.name()) && //
        this.getConceptClasses(type).size() > 0)
    {
      throw new UnsupportedOperationException("An taxonomy can only have a single concept class assignment");
    }
    else if (type.getDiscreteType().equals(DiscreteType.ENUMERATION.name()) && //
        this.getConceptClasses(type).size() > 0)
    {
      throw new UnsupportedOperationException("An enumeration can only have a single concept class assignment");
    }
    else if (type.getDiscreteType().equals(DiscreteType.ONTOLOGY.name()) && //
        this.getConceptClasses(type).stream().anyMatch(t -> t.getCode().equals(conceptClass.getCode())))
    {
      throw new UnsupportedOperationException("The concept class [" + conceptClass.getCode() + "] is already part of the concept set");
    }

    EdgeObject edge = type.addChild(conceptClass, EdgeConstant.HAS_CONCEPT.getMdEdge());
    edge.apply();

    return edge;
  }

  @Override
  public EdgeObject addConceptEdgeType(ConceptSet type, ConceptEdgeType conceptEdgeType)
  {
    if (type.getDiscreteType().equals(DiscreteType.TAXONOMY.name()) && //
        this.getConceptEdgeTypes(type).size() > 0)
    {
      throw new UnsupportedOperationException("A taxonomy can only have a single concept edge type assignment");
    }
    else if (type.getDiscreteType().equals(DiscreteType.ENUMERATION.name()) && //
        this.getConceptEdgeTypes(type).size() > 0)
    {
      throw new UnsupportedOperationException("An enumeration can only have a single concept edge type assignment");
    }
    else if (type.getDiscreteType().equals(DiscreteType.ONTOLOGY.name()) && //
        this.getConceptEdgeTypes(type).stream().anyMatch(t -> t.getCode().equals(conceptEdgeType.getCode())))
    {
      throw new UnsupportedOperationException("The concept edge type [" + conceptEdgeType.getCode() + "] is already part of the concept set");
    }

    EdgeObject edge = type.addChild(conceptEdgeType, EdgeConstant.HAS_CONCEPT_EDGE.getMdEdge());
    edge.apply();

    return edge;
  }

  @Override
  public List<EdgeObject> getConceptClassEdges(ConceptSet type)
  {
    return type.getChildEdges(EdgeConstant.HAS_CONCEPT.getMdEdge(), EdgeObject.class);
  }

  @Override
  public List<EdgeObject> getConceptEdgeTypeEdges(ConceptSet type)
  {
    return type.getChildEdges(EdgeConstant.HAS_CONCEPT_EDGE.getMdEdge(), EdgeObject.class);
  }

  @Override
  public List<ConceptClass> getConceptClasses(ConceptSet type)
  {
    return type.getChildren(EdgeConstant.HAS_CONCEPT.getMdEdge(), ConceptClass.class);
  }

  @Override
  public List<ConceptEdgeType> getConceptEdgeTypes(ConceptSet type)
  {
    return type.getChildren(EdgeConstant.HAS_CONCEPT_EDGE.getMdEdge(), ConceptEdgeType.class);
  }

}
