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
import com.runwaysdk.system.metadata.MdBusiness;
import com.runwaysdk.system.metadata.MdView;

public class TargetBinding extends TargetBindingBase implements com.runwaysdk.generation.loader.Reloadable
{
  private static final long serialVersionUID = 2043607150;

  public TargetBinding()
  {
    super();
  }

  @Override
  @Transaction
  public void delete()
  {
    List<TargetFieldBinding> fields = this.getFields();

    for (TargetFieldBinding field : fields)
    {
      field.delete();
    }

    super.delete();

    MdView mdView = this.getSourceView();

    ExcelSourceBinding source = ExcelSourceBinding.getBinding(mdView.definesType());
    source.delete();
  }

  public List<TargetFieldBinding> getFields()
  {
    List<TargetFieldBinding> list = new LinkedList<TargetFieldBinding>();

    TargetFieldBindingQuery query = new TargetFieldBindingQuery(new QueryFactory());
    query.WHERE(query.getTarget().EQ(this));

    OIterator<? extends TargetFieldBinding> iterator = query.getIterator();

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

  public static TargetBinding getBinding(String type)
  {
    TargetBindingQuery query = new TargetBindingQuery(new QueryFactory());
    query.WHERE(query.getTargetBusiness().EQ(MdBusiness.getMdBusiness(type)));

    OIterator<? extends TargetBinding> iterator = query.getIterator();

    try
    {
      if (iterator.hasNext())
      {
        TargetBinding binding = iterator.next();

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
