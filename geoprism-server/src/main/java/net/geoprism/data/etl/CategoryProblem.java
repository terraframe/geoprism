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

import org.json.JSONException;
import org.json.JSONObject;

public class CategoryProblem implements ImportProblemIF, Comparable<ImportProblemIF>
{
  public static final String TYPE = "categories";

  private String             label;

  private String             mdAttributeId;

  private String             attributeLabel;

  private String             categoryId;

  public CategoryProblem(String label, String categoryId, String mdAttributeId, String attributeLabel)
  {
    this.label = label;
    this.categoryId = categoryId;
    this.mdAttributeId = mdAttributeId;
    this.attributeLabel = attributeLabel;
  }

  public String getMdAttributeId()
  {
    return mdAttributeId;
  }

  public String getKey()
  {
    return this.categoryId + "-" + this.label;
  }

  @Override
  public JSONObject toJSON() throws JSONException
  {
    JSONObject object = new JSONObject();
    object.put("type", "DOMAIN");
    object.put("label", label);
    object.put("categoryId", categoryId);
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
