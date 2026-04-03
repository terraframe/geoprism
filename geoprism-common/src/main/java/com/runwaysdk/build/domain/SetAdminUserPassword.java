package com.runwaysdk.build.domain;

import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.session.Request;

import net.geoprism.GeoprismUserQuery;

public class SetAdminUserPassword
{
  public static void main(String[] args)
  {
    mainInRequest();
  }
  
  @Request
  public static void mainInRequest()
  {
    mainInTrans();
  }
  
  @Transaction
  public static void mainInTrans()
  {
    String adminUsername = System.getenv("GEOPRISM_ADMIN_USER");
    String adminPassword = System.getenv("GEOPRISM_ADMIN_PASSWORD");
    
    if (adminUsername == null && adminPassword == null)
    {
      System.out.println("WARNING : Environment variables GEOPRISM_ADMIN_USER and GEOPRISM_ADMIN_PASSWORD are unset. App will utilize defaults. Make sure to change the admin password!");
      return;
    }
    
    GeoprismUserQuery query = new GeoprismUserQuery(new QueryFactory());
    
    query.WHERE(query.getUsername().EQ("admin"));
    
    net.geoprism.GeoprismUser admin = query.getIterator().getAll().get(0);
    admin.appLock();
    
    if (adminUsername != null)
    {
      admin.setUsername(adminUsername);
    }
    
    if (adminPassword != null)
    {
      admin.setPassword(adminPassword);
    }
    
    admin.apply();
  }
}
