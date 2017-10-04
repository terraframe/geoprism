package net.geoprism.util;

import java.util.HashMap;
import java.util.Map;

public class ProgressManager
{
  private Map<String, ProgressState> cache;

  public ProgressManager()
  {
    this.cache = new HashMap<String, ProgressState>();
  }

  public void add(ProgressState state)
  {
    this.cache.put(state.getId(), state);
  }

  public void remove(String id)
  {
    this.cache.remove(id);
  }

  public ProgressState get(String id)
  {
    return this.cache.get(id);
  }
}
