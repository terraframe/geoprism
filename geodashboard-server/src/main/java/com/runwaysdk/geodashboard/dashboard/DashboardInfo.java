package com.runwaysdk.geodashboard.dashboard;

import java.util.HashMap;
import java.util.Map;

import com.runwaysdk.generation.loader.Reloadable;

public class DashboardInfo implements Reloadable
{
  private String[]            attributes;

  private Integer             index;

  private Map<String, String> labels;

  public DashboardInfo(String[] _attributes, Integer _index)
  {
    this.attributes = _attributes;
    this.index = _index;
    this.labels = new HashMap<String, String>();
  }

  public Integer getIndex()
  {
    return index;
  }

  public void setIndex(Integer _index)
  {
    this.index = _index;
  }

  public String[] getAttributes()
  {
    return attributes;
  }

  public void setAttributes(String[] _attributes)
  {
    this.attributes = _attributes;
  }

  public void setLabel(String _attributeName, String _label)
  {
    this.labels.put(_attributeName, _label);
  }

  public String getLabel(String _attributeName)
  {
    return this.labels.get(_attributeName);
  }

}