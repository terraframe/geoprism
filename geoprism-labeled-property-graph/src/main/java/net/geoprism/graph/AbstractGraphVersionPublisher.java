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
package net.geoprism.graph;

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

import com.runwaysdk.business.graph.VertexObject;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.system.metadata.MdVertex;

import net.geoprism.graph.service.LabeledPropertyGraphServiceIF;
import net.geoprism.registry.RegistryConstants;
import net.geoprism.registry.conversion.LocalizedValueConverter;
import net.geoprism.registry.model.Classification;

public abstract class AbstractGraphVersionPublisher
{
  private Map<String, Object> cache;

  public AbstractGraphVersionPublisher()
  {
    this.cache = new HashMap<String, Object>();
  }
  
  public Map<String, Object> getCache()
  {
    return cache;
  }

  // protected VertexObject publish(ServerGeoObjectIF object, MdVertex mdVertex,
  // Date forDate)
  // {
  // return publish(mdVertex, object.toGeoObject(forDate));
  // }

  @Transaction
  protected VertexObject publish(LabeledPropertyGraphSynchronization synchronization, MdVertex mdVertex, GeoObject geoObject)
  {
    GeoObjectType type = geoObject.getType();

    VertexObject node = new VertexObject(mdVertex.definesType());

    node.setValue(DefaultAttribute.CODE.getName(), geoObject.getCode());
    node.setValue(RegistryConstants.UUID, geoObject.getUid());

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
            Classification classification = Classification.get((AttributeClassificationType) attribute, value);

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

    LabeledPropertyGraphServiceIF.getInstance().postSynchronization(synchronization, node, this.cache);

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

}
