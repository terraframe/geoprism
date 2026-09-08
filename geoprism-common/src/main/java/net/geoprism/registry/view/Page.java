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
package net.geoprism.registry.view;

import java.util.Iterator;
import java.util.List;

public class Page<T> implements Iterable<T>
{

  protected Long    count;

  protected Integer pageNumber;

  protected Integer pageSize;

  protected List<T> resultSet;

  public Page()
  {
  }

  public Page(Integer count, Integer pageNumber, Integer pageSize, List<T> resultSet)
  {
    this(count.longValue(), pageNumber, pageSize, resultSet);
  }

  public Page(Long count, Integer pageNumber, Integer pageSize, List<T> resultSet)
  {
    super();
    this.count = count;
    this.pageNumber = pageNumber;
    this.pageSize = pageSize;
    this.resultSet = resultSet;
  }

  public Long getCount()
  {
    return count;
  }

  public void setCount(Long count)
  {
    this.count = count;
  }

  public Integer getPageNumber()
  {
    return pageNumber;
  }

  public void setPageNumber(Integer pageNumber)
  {
    this.pageNumber = pageNumber;
  }

  public Integer getPageSize()
  {
    return pageSize;
  }

  public void setPageSize(Integer pageSize)
  {
    this.pageSize = pageSize;
  }

  public List<T> getResultSet()
  {
    return resultSet;
  }

  public void setResultSet(List<T> resultSet)
  {
    this.resultSet = resultSet;
  }

  @Override
  public Iterator<T> iterator()
  {
    return this.resultSet.iterator();
  }
}
