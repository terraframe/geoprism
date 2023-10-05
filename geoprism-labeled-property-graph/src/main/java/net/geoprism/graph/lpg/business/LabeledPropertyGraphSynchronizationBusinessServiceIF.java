package net.geoprism.graph.lpg.business;

import org.springframework.stereotype.Component;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.runwaysdk.business.graph.VertexObject;

import net.geoprism.graph.LabeledPropertyGraphSynchronization;
import net.geoprism.registry.view.Page;

@Component
public interface LabeledPropertyGraphSynchronizationBusinessServiceIF
{

  void delete(LabeledPropertyGraphSynchronization synchronization);

  void updateRemoteVersion(LabeledPropertyGraphSynchronization synchronization, String versionId, Integer versionNumber);

  void execute(LabeledPropertyGraphSynchronization synchronization);

  void executeNoAuth(LabeledPropertyGraphSynchronization synchronization);

  VertexObject getObject(LabeledPropertyGraphSynchronization synchronization, String uid);

  LabeledPropertyGraphSynchronization fromJSON(JsonObject object);

  LabeledPropertyGraphSynchronization apply(JsonObject object);

  JsonArray getAll();

  Page<LabeledPropertyGraphSynchronization> page(JsonObject criteria);

  LabeledPropertyGraphSynchronization get(String oid);

}
