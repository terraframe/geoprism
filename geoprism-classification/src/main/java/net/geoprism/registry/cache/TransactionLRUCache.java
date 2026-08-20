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
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.function.Supplier;

import org.apache.commons.collections4.map.LRUMap;

import com.runwaysdk.dataaccess.transaction.TransactionState;

import net.geoprism.registry.command.AbstractCommand;

public class TransactionLRUCache<K, V>
{
  private static class CacheResult<V>
  {
    private V v;

    public CacheResult(V v)
    {
      this.v = v;
    }

    public V get()
    {
      return v;
    }
  }

  private static class TransactionCache<K, V>
  {
    private final String           cacheKey;

    private final Function<V, K[]> mapper;

    public TransactionCache(String cacheKey, Function<V, K[]> mapper)
    {
      super();
      this.cacheKey = cacheKey;
      this.mapper = mapper;
    }

    public void put(V value)
    {
      Map<K, CacheResult<V>> cache = getTransactionCache();

      if (cache != null)
      {
        CacheResult<V> result = new CacheResult<V>(value);

        K[] keys = this.mapper.apply(value);

        for (K key : keys)
        {
          cache.put(key, result);
        }
      }
    }

    public Optional<CacheResult<V>> get(K key)
    {
      Map<K, CacheResult<V>> cache = getTransactionCache();

      if (cache != null)
      {
        return Optional.ofNullable(cache.get(key));
      }

      return Optional.empty();
    }

    @SuppressWarnings("unchecked")
    private Map<K, CacheResult<V>> getTransactionCache()
    {
      TransactionState state = TransactionState.getCurrentTransactionState();

      if (state != null)
      {
        Map<K, CacheResult<V>> cache = (Map<K, CacheResult<V>>) state.getTransactionObject(cacheKey);

        if (cache == null)
        {
          cache = new TreeMap<K, CacheResult<V>>();

          state.putTransactionObject(cacheKey, cache);
        }

        return cache;
      }

      return null;
    }
  }

  // Objects in a transaction
  private final TransactionCache<K, V> transactionCache;

  // Objects in the global cache
  private final Map<K, CacheResult<V>> cache;

  private final Function<V, K[]>       mapper;

  public TransactionLRUCache(String cacheKey, Function<V, K[]> mapper)
  {
    this(cacheKey, mapper, 20);
  }

  public TransactionLRUCache(String cacheKey, Function<V, K[]> mapper, int maxSize)
  {
    this.mapper = mapper;
    this.transactionCache = new TransactionCache<>(cacheKey, mapper);
    this.cache = Collections.synchronizedMap(new LRUMap<K, CacheResult<V>>(20));
  }

  public void put(V value)
  {
    this.transactionCache.put(value);

    // If an object has been updated then remove the current version from the
    // cache so an updated version will be retrieved when the getter is next
    // called
    this.remove(value);
  }

  public void remove(V value)
  {
    new AbstractCommand()
    {
      @Override
      public void doIt()
      {
        K[] keys = mapper.apply(value);

        for (K key : keys)
        {
          cache.remove(key);
        }
      }
    }.doIt();
  }

  public Optional<V> get(K key, Supplier<Optional<V>> supplier)
  {
    return this.transactionCache.get(key) //
        .or(() -> {
          return Optional.ofNullable(this.cache.get(key));
        }).or(() -> {
          Optional<V> v = supplier.get();

          // Cache the empty response to prevent further look ups
          // of the same key value. This assumes that when an object
          // is created it is explicitly added to the cache.
//          if (v.isEmpty())
//          {
//            cache.put(key, new CacheResult<V>(null));
//          }

          return v.map(value -> {
            CacheResult<V> result = new CacheResult<V>(value);

            K[] keys = mapper.apply(value);

            for (K k : keys)
            {
              cache.put(k, result);
            }

            return result;
          });
        }).map(result -> result.get());
  }

  public void clear()
  {
    this.cache.clear();
  }

}
