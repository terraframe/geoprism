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
package net.geoprism.shapefile.locatedIn;

import java.util.Collection;
import java.util.LinkedList;

import net.geoprism.data.importer.LocatedInBean;
import net.geoprism.shapefile.LabeledValueBean;
import net.geoprism.shapefile.Localizer;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;




public class OptionContentProvider implements IStructuredContentProvider
{
  private Collection<LabeledValueBean> options;

  public OptionContentProvider()
  {
    this.options = new LinkedList<LabeledValueBean>();
    this.options.add(new LabeledValueBean(null, Localizer.getMessage("CHOOSE_OPTION")));
    this.options.add(new LabeledValueBean(LocatedInBean.BuildTypes.REBUILD_ALL.name(), Localizer.getMessage("DELETE_EXISTING")));
    this.options.add(new LabeledValueBean(LocatedInBean.BuildTypes.ORPHANED_ONLY.name(), Localizer.getMessage("BUILD_ORPHANED")));
  }

  @Override
  public Object[] getElements(Object element)
  {
    return options.toArray(new LabeledValueBean[options.size()]);
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
