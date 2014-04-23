package com.runwaysdk.geodashboard.localization;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

public class MultiBundleTest
{
  @Test
  public void testGetFromDefault()
  {
    Assert.assertEquals("Test", MultiBundle.get("com.runwaysdk.ui.userstable.UsersTable.newUser"));
  }

  @Test
  public void testGetFromLocale()
  {
    Assert.assertEquals("Edit", MultiBundle.get("com.runwaysdk.ui.userstable.UsersTable.editUser"));
  }

  @Test
  public void testGetAll()
  {    
    Map<String, String> properties = MultiBundle.getAll();
    
    Assert.assertEquals("Test", properties.get("com.runwaysdk.ui.userstable.UsersTable.newUser"));
    Assert.assertEquals("Edit", properties.get("com.runwaysdk.ui.userstable.UsersTable.editUser"));
    Assert.assertEquals("Value", properties.get("test"));
  }
}
