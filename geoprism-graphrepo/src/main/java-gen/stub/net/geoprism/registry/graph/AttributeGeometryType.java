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

import org.commongeoregistry.adapter.metadata.AttributeType;

import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.system.gis.geo.GeometryType;

import net.geoprism.registry.model.GeometryValueNodeStrategy;
import net.geoprism.registry.model.ValueStrategy;
import net.geoprism.registry.model.VertexValueStrategy;

public class AttributeGeometryType extends AttributeGeometryTypeBase
{
  @SuppressWarnings("unused")
  private static final long serialVersionUID = 1252972775;

  public AttributeGeometryType()
  {
    super();
  }

  @Override
  @Transaction
  public void apply()
  {
    if (!this.getIsChangeOverTime())
    {
      throw new UnsupportedOperationException();
    }

    super.apply();
  }

  @Override
  public AttributeType toDTO()
  {
    org.commongeoregistry.adapter.metadata.AttributeGeometryType dto = new org.commongeoregistry.adapter.metadata.AttributeGeometryType(this.getCode(), getLocalizedLabel(), getLocalizedDescription(), getIsDefault(), isNew(), getUnique());

    this.populate(dto);

    return dto;
  }

  @Override
  public ValueStrategy getStrategy()
  {
    if (!this.getIsChangeOverTime())
    {
      return new VertexValueStrategy(this);
    }
    else
    {
      return new GeometryValueNodeStrategy(this, getGeometryMdVertex(), AttributeGeometryValue.VALUE);
    }
  }

  protected MdVertexDAOIF getGeometryMdVertex()
  {
    if (this.getGeometryType().equals(GeometryType.POINT.name()) || this.getGeometryType().equals(GeometryType.MULTIPOINT.name()))
    {
      return MdVertexDAO.getMdVertexDAO(AttributeMultiPointValue.CLASS);
    }
    else if (this.getGeometryType().equals(GeometryType.LINE.name()) || this.getGeometryType().equals(GeometryType.MULTILINE.name()))
    {
      return MdVertexDAO.getMdVertexDAO(AttributeMultiLineValue.CLASS);
    }
    else if (this.getGeometryType().equals(GeometryType.POLYGON.name()) || this.getGeometryType().equals(GeometryType.MULTIPOLYGON.name()))
    {
      return MdVertexDAO.getMdVertexDAO(AttributeMultiPolygonValue.CLASS);
    }
    else if (this.getGeometryType().equals(GeometryType.SHAPE.name()))
    {
      return MdVertexDAO.getMdVertexDAO(AttributeShapeValue.CLASS);
    }

    throw new UnsupportedOperationException("Unsupported geometry type [" + this.getGeometryType() + "]");
  }

}
