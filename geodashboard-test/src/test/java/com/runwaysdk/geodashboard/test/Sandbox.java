package com.runwaysdk.geodashboard.test;

import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.geodashboard.gis.persist.DashboardMap;
import com.runwaysdk.session.Request;

public class Sandbox
{
  public static void main(String[] args)
  {
    testBuildMap();
  }
  
  @Request
  @Transaction
  public static void testBuildMap()
  {
    System.out.println("testBuildMap");
    
     DashboardMap map = new DashboardMap();
//     map.setName("Test Map");
//     map.apply();
  }
}
