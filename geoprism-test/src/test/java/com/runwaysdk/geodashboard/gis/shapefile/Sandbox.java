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
        country.getDisplayLabel().setValue("Cambodia");
        country.setUniversalId("Cambodia");
        country.apply();

        Universal municipality = new Universal();
        municipality.getDisplayLabel().setValue("Municipality");
        municipality.setUniversalId("Municipality");
        municipality.apply();

        Universal province = new Universal();
        province.getDisplayLabel().setValue("Province");
        province.setUniversalId("Province");
        province.apply();

        Universal district = new Universal();
        district.getDisplayLabel().setValue("District");
        district.setUniversalId("District");
        district.apply();

        Universal commune = new Universal();
        commune.getDisplayLabel().setValue("Commune");
        commune.setUniversalId("Commune");
        commune.apply();

        Universal village = new Universal();
        village.getDisplayLabel().setValue("Village");
        village.setUniversalId("village");
        village.apply();

        province.addLink(country, AllowedIn.CLASS);
        municipality.addLink(country, AllowedIn.CLASS);
        district.addLink(province, AllowedIn.CLASS);
        district.addLink(municipality, AllowedIn.CLASS);
        commune.addLink(district, AllowedIn.CLASS);
        village.addLink(commune, AllowedIn.CLASS);
      }
    }.execute();
  }
}
