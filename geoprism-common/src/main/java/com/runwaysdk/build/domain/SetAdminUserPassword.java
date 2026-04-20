/**
 * Copyright (c) 2023 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Geoprism(tm).
 *
 * Geoprism(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Geoprism(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Geoprism(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
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
