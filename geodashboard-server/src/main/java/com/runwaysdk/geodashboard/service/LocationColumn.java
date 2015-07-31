package com.runwaysdk.geodashboard.service;

import com.runwaysdk.generation.loader.Reloadable;

public class LocationColumn implements Reloadable, Comparable<LocationColumn>
{
  private String  name;

  private String  universalType;

  private Integer depth;

  public LocationColumn(String name, String universalType, Integer depth)
  {
    super();
    this.name = name;
    this.universalType = universalType;
    this.depth = depth;
  }

  public String getName()
  {
    return name;
  }

  public String getUniversalType()
  {
    return universalType;
  }

  public Integer getDepth()
  {
    return depth;
  }

  @Override
  public int compareTo(LocationColumn o)
  {
    return this.depth.compareTo(o.getDepth());
  }
}
