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
import java.util.TreeMap;
import java.util.UUID;
import java.util.stream.Collectors;

import org.commongeoregistry.adapter.constants.DefaultAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;
import com.runwaysdk.business.graph.EdgeObject;
import com.runwaysdk.business.graph.GraphQuery;
import com.runwaysdk.business.graph.VertexObject;
import com.runwaysdk.dataaccess.MdAttributeBooleanDAOIF;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDateDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDecimalDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDoubleDAOIF;
import com.runwaysdk.dataaccess.MdAttributeLongDAOIF;
import com.runwaysdk.dataaccess.MdAttributeNumberDAOIF;
import com.runwaysdk.dataaccess.MdAttributeTermDAOIF;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.graph.VertexObjectDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;

import net.geoprism.configuration.GeoprismProperties;
import net.geoprism.ontology.Classifier;
import net.geoprism.registry.BusinessEdgeType;
import net.geoprism.registry.BusinessType;
import net.geoprism.registry.DateFormatter;
import net.geoprism.registry.OriginException;
import net.geoprism.registry.model.BusinessObject;
import net.geoprism.registry.model.EdgeDirection;
import net.geoprism.registry.model.ServerGeoObjectIF;
import net.geoprism.registry.model.ServerGeoObjectType;
import net.geoprism.registry.model.graph.VertexServerGeoObject;

@Service
public class BusinessObjectBusinessService implements BusinessObjectBusinessServiceIF
{
  @Autowired
  private BusinessTypeBusinessServiceIF typeService;

  @Override
  public JsonObject toJSON(BusinessObject object)
  {
    JsonObject data = new JsonObject();

    List<? extends MdAttributeConcreteDAOIF> mdAttributes = object.getType().getMdVertexDAO().definesAttributes();

    for (MdAttributeConcreteDAOIF mdAttribute : mdAttributes)
    {
      String attributeName = mdAttribute.definesAttribute();

      if (!attributeName.equals(BusinessObject.CODE))
      {

        Object value = object.getVertex().getObjectValue(attributeName);

        if (value != null)
        {
          if (mdAttribute instanceof MdAttributeTermDAOIF)
          {
            Classifier classifier = Classifier.get((String) value);

            data.addProperty(mdAttribute.definesAttribute(), classifier.getDisplayLabel().getValue());
          }
          else if (value instanceof Number)
          {
            data.addProperty(mdAttribute.definesAttribute(), (Number) value);
          }
          else if (value instanceof Boolean)
          {
            data.addProperty(mdAttribute.definesAttribute(), (Boolean) value);
          }
          else if (value instanceof String)
          {
            data.addProperty(mdAttribute.definesAttribute(), (String) value);
          }
          else if (value instanceof Character)
          {
            data.addProperty(mdAttribute.definesAttribute(), (Character) value);
          }
          else if (value instanceof Date)
          {
            data.addProperty(mdAttribute.definesAttribute(), DateFormatter.formatDate((Date) value, false));
          }
        }
      }
    }

    JsonObject json = new JsonObject();
    json.addProperty("code", object.getCode());
    json.addProperty("label", object.getLabel());
    json.add("data", data);

    return json;
  }

  @Override
  public BusinessObject newInstance(BusinessType type, JsonObject json)
  {
    BusinessObject object = this.newInstance(type);

    populate(object, json);

    return object;
  }

  @Override
  public void populate(BusinessObject object, JsonObject json)
  {
    object.setCode(json.get("code").getAsString());

    JsonObject data = json.get("data").getAsJsonObject();

    object.getType().getMdVertexDAO().definesAttributes().stream().filter(mdAttribute -> {
      String attributeName = mdAttribute.definesAttribute();

      return !attributeName.equals(BusinessObject.CODE) && !mdAttribute.isSystem();
    }).forEach(mdAttribute -> {
      String attributeName = mdAttribute.definesAttribute();

      if (data.has(attributeName) && !data.get(attributeName).isJsonNull())
      {
        if (mdAttribute instanceof MdAttributeTermDAOIF)
        {
          String value = data.get(attributeName).getAsString();

          Classifier classifier = Classifier.get((String) value);

          object.setValue(attributeName, classifier);
        }
        else if (mdAttribute instanceof MdAttributeLongDAOIF)
        {
          object.setValue(attributeName, data.get(attributeName).getAsLong());
        }
        else if (mdAttribute instanceof MdAttributeDoubleDAOIF)
        {
          object.setValue(attributeName, data.get(attributeName).getAsDouble());
        }
        else if (mdAttribute instanceof MdAttributeDecimalDAOIF)
        {
          object.setValue(attributeName, data.get(attributeName).getAsBigDecimal());
        }
        else if (mdAttribute instanceof MdAttributeNumberDAOIF)
        {
          object.setValue(attributeName, data.get(attributeName).getAsNumber());
        }
        else if (mdAttribute instanceof MdAttributeBooleanDAOIF)
        {
          object.setValue(attributeName, data.get(attributeName).getAsBoolean());
        }
        else if (mdAttribute instanceof MdAttributeDateDAOIF)
        {
          object.setValue(attributeName, DateFormatter.parseDate(data.get(attributeName).getAsString()));
        }
        else
        {
          object.setValue(attributeName, data.get(attributeName).getAsString());
        }
      }
    });
  }

  @Override
  @Transaction
  public void apply(BusinessObject object)
  {
    apply(object, true);
  }

  @Override
  @Transaction
  public void apply(BusinessObject object, boolean validateOrigin)
  {
    if (validateOrigin)
    {
      BusinessType type = object.getType();

      if (!type.getOrigin().equals(GeoprismProperties.getOrigin()))
      {
        throw new OriginException();
      }
    }

    object.getVertex().apply();
  }

  @Override
  @Transaction
  public void delete(BusinessObject object)
  {
    this.delete(object, true);
  }

  @Override
  public void delete(BusinessObject object, boolean validateOrigin)
  {
    if (validateOrigin)
    {
      if (!object.getType().getOrigin().equals(GeoprismProperties.getOrigin()))
      {
        throw new OriginException();
      }
    }

    object.getVertex().delete();
  }

  @Override
  public boolean exists(BusinessObject object, BusinessEdgeType edgeType, ServerGeoObjectIF geoObject, EdgeDirection direction)
  {
    if (geoObject != null && geoObject instanceof VertexServerGeoObject)
    {
      return getEdge(object, edgeType, geoObject, direction) != null;
    }

    return false;
  }

  protected EdgeObject getEdge(BusinessObject object, BusinessEdgeType edgeType, ServerGeoObjectIF geoObject, EdgeDirection direction)
  {
    VertexObject geoVertex = ( (VertexServerGeoObject) geoObject ).getVertex();

    String statement = "SELECT FROM " + edgeType.getMdEdgeDAO().getDBClassName();
    statement += " WHERE out = :parent";
    statement += " AND in = :child";

    GraphQuery<EdgeObject> query = new GraphQuery<EdgeObject>(statement);
    query.setParameter("parent", direction.equals(EdgeDirection.PARENT) ? geoVertex.getRID() : object.getVertex().getRID());
    query.setParameter("child", direction.equals(EdgeDirection.PARENT) ? object.getVertex().getRID() : geoVertex.getRID());

    return query.getSingleResult();
  }

  @Override
  public void addGeoObject(BusinessObject object, BusinessEdgeType edgeType, ServerGeoObjectIF geoObject, EdgeDirection direction, String uid, boolean validateOrigin)
  {
    if (validateOrigin)
    {
      if (!edgeType.getOrigin().equals(GeoprismProperties.getOrigin()))
      {
        throw new OriginException();
      }
    }

    if (geoObject != null && geoObject instanceof VertexServerGeoObject && !this.exists(object, edgeType, geoObject, direction))
    {
      VertexObject geoVertex = ( (VertexServerGeoObject) geoObject ).getVertex();

      if (direction.equals(EdgeDirection.CHILD))
      {
        EdgeObject newEdge = geoVertex.addParent(object.getVertex(), edgeType.getMdEdgeDAO());
        newEdge.setValue(DefaultAttribute.UID.getName(), UUID.randomUUID().toString());
        newEdge.apply();
      }
      else if (direction.equals(EdgeDirection.PARENT))
      {
        EdgeObject newEdge = geoVertex.addChild(object.getVertex(), edgeType.getMdEdgeDAO());
        newEdge.setValue(DefaultAttribute.UID.getName(), UUID.randomUUID().toString());
        newEdge.apply();
      }
      else
      {
        throw new UnsupportedOperationException();
      }
    }
  }

  @Override
  public void removeGeoObject(BusinessObject object, BusinessEdgeType edgeType, ServerGeoObjectIF geoObject, EdgeDirection direction, boolean validateOrigin)
  {
    if (validateOrigin)
    {
      if (!edgeType.getOrigin().equals(GeoprismProperties.getOrigin()))
      {
        throw new OriginException();
      }
    }

    if (geoObject != null && geoObject instanceof VertexServerGeoObject)
    {
      VertexObject geoVertex = ( (VertexServerGeoObject) geoObject ).getVertex();

      if (direction.equals(EdgeDirection.CHILD))
      {
        geoVertex.removeParent(object.getVertex(), edgeType.getMdEdgeDAO());
      }
      else if (direction.equals(EdgeDirection.PARENT))
      {
        geoVertex.removeChild(object.getVertex(), edgeType.getMdEdgeDAO());
      }
      else
      {
        throw new UnsupportedOperationException();
      }

    }
  }

  @Override
  public List<VertexServerGeoObject> getGeoObjects(BusinessObject object, BusinessEdgeType edgeType, EdgeDirection direction)
  {
    List<VertexObject> geoObjects = direction.equals(EdgeDirection.PARENT) ? object.getVertex().getParents(edgeType.getMdEdgeDAO(), VertexObject.class) : object.getVertex().getChildren(edgeType.getMdEdgeDAO(), VertexObject.class);

    return geoObjects.stream().map(geoVertex -> {
      MdVertexDAOIF mdVertex = (MdVertexDAOIF) geoVertex.getMdClass();
      ServerGeoObjectType vertexType = ServerGeoObjectType.get(mdVertex);

      return new VertexServerGeoObject(vertexType, geoVertex, new TreeMap<>());

    }).sorted((a, b) -> {
      return a.getDisplayLabel().getValue().compareTo(b.getDisplayLabel().getValue());
    }).collect(Collectors.toList());
  }

  @Override
  public boolean exists(BusinessEdgeType type, BusinessObject parent, BusinessObject child)
  {
    return getEdge(type, parent, child) != null;
  }

  protected EdgeObject getEdge(BusinessEdgeType type, BusinessObject parent, BusinessObject child)
  {
    String statement = "SELECT FROM " + type.getMdEdgeDAO().getDBClassName();
    statement += " WHERE out = :parent";
    statement += " AND in = :child";

    GraphQuery<EdgeObject> query = new GraphQuery<EdgeObject>(statement);
    query.setParameter("parent", parent.getVertex().getRID());
    query.setParameter("child", child.getVertex().getRID());

    return query.getSingleResult();
  }

  @Override
  public void addParent(BusinessObject object, BusinessEdgeType type, BusinessObject parent, String uid)
  {
    this.addParent(object, type, parent, uid, true);
  }

  @Override
  public void addParent(BusinessObject object, BusinessEdgeType type, BusinessObject parent, String uid, boolean validateOrigin)
  {
    if (validateOrigin)
    {
      if (!type.getOrigin().equals(GeoprismProperties.getOrigin()))
      {
        throw new OriginException();
      }
    }

    if (parent != null && !this.exists(type, parent, object))
    {
      EdgeObject newEdge = object.getVertex().addParent(parent.getVertex(), type.getMdEdgeDAO());
      newEdge.setValue(DefaultAttribute.UID.getName(), uid);
      newEdge.apply();
    }
  }

  @Override
  public void removeParent(BusinessObject object, BusinessEdgeType type, BusinessObject parent)
  {
    this.removeParent(object, type, parent, true);
  }

  @Override
  public void removeParent(BusinessObject object, BusinessEdgeType type, BusinessObject parent, boolean validateOrigin)
  {
    if (validateOrigin)
    {
      if (!type.getOrigin().equals(GeoprismProperties.getOrigin()))
      {
        throw new OriginException();
      }
    }

    if (parent != null)
    {
      object.getVertex().removeParent(parent.getVertex(), type.getMdEdgeDAO());
    }
  }

  @Override
  public List<BusinessObject> getParents(BusinessObject object, BusinessEdgeType type)
  {
    List<VertexObject> vertexObjects = object.getVertex().getParents(type.getMdEdgeDAO(), VertexObject.class);

    return vertexObjects.stream().map(vertex -> {
      MdVertexDAOIF mdVertex = (MdVertexDAOIF) vertex.getMdClass();
      BusinessType businessType = this.typeService.getByMdVertex(mdVertex);

      return new BusinessObject(vertex, businessType);

    }).sorted((a, b) -> {
      return a.getLabel().compareTo(b.getLabel());
    }).collect(Collectors.toList());
  }

  @Override
  public void addChild(BusinessObject object, BusinessEdgeType type, BusinessObject child, String uid)
  {
    this.addChild(object, type, child, uid, true);
  }

  @Override
  public void addChild(BusinessObject object, BusinessEdgeType type, BusinessObject child, String uid, boolean validateOrigin)
  {
    if (validateOrigin)
    {
      if (!type.getOrigin().equals(GeoprismProperties.getOrigin()))
      {
        throw new OriginException();
      }
    }

    if (child != null && !this.exists(type, object, child))
    {
      EdgeObject newEdge = object.getVertex().addChild(child.getVertex(), type.getMdEdgeDAO());
      newEdge.setValue(DefaultAttribute.UID.getName(), uid);
      newEdge.apply();
    }
  }

  @Override
  public void removeChild(BusinessObject object, BusinessEdgeType type, BusinessObject child)
  {
    this.removeChild(object, type, child, true);
  }

  @Override
  public void removeChild(BusinessObject object, BusinessEdgeType type, BusinessObject child, boolean validateOrigin)
  {
    if (child != null)
    {
      object.getVertex().removeChild(child.getVertex(), type.getMdEdgeDAO());
    }
  }

  @Override
  public List<BusinessObject> getChildren(BusinessObject object, BusinessEdgeType type)
  {
    List<VertexObject> vertexObjects = object.getVertex().getChildren(type.getMdEdgeDAO(), VertexObject.class);

    return vertexObjects.stream().map(vertex -> {
      MdVertexDAOIF mdVertex = (MdVertexDAOIF) vertex.getMdClass();
      BusinessType businessType = this.typeService.getByMdVertex(mdVertex);

      return new BusinessObject(vertex, businessType);

    }).sorted((a, b) -> {
      return a.getLabel().compareTo(b.getLabel());
    }).collect(Collectors.toList());
  }

  @Override
  public BusinessObject newInstance(BusinessType type)
  {
    VertexObject vertex = VertexObject.instantiate(VertexObjectDAO.newInstance(type.getMdVertexDAO()));

    return new BusinessObject(vertex, type);
  }

  @Override
  public BusinessObject get(BusinessType type, String attributeName, Object value)
  {
    MdVertexDAOIF mdVertex = type.getMdVertexDAO();
    MdAttributeDAOIF mdAttribute = mdVertex.definesAttribute(attributeName);

    StringBuilder statement = new StringBuilder();
    statement.append("SELECT FROM " + mdVertex.getDBClassName());
    statement.append(" WHERE " + mdAttribute.getColumnName() + " = :" + attributeName);

    GraphQuery<VertexObject> query = new GraphQuery<VertexObject>(statement.toString());
    query.setParameter(attributeName, value);

    VertexObject result = query.getSingleResult();

    if (result != null)
    {
      return new BusinessObject(result, type);
    }

    return null;
  }

  @Override
  public BusinessObject getByCode(BusinessType type, Object value)
  {
    return this.get(type, DefaultAttribute.CODE.getName(), value);
  }

}
