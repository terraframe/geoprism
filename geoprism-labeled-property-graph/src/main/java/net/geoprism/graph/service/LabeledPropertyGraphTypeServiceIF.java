package net.geoprism.graph.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public interface LabeledPropertyGraphTypeServiceIF
{
  public JsonArray getAll(String sessionId);

  public JsonObject apply(String sessionId, JsonObject list);

  public JsonObject createEntries(String sessionId, String oid);

  public void remove(String sessionId, String oid);

  public JsonObject createVersion(String sessionId, String oid);

  public JsonObject get(String sessionId, String oid);

  public JsonObject getEntries(String sessionId, String oid);

  public JsonArray getVersions(String sessionId, String oid);

  public JsonObject getEntry(String sessionId, String oid);

  public JsonObject getVersion(String sessionId, String oid, Boolean includeTableDefinitions);

  public void removeVersion(String sessionId, String oid);

  public JsonObject getData(String sessionId, String oid);

  public JsonArray getGeoObjects(String sessionId, String oid, Long skip, Integer blockSize);

  public JsonArray getEdges(String sessionId, String oid, Long skip, Integer blockSize);

}