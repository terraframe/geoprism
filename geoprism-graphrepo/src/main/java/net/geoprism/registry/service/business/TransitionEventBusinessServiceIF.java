/**
 * Copyright (c) 2023 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Geoprism(tm).
 *
 * Geoprism(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Geoprism(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Geoprism(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
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
