package com.runwaysdk.geodashboard.oda.driver;

import javax.net.ssl.SSLContext;

import com.runwaysdk.request.SslRMIClientSocketFactory;

public class SSLContextConfiguration
{
  public static void configure(SSLContext context)
  {
    SslRMIClientSocketFactory.setFactory(context.getSocketFactory());
  }

}
