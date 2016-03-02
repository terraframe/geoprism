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

import java.util.HashMap;

import net.geoprism.importer.GISImportLoggerIF;

public class MockLogger implements GISImportLoggerIF
{
  private HashMap<String, Throwable> map;

  private HashMap<String, String> messages;

  public MockLogger()
  {
    this.map = new HashMap<String, Throwable>();
    this.messages = new HashMap<String, String>();
  }

  @Override
  public boolean hasLogged()
  {
    return ( map.size() > 0 || messages.size() > 0);
  }

  @Override
  public void log(String featureId, Throwable t)
  {
    map.put(featureId, t);
  }
  
  @Override
  public void log(String featureId, String message)
  {
    messages.put(featureId, message);
  }


  @Override
  public void close()
  {
  }

  public HashMap<String, Throwable> getMap()
  {
    return map;
  }
  
  public HashMap<String, String> getMessages()
  {
    return messages;
  }
}
