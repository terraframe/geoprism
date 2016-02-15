package com.runwayskd.geodashboard.etl;

public abstract class TargetField implements TargetFieldIF
{
  private String name;

  public void setName(String name)
  {
    this.name = name;
  }

  @Override
  public String getName()
  {
    return this.name;
  }
}
