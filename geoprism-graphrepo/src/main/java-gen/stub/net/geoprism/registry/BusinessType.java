/**
 * Copyright (c) 2023 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Geoprism(tm).
 *
 * Geoprism(tm) is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * Geoprism(tm) is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Geoprism(tm). If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism.registry;

import java.util.Map;
import java.util.stream.Collectors;

import org.commongeoregistry.adapter.dataaccess.LocalizedValue;
import org.commongeoregistry.adapter.metadata.AttributeType;

import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;

import net.geoprism.registry.conversion.RegistryAttributeTypeConverter;
import net.geoprism.registry.conversion.RegistryLocalizedValueConverter;
import net.geoprism.registry.model.ServerElement;
import net.geoprism.registry.model.graph.EdgeVertexType;

public class BusinessType extends BusinessTypeBase implements ServerElement, EdgeVertexType
{
  private static final long  serialVersionUID = 88826735;

  public static final String JSON_ATTRIBUTES  = "attributes";

  public static final String JSON_CODE        = "code";

  public BusinessType()
  {
    super();
  }

  @Override
  public void apply()
  {
    super.apply();
  }

  @Override
  public void delete()
  {
    super.delete();
  }

  public MdVertexDAOIF getMdVertexDAO()
  {
    return MdVertexDAO.get(this.getMdVertexOid());
  }

  public LocalizedValue getLabel()
  {
    return RegistryLocalizedValueConverter.convertNoAutoCoalesce(this.getDisplayLabel());
  }

  public Map<String, AttributeType> getAttributeMap()
  {
    RegistryAttributeTypeConverter converter = new RegistryAttributeTypeConverter();

    MdVertexDAOIF mdVertex = this.getMdVertexDAO();

    return mdVertex.definesAttributes().stream().filter(attr -> {
      return !attr.isSystem() && !attr.definesAttribute().equals(BusinessType.SEQ);
    }).map(attr -> converter.build(attr)).collect(Collectors.toMap(AttributeType::getName, attr -> attr));
  }

  public AttributeType getAttribute(String name)
  {
    RegistryAttributeTypeConverter converter = new RegistryAttributeTypeConverter();

    MdVertexDAOIF mdVertex = this.getMdVertexDAO();
    MdAttributeConcreteDAOIF mdAttribute = (MdAttributeConcreteDAOIF) mdVertex.definesAttribute(name);

    return converter.build(mdAttribute);
  }

  public void setLabelAttribute(String name)
  {
    MdVertexDAOIF mdVertex = this.getMdVertexDAO();
    MdAttributeConcreteDAOIF mdAttribute = (MdAttributeConcreteDAOIF) mdVertex.definesAttribute(name);

    this.setLabelAttributeId(mdAttribute.getOid());
  }

  @Override
  public boolean isGeoObjectType()
  {
    return false;
  }

  @Override
  public MdVertexDAOIF toGeoObjectType()
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean isBusinessType()
  {
    return true;
  }

  @Override
  public BusinessType toBusinessType()
  {
    return this;
  }
}
