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

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.runwaysdk.business.graph.GraphQuery;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;

import net.geoprism.registry.graph.DataSource;
import net.geoprism.registry.model.DataSourceDTO;

@Service
public class DataSourceBusinessService implements DataSourceBusinessServiceIF
{

  @Override
  @Transaction
  public void delete(DataSource source)
  {
    source.delete();
  }

  @Override
  public DataSourceDTO toDTO(DataSource source)
  {
    DataSourceDTO object = new DataSourceDTO();
    object.setOid(source.getOid());
    object.setCode(source.getCode());

    return object;
  }

  @Override
  @Transaction
  public DataSource apply(DataSourceDTO object)
  {
    DataSource source = null;

    if (!StringUtils.isBlank(object.getOid()))
    {
      source = DataSource.get(object.getOid());
    }
    else
    {
      source = new DataSource();
    }

    source.setCode(object.getCode());

    return apply(source);
  }

  @Override
  @Transaction
  public DataSource apply(DataSource source)
  {
    source.apply();

    return source;
  }

  @Override
  public Optional<DataSource> getByCode(String code)
  {
    return DataSource.getByCode(code);
  }
  
  @Override
  public DataSource get(String sourceOid)
  {
    return DataSource.get(sourceOid);
  }



  @Override
  public List<DataSource> getAll()
  {
    MdVertexDAOIF mdVertex = MdVertexDAO.getMdVertexDAO(DataSource.CLASS);
    MdAttributeDAOIF mdAttribute = mdVertex.definesAttribute(DataSource.CODE);

    StringBuilder statement = new StringBuilder();
    statement.append("SELECT FROM " + mdVertex.getDBClassName());
    statement.append(" ORDER BY " + mdAttribute.getColumnName());

    GraphQuery<DataSource> query = new GraphQuery<DataSource>(statement.toString());

    return query.getResults();
  }

  @Override
  public List<DataSource> search(String text)
  {
    MdVertexDAOIF mdVertex = MdVertexDAO.getMdVertexDAO(DataSource.CLASS);
    MdAttributeDAOIF mdAttribute = mdVertex.definesAttribute(DataSource.CODE);

    StringBuilder statement = new StringBuilder();
    statement.append("SELECT FROM " + mdVertex.getDBClassName());
    statement.append(" WHERE " + mdAttribute.getColumnName() + " LIKE :text");
    statement.append(" ORDER BY " + mdAttribute.getColumnName());

    GraphQuery<DataSource> query = new GraphQuery<DataSource>(statement.toString());
    query.setParameter("text", "%" + text + "%");

    return query.getResults();
  }

}
