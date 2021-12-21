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

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;

import com.runwaysdk.controller.RequestManager;
import com.runwaysdk.mvc.ResponseIF;
import com.runwaysdk.request.ServletResponseIF;

public class LogoutResponse implements ResponseIF
{
  private String location;

  private String name;

  private int    maxAge;

  public LogoutResponse(String location, String name, int maxAge)
  {
    this.location = location;
    this.name = name;
    this.maxAge = maxAge;
  }

  @Override
  public void handle(RequestManager manager) throws ServletException, IOException
  {
    Cookie cookie = new Cookie(this.name, "");
    cookie.setMaxAge(this.maxAge);
    cookie.setPath(manager.getReq().getContextPath());

    ServletResponseIF resp = manager.getResp();
    resp.addCookie(cookie);
    resp.sendRedirect(this.location);
  }

}
