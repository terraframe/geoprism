package net.geoprism.graph.lpg.business;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.geoprism.graph.LabeledPropertyGraphType;
import net.geoprism.graph.LabeledPropertyGraphTypeEntry;
import net.geoprism.graph.LabeledPropertyGraphTypeVersion;

@Component
public interface LabeledPropertyGraphTypeEntryBusinessServiceIF
{

  public void delete(LabeledPropertyGraphTypeEntry entry);

  JsonObject toJSON(LabeledPropertyGraphTypeEntry entry);

  List<LabeledPropertyGraphTypeVersion> getVersions(LabeledPropertyGraphTypeEntry entry);

  JsonArray getVersionJson(LabeledPropertyGraphTypeEntry entry);

  LabeledPropertyGraphTypeVersion publish(LabeledPropertyGraphTypeEntry entry);

  LabeledPropertyGraphTypeVersion publishNoAuth(LabeledPropertyGraphTypeEntry entry);

  LabeledPropertyGraphTypeEntry create(LabeledPropertyGraphType type, Date forDate);

  LabeledPropertyGraphTypeEntry create(LabeledPropertyGraphType list, JsonObject json);

  public LabeledPropertyGraphTypeEntry get(String oid);

}
