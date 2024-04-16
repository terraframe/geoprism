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
package net.geoprism.graph;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.commongeoregistry.adapter.constants.GeometryType;
import org.commongeoregistry.adapter.dataaccess.LocalizedValue;
import org.commongeoregistry.adapter.metadata.AttributeType;
import org.commongeoregistry.adapter.metadata.GeoObjectType;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;
import com.runwaysdk.gis.dataaccess.MdAttributeGeometryDAOIF;

import net.geoprism.registry.conversion.AttributeTypeConverter;
import net.geoprism.registry.conversion.LocalizedValueConverter;

public class GeoObjectTypeSnapshot extends GeoObjectTypeSnapshotBase
{
  public static final String TABLE_PACKAGE    = "net.geoprism.lpg";

  public static final String PREFIX           = "g_";

  public static final String SPLIT            = "__";

  public static final String ROOT             = "__ROOT__";

  @SuppressWarnings("unused")
  private static final long  serialVersionUID = -1232639915;

  public GeoObjectTypeSnapshot()
  {
    super();
  }

  @Override
  public String toString()
  {
    return this.getCode();
  }

  public boolean isRoot()
  {
    return StringUtils.isEmpty(this.getGeometryType());
  }

  public List<org.commongeoregistry.adapter.metadata.AttributeType> getAttributeTypes()
  {
    AttributeTypeConverter converter = new AttributeTypeConverter();

    List<AttributeType> attributes = new LinkedList<>();

    MdVertexDAOIF mdVertex = MdVertexDAO.get(this.getGraphMdVertexOid());
    List<? extends MdAttributeConcreteDAOIF> mdAttributes = mdVertex.definesAttributes();
    mdAttributes.forEach(attribute -> {
      if (! ( attribute instanceof MdAttributeGeometryDAOIF ))
      {
        attributes.add(converter.build(attribute));
      }
    });
    return attributes;
  }

  public JsonObject toJSON()
  {
    JsonArray attributes = new JsonArray();

    this.getAttributeTypes().forEach(attribute -> attributes.add(attribute.toJSON()));

    JsonObject typeObject = new JsonObject();
    typeObject.addProperty(CODE, this.getCode());
    typeObject.addProperty(ORGCODE, this.getOrgCode());
    typeObject.add(DISPLAYLABEL, LocalizedValueConverter.convertNoAutoCoalesce(this.getDisplayLabel()).toJSON());
    typeObject.add(DESCRIPTION, LocalizedValueConverter.convertNoAutoCoalesce(this.getDescription()).toJSON());
    typeObject.addProperty(GEOMETRYTYPE, this.getGeometryType());
    typeObject.addProperty(ISABSTRACT, this.getIsAbstract());
    typeObject.addProperty(ISROOT, this.getIsRoot());
    typeObject.addProperty(ISPRIVATE, this.getIsPrivate());
    typeObject.add("attributes", attributes);

    GeoObjectTypeSnapshot parent = this.getParent();

    if (parent != null)
    {
      typeObject.addProperty(PARENT, parent.getCode());
    }

    return typeObject;
  }

  public GeoObjectType toGeoObjectType()
  {
    String code = this.getCode();
    GeometryType geometryType = GeometryType.valueOf(this.getGeometryType());
    LocalizedValue label = LocalizedValueConverter.convertNoAutoCoalesce(this.getDisplayLabel());
    LocalizedValue description = LocalizedValueConverter.convertNoAutoCoalesce(this.getDescription());

    GeoObjectType type = new GeoObjectType(code, geometryType, label, description, this.getIsGeometryEditable(), null, null);
    type.setIsAbstract(this.getIsAbstract());
    type.setIsPrivate(this.getIsPrivate());

    List<AttributeType> attributes = this.getAttributeTypes();

    for (AttributeType attribute : attributes)
    {
      type.addAttribute(attribute);
    }

    return type;
  }

}
