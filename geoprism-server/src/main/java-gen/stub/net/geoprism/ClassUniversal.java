/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
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
package net.geoprism;


public class ClassUniversal extends ClassUniversalBase 
{
  private static final long serialVersionUID = 538122561;
  
  public ClassUniversal(String parentOid, String childOid)
  {
    super(parentOid, childOid);
  }
  
  public ClassUniversal(net.geoprism.MappableClass parent, com.runwaysdk.system.gis.geo.Universal child)
  {
    this(parent.getOid(), child.getOid());
  }
  
}
