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

import java.util.Date;

import org.commongeoregistry.adapter.dataaccess.LocalizedValue;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.geoprism.registry.DateFormatter;

@JsonTypeName(AttributeClassificationType.TYPE)
public class AttributeClassificationType extends AttributeType
{
  /**
   * 
   */
  private static final long  serialVersionUID = 6431580798592645011L;

  public static final String TYPE             = "classification";

  public static final String JSON_ROOT_TERM   = "rootTerm";

  public static final String JSON_CONCEPT_SET = "conceptSet";

  public static final String JSON_START_DATE  = "startDate";

  public static final String JSON_END_DATE    = "endDate";

  private CodeReference      rootTerm         = null;

  private String             conceptSet       = null;

  private Date               startDate;

  private Date               endDate;

  public AttributeClassificationType()
  {
  }

  public AttributeClassificationType(String _name, LocalizedValue _label, LocalizedValue _description, boolean _isDefault, boolean _required, boolean _unique)
  {
    super(_name, _label, _description, _isDefault, _required, _unique);
  }

  public AttributeClassificationType(String _name, LocalizedValue _label, LocalizedValue _description, boolean _isDefault, boolean _required, boolean _unique, boolean isChangeOverTime)
  {
    super(_name, _label, _description, _isDefault, _required, _unique, isChangeOverTime);
  }

  @Override
  @JsonIgnore
  public String getType()
  {
    return TYPE;
  }

  public CodeReference getRootTerm()
  {
    return rootTerm;
  }

  public void setRootTerm(CodeReference rootTerm)
  {
    this.rootTerm = rootTerm;
  }

  public void setConceptSet(String conceptSet)
  {
    this.conceptSet = conceptSet;
  }

  public String getConceptSet()
  {
    return conceptSet;
  }

  public Date getStartDate()
  {
    return startDate;
  }

  public void setStartDate(Date startDate)
  {
    this.startDate = startDate;
  }

  public Date getEndDate()
  {
    return endDate;
  }

  public void setEndDate(Date endDate)
  {
    this.endDate = endDate;
  }

  @Override
  public JsonObject toJSON(CustomSerializer serializer)
  {
    JsonObject json = super.toJSON(serializer);
    json.addProperty(JSON_CONCEPT_SET, this.getConceptSet());
    json.addProperty(JSON_START_DATE, DateFormatter.formatDate(this.getStartDate(), false));
    json.addProperty(JSON_END_DATE, DateFormatter.formatDate(this.getEndDate(), false));

    if (this.rootTerm != null)
    {
      json.add(JSON_ROOT_TERM, this.getRootTerm().toJSON());
    }

    return json;
  }

  /**
   * Populates any additional attributes from JSON that were not populated in
   * {@link GeoObjectType#fromJSON(String, org.commongeoregistry.adapter.RegistryAdapter)}
   * 
   * @param attrObj
   * @return {@link AttributeType}
   */
  @Override
  public void fromJSON(JsonObject attrObj)
  {
    super.fromJSON(attrObj);

    this.setConceptSet(attrObj.get(AttributeClassificationType.JSON_CONCEPT_SET).getAsString());
    this.setStartDate(DateFormatter.parseDate(attrObj.get(AttributeClassificationType.JSON_START_DATE).getAsString()));
    this.setEndDate(DateFormatter.parseDate(attrObj.get(AttributeClassificationType.JSON_END_DATE).getAsString()));

    JsonElement termElement = attrObj.get(AttributeClassificationType.JSON_ROOT_TERM);

    if (termElement != null && !termElement.isJsonNull())
    {
      this.setRootTerm(new CodeReference().fromJSON(termElement.getAsJsonObject()));
    }
  }

  @Override
  public void validate(Object _value)
  {
  }

}
