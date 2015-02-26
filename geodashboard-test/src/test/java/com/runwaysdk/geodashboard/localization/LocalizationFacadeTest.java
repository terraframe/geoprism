package com.runwaysdk.geodashboard.localization;

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
