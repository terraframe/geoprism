package com.runwaysdk.geodashboard;

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
        ServerContextListenerDocumentBuilder builder = new ServerContextListenerDocumentBuilder();
        List<ServerContextListenerInfo> infos = builder.read();

        for (ServerContextListenerInfo info : infos)
        {
            System.out.println(info.getClassName());
            
            try
            {

                Class<?> clazz = LoaderDecorator.load(info.getClassName());
                Object newInstance = clazz.newInstance();

                Class<? extends Object> class1 = newInstance.getClass();
                ClassLoader loader1 = class1.getClassLoader();

                System.out.println("New instance class : " + class1.hashCode());
                System.out.println("New instance class loader: " + loader1.hashCode());

                Class<? extends Object> class2 = ServerContextListener.class;
                ClassLoader loader2 = class2.getClassLoader();

                System.out.println("Interface class : " + class2.hashCode());
                System.out.println("New instance class loader: " + loader2.hashCode());

                ServerContextListener listener = (ServerContextListener) newInstance;
                listener.startup();

                log.debug("COMLPETE: " + info.getClassName() + ".setup();");
            }
            catch (Exception e)
            {
                log.error(e);
                
                e.printStackTrace();

                //throw new ProgrammingErrorException("Unable to startup the server context listener [" + info.getClassName() + "]", e);
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

                log.debug("COMLPETE: " + info.getClassName() + ".setup();");

            }
            catch (Exception e)
            {
                log.error(e);

                throw new ProgrammingErrorException("Unable to startup the server context listener [" + info.getClassName() + "]", e);
            }
        }

    }
}
