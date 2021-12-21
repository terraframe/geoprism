/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
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
package net.geoprism;

import com.runwaysdk.constants.ClientRequestIF;

public class SessionEvent
{
  public static enum EventType {
    LOGIN_SUCCESS, LOGIN_FAILURE
  }

  private EventType       type;

  private ClientRequestIF request;

  private String          username;

  public SessionEvent(EventType type, ClientRequestIF request, String username)
  {
    super();
    this.type = type;
    this.request = request;
    this.username = username;
  }

  public EventType getType()
  {
    return type;
  }

  public void setType(EventType type)
  {
    this.type = type;
  }

  public ClientRequestIF getRequest()
  {
    return request;
  }

  public void setRequest(ClientRequestIF request)
  {
    this.request = request;
  }

  public String getUsername()
  {
    return username;
  }

  public void setUsername(String username)
  {
    this.username = username;
  }

}
