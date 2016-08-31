/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General
 * Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with Runway SDK(tm). If not, see
 * <http://www.gnu.org/licenses/>.
 */
package net.geoprism.data.etl;

import org.json.JSONException;
import org.json.JSONObject;

public class CategoryProblem implements ImportProblemIF, Comparable<ImportProblemIF>
{
  public static final String TYPE = "categories";

  private String             label;

  private String             mdAttributeId;

  private String             attributeLabel;

  private String             rootId;

  public CategoryProblem(String label, String rootId, String mdAttributeId, String attributeLabel)
  {
    this.label = label;
    this.rootId = rootId;
    this.mdAttributeId = mdAttributeId;
    this.attributeLabel = attributeLabel;
  }

  public String getMdAttributeId()
  {
    return mdAttributeId;
  }

  public String getKey()
  {
    return this.rootId + "-" + this.label;
  }

  @Override
  public JSONObject toJSON() throws JSONException
  {
    JSONObject object = new JSONObject();
    object.put("type", "DOMAIN");
    object.put("label", label);
    object.put("mdAttributeId", mdAttributeId);
    object.put("attributeLabel", attributeLabel);

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