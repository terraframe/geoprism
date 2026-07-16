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
package net.geoprism.registry.service.business;

import java.util.HashMap;
import java.util.Map;

import org.commongeoregistry.adapter.constants.DefaultAttribute;
import org.commongeoregistry.adapter.dataaccess.GeoObject;
import org.commongeoregistry.adapter.dataaccess.LocalizedValue;
import org.commongeoregistry.adapter.metadata.AttributeClassificationType;
import org.commongeoregistry.adapter.metadata.AttributeDataSourceType;
import org.commongeoregistry.adapter.metadata.AttributeType;
import org.commongeoregistry.adapter.metadata.GeoObjectType;
import org.locationtech.jts.geom.Geometry;
import org.springframework.beans.factory.annotation.Autowired;

import com.runwaysdk.business.graph.VertexObject;
import com.runwaysdk.dataaccess.MdAttributeGraphReferenceDAOIF;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.system.metadata.MdVertex;

import net.geoprism.graph.LabeledPropertyGraphSynchronization;
import net.geoprism.graph.LabeledPropertyGraphTypeVersion;
import net.geoprism.registry.conversion.LocalizedValueConverter;
import net.geoprism.registry.graph.DataSource;
import net.geoprism.registry.lpg.LPGPublishProgressMonitorIF;
import net.geoprism.registry.view.ObjectAtTimeDTO;

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

  protected LPGPublishProgressMonitorIF                      monitor;

  @Autowired
  protected LabeledPropertyGraphTypeVersionBusinessServiceIF service;

  @Autowired
  protected ClassificationBusinessServiceIF                  classificationService;

  @Autowired
  protected DataSourceBusinessServiceIF                      sourceService;

  public State createState(LabeledPropertyGraphSynchronization synchronization, LabeledPropertyGraphTypeVersion version)
  {
    return new State(synchronization, version);
  }

  @Transaction
  protected VertexObject publish(State state, MdVertex mdVertex, GeoObject geoObject)
  {
    GeoObjectType type = geoObject.getType();

    VertexObject node = new VertexObject(mdVertex.definesType());

    Map<String, AttributeType> attributes = type.getAttributeMap();

    attributes.forEach((attributeName, attribute) -> {

      if (node.hasAttribute(attributeName))
      {
        if (attribute instanceof AttributeClassificationType)
        {
          String value = (String) geoObject.getValue(attributeName);

          if (value != null)
          {
            this.classificationService.get((AttributeClassificationType) attribute, value).ifPresent(classification -> {
              node.setValue(attributeName, classification.getVertex());
            });

          }
          else
          {
            node.setValue(attributeName, (String) null);
          }
        }
        else if (attribute instanceof AttributeDataSourceType)
        {
          String code = (String) geoObject.getValue(attributeName);

          if (code != null)
          {
            Map<String, Object> cache = state.getCache();

            String key = "SOURCE-" + code;

            if (!cache.containsKey(key))
            {
              cache.put(key, this.sourceService.getByCode(code).orElse(null));
            }

            node.setValue(attributeName, (DataSource) cache.get(key));
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

    this.setGeometryValue(geoObject, type, node);
    node.setValue(DefaultAttribute.CODE.getName(), geoObject.getCode());
    node.setValue(DefaultAttribute.UID.getName(), geoObject.getUid());
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

  protected void beginWork(long workTotal, Object importStage)
  {
    if (monitor != null)
    {
      monitor.appLock();
      monitor.setWorkTotal(workTotal);
      monitor.setWorkProgress(0L);
      monitor.clearStage();
      monitor.addStage(importStage);
      monitor.apply();
    }
  }

  protected void recordProgress(long progress, Object importStage)
  {
    if (monitor != null)
    {
      monitor.appLock();
      monitor.setWorkProgress(progress);
      monitor.clearStage();
      monitor.addStage(importStage);
      monitor.apply();
    }
  }

  protected void updateProgress(long workTotal, long progress, Object importStage)
  {
    if (monitor != null)
    {
      monitor.appLock();
      monitor.setWorkProgress(progress);
      monitor.setWorkTotal(workTotal);
      monitor.clearStage();
      monitor.addStage(importStage);
      monitor.apply();
    }
  }

  @Transaction
  protected VertexObject publishObject(State state, MdVertexDAOIF mdVertex, ObjectAtTimeDTO dto)
  {
    VertexObject node = new VertexObject(mdVertex.definesType());

    mdVertex.definesAttributes().stream().filter(attribute -> !attribute.isSystem()).forEach(attribute -> {

      String attributeName = attribute.definesAttribute();

      if (node.hasAttribute(attributeName))
      {
        if (!dto.has(attributeName))
        {
          node.setValue(attributeName, (String) null);
        }
        else if (attribute instanceof AttributeClassificationType)
        {
          String value = dto.getValue(attributeName);

          if (value != null)
          {
            this.classificationService.get((AttributeClassificationType) attribute, value).ifPresent(classification -> {
              node.setValue(attributeName, classification.getVertex());
            });

          }
          else
          {
            node.setValue(attributeName, (String) null);
          }
        }
        else if (attribute instanceof MdAttributeGraphReferenceDAOIF)
        {
          String value = dto.getValue(attributeName);

          this.classificationService.get((AttributeClassificationType) attribute, value).ifPresent(classification -> {
            node.setValue(attributeName, classification.getVertex());
          });

        }
        else
        {
          node.setValue(attributeName, dto.getValue(attributeName));
        }
      }
    });

    node.setValue(DefaultAttribute.CODE.getName(), dto.getCode());
    node.apply();

    return node;
  }

}
