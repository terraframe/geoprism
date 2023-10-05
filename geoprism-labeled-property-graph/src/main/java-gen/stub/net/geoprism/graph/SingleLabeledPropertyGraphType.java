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
package net.geoprism.graph;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.google.gson.JsonObject;

import net.geoprism.graph.lpg.LabeledVersion;
import net.geoprism.registry.DateUtil;

public class SingleLabeledPropertyGraphType extends SingleLabeledPropertyGraphTypeBase
{
  @SuppressWarnings("unused")
  private static final long serialVersionUID = -1579048184;

  public SingleLabeledPropertyGraphType()
  {
    super();
  }

  @Override
  public JsonObject toJSON()
  {
    JsonObject object = super.toJSON();
    object.addProperty(GRAPH_TYPE, SINGLE);
    object.addProperty(VALIDON, DateUtil.formatDate(this.getValidOn(), false));

    return object;
  }

  @Override
  public void parse(JsonObject object)
  {
    super.parse(object);

    this.setValidOn(DateUtil.parseDate(object.get(SingleLabeledPropertyGraphType.VALIDON).getAsString()));
  }

  @Override
  public List<Date> getEntryDates()
  {
    return Arrays.asList(this.getValidOn());
  }

  @Override
  public JsonObject formatVersionLabel(LabeledVersion version)
  {
    JsonObject object = new JsonObject();
    object.addProperty("type", "date");
    object.addProperty("value", DateUtil.formatDate(this.getValidOn(), false));

    return object;
  }

}
