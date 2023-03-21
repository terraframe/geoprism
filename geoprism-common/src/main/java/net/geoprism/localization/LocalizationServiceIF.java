package net.geoprism.localization;

import com.google.gson.JsonObject;

public interface LocalizationServiceIF
{
  public JsonObject getNewLocaleInformation(String sessionId);
}
