/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Common Geo Registry Adapter(tm).
 *
 * Common Geo Registry Adapter(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Common Geo Registry Adapter(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Common Geo Registry Adapter(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package org.commongeoregistry.adapter.constants;

import org.commongeoregistry.adapter.dataaccess.AlternateId;
import org.commongeoregistry.adapter.dataaccess.LocalizedValue;
import org.commongeoregistry.adapter.metadata.AttributeBooleanType;
import org.commongeoregistry.adapter.metadata.AttributeCharacterType;
import org.commongeoregistry.adapter.metadata.AttributeClassificationType;
import org.commongeoregistry.adapter.metadata.AttributeDateType;
import org.commongeoregistry.adapter.metadata.AttributeGeometryType;
import org.commongeoregistry.adapter.metadata.AttributeIntegerType;
import org.commongeoregistry.adapter.metadata.AttributeListType;
import org.commongeoregistry.adapter.metadata.AttributeLocalType;
import org.commongeoregistry.adapter.metadata.AttributeDataSourceType;
import org.commongeoregistry.adapter.metadata.AttributeType;

public enum DefaultAttribute {
  UID("uid", "UID", "Internal, globally unique identifier", AttributeCharacterType.TYPE, true, true, false, false), 
  
  CODE("code", "Code", "Human-readable unique identifier", AttributeCharacterType.TYPE, true, true, false, false),

  INVALID("invalid", "Invalid", "Whether this Geo-Object is no longer valid", AttributeBooleanType.TYPE, true, true, false, false),

  DISPLAY_LABEL("displayLabel", "Display Label", "Display name of the Geo-Object", AttributeLocalType.TYPE, true, false, false, true),

  ALT_IDS("altIds", "Alternate IDs", "A list of alternate IDs for this Geo-Object", AttributeListType.TYPE, true, false, false, false),

  TYPE("type", "Type", "The type of the Geo-Object", AttributeCharacterType.TYPE, true, false, false, false),

  EXISTS("exists", "Exists", "Whether this Geo-Object exists during a given time period. For example, a district created by splitting another district would not exist before the date of the split", AttributeBooleanType.TYPE, true, false, false, true),

  SEQUENCE("sequence", "Sequence", "Sequence number, incremented each time the Geo-Object is updated", AttributeIntegerType.TYPE, true, false, false, false),

  CREATE_DATE("createDate", "Date Created", "The date the Geo-Object was created", AttributeDateType.TYPE, true, false, false, false),

  LAST_UPDATE_DATE("lastUpdateDate", "Date Last Updated", "The date the Geo-Object was last updated", AttributeDateType.TYPE, true, false, false, false),

  DATA_SOURCE("dataSource", "Data Source", "The source of the Geo-Object's data", AttributeDataSourceType.TYPE, true, false, false, true),

  GEOMETRY("geometry", "Geometry", "The geometry of the Geo-Object", AttributeGeometryType.TYPE, true, false, false, true),

  CLASSIFICATION("classification", "Classification", "The subtype classification of the Geo-Object", AttributeClassificationType.TYPE, false, false, false, true);


  public static final String ALTERNATE_ID_ELEMENT_TYPE = AlternateId.class.getName();

  private String             name;

  private String             defaultLabel;

  private String             defaultDescription;

  private String             type;

  private boolean            isDefault;

  private boolean            required;

  private boolean            unique;

  private boolean            isChange;

  private DefaultAttribute(String name, String defaultLabel, String defaultDescription, String type, boolean isDefault, boolean required, boolean unique, boolean isChange)
  {
    this.name = name;
    this.defaultLabel = defaultLabel;
    this.defaultDescription = defaultDescription;
    this.type = type;
    this.isDefault = isDefault;
    this.required = required;
    this.unique = unique;
    this.isChange = isChange;
  }

  public String getName()
  {
    return this.name;
  }

  public String getDefaultLocalizedName()
  {
    return this.defaultLabel;
  }

  public String getDefaultDescription()
  {
    return this.defaultDescription;
  }

  public String getType()
  {
    return this.type;
  }

  public boolean getIsDefault()
  {
    return this.isDefault;
  }

  public boolean isRequired()
  {
    return required;
  }

  public void setRequired(boolean required)
  {
    this.required = required;
  }

  public boolean isUnique()
  {
    return unique;
  }

  public void setUnique(boolean unique)
  {
    this.unique = unique;
  }

  public boolean isChangeOverTime()
  {
    return isChange;
  }

  public void setChangeOverTime(boolean isChange)
  {
    this.isChange = isChange;
  }

  public static DefaultAttribute getByAttributeName(String attributeName)
  {
    DefaultAttribute[] all = DefaultAttribute.values();

    for (int i = 0; i < all.length; ++i)
    {
      if (all[i].getName().equals(attributeName))
      {
        return all[i];
      }
    }

    return null;
  }

  public AttributeType createAttributeType()
  {
    LocalizedValue label = new LocalizedValue(this.getDefaultLocalizedName());
    LocalizedValue description = new LocalizedValue(this.getDefaultDescription());

    AttributeType at = AttributeType.factory(this.getName(), label, description, this.getType(), this.isRequired(), this.isUnique(), this.isChangeOverTime());

    if (this == ALT_IDS)
    {
      ( (AttributeListType) at ).setElementType(ALTERNATE_ID_ELEMENT_TYPE);
    }

    return at;
  }
}
