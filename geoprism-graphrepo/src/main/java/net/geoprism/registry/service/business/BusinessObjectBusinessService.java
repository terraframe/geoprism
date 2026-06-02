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
import java.util.Optional;
import java.util.stream.Collectors;

import org.commongeoregistry.adapter.constants.DefaultAttribute;
import org.commongeoregistry.adapter.metadata.AttributeClassificationType;
import org.commongeoregistry.adapter.metadata.AttributeDataSourceType;
import org.commongeoregistry.adapter.metadata.AttributeTermType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;
import com.runwaysdk.business.graph.EdgeObject;
import com.runwaysdk.business.graph.GraphQuery;
import com.runwaysdk.business.graph.VertexObject;
import com.runwaysdk.dataaccess.MdAttributeBooleanDAOIF;
import com.runwaysdk.dataaccess.MdAttributeClassificationDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDateDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDecimalDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDoubleDAOIF;
import com.runwaysdk.dataaccess.MdAttributeGraphReferenceDAOIF;
import com.runwaysdk.dataaccess.MdAttributeLongDAOIF;
import com.runwaysdk.dataaccess.MdAttributeNumberDAOIF;
import com.runwaysdk.dataaccess.MdAttributeTermDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdClassificationDAOIF;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.graph.VertexObjectDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;

import net.geoprism.configuration.GeoprismProperties;
import net.geoprism.ontology.Classifier;
import net.geoprism.registry.BusinessEdgeType;
import net.geoprism.registry.BusinessType;
import net.geoprism.registry.DateFormatter;
import net.geoprism.registry.OriginException;
import net.geoprism.registry.graph.DataSource;
import net.geoprism.registry.model.BusinessObject;
import net.geoprism.registry.model.ClassificationType;
import net.geoprism.registry.model.EdgeConstant;
import net.geoprism.registry.model.EdgeType;
import net.geoprism.registry.model.graph.VertexComponent;
import net.geoprism.registry.model.graph.VertexServerGeoObject;

@Service
public class BusinessObjectBusinessService implements BusinessObjectBusinessServiceIF
{
  @Autowired
  private BusinessTypeBusinessServiceIF       typeService;

  @Autowired
  private ClassificationBusinessServiceIF     classificationService;

  @Autowired
  private ClassificationTypeBusinessServiceIF classificationTypeService;

  @Autowired
  private DataSourceBusinessServiceIF         sourceService;

  @Override
  public JsonObject toJSON(BusinessObject object)
  {
    JsonObject data = new JsonObject();

    BusinessType type = object.getType();

    type.getAttributeMap().values().stream() //
        .filter(attribute -> !attribute.getName().equals(BusinessObject.CODE)) //
        .forEach(attribute -> {

          String attributeName = attribute.getName();

          Object value = object.getVertex().getObjectValue(attributeName);

          if (value != null)
          {
            if (attribute instanceof AttributeTermType)
            {
              Classifier classifier = Classifier.get((String) value);

              data.addProperty(attributeName, classifier.getDisplayLabel().getValue());
            }
            else if (attribute instanceof AttributeClassificationType)
            {
              ClassificationType classificationType = this.classificationTypeService.getByCode( ( (AttributeClassificationType) attribute ).getClassificationType(), true);

              this.classificationService.getByOid(classificationType, (String) value).ifPresent(classification -> {
                data.addProperty(attributeName, classification.getCode());
              });
            }
            else if (attribute instanceof AttributeDataSourceType)
            {
              DataSource dataSource = this.sourceService.get((String) value);

              if (dataSource != null)
              {
                data.addProperty(attributeName, dataSource.getCode());
              }
            }
            else if (value instanceof Number)
            {
              data.addProperty(attributeName, (Number) value);
            }
            else if (value instanceof Boolean)
            {
              data.addProperty(attributeName, (Boolean) value);
            }
            else if (value instanceof String)
            {
              data.addProperty(attributeName, (String) value);
            }
            else if (value instanceof Character)
            {
              data.addProperty(attributeName, (Character) value);
            }
            else if (value instanceof Date)
            {
              data.addProperty(attributeName, DateFormatter.formatDate((Date) value, false));
            }
            else
            {
              throw new UnsupportedOperationException();
            }
          }
        });

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
        else if (mdAttribute instanceof MdAttributeClassificationDAOIF)
        {
          String code = data.get(attributeName).getAsString();

          MdClassificationDAOIF mdClassification = ( (MdAttributeClassificationDAOIF) mdAttribute ).getMdClassificationDAOIF();

          ClassificationType type = new ClassificationType(mdClassification);

          this.classificationService.getByCode(type, code).ifPresent(classification -> {
            object.setValue(attributeName, classification.getVertex());
          });
        }
        else if (mdAttribute instanceof MdAttributeGraphReferenceDAOIF)
        {
          MdClassDAOIF mdVertex = ( (MdAttributeGraphReferenceDAOIF) mdAttribute ).getReferenceMdVertexDAOIF();

          if (mdVertex.definesType().equals(DataSource.CLASS))
          {
            String code = data.get(attributeName).getAsString();

            this.sourceService.getByCode(code).ifPresent(dataSource -> {
              object.setValue(attributeName, dataSource);
            });
          }
          else
          {
            throw new UnsupportedOperationException();
          }
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
  public boolean exists(VertexComponent object, BusinessEdgeType edgeType, VertexComponent parent)
  {
    return getEdge(object, edgeType, parent) != null;
  }

  protected EdgeObject getEdge(VertexComponent object, BusinessEdgeType edgeType, VertexComponent parent)
  {
    String statement = "SELECT FROM " + edgeType.getMdEdgeDAO().getDBClassName();
    statement += " WHERE out = :parent";
    statement += " AND in = :child";

    GraphQuery<EdgeObject> query = new GraphQuery<EdgeObject>(statement);
    query.setParameter("parent", parent.getVertex().getRID());
    query.setParameter("child", object.getVertex().getRID());

    return query.getSingleResult();
  }

  @Override
  public boolean exists(BusinessEdgeType type, VertexComponent parent, VertexComponent child, Date startDate, Date endDate)
  {
    return getEdge(type, parent, child, startDate, endDate) != null;
  }

  protected EdgeObject getEdge(BusinessEdgeType type, VertexComponent parent, VertexComponent child, Date startDate, Date endDate)
  {
    String statement = "SELECT FROM " + type.getMdEdgeDAO().getDBClassName();
    statement += " WHERE out = :parent";
    statement += " AND in = :child";
    statement += " AND startDate = :startDate";
    statement += " AND endDate = :endDate";

    GraphQuery<EdgeObject> query = new GraphQuery<EdgeObject>(statement);
    query.setParameter("parent", parent.getVertex().getRID());
    query.setParameter("child", child.getVertex().getRID());
    query.setParameter("startDate", startDate);
    query.setParameter("endDate", endDate);

    return query.getSingleResult();
  }

  @Override
  public boolean exists(BusinessEdgeType type, String uid)
  {
    String statement = "SELECT FROM " + type.getMdEdgeDAO().getDBClassName();
    statement += " WHERE uid = :uid";

    GraphQuery<EdgeObject> query = new GraphQuery<EdgeObject>(statement);
    query.setParameter("uid", uid);

    return ( query.getSingleResult() != null );
  }

  @Override
  public Optional<EdgeObject> addParent(VertexComponent object, BusinessEdgeType type, VertexComponent parent, String uid, Date startDate, Date endDate, DataSource source)
  {
    return this.addParent(object, type, parent, uid, startDate, endDate, source, true);
  }

  @Override
  public Optional<EdgeObject> addParent(VertexComponent object, BusinessEdgeType type, VertexComponent parent, String uid, Date startDate, Date endDate, DataSource source, boolean validateOrigin)
  {
    if (validateOrigin)
    {
      if (!type.getOrigin().equals(GeoprismProperties.getOrigin()))
      {
        throw new OriginException();
      }
    }

    if (parent != null && !this.exists(object, type, parent))
    {
      EdgeObject newEdge = object.getVertex().addParent(parent.getVertex(), type.getMdEdgeDAO());
      newEdge.setValue(DefaultAttribute.UID.getName(), uid);
      newEdge.setValue(DefaultAttribute.DATA_SOURCE.getName(), source);
      newEdge.setValue(EdgeType.START_DATE, startDate);
      newEdge.setValue(EdgeType.END_DATE, endDate);
      newEdge.apply();

      return Optional.of(newEdge);
    }

    return Optional.empty();
  }

  @Override
  public void removeParent(VertexComponent object, BusinessEdgeType type, VertexComponent parent, Date startDate, Date endDate)
  {
    this.removeParent(object, type, parent, startDate, endDate, true);
  }

  @Override
  public void removeParent(VertexComponent object, BusinessEdgeType type, VertexComponent parent, Date startDate, Date endDate, boolean validateOrigin)
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
      EdgeObject edge = this.getEdge(type, parent, object, startDate, endDate);

      if (edge != null)
      {
        edge.delete();
      }
    }
  }

  @Override
  public List<VertexComponent> getParents(BusinessObject object, BusinessEdgeType type, Date date)
  {
    if (type.getIsParentGeoObject())
    {
      StringBuilder statement = new StringBuilder();
      statement.append("TRAVERSE out('" + EdgeConstant.HAS_VALUE.getDBClassName() + "', '" + EdgeConstant.HAS_GEOMETRY.getDBClassName() + "') FROM (");
      statement.append("  SELECT EXPAND(inE('" + type.getMdEdge().getDbClassName() + "')[:date BETWEEN startDate AND endDate].out)");
      statement.append("  FROM :rid " + "\n");
      statement.append(")");

      GraphQuery<VertexObject> query = new GraphQuery<VertexObject>(statement.toString());
      query.setParameter("rid", object.getVertex().getRID());
      query.setParameter("date", date);

      return VertexServerGeoObject.processTraverseResults(query.getResults(), date).stream().map(s -> (VertexComponent) s).toList();
    }

    StringBuilder statement = new StringBuilder();
    statement.append("SELECT EXPAND(inE('" + type.getMdEdge().getDbClassName() + "')[:date BETWEEN startDate AND endDate].out)");
    statement.append(" FROM :rid " + "\n");

    GraphQuery<VertexObject> query = new GraphQuery<VertexObject>(statement.toString());
    query.setParameter("rid", object.getVertex().getRID());
    query.setParameter("date", date);

    List<VertexObject> results = query.getResults();

    return results.stream().map(vertex -> {
      MdVertexDAOIF mdVertex = (MdVertexDAOIF) vertex.getMdClass();
      BusinessType businessType = this.typeService.getByMdVertex(mdVertex);

      return new BusinessObject(vertex, businessType);

    }).sorted((a, b) -> {
      return a.getLabel().compareTo(b.getLabel());
    }).collect(Collectors.toList());
  }

  @Override
  public Optional<EdgeObject> addChild(VertexComponent object, BusinessEdgeType type, VertexComponent child, String uid, Date startDate, Date endDate, DataSource source)
  {
    return this.addChild(object, type, child, uid, startDate, endDate, source, true);
  }

  @Override
  public Optional<EdgeObject> addChild(VertexComponent object, BusinessEdgeType type, VertexComponent child, String uid, Date startDate, Date endDate, DataSource source, boolean validateOrigin)
  {
    if (validateOrigin)
    {
      if (!type.getOrigin().equals(GeoprismProperties.getOrigin()))
      {
        throw new OriginException();
      }
    }

    if (child != null && !this.exists(child, type, object))
    {
      EdgeObject newEdge = object.getVertex().addChild(child.getVertex(), type.getMdEdgeDAO());
      newEdge.setValue(DefaultAttribute.UID.getName(), uid);
      newEdge.setValue(DefaultAttribute.DATA_SOURCE.getName(), source);
      newEdge.setValue(EdgeType.START_DATE, startDate);
      newEdge.setValue(EdgeType.END_DATE, endDate);
      newEdge.apply();

      return Optional.of(newEdge);
    }

    return Optional.empty();
  }

  @Override
  public void removeChild(VertexComponent object, BusinessEdgeType type, VertexComponent child, Date startDate, Date endDate)
  {
    this.removeChild(object, type, child, startDate, endDate, true);
  }

  @Override
  public void removeChild(VertexComponent object, BusinessEdgeType type, VertexComponent child, Date startDate, Date endDate, boolean validateOrigin)
  {
    if (validateOrigin)
    {
      if (!type.getOrigin().equals(GeoprismProperties.getOrigin()))
      {
        throw new OriginException();
      }
    }

    if (child != null)
    {
      EdgeObject edge = this.getEdge(type, object, child, startDate, endDate);

      if (edge != null)
      {
        edge.delete();
      }
    }
  }

  @Override
  public List<VertexComponent> getChildren(BusinessObject object, BusinessEdgeType type, Date date)
  {
    if (type.getIsChildGeoObject())
    {
      StringBuilder statement = new StringBuilder();
      statement.append("TRAVERSE out('" + EdgeConstant.HAS_VALUE.getDBClassName() + "', '" + EdgeConstant.HAS_GEOMETRY.getDBClassName() + "') FROM (");
      statement.append("  SELECT EXPAND(outE('" + type.getMdEdge().getDbClassName() + "')[:date BETWEEN startDate AND endDate].in)");
      statement.append("  FROM :rid " + "\n");
      statement.append(")");

      GraphQuery<VertexObject> query = new GraphQuery<VertexObject>(statement.toString());
      query.setParameter("rid", object.getVertex().getRID());
      query.setParameter("date", date);

      return VertexServerGeoObject.processTraverseResults(query.getResults(), date).stream().map(s -> (VertexComponent) s).toList();
    }

    StringBuilder statement = new StringBuilder();
    statement.append("SELECT EXPAND(outE('" + type.getMdEdge().getDbClassName() + "')[:date BETWEEN startDate AND endDate].in)");
    statement.append(" FROM :rid " + "\n");

    GraphQuery<VertexObject> query = new GraphQuery<VertexObject>(statement.toString());
    query.setParameter("rid", object.getVertex().getRID());
    query.setParameter("date", date);

    List<VertexObject> results = query.getResults();

    return results.stream().map(vertex -> {
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
