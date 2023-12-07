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

import org.commongeoregistry.adapter.constants.DefaultAttribute;
import org.commongeoregistry.adapter.dataaccess.LocalizedValue;
import org.commongeoregistry.adapter.metadata.AttributeLocalType;
import org.commongeoregistry.adapter.metadata.AttributeType;

import com.runwaysdk.business.graph.GraphObject;
import com.runwaysdk.business.graph.VertexObject;
import com.runwaysdk.system.metadata.MdAttribute;

import net.geoprism.registry.BusinessType;
import net.geoprism.registry.DateFormatter;
import net.geoprism.registry.conversion.RegistryLocalizedValueConverter;

public class BusinessObject
{
  public static final String CODE = "code";

  private VertexObject       vertex;

  private BusinessType       type;

  public BusinessObject(VertexObject vertex, BusinessType type)
  {
    this.vertex = vertex;
    this.type = type;
  }

  public BusinessType getType()
  {
    return type;
  }

  public VertexObject getVertex()
  {
    return vertex;
  }

  public String getLabel()
  {
    MdAttribute labelAttribute = this.getType().getLabelAttribute();

    if (labelAttribute != null)
    {
      String attributeName = labelAttribute.getAttributeName();

      Object value = this.getObjectValue(attributeName);

      if (value != null)
      {
        if (value instanceof Date)
        {
          return DateFormatter.formatDate((Date) value, false);
        }

        return value.toString();
      }
    }

    return this.getCode();
  }

  public String getCode(BusinessObject this)
  {
    return this.getObjectValue(DefaultAttribute.CODE.getName());
  }

  public void setCode(String code)
  {
    this.setValue(DefaultAttribute.CODE.getName(), code);
  }

  public void setValue(String attributeName, Object value)
  {
    AttributeType at = this.getType().getAttribute(attributeName);

    if (at instanceof AttributeLocalType)
    {
      RegistryLocalizedValueConverter.populate(this.getVertex(), attributeName, (LocalizedValue) value);
    }
    else
    {
      this.getVertex().setValue(attributeName, value);
    }
  }

  @SuppressWarnings("unchecked")
  public <T> T getObjectValue(String attributeName)
  {
    AttributeType at = this.getType().getAttribute(attributeName);

    if (at instanceof AttributeLocalType)
    {
      return (T) this.getValueLocalized(attributeName);
    }

    return this.getVertex().getObjectValue(attributeName);
  }

  private LocalizedValue getValueLocalized(String attributeName)
  {
    GraphObject graphObject = this.getVertex().getEmbeddedComponent(attributeName);

    if (graphObject == null)
    {
      return null;
    }

    return RegistryLocalizedValueConverter.convert(graphObject);
  }

}
