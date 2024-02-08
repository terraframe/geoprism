package net.geoprism.registry.service.request;

import org.springframework.stereotype.Component;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Component
public interface LabeledPropertyGraphSynchronizationServiceIF
{

  JsonArray getAll(String sessionId);

  JsonArray getForOrganization(String sessionId, String orginzationCode);

  JsonObject apply(String sessionId, JsonObject json);

  void remove(String sessionId, String oid);

  JsonObject updateRemoteVersion(String sessionId, String oid, String versionId, Integer versionNumber);

  JsonObject page(String sessionId, JsonObject criteria);

  JsonObject newInstance(String sessionId);

  JsonObject get(String sessionId, String oid);

}
