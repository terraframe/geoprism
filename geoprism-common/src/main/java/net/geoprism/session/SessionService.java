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

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.runwaysdk.localization.LocalizationFacade;

@Component
public class SessionService implements SessionServiceIF
{
  // TODO : Don't autowire components here because this service is directly instantiated in IDM
  
  @Override
  public JsonArray getInstalledLocales(String sessionId)
  {
    JsonArray jaLocales = new JsonArray();
    
    Collection<Locale> installedLocales = LocalizationFacade.getInstalledLocales();
    for (Locale loc : installedLocales)
    {
      JsonObject locObj = new JsonObject();
      locObj.addProperty("language", loc.getDisplayLanguage());
      locObj.addProperty("country", loc.getDisplayCountry());
      locObj.addProperty("name", loc.getDisplayName());
      locObj.addProperty("variant", loc.getDisplayVariant());

      jaLocales.add(locObj);
    }
    
    return jaLocales;
  }
  
  @Override
  public String getServerVersion()
  {
    return "1.0-SNAPSHOT";
  }
  
  @Override
  public String getHomeUrl()
  {
    return "/prism/home";
  }

  @Override
  public String getLoginUrl()
  {
    return "/prism/home#login";
  }
  
  @Override
  public Set<String> getPublicEndpoints()
  {
    TreeSet<String> endpoint = new TreeSet<String>();
    endpoint.add("logo/view");
    endpoint.add("prism/admin");
    endpoint.add("prism/home");
    endpoint.add("forgotpassword/initiate");
    endpoint.add("forgotpassword/complete");
    endpoint.add("invite-user/initiate");
    endpoint.add("invite-user/complete");

    return endpoint;
  }
  
  @Override
  public boolean isPublic(HttpServletRequest req)
  {
    String uri = req.getRequestURI();
    
    if (uri.equals("/") || uri.equals(""))
    {
      return true;
    }

    Set<String> endpoints = this.getPublicEndpoints();

    for (String endpoint : endpoints)
    {
      if (uri.contains(req.getContextPath() + "/" + endpoint))
      {
        return true;
      }
    }

    if (uri.endsWith("Localized.js.jsp"))
    {
      return true;
    }

    return false;
  }

  @Override
  public boolean pathAllowed(HttpServletRequest req)
  {
    String uri = req.getRequestURI();

    List<String> endpoints = new LinkedList<String>();

    // They're allowed to hit the login view page, otherwise its a redirect loop
    endpoints.add("loginRedirect");

    // They can also invoke the login action on SessionController @
    // session/form and session/login
    endpoints.add("session/form");
    endpoints.add("session/login");
    endpoints.add("session/ologin");

    for (String endpoint : endpoints)
    {
      if (uri.equals(req.getContextPath() + "/" + endpoint))
      {
        return true;
      }
    }

    List<String> directories = new LinkedList<String>();
    directories.add("jquery");
    directories.add("font-awesome");
    directories.add("fontawesome");
    directories.add("3rd-party");

    // Allow direct hitting of all page resources in login directories.
    directories.add("/net/geoprism/login");

    // Directory of uploaded images
    directories.add("uploaded_images/");

    for (String directory : directories)
    {
      if (uri.contains(directory))
      {
        return true;
      }
    }

    List<String> extensions = new LinkedList<String>();
    extensions.add(".sld");
    extensions.add(".css");
    extensions.add(".png");
    extensions.add(".jpg");
    extensions.add(".bmp");
    extensions.add(".jpeg");
    extensions.add(".gif");
    extensions.add(".svg");
    extensions.add(".pdf");
    extensions.add(".otf");
    extensions.add(".mp4");

    extensions.add(".js");

    for (String extension : extensions)
    {
      if (uri.endsWith(extension))
      {
        return true;
      }
    }

    return false;
  }

  @Override
  public void onLoginSuccess(String username, String sessionId)
  {
    // Do nothing
  }

  @Override
  public void onLoginFailure(String username)
  {
    // Do nothing
  }
}
