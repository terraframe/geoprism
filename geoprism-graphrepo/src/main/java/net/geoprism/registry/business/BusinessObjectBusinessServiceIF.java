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

  public JsonObject toJSON(BusinessObject object);

  public void apply(BusinessObject object);

  public void delete(BusinessObject object);

  public boolean exists(BusinessObject object, ServerGeoObjectIF geoObject);

  public void addGeoObject(BusinessObject object, ServerGeoObjectIF geoObject);

  public void removeGeoObject(BusinessObject object, ServerGeoObjectIF geoObject);

  public List<VertexServerGeoObject> getGeoObjects(BusinessObject object);

  public boolean exists(BusinessEdgeType type, BusinessObject parent, BusinessObject child);

  public void addParent(BusinessObject object, BusinessEdgeType type, BusinessObject parent);

  public void removeParent(BusinessObject object, BusinessEdgeType type, BusinessObject parent);

  public List<BusinessObject> getParents(BusinessObject object, BusinessEdgeType type);

  public void addChild(BusinessObject object, BusinessEdgeType type, BusinessObject child);

  public void removeChild(BusinessObject object, BusinessEdgeType type, BusinessObject child);

  public List<BusinessObject> getChildren(BusinessObject object, BusinessEdgeType type);

  public BusinessObject newInstance(BusinessType type);

  public BusinessObject get(BusinessType type, String attributeName, Object value);

  public BusinessObject getByCode(BusinessType type, Object value);

}
