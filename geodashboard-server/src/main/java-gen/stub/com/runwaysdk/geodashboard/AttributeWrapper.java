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
package com.runwaysdk.geodashboard;

import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayer;
import com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayerQuery;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;

public class AttributeWrapper extends AttributeWrapperBase implements com.runwaysdk.generation.loader.Reloadable
{
  private static final long serialVersionUID = -1313778104;
  
  public AttributeWrapper()
  {
    super();
  }
  
  @Transaction
  public void delete(Dashboard dashboard)
  {
    // Delete any existing layers    
    DashboardThematicLayerQuery query = new DashboardThematicLayerQuery(new QueryFactory());
    query.WHERE(query.getDashboardMap().getDashboard().EQ(dashboard));
    query.AND(query.getMdAttribute().EQ(this.getWrappedMdAttribute()));
    
    OIterator<? extends DashboardThematicLayer> it = query.getIterator();
    
    try
    {
      while(it.hasNext())
      {
        DashboardThematicLayer layer = it.next();
        layer.delete();
      }
    }
    finally
    {
      it.close();
    }
        
    this.delete();
  }

  public AttributeWrapper clone(MetadataWrapper metadata, Integer order)
  {
    AttributeWrapper clone = new AttributeWrapper();
    clone.setWrappedMdAttribute(this.getWrappedMdAttribute());
    clone.apply();
    
    DashboardAttributes attribute = metadata.addAttributeWrapper(clone);
    attribute.setListOrder(order);
    attribute.apply();
    
    return clone;
  }  
}
