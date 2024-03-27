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
package net.geoprism.registry.graph.transition;

import com.google.gson.JsonObject;

import net.geoprism.registry.service.business.ServiceFactory;
import net.geoprism.registry.service.business.TransitionEventBusinessServiceIF;
import net.geoprism.registry.view.JsonSerializable;

public class TransitionEvent extends TransitionEventBase implements JsonSerializable
{
  public static final String EVENT_SEQUENCE   = "event_seq";

  private static final long  serialVersionUID = 112753140;

  public TransitionEvent()
  {
    super();
  }

  @Override
  public JsonObject toJSON()
  {
    return ServiceFactory.getBean(TransitionEventBusinessServiceIF.class).toJSON(this, false);
  }
}
