package com.runwaysdk.geodashboard.oda.driver.session;

import com.runwaysdk.ClientSession;
import com.runwaysdk.constants.ClientRequestIF;

public class ClientSessionProxy implements IClientSession
{
  private String        url;

  private String        username;

  private ClientSession session;

  public ClientSessionProxy(String url, String username, ClientSession session)
  {
    this.url = url;
    this.username = username;
    this.session = session;
  }

  public ClientRequestIF getRequest()
  {
    return this.session.getRequest();
  }

  public void logout()
  {
    this.session.logout();
  }

  public String getKey()
  {
    return ClientSessionProxy.buildKey(this.url, this.username);
  }

  public static String buildKey(String url, String username)
  {
    return url + "-" + username;
  }
}
