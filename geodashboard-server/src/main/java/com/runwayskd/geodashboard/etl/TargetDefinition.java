package com.runwayskd.geodashboard.etl;

public class TargetDefinition implements TargetDefinitionIF
{
  private String sourceType;

  private String targetType;

  public String getSourceType()
  {
    return sourceType;
  }

  public void setSourceType(String sourceType)
  {
    this.sourceType = sourceType;
  }

  public String getTargetType()
  {
    return targetType;
  }

  public void setTargetType(String targetType)
  {
    this.targetType = targetType;
  }

  public void addField(TargetFieldIF field)
  {
    // TODO Auto-generated method stub

  }
}
