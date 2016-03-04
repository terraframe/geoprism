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
package net.geoprism.localization;

import net.geoprism.localization.LocalizationFacade;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import com.runwaysdk.session.Request;

public class LocalizationFacadeTest
{
  @Test
  public void testGetFromDefault()
  {
    Assert.assertEquals("Test", LocalizationFacade.getFromBundles("com.runwaysdk.ui.userstable.UsersTable.newUser"));
  }

  @Test
  public void testGetFromLocale()
  {
    Assert.assertEquals("Edit", LocalizationFacade.getFromBundles("com.runwaysdk.ui.userstable.UsersTable.editUser"));
  }

  @Test
  public void testGetJSON() throws JSONException
  {
    JSONObject object = new JSONObject(LocalizationFacade.getJSON());

    Assert.assertTrue(object.has("com.runwaysdk.ui.userstable.UsersTable"));

    JSONObject json = object.getJSONObject("com.runwaysdk.ui.userstable.UsersTable");

    Assert.assertNotNull(json);
    Assert.assertEquals("Edit", json.getString("editUser"));
  }

  @Test
  @Request
  public void testGetConfigurationJSON() throws Exception
  {
    JSONObject configuration = new JSONObject(LocalizationFacade.getConfigurationJSON());

    Assert.assertNotNull(configuration);

    JSONObject main = configuration.getJSONObject("main");

    Assert.assertNotNull(main);

    JSONObject en = main.getJSONObject("en");

    Assert.assertNotNull(en);

    JSONObject numbers = en.getJSONObject("numbers");

    Assert.assertNotNull(numbers);
  }

  @Test
  @Request
  public void testGetCalendarLocale() throws Exception
  {
    String locale = LocalizationFacade.getCalendarLocale();

    Assert.assertEquals("", locale);
  }
}
