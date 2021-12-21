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
package net.geoprism.oda.driver.session;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.runwaysdk.ClientSession;
import com.runwaysdk.configuration.CommonsConfigurationResolver;
import com.runwaysdk.request.ClientRequestManager;
import com.runwaysdk.request.ConnectionLabel;
import com.runwaysdk.request.ConnectionLabel.Type;

public class ClientSessionCache
{
  private static ClientSessionCache   instance;

  private Map<String, IClientSession> cache;

  private ClientSessionCache()
  {
    this.cache = new HashMap<String, IClientSession>();
  }

  private boolean has(String key)
  {
    return this.cache.containsKey(key);
  }

  private IClientSession get(String key)
  {
    return this.cache.get(key);
  }

  private void add(String key, IClientSession session)
  {
    this.cache.put(key, session);
  }

  private void remove(String key)
  {
    this.cache.remove(key);
  }

  private static synchronized ClientSessionCache getInstance()
  {
    if (instance == null)
    {
      instance = new ClientSessionCache();
    }

    return instance;
  }

  public static IClientSession getClientSession(String url, String username, String password)
  {
    String key = ClientSessionProxy.buildKey(url, username);

    if (!getInstance().has(key))
    {
      String label = getConnectionLabel(url);

      ClientSession session = ClientSession.createUserSession(label, username, password, new Locale[] { Locale.US });

      getInstance().add(key, new ClientSessionProxy(url, username, session));
    }

    return getInstance().get(key);
  }

  public static IClientSession getClientSession(String url, String sessionId)
  {
    String key = ExistingClientSession.buildKey(url, sessionId);

    if (!getInstance().has(key))
    {
      String label = getConnectionLabel(url);

      ClientSession session = ClientSession.getExistingSession(label, sessionId, new Locale[] { Locale.US });

      getInstance().add(key, new ExistingClientSession(url, session));
    }

    return getInstance().get(key);
  }

  public static void close(IClientSession session)
  {
    if (getInstance().has(session.getKey()))
    {
      getInstance().remove(session.getKey());
    }
  }

  private static String getConnectionLabel(String url)
  {
    String prop = System.getProperty("birt-server");

    if (prop != null && prop.equals("true"))
    {
      return "default";
    }
    else
    {
      long time = System.currentTimeMillis();
      String label = "BIRT-RMI-" + time;

      CommonsConfigurationResolver.setIncludeRuntimeProperties(false);

      ClientRequestManager.addConnection(new ConnectionLabel(label, Type.RMI, url));
      return label;
    }
  }
}
