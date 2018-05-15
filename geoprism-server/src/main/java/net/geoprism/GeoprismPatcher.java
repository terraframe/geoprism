package org.denversouthedp;

import java.io.File;

import com.runwaysdk.constants.DeployProperties;
import com.runwaysdk.session.Request;

import net.geoprism.GeoprismPatcher;

public class DsedpPatcher extends GeoprismPatcher
{
  public DsedpPatcher(File metadataDir)
  {
    super(metadataDir);
  }

  @Override
  protected String[] getModules()
  {
    return new String[] { "geoprism", "dsedp" };
  }

  public static void main(String[] args)
  {
    String metadataPath = null;

    if (args.length > 0)
    {
      metadataPath = args[0];
    }

    executeWithRequest(metadataPath);
  }

  @Request
  private static void executeWithRequest(String metadataPath)
  {
    File fMetadataPath = null;
    if (metadataPath == null)
    {
      metadataPath = DeployProperties.getDeployBin();
      fMetadataPath = new File(metadataPath, "metadata");
    }
    else
    {
      fMetadataPath = new File(metadataPath);
    }

    execute(fMetadataPath);
  }

  public static void execute(File metadataDir)
  {
    GeoprismPatcher listener = new DsedpPatcher(metadataDir);
    listener.initialize();
    listener.startup();
    listener.shutdown();
  }
}
