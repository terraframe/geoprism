package dss.vector.solutions.gis;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import com.runwaysdk.dataaccess.io.FileWriteException;

public class GISImportLogger implements GISImportLoggerIF
{
  private File           file;

  private BufferedWriter writer;

  private boolean        logged;

  public GISImportLogger(File file)
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
