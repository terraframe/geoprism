package com.runwaysdk.geodashboard.localization;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

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
}
