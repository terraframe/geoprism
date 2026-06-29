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

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.commongeoregistry.adapter.constants.DefaultAttribute;
import org.commongeoregistry.adapter.dataaccess.LocalizedValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.graph.MdVertexInfo;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.gis.constants.MdGeoVertexInfo;
import com.runwaysdk.system.metadata.MdVertex;

import net.geoprism.configuration.GeoprismProperties;
import net.geoprism.ontology.Classifier;
import net.geoprism.registry.CodeLengthException;
import net.geoprism.registry.RegistryConstants;
import net.geoprism.registry.cache.TransactionLRUCache;
import net.geoprism.registry.conversion.RegistryLocalizedValueConverter;
import net.geoprism.registry.conversion.TermConverter;
import net.geoprism.registry.graph.AttributeBooleanType;
import net.geoprism.registry.graph.AttributeCharacterType;
import net.geoprism.registry.graph.AttributeDataSourceType;
import net.geoprism.registry.graph.AttributeType;
import net.geoprism.registry.graph.AttributeUUIDType;
import net.geoprism.registry.graph.ConceptClass;
import net.geoprism.registry.graph.ConceptVertex;
import net.geoprism.registry.model.ServerOrganization;
import net.geoprism.registry.view.ConceptClassDTO;

@Service
public class ConceptClassBusinessService extends ObjectClassBusinessService<ConceptClass, ConceptClassDTO> implements ConceptClassBusinessServiceIF
{
  @Autowired
  private ClassificationTypeBusinessServiceIF             cTypeService;

  private final TransactionLRUCache<String, ConceptClass> cache;

  public ConceptClassBusinessService()
  {
    super(ConceptClass.CLASS);

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
  protected Optional<ConceptClass> get(String code, Supplier<Optional<ConceptClass>> supplier)
  {
    return this.cache.get(code, supplier);
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

    cTypeService.validateName(code);

    if (code.length() > 64)
    {
      // Setting the typename on the MdBusiness creates this limitation.
      CodeLengthException ex = new CodeLengthException();
      ex.setLength(64);
      throw ex;
    }

    LocalizedValue localizedValue = object.getDisplayLabel();

    ConceptClass businessType = object.hasOid() ? ConceptClass.get(object.getOid()) : new ConceptClass();
    businessType.setCode(code);
    businessType.setOrganization(organization.getGraphOrganization());
    RegistryLocalizedValueConverter.populate(businessType, ConceptClass.DISPLAYLABEL, localizedValue);

    boolean isNew = businessType.isNew();

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

      businessType.setMdVertexId(mdVertex.getOid());
      businessType.setOrigin(origin);
      businessType.setSequence(object.hasSequence() ? object.getSequence() : 0L);
    }
    else
    {
      if (businessType.getOrigin().equals(GeoprismProperties.getOrigin()))
      {
        businessType.setSequence(businessType.getSequence() + 1);
      }
      else if (object.hasSequence())
      {
        businessType.setSequence(object.getSequence());
      }
    }

    businessType.apply();

    if (isNew)
    {
      AttributeCharacterType codeAttr = new AttributeCharacterType();
      codeAttr.setCode(DefaultAttribute.CODE.getName());
      codeAttr.setEmbeddedValue(AttributeUUIDType.LABEL, LocalizedValue.DEFAULT_LOCALE, DefaultAttribute.CODE.getDefaultLocalizedName());
      codeAttr.setEmbeddedValue(AttributeUUIDType.DESCRIPTION, LocalizedValue.DEFAULT_LOCALE, DefaultAttribute.CODE.getDefaultDescription());
      codeAttr.setValue(AttributeBooleanType.OBJECTTYPE, businessType.getOid());
      codeAttr.setRequired(true);
      codeAttr.setUnique(true);
      codeAttr.setIsChangeOverTime(false);
      codeAttr.setIsDefault(true);
      codeAttr.apply();

      AttributeDataSourceType sourceAttr = new AttributeDataSourceType();
      sourceAttr.setCode(DefaultAttribute.DATA_SOURCE.getName());
      sourceAttr.setEmbeddedValue(AttributeUUIDType.LABEL, LocalizedValue.DEFAULT_LOCALE, DefaultAttribute.DATA_SOURCE.getDefaultLocalizedName());
      sourceAttr.setEmbeddedValue(AttributeUUIDType.DESCRIPTION, LocalizedValue.DEFAULT_LOCALE, DefaultAttribute.DATA_SOURCE.getDefaultDescription());
      sourceAttr.setValue(AttributeBooleanType.OBJECTTYPE, businessType.getOid());
      sourceAttr.setRequired(false);
      sourceAttr.setUnique(false);
      sourceAttr.setIsChangeOverTime(false);
      sourceAttr.setIsDefault(true);
      sourceAttr.apply();
    }

    this.cache.put(businessType);

    return businessType;
  }

  @Transaction
  @Override
  public ConceptClass apply(ConceptClass businessType)
  {
    businessType.apply();

    this.cache.put(businessType);

    return businessType;
  }

}
