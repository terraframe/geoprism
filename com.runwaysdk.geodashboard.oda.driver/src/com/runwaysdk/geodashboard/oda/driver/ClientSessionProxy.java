package com.runwaysdk.geodashboard.oda.driver;

import com.runwaysdk.ClientSession;
import com.runwaysdk.constants.ClientRequestIF;

public class ClientSessionProxy
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

    ClientSessionCache.close(this.url, this.username);
  }

}
