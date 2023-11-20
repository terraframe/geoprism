package net.geoprism.registry.service.request;

import org.springframework.stereotype.Component;

import com.google.gson.JsonArray;

import net.geoprism.graph.LabeledPropertyGraphSynchronization;
import net.geoprism.graph.LabeledPropertyGraphTypeVersion;
import net.geoprism.registry.service.request.AbstractGraphVersionPublisherService.State;

@Component
public interface JsonGraphVersionPublisherServiceIF
{

  public State createState(LabeledPropertyGraphSynchronization synchronization, LabeledPropertyGraphTypeVersion version);

  public void publishGeoObjects(State state, JsonArray results);

  public void publishEdges(State state, JsonArray results);

}
