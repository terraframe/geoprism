package com.runwaysdk.geodashboard.oda.driver.session;

import com.runwaysdk.constants.ClientRequestIF;

public interface IClientSession
{
  public static final String SESSION_ID = "SESSION_ID";

  public ClientRequestIF getRequest();

  public void logout();

  public String getKey();

}
