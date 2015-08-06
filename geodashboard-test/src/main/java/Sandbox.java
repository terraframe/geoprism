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
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;

import com.runwaysdk.facade.Facade;
import com.runwaysdk.system.metadata.MdBusiness;

public class Sandbox
{
  public static void main(String[] args) throws Exception
  {
//    GeometryFactory gf = new GeometryFactory();
//
//    Coordinate coord = new Coordinate(1,1);
//    Point point = gf.createPoint(coord);
//
//    System.out.println(point);
    
    Locale[] locales = new Locale[1];
    
    locales[0] = Locale.ENGLISH;
    
    System.out.println("Logging in");
    String sessionId = Facade.login("admin", "_nm8P4gfdWxGqNRQ#8", locales);
    
    System.out.println("Logged in");
    
    String[] params = new String[0];
    
    InputStream inputStream = Facade.exportExcelFile(sessionId, MdBusiness.CLASS, null, null);
    
    System.out.println(inputStream);

    OutputStream outputStream = 
        new FileOutputStream(new File("/Users/nathan/git/geodashboard/geodashboard-test/excelTemplate.xls"));   
    
    try
    {
      int read = 0;
      byte[] bytes = new byte[1024];

      while ((read = inputStream.read(bytes)) != -1) 
      {
        outputStream.write(bytes, 0, read);
      }
    }
    finally
    {
      outputStream.close();
      
      System.out.println("Logging out");
      Facade.logout(sessionId);
    }
      
    System.out.println("Logging out");
    Facade.logout(sessionId);

    System.out.println("Logged out");
    
  }
}
