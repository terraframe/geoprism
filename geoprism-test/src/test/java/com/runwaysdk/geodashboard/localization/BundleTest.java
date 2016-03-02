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
package com.runwaysdk.geodashboard.localization;

import net.geoprism.localization.Bundle;
import net.geoprism.localization.LocaleDimension;

import org.junit.Assert;
import org.junit.Test;

import com.runwaysdk.session.Request;

public class BundleTest
{
 
  @Test
  @Request
  public void testGetFromDefault()
  {
    Bundle bundle = new Bundle("messages", new LocaleDimension());

    Assert.assertEquals("New", bundle.getValue("com.runwaysdk.ui.userstable.UsersTable.newUser"));
  }

  @Test
  @Request
  public void testGetFromLocale()
  {
    Bundle bundle = new Bundle("messages", new LocaleDimension("en"));

    Assert.assertEquals("Test", bundle.getValue("com.runwaysdk.ui.userstable.UsersTable.newUser"));
  }
}
