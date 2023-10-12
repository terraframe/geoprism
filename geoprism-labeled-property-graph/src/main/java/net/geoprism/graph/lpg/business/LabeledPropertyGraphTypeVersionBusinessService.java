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
package net.geoprism.graph.lpg.business;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.commongeoregistry.adapter.Term;
import org.commongeoregistry.adapter.constants.DefaultAttribute;
import org.commongeoregistry.adapter.metadata.AttributeClassificationType;
import org.commongeoregistry.adapter.metadata.GeoObjectType;
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

import net.geoprism.graph.GeoObjectTypeSnapshot;
import net.geoprism.graph.GeoObjectTypeSnapshotQuery;
import net.geoprism.graph.HierarchyTypeSnapshot;
import net.geoprism.graph.HierarchyTypeSnapshotQuery;
import net.geoprism.graph.LabeledPropertyGraphSynchronization;
import net.geoprism.graph.LabeledPropertyGraphType;
import net.geoprism.graph.LabeledPropertyGraphTypeEntry;
import net.geoprism.graph.LabeledPropertyGraphTypeVersion;
import net.geoprism.graph.LabeledPropertyGraphTypeVersionQuery;
import net.geoprism.graph.LabeledPropertyGraphUtil;
import net.geoprism.registry.DateUtil;
import net.geoprism.registry.RegistryConstants;
import net.geoprism.registry.model.Classification;
import net.geoprism.registry.model.ClassificationType;

@Service
public class LabeledPropertyGraphTypeVersionBusinessService implements LabeledPropertyGraphTypeVersionBusinessServiceIF
{
  @Autowired
  private GeoObjectTypeSnapshotBusinessServiceIF objectService;

  @Autowired
  private HierarchyTypeSnapshotBusinessServiceIF hierarchyService;

  @Override
  @Transaction
  public void delete(LabeledPropertyGraphTypeVersion version)
  {
    this.getHierarchies(version).forEach(e -> this.hierarchyService.delete(e));

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
    MdAttributeDAOIF mdAttribute = mdVertex.getAllDefinedMdAttributeMap().get(RegistryConstants.UUID);

    StringBuilder statement = new StringBuilder();
    statement.append("SELECT FROM " + mdVertex.getDBClassName());
    statement.append(" WHERE " + mdAttribute.getColumnName() + " = :uid");

    GraphQuery<VertexObject> query = new GraphQuery<VertexObject>(statement.toString());
    query.setParameter("uid", uid);

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
    GeoObjectTypeSnapshotQuery query = new GeoObjectTypeSnapshotQuery(new QueryFactory());
    query.WHERE(query.getVersion().EQ(version));

    try (OIterator<? extends GeoObjectTypeSnapshot> it = query.getIterator())
    {
      return it.getAll().stream().map(b -> (GeoObjectTypeSnapshot) b).collect(Collectors.toList());
    }
  }

  @Override
  public List<HierarchyTypeSnapshot> getHierarchies(LabeledPropertyGraphTypeVersion version)
  {
    HierarchyTypeSnapshotQuery query = new HierarchyTypeSnapshotQuery(new QueryFactory());
    query.WHERE(query.getVersion().EQ(version));

    try (OIterator<? extends HierarchyTypeSnapshot> it = query.getIterator())
    {
      return it.getAll().stream().map(b -> (HierarchyTypeSnapshot) b).collect(Collectors.toList());
    }
  }

  @Override
  public GeoObjectTypeSnapshot getSnapshot(LabeledPropertyGraphTypeVersion version, String typeCode)
  {
    GeoObjectTypeSnapshotQuery query = new GeoObjectTypeSnapshotQuery(new QueryFactory());
    query.WHERE(query.getVersion().EQ(version));
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
  public HierarchyTypeSnapshot getHierarchySnapshot(LabeledPropertyGraphTypeVersion version, String typeCode)
  {
    HierarchyTypeSnapshotQuery query = new HierarchyTypeSnapshotQuery(new QueryFactory());
    query.WHERE(query.getVersion().EQ(version));
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

    this.objectService.truncate(this.getRootType(version));

  }

  @Override
  public VertexObject getObject(LabeledPropertyGraphTypeVersion version, String uid)
  {
    GeoObjectTypeSnapshot rootType = this.getRootType(version);

    GraphQuery<VertexObject> query = new GraphQuery<VertexObject>("SELECT FROM " + rootType.getGraphMdVertex().getDbClassName() + " WHERE uuid = :uuid");
    query.setParameter("uuid", uid);

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

    LabeledPropertyGraphType graphType = version.getGraphType();

    JsonObject object = new JsonObject();

    if (version.isAppliedToDB())
    {
      object.addProperty(LabeledPropertyGraphTypeVersion.OID, version.getOid());
    }

    object.addProperty(LabeledPropertyGraphType.DISPLAYLABEL, graphType.getDisplayLabel().getValue());
    object.addProperty(LabeledPropertyGraphTypeVersion.GRAPHTYPE, graphType.getOid());
    object.addProperty(LabeledPropertyGraphTypeVersion.ENTRY, version.getEntryOid());
    object.addProperty(LabeledPropertyGraphTypeVersion.FORDATE, format.format(version.getForDate()));
    object.addProperty(LabeledPropertyGraphTypeVersion.CREATEDATE, format.format(version.getCreateDate()));
    object.addProperty(LabeledPropertyGraphTypeVersion.VERSIONNUMBER, version.getVersionNumber());
    object.add(LabeledPropertyGraphTypeVersion.PERIOD, graphType.formatVersionLabel(version));

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

      JsonArray hierarchies = new JsonArray();

      GeoObjectTypeSnapshot root = this.getRootType(version);

      this.getHierarchies(version).forEach(hierarchy -> {

        hierarchies.add(hierarchy.toJSON(root));
      });

      object.add("hierarchies", hierarchies);

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
              typeObject.add("type", ClassificationType.getByCode(classificationType).toJSON());
              typeObject.add("tree", Classification.exportToJson(classificationType, rootTerm.getCode()));

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
      if (ClassificationType.getByCode(code, false) == null)
      {
        classificationType.remove(LabeledPropertyGraphTypeVersion.OID);

        ClassificationType type = ClassificationType.apply(classificationType);

        // Refresh permissions in case new definitions were defined during the
        // synchronization process
        Session session = (Session) Session.getCurrentSession();

        if (session != null)
        {
          session.reloadPermissions();
        }

        Classification.importJsonTree(type, null, classificationObject.get("tree").getAsJsonObject());
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

    JsonArray hierarchies = json.get("hierarchies").getAsJsonArray();

    for (JsonElement element : hierarchies)
    {
      this.hierarchyService.create(version, element.getAsJsonObject(), root);
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
  public void publish(LabeledPropertyGraphTypeVersion version)
  {
    new LabeledPropertyGraphUtil(this).publishVersion(version);
  }
  
  @Override
  public void publishNoAuth(LabeledPropertyGraphTypeVersion version)
  {
    // Do nothing
  }

  @Override
  public void postSynchronization(LabeledPropertyGraphSynchronization synchronization, VertexObject node, Map<String, Object> cache)
  {
    // Do nothing
  }

  @Override
  public void createPublishJob(LabeledPropertyGraphTypeVersion version)
  {
    // Do nothing
  }

}
