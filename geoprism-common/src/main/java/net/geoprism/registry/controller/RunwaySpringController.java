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
package net.geoprism.registry.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;

import com.runwaysdk.constants.ClientConstants;
import com.runwaysdk.constants.ClientRequestIF;

public abstract class RunwaySpringController
{
  public static class OidBody
  {
    @NotEmpty
    private String oid;

    public String getOid()
    {
      return oid;
    }

    public void setOid(String oid)
    {
      this.oid = oid;
    }
  }
  
  @Autowired
  private HttpServletRequest  request;

  @Autowired
  private HttpServletResponse response;

  protected HttpServletRequest getRequest()
  {
    return request;
  }

  protected HttpServletResponse getResponse()
  {
    return response;
  }

  protected String getSessionId()
  {
    return getClientRequest().getSessionId();
  }

  public ClientRequestIF getClientRequest()
  {
    return (ClientRequestIF) request.getAttribute(ClientConstants.CLIENTREQUEST);
  }
}
