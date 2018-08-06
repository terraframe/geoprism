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

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.system.gis.geo.GeoEntity;
import com.runwaysdk.system.gis.geo.Universal;

public class LocationProblem extends FieldValue implements ImportProblemIF, Comparable<ImportProblemIF>
{
  public static final String TYPE = "locations";

  private String             label;

  private List<JSONObject>   context;

  private GeoEntity          parent;

  private Universal          universal;

  public LocationProblem(String label, List<JSONObject> context, GeoEntity parent, Universal universal)
  {
    this.label = label;
    this.context = context;
    this.parent = parent;
    this.universal = universal;
  }

  public String getKey()
  {
    return this.parent.getId() + "-" + this.label;
  }

  @Override
  public JSONObject toJSON() throws JSONException
  {
    JSONObject object = new JSONObject();
    object.put("type", "LOCATION");
    object.put("label", label);
    object.put("parentId", parent.getId());
    object.put("universalId", universal.getId());
    object.put("universalLabel", universal.getDisplayLabel().getValue());
    object.put("context", new JSONArray(context));

    return object;
  }

  @Override
  public int compareTo(ImportProblemIF problem)
  {
    return this.getKey().compareTo(problem.getKey());
  }

  @Override
  public String getType()
  {
    return TYPE;
  }
}
