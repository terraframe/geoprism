package com.runwaysdk.geodashboard;

import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.generation.loader.LoaderDecorator;
import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.session.Request;

public class ServerInitializer implements Reloadable
{

  private static final Log log = LogFactory.getLog(ServerInitializer.class);

  @Request
  public static void initialize()
  {
//    throw new RuntimeException("Test");
    
    ServerContextListenerDocumentBuilder builder = new ServerContextListenerDocumentBuilder();
    List<ServerContextListenerInfo> infos = builder.read();

    for (ServerContextListenerInfo info : infos)
    {
      try
      {

        Class<?> clazz = LoaderDecorator.load(info.getClassName());
        Object newInstance = clazz.newInstance();

        try
        {
          ServerContextListener listener = (ServerContextListener) newInstance;
          listener.startup();
        }
        catch (ClassCastException e)
        {
          log.error("ClassCastException initializer", e);

          Class<? extends Object> class1 = newInstance.getClass();
          ClassLoader loader1 = class1.getClassLoader();

          log.debug("New instance class : " + class1.hashCode());
          log.debug("New instance class loader: " + loader1.hashCode());

          Class<? extends Object> class2 = ServerContextListener.class;
          ClassLoader loader2 = class2.getClassLoader();

          log.debug("Interface class : " + class2.hashCode());
          log.debug("New instance class loader: " + loader2.hashCode());

          clazz.getMethod("startup").invoke(newInstance);
        }

        log.debug("COMLPETE: " + info.getClassName() + ".setup();");
      }
      catch (Exception e)
      {
        log.error(e);

        throw new ProgrammingErrorException("Unable to startup the server context listener [" + info.getClassName() + "]", e);
      }
    }
  }

  @Request
  public static void destroy()
  {
    ServerContextListenerDocumentBuilder builder = new ServerContextListenerDocumentBuilder();
    List<ServerContextListenerInfo> infos = builder.read();
    
    Collections.reverse(infos);

    for (ServerContextListenerInfo info : infos)
    {
      try
      {

        Class<?> clazz = LoaderDecorator.load(info.getClassName());
        Object newInstance = clazz.newInstance();

        try
        {
          ServerContextListener listener = (ServerContextListener) newInstance;
          listener.shutdown();
        }
        catch (ClassCastException e)
        {
          log.error("ClassCastException in ServerInitializer.shutdown", e);

          Class<? extends Object> class1 = newInstance.getClass();
          ClassLoader loader1 = class1.getClassLoader();

          log.debug("New instance class : " + class1.hashCode());
          log.debug("New instance class loader: " + loader1.hashCode());

          Class<? extends Object> class2 = ServerContextListener.class;
          ClassLoader loader2 = class2.getClassLoader();

          log.debug("Interface class : " + class2.hashCode());
          log.debug("New instance class loader: " + loader2.hashCode());

          clazz.getMethod("shutdown").invoke(newInstance);
        }

        log.debug("COMLPETE: " + info.getClassName() + ".shutdown();");
      }
      catch (Exception e)
      {
        log.error(e);

        throw new ProgrammingErrorException("Unable to shutdown the server context listener [" + info.getClassName() + "]", e);
      }
    }
  }
}
