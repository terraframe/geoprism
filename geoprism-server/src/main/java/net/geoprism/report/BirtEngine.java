/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism.report;

import java.util.logging.Level;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportEngineFactory;



public class BirtEngine 
{
  private enum LogLevel  {
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