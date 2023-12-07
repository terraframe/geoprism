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

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.commongeoregistry.adapter.constants.DefaultAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;
import com.runwaysdk.business.graph.EdgeObject;
import com.runwaysdk.business.graph.GraphQuery;
import com.runwaysdk.business.graph.VertexObject;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeTermDAOIF;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.graph.VertexObjectDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;

import net.geoprism.ontology.Classifier;
import net.geoprism.registry.BusinessEdgeType;
import net.geoprism.registry.BusinessType;
import net.geoprism.registry.DateFormatter;
import net.geoprism.registry.model.BusinessObject;
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
  @Transaction
  public void apply(BusinessObject object)
  {
    object.getVertex().apply();
  }

  @Override
  @Transaction
  public void delete(BusinessObject object)
  {
    object.getVertex().delete();
  }

  @Override
  public boolean exists(BusinessObject object, ServerGeoObjectIF geoObject)
  {
    if (geoObject != null && geoObject instanceof VertexServerGeoObject)
    {
      return getEdge(object, geoObject) != null;
    }

    return false;
  }

  protected EdgeObject getEdge(BusinessObject object, ServerGeoObjectIF geoObject)
  {
    VertexObject geoVertex = ( (VertexServerGeoObject) geoObject ).getVertex();

    String statement = "SELECT FROM " + object.getType().getMdEdgeDAO().getDBClassName();
    statement += " WHERE out = :parent";
    statement += " AND in = :child";

    GraphQuery<EdgeObject> query = new GraphQuery<EdgeObject>(statement);
    query.setParameter("parent", geoVertex.getRID());
    query.setParameter("child", object.getVertex().getRID());

    return query.getSingleResult();
  }

  @Override
  public void addGeoObject(BusinessObject object, ServerGeoObjectIF geoObject)
  {
    if (geoObject != null && geoObject instanceof VertexServerGeoObject && !this.exists(object, geoObject))
    {
      VertexObject geoVertex = ( (VertexServerGeoObject) geoObject ).getVertex();

      geoVertex.addChild(object.getVertex(), object.getType().getMdEdgeDAO()).apply();
    }
  }

  @Override
  public void removeGeoObject(BusinessObject object, ServerGeoObjectIF geoObject)
  {
    if (geoObject != null && geoObject instanceof VertexServerGeoObject)
    {
      VertexObject geoVertex = ( (VertexServerGeoObject) geoObject ).getVertex();

      geoVertex.removeChild(object.getVertex(), object.getType().getMdEdgeDAO());
    }
  }

  @Override
  public List<VertexServerGeoObject> getGeoObjects(BusinessObject object)
  {
    List<VertexObject> geoObjects = object.getVertex().getParents(object.getType().getMdEdgeDAO(), VertexObject.class);

    return geoObjects.stream().map(geoVertex -> {
      MdVertexDAOIF mdVertex = (MdVertexDAOIF) geoVertex.getMdClass();
      ServerGeoObjectType vertexType = ServerGeoObjectType.get(mdVertex);

      return new VertexServerGeoObject(vertexType, geoVertex);

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
  public void addParent(BusinessObject object, BusinessEdgeType type, BusinessObject parent)
  {
    if (parent != null && !this.exists(type, parent, object))
    {
      object.getVertex().addParent(parent.getVertex(), type.getMdEdgeDAO()).apply();
    }
  }

  @Override
  public void removeParent(BusinessObject object, BusinessEdgeType type, BusinessObject parent)
  {
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
  public void addChild(BusinessObject object, BusinessEdgeType type, BusinessObject child)
  {
    if (child != null && !this.exists(type, object, child))
    {
      object.getVertex().addChild(child.getVertex(), type.getMdEdgeDAO()).apply();
    }
  }

  @Override
  public void removeChild(BusinessObject object, BusinessEdgeType type, BusinessObject child)
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
