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

import java.util.LinkedList;
import java.util.List;

import net.geoprism.data.importer.PathOption;

import org.eclipse.jface.viewers.ICheckStateProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

public class PathOptionProvider implements ILabelProvider, ICheckStateProvider
{
  private List<ILabelProviderListener> listeners;

  public PathOptionProvider()
  {
    this.listeners = new LinkedList<ILabelProviderListener>();
  }

  @Override
  public void addListener(ILabelProviderListener listener)
  {
    this.listeners.add(listener);
  }

  @Override
  public void removeListener(ILabelProviderListener listener)
  {
    this.listeners.remove(listener);
  }

  @Override
  public void dispose()
  {
    // Do nothing
  }

  @Override
  public boolean isLabelProperty(Object arg0, String arg1)
  {
    return false;
  }

  @Override
  public Image getImage(Object arg0)
  {
    return null;
  }

  @Override
  public String getText(Object object)
  {
    PathOption option = (PathOption) object;

    return option.getParentLabel();
  }

  @Override
  public boolean isChecked(Object object)
  {
    PathOption option = (PathOption) object;

    return option.isEnabled();
  }

  @Override
  public boolean isGrayed(Object object)
  {
//    /*
//     * Only nodes with checked children can be check
//     */
//    PathOption option = (PathOption) object;
//    List<PathOption> children = option.getChildren();
//
//    if (children.size() > 0)
//    {
//      boolean enabled = false;
//
//      for (PathOption child : children)
//      {
//        enabled = enabled || child.isEnabled();
//      }
//
//      return enabled;
//    }

    return false;
  }

}
