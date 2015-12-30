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
package com.runwaysdk.geodashboard.context;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.runwaysdk.constants.DeployProperties;
import com.runwaysdk.constants.LocalProperties;
import com.runwaysdk.dataaccess.InstallerCP;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.io.Versioning;
import com.runwaysdk.dataaccess.io.XMLImporter;
import com.runwaysdk.dataaccess.io.dataDefinition.SAXSourceParser;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.geodashboard.service.GeodashboardImportPlugin;
import com.runwaysdk.util.ServerInitializerFacade;

public abstract class PatchingContextListener implements Reloadable, ServerContextListener
{
  private static Logger logger = LoggerFactory.getLogger(PatchingContextListener.class);

  @Override
  public void initialize()
  {
    LocalProperties.setSkipCodeGenAndCompile(true);

    if (!Database.tableExists("md_class"))
    {
      this.initializeDatabase();
    }
  }

  private void initializeDatabase()
  {
    try
    {
      InputStream schema = this.getClass().getResourceAsStream("/com/runwaysdk/resources/xsd/schema.xsd");

      InputStream[] xmlFilesIS = InstallerCP.buildMetadataInputStreamList();

      XMLImporter x = new XMLImporter(schema, xmlFilesIS);
      x.toDatabase();

      ServerInitializerFacade.rebuild();
    }
    catch (IOException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

  @Override
  public void startup()
  {
    SAXSourceParser.registerPlugin(new GeodashboardImportPlugin());

    this.patchMetadata();
  }

  protected String[] getModules()
  {
    return new String[] { "geodashboard" };
  }

  @Transaction
  protected void patchMetadata()
  {
    String[] modules = this.getModules();

    for (String module : modules)
    {
      File metadata = new File(DeployProperties.getDeployBin() + "/metadata/" + module);

      if (metadata.exists() && metadata.isDirectory())
      {
        logger.info("Importing metadata schema files from [" + metadata.getAbsolutePath() + "].");
        Versioning.main(new String[] { metadata.getAbsolutePath() });
      }
      else
      {
        logger.error("Metadata schema files were not found! Unable to import schemas.");
      }
    }
  }

  @Override
  public void shutdown()
  {

  }
}
