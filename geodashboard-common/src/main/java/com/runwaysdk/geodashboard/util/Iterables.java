package com.runwaysdk.geodashboard.util;

import java.util.Iterator;

import com.runwaysdk.generation.loader.Reloadable;

public class Iterables<T> implements Reloadable
{
  public void remove(Iterable<T> iterable, Predicate<T> predicate)
  {
    Iterator<T> iterator = iterable.iterator();

    while (iterator.hasNext())
    {
      T item = iterator.next();

      if (predicate.evaulate(item))
      {
        iterator.remove();
      }
    }
  }
}
