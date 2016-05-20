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

import java.util.List;

import net.geoprism.data.importer.PathOption;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * @author terraframe
 *
 */
public class PathTreeContentProvider implements ITreeContentProvider
{
  private List<PathOption> paths;

  public PathTreeContentProvider(List<PathOption> paths)
  {
    this.paths = paths;
  }

  @Override
  public Object[] getElements(Object element)
  {
    return this.paths.toArray(new PathOption[this.paths.size()]);
  }

  @Override
  public Object[] getChildren(Object object)
  {
    PathOption option = (PathOption) object;

    List<PathOption> parents = option.getParents();
    return parents.toArray(new PathOption[parents.size()]);
  }

  @Override
  public Object getParent(Object object)
  {
    PathOption option = (PathOption) object;

    List<PathOption> children = option.getChildren();

    if (children.size() > 0)
    {
      return children.get(0);
    }

    return null;
  }

  @Override
  public boolean hasChildren(Object object)
  {
    PathOption option = (PathOption) object;
    List<PathOption> parents = option.getParents();

    return ( parents.size() > 0 );
  }

  @Override
  public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
  {
    // Do nothing
  }

  @Override
  public void dispose()
  {
    // Do nothing
  }
}
