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

import org.commongeoregistry.adapter.constants.DefaultAttribute;
import org.commongeoregistry.adapter.dataaccess.LocalizedValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;
import com.runwaysdk.business.BusinessFacade;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeConcreteInfo;
import com.runwaysdk.constants.MdAttributeGraphReferenceInfo;
import com.runwaysdk.constants.graph.MdEdgeInfo;
import com.runwaysdk.dataaccess.DuplicateDataException;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.metadata.MdAttributeGraphReferenceDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeUUIDDAO;
import com.runwaysdk.dataaccess.metadata.graph.MdEdgeDAO;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.system.metadata.MdEdge;

import net.geoprism.registry.BusinessEdgeType;
import net.geoprism.registry.BusinessEdgeTypeQuery;
import net.geoprism.registry.BusinessType;
import net.geoprism.registry.DataNotFoundException;
import net.geoprism.registry.DuplicateHierarchyTypeException;
import net.geoprism.registry.Organization;
import net.geoprism.registry.RegistryConstants;
import net.geoprism.registry.cache.TransactionLRUCache;
import net.geoprism.registry.conversion.RegistryLocalizedValueConverter;
import net.geoprism.registry.graph.DataSource;
import net.geoprism.registry.graph.GeoVertex;
import net.geoprism.registry.model.EdgeDirection;
import net.geoprism.registry.model.graph.EdgeVertexType;
import net.geoprism.registry.model.graph.GeoVertexEdgeType;
import net.geoprism.registry.view.BusinessEdgeTypeView;
import net.geoprism.registry.view.BusinessGeoEdgeTypeView;

@Service
public class BusinessEdgeTypeBusinessService implements BusinessEdgeTypeBusinessServiceIF
{
  @Autowired
  private BusinessTypeBusinessServiceIF                       typeService;

  @Autowired
  private HierarchyTypeBusinessServiceIF                      hierarchyService;

  private final TransactionLRUCache<String, BusinessEdgeType> cache;

  public BusinessEdgeTypeBusinessService()
  {
    this.cache = new TransactionLRUCache<String, BusinessEdgeType>("t-b-edge-cache", (v) -> {

      return new String[] { v.getCode(), v.getMdEdgeOid() };
    }, 20);
  }

  @Override
  public EdgeVertexType getParent(BusinessEdgeType edgeType)
  {
    return getEdgeVertexType((MdVertexDAOIF) BusinessFacade.getEntityDAO(edgeType.getParentType()));
  }

  private EdgeVertexType getEdgeVertexType(MdVertexDAOIF mdVertex)
  {
    MdVertexDAOIF mdBusGeoEntity = MdVertexDAO.getMdVertexDAO(GeoVertex.CLASS);

    if (mdVertex.equals(mdBusGeoEntity))
    {
      return new GeoVertexEdgeType(mdBusGeoEntity);
    }

    return this.typeService.getByMdVertex(mdVertex);
  }

  @Override
  public EdgeVertexType getChild(BusinessEdgeType edgeType)
  {
    return getEdgeVertexType((MdVertexDAOIF) BusinessFacade.getEntityDAO(edgeType.getChildType()));
  }

  @Override
  @Transaction
  public void update(BusinessEdgeType edgeType, JsonObject object)
  {
    LocalizedValue label = object.has(BusinessEdgeType.DISPLAYLABEL) ? //
        LocalizedValue.fromJSON(object.getAsJsonObject(BusinessEdgeType.DISPLAYLABEL)) : //
        null;

    LocalizedValue description = object.has(BusinessEdgeType.DESCRIPTION) ? //
        LocalizedValue.fromJSON(object.getAsJsonObject(BusinessEdgeType.DESCRIPTION)) : //
        null;

    this.update(edgeType, label, description);
  }

  @Override
  @Transaction
  public void update(BusinessEdgeType edgeType, LocalizedValue label, LocalizedValue description)
  {
    try
    {
      edgeType.appLock();

      if (label != null)
      {
        RegistryLocalizedValueConverter.populate(edgeType.getDisplayLabel(), label);
      }

      if (description != null)
      {
        RegistryLocalizedValueConverter.populate(edgeType.getDescription(), description);
      }

      edgeType.setSequence(edgeType.getSequence() + 1);
      edgeType.apply();
    }
    finally
    {
      edgeType.unlock();
    }

    this.cache.put(edgeType);
  }

  @Override
  @Transaction
  public void delete(BusinessEdgeType edgeType)
  {
    MdEdge mdEdge = edgeType.getMdEdge();

    edgeType.delete();

    mdEdge.delete();

    this.cache.remove(edgeType);
  }

  @Override
  public JsonObject toJSON(BusinessEdgeType edgeType)
  {
    JsonObject object = new JsonObject();
    object.addProperty(BusinessEdgeType.OID, edgeType.getOid());
    object.addProperty(BusinessEdgeType.TYPE, "BusinessEdgeType");
    object.addProperty(BusinessEdgeType.CODE, edgeType.getCode());
    object.addProperty(BusinessEdgeType.ORIGIN, edgeType.getOrigin());
    object.addProperty(BusinessEdgeType.SEQ, edgeType.getSeq());
    object.addProperty(BusinessEdgeType.ORGANIZATION, edgeType.getOrganization().getCode());
    object.addProperty(BusinessEdgeType.PARENTTYPE, edgeType.getParentType().getTypeName());
    object.addProperty(BusinessEdgeType.CHILDTYPE, edgeType.getChildType().getTypeName());
    object.add(BusinessEdgeType.JSON_LABEL, RegistryLocalizedValueConverter.convertNoAutoCoalesce(edgeType.getDisplayLabel()).toJSON());
    object.add(BusinessEdgeType.DESCRIPTION, RegistryLocalizedValueConverter.convertNoAutoCoalesce(edgeType.getDescription()).toJSON());

    return object;
  }

  @Override
  public List<BusinessEdgeType> getAll()
  {
    BusinessEdgeTypeQuery query = new BusinessEdgeTypeQuery(new QueryFactory());

    try (OIterator<? extends BusinessEdgeType> it = query.getIterator())
    {
      return new LinkedList<BusinessEdgeType>(it.getAll());
    }
  }

  @Override
  public Optional<BusinessEdgeType> getByCode(String code)
  {
    return this.cache.get(code, () -> {
      BusinessEdgeTypeQuery query = new BusinessEdgeTypeQuery(new QueryFactory());
      query.WHERE(query.getCode().EQ(code));

      try (OIterator<? extends BusinessEdgeType> it = query.getIterator())
      {
        if (it.hasNext())
        {
          return Optional.of(it.next());
        }
      }

      return Optional.empty();
    });

  }

  @Override
  public BusinessEdgeType getByCodeOrThrow(String code)
  {
    return this.getByCode(code).orElseThrow(() -> {
      throw new DataNotFoundException("Unable to find business edge type with code [" + code + "]");
    });
  }

  @Override
  public BusinessEdgeType getByMdEdge(MdEdge mdEdge)
  {
    return this.cache.get(mdEdge.getOid(), () -> {
      BusinessEdgeTypeQuery query = new BusinessEdgeTypeQuery(new QueryFactory());
      query.WHERE(query.getMdEdge().EQ(mdEdge));

      try (OIterator<? extends BusinessEdgeType> it = query.getIterator())
      {
        if (it.hasNext())
        {
          return Optional.of(it.next());
        }
      }

      return Optional.empty();
    }).orElse(null);
  }

  @Override
  public BusinessEdgeType create(JsonObject object)
  {
    return create(BusinessEdgeTypeView.fromJSON(object));
  }

  @Override
  @Transaction
  public BusinessEdgeType create(BusinessEdgeTypeView dto)
  {
    BusinessType parentType = this.typeService.getByCode(dto.getParentTypeCode());
    BusinessType childType = this.typeService.getByCode(dto.getChildTypeCode());
    Organization organization = Organization.getByCode(dto.getOrganizationCode());

    try
    {
      MdEdgeDAO mdEdgeDAO = MdEdgeDAO.newInstance();
      mdEdgeDAO.setValue(MdEdgeInfo.PACKAGE, RegistryConstants.DAG_PACKAGE);
      mdEdgeDAO.setValue(MdEdgeInfo.NAME, dto.getCode());
      mdEdgeDAO.setValue(MdEdgeInfo.PARENT_MD_VERTEX, parentType.getMdVertexOid());
      mdEdgeDAO.setValue(MdEdgeInfo.CHILD_MD_VERTEX, childType.getMdVertexOid());
      RegistryLocalizedValueConverter.populate(mdEdgeDAO, MdEdgeInfo.DISPLAY_LABEL, dto.getLabel());
      RegistryLocalizedValueConverter.populate(mdEdgeDAO, MdEdgeInfo.DESCRIPTION, dto.getDescription());
      mdEdgeDAO.setValue(MdEdgeInfo.ENABLE_CHANGE_OVER_TIME, MdAttributeBooleanInfo.FALSE);
      mdEdgeDAO.apply();

      MdAttributeUUIDDAO uidAttr = MdAttributeUUIDDAO.newInstance();
      uidAttr.setValue(MdAttributeConcreteInfo.NAME, DefaultAttribute.UID.getName());
      uidAttr.setStructValue(MdAttributeBooleanInfo.DISPLAY_LABEL, LocalizedValue.DEFAULT_LOCALE, DefaultAttribute.UID.getDefaultLocalizedName());
      uidAttr.setStructValue(MdAttributeBooleanInfo.DESCRIPTION, LocalizedValue.DEFAULT_LOCALE, DefaultAttribute.UID.getDefaultDescription());
      uidAttr.setValue(MdAttributeConcreteInfo.DEFINING_MD_CLASS, mdEdgeDAO.getOid());
      uidAttr.setValue(MdAttributeConcreteInfo.REQUIRED, true);
      uidAttr.apply();

      MdAttributeGraphReferenceDAO sourceAttr = MdAttributeGraphReferenceDAO.newInstance();
      sourceAttr.setValue(MdAttributeConcreteInfo.NAME, DefaultAttribute.DATA_SOURCE.getName());
      sourceAttr.setStructValue(MdAttributeBooleanInfo.DISPLAY_LABEL, LocalizedValue.DEFAULT_LOCALE, DefaultAttribute.DATA_SOURCE.getDefaultLocalizedName());
      sourceAttr.setStructValue(MdAttributeBooleanInfo.DESCRIPTION, LocalizedValue.DEFAULT_LOCALE, DefaultAttribute.DATA_SOURCE.getDefaultDescription());
      sourceAttr.setValue(MdAttributeConcreteInfo.DEFINING_MD_CLASS, mdEdgeDAO.getOid());
      sourceAttr.setValue(MdAttributeGraphReferenceInfo.REFERENCE_MD_VERTEX, MdVertexDAO.getMdVertexDAO(DataSource.CLASS).getOid());
      sourceAttr.setValue(MdAttributeConcreteInfo.REQUIRED, false);
      sourceAttr.apply();

      hierarchyService.grantWritePermissionsOnMdTermRel(mdEdgeDAO);

      BusinessEdgeType businessEdgeType = new BusinessEdgeType();
      businessEdgeType.setOrganization(organization);
      businessEdgeType.setCode(dto.getCode());
      businessEdgeType.setMdEdgeId(mdEdgeDAO.getOid());
      businessEdgeType.setParentTypeId(parentType.getMdVertexOid());
      businessEdgeType.setChildTypeId(childType.getMdVertexOid());
      businessEdgeType.setOrigin(dto.getOrigin());
      businessEdgeType.setSequence(dto.getSeq());
      RegistryLocalizedValueConverter.populate(businessEdgeType.getDisplayLabel(), dto.getLabel());
      RegistryLocalizedValueConverter.populate(businessEdgeType.getDescription(), dto.getDescription());
      businessEdgeType.apply();

      this.cache.put(businessEdgeType);

      return businessEdgeType;
    }
    catch (DuplicateDataException ex)
    {
      DuplicateHierarchyTypeException ex2 = new DuplicateHierarchyTypeException();
      ex2.setDuplicateValue(dto.getCode());
      throw ex2;
    }
  }

  @Override
  @Transaction
  public BusinessEdgeType createGeoEdge(BusinessGeoEdgeTypeView dto)
  {
    BusinessType buisnessType = this.typeService.getByCode(dto.getTypeCode());
    MdVertexDAO mdVertexDAO = MdVertexDAO.getMdVertexDAO(GeoVertex.CLASS).getBusinessDAO();
    EdgeDirection direction = dto.getDirection();
    String code = dto.getCode();
    LocalizedValue label = dto.getLabel();
    LocalizedValue description = dto.getDescription();
    Organization organization = Organization.getByCode(dto.getOrganizationCode());

    try
    {
      String parentOid = direction.equals(EdgeDirection.PARENT) ? mdVertexDAO.getOid() : buisnessType.getMdVertexOid();
      String childOid = direction.equals(EdgeDirection.PARENT) ? buisnessType.getMdVertexOid() : mdVertexDAO.getOid();

      MdEdgeDAO mdEdgeDAO = MdEdgeDAO.newInstance();
      mdEdgeDAO.setValue(MdEdgeInfo.PACKAGE, RegistryConstants.DAG_PACKAGE);
      mdEdgeDAO.setValue(MdEdgeInfo.NAME, code);
      mdEdgeDAO.setValue(MdEdgeInfo.PARENT_MD_VERTEX, parentOid);
      mdEdgeDAO.setValue(MdEdgeInfo.CHILD_MD_VERTEX, childOid);
      RegistryLocalizedValueConverter.populate(mdEdgeDAO, MdEdgeInfo.DISPLAY_LABEL, label);
      RegistryLocalizedValueConverter.populate(mdEdgeDAO, MdEdgeInfo.DESCRIPTION, description);
      mdEdgeDAO.setValue(MdEdgeInfo.ENABLE_CHANGE_OVER_TIME, MdAttributeBooleanInfo.FALSE);
      mdEdgeDAO.apply();

      MdAttributeUUIDDAO uidAttr = MdAttributeUUIDDAO.newInstance();
      uidAttr.setValue(MdAttributeConcreteInfo.NAME, DefaultAttribute.UID.getName());
      uidAttr.setStructValue(MdAttributeBooleanInfo.DISPLAY_LABEL, LocalizedValue.DEFAULT_LOCALE, DefaultAttribute.UID.getDefaultLocalizedName());
      uidAttr.setStructValue(MdAttributeBooleanInfo.DESCRIPTION, LocalizedValue.DEFAULT_LOCALE, DefaultAttribute.UID.getDefaultDescription());
      uidAttr.setValue(MdAttributeConcreteInfo.DEFINING_MD_CLASS, mdEdgeDAO.getOid());
      uidAttr.setValue(MdAttributeConcreteInfo.REQUIRED, true);
      uidAttr.apply();

      MdAttributeGraphReferenceDAO sourceAttr = MdAttributeGraphReferenceDAO.newInstance();
      sourceAttr.setValue(MdAttributeConcreteInfo.NAME, DefaultAttribute.DATA_SOURCE.getName());
      sourceAttr.setStructValue(MdAttributeBooleanInfo.DISPLAY_LABEL, LocalizedValue.DEFAULT_LOCALE, DefaultAttribute.DATA_SOURCE.getDefaultLocalizedName());
      sourceAttr.setStructValue(MdAttributeBooleanInfo.DESCRIPTION, LocalizedValue.DEFAULT_LOCALE, DefaultAttribute.DATA_SOURCE.getDefaultDescription());
      sourceAttr.setValue(MdAttributeConcreteInfo.DEFINING_MD_CLASS, mdEdgeDAO.getOid());
      sourceAttr.setValue(MdAttributeGraphReferenceInfo.REFERENCE_MD_VERTEX, MdVertexDAO.getMdVertexDAO(DataSource.CLASS).getOid());
      sourceAttr.setValue(MdAttributeConcreteInfo.REQUIRED, false);
      sourceAttr.apply();

      hierarchyService.grantWritePermissionsOnMdTermRel(mdEdgeDAO);

      BusinessEdgeType businessEdgeType = new BusinessEdgeType();
      businessEdgeType.setOrganization(organization);
      businessEdgeType.setCode(code);
      businessEdgeType.setMdEdgeId(mdEdgeDAO.getOid());
      businessEdgeType.setParentTypeId(parentOid);
      businessEdgeType.setChildTypeId(childOid);
      RegistryLocalizedValueConverter.populate(businessEdgeType.getDisplayLabel(), label);
      RegistryLocalizedValueConverter.populate(businessEdgeType.getDescription(), description);
      businessEdgeType.setOrigin(dto.getOrigin());
      businessEdgeType.setSequence(dto.getSeq());
      businessEdgeType.apply();

      return businessEdgeType;
    }
    catch (DuplicateDataException ex)
    {
      DuplicateHierarchyTypeException ex2 = new DuplicateHierarchyTypeException();
      ex2.setDuplicateValue(code);
      throw ex2;
    }
  }

}
