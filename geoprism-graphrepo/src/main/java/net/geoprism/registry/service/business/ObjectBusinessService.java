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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.commongeoregistry.adapter.constants.DefaultAttribute;
import org.commongeoregistry.adapter.dataaccess.LocalizedValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;
import com.runwaysdk.business.graph.GraphQuery;
import com.runwaysdk.business.graph.VertexObject;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.graph.attributes.ValueOverTime;
import com.runwaysdk.dataaccess.graph.attributes.ValueOverTimeCollection;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;

import net.geoprism.configuration.GeoprismProperties;
import net.geoprism.registry.DateFormatter;
import net.geoprism.registry.OriginException;
import net.geoprism.registry.graph.AttributeBooleanType;
import net.geoprism.registry.graph.AttributeCharacterType;
import net.geoprism.registry.graph.AttributeDataSourceType;
import net.geoprism.registry.graph.AttributeDateType;
import net.geoprism.registry.graph.AttributeDoubleType;
import net.geoprism.registry.graph.AttributeLocalType;
import net.geoprism.registry.graph.AttributeLongType;
import net.geoprism.registry.graph.AttributeType;
import net.geoprism.registry.graph.AttributeValue;
import net.geoprism.registry.graph.DataSource;
import net.geoprism.registry.graph.ObjectClass;
import net.geoprism.registry.model.EdgeConstant;
import net.geoprism.registry.model.graph.ServerObjectVertex;
import net.geoprism.registry.query.graph.ObjectPageQuery;
import net.geoprism.registry.view.AttributeBooleanDTO;
import net.geoprism.registry.view.AttributeCharacterDTO;
import net.geoprism.registry.view.AttributeDataSourceDTO;
import net.geoprism.registry.view.AttributeDateDTO;
import net.geoprism.registry.view.AttributeFloatDTO;
import net.geoprism.registry.view.AttributeIntegerDTO;
import net.geoprism.registry.view.AttributeLocalDTO;
import net.geoprism.registry.view.JsonSerializable;
import net.geoprism.registry.view.ObjectAtTimeDTO;
import net.geoprism.registry.view.ObjectClassDTO;
import net.geoprism.registry.view.ObjectOverTimeDTO;
import net.geoprism.registry.view.Page;
import net.geoprism.registry.view.ValueOverTimeEntryDTO;

@Service
public abstract class ObjectBusinessService<V extends ServerObjectVertex, T extends ObjectClass, D extends ObjectClassDTO> implements ObjectBusinessServiceIF<V, T, D>
{
  private ObjectClassBusinessServiceIF<T, D> typeService;

  private String                             baseVertexClass;

  @Autowired
  private DataSourceBusinessServiceIF        sourceService;

  public ObjectBusinessService(ObjectClassBusinessServiceIF<T, D> typeService, String baseVertexClass)
  {
    this.typeService = typeService;
    this.baseVertexClass = baseVertexClass;
  }

  protected abstract V build(T type, VertexObject current, Map<String, List<VertexObject>> nodeMap, Date date);

  protected ObjectClassBusinessServiceIF<T, D> getTypeService()
  {
    return typeService;
  }

  @Override
  public ObjectOverTimeDTO toDTO(V object)
  {
    T type = object.getType();

    ObjectOverTimeDTO dto = new ObjectOverTimeDTO();
    dto.setCode(object.getCode());
    dto.setType(type.getTypeInfo());

    type.getAttributeMap().values().stream() //
        .forEach(attribute -> {

          String attributeName = attribute.getCode();

          if (attribute.getIsChangeOverTime())
          {
            ValueOverTimeCollection collection = object.getValuesOverTime(attributeName);

            if (attribute instanceof AttributeBooleanType)
            {
              List<ValueOverTimeEntryDTO<Boolean>> entries = collection.stream() //
                  .map(vot -> ValueOverTimeEntryDTO.of(vot.getOid(), vot.getStartDate(), vot.getEndDate(), (Boolean) vot.getValue())).toList();

              dto.put(attributeName, AttributeBooleanDTO.of(entries));
            }
            else if (attribute instanceof AttributeCharacterType)
            {
              List<ValueOverTimeEntryDTO<String>> entries = collection.stream() //
                  .map(vot -> ValueOverTimeEntryDTO.of(vot.getOid(), vot.getStartDate(), vot.getEndDate(), (String) vot.getValue())).toList();

              dto.put(attributeName, AttributeCharacterDTO.of(entries));
            }
            else if (attribute instanceof AttributeDataSourceType)
            {
              List<ValueOverTimeEntryDTO<String>> entries = collection.stream().map(vot -> {
                DataSource dataSource = this.sourceService.get((String) vot.getValue());

                if (dataSource == null)
                {
                  // TODO throw better exception
                  throw new UnsupportedOperationException();
                }

                return ValueOverTimeEntryDTO.of(vot.getOid(), vot.getStartDate(), vot.getEndDate(), dataSource.getCode());

              }).toList();

              dto.put(attributeName, AttributeDataSourceDTO.of(entries));
            }
            else if (attribute instanceof AttributeDateType)
            {
              List<ValueOverTimeEntryDTO<Date>> entries = collection.stream() //
                  .map(vot -> ValueOverTimeEntryDTO.of(vot.getOid(), vot.getStartDate(), vot.getEndDate(), (Date) vot.getValue())).toList();

              dto.put(attributeName, AttributeDateDTO.of(entries));
            }
            else if (attribute instanceof AttributeDoubleType)
            {
              List<ValueOverTimeEntryDTO<Double>> entries = collection.stream() //
                  .map(vot -> ValueOverTimeEntryDTO.of(vot.getOid(), vot.getStartDate(), vot.getEndDate(), (Double) vot.getValue())).toList();

              dto.put(attributeName, AttributeFloatDTO.of(entries));
            }
            else if (attribute instanceof AttributeLongType)
            {
              List<ValueOverTimeEntryDTO<Long>> entries = collection.stream() //
                  .map(vot -> ValueOverTimeEntryDTO.of(vot.getOid(), vot.getStartDate(), vot.getEndDate(), (Long) vot.getValue())).toList();

              dto.put(attributeName, AttributeIntegerDTO.of(entries));
            }
            else if (attribute instanceof AttributeLocalType)
            {
              List<ValueOverTimeEntryDTO<LocalizedValue>> entries = collection.stream() //
                  .map(vot -> ValueOverTimeEntryDTO.of(vot.getOid(), vot.getStartDate(), vot.getEndDate(), (LocalizedValue) vot.getValue())).toList();

              dto.put(attributeName, AttributeLocalDTO.of(entries));
            }
            else
            {
              throw new UnsupportedOperationException();
            }
          }
          else
          {
            Object value = object.getValue(attributeName);

            if (value != null)
            {
              if (attribute instanceof AttributeBooleanType)
              {
                dto.put(attributeName, AttributeBooleanDTO.of((Boolean) value));
              }
              else if (attribute instanceof AttributeCharacterType)
              {
                dto.put(attributeName, AttributeCharacterDTO.of((String) value));
              }
              else if (attribute instanceof AttributeDataSourceType)
              {
                DataSource dataSource = this.sourceService.get((String) value);

                if (dataSource == null)
                {
                  // TODO throw better exception
                  throw new UnsupportedOperationException();
                }

                dto.put(attributeName, AttributeDataSourceDTO.of(dataSource.getCode()));
              }
              else if (attribute instanceof AttributeDateType)
              {
                dto.put(attributeName, AttributeDateDTO.of((Date) value));
              }
              else if (attribute instanceof AttributeDoubleType)
              {
                dto.put(attributeName, AttributeFloatDTO.of((Double) value));
              }
              else if (attribute instanceof AttributeLongType)
              {
                dto.put(attributeName, AttributeIntegerDTO.of((Long) value));
              }
              else if (attribute instanceof AttributeLocalType)
              {
                dto.put(attributeName, AttributeLocalDTO.of((LocalizedValue) value));
              }
              else
              {
                throw new UnsupportedOperationException();
              }
            }

          }

        });

    return dto;
  }

  @Override
  public ObjectAtTimeDTO toDTO(V object, Date date)
  {
    T type = object.getType();

    ObjectAtTimeDTO dto = new ObjectAtTimeDTO();
    dto.setCode(object.getCode());
    dto.setDate(date);
    dto.setType(type.getTypeInfo());

    type.getAttributeMap().values().stream() //
        .forEach(attribute -> {

          String attributeName = attribute.getCode();

          Object value = attribute.getIsChangeOverTime() ? object.getValue(attributeName, date) : object.getValue(attributeName);

          if (value != null)
          {
            if (attribute instanceof AttributeDataSourceType)
            {
              DataSource dataSource = this.sourceService.get((String) value);

              if (dataSource != null)
              {
                dto.setValue(attributeName, dataSource.getCode());
              }
            }
            else
            {
              dto.setValue(attributeName, value);
            }
          }
        });

    return dto;
  }

  @Override
  public V newInstance(T type, ObjectOverTimeDTO dto)
  {
    V object = this.newInstance(type);

    populate(object, dto);

    return object;
  }

  @Override
  public void populate(V object, ObjectOverTimeDTO dto)
  {
    T type = object.getType();
    List<AttributeType> attributes = type.getAttributes();

    attributes.stream().filter(attribute -> {
      String attributeName = attribute.getCode();

      return !attributeName.equals(DefaultAttribute.CODE.getName());
    }).forEach(attribute -> {
      String attributeName = attribute.getCode();

      if (attribute.getIsChangeOverTime())
      {
        ValueOverTimeCollection c = new ValueOverTimeCollection();

        if (attribute instanceof AttributeDataSourceType)
        {
          List<ValueOverTimeEntryDTO<String>> entries = dto.has(attributeName) ? dto.getValuesOverTime(attributeName) : new LinkedList<>();

          for (ValueOverTimeEntryDTO<String> votDTO : entries)
          {
            DataSource dataSource = this.sourceService.getByCode((String) votDTO.getValue()).orElseThrow(() -> {
              throw new ProgrammingErrorException("Unable to find source with code [" + votDTO.getValue() + "]");
            });

            c.add(new ValueOverTime(votDTO.getOid(), votDTO.getStartDate(), votDTO.getEndDate(), dataSource));
          }
        }
        else if (attribute instanceof AttributeBooleanType //
            || attribute instanceof AttributeCharacterType //
            || attribute instanceof AttributeDateType //
            || attribute instanceof AttributeDoubleType //
            || attribute instanceof AttributeLongType //
            || attribute instanceof AttributeLocalType)
        {
          List<ValueOverTimeEntryDTO<Object>> entries = dto.has(attributeName) ? dto.getValuesOverTime(attributeName) : new LinkedList<>();

          for (ValueOverTimeEntryDTO<Object> votDTO : entries)
          {
            c.add(new ValueOverTime(votDTO.getOid(), votDTO.getStartDate(), votDTO.getEndDate(), votDTO.getValue()));
          }
        }
        else
        {
          throw new UnsupportedOperationException();
        }

        object.setValuesOverTime(attributeName, c);

      }
      else
      {
        Object value = dto.has(attributeName) ? dto.getValue(attributeName) : null;

        if (value == null)
        {
          object.setValue(attributeName, null);
        }
        else if (attribute instanceof AttributeDataSourceType)
        {
          DataSource dataSource = this.sourceService.getByCode((String) value).orElseThrow(() -> {
            throw new ProgrammingErrorException("Unable to find source with code [" + value + "]");
          });

          object.setValue(attributeName, dataSource);
        }
        else if (attribute instanceof AttributeBooleanType //
            || attribute instanceof AttributeCharacterType //
            || attribute instanceof AttributeDateType //
            || attribute instanceof AttributeDoubleType //
            || attribute instanceof AttributeLongType //
            || attribute instanceof AttributeLocalType)
        {
          object.setValue(attributeName, dto.getValue(attributeName));
        }
        else
        {
          throw new UnsupportedOperationException();
        }

      }
    });

    object.setCode(dto.getCode());

  }

  @Override
  public void populate(V object, ObjectAtTimeDTO dto, Date startDate, Date endDate)
  {
    T type = object.getType();

    List<AttributeType> attributes = type.getAttributes();

    attributes.stream().filter(attribute -> {
      String attributeName = attribute.getCode();

      return !attributeName.equals(DefaultAttribute.CODE.getName());
    }).forEach(attribute -> {
      String attributeName = attribute.getCode();

      Object value = dto.getValue(attributeName);

      if (value != null && attribute instanceof AttributeDateType)
      {
        value = DateFormatter.parseDate((String) value);
      }
      else if (value != null && attribute instanceof AttributeDataSourceType)
      {
        value = this.sourceService.getByCode((String) value).orElse(null);
      }

      if (attribute.getIsChangeOverTime())
      {
        object.setValue(attributeName, value, startDate, endDate);
      }
      else
      {
        object.setValue(attributeName, value);
      }
    });

    object.setCode(dto.getCode());
  }

  @Override
  @Transaction
  public void apply(V object)
  {
    apply(object, true);
  }

  @Override
  @Transaction
  public void apply(V object, boolean validateOrigin)
  {
    if (validateOrigin)
    {
      T type = object.getType();

      if (!type.getOrigin().equals(GeoprismProperties.getOrigin()))
      {
        throw new OriginException();
      }
    }

    object.apply();
  }

  @Override
  @Transaction
  public void delete(V object)
  {
    this.delete(object, true);
  }

  @Override
  public void delete(V object, boolean validateOrigin)
  {
    if (validateOrigin)
    {
      if (!object.getType().getOrigin().equals(GeoprismProperties.getOrigin()))
      {
        throw new OriginException();
      }
    }

    object.delete();
  }

  @Override
  public V get(T type, String attributeName, Object value)
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
  public V getByCode(T type, Object value)
  {
    return this.get(type, DefaultAttribute.CODE.getName(), value);
  }

  @Override
  public List<V> getAll(T type, Long skip, Integer limit)
  {
    MdVertexDAOIF mdVertex = type.getMdVertexDAO();
    MdAttributeDAOIF mdAttribute = mdVertex.definesAttribute(DefaultAttribute.CODE.getName());

    StringBuilder statement = new StringBuilder();
    statement.append("TRAVERSE out('" + EdgeConstant.HAS_VALUE.getDBClassName() + "') FROM (");
    statement.append("  SELECT FROM " + mdVertex.getDBClassName());
    statement.append("  ORDER BY " + mdAttribute.getColumnName());
    statement.append("  SKIP " + skip);
    statement.append("  LIMIT " + limit);
    statement.append(")");

    GraphQuery<VertexObject> query = new GraphQuery<VertexObject>(statement.toString());

    return this.processTraverseResults(query.getResults(), null);
  }

  @Override
  public Long getCount(T type)
  {
    MdVertexDAOIF mdVertex = type.getMdVertexDAO();

    StringBuilder statement = new StringBuilder();
    statement.append("SELECT COUNT(*) FROM " + mdVertex.getDBClassName());

    GraphQuery<Long> query = new GraphQuery<Long>(statement.toString());

    return query.getSingleResult();
  }

  @Override
  public Page<JsonSerializable> data(T type, JsonObject criteria)
  {
    return new ObjectPageQuery(type, criteria).getPage();
  }

  public List<V> processTraverseResults(List<VertexObject> results, Date date)
  {
    MdVertexDAOIF superVertex = MdVertexDAO.getMdVertexDAO(this.baseVertexClass);
    List<V> list = new LinkedList<V>();

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
          T type = this.typeService.getByMdVertex((MdVertexDAOIF) current.getMdClass());

          Map<String, List<VertexObject>> nodeMap = currentAttributes.stream().collect(Collectors.groupingBy(v -> {
            return (String) v.getObjectValue(AttributeValue.ATTRIBUTENAME);
          }));

          V bObject = this.build(type, current, nodeMap, date);
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
      T type = this.typeService.getByMdVertex((MdVertexDAOIF) current.getMdClass());

      Map<String, List<VertexObject>> nodeMap = currentAttributes.stream().collect(Collectors.groupingBy(v -> {
        return (String) v.getObjectValue(AttributeValue.ATTRIBUTENAME);
      }));

      V vsgo = this.build(type, current, nodeMap, date);
      list.add(vsgo);
    }

    return list;
  }

  public V processSingleResult(List<VertexObject> list, Date date)
  {
    List<V> results = this.processTraverseResults(list, date);

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
