package com.runwaysdk.geodashboard;

import java.util.List;

import org.junit.Test;

import com.runwaysdk.geodashboard.context.ServerContextListenerDocumentBuilder;
import com.runwaysdk.geodashboard.context.ServerContextListenerInfo;

public class ServerInitializerTest
{

  @Test
  public void test()
  {
    ServerContextListenerDocumentBuilder builder = new ServerContextListenerDocumentBuilder();
    List<ServerContextListenerInfo> infos = builder.read();

    for (ServerContextListenerInfo info : infos)
    {
      System.out.println(info.getClassName());
    }
  }
}
