package com.runwayskd.geodashboard.etl;

import java.util.HashMap;
import java.util.Map;

public class TargetDefinition implements TargetDefinitionIF
{
  private String                         sourceType;

  private String                         targetType;

  private Map<String, TargetFieldIF>     fieldMap;

  private HashMap<String, TargetFieldIF> labelMap;

  public TargetDefinition()
  {
    this.fieldMap = new HashMap<String, TargetFieldIF>();
    this.labelMap = new HashMap<String, TargetFieldIF>();
  }

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
    this.fieldMap.put(field.getName(), field);
    this.labelMap.put(field.getLabel(), field);
  }

  @Override
  public TargetFieldIF getFieldByName(String name)
  {
    return this.fieldMap.get(name);
  }

  @Override
  public TargetFieldIF getFieldByLabel(String label)
  {
    return this.labelMap.get(label);
  }
}
