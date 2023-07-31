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

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.runwaysdk.business.rbac.Authenticate;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.session.Session;

import net.geoprism.graph.service.LabeledPropertyGraphServiceIF;
import net.geoprism.registry.DateUtil;

public class LabeledPropertyGraphTypeEntry extends LabeledPropertyGraphTypeEntryBase implements LabeledVersion
{
  private static final long  serialVersionUID = 1112663869;

  public static final String VERSIONS         = "versions";

  public LabeledPropertyGraphTypeEntry()
  {
    super();
  }

  @Override
  @Transaction
  public void delete()
  {
    // Delete all versions
    // LabeledPropertyGraphTypeVersion working = this.getWorking();

    this.appLock();
    this.apply();

    this.getVersions().forEach(version -> {

      if (Session.getCurrentSession() != null)
      {
        version.remove();
      }
      else
      {
        version.delete();
      }
    });

    // if (working != null)
    // {
    // working.delete();
    // }

    super.delete();
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

    List<LabeledPropertyGraphTypeVersion> versions = this.getVersions();

    JsonArray jVersions = new JsonArray();

    for (LabeledPropertyGraphTypeVersion version : versions)
    {
      // Only include the versions the user has access to

      jVersions.add(version.toJSON());
    }

    object.add(LabeledPropertyGraphTypeEntry.VERSIONS, jVersions);

    return object;
  }

  public List<LabeledPropertyGraphTypeVersion> getVersions()
  {
    LabeledPropertyGraphTypeVersionQuery query = new LabeledPropertyGraphTypeVersionQuery(new QueryFactory());
    query.WHERE(query.getEntry().EQ(this));
    // query.AND(query.getWorking().EQ(false));
    query.ORDER_BY_DESC(query.getVersionNumber());

    try (OIterator<? extends LabeledPropertyGraphTypeVersion> it = query.getIterator())
    {
      return new LinkedList<LabeledPropertyGraphTypeVersion>(it.getAll());
    }
  }

  public JsonArray getVersionJson()
  {
    List<LabeledPropertyGraphTypeVersion> versions = this.getVersions();

    JsonArray jVersions = new JsonArray();

    for (LabeledPropertyGraphTypeVersion entry : versions)
    {
      jVersions.add(entry.toJSON());
    }

    return jVersions;
  }

  @Transaction
  public LabeledPropertyGraphTypeVersion createVersion()
  {
    List<LabeledPropertyGraphTypeVersion> versions = this.getVersions();

    int versionNumber = versions.size() > 0 ? versions.get(0).getVersionNumber() + 1 : 1;

    return LabeledPropertyGraphTypeVersion.create(this, false, versionNumber);
  }

  @Override
  @Authenticate
  public String publish()
  {
    // Create a new version and publish it
    LabeledPropertyGraphTypeVersion version = this.createVersion();

    // Refresh the users session
    ( (Session) Session.getCurrentSession() ).reloadPermissions();
    
    LabeledPropertyGraphServiceIF.getInstance().createPublishJob(version);

    return version.toJSON().toString();
  }

  @Transaction
  public static LabeledPropertyGraphTypeEntry create(LabeledPropertyGraphType list, Date forDate)
  {
    LabeledPropertyGraphTypeEntry entry = new LabeledPropertyGraphTypeEntry();
    entry.setGraphType(list);
    entry.setForDate(forDate);
    entry.apply();

    LabeledPropertyGraphTypeVersion.create(entry, true, 0);

    return entry;
  }

  @Transaction
  public static LabeledPropertyGraphTypeEntry create(LabeledPropertyGraphType list, JsonObject json)
  {
    String forDate = json.get(FORDATE).getAsString();

    LabeledPropertyGraphTypeEntry entry = new LabeledPropertyGraphTypeEntry();
    entry.setGraphType(list);
    entry.setForDate(DateUtil.parseDate(forDate, false));
    entry.apply();

    return entry;
  }

}
