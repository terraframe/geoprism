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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.commongeoregistry.adapter.Term;
import org.commongeoregistry.adapter.constants.DefaultAttribute;
import org.commongeoregistry.adapter.metadata.AttributeClassificationType;
import org.commongeoregistry.adapter.metadata.GeoObjectType;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.runwaysdk.business.BusinessFacade;
import com.runwaysdk.business.graph.GraphQuery;
import com.runwaysdk.business.graph.VertexObject;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.session.Session;

import net.geoprism.graph.BusinessEdgeTypeSnapshot;
import net.geoprism.graph.BusinessEdgeTypeSnapshotQuery;
import net.geoprism.graph.BusinessTypeSnapshot;
import net.geoprism.graph.BusinessTypeSnapshotQuery;
import net.geoprism.graph.DirectedAcyclicGraphTypeSnapshot;
import net.geoprism.graph.DirectedAcyclicGraphTypeSnapshotQuery;
import net.geoprism.graph.GeoObjectTypeSnapshot;
import net.geoprism.graph.GeoObjectTypeSnapshotQuery;
import net.geoprism.graph.GraphTypeSnapshot;
import net.geoprism.graph.HierarchyTypeSnapshot;
import net.geoprism.graph.HierarchyTypeSnapshotQuery;
import net.geoprism.graph.LabeledPropertyGraphType;
import net.geoprism.graph.LabeledPropertyGraphTypeEntry;
import net.geoprism.graph.LabeledPropertyGraphTypeSnapshotQuery;
import net.geoprism.graph.LabeledPropertyGraphTypeVersion;
import net.geoprism.graph.LabeledPropertyGraphTypeVersionQuery;
import net.geoprism.graph.LabeledPropertyGraphUtil;
import net.geoprism.graph.UndirectedGraphTypeSnapshot;
import net.geoprism.graph.UndirectedGraphTypeSnapshotQuery;
import net.geoprism.registry.DateUtil;
import net.geoprism.registry.LPGTileCache;
import net.geoprism.registry.lpg.LPGPublishProgressMonitorIF;
import net.geoprism.registry.model.ClassificationType;

@Service
public class LabeledPropertyGraphTypeVersionBusinessService implements LabeledPropertyGraphTypeVersionBusinessServiceIF
{
  @Autowired
  private GeoObjectTypeSnapshotBusinessServiceIF    objectService;

  @Autowired
  private BusinessTypeSnapshotBusinessServiceIF     businessService;

  @Autowired
  private BusinessEdgeTypeSnapshotBusinessServiceIF bEdgeTypeService;

  // @Autowired
  // private HierarchyTypeSnapshotBusinessServiceIF hierarchyService;

  @Autowired
  private GraphTypeSnapshotBusinessServiceIF        graphService;

  @Autowired
  private ClassificationBusinessServiceIF           classificationService;

  @Autowired
  private ClassificationTypeBusinessServiceIF       typeService;

  @Override
  @Transaction
  public void delete(LabeledPropertyGraphTypeVersion version)
  {
    LPGTileCache.deleteTiles(version);

    this.getGraphSnapshots(version).forEach(e -> this.graphService.delete(e));

    // Delete all business edge types
    this.getBusinessEdgeTypes(version).stream().forEach(v -> this.bEdgeTypeService.delete(v));

    // Delete all business types
    this.getBusinessTypes(version).stream().forEach(v -> this.businessService.delete(v));

    // Delete the non-root snapshots first
    this.getTypes(version).stream().filter(v -> !v.getIsAbstract()).forEach(v -> this.objectService.delete(v));

    // Delete the abstract snapshots after all the sub snapshots have been
    // deleted
    this.getTypes(version).stream().filter(v -> !v.getIsRoot()).forEach(v -> this.objectService.delete(v));

    // Delete the root snapshots after all the sub snapshots have been deleted
    this.getTypes(version).stream().filter(v -> v.isRoot()).forEach(v -> this.objectService.delete(v));

    version.delete();
  }

  @Override
  public void remove(LabeledPropertyGraphTypeVersion version)
  {
    new LabeledPropertyGraphUtil(this).removeVersion(version);
  }

  @Override
  public VertexObject getVertex(LabeledPropertyGraphTypeVersion version, String uid, String typeCode)
  {
    GeoObjectTypeSnapshot type = this.objectService.get(version, typeCode);

    MdVertexDAOIF mdVertex = (MdVertexDAOIF) BusinessFacade.getEntityDAO(type.getGraphMdVertex());
    MdAttributeDAOIF mdAttribute = mdVertex.getAllDefinedMdAttributeMap().get(DefaultAttribute.UID.getName());

    StringBuilder statement = new StringBuilder();
    statement.append("SELECT FROM " + mdVertex.getDBClassName());
    statement.append(" WHERE " + mdAttribute.getColumnName() + " = :uid");

    GraphQuery<VertexObject> query = new GraphQuery<VertexObject>(statement.toString());
    query.setParameter("uid", uid);

    return query.getSingleResult();
  }

  @Override
  public VertexObject getBusinessVertex(LabeledPropertyGraphTypeVersion version, String code, String typeCode)
  {
    BusinessTypeSnapshot type = this.businessService.get(version, typeCode);

    MdVertexDAOIF mdVertex = (MdVertexDAOIF) BusinessFacade.getEntityDAO(type.getGraphMdVertex());
    MdAttributeDAOIF mdAttribute = mdVertex.getAllDefinedMdAttributeMap().get(DefaultAttribute.CODE.getName());

    StringBuilder statement = new StringBuilder();
    statement.append("SELECT FROM " + mdVertex.getDBClassName());
    statement.append(" WHERE " + mdAttribute.getColumnName() + " = :uid");

    GraphQuery<VertexObject> query = new GraphQuery<VertexObject>(statement.toString());
    query.setParameter("code", code);

    return query.getSingleResult();
  }

  @Override
  public GeoObjectTypeSnapshot getRootType(LabeledPropertyGraphTypeVersion version)
  {
    return this.objectService.getRoot(version);
  }

  @Override
  public List<GeoObjectTypeSnapshot> getTypes(LabeledPropertyGraphTypeVersion version)
  {
    QueryFactory factory = new QueryFactory();

    LabeledPropertyGraphTypeSnapshotQuery vQuery = new LabeledPropertyGraphTypeSnapshotQuery(factory);
    vQuery.WHERE(vQuery.getParent().EQ(version));

    GeoObjectTypeSnapshotQuery query = new GeoObjectTypeSnapshotQuery(factory);
    query.LEFT_JOIN_EQ(vQuery.getChild());

    try (OIterator<? extends GeoObjectTypeSnapshot> it = query.getIterator())
    {
      return it.getAll().stream().map(b -> (GeoObjectTypeSnapshot) b).collect(Collectors.toList());
    }
  }

  @Override
  public List<BusinessTypeSnapshot> getBusinessTypes(LabeledPropertyGraphTypeVersion version)
  {
    QueryFactory factory = new QueryFactory();

    LabeledPropertyGraphTypeSnapshotQuery vQuery = new LabeledPropertyGraphTypeSnapshotQuery(factory);
    vQuery.WHERE(vQuery.getParent().EQ(version));

    BusinessTypeSnapshotQuery query = new BusinessTypeSnapshotQuery(factory);
    query.LEFT_JOIN_EQ(vQuery.getChild());

    try (OIterator<? extends BusinessTypeSnapshot> it = query.getIterator())
    {
      return it.getAll().stream().map(b -> (BusinessTypeSnapshot) b).collect(Collectors.toList());
    }
  }

  @Override
  public List<BusinessEdgeTypeSnapshot> getBusinessEdgeTypes(LabeledPropertyGraphTypeVersion version)
  {
    QueryFactory factory = new QueryFactory();

    LabeledPropertyGraphTypeSnapshotQuery vQuery = new LabeledPropertyGraphTypeSnapshotQuery(factory);
    vQuery.WHERE(vQuery.getParent().EQ(version));

    BusinessEdgeTypeSnapshotQuery query = new BusinessEdgeTypeSnapshotQuery(factory);
    query.LEFT_JOIN_EQ(vQuery.getChild());

    try (OIterator<? extends BusinessEdgeTypeSnapshot> it = query.getIterator())
    {
      return it.getAll().stream().map(b -> (BusinessEdgeTypeSnapshot) b).collect(Collectors.toList());
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T extends GraphTypeSnapshot> List<T> getHiearchyTypes(LabeledPropertyGraphTypeVersion version)
  {
    QueryFactory factory = new QueryFactory();

    LabeledPropertyGraphTypeSnapshotQuery vQuery = new LabeledPropertyGraphTypeSnapshotQuery(factory);
    vQuery.WHERE(vQuery.getParent().EQ(version));

    HierarchyTypeSnapshotQuery query = new HierarchyTypeSnapshotQuery(factory);
    query.LEFT_JOIN_EQ(vQuery.getChild());

    try (OIterator<? extends HierarchyTypeSnapshot> it = query.getIterator())
    {
      return it.getAll().stream().map(b -> (T) b).collect(Collectors.toList());
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T extends GraphTypeSnapshot> List<T> getDirectedAcyclicGraphTypes(LabeledPropertyGraphTypeVersion version)
  {
    QueryFactory factory = new QueryFactory();
    
    LabeledPropertyGraphTypeSnapshotQuery vQuery = new LabeledPropertyGraphTypeSnapshotQuery(factory);
    vQuery.WHERE(vQuery.getParent().EQ(version));

    DirectedAcyclicGraphTypeSnapshotQuery query = new DirectedAcyclicGraphTypeSnapshotQuery(factory);
    query.LEFT_JOIN_EQ(vQuery.getChild());
    
    try (OIterator<? extends DirectedAcyclicGraphTypeSnapshot> it = query.getIterator())
    {
      return it.getAll().stream().map(b -> (T) b).collect(Collectors.toList());
    }
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public <T extends GraphTypeSnapshot> List<T> getUndirectedGraphTypes(LabeledPropertyGraphTypeVersion version)
  {
    QueryFactory factory = new QueryFactory();
    
    LabeledPropertyGraphTypeSnapshotQuery vQuery = new LabeledPropertyGraphTypeSnapshotQuery(factory);
    vQuery.WHERE(vQuery.getParent().EQ(version));

    UndirectedGraphTypeSnapshotQuery query = new UndirectedGraphTypeSnapshotQuery(factory);
    query.LEFT_JOIN_EQ(vQuery.getChild());
    
    try (OIterator<? extends UndirectedGraphTypeSnapshot> it = query.getIterator())
    {
      return it.getAll().stream().map(b -> (T) b).collect(Collectors.toList());
    }
  }
  
  @Override
  public List<GraphTypeSnapshot> getGraphSnapshots(LabeledPropertyGraphTypeVersion version)
  {
    String strategy = version.getGraphType().getStrategyType();

    if (strategy.equals(LabeledPropertyGraphType.TREE))
    {
      return this.getHiearchyTypes(version);
    }
    else
    {
      List<GraphTypeSnapshot> snapshots = new ArrayList<GraphTypeSnapshot>();

      snapshots.addAll(this.getHiearchyTypes(version));
      snapshots.addAll(this.getDirectedAcyclicGraphTypes(version));
      snapshots.addAll(this.getUndirectedGraphTypes(version));

      return snapshots;
    }
  }

  @Override
  public GeoObjectTypeSnapshot getSnapshot(LabeledPropertyGraphTypeVersion version, String typeCode)
  {
    QueryFactory factory = new QueryFactory();

    LabeledPropertyGraphTypeSnapshotQuery vQuery = new LabeledPropertyGraphTypeSnapshotQuery(factory);
    vQuery.WHERE(vQuery.getParent().EQ(version));

    GeoObjectTypeSnapshotQuery query = new GeoObjectTypeSnapshotQuery(factory);
    query.LEFT_JOIN_EQ(vQuery.getChild());
    query.AND(query.getCode().EQ(typeCode));

    try (OIterator<? extends GeoObjectTypeSnapshot> it = query.getIterator())
    {
      if (it.hasNext())
      {
        return it.next();
      }
    }

    throw new ProgrammingErrorException("Unable to find Geo-Object Type Snapshot definition for the type [" + typeCode + "]");
  }

  @Override
  public GraphTypeSnapshot getGraphTypeSnapshot(LabeledPropertyGraphTypeVersion version, String typeCode)
  {
    QueryFactory factory = new QueryFactory();

    LabeledPropertyGraphTypeSnapshotQuery vQuery = new LabeledPropertyGraphTypeSnapshotQuery(factory);
    vQuery.WHERE(vQuery.getParent().EQ(version));

    HierarchyTypeSnapshotQuery query = new HierarchyTypeSnapshotQuery(factory);
    query.LEFT_JOIN_EQ(vQuery.getChild());
    query.AND(query.getCode().EQ(typeCode));

    try (OIterator<? extends HierarchyTypeSnapshot> it = query.getIterator())
    {
      if (it.hasNext())
      {
        return it.next();
      }
    }

    throw new ProgrammingErrorException("Unable to find Hierarchy Type Snapshot definition for the type [" + typeCode + "]");
  }

  // // @Transaction
  // @Authenticate
  // public String publish(LabeledPropertyGraphTypeVersion version)
  // {
  // return this.publishNoAuth(version);
  // }
  //
  // // @Transaction
  // public String publishNoAuth(LabeledPropertyGraphTypeVersion version)
  // {
  // LabeledPropertyGraphServiceIF.getInstance().publish(version);
  //
  // return null;
  // }

  @Override
  public void truncate(LabeledPropertyGraphTypeVersion version)
  {
    // this.getTypes().forEach(type -> {
    // type.truncate();
    // });

    LPGTileCache.deleteTiles(version);

    this.getBusinessTypes(version).forEach(type -> this.businessService.truncate(type));

    this.objectService.truncate(this.getRootType(version));

  }

  @Override
  public VertexObject getObject(LabeledPropertyGraphTypeVersion version, String uid)
  {
    GeoObjectTypeSnapshot rootType = this.getRootType(version);

    GraphQuery<VertexObject> query = new GraphQuery<VertexObject>("SELECT FROM " + rootType.getGraphMdVertex().getDbClassName() + " WHERE uid = :uid");
    query.setParameter("uid", uid);

    return query.getSingleResult();
  }

  @Override
  public JsonObject toJSON(LabeledPropertyGraphTypeVersion version)
  {
    return this.toJSON(version, false);
  }

  @Override
  public JsonObject toJSON(LabeledPropertyGraphTypeVersion version, boolean includeTableDefinitions)
  {
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    format.setTimeZone(DateUtil.SYSTEM_TIMEZONE);

    LabeledPropertyGraphType lpgt = version.getGraphType();

    JsonObject object = new JsonObject();

    if (version.isAppliedToDB())
    {
      object.addProperty(LabeledPropertyGraphTypeVersion.OID, version.getOid());
    }

    object.addProperty(LabeledPropertyGraphType.DISPLAYLABEL, lpgt.getDisplayLabel().getValue());
    object.addProperty(LabeledPropertyGraphTypeVersion.GRAPHTYPE, lpgt.getOid());
    object.addProperty(LabeledPropertyGraphTypeVersion.ENTRY, version.getEntryOid());
    object.addProperty(LabeledPropertyGraphTypeVersion.FORDATE, format.format(version.getForDate()));
    object.addProperty(LabeledPropertyGraphTypeVersion.CREATEDATE, format.format(version.getCreateDate()));
    object.addProperty(LabeledPropertyGraphTypeVersion.VERSIONNUMBER, version.getVersionNumber());
    object.add(LabeledPropertyGraphTypeVersion.PERIOD, lpgt.formatVersionLabel(version));

    // Progress progress = ProgressService.get(version.getOid());
    //
    // if (progress != null)
    // {
    // object.add("refreshProgress", progress.toJson());
    // }

    if (version.getPublishDate() != null)
    {
      object.addProperty(LabeledPropertyGraphTypeVersion.PUBLISHDATE, format.format(version.getPublishDate()));
    }

    if (includeTableDefinitions)
    {
      JsonArray types = new JsonArray();

      List<GeoObjectTypeSnapshot> vertices = this.getTypes(version);
      vertices.stream().sorted((a, b) -> b.getIsAbstract().compareTo(a.getIsAbstract())).filter(type -> !type.isRoot()).forEach(type -> {
        types.add(type.toJSON());
      });

      object.add("types", types);

      JsonArray graphTypes = new JsonArray();

      GeoObjectTypeSnapshot root = this.getRootType(version);

      this.getGraphSnapshots(version).forEach(graphType -> {
        graphTypes.add(graphType.toJSON(root));
      });

      object.add("graphTypes", graphTypes);

      JsonArray businessTypes = new JsonArray();

      this.getBusinessTypes(version).forEach(type -> {
        businessTypes.add(type.toJSON());
      });

      object.add("businessTypes", businessTypes);

      JsonArray businessEdges = new JsonArray();

      this.getBusinessEdgeTypes(version).forEach(type -> {
        businessEdges.add(this.bEdgeTypeService.toJSON(type));
      });

      object.add("businessEdges", businessEdges);

      // Add classification definitions
      JsonArray classifications = new JsonArray();
      Set<String> processed = new TreeSet<String>();

      vertices.stream().sorted((a, b) -> b.getIsAbstract().compareTo(a.getIsAbstract())).filter(type -> !type.isRoot()).forEach(t -> {
        GeoObjectType type = t.toGeoObjectType();

        type.getAttributeMap().forEach((name, a) -> {
          if (a instanceof AttributeClassificationType)
          {
            AttributeClassificationType attribute = (AttributeClassificationType) a;
            Term rootTerm = attribute.getRootTerm();
            String classificationType = attribute.getClassificationType();

            if (!processed.contains(classificationType))
            {
              JsonObject typeObject = new JsonObject();
              typeObject.add("type", this.typeService.getByCode(classificationType).toJSON());
              typeObject.add("tree", this.classificationService.exportToJson(classificationType, rootTerm.getCode()));

              classifications.add(typeObject);

              processed.add(classificationType);
            }
          }
        });
      });

      object.add("classifications", classifications);

    }

    return object;
  }

  @Override
  @Transaction
  public LabeledPropertyGraphTypeVersion create(LabeledPropertyGraphTypeEntry listEntry, boolean working, int versionNumber)
  {
    LabeledPropertyGraphType listType = listEntry.getGraphType();

    LabeledPropertyGraphTypeVersion version = new LabeledPropertyGraphTypeVersion();
    version.setEntry(listEntry);
    version.setGraphType(listType);
    version.setForDate(listEntry.getForDate());
    version.setVersionNumber(versionNumber);
    version.apply();

    return version;
  }

  @Override
  @Transaction
  public LabeledPropertyGraphTypeVersion create(LabeledPropertyGraphTypeEntry entry, JsonObject json)
  {
    // Handle classification definitions
    JsonArray classifications = json.get("classifications").getAsJsonArray();

    for (JsonElement element : classifications)
    {
      JsonObject classificationObject = element.getAsJsonObject();
      JsonObject classificationType = classificationObject.get("type").getAsJsonObject();

      String code = classificationType.get(DefaultAttribute.CODE.getName()).getAsString();

      // If a type doesn't exist create it
      if (this.typeService.getByCode(code, false) == null)
      {
        classificationType.remove(LabeledPropertyGraphTypeVersion.OID);

        ClassificationType type = this.typeService.apply(classificationType);

        // Refresh permissions in case new definitions were defined during the
        // synchronization process
        Session session = (Session) Session.getCurrentSession();

        if (session != null)
        {
          session.reloadPermissions();
        }

        this.classificationService.importJsonTree(type, null, classificationObject.get("tree").getAsJsonObject());
      }

    }

    LabeledPropertyGraphType graphType = entry.getGraphType();

    LabeledPropertyGraphTypeVersion version = new LabeledPropertyGraphTypeVersion();
    version.setEntry(entry);
    version.setGraphType(graphType);
    version.setForDate(entry.getForDate());
    version.setVersionNumber(json.get(LabeledPropertyGraphTypeVersion.VERSIONNUMBER).getAsInt());
    version.apply();

    GeoObjectTypeSnapshot root = this.objectService.createRoot(version);

    JsonArray types = json.get("types").getAsJsonArray();

    for (JsonElement element : types)
    {
      this.objectService.create(version, element.getAsJsonObject());
    }

    JsonArray businessTypes = json.get("businessTypes").getAsJsonArray();

    for (JsonElement element : businessTypes)
    {
      this.businessService.create(version, element.getAsJsonObject());
    }

    JsonArray graphTypes = json.get("graphTypes").getAsJsonArray();

    for (JsonElement element : graphTypes)
    {
      this.graphService.create(version, element.getAsJsonObject(), root);
    }

    JsonArray businessEdges = json.get("businessEdges").getAsJsonArray();

    for (JsonElement element : businessEdges)
    {
      this.bEdgeTypeService.create(version, element.getAsJsonObject());
    }

    return version;
  }

  @Override
  public List<? extends LabeledPropertyGraphTypeVersion> getAll()
  {
    LabeledPropertyGraphTypeVersionQuery query = new LabeledPropertyGraphTypeVersionQuery(new QueryFactory());

    try (OIterator<? extends LabeledPropertyGraphTypeVersion> it = query.getIterator())
    {
      return it.getAll();
    }
  }

  @Override
  public LabeledPropertyGraphTypeVersion get(String oid)
  {
    return LabeledPropertyGraphTypeVersion.get(oid);
  }

  @Override
  public void publish(LPGPublishProgressMonitorIF monitor, LabeledPropertyGraphTypeVersion version)
  {
    new LabeledPropertyGraphUtil(this, monitor).publishVersion(version);
  }

  @Override
  public void publishNoAuth(LPGPublishProgressMonitorIF monitor, LabeledPropertyGraphTypeVersion version)
  {
    // Do nothing
  }

  @Override
  public void createPublishJob(LabeledPropertyGraphTypeVersion version)
  {
    // Do nothing
  }

  @Override
  public void createTiles(LabeledPropertyGraphTypeVersion version)
  {
    this.getTypes(version).forEach(snapshot -> {
      if (!snapshot.getIsAbstract())
      {
        // Ensure there is at least one geometry defined for the type

        for (int z = 0; z < 4; z++)
        {
          int tiles = (int) Math.pow(2, z);

          for (int x = 0; x < tiles; x++)
          {

            for (int y = 0; y < tiles; y++)
            {
              JSONObject config = new JSONObject();
              config.put("oid", version.getOid());
              config.put("typeCode", snapshot.getCode());
              config.put("x", x);
              config.put("y", y);
              config.put("z", z);

              LPGTileCache.getTile(config);
            }
          }
        }
      }
    });
  }

}
