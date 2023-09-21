/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Geoprism Registry(tm).
 *
 * Geoprism Registry(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Geoprism Registry(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Geoprism Registry(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism.registry.conversion;

import java.util.Date;

import org.commongeoregistry.adapter.dataaccess.GeoObject;
import org.commongeoregistry.adapter.dataaccess.GeoObjectOverTime;

import net.geoprism.registry.model.ServerGeoObjectIF;
import net.geoprism.registry.model.ServerGeoObjectType;
import net.geoprism.registry.query.ServerGeoObjectQuery;

public interface ServerGeoObjectStrategyIF
{
  public ServerGeoObjectType getType();

  public ServerGeoObjectIF constructFromGeoObject(GeoObject geoObject, boolean isNew);

  public ServerGeoObjectIF constructFromGeoObjectOverTime(GeoObjectOverTime geoObject, boolean isNew);

  public ServerGeoObjectIF constructFromDB(Object dbObject);

  public ServerGeoObjectIF getGeoObjectByCode(String code);

  public ServerGeoObjectIF getGeoObjectByUid(String uid);

  public ServerGeoObjectIF newInstance();

  public ServerGeoObjectQuery createQuery(Date date);

}
