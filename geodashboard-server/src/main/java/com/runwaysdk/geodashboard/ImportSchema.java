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
package com.runwaysdk.geodashboard;

import com.runwaysdk.dataaccess.io.Versioning;
import com.runwaysdk.dataaccess.io.dataDefinition.GISImportPlugin;
import com.runwaysdk.dataaccess.io.dataDefinition.SAXSourceParser;
import com.runwaysdk.geodashboard.service.GeodashboardImportPlugin;
import com.runwaysdk.session.Request;

public class ImportSchema
{
  @Request
  public static void main(String[] args)
  {
    SAXSourceParser.registerPlugin(new GISImportPlugin());
    SAXSourceParser.registerPlugin(new GeodashboardImportPlugin());

    Versioning.main(args);
  }
}
