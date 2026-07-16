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

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.commongeoregistry.adapter.constants.DefaultAttribute;
import org.commongeoregistry.adapter.dataaccess.LocalizedValue;
import org.locationtech.jts.geom.Geometry;

import com.runwaysdk.business.graph.VertexObject;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.graph.attributes.ValueOverTime;

import net.geoprism.registry.model.graph.ObjectClassIF;
import net.geoprism.registry.model.graph.ServerObjectVertex;
import net.geoprism.registry.model.graph.VertexServerGeoObject;

public class GeometryStateValue extends PrimitiveStateValue
{
  private ObjectClassIF type;

  private Geometry      geometry;

  public GeometryStateValue(ObjectClassIF type, VertexObject node, String nodeAttribute)
  {
    super(node, nodeAttribute);

    this.type = type;

    if (! ( type instanceof ServerGeoObjectType ))
    {
      throw new UnsupportedOperationException();
    }
  }

  @Override
  public boolean hasValue()
  {
    return ( super.hasValue() || geometry != null );
  }

  @Override
  public void setValue(Object value)
  {
    if (value instanceof Geometry)
    {
      this.geometry = (Geometry) value;
    }
    else
    {
      super.setValue(value);
    }
  }

  public Geometry getGeometryValue()
  {
    if (geometry == null)
    {
      this.geometry = getGeometryValue((String) this.getValue());
    }

    return geometry;
  }

  public BusinessDAOIF getGeometryInstance()
  {
    return getGeometryInstance((String) this.getValue());
  }

  @Override
  public void apply(ServerObjectVertex v)
  {
    if (this.hasGeometryObject() && this.geometry == null)
    {
      return;
    }

    VertexServerGeoObject object = (VertexServerGeoObject) v;

    MdBusinessDAOIF geometryTable = ( (ServerGeoObjectType) this.type ).getGeometryTable();
    LocalizedValue localizedValue = object.getDisplayLabel(getStartDate());
    String label = localizedValue != null ? localizedValue.getValue() : object.getCode();

    // Create or update the entry in the geometry table
    BusinessDAO geometryInstance = !this.hasGeometryObject() ? //
        BusinessDAO.newInstance(geometryTable.definesType()) : //
        getGeometryInstance().getBusinessDAO();

    geometryInstance.setValue(DefaultAttribute.UID.getName(), object.getUid());
    geometryInstance.setValue(DefaultAttribute.CODE.getName(), object.getCode());
    geometryInstance.setValue(DefaultAttribute.DISPLAY_LABEL.getName(), label);
    geometryInstance.setValue(EdgeType.START_DATE, this.getStartDate());
    geometryInstance.setValue(EdgeType.END_DATE, this.getEndDate());
    geometryInstance.setValue(DefaultAttribute.GEOMETRY.getName(), this.geometry);
    geometryInstance.apply();

    // Set the value of this node
    this.setValue(geometryInstance.getOid());

    super.apply(object);
  }

  @Override
  public void delete()
  {
    // Ensure the geometry row is deleted
    if (hasGeometryObject())
    {
      getGeometryInstance().getBusinessDAO().delete();
    }

    super.delete();
  }

  public boolean hasGeometryObject()
  {
    return !StringUtils.isBlank((String) this.getValue());
  }

  public ValueOverTime toValueOverTime()
  {
    Date startDate = this.getStartDate();
    Date endDate = this.getEndDate();

    return new ValueOverTime(this.getOid(), startDate, endDate, this.getGeometryValue());
  }

  public static BusinessDAOIF getGeometryInstance(String id)
  {
    return BusinessDAO.get(id);
  }

  public static Geometry getGeometryValue(String oid)
  {
    if (!StringUtils.isBlank(oid))
    {
      return (Geometry) getGeometryInstance(oid).getObjectValue(DefaultAttribute.GEOMETRY.getName());
    }

    return null;
  }

}
