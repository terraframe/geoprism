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
package net.geoprism.registry.model;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.commongeoregistry.adapter.constants.DefaultAttribute;

import com.runwaysdk.business.graph.VertexObject;

import net.geoprism.registry.graph.ConceptClass;
import net.geoprism.registry.model.graph.ServerObjectVertex;
import net.geoprism.registry.model.graph.VertexComponent;

public class ConceptObject extends ServerObjectVertex implements VertexComponent
{
  public static final String CODE = "code";

  public ConceptObject(ConceptClass type, VertexObject vertex, Map<String, List<VertexObject>> valueNodeMap)
  {
    this(type, vertex, valueNodeMap, null);
  }

  public ConceptObject(ConceptClass type, VertexObject vertex, Map<String, List<VertexObject>> valueNodeMap, Date date)
  {
    super(type, vertex, valueNodeMap, date);
  }

  @SuppressWarnings("unchecked")
  public ConceptClass getType()
  {
    return (ConceptClass) super.getType();
  }

  public VertexObject getVertex()
  {
    return vertex;
  }

  public String getCode()
  {
    return this.getValue(DefaultAttribute.CODE.getName());
  }

  public void setCode(String code)
  {
    this.setValue(DefaultAttribute.CODE.getName(), code);
  }

}
