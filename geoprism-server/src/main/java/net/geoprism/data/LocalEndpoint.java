/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
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
package net.geoprism.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.util.IDGenerator;

public class LocalEndpoint implements XMLEndpoint
{
  private File root;

  public LocalEndpoint(File root)
  {
    this.root = root;
  }

  private String getPrefix(String country, String version, String type)
  {
    return country + File.separator + "xml" + File.separator + version + File.separator + type + File.separator;
  }

  @Override
  public List<String> listUniversalKeys(String country, String version)
  {
    String prefix = this.getPrefix(country, version, "universals");

    return this.listFiles(prefix);
  }

  @Override
  public List<String> listGeoEntityKeys(String country, String version)
  {
    String prefix = this.getPrefix(country, version, "geoentities");

    return this.listFiles(prefix);
  }

  @Override
  public File copyFiles(List<String> keys)
  {
    File directory = new File(FileUtils.getTempDirectory(), IDGenerator.nextID());
    directory.mkdirs();

    this.copyFiles(directory, keys, false);

    return directory;
  }

  @Override
  public void copyFiles(File directory, List<String> paths, boolean preserveDirectories)
  {
    try
    {
      List<File> files = new LinkedList<File>();

      for (String path : paths)
      {
        InputStream istream = new FileInputStream(new File(this.root, path));

        try
        {
          String targetPath = this.getTargetPath(preserveDirectories, path);
          File file = new File(directory, targetPath);

          FileUtils.copyInputStreamToFile(istream, file);

          files.add(file);
        }
        finally
        {
          // Process the objectData stream.
          istream.close();
        }
      }
    }
    catch (IOException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

  private String getTargetPath(boolean preserveDirectories, String key)
  {
    if (preserveDirectories)
    {
      return key;
    }
    else
    {
      return new File(key).getName();
    }
  }

  private List<String> listFiles(String prefix)
  {
    LinkedList<String> paths = new LinkedList<String>();

    FilenameFilter filter = new FilenameFilter()
    {
      @Override
      public boolean accept(File dir, String name)
      {
        return name.endsWith(".xml.gz");
      }
    };

    File directory = new File(this.root, prefix);

    File[] files = directory.listFiles(filter);

    if (files != null)
    {
      for (File file : files)
      {
        paths.add(this.root.toURI().relativize(file.toURI()).getPath());
      }
    }

    return paths;
  }
}
