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
package com.runwayskd.geodashboard.etl;

import java.util.LinkedList;
import java.util.List;

import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.system.metadata.MdView;

public class ExcelSourceBinding extends ExcelSourceBindingBase implements com.runwaysdk.generation.loader.Reloadable
{
  private static final long serialVersionUID = 1190316606;

  public ExcelSourceBinding()
  {
    super();
  }

  @Override
  @Transaction
  public void delete()
  {
    List<ExcelFieldBinding> fields = this.getFields();

    for (ExcelFieldBinding field : fields)
    {
      field.delete();
    }

    super.delete();

    this.getMdView().delete();
  }

  public List<ExcelFieldBinding> getFields()
  {
    List<ExcelFieldBinding> list = new LinkedList<ExcelFieldBinding>();

    ExcelFieldBindingQuery query = new ExcelFieldBindingQuery(new QueryFactory());
    query.WHERE(query.getSourceDefinition().EQ(this));

    OIterator<? extends ExcelFieldBinding> iterator = query.getIterator();

    try
    {
      while (iterator.hasNext())
      {
        list.add(iterator.next());
      }

      return list;
    }
    finally
    {
      iterator.close();
    }
  }

  public static ExcelSourceBinding getBinding(String type)
  {
    ExcelSourceBindingQuery query = new ExcelSourceBindingQuery(new QueryFactory());
    query.WHERE(query.getMdView().EQ(MdView.getMdView(type)));

    OIterator<? extends ExcelSourceBinding> iterator = query.getIterator();

    try
    {
      if (iterator.hasNext())
      {
        ExcelSourceBinding binding = iterator.next();

        return binding;
      }

      return null;
    }
    finally
    {
      iterator.close();
    }
  }
}
