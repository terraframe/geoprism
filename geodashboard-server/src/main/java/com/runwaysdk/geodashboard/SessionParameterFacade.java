package com.runwaysdk.geodashboard;

import java.util.HashMap;
import java.util.Map;

import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.session.Session;

public class SessionParameterFacade implements Reloadable
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
    return Session.getCurrentSession().getId();
  }

}
