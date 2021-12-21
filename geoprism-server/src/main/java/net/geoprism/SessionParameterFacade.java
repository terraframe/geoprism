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

import java.util.HashMap;
import java.util.Map;

import com.runwaysdk.session.Session;
import com.runwaysdk.session.SessionIF;

public class SessionParameterFacade 
{
  private static Map<String, Map<String, String>> sessions = new HashMap<String, Map<String, String>>();

  private static synchronized Map<String, String> getSession(String sessionId)
  {
    if (!sessions.containsKey(sessionId))
    {
      sessions.put(sessionId, new HashMap<String, String>(2));
    }

    return sessions.get(sessionId);
  }

  public static synchronized void clear(String sessionId)
  {
    if (sessions.containsKey(sessionId))
    {
      sessions.remove(sessionId);
    }
  }

  public static void put(String key, String value)
  {
    Map<String, String> session = getSession();

    session.put(key, value);
  }

  public static String get(String key)
  {
    Map<String, String> session = getSession();

    return session.get(key);
  }

  public static boolean containsKey(String key)
  {
    Map<String, String> session = getSession();

    return session.containsKey(key);
  }

  private static Map<String, String> getSession()
  {
    String sessionId = getSessionId();

    return getSession(sessionId);
  }

  private static String getSessionId()
  {
    SessionIF session = Session.getCurrentSession();

    if (session != null)
    {
      return session.getOid();
    }

    return "";
  }

}
