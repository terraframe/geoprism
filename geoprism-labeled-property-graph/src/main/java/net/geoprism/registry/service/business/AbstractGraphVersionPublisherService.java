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

import java.util.HashMap;
import java.util.Map;

import org.commongeoregistry.adapter.constants.DefaultAttribute;
import org.commongeoregistry.adapter.dataaccess.GeoObject;
import org.commongeoregistry.adapter.dataaccess.LocalizedValue;
import org.commongeoregistry.adapter.metadata.AttributeClassificationType;
import org.commongeoregistry.adapter.metadata.AttributeTermType;
import org.commongeoregistry.adapter.metadata.AttributeType;
import org.commongeoregistry.adapter.metadata.GeoObjectType;
import org.locationtech.jts.geom.Geometry;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.JsonObject;
import com.runwaysdk.business.graph.VertexObject;
import com.runwaysdk.dataaccess.MdAttributeBooleanDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDecDAOIF;
import com.runwaysdk.dataaccess.MdAttributeGraphReferenceDAOIF;
import com.runwaysdk.dataaccess.MdAttributeNumberDAOIF;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.system.metadata.MdVertex;

import net.geoprism.graph.LabeledPropertyGraphSynchronization;
import net.geoprism.graph.LabeledPropertyGraphTypeVersion;
import net.geoprism.registry.cache.ClassificationCache;
import net.geoprism.registry.conversion.LocalizedValueConverter;
import net.geoprism.registry.model.Classification;

public abstract class AbstractGraphVersionPublisherService
{
  public static class State
  {
    public Map<String, Object>                 cache;

    public LabeledPropertyGraphSynchronization synchronization;

    public LabeledPropertyGraphTypeVersion     version;

    public State(LabeledPropertyGraphSynchronization synchronization, LabeledPropertyGraphTypeVersion version)
    {
      super();
      this.synchronization = synchronization;
      this.version = version;
      this.cache = new HashMap<>();
    }

    public Map<String, Object> getCache()
    {
      return cache;
    }

    public LabeledPropertyGraphSynchronization getSynchronization()
    {
      return synchronization;
    }

    public LabeledPropertyGraphTypeVersion getVersion()
    {
      return version;
    }

  }

  @Autowired
  protected LabeledPropertyGraphTypeVersionBusinessServiceIF service;

  @Autowired
  protected ClassificationBusinessServiceIF                  classificationService;

  public State createState(LabeledPropertyGraphSynchronization synchronization, LabeledPropertyGraphTypeVersion version)
  {
    return new State(synchronization, version);
  }

  @Transaction
  protected VertexObject publish(State state, MdVertex mdVertex, GeoObject geoObject, ClassificationCache classiCache)
  {
    GeoObjectType type = geoObject.getType();

    VertexObject node = new VertexObject(mdVertex.definesType());

    node.setValue(DefaultAttribute.CODE.getName(), geoObject.getCode());
    node.setValue(DefaultAttribute.UID.getName(), geoObject.getUid());

    this.setGeometryValue(geoObject, type, node);

    Map<String, AttributeType> attributes = type.getAttributeMap();

    attributes.forEach((attributeName, attribute) -> {

      if (node.hasAttribute(attributeName))
      {
        if (attribute instanceof AttributeTermType)
        {
          // Iterator<String> it = (Iterator<String>)
          // geoObject.getValue(attributeName);
          //
          // if (it.hasNext())
          // {
          // String code = it.next();
          //
          // Term root = ( (AttributeTermType) attribute ).getRootTerm();
          // String parent =
          // TermConverter.buildClassifierKeyFromTermCode(root.getCode());
          //
          // String classifierKey = Classifier.buildKey(parent, code);
          // Classifier classifier = Classifier.getByKey(classifierKey);
          //
          // node.setValue(attributeName, classifier.getOid());
          // }
          // else
          // {
          // node.setValue(attributeName, (String) null);
          // }
        }
        else if (attribute instanceof AttributeClassificationType)
        {
          String value = (String) geoObject.getValue(attributeName);

          if (value != null)
          {
            String classificationTypeCode = ( (AttributeClassificationType) attribute ).getClassificationType();

            Classification classification = null;
            if (classiCache != null)
            {
              classification = classiCache.getClassification(classificationTypeCode, value.toString().trim());
            }

            if (classification == null)
            {
              classification = this.classificationService.get((AttributeClassificationType) attribute, value);

              if (classification != null && classiCache != null)
              {
                classiCache.putClassification(classificationTypeCode, value.toString().trim(), classification);
              }
            }

            node.setValue(attributeName, classification.getVertex());
          }
          else
          {
            node.setValue(attributeName, (String) null);
          }
        }
        else
        {
          Object value = geoObject.getValue(attributeName);

          if (value instanceof LocalizedValue)
          {
            LocalizedValueConverter.populate(node, attributeName, (LocalizedValue) value);
          }
          else
          {
            node.setValue(attributeName, value);
          }
        }
      }

    });

    node.apply();

    return node;
  }

  private void setGeometryValue(GeoObject object, GeoObjectType type, VertexObject node)
  {
    Geometry geometry = object.getGeometry();

    if (geometry != null)
    {
      node.setValue(DefaultAttribute.GEOMETRY.getName(), geometry);
    }
  }

  @Transaction
  protected VertexObject publishBusiness(State state, MdVertexDAOIF mdVertex, JsonObject dto, ClassificationCache classiCache)
  {
    VertexObject node = new VertexObject(mdVertex.definesType());

    node.setValue(DefaultAttribute.CODE.getName(), dto.get(DefaultAttribute.CODE.getName()).getAsString());

    JsonObject data = dto.get("data").getAsJsonObject();

    mdVertex.definesAttributes().stream().filter(attribute -> !attribute.isSystem()).forEach(attribute -> {

      String attributeName = attribute.definesAttribute();

      if (node.hasAttribute(attributeName) && data.has(attributeName))
      {
        if (data.get(attributeName).isJsonNull())
        {
          node.setValue(attributeName, (String) null);
        }
        else if (attribute instanceof MdAttributeGraphReferenceDAOIF)
        {
          String value = data.get(attributeName).getAsString();

          String classificationTypeCode = ( (AttributeClassificationType) attribute ).getClassificationType();

          Classification classification = null;
          if (classiCache != null)
          {
            classification = classiCache.getClassification(classificationTypeCode, value.toString().trim());
          }

          if (classification == null)
          {
            classification = this.classificationService.get((AttributeClassificationType) attribute, value);

            if (classification != null && classiCache != null)
            {
              classiCache.putClassification(classificationTypeCode, value.toString().trim(), classification);
            }
          }

          node.setValue(attributeName, classification.getVertex());
        }
        else if (attribute instanceof MdAttributeNumberDAOIF)
        {
          node.setValue(attributeName, data.get(attributeName).getAsNumber());
        }
        else if (attribute instanceof MdAttributeDecDAOIF)
        {
          node.setValue(attributeName, data.get(attributeName).getAsBigDecimal());
        }
        else if (attribute instanceof MdAttributeBooleanDAOIF)
        {
          node.setValue(attributeName, data.get(attributeName).getAsBoolean());
        }
        else
        {
          node.setValue(attributeName, data.get(attributeName).getAsString());
        }
      }

    });

    node.apply();

    return node;
  }

}
