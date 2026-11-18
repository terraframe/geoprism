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

import org.commongeoregistry.adapter.dataaccess.UnknownTermException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.runwaysdk.business.graph.GraphQuery;
import com.runwaysdk.business.graph.VertexObject;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.graph.VertexObjectDAO;
import com.runwaysdk.dataaccess.graph.attributes.ValueOverTime;
import com.runwaysdk.dataaccess.graph.attributes.ValueOverTimeCollection;

import net.geoprism.registry.graph.AttributeClassificationType;
import net.geoprism.registry.graph.AttributeType;
import net.geoprism.registry.graph.BusinessEdgeType;
import net.geoprism.registry.graph.BusinessType;
import net.geoprism.registry.graph.BusinessVertex;
import net.geoprism.registry.model.BusinessObject;
import net.geoprism.registry.model.ConceptObject;
import net.geoprism.registry.model.EdgeConstant;
import net.geoprism.registry.model.graph.VertexComponent;
import net.geoprism.registry.model.graph.VertexServerGeoObject;
import net.geoprism.registry.query.graph.VertexAndEdgeQuery;
import net.geoprism.registry.query.graph.VertexAndEdgeQuery.EdgeQueryObject;
import net.geoprism.registry.view.AttributeClassificationDTO;
import net.geoprism.registry.view.BusinessTypeDTO;
import net.geoprism.registry.view.ObjectAtTimeDTO;
import net.geoprism.registry.view.ObjectOverTimeDTO;
import net.geoprism.registry.view.ValueOverTimeEntryDTO;

@Service
public class BusinessObjectBusinessService extends ObjectEdgeBusinessService<BusinessObject, BusinessType, BusinessTypeDTO, BusinessEdgeType, VertexComponent> implements BusinessObjectBusinessServiceIF
{
  @Autowired
  private ConceptObjectBusinessServiceIF cObjectService;

  public BusinessObjectBusinessService(BusinessTypeBusinessServiceIF typeService)
  {
    super(typeService, BusinessVertex.CLASS);
  }

  @Override
  public BusinessObject newInstance(BusinessType type)
  {
    VertexObject vertex = VertexObject.instantiate(VertexObjectDAO.newInstance(type.getMdVertexDAO()));

    return new BusinessObject(type, vertex, new TreeMap<>());
  }

  @Override
  protected BusinessObject build(BusinessType type, VertexObject current, Map<String, List<VertexObject>> nodeMap, Date date)
  {
    return new BusinessObject(type, current, nodeMap, date);
  }

  @Override
  public void populate(BusinessObject object, ObjectAtTimeDTO dto, Date startDate, Date endDate, AttributeType attribute)
  {
    if (attribute instanceof AttributeClassificationType)
    {
      org.commongeoregistry.adapter.metadata.AttributeClassificationType attributeDto = (org.commongeoregistry.adapter.metadata.AttributeClassificationType) attribute.toDTO();

      String attributeName = attribute.getCode();

      Object value = dto.getValue(attributeName);

      if (value != null)
      {
        value = this.cObjectService.getByCode(attributeDto, (String) value).map(v -> v.getVertex()).orElse(null);
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
    else
    {
      super.populate(object, dto, startDate, endDate, attribute);
    }
  }

  @Override
  public void populate(BusinessObject object, ObjectOverTimeDTO dto, AttributeType attribute)
  {
    if (attribute instanceof AttributeClassificationType)
    {
      org.commongeoregistry.adapter.metadata.AttributeClassificationType attributeDto = (org.commongeoregistry.adapter.metadata.AttributeClassificationType) attribute.toDTO();

      String attributeName = attribute.getCode();

      if (attribute.getIsChangeOverTime())
      {
        ValueOverTimeCollection c = new ValueOverTimeCollection();

        List<ValueOverTimeEntryDTO<String>> entries = dto.has(attributeName) ? dto.getValuesOverTime(attributeName) : new LinkedList<>();

        for (ValueOverTimeEntryDTO<String> votDTO : entries)
        {
          String value = votDTO.getValue();

          ConceptObject cObject = this.cObjectService.getByCode(attributeDto, (String) value).orElseThrow(() -> {
            throw new ProgrammingErrorException("Unable to find source with code [" + value + "]");
          });

          c.add(new ValueOverTime(votDTO.getOid(), votDTO.getStartDate(), votDTO.getEndDate(), cObject.getVertex()));
        }
        
        object.setValuesOverTime(attributeName, c);
      }
      else
      {
        Object value = dto.getValue(attributeName);

        if (value != null)
        {
          ConceptObject cObject = this.cObjectService.getByCode(attributeDto, (String) value).orElseThrow(() -> {
            throw new ProgrammingErrorException("Unable to find concept with code [" + value + "]");
          });

          object.setValue(attributeName, cObject.getVertex());
        }
      }
    }
    else
    {
      super.populate(object, dto, attribute);
    }
  }

  @Override
  public ObjectAtTimeDTO toDTO(BusinessObject object, Date date)
  {
    ObjectAtTimeDTO dto = super.toDTO(object, date);
    dto.setLabel(object.getLabel());

    return dto;
  }

  @Override
  public void toDTO(BusinessObject object, ObjectOverTimeDTO dto, AttributeType attribute)
  {
    if (attribute instanceof AttributeClassificationType)
    {
      String attributeName = attribute.getCode();

      if (attribute.getIsChangeOverTime())
      {
        ValueOverTimeCollection collection = object.getValuesOverTime(attributeName);

        List<ValueOverTimeEntryDTO<String>> entries = collection.stream().map(vot -> {
          String value = (String) vot.getValue();
          
          ConceptObject concept = this.cObjectService.getByOid(value).orElseThrow(() -> new UnknownTermException(value, attribute.toDTO()));

          return ValueOverTimeEntryDTO.of(vot.getOid(), vot.getStartDate(), vot.getEndDate(), concept.getCode());
        }).toList();

        dto.put(attributeName, AttributeClassificationDTO.of(entries));
      }
      else
      {
        Object value = object.getValue(attributeName);

        if (value != null)
        {
          ConceptObject concept = this.cObjectService.getByOid((String) value).orElseThrow();

          dto.put(attributeName, AttributeClassificationDTO.of(concept.getCode()));
        }

      }
    }
    else
    {
      super.toDTO(object, dto, attribute);
    }
  }

  @Override
  public BusinessObject newInstance(BusinessType type, ObjectOverTimeDTO dto)
  {
    BusinessObject object = this.newInstance(type);

    populate(object, dto);

    return object;
  }

  @Override
  public List<VertexComponent> getParents(BusinessObject source, BusinessEdgeType type, Date date)
  {
    StringBuilder statement = new StringBuilder();

    statement.append("TRAVERSE out('");
    statement.append(EdgeConstant.HAS_VALUE.getDBClassName());
    statement.append("', '");
    statement.append(EdgeConstant.HAS_GEOMETRY.getDBClassName());
    statement.append("') FROM (");

    if (date != null)
    {
      statement.append("SELECT EXPAND(inE('");
      statement.append(type.getMdEdge().getDbClassName());
      statement.append("')[:date BETWEEN startDate AND endDate].outV()) ");
    }
    else
    {
      statement.append("SELECT EXPAND(in('");
      statement.append(type.getMdEdge().getDbClassName());
      statement.append("')) ");
    }

    statement.append("FROM :rid");
    statement.append(")");

    GraphQuery<VertexObject> query = new GraphQuery<VertexObject>(statement.toString());
    query.setParameter("rid", source.getVertex().getRID());
    query.setParameter("date", date);

    if (type.getIsParentGeoObject())
    {
      return VertexServerGeoObject.processTraverseResults(query.getResults(), date).stream().map(s -> (VertexComponent) s).toList();
    }

    return this.processTraverseResults(query.getResults(), date).stream().sorted((a, b) -> {
      return a.getLabel().compareTo(b.getLabel());
    }).collect(Collectors.toList());
  }

  @Override
  public List<VertexComponent> getChildren(BusinessObject source, BusinessEdgeType type, Date date)
  {
    StringBuilder statement = new StringBuilder();

    statement.append("TRAVERSE out('");
    statement.append(EdgeConstant.HAS_VALUE.getDBClassName());
    statement.append("', '");
    statement.append(EdgeConstant.HAS_GEOMETRY.getDBClassName());
    statement.append("') FROM (");

    if (date != null)
    {
      statement.append("SELECT EXPAND(outE('");
      statement.append(type.getMdEdge().getDbClassName());
      statement.append("')[:date BETWEEN startDate AND endDate].inV()) ");
    }
    else
    {
      statement.append("SELECT EXPAND(out('");
      statement.append(type.getMdEdge().getDbClassName());
      statement.append("')) ");
    }

    statement.append("FROM :rid");
    statement.append(")");

    GraphQuery<VertexObject> query = new GraphQuery<VertexObject>(statement.toString());
    query.setParameter("rid", source.getVertex().getRID());
    query.setParameter("date", date);

    if (type.getIsChildGeoObject())
    {
      return VertexServerGeoObject.processTraverseResults(query.getResults(), date).stream().map(s -> (VertexComponent) s).toList();
    }

    return this.processTraverseResults(query.getResults(), date).stream().sorted((a, b) -> {
      return a.getLabel().compareTo(b.getLabel());
    }).collect(Collectors.toList());
  }

  public List<EdgeQueryObject> getEdgeParents(BusinessObject object, BusinessEdgeType type, Date date)
  {
    return new VertexAndEdgeQuery(object.getVertex(), type.getMdEdge().getDbClassName(), VertexAndEdgeQuery.Direction.PARENTS, type.getIsParentGeoObject() ? VertexServerGeoObject::processTraverseResults : this::processTraverseResults).setDate(date).getResults();
  }

  public List<EdgeQueryObject> getEdgeChildren(BusinessObject object, BusinessEdgeType type, Date date)
  {
    return new VertexAndEdgeQuery(object.getVertex(), type.getMdEdge().getDbClassName(), VertexAndEdgeQuery.Direction.CHILDREN, type.getIsChildGeoObject() ? VertexServerGeoObject::processTraverseResults : this::processTraverseResults).setDate(date).getResults();
  }

}
