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
import java.util.stream.Collectors;

import org.commongeoregistry.adapter.constants.DefaultAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.runwaysdk.business.graph.GraphQuery;
import com.runwaysdk.business.graph.VertexObject;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.graph.attributes.ValueOverTimeCollection;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;

import net.geoprism.configuration.GeoprismProperties;
import net.geoprism.registry.DateFormatter;
import net.geoprism.registry.OriginException;
import net.geoprism.registry.graph.AttributeDataSourceType;
import net.geoprism.registry.graph.AttributeDateType;
import net.geoprism.registry.graph.AttributeType;
import net.geoprism.registry.graph.AttributeValue;
import net.geoprism.registry.graph.DataSource;
import net.geoprism.registry.graph.ObjectClass;
import net.geoprism.registry.model.EdgeConstant;
import net.geoprism.registry.model.graph.ServerObjectVertex;
import net.geoprism.registry.view.MultiValueDTO;
import net.geoprism.registry.view.ObjectAtTimeDTO;
import net.geoprism.registry.view.ObjectClassDTO;
import net.geoprism.registry.view.ObjectOverTimeDTO;
import net.geoprism.registry.view.TypeInfo;
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
        .filter(attribute -> !attribute.getCode().equals(DefaultAttribute.CODE.getName())) //
        .forEach(attribute -> {

          String attributeName = attribute.getCode();

          if (attribute.getIsChangeOverTime())
          {
            ValueOverTimeCollection collection = object.getValuesOverTime(attributeName);
            List<ValueOverTimeEntryDTO> values = collection.stream().map(vot -> {

              ValueOverTimeEntryDTO vDTO = new ValueOverTimeEntryDTO();
              vDTO.setStartDate(vot.getStartDate());
              vDTO.setEndDate(vot.getEndDate());
              vDTO.setOid(vot.getOid());

              Object value = vot.getValue();

              if (attribute instanceof AttributeDataSourceType)
              {
                DataSource dataSource = this.sourceService.get((String) value);

                if (dataSource != null)
                {
                  vDTO.setValue(dataSource.getCode());
                }
              }
              else if (value instanceof Date)
              {
                vDTO.setValue(DateFormatter.formatDate((Date) value, false));
              }
              else
              {
                vDTO.setValue(value);
              }

              return vDTO;
            }).toList();

            dto.put(attributeName, new MultiValueDTO(values));
          }
          else
          {
            Object value = object.getValue(attributeName);

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
              else if (value instanceof Date)
              {
                dto.setValue(attributeName, DateFormatter.formatDate((Date) value, false));
              }
              else
              {
                dto.setValue(attributeName, value);
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
        .filter(attribute -> !attribute.getCode().equals(DefaultAttribute.CODE.getName())) //
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
            else if (value instanceof Date)
            {
              dto.setValue(attributeName, DateFormatter.formatDate((Date) value, false));
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
    object.setCode(dto.getCode());

    T type = object.getType();
    List<AttributeType> attributes = type.getAttributes();

    attributes.stream().filter(attribute -> {
      String attributeName = attribute.getCode();

      return !attributeName.equals(DefaultAttribute.CODE.getName());
    }).forEach(attribute -> {
      String attributeName = attribute.getCode();

      if (dto.has(attributeName))
      {
        if (attribute.getIsChangeOverTime())
        {
          List<ValueOverTimeEntryDTO> vots = dto.getValuesOverTime(attributeName);

          vots.forEach(vot -> {
            if (attribute instanceof AttributeDateType)
            {
              object.setValue(attributeName, DateFormatter.parseDate((String) vot.getValue()), vot.getStartDate(), vot.getEndDate());
            }
            else if (attribute instanceof AttributeDataSourceType)
            {
              String code = dto.getValue(attributeName);

              this.sourceService.getByCode(code).ifPresent(dataSource -> {
                object.setValue(attributeName, dataSource, vot.getStartDate(), vot.getEndDate());
              });
            }
            else
            {
              object.setValue(attributeName, vot.getValue(), vot.getStartDate(), vot.getEndDate());
            }
          });

        }
        else
        {
          if (attribute instanceof AttributeDateType)
          {
            object.setValue(attributeName, DateFormatter.parseDate(dto.getValue(attributeName)));
          }
          else if (attribute instanceof AttributeDataSourceType)
          {
            String code = dto.getValue(attributeName);

            this.sourceService.getByCode(code).ifPresent(dataSource -> {
              object.setValue(attributeName, dataSource);
            });
          }
          else
          {
            object.setValue(attributeName, dto.getValue(attributeName));
          }
        }
      }
    });
  }

  @Override
  public void populate(V object, ObjectAtTimeDTO dto, Date startDate, Date endDate)
  {
    object.setCode(dto.getCode());

    T type = object.getType();

    List<AttributeType> attributes = type.getAttributes();

    attributes.stream().filter(attribute -> {
      String attributeName = attribute.getCode();

      return !attributeName.equals(DefaultAttribute.CODE.getName());
    }).forEach(attribute -> {
      String attributeName = attribute.getCode();

      if (dto.has(attributeName))
      {
        Object value = dto.getValue(attributeName);

        if (attribute instanceof AttributeDateType)
        {
          value = DateFormatter.parseDate((String) value);
        }
        else if (attribute instanceof AttributeDataSourceType)
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
      }
    });
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

    object.getVertex().apply();
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

    object.getVertex().delete();
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
