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

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import org.apache.commons.lang.StringUtils;
import org.commongeoregistry.adapter.constants.DefaultAttribute;
import org.commongeoregistry.adapter.dataaccess.LocalizedValue;
import org.commongeoregistry.adapter.metadata.AttributeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.runwaysdk.business.graph.GraphQuery;
import com.runwaysdk.constants.IndexTypes;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeCharacterInfo;
import com.runwaysdk.constants.MdAttributeConcreteInfo;
import com.runwaysdk.constants.MdAttributeGraphReferenceInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.graph.MdVertexInfo;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.metadata.MdAttributeCharacterDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeGraphReferenceDAO;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.gis.constants.MdGeoVertexInfo;
import com.runwaysdk.session.Session;
import com.runwaysdk.system.metadata.MdVertex;

import net.geoprism.configuration.GeoprismProperties;
import net.geoprism.ontology.Classifier;
import net.geoprism.registry.CodeLengthException;
import net.geoprism.registry.Organization;
import net.geoprism.registry.RegistryConstants;
import net.geoprism.registry.cache.TransactionLRUCache;
import net.geoprism.registry.conversion.RegistryLocalizedValueConverter;
import net.geoprism.registry.conversion.TermConverter;
import net.geoprism.registry.graph.ConceptClass;
import net.geoprism.registry.graph.DataSource;
import net.geoprism.registry.model.GeoObjectMetadata;
import net.geoprism.registry.model.ServerOrganization;
import net.geoprism.registry.service.permission.PermissionServiceIF;
import net.geoprism.registry.view.ConceptClassDTO;

@Service
public class ConceptClassBusinessService extends ObjectClassBusinessService<ConceptClass> implements ConceptClassBusinessServiceIF
{
  @Autowired
  private ClassificationTypeBusinessServiceIF             cTypeService;

  @Autowired
  private PermissionServiceIF                             permissions;

  private final TransactionLRUCache<String, ConceptClass> cache;

  public ConceptClassBusinessService()
  {
    this.cache = new TransactionLRUCache<String, ConceptClass>("t-b-type-cache", (v) -> {

      return new String[] { v.getCode(), v.getMdVertexOid() };
    }, 20);
  }

  @Override
  protected void put(ConceptClass type)
  {
    this.cache.put(type);
  }

  @Override
  @Transaction
  public void delete(ConceptClass type)
  {
    // Delete the term root
    Classifier classRootTerm = TermConverter.buildIfNotExistGeoObjectTypeClassifier(type);
    classRootTerm.delete();

    MdVertex mdVertex = type.getMdVertex();

    type.delete();

    mdVertex.delete();

    this.cache.remove(type);
  }

  @Override
  public ConceptClassDTO toDTO(ConceptClass type)
  {
    return toDTO(type, false, false);
  }

  @Override
  public ConceptClassDTO toDTO(ConceptClass type, boolean includeAttribute, boolean flattenLocalAttributes)
  {
    return this.toDTO(type, includeAttribute, flattenLocalAttributes, (attribute) -> true);
  }

  @Override
  public ConceptClassDTO toDTO(ConceptClass type, boolean includeAttribute, boolean flattenLocalAttributes, Predicate<AttributeType> filter)
  {
    ServerOrganization organization = type.getServerOrganization();

    ConceptClassDTO dto = new ConceptClassDTO();
    dto.setCode(type.getCode());
    dto.setOrganization(organization.getCode());
    dto.setOrganizationLabel(organization.getDisplayLabel().getValue());
    dto.setOrigin(type.getOrigin());
    dto.setSequence(type.getSequence());
    dto.setDisplayLabel(type.getLabel());

    if (type.isAppliedToDb())
    {
      dto.setOid(type.getOid());
    }

    if (includeAttribute)
    {
      // type.getAttributeMap().values().stream() //
      // .filter(filter) //
      // .sorted((a, b) -> {
      // return a.getName().compareTo(b.getName());
      // }).flatMap(attr -> {
      // if (flattenLocalAttributes && attr instanceof AttributeLocalType)
      // {
      // List<ConceptClassDTO> list = new LinkedList<>();
      // list.add(this.serializeLocale(type, attr,
      // LocalizedValue.DEFAULT_LOCALE,
      // LocalizationFacade.localize(DefaultAttribute.DISPLAY_LABEL.getName())));
      //
      // for (SupportedLocaleIF locale :
      // LocalizationFacade.getSupportedLocales())
      // {
      // list.add(this.serializeLocale(type, attr,
      // locale.getLocale().toString(), locale.getDisplayLabel().getValue()));
      // }
      //
      // return list.stream();
      // }
      //
      // return Stream.of(attr.toDTO());
      // }).collect(JsonCollectors.toDTOArray());
      //
      // object.add(ConceptClass.JSON_ATTRIBUTES, attributes);
    }

    return dto;
  }

  // private ConceptClassDTO serializeLocale(ConceptClass type, AttributeType
  // attributeType, String key, String label)
  // {
  // ConceptClassDTO object = attributeType.toDTO();
  // object.addProperty("locale", key);
  // object.addProperty(AttributeType.JSON_CODE, attributeType.getName());
  //
  // ConceptClassDTO jaLabel =
  // object.get(AttributeType.JSON_LOCALIZED_LABEL).getAsConceptClassDTO();
  // String value = jaLabel.get(LocalizedValue.LOCALIZED_VALUE).getAsString();
  // value += " (" + label + ")";
  // jaLabel.addProperty(LocalizedValue.LOCALIZED_VALUE, value);
  //
  // return object;
  // }

  @Transaction
  @Override
  public ConceptClass apply(ConceptClassDTO dto)
  {
    String code = dto.getCode();
    String organizationCode = dto.getOrganization();
    ServerOrganization organization = ServerOrganization.getByCode(organizationCode);
    String origin = !StringUtils.isBlank(dto.getOrigin()) ? dto.getOrigin() : GeoprismProperties.getOrigin();

    ServiceFactory.getGeoObjectTypePermissionService().enforceCanCreate(organization.getCode(), false);

    cTypeService.validateName(code);

    if (code.length() > 64)
    {
      // Setting the typename on the MdBusiness creates this limitation.
      CodeLengthException ex = new CodeLengthException();
      ex.setLength(64);
      throw ex;
    }

    LocalizedValue localizedValue = dto.getDisplayLabel();

    ConceptClass conceptClass = !StringUtils.isBlank(dto.getOid()) ? ConceptClass.get(dto.getOid()) : new ConceptClass();
    conceptClass.setCode(code);
    conceptClass.setOrganization(organization.getGraphOrganization());

    RegistryLocalizedValueConverter.populate(conceptClass, ConceptClass.DISPLAYLABEL, localizedValue);

    boolean isNew = conceptClass.isNew();

    if (isNew)
    {
      MdVertexDAO mdVertex = MdVertexDAO.newInstance();
      mdVertex.setValue(MdGeoVertexInfo.PACKAGE, RegistryConstants.BUSINESS_PACKAGE);
      mdVertex.setValue(MdGeoVertexInfo.NAME, code);
      mdVertex.setValue(MdGeoVertexInfo.ENABLE_CHANGE_OVER_TIME, MdAttributeBooleanInfo.FALSE);
      mdVertex.setValue(MdGeoVertexInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
      RegistryLocalizedValueConverter.populate(mdVertex, MdVertexInfo.DISPLAY_LABEL, localizedValue);
      mdVertex.apply();

      // DefaultAttribute.CODE
      MdAttributeCharacterDAO vertexCodeMdAttr = MdAttributeCharacterDAO.newInstance();
      vertexCodeMdAttr.setValue(MdAttributeConcreteInfo.NAME, DefaultAttribute.CODE.getName());
      vertexCodeMdAttr.setStructValue(MdAttributeConcreteInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, DefaultAttribute.CODE.getDefaultLocalizedName());
      vertexCodeMdAttr.setStructValue(MdAttributeConcreteInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, DefaultAttribute.CODE.getDefaultDescription());
      vertexCodeMdAttr.setValue(MdAttributeCharacterInfo.SIZE, MdAttributeCharacterInfo.MAX_CHARACTER_SIZE);
      vertexCodeMdAttr.setValue(MdAttributeConcreteInfo.DEFINING_MD_CLASS, mdVertex.getOid());
      vertexCodeMdAttr.setValue(MdAttributeConcreteInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
      vertexCodeMdAttr.addItem(MdAttributeConcreteInfo.INDEX_TYPE, IndexTypes.UNIQUE_INDEX.getOid());
      vertexCodeMdAttr.apply();

      // DefaultAttribute.DATA_SOURCE
      MdAttributeGraphReferenceDAO sourceAttr = MdAttributeGraphReferenceDAO.newInstance();
      sourceAttr.setValue(MdAttributeConcreteInfo.NAME, DefaultAttribute.DATA_SOURCE.getName());
      sourceAttr.setStructValue(MdAttributeBooleanInfo.DISPLAY_LABEL, LocalizedValue.DEFAULT_LOCALE, DefaultAttribute.DATA_SOURCE.getDefaultLocalizedName());
      sourceAttr.setStructValue(MdAttributeBooleanInfo.DESCRIPTION, LocalizedValue.DEFAULT_LOCALE, DefaultAttribute.DATA_SOURCE.getDefaultDescription());
      sourceAttr.setValue(MdAttributeConcreteInfo.DEFINING_MD_CLASS, mdVertex.getOid());
      sourceAttr.setValue(MdAttributeGraphReferenceInfo.REFERENCE_MD_VERTEX, MdVertexDAO.getMdVertexDAO(DataSource.CLASS).getOid());
      sourceAttr.setValue(MdAttributeConcreteInfo.REQUIRED, false);
      sourceAttr.apply();

      conceptClass.setMdVertexId(mdVertex.getOid());
      conceptClass.setOrigin(origin);
      conceptClass.setSequence(dto.getSequence() != null ? dto.getSequence() : 0L);
    }
    else
    {
      if (conceptClass.getOrigin().equals(GeoprismProperties.getOrigin()))
      {
        conceptClass.setSequence(conceptClass.getSequence() + 1);
      }
      else if (dto.getSequence() != null)
      {
        conceptClass.setSequence(dto.getSequence());
      }
    }

    return apply(conceptClass);
  }

  @Transaction
  @Override
  public ConceptClass apply(ConceptClass ConceptClass)
  {
    ConceptClass.apply();

    this.cache.put(ConceptClass);

    return ConceptClass;
  }

  @Override
  public Optional<ConceptClass> getByCode(String code)
  {
    return this.cache.get(code, () -> {

      MdVertexDAOIF mdVertex = MdVertexDAO.getMdVertexDAO(ConceptClass.CLASS);

      StringBuilder statement = new StringBuilder();
      statement.append("SELECT FROM " + mdVertex.getDBClassName());
      statement.append(" WHERE code = :code");

      GraphQuery<ConceptClass> query = new GraphQuery<ConceptClass>(statement.toString());
      query.setParameter("code", code);

      return Optional.ofNullable(query.getSingleResult());
    });
  }

  @Override
  public ConceptClass getByCodeOrThrow(String code)
  {
    return this.getByCode(code).orElseThrow(() -> {
      MdVertexDAOIF mdVertex = MdVertexDAO.getMdVertexDAO(ConceptClass.CLASS);

      net.geoprism.registry.DataNotFoundException ex = new net.geoprism.registry.DataNotFoundException();
      ex.setTypeLabel(mdVertex.getDisplayLabel(Session.getCurrentLocale()));
      ex.setDataIdentifier(code);
      ex.setAttributeLabel(GeoObjectMetadata.get().getAttributeDisplayLabel(DefaultAttribute.CODE.getName()));

      return ex;
    });
  }

  @Override
  public List<ConceptClass> getAll()
  {
    List<ConceptClass> response = new LinkedList<>();

    ServerOrganization.getSortedOrganizations().stream().filter(o -> this.permissions.isMember(o)).forEach(org -> {

      MdVertexDAOIF mdVertex = MdVertexDAO.getMdVertexDAO(ConceptClass.CLASS);

      StringBuilder statement = new StringBuilder();
      statement.append("SELECT FROM " + mdVertex.getDBClassName());
      statement.append(" WHERE organization = :organization");
      statement.append(" ORDER BY code DESC");

      GraphQuery<ConceptClass> query = new GraphQuery<ConceptClass>(statement.toString());
      query.setParameter("organization", org.getGraphOrganization().getRID());

      query.getResults().stream() //
          .sorted((a, b) -> a.getLabel().getValue().compareTo(b.getLabel().getValue())) //
          .forEach(type -> response.add(type)); //
    });

    return response;
  }

  @Override
  public List<ConceptClass> getForOrganization(ServerOrganization organization)
  {
    return this.getForOrganization(organization.getOrganization());
  }

  @Override
  public List<ConceptClass> getForOrganization(Organization organization)
  {
    ServerOrganization org = ServerOrganization.get(organization);

    MdVertexDAOIF mdVertex = MdVertexDAO.getMdVertexDAO(ConceptClass.CLASS);

    StringBuilder statement = new StringBuilder();
    statement.append("SELECT FROM " + mdVertex.getDBClassName());
    statement.append(" WHERE organization = :organization");
    statement.append(" ORDER BY code DESC");

    GraphQuery<ConceptClass> query = new GraphQuery<ConceptClass>(statement.toString());
    query.setParameter("organization", org.getGraphOrganization().getRID());

    return query.getResults();
  }

  @Override
  public ConceptClass getByMdVertex(MdVertexDAOIF mdVertex)
  {
    return this.cache.get(mdVertex.getOid(), () -> {
      MdVertexDAOIF table = MdVertexDAO.getMdVertexDAO(ConceptClass.CLASS);

      StringBuilder statement = new StringBuilder();
      statement.append("SELECT FROM " + table.getDBClassName());
      statement.append(" WHERE mdVertex = :mdVertex");
      statement.append(" ORDER BY code DESC");

      GraphQuery<ConceptClass> query = new GraphQuery<ConceptClass>(statement.toString());
      query.setParameter("mdVertex", mdVertex.getOid());

      return Optional.ofNullable(query.getSingleResult());
    }).orElse(null);
  }

}
