package net.geoprism.registry.service.business;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.google.gson.JsonObject;

import net.geoprism.registry.graph.transition.Transition;
import net.geoprism.registry.graph.transition.Transition.TransitionImpact;
import net.geoprism.registry.graph.transition.Transition.TransitionType;
import net.geoprism.registry.graph.transition.TransitionEvent;
import net.geoprism.registry.model.ServerGeoObjectIF;
import net.geoprism.registry.model.ServerGeoObjectType;
import net.geoprism.registry.view.Page;

@Component
public interface TransitionEventBusinessServiceIF
{
  public void delete(TransitionEvent tran);

  public List<Transition> getTransitions(TransitionEvent tran);

  public Transition addTransition(TransitionEvent tran, ServerGeoObjectIF source, ServerGeoObjectIF target, TransitionType transitionType, TransitionImpact impact);

  public boolean readOnly(TransitionEvent tran);

  public JsonObject toJSON(TransitionEvent tran, boolean includeTransitions);

  public void apply(TransitionEvent tran);

  public JsonObject apply(JsonObject json);

  public Long getCount();

  public Page<TransitionEvent> page(Integer pageSize, Integer pageNumber, String attrConditions);

  public void addPageWhereCriteria(StringBuilder statement, Map<String, Object> parameters, String attrConditions);
  
  public void removeAll(ServerGeoObjectType type);

  public List<TransitionEvent> getAll(ServerGeoObjectType type);

  public Long getNextSequenceNumber();
}
