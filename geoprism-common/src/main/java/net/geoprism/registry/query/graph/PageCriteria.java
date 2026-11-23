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

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

/**
 * Paging, sorting, and filtering criteria for a graph page query. The
 * structure mirrors the lazy load event of a PrimeNG table.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PageCriteria
{
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Filter
  {
    private String   matchMode;

    /**
     * Either a primitive value (for the 'contains' and 'equals' match modes) or
     * an object with 'startDate' and 'endDate' properties (for the 'between'
     * match mode).
     */
    private JsonNode value;

    public Filter()
    {
    }

    public Filter(String matchMode, JsonNode value)
    {
      this.matchMode = matchMode;
      this.value = value;
    }

    public String getMatchMode()
    {
      return matchMode;
    }

    public void setMatchMode(String matchMode)
    {
      this.matchMode = matchMode;
    }

    public JsonNode getValue()
    {
      return value;
    }

    public void setValue(JsonNode value)
    {
      this.value = value;
    }

    @JsonIgnore
    public String getValueAsString()
    {
      return this.value.asString();
    }

    @JsonIgnore
    public String getStartDate()
    {
      return this.getDateProperty("startDate");
    }

    @JsonIgnore
    public String getEndDate()
    {
      return this.getDateProperty("endDate");
    }

    private String getDateProperty(String name)
    {
      JsonNode node = this.value.get(name);

      if (node != null && !node.isNull())
      {
        return node.asString();
      }

      return null;
    }
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class SortMeta
  {
    private String  field;

    private Integer order;

    public SortMeta()
    {
    }

    public SortMeta(String field, Integer order)
    {
      this.field = field;
      this.order = order;
    }

    public String getField()
    {
      return field;
    }

    public void setField(String field)
    {
      this.field = field;
    }

    public Integer getOrder()
    {
      return order;
    }

    public void setOrder(Integer order)
    {
      this.order = order;
    }
  }

  private Integer             first;

  private Integer             rows;

  private String              sortField;

  private Integer             sortOrder;

  private List<SortMeta>      multiSortMeta;

  private Map<String, Filter> filters;

  public PageCriteria()
  {
    this.multiSortMeta = new LinkedList<>();
    this.filters = new LinkedHashMap<>();
  }

  public Integer getFirst()
  {
    return first;
  }

  public void setFirst(Integer first)
  {
    this.first = first;
  }

  public Integer getRows()
  {
    return rows;
  }

  public void setRows(Integer rows)
  {
    this.rows = rows;
  }

  public String getSortField()
  {
    return sortField;
  }

  public void setSortField(String sortField)
  {
    this.sortField = sortField;
  }

  public Integer getSortOrder()
  {
    return sortOrder;
  }

  public void setSortOrder(Integer sortOrder)
  {
    this.sortOrder = sortOrder;
  }

  public List<SortMeta> getMultiSortMeta()
  {
    return multiSortMeta;
  }

  public void setMultiSortMeta(List<SortMeta> multiSortMeta)
  {
    this.multiSortMeta = multiSortMeta != null ? multiSortMeta : new LinkedList<>();
  }

  public Map<String, Filter> getFilters()
  {
    return filters;
  }

  public void setFilters(Map<String, Filter> filters)
  {
    this.filters = filters != null ? filters : new LinkedHashMap<>();
  }

  @JsonIgnore
  public boolean hasFilters()
  {
    return this.filters.size() > 0;
  }

  @JsonIgnore
  public boolean hasMultiSortMeta()
  {
    return this.multiSortMeta.size() > 0;
  }

  public static PageCriteria fromJson(String json)
  {
    if (json == null || json.isBlank())
    {
      return new PageCriteria();
    }

    return JsonMapper.shared().readValue(json, PageCriteria.class);
  }
}
