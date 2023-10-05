package net.geoprism.graph.lpg.business;

import java.util.List;

import org.springframework.stereotype.Component;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.geoprism.graph.LabeledPropertyGraphType;
import net.geoprism.graph.LabeledPropertyGraphTypeEntry;
import net.geoprism.graph.LabeledPropertyGraphTypeVersion;

@Component
public interface LabeledPropertyGraphTypeBusinessServiceIF
{

  JsonArray list();

  public LabeledPropertyGraphType apply(JsonObject list);

  public LabeledPropertyGraphType apply(JsonObject object, boolean createEntries);

  public void apply(LabeledPropertyGraphType type);

  List<LabeledPropertyGraphTypeVersion> getVersions(LabeledPropertyGraphType type);

  List<LabeledPropertyGraphTypeEntry> getEntries(LabeledPropertyGraphType type);

  public void delete(LabeledPropertyGraphType type);

  JsonObject toJSON(LabeledPropertyGraphType type);

  JsonObject toJSON(LabeledPropertyGraphType type, boolean includeEntries);

  public void createEntries(LabeledPropertyGraphType type);

  LabeledPropertyGraphType get(String oid);

  LabeledPropertyGraphType fromJSON(JsonObject object);

}
