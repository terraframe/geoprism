package net.geoprism.account;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class OauthBridge
{
  private static List<OauthHandlerIF> handlers = new LinkedList<OauthHandlerIF>();

  public synchronized static void addHandler(OauthHandlerIF handler)
  {
    handlers.add(handler);
  }

  public synchronized static String createSession(String serverId, String code, Locale[] locales) throws Exception
  {
    for (OauthHandlerIF handler : handlers)
    {
      return handler.createSession(serverId, code, locales);
    }

    return null;
  }
}
