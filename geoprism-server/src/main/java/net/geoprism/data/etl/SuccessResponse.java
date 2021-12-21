/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
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
package net.geoprism.data.etl;

import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.dataaccess.ProgrammingErrorException;

import net.geoprism.ContentStream;

public class SuccessResponse implements ImportResponseIF
{
  private JSONArray  datasets;

  private JSONObject summary;

  public SuccessResponse(JSONArray datasets, JSONObject summary)
  {
    this.datasets = datasets;
    this.summary = summary;
  }

  public JSONObject toJSON() throws JSONException
  {
    JSONObject object = new JSONObject();
    object.put("success", true);
    object.put("datasets", datasets);

    if (summary != null)
    {
      object.put("summary", summary);
    }

    return object;
  }

  @Override
  public boolean hasProblems()
  {
    return false;
  }

  public String getJSON()
  {
    try
    {
      JSONObject object = this.toJSON();

      return object.toString();
    }
    catch (JSONException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

  @Override
  public ContentStream getStream()
  {
    byte[] bytes = this.getJSON().getBytes(Charset.forName("UTF-8"));

    return new ContentStream(new ByteArrayInputStream(bytes), "application/json");
  }
}
