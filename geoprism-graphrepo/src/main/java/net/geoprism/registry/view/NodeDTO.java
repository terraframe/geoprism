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
package net.geoprism.registry.view;

public class NodeDTO<T>
{
  private T                object;

  private Page<NodeDTO<T>> children;

  public NodeDTO()
  {
  }

  public NodeDTO(T object)
  {
    this.object = object;
  }

  public T getObject()
  {
    return object;
  }

  public void setObject(T object)
  {
    this.object = object;
  }

  public Page<NodeDTO<T>> getChildren()
  {
    return children;
  }

  public void setChildren(Page<NodeDTO<T>> children)
  {
    this.children = children;
  }

  @SuppressWarnings("rawtypes")
  @Override
  public boolean equals(Object obj)
  {
    if (obj instanceof NodeDTO)
    {
      return ( (NodeDTO) obj ).getObject().equals(this.getObject());
    }

    return super.equals(obj);
  }

  @Override
  public int hashCode()
  {
    return this.getObject().hashCode();
  }
}
