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
package net.geoprism.data.importer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import com.runwaysdk.dataaccess.io.FileWriteException;
import com.runwaysdk.util.FileIO;

public class FileImportLogger implements GISImportLoggerIF
{
  private File           file;

  private BufferedWriter writer;

  private boolean        logged;

  public FileImportLogger(File file)
  {
    try
    {
      this.file = file;
      this.writer = new BufferedWriter(new FileWriter(file));
      this.logged = false;
    }
    catch (IOException e)
    {
      throw new FileWriteException("Unable to write log file [" + file.getAbsolutePath() + "]", file);
    }
  }

  @Override
  public void close()
  {
    try
    {
      this.writer.close();
    }
    catch (IOException e)
    {
      throw new FileWriteException(file, e);
    }

    if (!this.hasLogged())
    {
      try
      {
        FileIO.deleteFile(file);
      }
      catch (IOException e)
      {
        throw new FileWriteException(file, e);
      }
    }
  }

  @Override
  public boolean hasLogged()
  {
    return this.logged;
  }

  @Override
  public void log(String featureId, Throwable t)
  {
    if (t instanceof InvocationTargetException)
    {
      this.log(featureId, t.getCause());
    }
    else
    {
      this.logged = true;

      try
      {
        writer.write(featureId + " : " + t.getLocalizedMessage() + "\n");
      }
      catch (IOException ex)
      {
        throw new FileWriteException(file, ex);
      }
    }
  }

  @Override
  public void log(String featureId, String message)
  {
    this.logged = true;

    try
    {
      writer.write(featureId + " : " + message + "\n");
    }
    catch (IOException ex)
    {
      throw new FileWriteException(file, ex);
    }
  }
}
