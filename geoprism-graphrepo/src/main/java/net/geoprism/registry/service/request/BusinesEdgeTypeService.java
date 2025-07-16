package net.geoprism.registry.service.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.runwaysdk.session.Request;
import com.runwaysdk.session.RequestType;

import net.geoprism.configuration.GeoprismProperties;
import net.geoprism.registry.BusinessEdgeType;
import net.geoprism.registry.JsonCollectors;
import net.geoprism.registry.OriginException;
import net.geoprism.registry.service.business.BusinessEdgeTypeBusinessServiceIF;

@Service
public class BusinesEdgeTypeService implements BusinessEdgeTypeServiceIF
{
  @Autowired
  private BusinessEdgeTypeBusinessServiceIF service;

  @Override
  @Request(RequestType.SESSION)
  public void update(String sessionId, String code, JsonObject object)
  {
    BusinessEdgeType type = this.service.getByCode(code);

    if (!type.getOrigin().equals(GeoprismProperties.getOrigin()))
    {
      throw new OriginException();
    }

    this.service.update(type, object);
  }

  @Override
  @Request(RequestType.SESSION)
  public void delete(String sessionId, String code)
  {
    BusinessEdgeType type = this.service.getByCode(code);

    if (!type.getOrigin().equals(GeoprismProperties.getOrigin()))
    {
      throw new OriginException();
    }

    this.service.delete(type);
  }

  @Override
  @Request(RequestType.SESSION)
  public JsonArray getAll(String sessionId)
  {
    return this.service.getAll().stream().map(type -> this.service.toJSON(type)).collect(JsonCollectors.toJsonArray());
  }

  @Override
  @Request(RequestType.SESSION)
  public JsonObject getByCode(String sessionId, String code)
  {
    return this.service.toJSON(this.service.getByCode(code));
  }

  @Override
  @Request(RequestType.SESSION)
  public JsonObject create(String sessionId, JsonObject object)
  {
    BusinessEdgeType type = this.service.create(object);

    return this.service.toJSON(type);
  }
}
