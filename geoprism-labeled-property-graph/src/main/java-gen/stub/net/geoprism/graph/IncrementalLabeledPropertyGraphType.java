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

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.JsonObject;
import com.runwaysdk.Pair;

import net.geoprism.graph.lpg.LabeledVersion;
import net.geoprism.registry.DateUtil;

public class IncrementalLabeledPropertyGraphType extends IncrementalLabeledPropertyGraphTypeBase
{
  @SuppressWarnings("unused")
  private static final long serialVersionUID = 794228873;

  public IncrementalLabeledPropertyGraphType()
  {
    super();
  }

  @Override
  public JsonObject formatVersionLabel(LabeledVersion version)
  {
    JsonObject object = new JsonObject();

    object.addProperty("type", "date");
    object.addProperty("value", DateUtil.formatDate(version.getForDate(), false));

    return object;
  }

  public List<Date> getFrequencyDates(Date startDate, Date endDate)
  {
    final Date today = new Date();
    LinkedList<Date> dates = new LinkedList<Date>();

    if (startDate != null && endDate != null)
    {
      Calendar end = Calendar.getInstance(DateUtil.SYSTEM_TIMEZONE);
      end.setTime(endDate);

      if (end.getTime().after(today))
      {
        end.setTime(today);
      }

      List<ChangeFrequency> frequencies = this.getFrequency();

      if (frequencies.contains(ChangeFrequency.ANNUAL))
      {
        Calendar calendar = Calendar.getInstance(DateUtil.SYSTEM_TIMEZONE);
        calendar.setTime(startDate);

        while (calendar.before(end) || calendar.equals(end))
        {
          dates.add(calendar.getTime());

          calendar.add(Calendar.YEAR, 1);
        }
      }
      else if (frequencies.contains(ChangeFrequency.BIANNUAL))
      {
        Calendar calendar = Calendar.getInstance(DateUtil.SYSTEM_TIMEZONE);
        calendar.setTime(startDate);

        while (calendar.before(end) || calendar.equals(end))
        {
          dates.add(calendar.getTime());

          calendar.add(Calendar.MONTH, 6);
        }
      }
      else if (frequencies.contains(ChangeFrequency.QUARTER))
      {
        Calendar calendar = Calendar.getInstance(DateUtil.SYSTEM_TIMEZONE);
        calendar.setTime(startDate);

        while (calendar.before(end) || calendar.equals(end))
        {
          dates.add(calendar.getTime());

          calendar.add(Calendar.MONTH, 3);
        }
      }
      else if (frequencies.contains(ChangeFrequency.MONTHLY))
      {
        Calendar calendar = Calendar.getInstance(DateUtil.SYSTEM_TIMEZONE);
        calendar.setTime(startDate);

        while (calendar.before(end) || calendar.equals(end))
        {
          dates.add(calendar.getTime());

          calendar.add(Calendar.MONTH, 1);
        }
      }
      else
      {
        throw new UnsupportedOperationException();
      }
    }

    return dates;
  }

  public ChangeFrequency toFrequency()
  {
    if (this.getFrequency().size() > 0)
    {
      return this.getFrequency().get(0);
    }

    return ChangeFrequency.ANNUAL;
  }

  private Pair<Date, Date> getDateRange()
  {
    // Pair<Date, Date> range = VertexServerGeoObject.getDataRange(objectType);
    //
    // // Only use the publishing start date if there is an actual range of data
    // if (this.getPublishingStartDate() != null && range != null)
    // {
    // return new Pair<Date, Date>(this.getPublishingStartDate(),
    // range.getSecond());
    // }
    // return range;

    return new Pair<Date, Date>(this.getPublishingStartDate(), this.getPublishingStartDate());

  }

  @Override
  public List<Date> getEntryDates()
  {
    Pair<Date, Date> range = this.getDateRange();

    if (range != null)
    {
      Date endDate = range.getSecond();

      if (endDate.after(new Date()))
      {
        endDate = DateUtil.getCurrentDate();
      }

      return this.getFrequencyDates(range.getFirst(), endDate);
    }

    return Arrays.asList(this.getPublishingStartDate());
  }

  @Override
  public JsonObject toJSON()
  {
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    format.setTimeZone(DateUtil.SYSTEM_TIMEZONE);

    JsonObject object = super.toJSON();
    object.addProperty(IncrementalLabeledPropertyGraphType.FREQUENCY, this.toFrequency().name());
    object.addProperty(IncrementalLabeledPropertyGraphType.PUBLISHINGSTARTDATE, format.format(this.getPublishingStartDate()));
    object.addProperty(GRAPH_TYPE, INCREMENTAL);

    return object;
  }

  @Override
  public void parse(JsonObject object)
  {
    super.parse(object);

    if (object.has(IncrementalLabeledPropertyGraphType.FREQUENCY) && !object.get(IncrementalLabeledPropertyGraphType.FREQUENCY).isJsonNull())
    {
      final String frequency = object.get(IncrementalLabeledPropertyGraphType.FREQUENCY).getAsString();

      final boolean same = this.getFrequency().stream().anyMatch(f -> {
        return f.name().equals(frequency);
      });

      if (!same)
      {
        this.clearFrequency();
        this.addFrequency(ChangeFrequency.valueOf(frequency));
      }
    }

    if (object.has(IncrementalLabeledPropertyGraphType.PUBLISHINGSTARTDATE))
    {
      if (!object.get(IncrementalLabeledPropertyGraphType.PUBLISHINGSTARTDATE).isJsonNull())
      {
        String date = object.get(IncrementalLabeledPropertyGraphType.PUBLISHINGSTARTDATE).getAsString();

        this.setPublishingStartDate(DateUtil.parseDate(date));
      }
      else
      {
        this.setPublishingStartDate(null);
      }
    }
  }

}
