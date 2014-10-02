package com.runwaysdk.geodashboard.oda.driver.session;

import com.runwaysdk.ClientSession;
import com.runwaysdk.constants.ClientRequestIF;

public class ExistingClientSession implements IClientSession
{
  private String        url;

  private ClientSession session;

  public ExistingClientSession(String url, ClientSession session)
  {
    this.url = url;
    this.session = session;
  }

  @Override
  public ClientRequestIF getRequest()
  {
    return this.session.getRequest();
  }

  @Override
  public void logout()
  {
    // Do nothing this is an existing session
  }

  public String getKey()
  {
    return ExistingClientSession.buildKey(this.url, this.session.getSessionId());
  }

  public static String buildKey(String url, String sessionId)
  {
    return url + "-" + sessionId;
  }

}
