package com.runwayskd.geodashboard.etl;

public interface TargetDefinitionIF
{
  public String getTargetType();

  public String getSourceType();

  public TargetFieldIF getFieldByName(String name);

  public TargetFieldIF getFieldByLabel(String label);
}
