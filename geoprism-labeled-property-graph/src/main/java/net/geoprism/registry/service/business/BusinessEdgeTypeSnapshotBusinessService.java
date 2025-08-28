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

import org.commongeoregistry.adapter.dataaccess.LocalizedValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;
import com.runwaysdk.ComponentIF;
import com.runwaysdk.business.BusinessFacade;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.graph.MdEdgeInfo;
import com.runwaysdk.dataaccess.metadata.graph.MdEdgeDAO;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.system.metadata.MdEdge;
import com.runwaysdk.system.metadata.MdGraphClassQuery;

import net.geoprism.graph.BusinessEdgeTypeSnapshot;
import net.geoprism.graph.BusinessEdgeTypeSnapshotBase;
import net.geoprism.graph.BusinessEdgeTypeSnapshotQuery;
import net.geoprism.graph.BusinessTypeSnapshot;
import net.geoprism.graph.GeoObjectTypeSnapshot;
import net.geoprism.graph.HierarchyTypeSnapshot;
import net.geoprism.graph.LabeledPropertyGraphTypeVersion;
import net.geoprism.registry.RegistryConstants;
import net.geoprism.registry.conversion.AttributeTypeConverter;
import net.geoprism.registry.conversion.LocalizedValueConverter;

@Service
public class BusinessEdgeTypeSnapshotBusinessService implements BusinessEdgeTypeSnapshotBusinessServiceIF
{
  public static final String                               PREFIX = "g_";

  public static final String                               SPLIT  = "__";

  @Autowired
  private BusinessTypeSnapshotBusinessServiceIF            typeService;

  @Autowired
  private LabeledPropertyGraphTypeVersionBusinessServiceIF versionService;

  @Override
  @Transaction
  public void delete(BusinessEdgeTypeSnapshot snapshot)
  {
    String mdEdgeOid = snapshot.getGraphMdEdgeOid();

    snapshot.delete();

    MdEdgeDAO.get(mdEdgeOid).getBusinessDAO().delete();
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
  public BusinessEdgeTypeSnapshot create(LabeledPropertyGraphTypeVersion version, JsonObject type)
  {
    boolean isParentGeoObject = type.get(BusinessEdgeTypeSnapshot.ISPARENTGEOOBJECT).getAsBoolean();
    boolean isChildGeoObject = type.get(BusinessEdgeTypeSnapshot.ISCHILDGEOOBJECT).getAsBoolean();

    String parentOid = null;
    String childOid = null;

    if (isParentGeoObject)
    {
      parentOid = this.versionService.getRootType(version).getGraphMdVertexOid();
    }
    else
    {
      String code = type.get(BusinessEdgeTypeSnapshot.PARENTTYPE).getAsString();

      BusinessTypeSnapshot parent = this.typeService.get(version, code);

      parentOid = parent.getGraphMdVertexOid();
    }
    
    if (isChildGeoObject)
    {
      childOid = this.versionService.getRootType(version).getGraphMdVertexOid();
    }
    else
    {
      String code = type.get(BusinessEdgeTypeSnapshot.CHILDTYPE).getAsString();
      
      BusinessTypeSnapshot child = this.typeService.get(version, code);
      
      childOid = child.getGraphMdVertexOid();
    }


    return create(version, type, parentOid, childOid);
  }

  @Override
  public BusinessEdgeTypeSnapshot create(LabeledPropertyGraphTypeVersion version, JsonObject type, BusinessTypeSnapshot parent, BusinessTypeSnapshot child)
  {
    String parentOid = parent.getGraphMdVertexOid();
    String childOid = child.getGraphMdVertexOid();

    return create(version, type, parentOid, childOid);
  }

  @Override
  public BusinessEdgeTypeSnapshot create(LabeledPropertyGraphTypeVersion version, JsonObject typeDTO, BusinessTypeSnapshot bType, GeoObjectTypeSnapshot gType, String direction)
  {
    String parentOid = direction.equals("PARENT") ? gType.getGraphMdVertexOid() : bType.getGraphMdVertexOid();
    String childOid = direction.equals("PARENT") ? bType.getGraphMdVertexOid() : gType.getGraphMdVertexOid();

    return create(version, typeDTO, parentOid, childOid);
  }

  private BusinessEdgeTypeSnapshot create(LabeledPropertyGraphTypeVersion version, JsonObject typeDTO, String parentOid, String childOid)
  {
    String code = typeDTO.get(HierarchyTypeSnapshot.CODE).getAsString();
    String viewName = getTableName(code);
    LocalizedValue label = LocalizedValue.fromJSON(typeDTO.get(HierarchyTypeSnapshot.DISPLAYLABEL).getAsJsonObject());
    LocalizedValue description = LocalizedValue.fromJSON(typeDTO.get(HierarchyTypeSnapshot.DESCRIPTION).getAsJsonObject());

    MdEdgeDAO mdEdgeDAO = MdEdgeDAO.newInstance();
    mdEdgeDAO.setValue(MdEdgeInfo.PACKAGE, RegistryConstants.UNIVERSAL_GRAPH_PACKAGE);
    mdEdgeDAO.setValue(MdEdgeInfo.NAME, viewName);
    mdEdgeDAO.setValue(MdEdgeInfo.DB_CLASS_NAME, viewName);
    mdEdgeDAO.setValue(MdEdgeInfo.PARENT_MD_VERTEX, parentOid);
    mdEdgeDAO.setValue(MdEdgeInfo.CHILD_MD_VERTEX, childOid);
    LocalizedValueConverter.populate(mdEdgeDAO, MdEdgeInfo.DISPLAY_LABEL, label);
    LocalizedValueConverter.populate(mdEdgeDAO, MdEdgeInfo.DESCRIPTION, description);
    mdEdgeDAO.setValue(MdEdgeInfo.ENABLE_CHANGE_OVER_TIME, MdAttributeBooleanInfo.FALSE);
    mdEdgeDAO.apply();

    MdEdge mdEdge = (MdEdge) BusinessFacade.get(mdEdgeDAO);

    this.assignPermissions(mdEdge);

    BusinessEdgeTypeSnapshot snapshot = new BusinessEdgeTypeSnapshot();
    snapshot.setVersion(version);
    snapshot.setGraphMdEdge(mdEdge);
    snapshot.setCode(code);
    snapshot.setIsChildGeoObject(typeDTO.get(BusinessEdgeTypeSnapshot.ISCHILDGEOOBJECT).getAsBoolean());
    snapshot.setIsParentGeoObject(typeDTO.get(BusinessEdgeTypeSnapshot.ISPARENTGEOOBJECT).getAsBoolean());
    snapshot.setParentTypeId(parentOid);
    snapshot.setChildTypeId(childOid);
    LocalizedValueConverter.populate(snapshot.getDisplayLabel(), label);
    LocalizedValueConverter.populate(snapshot.getDescription(), description);
    snapshot.apply();

    return snapshot;
  }

  @Override
  public BusinessEdgeTypeSnapshot get(LabeledPropertyGraphTypeVersion version, String code)
  {
    BusinessEdgeTypeSnapshotQuery query = new BusinessEdgeTypeSnapshotQuery(new QueryFactory());
    query.WHERE(query.getVersion().EQ(version));
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
    LabeledPropertyGraphTypeVersion version = snapshot.getVersion();

    JsonObject json = new JsonObject();
    json.addProperty(BusinessEdgeTypeSnapshotBase.CODE, snapshot.getCode());
    json.add(BusinessEdgeTypeSnapshotBase.DISPLAYLABEL, AttributeTypeConverter.convertNoAutoCoalesce(snapshot.getDisplayLabel()).toJSON());
    json.add(BusinessEdgeTypeSnapshotBase.DESCRIPTION, AttributeTypeConverter.convertNoAutoCoalesce(snapshot.getDescription()).toJSON());
    json.addProperty(BusinessEdgeTypeSnapshotBase.ISPARENTGEOOBJECT, snapshot.getIsParentGeoObject());
    json.addProperty(BusinessEdgeTypeSnapshotBase.ISCHILDGEOOBJECT, snapshot.getIsChildGeoObject());

    if (snapshot.getIsParentGeoObject())
    {
      json.addProperty(BusinessEdgeTypeSnapshotBase.PARENTTYPE, "GEOOBJECT");
    }
    else
    {
      BusinessTypeSnapshot parentType = this.typeService.get(version, MdVertexDAO.get(snapshot.getParentTypeOid()));

      json.addProperty(BusinessEdgeTypeSnapshotBase.PARENTTYPE, parentType.getCode());
    }

    if (snapshot.getIsChildGeoObject())
    {
      json.addProperty(BusinessEdgeTypeSnapshotBase.CHILDTYPE, "GEOOBJECT");
    }
    else
    {
      BusinessTypeSnapshot childType = this.typeService.get(version, MdVertexDAO.get(snapshot.getChildTypeOid()));

      json.addProperty(BusinessEdgeTypeSnapshotBase.CHILDTYPE, childType.getCode());
    }

    return json;
  }
}
