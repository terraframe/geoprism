package com.runwaysdk.geodashboard;

import com.runwaysdk.generation.loader.Reloadable;

public interface KeyGeneratorIF extends Reloadable
{
  public Long next();

  public String generateKey(String prefix);
}
