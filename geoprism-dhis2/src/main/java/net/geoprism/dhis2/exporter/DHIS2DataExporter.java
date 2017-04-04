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
import org.apache.commons.lang.StringUtils;

import com.runwaysdk.configuration.ConfigurationManager;
import com.runwaysdk.session.Request;
import com.runwaysdk.system.metadata.MdBusiness;
import com.runwaysdk.system.metadata.MdClass;

import net.geoprism.account.ExternalProfile;
import net.geoprism.configuration.GeoprismConfigurationResolver;
import net.geoprism.data.GeoprismDatasetExporterIF;
import net.geoprism.data.etl.TargetBuilder;
import net.geoprism.dhis2.DHIS2HTTPConnector;
import net.geoprism.dhis2.response.OAuthLoginRequiredException;

public class DHIS2DataExporter implements GeoprismDatasetExporterIF
{
  private DHIS2HTTPConnector dhis2;
  
  @Request
  public static void main(String[] args)
  {
    CommandLineParser parser = new DefaultParser();
    Options options = new Options();
    options.addOption(Option.builder("url").hasArg().argName("url").longOpt("url").desc("URL of the DHIS2 server to connect to, including the port. Defaults to: http://127.0.0.1:8085/").optionalArg(true).build());
    options.addOption(Option.builder("username").hasArg().argName("username").longOpt("username").desc("The username of the root (admin) DHIS2 user.").required().build());
    options.addOption(Option.builder("password").hasArg().argName("password").longOpt("password").desc("The password for the root (admin) DHIS2 user.").required().build());
    options.addOption(Option.builder("appcfgPath").hasArg().argName("appcfgPath").longOpt("appcfgPath").desc("An absolute path to the external configuration directory for this geoprism app.").optionalArg(true).build());
    options.addOption(Option.builder("dataset").hasArg().argName("dataset").longOpt("dataset").desc("The name of the dataset to export.").required().build());
    
    try {
      CommandLine line = parser.parse( options, args );
      
      String url = line.getOptionValue("url");
      String username = line.getOptionValue("username");
      String password = line.getOptionValue("password");
      String appcfgPath = line.getOptionValue("appcfgPath");
      String dataset = StringUtils.capitalize(line.getOptionValue("dataset").toLowerCase());
      
      if (url == null)
      {
        url = "http://127.0.0.1:8085/";
      }
      if (appcfgPath != null)
      {
        GeoprismConfigurationResolver resolver = (GeoprismConfigurationResolver) ConfigurationManager.Singleton.INSTANCE.getConfigResolver();
        resolver.setExternalConfigDir(new File(appcfgPath));
      }
      
      MdBusiness mdBiz = MdBusiness.getMdBusiness(TargetBuilder.PACKAGE_NAME + "." + dataset);
      
      DHIS2DataExporter exporter = new DHIS2DataExporter();
      exporter.exportWithCredentials(mdBiz, url, username, password);
    }
    catch (ParseException e)
    {
      throw new RuntimeException(e);
    }
  }
  
  // Our constructor must be 0 arguments because it conforms to Java service loader paradigm.
  public DHIS2DataExporter()
  {
    dhis2 = new DHIS2HTTPConnector();
  }
  
  public void exportWithCredentials(MdClass mdClass, String url, String username, String password)
  {
    dhis2.setServerUrl(url);
    dhis2.setCredentials(username, password);
    
    MdBusinessExporter exporter = new MdBusinessExporter((MdBusiness) mdClass, dhis2);
    exporter.exportToTracker();
  }
  
  @Override
  public void xport(MdClass mdClass)
  {
    if (ExternalProfile.getAccessToken() == null)
    {
      OAuthLoginRequiredException ex = new OAuthLoginRequiredException();
      throw ex;
    }
    
    MdBusinessExporter exporter = new MdBusinessExporter((MdBusiness) mdClass, dhis2);
    exporter.exportToTracker();
  }
}
