/**
 * Copyright (c) 2023 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Geoprism(tm).
 *
 * Geoprism(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Geoprism(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Geoprism(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism.registry.service.business;

import java.util.List;
import java.util.function.Predicate;

import org.commongeoregistry.adapter.constants.DefaultAttribute;
import org.commongeoregistry.adapter.dataaccess.LocalizedValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.runwaysdk.business.graph.GraphQuery;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.graph.MdVertexInfo;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.gis.constants.MdGeoVertexInfo;
import com.runwaysdk.system.metadata.MdVertex;

import net.geoprism.configuration.GeoprismProperties;
import net.geoprism.registry.CodeLengthException;
import net.geoprism.registry.RegistryConstants;
import net.geoprism.registry.cache.ClearObjectCacheEvent;
import net.geoprism.registry.conversion.RegistryLocalizedValueConverter;
import net.geoprism.registry.graph.AttributeBooleanType;
import net.geoprism.registry.graph.AttributeCharacterType;
import net.geoprism.registry.graph.AttributeDataSourceType;
import net.geoprism.registry.graph.AttributeType;
import net.geoprism.registry.graph.AttributeUUIDType;
import net.geoprism.registry.graph.BusinessEdgeType;
import net.geoprism.registry.graph.BusinessType;
import net.geoprism.registry.graph.ConceptClass;
import net.geoprism.registry.graph.ConceptEdgeType;
import net.geoprism.registry.graph.ConceptVertex;
import net.geoprism.registry.model.ServerOrganization;
import net.geoprism.registry.view.ConceptClassDTO;

@Service
public class ConceptClassBusinessService extends ObjectClassBusinessService<ConceptClass, ConceptClassDTO> implements ConceptClassBusinessServiceIF
{
  @Autowired
  private ApplicationEventPublisher publisher;

  public ConceptClassBusinessService()
  {
    super(ConceptClass.CLASS);
  }

  @Override
  public ConceptClass get(String oid)
  {
    return ConceptClass.get(oid);
  }

  @Override
  @Transaction
  public void delete(ConceptClass type)
  {
    MdVertex mdVertex = type.getMdVertex();

    type.delete();

    mdVertex.delete();

    this.getCache().remove(type);

    publisher.publishEvent(new ClearObjectCacheEvent(this));
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
      List<org.commongeoregistry.adapter.metadata.AttributeType> attributes = type.getAttributes().stream() //
          .filter(filter) //
          .sorted((a, b) -> {
            return a.getCode().compareTo(b.getCode());
          }).map(a -> a.toDTO()).toList();

      dto.setAttributes(attributes);
    }

    return dto;

  }

  @Transaction
  @Override
  public ConceptClass apply(ConceptClassDTO object)
  {
    String code = object.getCode();
    String organizationCode = object.getOrganization();
    ServerOrganization organization = ServerOrganization.getByCode(organizationCode);
    String origin = object.hasOrigin() ? object.getOrigin() : GeoprismProperties.getOrigin();

    ServiceFactory.getGeoObjectTypePermissionService().enforceCanCreate(organization.getCode(), false);

    this.validateName(code);

    if (code.length() > 64)
    {
      // Setting the typename on the MdBusiness creates this limitation.
      CodeLengthException ex = new CodeLengthException();
      ex.setLength(64);
      throw ex;
    }

    LocalizedValue localizedValue = object.getDisplayLabel();

    ConceptClass conceptClass = object.hasOid() ? ConceptClass.get(object.getOid()) : new ConceptClass();
    conceptClass.setCode(code);
    conceptClass.setOrganization(organization.getGraphOrganization());
    RegistryLocalizedValueConverter.populate(conceptClass, ConceptClass.DISPLAYLABEL, localizedValue);

    boolean isNew = conceptClass.isNew();

    if (isNew)
    {
      MdVertexDAOIF superMdVertex = MdVertexDAO.getMdVertexDAO(ConceptVertex.CLASS);

      MdVertexDAO mdVertex = MdVertexDAO.newInstance();
      mdVertex.setValue(MdGeoVertexInfo.PACKAGE, RegistryConstants.BUSINESS_PACKAGE);
      mdVertex.setValue(MdGeoVertexInfo.NAME, code);
      mdVertex.setValue(MdGeoVertexInfo.ENABLE_CHANGE_OVER_TIME, MdAttributeBooleanInfo.FALSE);
      mdVertex.setValue(MdGeoVertexInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
      mdVertex.setValue(MdGeoVertexInfo.SUPER_MD_VERTEX, superMdVertex.getOid());
      RegistryLocalizedValueConverter.populate(mdVertex, MdVertexInfo.DISPLAY_LABEL, localizedValue);
      mdVertex.apply();

      conceptClass.setMdVertexId(mdVertex.getOid());
      conceptClass.setOrigin(origin);
      conceptClass.setSequence(object.hasSequence() ? object.getSequence() : 0L);
    }
    else
    {
      if (conceptClass.getOrigin().equals(GeoprismProperties.getOrigin()))
      {
        conceptClass.setSequence(conceptClass.getSequence() + 1);
      }
      else if (object.hasSequence())
      {
        conceptClass.setSequence(object.getSequence());
      }
    }

    conceptClass.apply();

    if (isNew)
    {
      AttributeCharacterType codeAttr = new AttributeCharacterType();
      codeAttr.setCode(DefaultAttribute.CODE.getName());
      codeAttr.setEmbeddedValue(AttributeUUIDType.LABEL, LocalizedValue.DEFAULT_LOCALE, DefaultAttribute.CODE.getDefaultLocalizedName());
      codeAttr.setEmbeddedValue(AttributeUUIDType.DESCRIPTION, LocalizedValue.DEFAULT_LOCALE, DefaultAttribute.CODE.getDefaultDescription());
      codeAttr.setValue(AttributeBooleanType.OBJECTTYPE, conceptClass.getOid());
      codeAttr.setRequired(true);
      codeAttr.setUnique(true);
      codeAttr.setIsChangeOverTime(false);
      codeAttr.setIsDefault(true);
      codeAttr.setIsVirtual(true);
      codeAttr.apply();

      AttributeDataSourceType sourceAttr = new AttributeDataSourceType();
      sourceAttr.setCode(DefaultAttribute.DATA_SOURCE.getName());
      sourceAttr.setEmbeddedValue(AttributeUUIDType.LABEL, LocalizedValue.DEFAULT_LOCALE, DefaultAttribute.DATA_SOURCE.getDefaultLocalizedName());
      sourceAttr.setEmbeddedValue(AttributeUUIDType.DESCRIPTION, LocalizedValue.DEFAULT_LOCALE, DefaultAttribute.DATA_SOURCE.getDefaultDescription());
      sourceAttr.setValue(AttributeBooleanType.OBJECTTYPE, conceptClass.getOid());
      sourceAttr.setRequired(false);
      sourceAttr.setUnique(false);
      sourceAttr.setIsChangeOverTime(true);
      sourceAttr.setIsDefault(true);
      sourceAttr.apply();
    }

    this.getCache().put(conceptClass);

    return conceptClass;
  }

  @Transaction
  @Override
  public ConceptClass apply(ConceptClass conceptClass)
  {
    conceptClass.apply();

    this.getCache().put(conceptClass);

    return conceptClass;
  }

  @Override
  public List<ConceptEdgeType> getEdgeTypes(ConceptClass type)
  {
    MdVertexDAOIF mdVertex = MdVertexDAO.getMdVertexDAO(ConceptEdgeType.CLASS);

    StringBuilder statement = new StringBuilder();
    statement.append("SELECT FROM " + mdVertex.getDBClassName());
    statement.append(" WHERE childType = :type");
    statement.append(" OR parentType = :type");

    GraphQuery<ConceptEdgeType> query = new GraphQuery<ConceptEdgeType>(statement.toString());
    query.setParameter("type", type.getMdVertexOid());

    return query.getResults().stream() //
        .sorted((a, b) -> a.getLabel().getLocalizedValue().compareTo(b.getLabel().getLocalizedValue())) //
        .toList();
  }

}
