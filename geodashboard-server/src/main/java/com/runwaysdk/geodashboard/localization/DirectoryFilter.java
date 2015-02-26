package com.runwaysdk.geodashboard.localization;

import java.io.File;
import java.io.FileFilter;

import com.runwaysdk.generation.loader.Reloadable;

public class DirectoryFilter implements Reloadable, FileFilter
{
  @Override
  public boolean accept(File file)
  {
    return file.isDirectory();
  }

}
