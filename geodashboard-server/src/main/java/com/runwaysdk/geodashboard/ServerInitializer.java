package com.runwaysdk.geodashboard;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.runwaysdk.generation.loader.LoaderDecorator;
import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.session.Request;

public class ServerInitializer implements Reloadable
{

  private static final Log log = LogFactory.getLog(ServerInitializer.class);

  @Request
  public static void initialize()
  {
    ServerContextListenerDocumentBuilder builder = new ServerContextListenerDocumentBuilder();
    List<ServerContextListenerInfo> infos = builder.read();

    for (ServerContextListenerInfo info : infos)
    {
      try
      {
        Class<?> clazz = LoaderDecorator.load(info.getClassName());
        ServerContextListener listener = (ServerContextListener) clazz.newInstance();
        listener.startup();

        log.debug("COMLPETE: " + info.getClassName() + ".setup();");
      }
      catch (Exception e)
      {
        // TODO Add a better error message
        throw new RuntimeException("Unable to startup the server context listener [" + info.getClassName() + "]", e);
      }
    }
  }

  @Request
  public static void destroy()
  {
    ServerContextListenerDocumentBuilder builder = new ServerContextListenerDocumentBuilder();
    List<ServerContextListenerInfo> infos = builder.read();

    for (ServerContextListenerInfo info : infos)
    {
      try
      {
        Class<?> clazz = LoaderDecorator.load(info.getClassName());
        ServerContextListener listener = (ServerContextListener) clazz.newInstance();
        listener.shutdown();
      }
      catch (Exception e)
      {
        // TODO Add a better error message
        throw new RuntimeException("Unable to shutdown the server context listener [" + info.getClassName() + "]", e);
      }
    }

  }
}
