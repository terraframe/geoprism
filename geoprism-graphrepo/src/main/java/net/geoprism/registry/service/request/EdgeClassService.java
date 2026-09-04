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
package net.geoprism.registry.service.request;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.commongeoregistry.adapter.metadata.GraphTypeDTO;

import com.runwaysdk.session.Request;
import com.runwaysdk.session.RequestType;

import net.geoprism.configuration.GeoprismProperties;
import net.geoprism.registry.DataNotFoundException;
import net.geoprism.registry.OriginException;
import net.geoprism.registry.graph.EdgeClass;
import net.geoprism.registry.service.business.EdgeClassBusinessServiceIF;

public abstract class EdgeClassService<T extends EdgeClass, D extends GraphTypeDTO> implements EdgeClassServiceIF<T, D>
{
  protected abstract EdgeClassBusinessServiceIF<T, D> getService();

  @Override
  @Request(RequestType.SESSION)
  public void delete(String sessionId, String code)
  {
    T type = this.getService().getByCodeOrThrow(code);

    if (!type.getOrigin().equals(GeoprismProperties.getOrigin()))
    {
      throw new OriginException();
    }

    this.getService().delete(type);
  }

  @Override
  @Request(RequestType.SESSION)
  public List<D> getAll(String sessionId)
  {
    return this.getService().getAll().stream().map(type -> this.getService().toDTO(type)).toList();
  }

  @Override
  @Request(RequestType.SESSION)
  public D getByCode(String sessionId, String code)
  {
    return this.getService().getByCode(code).map(type -> this.getService().toDTO(type)).orElseThrow(() -> {
      throw new DataNotFoundException("Unable to find business edge type with code [" + code + "]");
    });
  }

  @Override
  @Request(RequestType.SESSION)
  public D apply(String sessionId, D object)
  {
    if (StringUtils.isBlank(object.getOid()))
    {
      T type = this.getService().create(object);

      return this.getService().toDTO(type);
    }

    T type = this.getService().getByCodeOrThrow(object.getCode());

    this.getService().update(type, object);

    return this.getService().toDTO(type);
  }
}
