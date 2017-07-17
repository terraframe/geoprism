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
package net.geoprism.dhis2;

import net.geoprism.dhis2.util.DHIS2IdFinder;

public class DHIS2Plugin implements DHIS2PluginIF
{

  @Override
  public String findAttributes()
  {
    return DHIS2IdFinder.findAttributes();
  }

  @Override
  public String findPrograms()
  {
    return DHIS2IdFinder.findPrograms();
  }

  @Override
  public String findTrackedEntities()
  {
    return DHIS2IdFinder.findTrackedEntities();
  }

}
