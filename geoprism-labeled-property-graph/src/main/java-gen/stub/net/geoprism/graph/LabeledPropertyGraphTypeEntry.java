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

import com.google.gson.JsonObject;

import net.geoprism.registry.DateUtil;
import net.geoprism.registry.lpg.LabeledVersion;

public class LabeledPropertyGraphTypeEntry extends LabeledPropertyGraphTypeEntryBase implements LabeledVersion
{
  private static final long  serialVersionUID = 1112663869;

  public static final String VERSIONS         = "versions";

  public LabeledPropertyGraphTypeEntry()
  {
    super();
  }

  public JsonObject toJSON()
  {
    LabeledPropertyGraphType listType = this.getGraphType();

    JsonObject object = new JsonObject();

    if (this.isAppliedToDB())
    {
      object.addProperty(LabeledPropertyGraphTypeVersion.OID, this.getOid());
    }

    object.addProperty(LabeledPropertyGraphType.DISPLAYLABEL, listType.getDisplayLabel().getValue());
    object.addProperty(LabeledPropertyGraphTypeVersion.FORDATE, DateUtil.formatDate(this.getForDate(), false));
    object.addProperty(LabeledPropertyGraphTypeVersion.CREATEDATE, DateUtil.formatDate(this.getCreateDate(), false));
    object.add(LabeledPropertyGraphTypeVersion.PERIOD, listType.formatVersionLabel(this));

    return object;
  }

}
