/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
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
package net.geoprism.dashboard.layer;


public class HasThematicLayer extends HasThematicLayerBase 
{
  private static final long serialVersionUID = 1449327966;
  
  public HasThematicLayer(String parentOid, String childOid)
  {
    super(parentOid, childOid);
  }
  
  public HasThematicLayer(net.geoprism.dashboard.DashboardMap parent, net.geoprism.dashboard.layer.DashboardThematicLayer child)
  {
    this(parent.getOid(), child.getOid());
  }
  
}
