package com.test;

import com.runwaysdk.session.Request;
import com.runwaysdk.system.gis.geo.Universal;


public class Sandbox
{
  @Request
  public static void main(String[] args)
  {
    Universal city = new Universal();
    city.getDisplayLabel().setDefaultValue("City");
    city.getDescription().setDefaultValue("City");
    city.setUniversalId("city");
    city.apply();
  }

}
