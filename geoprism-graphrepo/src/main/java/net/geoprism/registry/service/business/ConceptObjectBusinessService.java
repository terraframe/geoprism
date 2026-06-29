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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.commongeoregistry.adapter.constants.DefaultAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;
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
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.graph.VertexObjectDAO;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;

import net.geoprism.configuration.GeoprismProperties;
import net.geoprism.ontology.Classifier;
import net.geoprism.registry.DateFormatter;
import net.geoprism.registry.OriginException;
import net.geoprism.registry.graph.AttributeClassificationType;
import net.geoprism.registry.graph.AttributeDataSourceType;
import net.geoprism.registry.graph.AttributeValue;
import net.geoprism.registry.graph.ConceptClass;
import net.geoprism.registry.graph.ConceptVertex;
import net.geoprism.registry.graph.DataSource;
import net.geoprism.registry.model.ClassificationType;
import net.geoprism.registry.model.ConceptObject;
import net.geoprism.registry.model.EdgeConstant;

@Service
public class ConceptObjectBusinessService implements ConceptObjectBusinessServiceIF
{
  @Autowired
  private ConceptClassBusinessServiceIF       typeService;

  @Autowired
  private ClassificationBusinessServiceIF     classificationService;

  @Autowired
  private ClassificationTypeBusinessServiceIF classificationTypeService;

  @Autowired
  private DataSourceBusinessServiceIF         sourceService;

  @Override
  public JsonObject toJSON(ConceptObject object)
  {
    JsonObject data = new JsonObject();

    ConceptClass type = object.getType();

    type.getAttributeMap().values().stream() //
        .filter(attribute -> !attribute.getCode().equals(ConceptObject.CODE)) //
        .forEach(attribute -> {

          String attributeName = attribute.getCode();

          Object value = object.getValue(attributeName);

          if (value != null)
          {
            if (attribute instanceof AttributeClassificationType)
            {
              ClassificationType classificationType = this.classificationTypeService.getByCode( ( (AttributeClassificationType) attribute ).getClassificationType().getCode(), true);

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
    json.add("data", data);

    return json;
  }

  @Override
  public ConceptObject newInstance(ConceptClass type, JsonObject json)
  {
    ConceptObject object = this.newInstance(type);

    populate(object, json);

    return object;
  }

  @Override
  public void populate(ConceptObject object, JsonObject json)
  {
    object.setCode(json.get("code").getAsString());

    JsonObject data = json.get("data").getAsJsonObject();

    object.getType().getMdVertexDAO().definesAttributes().stream().filter(mdAttribute -> {
      String attributeName = mdAttribute.definesAttribute();

      return !attributeName.equals(ConceptObject.CODE) && !mdAttribute.isSystem();
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
  public void apply(ConceptObject object)
  {
    apply(object, true);
  }

  @Override
  @Transaction
  public void apply(ConceptObject object, boolean validateOrigin)
  {
    if (validateOrigin)
    {
      ConceptClass type = object.getType();

      if (!type.getOrigin().equals(GeoprismProperties.getOrigin()))
      {
        throw new OriginException();
      }
    }

    object.getVertex().apply();
  }

  @Override
  @Transaction
  public void delete(ConceptObject object)
  {
    this.delete(object, true);
  }

  @Override
  public void delete(ConceptObject object, boolean validateOrigin)
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
  public ConceptObject newInstance(ConceptClass type)
  {
    VertexObject vertex = VertexObject.instantiate(VertexObjectDAO.newInstance(type.getMdVertexDAO()));

    return new ConceptObject(type, vertex, new TreeMap<>());
  }

  @Override
  public ConceptObject get(ConceptClass type, String attributeName, Object value)
  {
    MdVertexDAOIF mdVertex = type.getMdVertexDAO();
    MdAttributeDAOIF mdAttribute = mdVertex.definesAttribute(attributeName);

    StringBuilder statement = new StringBuilder();
    statement.append("TRAVERSE out('" + EdgeConstant.HAS_VALUE.getDBClassName() + "', '" + EdgeConstant.HAS_GEOMETRY.getDBClassName() + "') FROM (");
    statement.append("  SELECT FROM " + mdVertex.getDBClassName());
    statement.append("  WHERE " + mdAttribute.getColumnName() + " = :" + attributeName);
    statement.append(")");

    GraphQuery<VertexObject> query = new GraphQuery<VertexObject>(statement.toString());
    query.setParameter(attributeName, value);

    return this.processSingleResult(query.getResults(), null);
  }

  @Override
  public ConceptObject getByCode(ConceptClass type, Object value)
  {
    return this.get(type, DefaultAttribute.CODE.getName(), value);
  }

  public List<ConceptObject> processTraverseResults(List<VertexObject> results, Date date)
  {
    MdVertexDAOIF superVertex = MdVertexDAO.getMdVertexDAO(ConceptVertex.CLASS);
    List<ConceptObject> list = new LinkedList<ConceptObject>();
    
    VertexObject current = null;
    List<VertexObject> currentAttributes = new LinkedList<>();

    for (VertexObject result : results)
    {
      MdVertexDAOIF mdClass = (MdVertexDAOIF) result.getMdClass();
      List<? extends MdVertexDAOIF> superClasses = mdClass.getSuperClasses();
      if (superClasses.contains(superVertex))
      {
        if (current != null)
        {
          ConceptClass type = this.typeService.getByMdVertex((MdVertexDAOIF) current.getMdClass());

          Map<String, List<VertexObject>> nodeMap = currentAttributes.stream().collect(Collectors.groupingBy(v -> {
            return (String) v.getObjectValue(AttributeValue.ATTRIBUTENAME);
          }));

          ConceptObject bObject = new ConceptObject(type, current, nodeMap, date);
          list.add(bObject);
        }

        current = result;
        currentAttributes = new LinkedList<>();
      }
      else
      {
        currentAttributes.add(result);
      }
    }

    if (current != null)
    {
      ConceptClass type = this.typeService.getByMdVertex((MdVertexDAOIF) current.getMdClass());

      Map<String, List<VertexObject>> nodeMap = currentAttributes.stream().collect(Collectors.groupingBy(v -> {
        return (String) v.getObjectValue(AttributeValue.ATTRIBUTENAME);
      }));

      ConceptObject vsgo = new ConceptObject(type, current, nodeMap, date);
      list.add(vsgo);
    }

    return list;
  }

  public ConceptObject processSingleResult(List<VertexObject> list, Date date)
  {
    List<ConceptObject> results = this.processTraverseResults(list, date);

    if (results.size() == 0)
    {
      return null;
    }
    else if (results.size() == 1)
    {
      return results.get(0);
    }
    else
    {
      throw new ProgrammingErrorException("Multiple results were returned when only one is allowed");
    }
  }

}
