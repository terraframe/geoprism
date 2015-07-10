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
package com.runwaysdk.geodashboard.test;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import com.runwaysdk.session.Request;
import com.runwaysdk.system.metadata.MdBusiness;
//import com.example.AdvancedDashboard;
//import com.example.BasicDashboard;
//import com.example.DashboardView;
//import com.example.DashboardViewDTO;

public class ValueQueryTest
{
  private static MdBusiness basicDashboard;

  private static MdBusiness advancedDashboard;

  @BeforeClass
  @Request
  public static void classStartup()
  {
//    basicDashboard = MdBusiness.getMdBusiness(BasicDashboard.CLASS);
//    advancedDashboard = MdBusiness.getMdBusiness(AdvancedDashboard.CLASS);
  }

  @AfterClass
  public static void classTeardown()
  {

  }
/*
  @Test
  @Request
  public void test1()
  {
    MdBusiness mdBusiness = MdBusiness.getMdBusiness(BasicDashboard.CLASS);

    ValueQuery values = DashboardView.getValues(mdBusiness.getId());

    OIterator<ValueObject> iterator = values.getIterator();

    try
    {
      int count = 0;

      while (iterator.hasNext())
      {
        ValueObject value = iterator.next();

        System.out.println("Result " + count + ": " + value.getValue(BasicDashboard.TESTCHAR) + ", " + value.getValue(BasicDashboard.TESTINTEGER));
      }
    }
    finally
    {
      iterator.close();
    }
  }

  @Test
  public void test2()
  {
    ClientSession session = ClientSession.createUserSession("admin", "admin", new Locale[] { Locale.US });

    try
    {
      ClientRequestIF request = session.getRequest();

      ValueQueryDTO query = DashboardViewDTO.getValues(request, basicDashboard.getId());

      List<ValueObjectDTO> results = query.getResultSet();
      Iterator<ValueObjectDTO> iterator = results.iterator();

      int count = 0;

      while (iterator.hasNext())
      {
        ValueObjectDTO value = iterator.next();

        System.out.println("Result " + count + ": " + value.getValue(BasicDashboard.TESTCHAR) + ", " + value.getValue(BasicDashboard.TESTINTEGER));
      }
    }
    finally
    {
      session.logout();
    }
  }

  @Test
  @Request
  public void testPopulator()
  {
    for (int i = 0; i < 100; i++)
    {
      BasicDashboard basic = new BasicDashboard();
      basic.setTestChar("Test basic character : " + ( i % 2 ));
      basic.setTestInteger(i);
      basic.apply();

      AdvancedDashboard advanced = new AdvancedDashboard();
      advanced.setAdvancedChar("Test advanced character : " + i);
      advanced.apply();
    }
  }
  */
}
