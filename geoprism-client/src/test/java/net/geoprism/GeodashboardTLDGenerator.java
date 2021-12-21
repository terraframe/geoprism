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
package net.geoprism;

import java.io.File;
import java.io.IOException;

import net.geoprism.LocalizedTagSupport;

import com.runwaysdk.controller.tag.develop.TLDGenerator;

public class GeodashboardTLDGenerator
{
  public static void main(String[] args)
  {
    try
    {
      new TLDGenerator(new File(args[0]), new Class<?>[] { LocalizedTagSupport.class }, "Geodashboard").generate();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }

}
