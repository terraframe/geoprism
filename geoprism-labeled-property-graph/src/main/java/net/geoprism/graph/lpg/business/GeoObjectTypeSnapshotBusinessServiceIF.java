package net.geoprism.graph.lpg.business;

import org.commongeoregistry.adapter.constants.GeometryType;
import org.commongeoregistry.adapter.dataaccess.GeoObject;
import org.commongeoregistry.adapter.metadata.GeoObjectType;
import org.springframework.stereotype.Component;

import com.google.gson.JsonObject;
import com.runwaysdk.business.graph.VertexObject;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;

import net.geoprism.graph.GeoObjectTypeSnapshot;
import net.geoprism.graph.LabeledPropertyGraphTypeVersion;

@Component
public interface GeoObjectTypeSnapshotBusinessServiceIF
{

  void truncate(GeoObjectTypeSnapshot snapshot);

  void delete(GeoObjectTypeSnapshot snapshot);

  void createGeometryAttribute(GeometryType geometryType, MdVertexDAO mdTableDAO);

  String getTableName(String className);

  GeoObjectTypeSnapshot createRoot(LabeledPropertyGraphTypeVersion version);

  GeoObjectTypeSnapshot create(LabeledPropertyGraphTypeVersion version, JsonObject type);

  GeoObject toGeoObject(GeoObjectTypeSnapshot snapshot, VertexObject vertex);

  GeoObject toGeoObject(VertexObject vertex, GeoObjectType type);

  GeoObjectTypeSnapshot get(LabeledPropertyGraphTypeVersion version, MdVertexDAOIF mdVertex);

  GeoObjectTypeSnapshot get(LabeledPropertyGraphTypeVersion version, String code);

  GeoObjectTypeSnapshot getRoot(LabeledPropertyGraphTypeVersion version);

}
