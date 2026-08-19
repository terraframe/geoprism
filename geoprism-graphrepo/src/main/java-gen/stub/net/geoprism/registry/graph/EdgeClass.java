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
package net.geoprism.registry.graph;

import org.commongeoregistry.adapter.dataaccess.LocalizedValue;

import com.runwaysdk.dataaccess.MdEdgeDAOIF;
import com.runwaysdk.system.metadata.MdEdge;

import net.geoprism.registry.conversion.LocalizedValueConverter;

public abstract class EdgeClass extends EdgeClassBase
{
  @SuppressWarnings("unused")
  private static final long serialVersionUID = 497212104;

  public EdgeClass()
  {
    super();
  }

  public abstract MdEdge getMdEdge();

  public abstract String getMdEdgeOid();

  public abstract MdEdgeDAOIF getMdEdgeDAO();

  public abstract String getOrigin();

  public abstract Long getSequence();

  public abstract void setSequence(Long sequence);

  public LocalizedValue getLabel()
  {
    return LocalizedValueConverter.convert(this.getEmbeddedComponent(BusinessEdgeType.DISPLAYLABEL));
  }

  public LocalizedValue getDescriptionLV()
  {
    return LocalizedValueConverter.convert(this.getEmbeddedComponent(BusinessEdgeType.DESCRIPTION));
  }

}
