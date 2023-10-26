/**
 * Copyright (c) 2023 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Geoprism(tm).
 *
 * Geoprism(tm) is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * Geoprism(tm) is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Geoprism(tm). If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism.registry.service.business;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;

import net.geoprism.graph.IncrementalLabeledPropertyGraphType;
import net.geoprism.graph.IntervalLabeledPropertyGraphType;
import net.geoprism.graph.LabeledPropertyGraphType;
import net.geoprism.graph.LabeledPropertyGraphTypeEntry;
import net.geoprism.graph.LabeledPropertyGraphTypeEntryQuery;
import net.geoprism.graph.LabeledPropertyGraphTypeQuery;
import net.geoprism.graph.LabeledPropertyGraphTypeVersion;
import net.geoprism.graph.LabeledPropertyGraphTypeVersionQuery;
import net.geoprism.graph.SingleLabeledPropertyGraphType;

@Service
public class LabeledPropertyGraphTypeBusinessService implements LabeledPropertyGraphTypeBusinessServiceIF
{
  @Autowired
  private LabeledPropertyGraphTypeEntryBusinessServiceIF entryService;

  @Autowired
  private ClassificationTypeBusinessServiceIF            objectService;

  @Override
  public JsonArray list()
  {
    JsonArray response = new JsonArray();

    LabeledPropertyGraphTypeQuery query = new LabeledPropertyGraphTypeQuery(new QueryFactory());

    try (OIterator<? extends LabeledPropertyGraphType> it = query.getIterator())
    {
      SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

      it.getAll().stream().sorted((a, b) -> {
        return a.getDisplayLabel().getValue().compareTo(b.getDisplayLabel().getValue());
      }).forEach(list -> {
        JsonObject object = new JsonObject();
        object.addProperty("label", list.getDisplayLabel().getValue());
        object.addProperty("oid", list.getOid());

        object.addProperty("createDate", format.format(list.getCreateDate()));
        object.addProperty("lasteUpdateDate", format.format(list.getLastUpdateDate()));

        response.add(object);
      });
    }

    return response;
  }

  @Override
  @Transaction
  public LabeledPropertyGraphType apply(JsonObject object)
  {
    return this.apply(object, true);
  }

  @Override
  @Transaction
  public LabeledPropertyGraphType apply(JsonObject object, boolean createEntries)
  {
    LabeledPropertyGraphType type = this.fromJSON(object);
    this.apply(type);

    if (createEntries)
    {
      this.createEntries(type);
    }

    return type;
  }

  @Override
  @Transaction
  public void createEntries(LabeledPropertyGraphType type)
  {
    List<Date> dates = type.getEntryDates();

    for (Date date : dates)
    {
      this.createEntry(type, date);
    }
  }

  @Override
  @Transaction
  public void apply(LabeledPropertyGraphType type)
  {
    this.objectService.validateName(type.getCode());

    type.apply();
  }

  @Override
  @Transaction
  public void delete(LabeledPropertyGraphType type)
  {
    this.getEntries(type).forEach(entry -> {
      this.entryService.delete(entry);
    });

    type.delete();

    final File directory = type.getShapefileDirectory();

    if (directory.exists())
    {
      try
      {
        FileUtils.deleteDirectory(directory);
      }
      catch (IOException e)
      {
        throw new ProgrammingErrorException(e);
      }
    }
  }

  @Transaction
  public LabeledPropertyGraphTypeEntry getOrCreateEntry(LabeledPropertyGraphType type, Date forDate)
  {
    if (!type.isValid())
    {
      // throw new InvalidMasterListException();
    }

    LabeledPropertyGraphTypeEntryQuery query = new LabeledPropertyGraphTypeEntryQuery(new QueryFactory());
    query.WHERE(query.getGraphType().EQ(type));
    query.AND(query.getForDate().EQ(forDate));

    try (OIterator<? extends LabeledPropertyGraphTypeEntry> it = query.getIterator())
    {
      if (it.hasNext())
      {
        return it.next();
      }
    }

    return this.entryService.create(type, forDate);
  }

  @Override
  public LabeledPropertyGraphType fromJSON(JsonObject object)
  {
    LabeledPropertyGraphType list = null;

    if (object.has("oid") && !object.get("oid").isJsonNull())
    {
      String oid = object.get("oid").getAsString();

      list = LabeledPropertyGraphType.lock(oid);
    }
    else if (object.get(LabeledPropertyGraphType.GRAPH_TYPE).getAsString().equals(LabeledPropertyGraphType.SINGLE))
    {
      list = new SingleLabeledPropertyGraphType();
    }
    else if (object.get(LabeledPropertyGraphType.GRAPH_TYPE).getAsString().equals(LabeledPropertyGraphType.INCREMENTAL))
    {
      list = new IncrementalLabeledPropertyGraphType();
    }
    else if (object.get(LabeledPropertyGraphType.GRAPH_TYPE).getAsString().equals(LabeledPropertyGraphType.INTERVAL))
    {
      list = new IntervalLabeledPropertyGraphType();
    }
    else
    {
      throw new UnsupportedOperationException("Unknown list type");
    }

    list.parse(object);

    return list;
  }

  @Override
  public List<LabeledPropertyGraphTypeEntry> getEntries(LabeledPropertyGraphType type)
  {
    LabeledPropertyGraphTypeEntryQuery query = new LabeledPropertyGraphTypeEntryQuery(new QueryFactory());
    query.WHERE(query.getGraphType().EQ(type));
    query.ORDER_BY_DESC(query.getForDate());

    try (OIterator<? extends LabeledPropertyGraphTypeEntry> it = query.getIterator())
    {
      return new LinkedList<LabeledPropertyGraphTypeEntry>(it.getAll());
    }
  }

  @Override
  public List<LabeledPropertyGraphTypeVersion> getVersions(LabeledPropertyGraphType type)
  {
    LabeledPropertyGraphTypeVersionQuery query = new LabeledPropertyGraphTypeVersionQuery(new QueryFactory());
    query.WHERE(query.getGraphType().EQ(type));
    query.ORDER_BY_DESC(query.getForDate());

    try (OIterator<? extends LabeledPropertyGraphTypeVersion> it = query.getIterator())
    {
      return new LinkedList<LabeledPropertyGraphTypeVersion>(it.getAll());
    }
  }

  @Override
  public JsonObject toJSON(LabeledPropertyGraphType type)
  {
    return this.toJSON(type, false);
  }

  @Override
  public JsonObject toJSON(LabeledPropertyGraphType type, boolean includeEntries)
  {
    JsonObject object = type.toJSON();

    if (includeEntries)
    {
      List<LabeledPropertyGraphTypeEntry> entries = this.getEntries(type);

      JsonArray jEntries = new JsonArray();

      for (LabeledPropertyGraphTypeEntry entry : entries)
      {
        jEntries.add(this.entryService.toJSON(entry));
      }

      object.add("entries", jEntries);
    }

    return object;
  }

  @Transaction
  public LabeledPropertyGraphTypeEntry createEntry(LabeledPropertyGraphType type, Date forDate)
  {
    // LabeledPropertyGraphTypeEntry entry = new LabeledPropertyGraphUtil(this,
    // this.entryService).createEntry(type.getOid(), forDate);
    //
    // return entry;
    return this.entryService.create(type, forDate);
  }

  @Override
  public LabeledPropertyGraphType get(String oid)
  {
    return LabeledPropertyGraphType.get(oid);
  }

}
