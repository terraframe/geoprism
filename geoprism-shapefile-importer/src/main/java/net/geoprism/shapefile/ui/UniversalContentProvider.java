/**
 * Copyright (c) 2023 TerraFrame, Inc. All rights reserved.
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
package net.geoprism.shapefile.ui;

import java.util.Collection;
import java.util.LinkedList;

import net.geoprism.shapefile.LabeledValueBean;
import net.geoprism.shapefile.Localizer;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;


import com.runwaysdk.system.gis.geo.Universal;

public class UniversalContentProvider implements IStructuredContentProvider
{
  private Collection<LabeledValueBean> universals;

  public UniversalContentProvider(Universal[] universals)
  {
    this.universals = new LinkedList<LabeledValueBean>();
    this.universals.add(new LabeledValueBean(null, Localizer.getMessage("CHOOSE_OPTION")));

    for (Universal universal : universals)
    {
      this.universals.add(new LabeledValueBean(universal.getOid(), universal.getDisplayLabel().getValue()));
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
