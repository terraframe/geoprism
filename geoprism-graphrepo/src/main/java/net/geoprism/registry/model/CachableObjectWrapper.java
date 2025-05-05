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
package net.geoprism.registry.model;

public abstract class CachableObjectWrapper<T>
{
  private boolean dirty;

  private T       object;

  public CachableObjectWrapper()
  {
  }

  public CachableObjectWrapper(T object)
  {
    this();
    this.object = object;

    if (this.object == null)
    {
      throw new UnsupportedOperationException();
    }
  }

  public void markAsDirty()
  {
    this.dirty = true;
  }

  protected void setObject(T object)
  {
    this.object = object;
  }

  public T getObject()
  {
    synchronized (this)
    {
      if (this.dirty)
      {
        this.refresh(object);

        this.dirty = false;
      }

    }

    return object;
  }

  protected abstract void refresh(T object);
}
