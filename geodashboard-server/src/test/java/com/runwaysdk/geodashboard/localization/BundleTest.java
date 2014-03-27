package com.runwaysdk.geodashboard.localization;

import org.junit.Assert;
import org.junit.Test;

import com.runwaysdk.session.Request;

public class BundleTest
{
  @Test
  @Request
  public void testGetFromDefault()
  {
    Bundle bundle = new Bundle("localize", new LocaleDimension());

    Assert.assertEquals("New", bundle.getValue("com.runwaysdk.ui.userstable.UsersTable.newUser"));
  }

  @Test
  @Request
  public void testGetFromLocale()
  {
    Bundle bundle = new Bundle("localize", new LocaleDimension("en"));

    Assert.assertEquals("Test", bundle.getValue("com.runwaysdk.ui.userstable.UsersTable.newUser"));
  }
}
