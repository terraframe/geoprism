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

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.google.gson.JsonObject;
import com.runwaysdk.business.graph.VertexObject;

import net.geoprism.registry.graph.ObjectClass;
import net.geoprism.registry.model.graph.ServerObjectVertex;
import net.geoprism.registry.view.JsonSerializable;
import net.geoprism.registry.view.ObjectAtTimeDTO;
import net.geoprism.registry.view.ObjectClassDTO;
import net.geoprism.registry.view.ObjectOverTimeDTO;
import net.geoprism.registry.view.Page;

@Component
public interface ObjectBusinessServiceIF<V extends ServerObjectVertex, T extends ObjectClass, D extends ObjectClassDTO>
{

  public void apply(V object);

  public void apply(V object, boolean validateOrigin);

  public void delete(V object);

  public void delete(V object, boolean validateOrigin);

  public V newInstance(T type);

  public V newInstance(T type, ObjectOverTimeDTO dto);

  public Optional<V> get(T type, String attributeName, Object value);

  public Optional<V> getByCode(T type, String code);

  public List<V> processTraverseResults(List<VertexObject> results, Date date);

  public V processSingleResult(List<VertexObject> list, Date date);

  public Long getCount(T type);

  public List<V> getAll(T type, Long skip, Integer limit);

  public ObjectAtTimeDTO toDTO(V object, Date date);

  public ObjectOverTimeDTO toDTO(V object);

  public void populate(V object, ObjectOverTimeDTO dto);

  public void populate(V object, ObjectAtTimeDTO dto, Date startDate, Date endDate);

  public Page<JsonSerializable> data(T type, JsonObject criteria);

}
