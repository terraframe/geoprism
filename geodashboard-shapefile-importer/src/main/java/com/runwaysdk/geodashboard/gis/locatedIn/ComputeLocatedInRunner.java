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
