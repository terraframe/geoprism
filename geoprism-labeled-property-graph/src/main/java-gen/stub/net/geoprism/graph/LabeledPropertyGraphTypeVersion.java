/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Geoprism Registry(tm).
 *
 * Geoprism Registry(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Geoprism Registry(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Geoprism Registry(tm). If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism.graph;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.commongeoregistry.adapter.Term;
import org.commongeoregistry.adapter.constants.DefaultAttribute;
import org.commongeoregistry.adapter.metadata.AttributeClassificationType;
import org.commongeoregistry.adapter.metadata.GeoObjectType;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.runwaysdk.business.BusinessFacade;
import com.runwaysdk.business.graph.GraphQuery;
import com.runwaysdk.business.graph.VertexObject;
import com.runwaysdk.business.rbac.Authenticate;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.session.Session;

import net.geoprism.graph.service.LabeledPropertyGraphServiceIF;
import net.geoprism.registry.DateUtil;
import net.geoprism.registry.RegistryConstants;
import net.geoprism.registry.model.Classification;
import net.geoprism.registry.model.ClassificationType;

public class LabeledPropertyGraphTypeVersion extends LabeledPropertyGraphTypeVersionBase implements LabeledVersion
{

  private static final long  serialVersionUID = -351397872;

  public static final String PREFIX           = "g_";

  public static final String SPLIT            = "__";

  public static final String TYPE_CODE        = "typeCode";

  public static final String ATTRIBUTES       = "attributes";

  public static final String HIERARCHIES      = "hierarchies";

  public static final String PERIOD           = "period";

  public LabeledPropertyGraphTypeVersion()
  {
    super();
  }

  @Override
  @Transaction
  public void delete()
  {
    // Delete all jobs
    // List<ExecutableJob> jobs = this.getJobs();
    //
    // for (ExecutableJob job : jobs)
    // {
    // job.delete();
    // }

    LabeledPropertyGraphServiceIF.getInstance().preDelete(this);

    this.getHierarchies().forEach(e -> HierarchyTypeSnapshot.get(e.getOid()).delete());

    // Delete the non-root snapshots first
    this.getTypes().stream().filter(v -> !v.getIsAbstract()).forEach(v -> GeoObjectTypeSnapshot.get(v.getOid()).delete());

    // Delete the abstract snapshots after all the sub snapshots have been
    // deleted
    this.getTypes().stream().filter(v -> !v.getIsRoot()).forEach(v -> GeoObjectTypeSnapshot.get(v.getOid()).delete());

    // Delete the root snapshots after all the sub snapshots have been deleted
    this.getTypes().stream().filter(v -> v.isRoot()).forEach(v -> GeoObjectTypeSnapshot.get(v.getOid()).delete());

    super.delete();

    LabeledPropertyGraphServiceIF.getInstance().postDelete(this);
  }

  @Override
  @Transaction
  @Authenticate
  public void remove()
  {
    this.delete();
  }

  public VertexObject getVertex(String uid, String typeCode)
  {
    GeoObjectTypeSnapshot type = GeoObjectTypeSnapshot.get(this, typeCode);

    MdVertexDAOIF mdVertex = (MdVertexDAOIF) BusinessFacade.getEntityDAO(type.getGraphMdVertex());
    MdAttributeDAOIF mdAttribute = mdVertex.getAllDefinedMdAttributeMap().get(RegistryConstants.UUID);

    StringBuilder statement = new StringBuilder();
    statement.append("SELECT FROM " + mdVertex.getDBClassName());
    statement.append(" WHERE " + mdAttribute.getColumnName() + " = :uid");

    GraphQuery<VertexObject> query = new GraphQuery<VertexObject>(statement.toString());
    query.setParameter("uid", uid);

    return query.getSingleResult();
  }

  public GeoObjectTypeSnapshot getRootType()
  {
    return GeoObjectTypeSnapshot.getRoot(this);
  }

  public List<GeoObjectTypeSnapshot> getTypes()
  {
    GeoObjectTypeSnapshotQuery query = new GeoObjectTypeSnapshotQuery(new QueryFactory());
    query.WHERE(query.getVersion().EQ(this));

    try (OIterator<? extends GeoObjectTypeSnapshot> it = query.getIterator())
    {
      return it.getAll().stream().map(b -> (GeoObjectTypeSnapshot) b).collect(Collectors.toList());
    }
  }

  public List<HierarchyTypeSnapshot> getHierarchies()
  {
    HierarchyTypeSnapshotQuery query = new HierarchyTypeSnapshotQuery(new QueryFactory());
    query.WHERE(query.getVersion().EQ(this));

    try (OIterator<? extends HierarchyTypeSnapshot> it = query.getIterator())
    {
      return it.getAll().stream().map(b -> (HierarchyTypeSnapshot) b).collect(Collectors.toList());
    }
  }

  public GeoObjectTypeSnapshot getSnapshot(String typeCode)
  {
    GeoObjectTypeSnapshotQuery query = new GeoObjectTypeSnapshotQuery(new QueryFactory());
    query.WHERE(query.getVersion().EQ(this));
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

  public HierarchyTypeSnapshot getHierarchySnapshot(String typeCode)
  {
    HierarchyTypeSnapshotQuery query = new HierarchyTypeSnapshotQuery(new QueryFactory());
    query.WHERE(query.getVersion().EQ(this));
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

  // public List<ExecutableJob> getJobs()
  // {
  // LinkedList<ExecutableJob> jobs = new LinkedList<ExecutableJob>();
  //
  // PublishLabeledPropertyGraphTypeVersionJobQuery pmlvj = new
  // PublishLabeledPropertyGraphTypeVersionJobQuery(new QueryFactory());
  // pmlvj.WHERE(pmlvj.getVersion().EQ(this));
  //
  // try (OIterator<? extends PublishLabeledPropertyGraphTypeVersionJob> it =
  // pmlvj.getIterator())
  // {
  // jobs.addAll(it.getAll());
  // }
  //
  // return jobs;
  // }

  // public JobHistory createPublishJob()
  // {
  // QueryFactory factory = new QueryFactory();
  //
  // PublishLabeledPropertyGraphTypeVersionJobQuery query = new
  // PublishLabeledPropertyGraphTypeVersionJobQuery(factory);
  // query.WHERE(query.getVersion().EQ(this));
  //
  // JobHistoryQuery q = new JobHistoryQuery(factory);
  // q.WHERE(q.getStatus().containsAny(AllJobStatus.NEW, AllJobStatus.QUEUED,
  // AllJobStatus.RUNNING));
  // q.AND(q.job(query));
  //
  // if (q.getCount() > 0)
  // {
  // throw new DuplicateJobException("This version has already been queued for
  // publishing");
  // }
  //
  // SingleActorDAOIF currentUser = Session.getCurrentSession().getUser();
  //
  // PublishLabeledPropertyGraphTypeVersionJob job = new
  // PublishLabeledPropertyGraphTypeVersionJob();
  // job.setRunAsUserId(currentUser.getOid());
  // job.setVersion(this);
  // job.setGraphType(this.getGraphType());
  // job.apply();
  //
  // NotificationFacade.queue(new
  // GlobalNotificationMessage(MessageType.PUBLISH_JOB_CHANGE, null));
  //
  // return job.start();
  // }

//  @Transaction
  @Authenticate
  public String publish()
  {
    return this.publishNoAuth();
  }

//  @Transaction
  public String publishNoAuth()
  {
    LabeledPropertyGraphServiceIF.getInstance().publish(this);

    return null;
  }

  public void truncate()
  {
//    this.getTypes().forEach(type -> {
//      type.truncate();
//    });
    
    this.getRootType().truncate();

  }

  public JsonObject toJSON()
  {
    return this.toJSON(false);
  }

  public JsonObject toJSON(boolean includeTableDefinitions)
  {
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    format.setTimeZone(DateUtil.SYSTEM_TIMEZONE);

    LabeledPropertyGraphType graphType = this.getGraphType();

    JsonObject object = new JsonObject();

    if (this.isAppliedToDB())
    {
      object.addProperty(LabeledPropertyGraphTypeVersion.OID, this.getOid());
    }

    object.addProperty(LabeledPropertyGraphType.DISPLAYLABEL, graphType.getDisplayLabel().getValue());
    object.addProperty(LabeledPropertyGraphTypeVersion.GRAPHTYPE, graphType.getOid());
    object.addProperty(LabeledPropertyGraphTypeVersion.ENTRY, this.getEntryOid());
    object.addProperty(LabeledPropertyGraphTypeVersion.FORDATE, format.format(this.getForDate()));
    object.addProperty(LabeledPropertyGraphTypeVersion.CREATEDATE, format.format(this.getCreateDate()));
    object.addProperty(LabeledPropertyGraphTypeVersion.VERSIONNUMBER, this.getVersionNumber());
    object.add(LabeledPropertyGraphTypeVersion.PERIOD, graphType.formatVersionLabel(this));

    // Progress progress = ProgressService.get(this.getOid());
    //
    // if (progress != null)
    // {
    // object.add("refreshProgress", progress.toJson());
    // }

    if (this.getPublishDate() != null)
    {
      object.addProperty(LabeledPropertyGraphTypeVersion.PUBLISHDATE, format.format(this.getPublishDate()));
    }

    if (includeTableDefinitions)
    {
      JsonArray types = new JsonArray();

      List<GeoObjectTypeSnapshot> vertices = this.getTypes();
      vertices.stream().sorted((a, b) -> b.getIsAbstract().compareTo(a.getIsAbstract())).filter(type -> !type.isRoot()).forEach(type -> {
        types.add(type.toJSON());
      });

      object.add("types", types);

      JsonArray hierarchies = new JsonArray();

      GeoObjectTypeSnapshot root = this.getRootType();

      this.getHierarchies().forEach(hierarchy -> {

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

  @Transaction
  public static LabeledPropertyGraphTypeVersion create(LabeledPropertyGraphTypeEntry listEntry, boolean working, int versionNumber)
  {
    LabeledPropertyGraphType listType = listEntry.getGraphType();

    LabeledPropertyGraphTypeVersion version = new LabeledPropertyGraphTypeVersion();
    version.setEntry(listEntry);
    version.setGraphType(listType);
    version.setForDate(listEntry.getForDate());
    version.setVersionNumber(versionNumber);
    version.apply();

    LabeledPropertyGraphServiceIF.getInstance().postCreate(version);

    return version;
  }

  @Transaction
  public static LabeledPropertyGraphTypeVersion create(LabeledPropertyGraphTypeEntry entry, JsonObject json)
  {
    // Handle classification definitions
    JsonArray classifications = json.get("classifications").getAsJsonArray();

    for (JsonElement element : classifications)
    {
      JsonObject classificationObject = element.getAsJsonObject();
      JsonObject classificationType = classificationObject.get("type").getAsJsonObject();
      
      String code = classificationType.get(DefaultAttribute.CODE.getName()).getAsString();
      
      // If a type doesn't exist create it      
      if(ClassificationType.getByCode(code) == null) {
        classificationType.remove(OID);
        
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
    version.setVersionNumber(json.get(VERSIONNUMBER).getAsInt());
    version.apply();

    GeoObjectTypeSnapshot root = GeoObjectTypeSnapshot.createRoot(version);

    JsonArray types = json.get("types").getAsJsonArray();

    for (JsonElement element : types)
    {
      GeoObjectTypeSnapshot.create(version, element.getAsJsonObject());
    }

    JsonArray hierarchies = json.get("hierarchies").getAsJsonArray();

    for (JsonElement element : hierarchies)
    {
      HierarchyTypeSnapshot.create(version, element.getAsJsonObject(), root);
    }
    
    LabeledPropertyGraphServiceIF.getInstance().postCreate(version);

    return version;
  }

  public static List<? extends LabeledPropertyGraphTypeVersion> getAll()
  {
    LabeledPropertyGraphTypeVersionQuery query = new LabeledPropertyGraphTypeVersionQuery(new QueryFactory());

    try (OIterator<? extends LabeledPropertyGraphTypeVersion> it = query.getIterator())
    {
      return it.getAll();
    }
  }

}
