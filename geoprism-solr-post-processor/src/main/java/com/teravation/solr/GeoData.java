/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package com.teravation.solr;

import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.opencsv.CSVWriter;

/**
 * @author chris
 *
 *         This is a Data Object class for individual GeoData entries. At this point the only functionality it has is to
 *         render itself as a tag
 */
public class GeoData
{
  private String                    displayLabel;

  /**
   * Map of all associated location ids and a list of their direct parent location ids
   */
  private Map<String, List<String>> locationMap;

  /**
   * Constuctor from locationId and displayLabel
   * 
   * @param displayLabel
   *          The display label for this GeoData
   */
  public GeoData(String displayLabel)
  {
    super();

    this.displayLabel = displayLabel;
    this.locationMap = new LinkedHashMap<String, List<String>>();
  }

  /**
   * @return The display label for this GeoData
   */
  public String getDisplayLabel()
  {
    return displayLabel;
  }

  /**
   * @param displayLabel
   *          The display label for this GeoData
   */
  public void setDisplayLabel(String displayLabel)
  {
    this.displayLabel = displayLabel;
  }

  /**
   * @return The location id for this GeoData
   */
  public String getLocationId(String previousToken)
  {
    Set<Entry<String, List<String>>> entries = this.locationMap.entrySet();

    if (previousToken != null && this.locationMap.size() > 0)
    {
      Iterator<Entry<String, List<String>>> iterator = entries.iterator();

      while (iterator.hasNext())
      {
        Entry<String, List<String>> entry = iterator.next();
        List<String> parents = entry.getValue();

        if (parents.contains(previousToken))
        {
          return entry.getKey();
        }
      }
    }

    /*
     * By default return the first option
     */
    return entries.iterator().next().getKey();
  }

  public Map<String, List<String>> getLocationMap()
  {
    return locationMap;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  public String toString()
  {
    return "{{" + this.locationMap + "}}";
  }

  public void addLocation(String locationId, String... parentIds)
  {
    this.locationMap.put(locationId, Arrays.asList(parentIds));
  }

  public void addLocation(String locationId, Collection<String> parentIds)
  {
    this.locationMap.put(locationId, new LinkedList<String>(parentIds));
  }
}
