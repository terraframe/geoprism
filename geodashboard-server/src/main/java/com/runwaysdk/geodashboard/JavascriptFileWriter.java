package com.runwaysdk.geodashboard;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;

import org.apache.commons.io.FileUtils;

import com.runwaysdk.business.generation.json.JSONFacade;
import com.runwaysdk.query.Condition;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.OR;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.session.Request;
import com.runwaysdk.session.SessionFacade;
import com.runwaysdk.system.metadata.MdClassQuery;
import com.runwaysdk.system.metadata.MdControllerQuery;
import com.runwaysdk.system.metadata.MdLocalizable;
import com.runwaysdk.system.metadata.MdType;
import com.runwaysdk.system.metadata.MdTypeQuery;

public class JavascriptFileWriter
{
  private String   destination;

  private String[] packageNames;

  public JavascriptFileWriter(String destination, String[] packageNames)
  {
    this.destination = destination;
    this.packageNames = packageNames;
  }

  @Request
  public void write()
  {
    String sessionId = SessionFacade.logIn("SYSTEM", "SYSTEM", new Locale[] { Locale.ENGLISH });

    try
    {
      write(sessionId);
    }
    finally
    {
      SessionFacade.closeSession(sessionId);
    }
  }

  private void write(String sessionId)
  {
    writeMdClasses(sessionId);

    writeMdControllers(sessionId);
  }

  private void writeMdClasses(String sessionId)
  {
    MdClassQuery query = new MdClassQuery(new QueryFactory());
    query.WHERE(query.getPublish().EQ(true));
    query.AND(this.getCondition(query));

    write(sessionId, query);
  }

  private void writeMdControllers(String sessionId)
  {
    MdControllerQuery query = new MdControllerQuery(new QueryFactory());
    query.AND(this.getCondition(query));

    write(sessionId, query);
  }

  private void write(String sessionId, MdTypeQuery query)
  {
    OIterator<? extends MdType> iterator = query.getIterator();

    try
    {
      while (iterator.hasNext())
      {
        MdType mdClass = iterator.next();

        write(sessionId, mdClass);
      }
    }
    finally
    {
      iterator.close();
    }
  }

  private Condition getCondition(MdTypeQuery query)
  {
    Condition condition = null;

    for (String packageName : packageNames)
    {
      if (condition == null)
      {
        condition = query.getPackageName().LIKE(packageName + "%");
      }
      else
      {
        condition = OR.get(condition, query.getPackageName().LIKE(packageName + "%"));
      }
    }
    return condition;
  }

  private void write(String sessionId, MdType mdClass)
  {
    if (! ( mdClass instanceof MdLocalizable ))
    {
      String type = mdClass.definesType();

//      String javascript = JSONFacade.importTypes(sessionId, new String[] { type });
      String path = destination + File.separator + type.replace(".", File.separator) + ".js";

//      try
//      {
        File file = new File(path);
        
        FileUtils.deleteQuietly(file);
//        file.getParentFile().mkdirs();
//
//        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
//
//        try
//        {
//          writer.write(javascript);
//        }
//        finally
//        {
//          writer.close();
//        }
//      }
//      catch (IOException e)
//      {
//        e.printStackTrace();
//      }
    }
  }

  public static void main(String[] args)
  {
    String destination = args[0];

    String[] packages = Arrays.copyOfRange(args, 1, args.length);

    new JavascriptFileWriter(destination, packages).write();
  }
}
