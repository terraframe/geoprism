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
package net.geoprism.graph.lpg.adapter;

import org.apache.http.NameValuePair;

import net.geoprism.graph.lpg.adapter.exception.BadServerUriException;
import net.geoprism.graph.lpg.adapter.exception.HTTPException;
import net.geoprism.graph.lpg.adapter.response.RegistryResponse;

public interface RegistryConnectorIF extends AutoCloseable
{

  public String getServerUrl();

  public RegistryResponse httpGet(String string, NameValuePair... params) throws HTTPException, BadServerUriException;

  @Override
  public void close();

}
