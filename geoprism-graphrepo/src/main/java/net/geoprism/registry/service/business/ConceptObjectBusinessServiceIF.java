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

import org.springframework.stereotype.Component;

import com.google.gson.JsonObject;
import com.runwaysdk.business.graph.VertexObject;

import net.geoprism.registry.graph.ConceptClass;
import net.geoprism.registry.model.ConceptObject;

@Component
public interface ConceptObjectBusinessServiceIF
{

  public void apply(ConceptObject object);

  public void apply(ConceptObject object, boolean validateOrigin);

  public void delete(ConceptObject object);

  public void delete(ConceptObject object, boolean validateOrigin);

  public ConceptObject newInstance(ConceptClass type);

  public ConceptObject newInstance(ConceptClass type, JsonObject json);

  public ConceptObject get(ConceptClass type, String attributeName, Object value);

  public ConceptObject getByCode(ConceptClass type, Object value);

  public void populate(ConceptObject object, JsonObject json);

  public JsonObject toJSON(ConceptObject object);

  public List<ConceptObject> processTraverseResults(List<VertexObject> results, Date date);

  public ConceptObject processSingleResult(List<VertexObject> list, Date date);

}
