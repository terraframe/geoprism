package com.runwaysdk.geodashboard.gis.shapefile;

import com.runwaysdk.geodashboard.gis.TransactionExecuter;
import com.runwaysdk.system.gis.geo.AllowedIn;
import com.runwaysdk.system.gis.geo.Universal;

public class Sandbox
{
  public static void main(String[] args) throws Exception
  {
    new TransactionExecuter()
    {
      @Override
      protected void executeMethod() throws Exception
      {
        Universal.getStrategy().initialize(AllowedIn.CLASS);

        Universal country = new Universal();
        country.getDisplayLabel().setValue("USA");
        country.setUniversalId("USA");
        country.apply();

        Universal city = new Universal();
        city.getDisplayLabel().setValue("City");
        city.setUniversalId("City");
        city.apply();

        Universal bike = new Universal();
        bike.getDisplayLabel().setValue("Bike depot");
        bike.setUniversalId("Bike depot");
        bike.apply();

        city.addLink(country, AllowedIn.CLASS);
        bike.addLink(city, AllowedIn.CLASS);
      }
    }.execute();
  }
}
