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

import com.runwaysdk.dataaccess.MdEdgeDAOIF;
import com.runwaysdk.dataaccess.metadata.graph.MdEdgeDAO;

public enum EdgeConstant {
  HAS_VALUE("net.geoprism.registry.graph.HasValue"), HAS_GEOMETRY("net.geoprism.registry.graph.HasGeometry");

  private String      edgeType;

  private MdEdgeDAOIF mdEdge;

  private EdgeConstant(String edgeType)
  {
    this.edgeType = edgeType;
  }

  public MdEdgeDAOIF getMdEdge()
  {
    synchronized (this)
    {
      if (this.mdEdge == null)
      {
        this.mdEdge = MdEdgeDAO.getMdEdgeDAO(edgeType);
      }
    }

    return mdEdge;
  }

  public String getDBClassName()
  {
    return this.getMdEdge().getDBClassName();
  }

}
