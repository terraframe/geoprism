package com.runwayskd.geodashboard.etl;

public abstract class TargetField implements TargetFieldIF
{
  private String name;

  private String label;

  private String key;

  @Override
  public String getKey()
  {
    return this.key;
  }

  public void setKey(String key)
  {
    this.key = key;
  }

  @Override
  public String getLabel()
  {
    return this.label;
  }

  public void setLabel(String label)
  {
    this.label = label;
  }

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
