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

import org.commongeoregistry.adapter.dataaccess.LocalizedValue;

import com.google.gson.JsonObject;

import net.geoprism.registry.DateUtil;
import net.geoprism.registry.conversion.LocalizedValueConverter;
import net.geoprism.registry.view.JsonSerializable;

public class LabeledPropertyGraphSynchronization extends LabeledPropertyGraphSynchronizationBase implements JsonSerializable
{
  @SuppressWarnings("unused")
  private static final long serialVersionUID = 63302423;

  public LabeledPropertyGraphSynchronization()
  {
    super();
  }

  public void parse(JsonObject object)
  {
    this.setUrl(object.get(LabeledPropertyGraphSynchronization.URL).getAsString());
    this.setRemoteType(object.get(LabeledPropertyGraphSynchronization.REMOTETYPE).getAsString());
    LocalizedValueConverter.populate(this.getDisplayLabel(), LocalizedValue.fromJSON(object.get(LabeledPropertyGraphType.DISPLAYLABEL).getAsJsonObject()));
    this.setRemoteEntry(object.get(LabeledPropertyGraphSynchronization.REMOTEENTRY).getAsString());
    this.setForDate(DateUtil.parseDate(object.get(LabeledPropertyGraphSynchronization.FORDATE).getAsString()));
    this.setRemoteVersion(object.get(LabeledPropertyGraphSynchronization.REMOTEVERSION).getAsString());
    this.setVersionNumber(object.get(LabeledPropertyGraphSynchronization.VERSIONNUMBER).getAsInt());
  }

  public final JsonObject toJSON()
  {
    JsonObject object = new JsonObject();

    if (this.isAppliedToDB())
    {
      object.addProperty(LabeledPropertyGraphSynchronization.OID, this.getOid());
    }

    object.addProperty(LabeledPropertyGraphSynchronization.URL, this.getUrl());
    object.addProperty(LabeledPropertyGraphSynchronization.REMOTETYPE, this.getRemoteType());
    object.add(LabeledPropertyGraphType.DISPLAYLABEL, LocalizedValueConverter.convertNoAutoCoalesce(this.getDisplayLabel()).toJSON());
    object.addProperty(LabeledPropertyGraphSynchronization.REMOTEENTRY, this.getRemoteEntry());
    object.addProperty(LabeledPropertyGraphSynchronization.FORDATE, DateUtil.formatDate(this.getForDate(), false));
    object.addProperty(LabeledPropertyGraphSynchronization.REMOTEVERSION, this.getRemoteVersion());
    object.addProperty(LabeledPropertyGraphSynchronization.VERSIONNUMBER, this.getVersionNumber());
    object.addProperty(LabeledPropertyGraphSynchronization.VERSION, this.getVersionOid());

    return object;
  }
}
