package com.runwaysdk.geodashboard.context;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.runwaysdk.constants.DeployProperties;
import com.runwaysdk.dataaccess.io.Versioning;
import com.runwaysdk.generation.loader.Reloadable;

public class PatchingContextListener implements Reloadable, ServerContextListener
{
  private static Logger logger = LoggerFactory.getLogger(PatchingContextListener.class);
  
  @Override
  public void startup()
  {
    File metadata = new File(DeployProperties.getDeployBin() + "/metadata");
    if (metadata.exists() && metadata.isDirectory())
    {
      logger.info("Importing metadata schema files from [" + metadata.getAbsolutePath() + "].");
      Versioning.main(new String[]{metadata.getAbsolutePath()});
    }
    else
    {
      logger.error("Metadata schema files were not found! Unable to import schemas.");
    }
  }

  @Override
  public void shutdown()
  {
    
  }
}
