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
package net.geoprism.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.util.IDGenerator;

public class CachedEndpoint implements XMLEndpoint
{
  private XMLEndpoint endpoint;

  private File        cacheDirectory;

  public CachedEndpoint(XMLEndpoint endpoint, File cacheDirectory)
  {
    this.endpoint = endpoint;
    this.cacheDirectory = cacheDirectory;
  }

  @Override
  public List<String> listUniversalKeys(String country, String version)
  {
    return this.endpoint.listUniversalKeys(country, version);
  }

  @Override
  public List<String> listGeoEntityKeys(String country, String version)
  {
    return this.endpoint.listGeoEntityKeys(country, version);
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
    if (!this.cacheDirectory.exists())
    {
      this.cacheDirectory.mkdirs();
    }

    List<String> pathsToRetrieve = new LinkedList<String>();

    for (String path : paths)
    {
      if (!new File(this.cacheDirectory, path).exists())
      {
        pathsToRetrieve.add(path);
      }
    }

    if (pathsToRetrieve.size() > 0)
    {
      this.endpoint.copyFiles(this.cacheDirectory, pathsToRetrieve, true);
    }

    try
    {
      List<File> files = new LinkedList<File>();

      for (String path : paths)
      {
        InputStream istream = new FileInputStream(new File(this.cacheDirectory, path));

        try
        {
          String name = new File(path).getName();
          File file = new File(directory, name);

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
}
