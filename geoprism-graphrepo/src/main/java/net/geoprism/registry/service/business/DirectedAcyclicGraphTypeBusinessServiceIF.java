/**
 * Copyright (c) 2023 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Geoprism(tm).
 *
 * Geoprism(tm) is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * Geoprism(tm) is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Geoprism(tm). If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism.registry.service.business;

import java.util.List;
import java.util.Optional;

import org.commongeoregistry.adapter.dataaccess.LocalizedValue;
import org.springframework.stereotype.Component;

import com.google.gson.JsonObject;
import com.runwaysdk.system.metadata.MdEdge;

import net.geoprism.registry.DirectedAcyclicGraphType;

@Component
public interface DirectedAcyclicGraphTypeBusinessServiceIF
{

  public void update(DirectedAcyclicGraphType dagt, JsonObject object);

  public void delete(DirectedAcyclicGraphType dagt);

  public DirectedAcyclicGraphType create(JsonObject object);

  public DirectedAcyclicGraphType create(String code, LocalizedValue label, LocalizedValue description, String origin, Long seq);

  public DirectedAcyclicGraphType create(String code, LocalizedValue label, LocalizedValue description, Long seq);

  DirectedAcyclicGraphType getByMdEdge(MdEdge mdEdge);

  Optional<DirectedAcyclicGraphType> getByCode(String code);

  List<DirectedAcyclicGraphType> getAll(String... typeCodes);

}