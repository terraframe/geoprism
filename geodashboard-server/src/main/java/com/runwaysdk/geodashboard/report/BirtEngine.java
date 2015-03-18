package com.runwaysdk.geodashboard.report;

import java.util.logging.Level;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportEngineFactory;

import com.runwaysdk.generation.loader.Reloadable;

public class BirtEngine implements Reloadable
{
  private enum LogLevel implements Reloadable {
    SEVERE(Level.SEVERE), WARNING(Level.WARNING), INFO(Level.INFO), CONFIG(Level.CONFIG), FINE(Level.FINE), FINER(Level.FINER), FINEST(Level.FINEST), OFF(Level.OFF);

    private Level level;

    private LogLevel(Level level)
    {
      this.level = level;
    }

    public Level getLevel()
    {
      return level;
    }
  }

  private static IReportEngine engine = null;

  public static synchronized IReportEngine getBirtEngine(String logDirectory) throws BirtException
  {
    if (engine == null)
    {
      EngineConfig config = new EngineConfig();
      config.setLogConfig(logDirectory, LogLevel.SEVERE.getLevel());

      Platform.startup(config);

      IReportEngineFactory factory = (IReportEngineFactory) Platform.createFactoryObject(IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY);
      engine = factory.createReportEngine(config);
    }

    return engine;
  }

  public static synchronized void destroyBirtEngine()
  {
    if (engine == null)
    {
      return;
    }

    engine.destroy();
    Platform.shutdown();

    engine = null;
  }

  public Object clone() throws CloneNotSupportedException
  {
    throw new CloneNotSupportedException();
  }
}