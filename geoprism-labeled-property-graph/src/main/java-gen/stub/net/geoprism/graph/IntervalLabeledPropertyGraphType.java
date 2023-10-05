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
package net.geoprism.graph;

import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.runwaysdk.Pair;

import net.geoprism.graph.lpg.LabeledVersion;
import net.geoprism.registry.DateUtil;

public class IntervalLabeledPropertyGraphType extends IntervalLabeledPropertyGraphTypeBase
{
  @SuppressWarnings("unused")
  private static final long  serialVersionUID = -223471707;

  public static final String START_DATE       = "startDate";

  public static final String END_DATE         = "endDate";

  public IntervalLabeledPropertyGraphType()
  {
    super();
  }

  @Override
  public JsonObject toJSON()
  {
    JsonObject object = super.toJSON();
    object.addProperty(GRAPH_TYPE, INTERVAL);
    object.add(INTERVALJSON, JsonParser.parseString(this.getIntervalJson()));

    return object;
  }

  @Override
  public void parse(JsonObject object)
  {
    super.parse(object);

    this.setIntervalJson(object.get(INTERVALJSON).getAsJsonArray().toString());
  }

  public List<Pair<Date, Date>> getIntervals()
  {
    List<Pair<Date, Date>> list = new LinkedList<Pair<Date, Date>>();

    JsonArray intervals = JsonParser.parseString(this.getIntervalJson()).getAsJsonArray();

    for (int i = 0; i < intervals.size(); i++)
    {
      JsonObject interval = intervals.get(i).getAsJsonObject();
      Date startDate = DateUtil.parseDate(interval.get(START_DATE).getAsString());
      Date endDate = DateUtil.parseDate(interval.get(END_DATE).getAsString());

      list.add(new Pair<Date, Date>(startDate, endDate));
    }

    list.sort(new Comparator<Pair<Date, Date>>()
    {
      @Override
      public int compare(Pair<Date, Date> o1, Pair<Date, Date> o2)
      {
        int compareTo = o1.getFirst().compareTo(o2.getFirst());

        if (compareTo == 0)
        {
          return o1.getSecond().compareTo(o2.getSecond());
        }

        return compareTo;
      }

    });

    return list;
  }

  @Override
  public JsonObject formatVersionLabel(LabeledVersion version)
  {
    Date versionDate = version.getForDate();
    List<Pair<Date, Date>> intervals = this.getIntervals();

    for (Pair<Date, Date> interval : intervals)
    {
      Date startDate = interval.getFirst();
      Date endDate = interval.getSecond();

      if (DateUtil.isBetweenInclusive(versionDate, startDate, endDate))
      {
        JsonObject range = new JsonObject();
        range.addProperty("startDate", DateUtil.formatDate(startDate, false));
        range.addProperty("endDate", DateUtil.formatDate(endDate, false));

        JsonObject object = new JsonObject();
        object.addProperty("type", "range");
        object.add("value", range);

        return object;
      }
    }

    throw new UnsupportedOperationException();
  }

  @Override
  public List<Date> getEntryDates()
  {
    return this.getIntervals().stream().map(pair -> pair.getFirst()).collect(Collectors.toList());
  }

  @Override
  public void apply()
  {
    List<Pair<Date, Date>> intervals = this.getIntervals();

    Iterator<Pair<Date, Date>> iterator = intervals.iterator();

    Pair<Date, Date> prev = null;

    while (iterator.hasNext())
    {
      Pair<Date, Date> pair = iterator.next();

      if (prev != null && ( prev.getSecond().after(pair.getFirst()) || prev.getSecond().equals(pair.getFirst()) ))
      {
        throw new UnsupportedOperationException("No overlaps");
      }

      prev = pair;
    }

    super.apply();
  }

}
