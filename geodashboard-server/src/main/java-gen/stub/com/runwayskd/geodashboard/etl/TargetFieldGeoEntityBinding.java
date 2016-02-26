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

public class TargetFieldGeoEntityBinding extends TargetFieldGeoEntityBindingBase implements com.runwaysdk.generation.loader.Reloadable
{
  private static final long serialVersionUID = -2005836550;

  public TargetFieldGeoEntityBinding()
  {
    super();
  }

  @Override
  @Transaction
  public void delete()
  {
    List<UniversalAttributeBinding> attributes = this.getUniversalAttributes();

    for (UniversalAttributeBinding attribute : attributes)
    {
      attribute.delete();
    }

    super.delete();
  }

  public List<UniversalAttributeBinding> getUniversalAttributes()
  {
    List<UniversalAttributeBinding> list = new LinkedList<UniversalAttributeBinding>();

    UniversalAttributeBindingQuery query = new UniversalAttributeBindingQuery(new QueryFactory());
    query.WHERE(query.getField().EQ(this));

    OIterator<? extends UniversalAttributeBinding> iterator = query.getIterator();

    try
    {
      while (iterator.hasNext())
      {
        UniversalAttributeBinding binding = iterator.next();

        list.add(binding);
      }

      return list;
    }
    finally
    {
      iterator.close();
    }
  }
}
