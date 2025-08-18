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

import org.springframework.stereotype.Component;

import net.geoprism.registry.model.DataSourceDTO;

@Component
public interface SourceServiceIF
{
  public List<DataSourceDTO> getAll(String sessionId);

  public void delete(String sessionId, String code);

  public DataSourceDTO apply(String sessionId, DataSourceDTO object);

  public DataSourceDTO getByCode(String sessionId, String code);

  public List<DataSourceDTO> search(String sessionId, String text);
}
