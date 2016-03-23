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
package net.geoprism.data.etl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DefinitionBuilder
{
  private SourceDefinitionIF source;

  private TargetDefinitionIF target;

  public DefinitionBuilder(SourceDefinitionIF source, TargetDefinitionIF target)
  {
    this.source = source;
    this.target = target;
  }

  public JSONObject getConfiguration() throws JSONException
  {
    JSONObject object = new JSONObject();
    object.put("name", source.getName());
    object.put("label", source.getLabel());
    object.put("country", source.getCountry());
    object.put("fields", this.getFields());
    object.put("attributes", this.getAttributes());
    object.put("coordinates", this.getCoordinates());

    if (!source.isNew())
    {
      object.put("existing", source.getId());
    }

    return object;
  }

  private JSONObject getAttributes() throws JSONException
  {
    JSONArray ids = new JSONArray();
    JSONObject values = new JSONObject();

    List<TargetFieldIF> fields = this.target.getFields();

    for (TargetFieldIF field : fields)
    {
      if (field instanceof TargetFieldGeoEntityIF)
      {
        TargetFieldGeoEntityIF eField = (TargetFieldGeoEntityIF) field;

        ids.put(eField.getId());

        values.put(eField.getId(), eField.toJSON());
      }
    }

    JSONObject object = new JSONObject();
    object.put("ids", ids);
    object.put("values", values);

    return object;
  }

  private JSONObject getCoordinates() throws JSONException
  {
    JSONArray ids = new JSONArray();
    JSONObject values = new JSONObject();

    List<TargetFieldIF> fields = this.target.getFields();

    for (TargetFieldIF field : fields)
    {
      if (field instanceof TargetFieldPointIF)
      {
        TargetFieldPointIF pField = (TargetFieldPointIF) field;

        ids.put(pField.getId());

        values.put(pField.getId(), pField.toJSON());
      }
    }

    JSONObject object = new JSONObject();
    object.put("ids", ids);
    object.put("values", values);

    return object;
  }

  private JSONArray getFields() throws JSONException
  {
    Map<String, String> map = this.getFieldUniversals();

    List<SourceFieldIF> sFields = this.source.getFields();
    JSONArray fields = new JSONArray();

    for (SourceFieldIF sField : sFields)
    {
      JSONObject object = sField.toJSON();

      String attributeName = sField.getAttributeName();

      if (map.containsKey(attributeName))
      {
        String universalId = map.get(attributeName);

        object.put("universal", universalId);
      }

      fields.put(object);
    }
    return fields;
  }

  private Map<String, String> getFieldUniversals()
  {
    Map<String, String> map = new HashMap<String, String>();

    List<TargetFieldIF> fields = this.target.getFields();

    for (TargetFieldIF field : fields)
    {
      if (field instanceof TargetFieldGeoEntityIF)
      {
        map.putAll( ( (TargetFieldGeoEntityIF) field ).getUniversalAttributes());
      }
    }

    return map;
  }
}
