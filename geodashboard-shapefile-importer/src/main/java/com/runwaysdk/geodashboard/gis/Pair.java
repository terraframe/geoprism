package com.runwaysdk.geodashboard.gis;

public class Pair<T, S>
{
  private T key;

  private S value;

  public Pair(T key, S value)
  {
    this.key = key;
    this.value = value;
  }

  public T getKey()
  {
    return key;
  }

  public S getValue()
  {
    return value;
  }
}
