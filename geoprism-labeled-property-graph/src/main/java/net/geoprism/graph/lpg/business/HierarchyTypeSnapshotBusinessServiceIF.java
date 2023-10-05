package net.geoprism.graph.lpg.business;

import org.springframework.stereotype.Component;

import com.google.gson.JsonObject;

import net.geoprism.graph.GeoObjectTypeSnapshot;
import net.geoprism.graph.HierarchyTypeSnapshot;
import net.geoprism.graph.LabeledPropertyGraphTypeVersion;

@Component
public interface HierarchyTypeSnapshotBusinessServiceIF
{

  void delete(HierarchyTypeSnapshot snapshot);

  String getTableName(String className);

  HierarchyTypeSnapshot create(LabeledPropertyGraphTypeVersion version, JsonObject type, GeoObjectTypeSnapshot root);

  HierarchyTypeSnapshot get(LabeledPropertyGraphTypeVersion version, String code);

}
