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
package net.geoprism.ontology;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.opencsv.CSVWriter;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.session.Request;
import com.runwaysdk.system.gis.geo.GeoEntity;
import com.runwaysdk.system.gis.geo.GeoEntityQuery;
import com.runwaysdk.system.gis.geo.Synonym;
import com.runwaysdk.system.gis.geo.Universal;

public class GeoAnnotation
{
  @Request
  public static Map<String, Set<String>> getMap(List<Universal> universals)
  {
    Map<String, Set<String>> map = new HashMap<String, Set<String>>();

    GeoEntityQuery query = new GeoEntityQuery(new QueryFactory());

    universals.forEach((item) -> query.OR(query.getUniversal().EQ(item)));

    OIterator<? extends GeoEntity> iterator = query.getIterator();

    try
    {
      while (iterator.hasNext())
      {
        GeoEntity entity = iterator.next();
        String geoId = entity.getGeoId();

        map.put(geoId, new TreeSet<String>());
        map.get(geoId).add(entity.getDisplayLabel().getValue());

        OIterator<? extends Synonym> synonyms = entity.getAllSynonym();

        while (synonyms.hasNext())
        {
          Synonym synonym = synonyms.next();

          map.get(geoId).add(synonym.getDisplayLabel().getValue());
        }
      }
    }
    finally
    {
      iterator.close();
    }

    return map;
  }

  public static String annotate(String content, Map<String, Set<String>> entities)
  {
    String annotated = new String(content);

    Set<Entry<String, Set<String>>> entries = entities.entrySet();

    for (Entry<String, Set<String>> entry : entries)
    {
      Set<String> tokens = entry.getValue();

      for (String token : tokens)
      {
        Pattern p = Pattern.compile("(\\s)(" + token + ")(\\s)");
        Matcher m = p.matcher(annotated);

        if (m.find())
        {
          annotated = m.replaceAll("$1$2 {{" + entry.getKey() + "}}$3");
        }
      }
    }

    return annotated;
  }

  public static void write(Writer w, List<Universal> universals) throws IOException
  {
    Map<String, Set<String>> map = GeoAnnotation.getMap(universals);

    CSVWriter writer = new CSVWriter(w, ',', '"');

    try
    {

      map.entrySet().forEach((entry) -> {
        String key = entry.getKey();

        Set<String> values = entry.getValue();

        values.forEach((value) -> writer.writeNext(new String[] { value, key }));
      });
    }
    finally
    {
      writer.close();
    }
  }
}
