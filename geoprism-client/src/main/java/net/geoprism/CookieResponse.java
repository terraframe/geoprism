/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
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
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;

import com.runwaysdk.controller.RequestManager;
import com.runwaysdk.mvc.ResponseIF;
import com.runwaysdk.mvc.RestResponse;
import com.runwaysdk.request.ServletResponseIF;

/**
 * Use the one in the Runway mvc package instead. This cookie response doesn't provide
 * a distinction between setting values in the response body versus the Set-Cookie header
 */
@Deprecated
public class CookieResponse extends RestResponse implements ResponseIF
{
  private String name;

  private int    maxAge;

  public CookieResponse(String name, int maxAge)
  {
    this.name = name;
    this.maxAge = maxAge;
  }

  @Override
  public void handle(RequestManager manager) throws ServletException, IOException
  {
    Object serialize = this.serialize();

    String path = manager.getReq().getContextPath();

    if (path.equals("") || path.length() == 0)
    {
      path = "/";
    }

    final String value = URLEncoder.encode(serialize.toString(), "UTF-8");
    
    Cookie cookie = new Cookie(this.name, value);
    cookie.setMaxAge(this.maxAge);
    cookie.setPath(path);

    ServletResponseIF resp = manager.getResp();
    resp.addCookie(cookie);

    super.handle(manager);
  }

}
