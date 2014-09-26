package com.runwaysdk.geodashboard.oda.driver;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.runwaysdk.ClientSession;
import com.runwaysdk.request.ClientRequestManager;
import com.runwaysdk.request.ConnectionLabel;
import com.runwaysdk.request.ConnectionLabel.Type;

public class ClientSessionCache
{
  private static ClientSessionCache       instance;

  private Map<String, ClientSessionProxy> cache;

  private ClientSessionCache()
  {
    this.cache = new HashMap<String, ClientSessionProxy>();
  }

  private boolean has(String url, String username)
  {
    String key = this.getKey(url, username);

    return this.cache.containsKey(key);
  }

  private ClientSessionProxy get(String url, String username)
  {
    String key = this.getKey(url, username);

    return this.cache.get(key);
  }

  private void add(String url, String username, ClientSession session)
  {
    String key = this.getKey(url, username);

    this.cache.put(key, new ClientSessionProxy(url, username, session));
  }

  private void remove(String url, String username)
  {
    String key = this.getKey(url, username);

    this.cache.remove(key);
  }

  private String getKey(String url, String username)
  {
    return url + "-" + username;
  }

  private static synchronized ClientSessionCache getInstance()
  {
    if (instance == null)
    {
      instance = new ClientSessionCache();
    }

    return instance;
  }

  public static ClientSessionProxy getClientSession(String url, String username, String password)
  {
    if (!getInstance().has(url, username))
    {
      long time = System.currentTimeMillis();
      String label = "BIRT-RMI-" + time;
      ClientRequestManager.addConnection(new ConnectionLabel(label, Type.RMI, url));

      ClientSession session = ClientSession.createUserSession(label, username, password, new Locale[] { Locale.US });

      getInstance().add(url, username, session);
    }

    return getInstance().get(url, username);
  }

  public static void close(String url, String username)
  {
    if (getInstance().has(url, username))
    {
      getInstance().remove(url, username);
    }
  }
}
