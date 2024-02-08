package net.geoprism.registry.service.request;

import java.io.InputStream;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public interface LabeledPropertyGraphTypeVersionServiceIF
{

  void createTiles(String sessionId, String oid);

  InputStream getTile(String sessionId, JSONObject object);

}
