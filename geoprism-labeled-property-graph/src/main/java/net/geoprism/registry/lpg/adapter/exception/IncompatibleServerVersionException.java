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
package net.geoprism.registry.lpg.adapter.exception;

public class IncompatibleServerVersionException extends Exception
{
  private static final long serialVersionUID = 6432902440602083768L;

  private int requestedVersion;
  
  private int remoteServerVersion;
  
  public IncompatibleServerVersionException(int requestedVersion, int remoteServerVersion)
  {
    this.requestedVersion = requestedVersion;
    this.remoteServerVersion = remoteServerVersion;
  }

  public int getRequestedVersion()
  {
    return requestedVersion;
  }

  public void setRequestedVersion(int requestedVersion)
  {
    this.requestedVersion = requestedVersion;
  }

  public int getRemoteServerVersion()
  {
    return remoteServerVersion;
  }

  public void setRemoteServerVersion(int remoteServerVersion)
  {
    this.remoteServerVersion = remoteServerVersion;
  }
}
