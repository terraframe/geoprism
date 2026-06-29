/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Common Geo Registry Adapter(tm).
 *
 * Common Geo Registry Adapter(tm) is free software: you can redistribute it
 * and/or modify it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * Common Geo Registry Adapter(tm) is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser
 * General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Common Geo Registry Adapter(tm). If not, see
 * <http://www.gnu.org/licenses/>.
 */
package org.commongeoregistry.adapter.metadata;

import java.io.Serializable;

import org.commongeoregistry.adapter.constants.DefaultAttribute;
import org.commongeoregistry.adapter.dataaccess.LocalizedValue;
import org.commongeoregistry.adapter.serialization.LocalizedValueDeserializer;
import org.commongeoregistry.adapter.serialization.LocalizedValueSerializer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.gson.JsonObject;

/**
 * Primary abstraction for attribute metadata on {@link GeoObjectType}.
 * 
 * @author nathan
 *
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, // use logical type name
    include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({ //
    @JsonSubTypes.Type(value = AttributeBooleanType.class, name = AttributeBooleanType.TYPE), //
    @JsonSubTypes.Type(value = AttributeClassificationType.class, name = AttributeClassificationType.TYPE), //
    @JsonSubTypes.Type(value = AttributeDataSourceType.class, name = AttributeDataSourceType.TYPE), //
    @JsonSubTypes.Type(value = AttributeGeometryType.class, name = AttributeGeometryType.TYPE), //
    @JsonSubTypes.Type(value = AttributeListType.class, name = AttributeListType.TYPE), //
    @JsonSubTypes.Type(value = AttributeCharacterType.class, name = AttributeCharacterType.TYPE), //
    @JsonSubTypes.Type(value = AttributeDateType.class, name = AttributeDateType.TYPE), //
    @JsonSubTypes.Type(value = AttributeFloatType.class, name = AttributeFloatType.TYPE), //
    @JsonSubTypes.Type(value = AttributeIntegerType.class, name = AttributeIntegerType.TYPE), //
    @JsonSubTypes.Type(value = AttributeLocalType.class, name = AttributeLocalType.TYPE) //
})
public abstract class AttributeType implements Serializable
{
  /**
   * 
   */
  private static final long  serialVersionUID           = -2037233821367602621L;

  public static final String JSON_CODE                  = "code";

  public static final String JSON_LOCALIZED_LABEL       = "label";

  public static final String JSON_LOCALIZED_DESCRIPTION = "description";

  public static final String JSON_TYPE                  = "type";

  public static final String JSON_IS_DEFAULT            = "isDefault";

  public static final String JSON_REQUIRED              = "required";

  public static final String JSON_UNIQUE                = "unique";

  public static final String JSON_IS_CHANGE             = "isChangeOverTime";

  /**
   * Unique code of the attribute
   */
  private String             code;

  /**
   * Label of the attribute
   */
  @JsonSerialize(using = LocalizedValueSerializer.class)
  @JsonDeserialize(using = LocalizedValueDeserializer.class)
  private LocalizedValue     label;

  /**
   * Description of the type
   */
  @JsonSerialize(using = LocalizedValueSerializer.class)
  @JsonDeserialize(using = LocalizedValueDeserializer.class)
  private LocalizedValue     description;

  /**
   * Flag denoting if the attribute represents a default attribute as opposed to
   * a custom attribute
   */
  private boolean            isDefault;

  /**
   * Flag denoting if the attribute value is required
   */
  private boolean            required;

  /**
   * Flag denoting if the attribute value is unique
   */
  private boolean            unique;

  private boolean            isChangeOverTime;

  public AttributeType()
  {
    this.isChangeOverTime = true;
  }

  public AttributeType(String code, LocalizedValue _label, LocalizedValue _description, boolean _isDefault, boolean _required, boolean _unique)
  {
    this(code, _label, _description, _isDefault, _required, _unique, true);
  }

  public AttributeType(String code, LocalizedValue _label, LocalizedValue _description, boolean _isDefault, boolean _required, boolean _unique, boolean isChangeOverTime)
  {
    this.code = code;
    this.label = _label;
    this.description = _description;
    this.isDefault = _isDefault;
    this.required = _required;
    this.unique = _unique;
    this.isChangeOverTime = isChangeOverTime;
  }

  @JsonIgnore
  public abstract String getType();

  public String getCode()
  {
    return code;
  }

  public void setCode(String code)
  {
    this.code = code;
  }

  public LocalizedValue getLabel()
  {
    return label;
  }

  public void setLabel(LocalizedValue label)
  {
    this.label = label;
  }

  public LocalizedValue getDescription()
  {
    return description;
  }

  public void setDescription(LocalizedValue description)
  {
    this.description = description;
  }

  public boolean isDefault()
  {
    return isDefault;
  }

  public void setDefault(boolean isDefault)
  {
    this.isDefault = isDefault;
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
    return isChangeOverTime;
  }

  public void setChangeOverTime(boolean isChangeOverTime)
  {
    this.isChangeOverTime = isChangeOverTime;
  }

  public void validate(Object _value)
  {
    // Stub method used to validate the value according to the metadata of the
    // AttributeType
  }

  public final JsonObject toJSON()
  {
    return this.toJSON(new DefaultSerializer());
  }

  public JsonObject toJSON(CustomSerializer serializer)
  {
    JsonObject json = new JsonObject();

    json.addProperty(JSON_CODE, this.getCode());

    json.addProperty(JSON_TYPE, this.getType());

    json.add(JSON_LOCALIZED_LABEL, this.getLabel().toJSON(serializer));

    json.add(JSON_LOCALIZED_DESCRIPTION, this.getDescription().toJSON(serializer));

    json.addProperty(JSON_IS_DEFAULT, this.isDefault());
    json.addProperty(JSON_REQUIRED, this.isRequired());
    json.addProperty(JSON_UNIQUE, this.isUnique());
    json.addProperty(JSON_IS_CHANGE, this.isChangeOverTime());

    serializer.configure(this, json);

    return json;
  }

  /**
   * Populates any additional attributes from JSON that were not populated in
   * {@link GeoObjectType#fromJSON(String, org.commongeoregistry.adapter.RegistryAdapter)}
   * 
   * @param attrObj
   * @return {@link AttributeType}
   */
  public void fromJSON(JsonObject attrObj)
  {
  }

  public static AttributeType factory(String _name, LocalizedValue _label, LocalizedValue _description, String _type, boolean _required, boolean _unique, boolean _isChange)
  {
    AttributeType attributeType = null;

    DefaultAttribute defaultAttr = DefaultAttribute.getByAttributeName(_name);
    boolean _isDefault = defaultAttr == null ? false : defaultAttr.getIsDefault();

    if (_type.equals(AttributeCharacterType.TYPE))
    {
      attributeType = new AttributeCharacterType(_name, _label, _description, _isDefault, _required, _unique);
    }
    else if (_type.equals(AttributeLocalType.TYPE))
    {
      attributeType = new AttributeLocalType(_name, _label, _description, _isDefault, _required, _unique);
    }
    else if (_type.equals(AttributeDateType.TYPE))
    {
      attributeType = new AttributeDateType(_name, _label, _description, _isDefault, _required, _unique);
    }
    else if (_type.equals(AttributeIntegerType.TYPE))
    {
      attributeType = new AttributeIntegerType(_name, _label, _description, _isDefault, _required, _unique);
    }
    else if (_type.equals(AttributeFloatType.TYPE))
    {
      attributeType = new AttributeFloatType(_name, _label, _description, _isDefault, _required, _unique);
    }
    else if (_type.equals(AttributeClassificationType.TYPE))
    {
      attributeType = new AttributeClassificationType(_name, _label, _description, _isDefault, _required, _unique);
    }
    else if (_type.equals(AttributeBooleanType.TYPE))
    {
      attributeType = new AttributeBooleanType(_name, _label, _description, _isDefault, _required, _unique);
    }
    else if (_type.equals(AttributeDataSourceType.TYPE))
    {
      attributeType = new AttributeDataSourceType(_name, _label, _description, _isDefault, _required, _unique);
    }
    else if (_type.equals(AttributeGeometryType.TYPE))
    {
      attributeType = new AttributeGeometryType(_name, _label, _description, _isDefault, _required, _unique);
    }
    else if (_type.equals(AttributeListType.TYPE))
    {
      attributeType = new AttributeListType(_name, _label, _description, _isDefault, _required, _unique);
    }

    attributeType.setChangeOverTime(_isChange);

    return attributeType;
  }

  public static AttributeType parse(JsonObject joAttr)
  {
    String name = joAttr.get(AttributeType.JSON_CODE).getAsString();
    boolean required = joAttr.get(AttributeType.JSON_REQUIRED).getAsBoolean();
    boolean unique = joAttr.get(AttributeType.JSON_UNIQUE).getAsBoolean();
    boolean isChange = joAttr.has(AttributeType.JSON_IS_CHANGE) ? joAttr.get(AttributeType.JSON_IS_CHANGE).getAsBoolean() : true;

    LocalizedValue attributeLabel = LocalizedValue.fromJSON(joAttr.get(AttributeType.JSON_LOCALIZED_LABEL).getAsJsonObject());
    LocalizedValue attributeDescription = LocalizedValue.fromJSON(joAttr.get(AttributeType.JSON_LOCALIZED_DESCRIPTION).getAsJsonObject());

    AttributeType attrType = AttributeType.factory(name, attributeLabel, attributeDescription, joAttr.get(AttributeType.JSON_TYPE).getAsString(), required, unique, isChange);
    attrType.fromJSON(joAttr);

    return attrType;
  }

}
