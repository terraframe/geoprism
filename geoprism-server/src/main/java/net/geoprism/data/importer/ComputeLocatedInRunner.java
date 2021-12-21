/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
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
package net.geoprism.data.importer;

import java.util.LinkedList;

import com.runwaysdk.dataaccess.ValueObject;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.session.Request;

public class ComputeLocatedInRunner implements Runnable
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

        String parentOid = valueObject.getValue(LocatedInBuilder.PARENT_ID);
        String childOid = valueObject.getValue(LocatedInBuilder.CHILD_ID);

        Pair<String, String> pair = new Pair<String, String>(parentOid, childOid);

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
