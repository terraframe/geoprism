package net.geoprism;
/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Geoprism(tm).
 *
 * Geoprism(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Geoprism(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Geoprism(tm).  If not, see <http://www.gnu.org/licenses/>.
 */


import com.runwaysdk.dataaccess.io.Versioning;
import com.runwaysdk.dataaccess.io.dataDefinition.GISImportPlugin;
import com.runwaysdk.dataaccess.io.dataDefinition.SAXSourceParser;
import com.runwaysdk.session.Request;

public class ImportSchema
{
  @Request
  public static void main(String[] args)
  {
    SAXSourceParser.registerPlugin(new GISImportPlugin());
//    SAXSourceParser.registerPlugin(new GeoprismImportPlugin());

    Versioning.main(args);
  }
}
