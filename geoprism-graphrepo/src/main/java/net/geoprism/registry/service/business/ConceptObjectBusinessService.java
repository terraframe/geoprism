/**
 * Copyright (c) 2023 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Geoprism(tm).
 *
 * Geoprism(tm) is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * Geoprism(tm) is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Geoprism(tm). If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism.registry.service.business;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.commongeoregistry.adapter.metadata.AttributeClassificationType;
import org.springframework.stereotype.Service;

import com.runwaysdk.business.graph.GraphQuery;
import com.runwaysdk.business.graph.VertexObject;
import com.runwaysdk.dataaccess.graph.VertexObjectDAO;

import net.geoprism.registry.graph.ConceptClass;
import net.geoprism.registry.graph.ConceptEdgeType;
import net.geoprism.registry.graph.ConceptSet;
import net.geoprism.registry.graph.ConceptVertex;
import net.geoprism.registry.model.ConceptObject;
import net.geoprism.registry.model.EdgeConstant;
import net.geoprism.registry.view.ConceptClassDTO;

@Service
public class ConceptObjectBusinessService extends ObjectEdgeBusinessService<ConceptObject, ConceptClass, ConceptClassDTO, ConceptEdgeType, ConceptObject> implements ConceptObjectBusinessServiceIF
{
  private final ConceptSetBusinessServiceIF   setService;

  private final ConceptClassBusinessServiceIF cClassService;

  public ConceptObjectBusinessService(ConceptClassBusinessServiceIF typeService, ConceptSetBusinessServiceIF setService, ConceptClassBusinessServiceIF cClassService)
  {
    super(typeService, ConceptVertex.CLASS);

    this.setService = setService;
    this.cClassService = cClassService;
  }

  @Override
  public ConceptObject newInstance(ConceptClass type)
  {
    VertexObject vertex = VertexObject.instantiate(VertexObjectDAO.newInstance(type.getMdVertexDAO()));

    return new ConceptObject(type, vertex, new TreeMap<>());
  }

  @Override
  protected ConceptObject build(ConceptClass type, VertexObject current, Map<String, List<VertexObject>> nodeMap, Date date)
  {
    return new ConceptObject(type, current, nodeMap, date);
  }

  @Override
  protected boolean isValidEdge(ConceptObject child, ConceptEdgeType type, ConceptObject parent, Date startDate, Date endDate)
  {
    boolean isValid = super.isValidEdge(child, type, parent, startDate, endDate);

    if (isValid)
    {
      // Concept edges must be a DAG
      return !this.isCycle(child, type, parent, startDate, endDate);
    }

    return true;
  }

  @Override
  public Optional<ConceptObject> getByCode(ConceptSet conceptSet, String typeCode, String code)
  {
    String rootOid = conceptSet.getRootTerm();

    if (StringUtils.isNotBlank(rootOid))
    {
      return this.getByOid(rootOid).map(rootTerm -> {
        List<ConceptEdgeType> edges = this.setService.getConceptEdgeTypes(conceptSet);
        String edgeNames = String.join(", ", edges.stream().map(e -> "'" + e.getMdEdge().getDbClassName() + "'").toList());

        StringBuilder statement = new StringBuilder();
        statement.append("TRAVERSE out('" + EdgeConstant.HAS_VALUE.getDBClassName() + "', '" + EdgeConstant.HAS_GEOMETRY.getDBClassName() + "') FROM (");
        statement.append("  SELECT FROM (");
        statement.append("    TRAVERSE out(" + edgeNames + ") FROM " + rootTerm.getRID());
        statement.append("  )");
        statement.append("  WHERE code = :code");
        statement.append(")");

        GraphQuery<VertexObject> query = new GraphQuery<VertexObject>(statement.toString());
        query.setParameter("code", code);

        return this.processSingleResult(query.getResults(), null);
      });
    }
    else
    {
      ConceptClass type = this.setService.getConceptClasses(conceptSet).stream() //
          .filter(t -> t.getCode().equals(typeCode)) //
          .findAny().orElseThrow(() -> {
            return new UnsupportedOperationException("The concept class [" + typeCode + "] is not a member of the concept set");
          });

      return this.getByCode(type, code);
    }
  }

  @Override
  public Optional<ConceptObject> getByCode(AttributeClassificationType attribute, String code)
  {
    ConceptSet conceptSet = this.setService.getByCodeOrThrow(attribute.getConceptSet());

    return this.getByCode(this.cClassService.getByCodeOrThrow(attribute.getRootTerm().getType()), attribute.getRootTerm().getCode()).map(root -> {
      List<ConceptEdgeType> edges = this.setService.getConceptEdgeTypes(conceptSet);
      String edgeNames = String.join(", ", edges.stream().map(e -> "'" + e.getMdEdge().getDbClassName() + "'").toList());

      StringBuilder statement = new StringBuilder();
      statement.append("TRAVERSE out('" + EdgeConstant.HAS_VALUE.getDBClassName() + "', '" + EdgeConstant.HAS_GEOMETRY.getDBClassName() + "') FROM (");
      statement.append("  SELECT FROM (");
      statement.append("    TRAVERSE outE(" + edgeNames + ")[(:startDate BETWEEN startDate AND endDate)].in FROM " + root.getRID());
      statement.append("  )");
      statement.append("  WHERE code = :code");
      statement.append(")");

      GraphQuery<VertexObject> query = new GraphQuery<VertexObject>(statement.toString());
      query.setParameter("code", code);
      query.setParameter("startDate", attribute.getStartDate());

      return this.processSingleResult(query.getResults(), null);
    });

  }
}
