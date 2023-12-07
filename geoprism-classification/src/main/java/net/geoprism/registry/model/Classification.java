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

import org.commongeoregistry.adapter.Term;
import org.commongeoregistry.adapter.dataaccess.LocalizedValue;

import com.google.gson.JsonObject;
import com.runwaysdk.business.graph.VertexObject;
import com.runwaysdk.system.AbstractClassification;

import net.geoprism.registry.conversion.LocalizedValueConverter;
import net.geoprism.registry.view.JsonSerializable;

public class Classification implements JsonSerializable
{
  private ClassificationType type;

  private VertexObject       vertex;

  public Classification(ClassificationType type, VertexObject vertex)
  {
    this.type = type;
    this.vertex = vertex;
  }

  public ClassificationType getType()
  {
    return type;
  }

  public VertexObject getVertex()
  {
    return vertex;
  }

  public String getOid()
  {
    return this.getVertex().getOid();
  }

  public void setCode(String code)
  {
    this.getVertex().setValue(AbstractClassification.CODE, code);
  }

  public String getCode()
  {
    return this.getVertex().getObjectValue(AbstractClassification.CODE);
  }

  public LocalizedValue getDisplayLabel()
  {
    return LocalizedValueConverter.convert(this.vertex.getEmbeddedComponent(AbstractClassification.DISPLAYLABEL));
  }

  public void setDisplayLabel(LocalizedValue displayLabel)
  {
    LocalizedValueConverter.populate(this.vertex, AbstractClassification.DISPLAYLABEL, displayLabel);
  }

  public LocalizedValue getDescription()
  {
    return LocalizedValueConverter.convert(this.vertex.getEmbeddedComponent(AbstractClassification.DESCRIPTION));
  }

  public void setDescription(LocalizedValue description)
  {
    LocalizedValueConverter.populate(this.vertex, AbstractClassification.DESCRIPTION, description);
  }

  @Override
  public JsonObject toJSON()
  {
    JsonObject object = new JsonObject();
    object.addProperty(AbstractClassification.CODE, (String) this.vertex.getObjectValue(AbstractClassification.CODE));
    object.add(AbstractClassification.DISPLAYLABEL, this.getDisplayLabel().toJSON());
    object.add(AbstractClassification.DESCRIPTION, this.getDescription().toJSON());

    return object;
  }

  @Override
  public String toString()
  {
    return this.getCode();
  }

  public Term toTerm()
  {
    return new Term(this.getCode(), this.getDisplayLabel(), this.getDescription());
  }

}
