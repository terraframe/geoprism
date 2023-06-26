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
package net.geoprism.session;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import net.geoprism.rbac.RoleView;

public interface SessionServiceIF
{
  public JsonArray getInstalledLocales(String sessionId);

  public JsonElement getCookieInformation(String sessionId, Set<RoleView> roles);

  public JsonElement getLoginResponse(String sessionId, Set<RoleView> roles);

  public String getServerVersion();

  public String getHomeUrl();

  public String getLoginUrl();

  public Set<String> getPublicEndpoints();

  public boolean isPublic(HttpServletRequest req);

  public boolean pathAllowed(HttpServletRequest req);

  public void onLoginSuccess(String username, String sessionId);

  public void onLoginFailure(String username);
}
