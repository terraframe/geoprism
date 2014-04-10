package com.runwaysdk.geodashboard.gis;

import java.lang.reflect.Constructor;
import java.util.Locale;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.widgets.Display;

import com.runwaysdk.generation.loader.LoaderDecorator;
import com.runwaysdk.generation.loader.Reloadable;

public class GISManagerLauncher
{
  private static class Arguments
  {
    private Locale locale;

    @SuppressWarnings("static-access")
    public Arguments(String[] args) throws ParseException
    {
      this.locale = Locale.getDefault();

      Options options = new Options();
      options.addOption(OptionBuilder.withDescription("LOCALE of app localization").hasArg().withArgName("LOCALE").create("l"));

      CommandLineParser parser = new PosixParser();
      CommandLine cmd = parser.parse(options, args);

      if (cmd.hasOption("l"))
      {
        String value = cmd.getOptionValue("l");
        String[] localeInfo = value.split("_");

        switch (localeInfo.length)
        {
          case 1:
            locale = new Locale(localeInfo[0]);
            break;
          case 2:
            locale = new Locale(localeInfo[0], localeInfo[1]);
            break;
          case 3:
            locale = new Locale(localeInfo[0], localeInfo[1], localeInfo[2]);
            break;
        }
      }

    }

    public Locale getLocale()
    {
      return locale;
    }
  }

  public static void main(String[] args) throws Exception
  {
    Arguments arguments = new Arguments(args);
    Localizer.setInstance("localization", arguments.getLocale());

    final Display display = Display.getDefault();

    class WindowRunner implements Runnable, Reloadable
    {
      public void run()
      {
        try
        {
          Class<?> clazz = LoaderDecorator.load("com.runwaysdk.geodashboard.gis.GISManagerWindow");

          Constructor<?> constructor = clazz.getConstructor();
          Object instance = constructor.newInstance();
          clazz.getMethod("run").invoke(instance);
        }
        catch (RuntimeException e)
        {
          throw e;
        }
        catch (Exception e)
        {
          throw new RuntimeException(e);
        }
      }
    }

    Realm.runWithDefault(SWTObservables.getRealm(display), new WindowRunner());
  }

}
