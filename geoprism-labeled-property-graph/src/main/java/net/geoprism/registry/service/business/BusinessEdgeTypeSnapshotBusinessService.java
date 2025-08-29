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

import org.apache.commons.lang.StringUtils;
import org.commongeoregistry.adapter.constants.DefaultAttribute;
import org.commongeoregistry.adapter.dataaccess.LocalizedValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;
import com.runwaysdk.ComponentIF;
import com.runwaysdk.business.BusinessFacade;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeConcreteInfo;
import com.runwaysdk.constants.graph.MdEdgeInfo;
import com.runwaysdk.dataaccess.metadata.MdAttributeUUIDDAO;
import com.runwaysdk.dataaccess.metadata.graph.MdEdgeDAO;
import com.runwaysdk.dataaccess.metadata.graph.MdGraphClassDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.system.metadata.MdEdge;
import com.runwaysdk.system.metadata.MdGraphClassQuery;

import net.geoprism.configuration.GeoprismProperties;
import net.geoprism.graph.BusinessEdgeTypeSnapshot;
import net.geoprism.graph.BusinessEdgeTypeSnapshotBase;
import net.geoprism.graph.BusinessEdgeTypeSnapshotQuery;
import net.geoprism.graph.BusinessTypeSnapshot;
import net.geoprism.graph.GeoObjectTypeSnapshot;
import net.geoprism.graph.HierarchyTypeSnapshot;
import net.geoprism.graph.LabeledPropertyGraphTypeSnapshotQuery;
import net.geoprism.graph.LabeledPropertyGraphTypeVersion;
import net.geoprism.graph.ObjectTypeSnapshot;
import net.geoprism.registry.RegistryConstants;
import net.geoprism.registry.conversion.LocalizedValueConverter;
import net.geoprism.registry.model.SnapshotContainer;

@Service
public class BusinessEdgeTypeSnapshotBusinessService implements BusinessEdgeTypeSnapshotBusinessServiceIF
{
  public static final String                     PREFIX = "g_";

  public static final String                     SPLIT  = "__";

  @Autowired
  private BusinessTypeSnapshotBusinessServiceIF  typeService;

  @Autowired
  private GeoObjectTypeSnapshotBusinessServiceIF gTypeService;

  @Override
  @Transaction
  public void delete(BusinessEdgeTypeSnapshot snapshot)
  {
    String mdEdgeOid = snapshot.getGraphMdEdgeOid();

    snapshot.delete();

    if (!StringUtils.isBlank(mdEdgeOid))
    {
      MdGraphClassDAO.get(mdEdgeOid).getBusinessDAO().delete();
    }
  }

  @Override
  public String getTableName(String className)
  {
    int count = 0;

    String name = PREFIX + count + SPLIT + className;

    if (name.length() > 25)
    {
      name = name.substring(0, 25);
    }

    while (isTableNameInUse(name))
    {
      count++;

      name = PREFIX + count + className;

      if (name.length() > 25)
      {
        name = name.substring(0, 25);
      }
    }

    return name;
  }

  private boolean isTableNameInUse(String name)
  {
    MdGraphClassQuery query = new MdGraphClassQuery(new QueryFactory());
    query.WHERE(query.getDbClassName().EQ(name));

    return query.getCount() > 0;
  }

  @Override
  public BusinessEdgeTypeSnapshot create(SnapshotContainer<?> version, JsonObject type)
  {
    boolean isParentGeoObject = type.get(BusinessEdgeTypeSnapshot.ISPARENTGEOOBJECT).getAsBoolean();
    boolean isChildGeoObject = type.get(BusinessEdgeTypeSnapshot.ISCHILDGEOOBJECT).getAsBoolean();

    ObjectTypeSnapshot parent = null;
    ObjectTypeSnapshot child = null;

    if (isParentGeoObject)
    {
      parent = this.gTypeService.getRoot(version);
    }
    else
    {
      String code = type.get(BusinessEdgeTypeSnapshot.PARENTTYPE).getAsString();

      parent = this.typeService.get(version, code);
    }

    if (isChildGeoObject)
    {
      child = this.gTypeService.getRoot(version);
    }
    else
    {
      String code = type.get(BusinessEdgeTypeSnapshot.CHILDTYPE).getAsString();

      child = this.typeService.get(version, code);
    }

    return create(version, type, parent, child);
  }

  @Override
  public BusinessEdgeTypeSnapshot create(SnapshotContainer<?> version, JsonObject type, BusinessTypeSnapshot parent, BusinessTypeSnapshot child)
  {
    return create(version, type, parent, child);
  }

  @Override
  public BusinessEdgeTypeSnapshot create(SnapshotContainer<?> version, JsonObject typeDTO, BusinessTypeSnapshot bType, GeoObjectTypeSnapshot gType, String direction)
  {
    ObjectTypeSnapshot parent = direction.equals("PARENT") ? gType : bType;
    ObjectTypeSnapshot child = direction.equals("PARENT") ? bType : gType;

    return create(version, typeDTO, parent, child);
  }

  private BusinessEdgeTypeSnapshot create(SnapshotContainer<?> version, JsonObject type, ObjectTypeSnapshot parent, ObjectTypeSnapshot child)
  {
    String code = type.get(BusinessEdgeTypeSnapshot.CODE).getAsString();
    String orgCode = type.get(HierarchyTypeSnapshot.ORGCODE).getAsString();
    String origin = type.has(BusinessEdgeTypeSnapshot.ORIGIN) ? type.get(BusinessEdgeTypeSnapshot.ORIGIN).getAsString() : GeoprismProperties.getOrigin();
    Long sequence = type.has(BusinessEdgeTypeSnapshot.SEQUENCE) ? type.get(BusinessEdgeTypeSnapshot.SEQUENCE).getAsLong() : 0;    
    String viewName = getTableName(code);
    LocalizedValue label = LocalizedValue.fromJSON(type.get(HierarchyTypeSnapshot.DISPLAYLABEL).getAsJsonObject());
    LocalizedValue description = LocalizedValue.fromJSON(type.get(HierarchyTypeSnapshot.DESCRIPTION).getAsJsonObject());

    MdEdge mdEdge = createMdEdge(version, parent, child, viewName, label, description);

    BusinessEdgeTypeSnapshot snapshot = new BusinessEdgeTypeSnapshot();
    snapshot.setGraphMdEdge(mdEdge);
    snapshot.setCode(code);
    snapshot.setOrigin(origin);
    snapshot.setSequence(sequence);
    snapshot.setOrgCode(orgCode);
    snapshot.setIsChildGeoObject(type.get(BusinessEdgeTypeSnapshot.ISCHILDGEOOBJECT).getAsBoolean());
    snapshot.setIsParentGeoObject(type.get(BusinessEdgeTypeSnapshot.ISPARENTGEOOBJECT).getAsBoolean());
    snapshot.setParentType(parent);
    snapshot.setChildType(child);
    LocalizedValueConverter.populate(snapshot.getDisplayLabel(), label);
    LocalizedValueConverter.populate(snapshot.getDescription(), description);
    snapshot.apply();

    version.addSnapshot(snapshot).apply();

    return snapshot;
  }

  protected MdEdge createMdEdge(SnapshotContainer<?> version, ObjectTypeSnapshot parent, ObjectTypeSnapshot child, String viewName, LocalizedValue label, LocalizedValue description)
  {
    if (version.createTablesWithSnapshot())
    {
      MdEdgeDAO mdEdgeDAO = MdEdgeDAO.newInstance();
      mdEdgeDAO.setValue(MdEdgeInfo.PACKAGE, RegistryConstants.UNIVERSAL_GRAPH_PACKAGE);
      mdEdgeDAO.setValue(MdEdgeInfo.NAME, viewName);
      mdEdgeDAO.setValue(MdEdgeInfo.DB_CLASS_NAME, viewName);
      mdEdgeDAO.setValue(MdEdgeInfo.PARENT_MD_VERTEX, parent.getGraphMdVertexOid());
      mdEdgeDAO.setValue(MdEdgeInfo.CHILD_MD_VERTEX, child.getGraphMdVertexOid());
      LocalizedValueConverter.populate(mdEdgeDAO, MdEdgeInfo.DISPLAY_LABEL, label);
      LocalizedValueConverter.populate(mdEdgeDAO, MdEdgeInfo.DESCRIPTION, description);
      mdEdgeDAO.setValue(MdEdgeInfo.ENABLE_CHANGE_OVER_TIME, MdAttributeBooleanInfo.FALSE);
      mdEdgeDAO.apply();

      MdAttributeUUIDDAO uidAttr = MdAttributeUUIDDAO.newInstance();
      uidAttr.setValue(MdAttributeConcreteInfo.NAME, DefaultAttribute.UID.getName());
      uidAttr.setStructValue(MdAttributeBooleanInfo.DISPLAY_LABEL, LocalizedValue.DEFAULT_LOCALE, DefaultAttribute.UID.getDefaultLocalizedName());
      uidAttr.setStructValue(MdAttributeBooleanInfo.DESCRIPTION, LocalizedValue.DEFAULT_LOCALE, DefaultAttribute.UID.getDefaultDescription());
      uidAttr.setValue(MdAttributeConcreteInfo.DEFINING_MD_CLASS, mdEdgeDAO.getOid());
      uidAttr.setValue(MdAttributeConcreteInfo.REQUIRED, true);
      uidAttr.apply();

      MdEdge mdEdge = (MdEdge) BusinessFacade.get(mdEdgeDAO);

      this.assignPermissions(mdEdge);

      return mdEdge;
    }

    return null;
  }

  @Override
  public BusinessEdgeTypeSnapshot get(SnapshotContainer<?> version, String code)
  {
    QueryFactory factory = new QueryFactory();

    LabeledPropertyGraphTypeSnapshotQuery vQuery = new LabeledPropertyGraphTypeSnapshotQuery(factory);
    vQuery.WHERE(vQuery.getParent().EQ((LabeledPropertyGraphTypeVersion) version));

    BusinessEdgeTypeSnapshotQuery query = new BusinessEdgeTypeSnapshotQuery(factory);
    query.WHERE(query.EQ(vQuery.getChild()));
    query.AND(query.getCode().EQ(code));

    try (OIterator<? extends BusinessEdgeTypeSnapshot> it = query.getIterator())
    {
      if (it.hasNext())
      {
        return it.next();
      }
    }

    return null;
  }

  protected void assignPermissions(ComponentIF component)
  {
  }

  @Override
  public JsonObject toJSON(BusinessEdgeTypeSnapshot snapshot)
  {
    JsonObject json = new JsonObject();
    json.addProperty(BusinessEdgeTypeSnapshot.CODE, snapshot.getCode());
    json.addProperty(BusinessEdgeTypeSnapshot.ORGCODE, snapshot.getOrgCode());
    json.addProperty(BusinessEdgeTypeSnapshot.ORIGIN, snapshot.getOrigin());
    json.addProperty(BusinessEdgeTypeSnapshot.SEQUENCE, snapshot.getSequence());
    json.addProperty(BusinessEdgeTypeSnapshot.ORGCODE, snapshot.getOrgCode());
    json.add(BusinessEdgeTypeSnapshot.DISPLAYLABEL, LocalizedValueConverter.convertNoAutoCoalesce(snapshot.getDisplayLabel()).toJSON());
    json.add(BusinessEdgeTypeSnapshot.DESCRIPTION, LocalizedValueConverter.convertNoAutoCoalesce(snapshot.getDescription()).toJSON());
    json.addProperty(BusinessEdgeTypeSnapshot.ISPARENTGEOOBJECT, snapshot.getIsParentGeoObject());
    json.addProperty(BusinessEdgeTypeSnapshot.ISCHILDGEOOBJECT, snapshot.getIsChildGeoObject());

    if (snapshot.getIsParentGeoObject())
    {
      json.addProperty(BusinessEdgeTypeSnapshotBase.PARENTTYPE, "GEOOBJECT");
    }
    else
    {
      ObjectTypeSnapshot parentType = snapshot.getParentType();

      json.addProperty(BusinessEdgeTypeSnapshotBase.PARENTTYPE, parentType.getCode());
    }

    if (snapshot.getIsChildGeoObject())
    {
      json.addProperty(BusinessEdgeTypeSnapshotBase.CHILDTYPE, "GEOOBJECT");
    }
    else
    {
      ObjectTypeSnapshot childType = snapshot.getChildType();

      json.addProperty(BusinessEdgeTypeSnapshotBase.CHILDTYPE, childType.getCode());
    }

    return json;
  }

  public LabeledPropertyGraphTypeVersion getVersion(BusinessEdgeTypeSnapshot snapshot)
  {
    try (OIterator<? extends LabeledPropertyGraphTypeVersion> iterator = snapshot.getAllVersion())
    {
      if (iterator.hasNext())
      {
        return iterator.next();
      }

    }
    return null;
  }
}
