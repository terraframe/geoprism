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

import java.util.List;
import java.util.stream.Collectors;

import org.commongeoregistry.adapter.dataaccess.LocalizedValue;

import com.runwaysdk.constants.IndexTypes;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeConcreteInfo;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.metadata.MdAttributeConcreteDAO;
import com.runwaysdk.system.metadata.MdVertex;

import net.geoprism.registry.conversion.LocalizedValueConverter;
import net.geoprism.registry.model.ValueStrategy;

public abstract class AttributeType extends AttributeTypeBase
{
  @SuppressWarnings("unused")
  private static final long serialVersionUID = -1935054848;

  public AttributeType()
  {
    super();
  }

  public abstract org.commongeoregistry.adapter.metadata.AttributeType toDTO();

  public abstract ValueStrategy getStrategy();

  @Override
  public GeoObjectType getGeoObjectType()
  {
    String oid = this.getObjectValue(GEOOBJECTTYPE);

    return GeoObjectType.get(oid);
  }

  public LocalizedValue getLocalizedLabel()
  {
    return LocalizedValueConverter.convert(this.getEmbeddedComponent(LABEL));
  }

  public LocalizedValue getLocalizedDescription()
  {
    return LocalizedValueConverter.convert(this.getEmbeddedComponent(DESCRIPTION));
  }

  protected void populate(org.commongeoregistry.adapter.metadata.AttributeType dto)
  {
    dto.setUnique(this.getUnique());
    dto.setRequired(this.getRequired());
    dto.setIsChangeOverTime(this.getIsChangeOverTime());
  }

  protected void populate(MdAttributeConcreteDAO mdAttribute)
  {
    GeoObjectType type = this.getGeoObjectType();
    MdVertex mdVertex = type.getMdVertex();

    mdAttribute.setValue(MdAttributeConcreteInfo.NAME, this.getCode());
    LocalizedValueConverter.populate(mdAttribute, MdAttributeBooleanInfo.DISPLAY_LABEL, this.getLocalizedLabel());
    LocalizedValueConverter.populate(mdAttribute, MdAttributeBooleanInfo.DESCRIPTION, this.getLocalizedDescription());
    mdAttribute.setValue(MdAttributeConcreteInfo.DEFINING_MD_CLASS, mdVertex.getOid());
    mdAttribute.setValue(MdAttributeConcreteInfo.REQUIRED, this.getRequired());

    if (this.getUnique())
    {
      mdAttribute.addItem(MdAttributeConcreteInfo.INDEX_TYPE, IndexTypes.UNIQUE_INDEX.getOid());
    }
  }

  public void fromDTO(org.commongeoregistry.adapter.metadata.AttributeType dto)
  {
    this.setCode(dto.getName());
    this.setRequired(dto.isRequired());
    this.setUnique(dto.isUnique());
    this.setIsChangeOverTime(dto.isChangeOverTime());

    LocalizedValueConverter.populate(this, AttributeType.LABEL, dto.getLabel());
    LocalizedValueConverter.populate(this, AttributeType.DESCRIPTION, dto.getDescription());
  }

  public List<MdAttributeDAOIF> getValueAttributes()
  {
    return this.getStrategy().getValueAttributes();
  }

  public List<String> getColumnNames()
  {
    return this.getValueAttributes().stream().map(mdAttribute -> mdAttribute.getColumnName()).collect(Collectors.toList());
  }

}
