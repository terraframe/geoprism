package net.geoprism.registry.business;

import java.util.List;

import org.springframework.stereotype.Component;

import com.google.gson.JsonObject;

import net.geoprism.registry.BusinessEdgeType;
import net.geoprism.registry.BusinessType;
import net.geoprism.registry.model.BusinessObject;
import net.geoprism.registry.model.ServerGeoObjectIF;
import net.geoprism.registry.model.graph.VertexServerGeoObject;

@Component
public interface BusinessObjectBusinessServiceIF
{

  JsonObject toJSON(BusinessObject object);

  void apply(BusinessObject object);

  void delete(BusinessObject object);

  boolean exists(BusinessObject object, ServerGeoObjectIF geoObject);

  void addGeoObject(BusinessObject object, ServerGeoObjectIF geoObject);

  void removeGeoObject(BusinessObject object, ServerGeoObjectIF geoObject);

  List<VertexServerGeoObject> getGeoObjects(BusinessObject object);

  boolean exists(BusinessEdgeType type, BusinessObject parent, BusinessObject child);

  void addParent(BusinessObject object, BusinessEdgeType type, BusinessObject parent);

  void removeParent(BusinessObject object, BusinessEdgeType type, BusinessObject parent);

  List<BusinessObject> getParents(BusinessObject object, BusinessEdgeType type);

  void addChild(BusinessObject object, BusinessEdgeType type, BusinessObject child);

  void removeChild(BusinessObject object, BusinessEdgeType type, BusinessObject child);

  List<BusinessObject> getChildren(BusinessObject object, BusinessEdgeType type);

  BusinessObject newInstance(BusinessType type);

  BusinessObject get(BusinessType type, String attributeName, Object value);

  BusinessObject getByCode(BusinessType type, Object value);

}
