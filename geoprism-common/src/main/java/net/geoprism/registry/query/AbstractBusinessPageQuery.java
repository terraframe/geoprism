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
package net.geoprism.registry.query;

import java.util.List;
import java.util.Map.Entry;

import com.runwaysdk.business.Business;
import com.runwaysdk.business.BusinessQuery;
import com.runwaysdk.dataaccess.AttributeDoesNotExistException;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.query.ComponentQuery;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.OrderBy.SortOrder;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.query.Selectable;
import com.runwaysdk.query.SelectableBoolean;
import com.runwaysdk.query.SelectableChar;
import com.runwaysdk.query.SelectableMoment;
import com.runwaysdk.query.ValueQuery;

import net.geoprism.registry.DateUtil;
import net.geoprism.registry.query.graph.PageCriteria;
import net.geoprism.registry.view.JsonSerializable;
import net.geoprism.registry.view.JsonSerializablePage;

public abstract class AbstractBusinessPageQuery<T extends JsonSerializable>
{
  private PageCriteria    criteria;

  private MdBusinessDAOIF mdBusiness;

  public AbstractBusinessPageQuery(MdBusinessDAOIF mdBusiness)
  {
    this(mdBusiness, new PageCriteria());
  }

  public AbstractBusinessPageQuery(MdBusinessDAOIF mdBusiness, PageCriteria criteria)
  {
    super();
    this.mdBusiness = mdBusiness;
    this.criteria = criteria != null ? criteria : new PageCriteria();
  }

  public MdBusinessDAOIF getMdBusiness()
  {
    return mdBusiness;
  }

  public PageCriteria getCriteria()
  {
    return criteria;
  }

  protected abstract List<T> getResults(final List<? extends Business> list);

  public JsonSerializablePage<T> getPage()
  {
    BusinessQuery query = this.getQuery();

    int pageSize = 10;
    int pageNumber = 1;

    if (criteria.getFirst() != null && criteria.getRows() != null)
    {
      int first = criteria.getFirst();
      pageSize = criteria.getRows();
      pageNumber = ( first / pageSize ) + 1;
    }

    long count = query.getCount();

    try (OIterator<? extends Business> iterator = query.getIterator(pageSize, pageNumber))
    {
      return new JsonSerializablePage<T>(count, pageNumber, pageSize, this.getResults(iterator.getAll()));
    }
  }

  public BusinessQuery getQuery()
  {
    BusinessQuery query = new QueryFactory().businessQuery(this.mdBusiness.definesType());

    return getQuery(query, query);
  }

  public BusinessQuery getQuery(ValueQuery vQuery)
  {
    return getQuery(vQuery, new BusinessQuery(vQuery, mdBusiness.definesType()));
  }

  protected BusinessQuery getQuery(ComponentQuery qQuery, BusinessQuery query)
  {
    if (criteria.getSortField() != null && criteria.getSortOrder() != null)
    {
      String field = criteria.getSortField();
      SortOrder order = criteria.getSortOrder() == 1 ? SortOrder.ASC : SortOrder.DESC;

      qQuery.ORDER_BY(query.getS(field), order);
    }
    else if (criteria.hasMultiSortMeta())
    {
      for (PageCriteria.SortMeta sort : criteria.getMultiSortMeta())
      {
        String field = sort.getField();
        SortOrder order = sort.getOrder() != null && sort.getOrder() == 1 ? SortOrder.ASC : SortOrder.DESC;

        qQuery.ORDER_BY(query.getS(field), order);
      }
    }

    if (criteria.hasFilters())
    {
      for (Entry<String, PageCriteria.Filter> entry : criteria.getFilters().entrySet())
      {
        String attributeName = entry.getKey();

        Selectable attribute = this.getSelectable(query, attributeName);

        if (attribute != null)
        {
          PageCriteria.Filter filter = entry.getValue();
          String mode = filter.getMatchMode();

          if (attribute instanceof SelectableMoment)
          {
            String startDate = filter.getStartDate();

            if (startDate != null && startDate.length() > 0)
            {
              qQuery.WHERE( ( (SelectableMoment) attribute ).GE(DateUtil.parseDate(startDate)));
            }

            String endDate = filter.getEndDate();

            if (endDate != null && endDate.length() > 0)
            {
              qQuery.WHERE( ( (SelectableMoment) attribute ).LE(DateUtil.parseDate(endDate)));
            }
          }
          else if (attribute instanceof SelectableBoolean)
          {
            String value = filter.getValueAsString();

            filterBoolean(qQuery, attribute, Boolean.valueOf(value));
          }
          else if ("contains".equals(mode))
          {
            String value = filter.getValueAsString();

            SelectableChar selectable = (SelectableChar) attribute;

            qQuery.WHERE(selectable.LIKEi("%" + value + "%"));
          }
          else if ("equals".equals(mode))
          {
            String value = filter.getValueAsString();

            qQuery.WHERE(attribute.EQ(value));
          }
        }
      }
    }
    return query;
  }

  public Selectable getSelectable(BusinessQuery query, String attributeName)
  {
    try
    {
      return query.get(attributeName);
    }
    catch (AttributeDoesNotExistException e)
    {
      return null;
    }
  }

  protected void filterBoolean(ComponentQuery qQuery, Selectable attribute, Boolean value)
  {
    qQuery.WHERE( ( (SelectableBoolean) attribute ).EQ(value));
  }

}
