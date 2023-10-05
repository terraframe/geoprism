package net.geoprism.registry.business;

import net.geoprism.registry.graph.transition.Transition;
import net.geoprism.registry.graph.transition.TransitionEvent;
import net.geoprism.registry.model.ServerGeoObjectType;
import net.geoprism.registry.model.graph.VertexServerGeoObject;

public interface TransitionBusinessServiceIF
{
  public void apply(Transition tran, TransitionEvent event, int order, VertexServerGeoObject source, VertexServerGeoObject target);
  
  public void validate(Transition tran, TransitionEvent event, VertexServerGeoObject source, VertexServerGeoObject target);
  
  public void removeAll(ServerGeoObjectType type);
}
