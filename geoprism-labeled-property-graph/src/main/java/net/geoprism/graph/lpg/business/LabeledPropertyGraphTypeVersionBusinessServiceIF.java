package net.geoprism.graph.lpg.business;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.google.gson.JsonObject;
import com.runwaysdk.business.graph.VertexObject;

import net.geoprism.graph.GeoObjectTypeSnapshot;
import net.geoprism.graph.HierarchyTypeSnapshot;
import net.geoprism.graph.LabeledPropertyGraphSynchronization;
import net.geoprism.graph.LabeledPropertyGraphTypeEntry;
import net.geoprism.graph.LabeledPropertyGraphTypeVersion;
import net.geoprism.spring.ApplicationContextHolder;

@Component
public interface LabeledPropertyGraphTypeVersionBusinessServiceIF
{
  // Hack for published jobs
  public static LabeledPropertyGraphTypeVersionBusinessServiceIF getInstance()
  {
    return ApplicationContextHolder.getContext().getBean(LabeledPropertyGraphTypeVersionBusinessServiceIF.class);
  }

  public void delete(LabeledPropertyGraphTypeVersion version);

  public void remove(LabeledPropertyGraphTypeVersion version);

  GeoObjectTypeSnapshot getRootType(LabeledPropertyGraphTypeVersion version);

  GeoObjectTypeSnapshot getSnapshot(LabeledPropertyGraphTypeVersion version, String typeCode);

  List<GeoObjectTypeSnapshot> getTypes(LabeledPropertyGraphTypeVersion version);

  List<HierarchyTypeSnapshot> getHierarchies(LabeledPropertyGraphTypeVersion version);

  HierarchyTypeSnapshot getHierarchySnapshot(LabeledPropertyGraphTypeVersion version, String typeCode);

  VertexObject getVertex(LabeledPropertyGraphTypeVersion version, String uid, String typeCode);

  void truncate(LabeledPropertyGraphTypeVersion version);

  VertexObject getObject(LabeledPropertyGraphTypeVersion version, String uid);

  JsonObject toJSON(LabeledPropertyGraphTypeVersion version);

  JsonObject toJSON(LabeledPropertyGraphTypeVersion version, boolean includeTableDefinitions);

  public LabeledPropertyGraphTypeVersion create(LabeledPropertyGraphTypeEntry entry, boolean working, int versionNumber);

  LabeledPropertyGraphTypeVersion create(LabeledPropertyGraphTypeEntry entry, JsonObject json);

  public LabeledPropertyGraphTypeVersion get(String oid);

  List<? extends LabeledPropertyGraphTypeVersion> getAll();

  public void publish(LabeledPropertyGraphTypeVersion version);

  public void publishNoAuth(LabeledPropertyGraphTypeVersion version);

  public void postSynchronization(LabeledPropertyGraphSynchronization synchronization, VertexObject node, Map<String, Object> cache);

  public void createPublishJob(LabeledPropertyGraphTypeVersion version);

}
