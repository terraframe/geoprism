/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism.dashboard.condition;

import java.util.LinkedList;
import java.util.List;

import net.geoprism.QueryUtil;
import net.geoprism.localization.LocalizationFacade;
import net.geoprism.ontology.GeoEntityUtil;

import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.generated.system.gis.geo.GeoEntityAllPathsTableQuery;
import com.runwaysdk.query.Attribute;
import com.runwaysdk.query.AttributeReference;
import com.runwaysdk.query.GeneratedComponentQuery;
import com.runwaysdk.query.ValueQuery;
import com.runwaysdk.system.gis.geo.GeoEntity;

public class LocationCondition extends DashboardCondition implements com.runwaysdk.generation.loader.Reloadable
{

  /**
   * Condition type for restricting on global location
   */
  public static final String CONDITION_TYPE = "LOCATION_CONDITION";

  /**
   * JSON key for the serialized label attribute
   */
  public static final String LABEL_KEY      = "label";

  private String             comparisonValueId;

  public LocationCondition()
  {
    super();
  }

  public LocationCondition(String comparisonValueId)
  {
    super();

    this.comparisonValueId = comparisonValueId;
  }

  public String getComparisonValueId()
  {
    return comparisonValueId;
  }

  public void setComparisonValueId(String comparisonValueId)
  {
    this.comparisonValueId = comparisonValueId;
  }

  public GeoEntity getComparisonValue()
  {
    if (this.comparisonValueId != null && this.comparisonValueId.length() > 0)
    {
      return GeoEntity.get(comparisonValueId);
    }

    return null;
  }

  @Override
  public void restrictQuery(ValueQuery _vQuery, Attribute _attribute)
  {
    GeoEntity entity = this.getComparisonValue();

    if (entity != null)
    {
      AttributeReference attributeReference = (AttributeReference) _attribute;

      GeoEntityAllPathsTableQuery aptQuery = new GeoEntityAllPathsTableQuery(_vQuery);

      _vQuery.AND(aptQuery.getParentTerm().EQ(entity));
      _vQuery.AND(attributeReference.EQ(aptQuery.getChildTerm()));
    }
  }

  // @Override
  public String getComparisonLabel()
  {
    GeoEntity entity = this.getComparisonValue();

    if (entity != null)
    {
      return GeoEntityUtil.getEntityLabel(entity);
    }

    return "";
  }

  @Override
  public JSONObject getJSON()
  {
    try
    {
      JSONObject object = new JSONObject();
      object.put(TYPE_KEY, CONDITION_TYPE);
      object.put(OPERATION_KEY, OPERATION_KEY);
      object.put(VALUE_KEY, this.getComparisonValueId());
      object.put(LABEL_KEY, this.getComparisonLabel());

      return object;
    }
    catch (JSONException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

  @Override
  public String getJSONKey()
  {
    return CONDITION_TYPE;
  }

  @Override
  public List<String> getConditionInformation()
  {
    List<String> messages = new LinkedList<String>();

    GeoEntity entity = this.getComparisonValue();

    if (entity != null)
    {
      String localizedValue = entity.getDisplayLabel().getValue();

      String message = LocalizationFacade.getFromBundles("location.condition");
      message = message.replace("{0}", localizedValue);

      messages.add(message);
    }

    return messages;
  }

  @Override
  public void restrictQuery(String _type, ValueQuery _vQuery, GeneratedComponentQuery _query)
  {
    MdClassDAOIF mdClass = _query.getMdClassIF();
    MdAttributeDAOIF mdAttribute = QueryUtil.getGeoEntityAttribute(mdClass);

    if (mdAttribute != null)
    {
      AttributeReference attribute = (AttributeReference) _query.get(mdAttribute.definesAttribute());
      
      this.restrictQuery(_vQuery, attribute);
    }
  }
}
