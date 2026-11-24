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
package net.geoprism.registry.query.graph;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.runwaysdk.business.graph.GraphQuery;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;
import com.runwaysdk.query.OrderBy.SortOrder;

import net.geoprism.registry.DateFormatter;
import net.geoprism.registry.view.JsonSerializable;
import net.geoprism.registry.view.JsonSerializablePage;

public abstract class AbstractGraphPageQuery<K, T extends JsonSerializable>
{
  private String     type;

  private PageCriteria criteria;

  public AbstractGraphPageQuery(String type)
  {
    this(type, new PageCriteria());
  }

  public AbstractGraphPageQuery(String type, PageCriteria criteria)
  {
    super();
    this.type = type;
    this.criteria = criteria != null ? criteria : new PageCriteria();
  }

  protected abstract List<T> getResults(final GraphQuery<K> query);

  protected abstract String getColumnName(MdAttributeDAOIF mdAttribute);

  protected abstract void addSelectAttributes(final MdVertexDAOIF mdVertex, StringBuilder statement);

  public Long getCount()
  {
    final MdVertexDAOIF mdVertex = MdVertexDAO.getMdVertexDAO(this.type);

    StringBuilder statement = new StringBuilder();
    statement.append("SELECT COUNT(*) FROM " + mdVertex.getDBClassName() + "");

    Map<String, Object> parameters = new HashMap<String, Object>();

    if (criteria.hasFilters())
    {
      this.addCriteria(mdVertex, criteria.getFilters(), statement, parameters);
    }

    final GraphQuery<Long> query = new GraphQuery<Long>(statement.toString(), parameters);

    return query.getSingleResult();
  }

  private void addCriteria(final MdVertexDAOIF mdVertex, Map<String, PageCriteria.Filter> filters, StringBuilder statement, Map<String, Object> parameters)
  {
    int i = 0;

    for (Entry<String, PageCriteria.Filter> entry : filters.entrySet())
    {
      String attributeName = entry.getKey();

      MdAttributeDAOIF mdAttribute = mdVertex.getAllDefinedMdAttributeMap().get(attributeName.toLowerCase());

      if (mdAttribute != null)
      {
        String columnName = this.getColumnName(mdAttribute);

        PageCriteria.Filter filter = entry.getValue();

        PageCriteria.MatchMode mode = filter.getMatchMode();

        if (mode == PageCriteria.MatchMode.BETWEEN)
        {
          String startDate = filter.getStartDate();

          if (startDate != null && startDate.length() > 0)
          {
            parameters.put(attributeName + "StartDate", DateFormatter.parseDate(startDate));

            statement.append( ( ( i == 0 ) ? " WHERE " : " AND " ) + columnName + " >= :" + attributeName + "StartDate");

            i++;
          }

          String endDate = filter.getEndDate();

          if (endDate != null && endDate.length() > 0)
          {
            parameters.put(attributeName + "EndDate", DateFormatter.parseDate(endDate));

            statement.append( ( ( i == 0 ) ? " WHERE " : " AND " ) + columnName + " <= :" + attributeName + "EndDate");

            i++;
          }

        }
        else if (mode == PageCriteria.MatchMode.CONTAINS)
        {
          parameters.put(attributeName, "%" + filter.getValueAsString().toUpperCase() + "%");

          statement.append( ( ( i == 0 ) ? " WHERE " : " AND " ) + columnName + ".toUpperCase() LIKE :" + attributeName);

          i++;
        }
        else if (mode == PageCriteria.MatchMode.STARTS_WITH)
        {
          parameters.put(attributeName, filter.getValueAsString().toUpperCase() + "%");

          statement.append( ( ( i == 0 ) ? " WHERE " : " AND " ) + columnName + ".toUpperCase() LIKE :" + attributeName);

          i++;
        }
        else if (mode == PageCriteria.MatchMode.EQUALS)
        {
          parameters.put(attributeName, filter.getValueAsString());

          statement.append( ( ( i == 0 ) ? " WHERE " : " AND " ) + columnName + " = :" + attributeName);

          i++;
        }

      }
    }
  }

  public JsonSerializablePage<T> getPage()
  {
    int pageSize = criteria.getRows();
    int pageNumber = 1;

    Long count = this.getCount();

    final MdVertexDAOIF mdVertex = MdVertexDAO.getMdVertexDAO(this.type);

    StringBuilder statement = new StringBuilder();
    statement.append("SELECT ");
    this.addSelectAttributes(mdVertex, statement);
    statement.append(" FROM " + mdVertex.getDBClassName() + "");

    Map<String, Object> parameters = new HashMap<String, Object>();

    if (criteria.hasFilters())
    {
      this.addCriteria(mdVertex, criteria.getFilters(), statement, parameters);
    }

    if (criteria.getSortField() != null && criteria.getSortOrder() != null)
    {
      String field = criteria.getSortField();
      SortOrder order = criteria.getSortOrder() == 1 ? SortOrder.ASC : SortOrder.DESC;
      MdAttributeDAOIF mdAttribute = mdVertex.getAllDefinedMdAttributeMap().get(field.toLowerCase());

      statement.append(" ORDER BY " + this.getColumnName(mdAttribute) + " " + order.name());
    }
    else if (criteria.hasMultiSortMeta())
    {
      List<PageCriteria.SortMeta> sorts = criteria.getMultiSortMeta();

      for (int i = 0; i < sorts.size(); i++)
      {
        PageCriteria.SortMeta sort = sorts.get(i);

        String field = sort.getField();
        SortOrder order = sort.getOrder() != null && sort.getOrder() == 1 ? SortOrder.ASC : SortOrder.DESC;
        MdAttributeDAOIF mdAttribute = mdVertex.getAllDefinedMdAttributeMap().get(field.toLowerCase());

        if (i == 0)
        {
          statement.append(" ORDER BY " + this.getColumnName(mdAttribute) + " " + order.name());
        }
        else
        {
          statement.append(", " + this.getColumnName(mdAttribute) + " " + order.name());
        }
      }
    }

    if (criteria.getFirst() != null && criteria.getRows() != null)
    {
      int first = criteria.getFirst();

      statement.append(" SKIP " + first + " LIMIT " + pageSize);

      pageNumber = ( first / pageSize ) + 1;
    }

    final GraphQuery<K> query = new GraphQuery<K>(statement.toString(), parameters);

    return new JsonSerializablePage<T>(count, pageNumber, pageSize, this.getResults(query));
  }
}
