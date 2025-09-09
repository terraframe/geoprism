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
      Map<K, V> cache = getTransactionCache();

      if (cache != null)
      {
        K[] keys = this.mapper.apply(value);

        for (K key : keys)
        {
          cache.put(key, value);
        }
      }
    }

    public Optional<V> get(K key)
    {
      Map<K, V> cache = getTransactionCache();

      if (cache != null)
      {
        return Optional.ofNullable(cache.get(key));
      }

      return Optional.empty();
    }

    @SuppressWarnings("unchecked")
    private Map<K, V> getTransactionCache()
    {
      TransactionState state = TransactionState.getCurrentTransactionState();

      if (state != null)
      {
        Map<K, V> cache = (Map<K, V>) state.getTransactionObject(cacheKey);

        if (cache == null)
        {
          cache = new TreeMap<K, V>();

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
  private final Map<K, V>              cache;

  private final Function<V, K[]>       mapper;

  public TransactionLRUCache(String cacheKey, Function<V, K[]> mapper)
  {
    this(cacheKey, mapper, 20);
  }

  public TransactionLRUCache(String cacheKey, Function<V, K[]> mapper, int maxSize)
  {
    this.mapper = mapper;
    this.transactionCache = new TransactionCache<>(cacheKey, mapper);
    this.cache = Collections.synchronizedMap(new LRUMap<K, V>(20));
  }

  public void put(V value)
  {
    this.transactionCache.put(value);

    // If an object has been updated then remove the current version from the
    // cache so an updated version will be retrieved when its the getter next
    // called
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
          cache.remove(key, value);
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

          v.ifPresent(value -> {
            K[] keys = mapper.apply(value);

            for (K k : keys)
            {
              cache.put(k, value);
            }
          });

          return v;
        });
  }

  public void clear()
  {
    this.cache.clear();
  }

}
