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
package net.geoprism.shapefile;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;


public class LabelProvider implements ILabelProvider
{

  @Override
  public Image getImage(Object arg0)
  {
    return null;
  }

  @Override
  public String getText(Object object)
  {
    if (object instanceof LabeledValueBean)
    {
      return ( (LabeledValueBean) object ).getLabel();
    }

    return null;
  }

  @Override
  public void addListener(ILabelProviderListener arg0)
  {
  }

  @Override
  public void dispose()
  {
  }

  @Override
  public boolean isLabelProperty(Object arg0, String arg1)
  {
    return false;
  }

  @Override
  public void removeListener(ILabelProviderListener arg0)
  {
  }

}
