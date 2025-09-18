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
import java.util.stream.Collectors;

import org.commongeoregistry.adapter.constants.DefaultAttribute;
import org.commongeoregistry.adapter.dataaccess.LocalizedValue;
import org.commongeoregistry.adapter.metadata.AttributeClassificationType;
import org.commongeoregistry.adapter.metadata.AttributeDataSourceType;
import org.commongeoregistry.adapter.metadata.AttributeLocalType;
import org.commongeoregistry.adapter.metadata.AttributeTermType;
import org.commongeoregistry.adapter.metadata.AttributeType;

import com.google.gson.JsonObject;
import com.runwaysdk.business.graph.GraphQuery;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.session.Session;

import net.geoprism.ontology.Classifier;
import net.geoprism.registry.BusinessType;
import net.geoprism.registry.DateFormatter;
import net.geoprism.registry.conversion.RegistryLocalizedValueConverter;
import net.geoprism.registry.view.JsonSerializable;
import net.geoprism.registry.view.JsonWrapper;

public class BusinessObjectPageQuery extends AbstractGraphPageQuery<HashMap<String, Object>, JsonSerializable>
{
  private SimpleDateFormat          format;

  private NumberFormat              numberFormat;

  private Collection<AttributeType> attributes;

  public BusinessObjectPageQuery(BusinessType businessType, JsonObject criteria)
  {
    super(businessType.getMdVertexDAO().definesType(), criteria);

    this.format = new SimpleDateFormat("yyyy-MM-dd");
    this.format.setTimeZone(DateFormatter.SYSTEM_TIMEZONE);

    this.numberFormat = NumberFormat.getInstance(Session.getCurrentLocale());

    this.attributes = businessType.getAttributeMap().values();
  }

  @SuppressWarnings("unchecked")
  protected List<JsonSerializable> getResults(final GraphQuery<HashMap<String, Object>> query)
  {
    List<HashMap<String, Object>> results = query.getResults();

    return results.stream().map(row -> {
      JsonObject object = new JsonObject();

      object.addProperty(DefaultAttribute.CODE.getName(), (String) row.get(DefaultAttribute.CODE.getName()));

      this.attributes.stream().filter(a -> !a.getName().equals(DefaultAttribute.CODE.name())).forEach(attribute -> {
        String attributeName = attribute.getName();

        Object value = row.get(attributeName);

        if (value != null)
        {
          if (attribute instanceof AttributeTermType)
          {
            Classifier classifier = Classifier.get((String) value);

            object.addProperty(attributeName, classifier.getDisplayLabel().getValue());
          }
          else if (attribute instanceof AttributeLocalType || attribute instanceof AttributeClassificationType)
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
    return this.getColumnName(mdVertex.definesAttribute(type.getName()));
  }

  @Override
  protected String getColumnName(MdAttributeDAOIF mdAttribute)
  {
    return mdAttribute.getColumnName();
  }

  public void addSelectAttributes(final MdVertexDAOIF mdVertex, StringBuilder statement)
  {
    List<String> columnNames = this.attributes.stream().map(attribute -> {
      if (attribute instanceof AttributeClassificationType || attribute instanceof AttributeLocalType)
      {
        return this.getColumnName(mdVertex, attribute) + ".displayLabel AS " + attribute.getName();
      }
      else if (attribute instanceof AttributeDataSourceType)
      {
        return this.getColumnName(mdVertex, attribute) + ".code AS " + attribute.getName();
      }

      return this.getColumnName(mdVertex, attribute) + " AS " + attribute.getName();
    }).collect(Collectors.toList());

    statement.append(String.join(", ", columnNames));
  }

}
