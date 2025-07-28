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

import org.apache.commons.lang.StringUtils;
import org.commongeoregistry.adapter.constants.DefaultAttribute;
import org.commongeoregistry.adapter.dataaccess.LocalizedValue;
import org.commongeoregistry.adapter.metadata.HierarchyType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.runwaysdk.ComponentIF;
import com.runwaysdk.business.BusinessFacade;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeConcreteInfo;
import com.runwaysdk.constants.graph.MdEdgeInfo;
import com.runwaysdk.dataaccess.metadata.MdAttributeUUIDDAO;
import com.runwaysdk.dataaccess.metadata.graph.MdEdgeDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.system.metadata.MdEdge;
import com.runwaysdk.system.metadata.MdGraphClassQuery;

import net.geoprism.configuration.GeoprismProperties;
import net.geoprism.graph.GeoObjectTypeSnapshot;
import net.geoprism.graph.GraphTypeSnapshot;
import net.geoprism.graph.HierarchyTypeSnapshot;
import net.geoprism.graph.HierarchyTypeSnapshotBase;
import net.geoprism.graph.HierarchyTypeSnapshotQuery;
import net.geoprism.graph.LabeledPropertyGraphTypeSnapshotQuery;
import net.geoprism.graph.LabeledPropertyGraphTypeVersion;
import net.geoprism.graph.SnapshotHierarchy;
import net.geoprism.graph.SnapshotHierarchyQuery;
import net.geoprism.registry.RegistryConstants;
import net.geoprism.registry.conversion.LocalizedValueConverter;
import net.geoprism.registry.model.SnapshotContainer;

@Service
public class HierarchyTypeSnapshotBusinessService implements HierarchyTypeSnapshotBusinessServiceIF
{
  public static final String                     PREFIX = "g_";

  public static final String                     SPLIT  = "__";

  @Autowired
  private GeoObjectTypeSnapshotBusinessServiceIF service;

  @Override
  @Transaction
  public void delete(HierarchyTypeSnapshot snapshot)
  {
    String mdEdgeOid = snapshot.getGraphMdEdgeOid();

    snapshot.delete();

    if (!StringUtils.isBlank(mdEdgeOid))
    {
      MdEdgeDAO.get(mdEdgeOid).getBusinessDAO().delete();
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
  public HierarchyTypeSnapshot create(SnapshotContainer<?> version, JsonObject type, GeoObjectTypeSnapshot root)
  {
    String code = type.get(HierarchyTypeSnapshot.CODE).getAsString();
    String orgCode = type.get(HierarchyTypeSnapshot.ORGCODE).getAsString();
    String origin = type.has(HierarchyTypeSnapshot.ORIGIN) ? type.get(HierarchyTypeSnapshot.ORIGIN).getAsString() : GeoprismProperties.getOrigin();
    Long sequence = type.has(HierarchyTypeSnapshot.SEQUENCE) ? type.get(HierarchyTypeSnapshot.SEQUENCE).getAsLong() : 0;

    String viewName = getTableName(code);
    LocalizedValue label = LocalizedValue.fromJSON(type.get(HierarchyTypeSnapshot.DISPLAYLABEL).getAsJsonObject());
    LocalizedValue description = LocalizedValue.fromJSON(type.get(HierarchyTypeSnapshot.DESCRIPTION).getAsJsonObject());

    MdEdge mdEdge = createMdEdge(version, root, viewName, label, description);

    HierarchyTypeSnapshot snapshot = new HierarchyTypeSnapshot();
    snapshot.setGraphMdEdge(mdEdge);
    snapshot.setCode(code);
    snapshot.setOrgCode(orgCode);
    snapshot.setOrigin(origin);
    snapshot.setSequence(sequence);
    LocalizedValueConverter.populate(snapshot.getDisplayLabel(), label);
    LocalizedValueConverter.populate(snapshot.getDescription(), description);
    snapshot.apply();

    version.addSnapshot(snapshot).apply();

    // Assign the relationship information
    createHierarchyRelationship(version, snapshot, type, root);

    return snapshot;

  }

  protected MdEdge createMdEdge(SnapshotContainer<?> version, GeoObjectTypeSnapshot root, String viewName, LocalizedValue label, LocalizedValue description)
  {
    MdEdge mdEdge = null;

    if (version.createTablesWithSnapshot())
    {

      MdEdgeDAO mdEdgeDAO = MdEdgeDAO.newInstance();
      mdEdgeDAO.setValue(MdEdgeInfo.PACKAGE, RegistryConstants.UNIVERSAL_GRAPH_PACKAGE);
      mdEdgeDAO.setValue(MdEdgeInfo.NAME, viewName);
      mdEdgeDAO.setValue(MdEdgeInfo.DB_CLASS_NAME, viewName);
      mdEdgeDAO.setValue(MdEdgeInfo.PARENT_MD_VERTEX, root.getGraphMdVertexOid());
      mdEdgeDAO.setValue(MdEdgeInfo.CHILD_MD_VERTEX, root.getGraphMdVertexOid());
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

      mdEdge = (MdEdge) BusinessFacade.get(mdEdgeDAO);

      this.assignPermissions(mdEdge);
    }
    return mdEdge;
  }

  private void createHierarchyRelationship(SnapshotContainer<?> version, HierarchyTypeSnapshot hierarchy, JsonObject type, GeoObjectTypeSnapshot parent)
  {
    type.get("nodes").getAsJsonArray().forEach(node -> {
      JsonObject object = node.getAsJsonObject();
      String code = object.get(GeoObjectTypeSnapshot.CODE).getAsString();

      GeoObjectTypeSnapshot child = this.service.get(version, code);

      createHierarchyRelationship(hierarchy, parent, child);

      createHierarchyRelationship(version, hierarchy, object, child);
    });
  }

  @Override
  public void createHierarchyRelationship(HierarchyTypeSnapshot hierarchy, GeoObjectTypeSnapshot parent, GeoObjectTypeSnapshot child)
  {
    SnapshotHierarchy relationship = parent.addChildSnapshot(child);
    relationship.setHierarchyTypeCode(hierarchy.getCode());
    relationship.apply();
  }

  @Override
  public HierarchyTypeSnapshot get(SnapshotContainer<?> version, String code)
  {
    QueryFactory factory = new QueryFactory();

    LabeledPropertyGraphTypeSnapshotQuery vQuery = new LabeledPropertyGraphTypeSnapshotQuery(factory);
    vQuery.WHERE(vQuery.getParent().EQ((LabeledPropertyGraphTypeVersion) version));

    HierarchyTypeSnapshotQuery query = new HierarchyTypeSnapshotQuery(factory);
    query.LEFT_JOIN_EQ(vQuery.getChild());
    query.AND(query.getCode().EQ(code));

    try (OIterator<? extends HierarchyTypeSnapshot> it = query.getIterator())
    {
      if (it.hasNext())
      {
        return it.next();
      }
    }

    return null;
  }

  @Override
  public List<GeoObjectTypeSnapshot> getChildren(HierarchyTypeSnapshot hierarchy, GeoObjectTypeSnapshot parent)
  {
    QueryFactory factory = new QueryFactory();

    SnapshotHierarchyQuery vQuery = new SnapshotHierarchyQuery(factory);
    vQuery.WHERE(vQuery.getParent().EQ(parent));
    vQuery.AND(vQuery.getHierarchyTypeCode().EQ(hierarchy.getCode()));

    try (OIterator<? extends SnapshotHierarchy> iterator = vQuery.getIterator())
    {
      return iterator.getAll().stream().map(rel -> rel.getChild()).toList();
    }

    // GeoObjectTypeSnapshotQuery query = new
    // GeoObjectTypeSnapshotQuery(factory);
    // query.LEFT_JOIN_EQ(vQuery.getChild());
    //
    // try (OIterator<? extends GeoObjectTypeSnapshot> it = query.getIterator())
    // {
    // return new LinkedList<>(it.getAll());
    // }
  }

  public HierarchyType toHierarchyType(HierarchyTypeSnapshot snapshot)
  {
    String code = snapshot.getCode();
    LocalizedValue label = LocalizedValueConverter.convertNoAutoCoalesce(snapshot.getDisplayLabel());
    LocalizedValue description = LocalizedValueConverter.convertNoAutoCoalesce(snapshot.getDescription());

    HierarchyType type = new HierarchyType(code, label, description, snapshot.getOrgCode());
    type.setSequenceNumber(snapshot.getSequence());
    
    return type;
  }

  @Override
  public JsonObject toJSON(HierarchyTypeSnapshot hierarchy, GeoObjectTypeSnapshot root)
  {
    JsonArray nodes = new JsonArray();

    this.getChildren(hierarchy, root).forEach(child -> nodes.add(this.toNode(hierarchy, child)));

    JsonObject hierarchyObject = new JsonObject();
    hierarchyObject.addProperty(HierarchyTypeSnapshotBase.CODE, hierarchy.getCode());
    hierarchyObject.addProperty(HierarchyTypeSnapshotBase.ORGCODE, hierarchy.getOrgCode());
    hierarchyObject.addProperty(HierarchyTypeSnapshotBase.ORIGIN, hierarchy.getOrigin());
    hierarchyObject.addProperty(HierarchyTypeSnapshotBase.SEQUENCE, hierarchy.getSequence());
    hierarchyObject.addProperty(GraphTypeSnapshot.TYPE_CODE, GraphTypeSnapshot.HIERARCHY_TYPE);
    hierarchyObject.add(HierarchyTypeSnapshotBase.DISPLAYLABEL, LocalizedValueConverter.convertNoAutoCoalesce(hierarchy.getDisplayLabel()).toJSON());
    hierarchyObject.add(HierarchyTypeSnapshotBase.DESCRIPTION, LocalizedValueConverter.convertNoAutoCoalesce(hierarchy.getDescription()).toJSON());
    hierarchyObject.add("nodes", nodes);

    return hierarchyObject;
  }

  private JsonObject toNode(HierarchyTypeSnapshot hierarchy, GeoObjectTypeSnapshot snapshot)
  {
    JsonArray children = new JsonArray();

    this.getChildren(hierarchy, snapshot).forEach(child -> children.add(this.toNode(hierarchy, child)));

    JsonObject node = new JsonObject();
    node.addProperty(GeoObjectTypeSnapshot.CODE, snapshot.getCode());
    node.add("nodes", children);

    return node;
  }

  protected void assignPermissions(ComponentIF component)
  {
  }
}
