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

import java.util.Locale;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import com.runwaysdk.ClientSession;
import com.runwaysdk.constants.ClientConstants;
import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.constants.CommonProperties;


public class CachedImageUtil 
{
  public static void setBannerPath(ServletRequest request)
  {
    if (request instanceof HttpServletRequest)
    {
      HttpServletRequest httpRequest = (HttpServletRequest) request;

      String banner = httpRequest.getContextPath() + "/" + "net/geoprism/images/splash_logo.png";

      try
      {

        ClientSession clientSession = (ClientSession) httpRequest.getAttribute(ClientConstants.CLIENTSESSION);

        if (clientSession != null)
        {
          ClientRequestIF clientRequest = clientSession.getRequest();

          String cache = SystemLogoSingletonDTO.getBannerFileFromCache(clientRequest, httpRequest);

          if (cache != null)
          {
            banner = cache;
          }
        }
        else
        {
          ClientSession session = ClientSession.createAnonymousSession(new Locale[] { CommonProperties.getDefaultLocale() });

          try
          {
            ClientRequestIF clientRequest = session.getRequest();

            String cache = SystemLogoSingletonDTO.getBannerFileFromCache(clientRequest, httpRequest);

            if (cache != null)
            {
              banner = cache;
            }
          }
          finally
          {
            session.logout();
          }
        }
      }
      catch (Exception e)
      {
        // Use the original banner location
      }

      httpRequest.setAttribute("banner", banner);
    }
  }
}
