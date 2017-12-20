package net.geoprism;

import java.util.HashMap;
import java.util.LinkedList;

public class Cache<K, V>
{
  protected int           cacheSize;

  protected HashMap<K, V> map;

  protected LinkedList<K> list;

  public Cache(int cacheSize)
  {
    this.cacheSize = cacheSize;
    this.map = new HashMap<K, V>(cacheSize);
    this.list = new LinkedList<K>();
  }

  public void put(K key, V val)
  {
    // check if pruning is needed
    if (list.size() == this.cacheSize)
    {
      this.prune();
    }

    list.addFirst(key);
    map.put(key, val);
  }

  public V get(K key)
  {
    boolean res = list.remove(key);

    if (res)
    {
      list.addFirst(key);
      return map.get(key);
    }
    return null;
  }

  public void invalidate(Object key)
  {
    list.remove(key);
    map.remove(key);
  }

  public void prune()// removes the tail
  {
    K key = list.removeLast();
    map.remove(key);
  }

  public boolean containsKey(K key)
  {
    return this.map.containsKey(key);
  }

}