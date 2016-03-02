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
package com.runwaysdk.geodashboard.gis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GISImportSLF4jLogger implements GISImportLoggerIF
{
  private Logger logger = LoggerFactory.getLogger(GISImportSLF4jLogger.class);

  private boolean        logged;
  
  public GISImportSLF4jLogger()
  {
    this.logged = false;
  }

  @Override
  public void close()
  {
    // NO OP
  }

  @Override
  public boolean hasLogged()
  {
    return this.logged;
  }

  @Override
  public void log(String featureId, Throwable t)
  {
    this.logged = true;
    logger.error(featureId, t);
  }

  @Override
  public void log(String featureId, String message)
  {
    this.logged = true;
    logger.error(featureId + " : " + message);
  }
}
