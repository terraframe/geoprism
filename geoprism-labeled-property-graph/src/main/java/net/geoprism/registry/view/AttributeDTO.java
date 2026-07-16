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
package net.geoprism.registry.view;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.commongeoregistry.adapter.metadata.AttributeBooleanType;
import org.commongeoregistry.adapter.metadata.AttributeCharacterType;
import org.commongeoregistry.adapter.metadata.AttributeClassificationType;
import org.commongeoregistry.adapter.metadata.AttributeDataSourceType;
import org.commongeoregistry.adapter.metadata.AttributeDateType;
import org.commongeoregistry.adapter.metadata.AttributeFloatType;
import org.commongeoregistry.adapter.metadata.AttributeIntegerType;
import org.commongeoregistry.adapter.metadata.AttributeLocalType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, // use logical type name
    include = JsonTypeInfo.As.PROPERTY, property = "type")

@JsonSubTypes({ //
    @JsonSubTypes.Type(value = AttributeBooleanDTO.class, name = AttributeBooleanType.TYPE), //
    @JsonSubTypes.Type(value = AttributeCharacterDTO.class, name = AttributeCharacterType.TYPE), //
    @JsonSubTypes.Type(value = AttributeClassificationDTO.class, name = AttributeClassificationType.TYPE), //
    @JsonSubTypes.Type(value = AttributeDataSourceDTO.class, name = AttributeDataSourceType.TYPE), //
    @JsonSubTypes.Type(value = AttributeDateDTO.class, name = AttributeDateType.TYPE), //
    @JsonSubTypes.Type(value = AttributeFloatDTO.class, name = AttributeFloatType.TYPE), //
    @JsonSubTypes.Type(value = AttributeIntegerDTO.class, name = AttributeIntegerType.TYPE), //
    @JsonSubTypes.Type(value = AttributeLocalDTO.class, name = AttributeLocalType.TYPE), //
})
@JsonIgnoreProperties({ "type" })
public abstract class AttributeDTO<T>
{
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  private List<ValueOverTimeEntryDTO<T>> values;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private T                              value;

  private Boolean                        changeOverTime;

  public AttributeDTO()
  {
    this.values = new LinkedList<>();
    this.changeOverTime = false;
  }

  @JsonProperty("type")
  public abstract String getType();

  public Boolean getChangeOverTime()
  {
    return changeOverTime;
  }

  public void setChangeOverTime(Boolean changeOverTime)
  {
    this.changeOverTime = changeOverTime;
  }

  public List<ValueOverTimeEntryDTO<T>> getValues()
  {
    return values;
  }

  public void setValues(List<ValueOverTimeEntryDTO<T>> values)
  {
    this.values = values;
  }

  @JsonIgnore
  public void addValue(ValueOverTimeEntryDTO<T> value)
  {
    this.values.add(value);
  }

  public T getValue()
  {
    return value;
  }

  public void setValue(T value)
  {
    this.value = value;
  }

  @SuppressWarnings("unchecked")
  @JsonIgnore
  public void setObjectValue(Object value)
  {
    this.setValue((T) value);
  }

  @JsonIgnore
  public Optional<T> getValue(Date date)
  {
    return this.getValues().stream() //
        .filter(vot -> ( vot.getStartDate().equals(date) || vot.getStartDate().before(date) ) && ( vot.getEndDate().equals(date) || vot.getEndDate().after(date) )) //
        .findFirst() //
        .map(vot -> vot.getValue());
  }

}
