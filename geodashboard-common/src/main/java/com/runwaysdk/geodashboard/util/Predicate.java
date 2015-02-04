package com.runwaysdk.geodashboard.util;

import com.runwaysdk.generation.loader.Reloadable;

public interface Predicate<T> extends Reloadable
{
  public boolean evaulate(T _t);
}
