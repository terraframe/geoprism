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
package net.geoprism.dhis2.exporter;

import java.io.File;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.runwaysdk.configuration.ConfigurationManager;
import com.runwaysdk.session.Request;
import com.runwaysdk.system.metadata.MdBusiness;

import net.geoprism.configuration.GeoprismConfigurationResolver;
import net.geoprism.data.etl.TargetBuilder;
import net.geoprism.dhis2.DHIS2BasicConnector;

public class DHIS2DataExporter
{
  private DHIS2BasicConnector dhis2;
  
  public static void main(String[] args)
  {
    CommandLineParser parser = new DefaultParser();
    Options options = new Options();
    options.addOption(Option.builder("url").hasArg().argName("url").longOpt("url").desc("URL of the DHIS2 server to connect to, including the port. Defaults to: http://127.0.0.1:8085/").optionalArg(true).build());
    options.addOption(Option.builder("username").hasArg().argName("username").longOpt("username").desc("The username of the root (admin) DHIS2 user.").required().build());
    options.addOption(Option.builder("password").hasArg().argName("password").longOpt("password").desc("The password for the root (admin) DHIS2 user.").required().build());
    options.addOption(Option.builder("appcfgPath").hasArg().argName("appcfgPath").longOpt("appcfgPath").desc("An absolute path to the external configuration directory for this geoprism app.").optionalArg(true).build());
    
    try {
      CommandLine line = parser.parse( options, args );
      
      String url = line.getOptionValue("url");
      String username = line.getOptionValue("username");
      String password = line.getOptionValue("password");
      String appcfgPath = line.getOptionValue("appcfgPath");
      
      if (url == null)
      {
        url = "http://127.0.0.1:8085/";
      }
      if (appcfgPath != null)
      {
        GeoprismConfigurationResolver resolver = (GeoprismConfigurationResolver) ConfigurationManager.Singleton.INSTANCE.getConfigResolver();
        resolver.setExternalConfigDir(new File(appcfgPath));
      }
      
      new DHIS2DataExporter(url, username, password).exportToTrackerInRequest(TargetBuilder.PACKAGE_NAME + ".LaborForceByState2015");
    }
    catch (ParseException e)
    {
      throw new RuntimeException(e);
    }
  }
  
  public DHIS2DataExporter(String url, String username, String password)
  {
    dhis2 = new DHIS2BasicConnector(url, username, password);
  }
  
  @Request
  public void exportToTrackerInRequest(String mdbiz)
  {
    exportToTracker(MdBusiness.getMdBusiness(mdbiz));
  }
  
  public void exportToTracker(MdBusiness mdbiz)
  {
    dhis2.initialize();
    exportMetadata(mdbiz);
    // exportData(mdbiz);
  }
  
  public void exportMetadata(MdBusiness mdbiz)
  {
    MdBusinessExporter exporter = new MdBusinessExporter(mdbiz, dhis2);
    exporter.exportToTracker();
  }
}
