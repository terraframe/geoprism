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

import net.geoprism.graph.DirectedAcyclicGraphTypeSnapshot;
import net.geoprism.graph.DirectedAcyclicGraphTypeSnapshotQuery;
import net.geoprism.graph.GeoObjectTypeSnapshot;
import net.geoprism.graph.GraphTypeSnapshot;
import net.geoprism.graph.HierarchyTypeSnapshot;
import net.geoprism.graph.LabeledPropertyGraphTypeSnapshotQuery;
import net.geoprism.graph.LabeledPropertyGraphTypeVersion;
import net.geoprism.graph.UndirectedGraphTypeSnapshot;
import net.geoprism.graph.UndirectedGraphTypeSnapshotQuery;
import net.geoprism.registry.RegistryConstants;
import net.geoprism.registry.conversion.LocalizedValueConverter;
import net.geoprism.registry.model.SnapshotContainer;

@Service
public class GraphTypeSnapshotBusinessService implements GraphTypeSnapshotBusinessServiceIF
{
  public static final String                     PREFIX = "g_";

  public static final String                     SPLIT  = "__";

  @Autowired
  private GeoObjectTypeSnapshotBusinessServiceIF service;

  @Autowired
  private HierarchyTypeSnapshotBusinessServiceIF hService;

  @Override
  @Transaction
  public void delete(GraphTypeSnapshot snapshot)
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
  public GraphTypeSnapshot create(SnapshotContainer<?> version, JsonObject type, GeoObjectTypeSnapshot root)
  {
    String code = type.get(HierarchyTypeSnapshot.CODE).getAsString();
    String typeCode = type.get(GraphTypeSnapshot.TYPE_CODE).getAsString();
    String origin = type.get(HierarchyTypeSnapshot.ORIGIN).getAsString();
    LocalizedValue label = LocalizedValue.fromJSON(type.get(HierarchyTypeSnapshot.DISPLAYLABEL).getAsJsonObject());
    LocalizedValue description = LocalizedValue.fromJSON(type.get(HierarchyTypeSnapshot.DESCRIPTION).getAsJsonObject());

    GraphTypeSnapshot snapshot;

    if (typeCode.equals(GraphTypeSnapshot.DIRECTED_ACYCLIC_GRAPH_TYPE))
    {
      MdEdge mdEdge = createMdEdge(version, root, code, label, description);
      
      DirectedAcyclicGraphTypeSnapshot htsnapshot = new DirectedAcyclicGraphTypeSnapshot();
      htsnapshot.setGraphMdEdge(mdEdge);
      htsnapshot.setOrigin(origin);
      htsnapshot.setCode(code);
      LocalizedValueConverter.populate(htsnapshot.getDisplayLabel(), label);
      LocalizedValueConverter.populate(htsnapshot.getDescription(), description);
      htsnapshot.apply();

      version.addSnapshot(htsnapshot).apply();

      snapshot = htsnapshot;
    }
    else if (typeCode.equals(GraphTypeSnapshot.UNDIRECTED_GRAPH_TYPE))
    {
      MdEdge mdEdge = createMdEdge(version, root, code, label, description);
      
      UndirectedGraphTypeSnapshot htsnapshot = new UndirectedGraphTypeSnapshot();
      htsnapshot.setGraphMdEdge(mdEdge);
      htsnapshot.setCode(code);
      htsnapshot.setOrigin(origin);
      LocalizedValueConverter.populate(htsnapshot.getDisplayLabel(), label);
      LocalizedValueConverter.populate(htsnapshot.getDescription(), description);
      htsnapshot.apply();

      version.addSnapshot(htsnapshot).apply();

      snapshot = htsnapshot;
    }
    else if (typeCode.equals(GraphTypeSnapshot.HIERARCHY_TYPE))
    {
      snapshot = this.hService.create(version, type, root);
    }
    else
    {
      throw new UnsupportedOperationException();
    }

    // Assign the relationship information
    createHierarchyRelationship(version, type, root);

    return snapshot;
  }

  protected MdEdge createMdEdge(SnapshotContainer<?> version, GeoObjectTypeSnapshot root, String code, LocalizedValue label, LocalizedValue description)
  {
    MdEdge mdEdge = null;

    if (version.createTablesWithSnapshot())
    {
      String viewName = getTableName(code);

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

  private void createHierarchyRelationship(SnapshotContainer<?> version, JsonObject type, GeoObjectTypeSnapshot parent)
  {
    type.get("nodes").getAsJsonArray().forEach(node -> {
      JsonObject object = node.getAsJsonObject();
      String code = object.get(HierarchyTypeSnapshot.CODE).getAsString();

      GeoObjectTypeSnapshot child = this.service.get(version, code);

      parent.addChildSnapshot(child).apply();

      createHierarchyRelationship(version, object, child);
    });
  }

  @Override
  public GraphTypeSnapshot get(SnapshotContainer<?> version, String typeCode, String code)
  {
    if (GraphTypeSnapshot.DIRECTED_ACYCLIC_GRAPH_TYPE.equals(typeCode))
    {
      QueryFactory factory = new QueryFactory();

      LabeledPropertyGraphTypeSnapshotQuery vQuery = new LabeledPropertyGraphTypeSnapshotQuery(factory);
      vQuery.WHERE(vQuery.getParent().EQ((LabeledPropertyGraphTypeVersion) version));

      DirectedAcyclicGraphTypeSnapshotQuery query = new DirectedAcyclicGraphTypeSnapshotQuery(factory);
      query.LEFT_JOIN_EQ(vQuery.getChild());
      query.AND(query.getCode().EQ(code));

      try (OIterator<? extends GraphTypeSnapshot> it = query.getIterator())
      {
        if (it.hasNext())
        {
          return it.next();
        }
      }
    }
    else if (GraphTypeSnapshot.HIERARCHY_TYPE.equals(typeCode))
    {
      return this.hService.get(version, code);
    }
    else if (GraphTypeSnapshot.UNDIRECTED_GRAPH_TYPE.equals(typeCode))
    {
      QueryFactory factory = new QueryFactory();

      LabeledPropertyGraphTypeSnapshotQuery vQuery = new LabeledPropertyGraphTypeSnapshotQuery(factory);
      vQuery.WHERE(vQuery.getParent().EQ((LabeledPropertyGraphTypeVersion) version));

      UndirectedGraphTypeSnapshotQuery query = new UndirectedGraphTypeSnapshotQuery(factory);
      query.LEFT_JOIN_EQ(vQuery.getChild());
      query.AND(query.getCode().EQ(code));

      try (OIterator<? extends GraphTypeSnapshot> it = query.getIterator())
      {
        if (it.hasNext())
        {
          return it.next();
        }
      }
    }

    return null;
  }

  protected void assignPermissions(ComponentIF component)
  {
  }
}
