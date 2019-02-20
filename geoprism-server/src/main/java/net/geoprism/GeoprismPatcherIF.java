package net.geoprism;

import java.io.File;

public interface GeoprismPatcherIF
{
  public void initialize(File metadataDir);
  
  public void initialize(String[] cliArgs);
  
  public void run();
}
