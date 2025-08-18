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
package net.geoprism.registry.service.request;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.runwaysdk.session.Request;
import com.runwaysdk.session.RequestType;

import net.geoprism.registry.graph.DataSource;
import net.geoprism.registry.model.DataSourceDTO;
import net.geoprism.registry.service.business.DataSourceBusinessServiceIF;

@Service
public class SourceService implements SourceServiceIF
{
  @Autowired
  private DataSourceBusinessServiceIF service;

  @Override
  @Request(RequestType.SESSION)
  public List<DataSourceDTO> getAll(String sessionId)
  {
    List<DataSource> sources = this.service.getAll();

    return sources.stream().map(source -> this.service.toDTO(source)).collect(Collectors.toList());
  }

  @Override
  @Request(RequestType.SESSION)
  public void delete(String sessionId, String code)
  {
    this.service.getByCode(code).ifPresent(source -> {
      this.service.delete(source);
    });
  }

  @Override
  @Request(RequestType.SESSION)
  public DataSourceDTO apply(String sessionId, DataSourceDTO object)
  {
    DataSource source = this.service.apply(object);

    return this.service.toDTO(source);
  }

  @Override
  @Request(RequestType.SESSION)
  public DataSourceDTO getByCode(String sessionId, String code)
  {
    // TODO: Add localized error message for missing source
    DataSource source = this.service.getByCode(code).orElseThrow();

    return this.service.toDTO(source);
  }
  
  @Override
  @Request(RequestType.SESSION)
  public List<DataSourceDTO> search(String sessionId, String text)
  {
    List<DataSource> sources = this.service.search(text);

    return sources.stream().map(source -> this.service.toDTO(source)).collect(Collectors.toList());
  }
}
