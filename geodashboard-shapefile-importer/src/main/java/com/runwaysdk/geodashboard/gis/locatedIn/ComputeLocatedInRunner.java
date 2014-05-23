package com.runwaysdk.geodashboard.gis.locatedIn;

import java.util.LinkedList;

import com.runwaysdk.dataaccess.ValueObject;
import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.geodashboard.gis.Pair;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.session.Request;

public class ComputeLocatedInRunner implements Runnable, Reloadable
{
  private LinkedList<Pair<String, String>> list;

  private LocatedInBuilder                 builder;

  public ComputeLocatedInRunner(LocatedInBuilder builder)
  {
    this.builder = builder;
    this.list = new LinkedList<Pair<String, String>>();
  }

  @Request
  public void run()
  {
    OIterator<ValueObject> it = builder.deriveLocatedIn();

    try
    {
      while (it.hasNext())
      {
        ValueObject valueObject = it.next();

        String parentId = valueObject.getValue(LocatedInBuilder.PARENT_ID);
        String childId = valueObject.getValue(LocatedInBuilder.CHILD_ID);

        Pair<String, String> pair = new Pair<String, String>(parentId, childId);

        list.add(pair);
      }
    }
    finally
    {
      it.close();
    }
  }

  public LinkedList<Pair<String, String>> getList()
  {
    return list;
  }
}
