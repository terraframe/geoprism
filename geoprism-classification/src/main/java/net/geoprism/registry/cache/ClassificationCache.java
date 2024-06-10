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
package net.geoprism.registry.cache;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import net.geoprism.registry.model.Classification;

public class ClassificationCache
{
  @SuppressWarnings("serial")
  protected class LinkedHashMapCache<a, b> extends LinkedHashMap<a, b>
  {
    protected LinkedHashMapCache()
    {
      super(cacheSize + 1, .75F, true);
    }

    protected boolean removeEldestEntry(@SuppressWarnings("rawtypes") Map.Entry eldest)
    {
      return size() > cacheSize;
    }
  };

  protected Integer cacheSize = 500;

  protected Map<String, Map<String, Classification>> cache = Collections.synchronizedMap(new HashMap<String, Map<String, Classification>>());

  public ClassificationCache()
  {

  }

  public ClassificationCache(Integer cacheSize)
  {
    this.cacheSize = cacheSize;
  }

  public Classification getClassification(String classificationType, String code)
  {
    if (!this.cache.containsKey(classificationType))
    {
      this.cache.put(classificationType, new LinkedHashMapCache<String, Classification>());
    }

    return this.cache.get(classificationType).get(code);
  }

  public void putClassification(String classificationType, String code, Classification classification)
  {
    if (!this.cache.containsKey(classificationType))
    {
      this.cache.put(classificationType, new LinkedHashMap<String, Classification>());
    }

    this.cache.get(classificationType).put(code, classification);
  }
}
