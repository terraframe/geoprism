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
package com.runwaysdk.geodashboard.gis.shapefile;

import java.util.Collection;
import java.util.LinkedList;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.geodashboard.gis.LabeledValueBean;
import com.runwaysdk.geodashboard.gis.Localizer;
import com.runwaysdk.system.gis.geo.Universal;

public class UniversalContentProvider implements IStructuredContentProvider, Reloadable
{
  private Collection<LabeledValueBean> universals;

  public UniversalContentProvider(Universal[] universals)
  {
    this.universals = new LinkedList<LabeledValueBean>();
    this.universals.add(new LabeledValueBean(null, Localizer.getMessage("CHOOSE_OPTION")));

    for (Universal universal : universals)
    {
      this.universals.add(new LabeledValueBean(universal.getId(), universal.getDisplayLabel().getValue()));
    }
  }

  @Override
  public Object[] getElements(Object arg0)
  {
    return universals.toArray(new LabeledValueBean[universals.size()]);
  }

  @Override
  public void dispose()
  {
    // Do nothing
  }

  @Override
  public void inputChanged(Viewer arg0, Object arg1, Object arg2)
  {
    // Do nothing
  }

}
