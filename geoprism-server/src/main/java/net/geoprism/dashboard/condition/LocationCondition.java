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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.generated.system.gis.geo.GeoEntityAllPathsTableQuery;
import com.runwaysdk.query.AttributeReference;
import com.runwaysdk.query.GeneratedComponentQuery;
import com.runwaysdk.query.Selectable;
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

  public static final String LOCATIONS_KEY  = "locations";

  private String             locations;

  public LocationCondition()
  {
    super();
  }

  public LocationCondition(String locations)
  {
    super();

    this.locations = locations;
  }

  public String getLocations()
  {
    return locations;
  }

  public void setLocations(String locations)
  {
    this.locations = locations;
  }

  public List<GeoEntity> getGeoEntities()
  {
    List<GeoEntity> entities = new LinkedList<GeoEntity>();

    /*
     * Handle legacy data
     */
    if (this.locations != null && this.locations.length() > 0)
    {
      if (!this.locations.startsWith("["))
      {
        entities.add(GeoEntity.get(this.locations));
      }
      else
      {
        try
        {
          JSONArray array = new JSONArray(this.locations);

          for (int i = 0; i < array.length(); i++)
          {
            JSONObject location = array.getJSONObject(i);
            String entityId = location.getString(VALUE_KEY);

            entities.add(GeoEntity.get(entityId));
          }
        }
        catch (JSONException e)
        {
          throw new ProgrammingErrorException(e);
        }
      }
    }

    return entities;
  }

  @Override
  public void restrictQuery(ValueQuery _vQuery, Selectable _attribute)
  {
    List<GeoEntity> entities = this.getGeoEntities();

    if (entities.size() > 0)
    {
      AttributeReference attributeReference = (AttributeReference) _attribute;

      GeoEntityAllPathsTableQuery aptQuery = new GeoEntityAllPathsTableQuery(_vQuery);

      for (GeoEntity entity : entities)
      {
        aptQuery.OR(aptQuery.getParentTerm().EQ(entity));
      }

      _vQuery.AND(attributeReference.EQ(aptQuery.getChildTerm()));
    }
  }

  @Override
  public JSONObject getJSON()
  {
    try
    {
      JSONArray locations = new JSONArray();

      List<GeoEntity> entities = this.getGeoEntities();

      for (GeoEntity entity : entities)
      {
        JSONObject location = new JSONObject();
        location.put(VALUE_KEY, entity.getId());
        location.put(LABEL_KEY, GeoEntityUtil.getEntityLabel(entity));

        locations.put(location);
      }

      JSONObject object = new JSONObject();
      object.put(TYPE_KEY, CONDITION_TYPE);
      object.put(OPERATION_KEY, OPERATION_KEY);
      object.put(LOCATIONS_KEY, locations);

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

    List<GeoEntity> entities = this.getGeoEntities();

    for (GeoEntity entity : entities)
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
