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
package net.geoprism.registry.query.graph;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.commongeoregistry.adapter.constants.DefaultAttribute;
import org.commongeoregistry.adapter.dataaccess.LocalizedValue;

import com.google.gson.JsonObject;
import com.runwaysdk.business.graph.GraphQuery;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.session.Session;

import net.geoprism.registry.DateFormatter;
import net.geoprism.registry.conversion.RegistryLocalizedValueConverter;
import net.geoprism.registry.graph.AttributeClassificationType;
import net.geoprism.registry.graph.AttributeDataSourceType;
import net.geoprism.registry.graph.AttributeLocalType;
import net.geoprism.registry.graph.AttributeType;
import net.geoprism.registry.model.graph.ObjectClassIF;
import net.geoprism.registry.view.JsonSerializable;
import net.geoprism.registry.view.JsonWrapper;

public class ObjectPageQuery extends AbstractGraphPageQuery<HashMap<String, Object>, JsonSerializable>
{
  private SimpleDateFormat          format;

  private NumberFormat              numberFormat;

  private Collection<AttributeType> attributes;

  public ObjectPageQuery(ObjectClassIF objectClass, JsonObject criteria)
  {
    super(objectClass.getMdVertexDAO().definesType(), criteria);

    this.format = new SimpleDateFormat("yyyy-MM-dd");
    this.format.setTimeZone(DateFormatter.SYSTEM_TIMEZONE);

    this.numberFormat = NumberFormat.getInstance(Session.getCurrentLocale());

    this.attributes = objectClass.getAttributeMap().values();
  }

  @SuppressWarnings("unchecked")
  protected List<JsonSerializable> getResults(final GraphQuery<HashMap<String, Object>> query)
  {
    List<?> results = query.getResults();

    return results.stream().map(result -> {
      JsonObject object = new JsonObject();

      Map<String, Object> row = ! ( result instanceof Map ) ? Map.of(DefaultAttribute.CODE.getName(), result) : (Map<String, Object>) result;

      object.addProperty(DefaultAttribute.CODE.getName(), (String) row.get(DefaultAttribute.CODE.getName()));

      this.attributes.stream() //
          .filter(a -> !a.getIsChangeOverTime()) //
          .filter(a -> !a.getCode().equals(DefaultAttribute.CODE.name())) //
          .forEach(attribute -> {
            String attributeName = attribute.getCode();

            Object value = row.get(attributeName);

            if (value != null)
            {
              if (attribute instanceof AttributeLocalType || attribute instanceof AttributeClassificationType)
              {
                LocalizedValue localizedValue = RegistryLocalizedValueConverter.convert((HashMap<String, ?>) value);

                object.addProperty(attributeName, localizedValue.getValue());
              }
              else if (value instanceof Double)
              {
                object.addProperty(attributeName, numberFormat.format((Double) value));
              }
              else if (value instanceof Number)
              {
                object.addProperty(attributeName, (Number) value);
              }
              else if (value instanceof Boolean)
              {
                object.addProperty(attributeName, (Boolean) value);
              }
              else if (value instanceof String)
              {
                object.addProperty(attributeName, (String) value);
              }
              else if (value instanceof Character)
              {
                object.addProperty(attributeName, (Character) value);
              }
              else if (value instanceof Date)
              {
                object.addProperty(attributeName, format.format((Date) value));
              }
            }

          });

      return new JsonWrapper(object);

    }).collect(Collectors.toList());
  }

  protected String getColumnName(final MdVertexDAOIF mdVertex, AttributeType type)
  {
    return this.getColumnName(mdVertex.getAllDefinedMdAttributeMap().get(type.getCode().toLowerCase()));
  }

  @Override
  protected String getColumnName(MdAttributeDAOIF mdAttribute)
  {
    return mdAttribute.getColumnName();
  }

  public void addSelectAttributes(final MdVertexDAOIF mdVertex, StringBuilder statement)
  {
    List<String> columnNames = this.attributes.stream() //
        .filter(a -> !a.getIsChangeOverTime()) //
        .map(attribute -> {
          if (attribute instanceof AttributeClassificationType || attribute instanceof AttributeLocalType)
          {
            return this.getColumnName(mdVertex, attribute) + ".displayLabel AS " + attribute.getCode();
          }
          else if (attribute instanceof AttributeDataSourceType)
          {
            return this.getColumnName(mdVertex, attribute) + ".code AS " + attribute.getCode();
          }

          return this.getColumnName(mdVertex, attribute) + " AS " + attribute.getCode();
        }).collect(Collectors.toList());

    statement.append(String.join(", ", columnNames));
  }

}
