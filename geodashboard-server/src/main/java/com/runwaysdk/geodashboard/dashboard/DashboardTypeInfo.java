package com.runwaysdk.geodashboard.dashboard;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.system.gis.geo.GeoNode;

public class DashboardTypeInfo implements Reloadable
{
  private List<String>        attributes;

  private List<String>        dashboardAttributes;

  private Integer             index;

  private Map<String, String> labels;

  private List<GeoNode>       nodes;

  public DashboardTypeInfo(Integer _index)
  {
    this.index = _index;
    this.labels = new HashMap<String, String>();
    this.nodes = new LinkedList<GeoNode>();
    this.attributes = new LinkedList<String>();
    this.dashboardAttributes = new LinkedList<String>();
  }

  public void add(String... attributeNames)
  {
    for (String attributeName : attributeNames)
    {
      this.dashboardAttributes.add(attributeName);
    }

    this.addViewOnlyAttribute(attributeNames);
  }

  public void addViewOnlyAttribute(String... attributeNames)
  {
    for (String attributeName : attributeNames)
    {
      this.attributes.add(attributeName);
    }
  }

  public Integer getIndex()
  {
    return index;
  }

  public void setIndex(Integer _index)
  {
    this.index = _index;
  }

  public List<String> getDashboardAttribute()
  {
    return dashboardAttributes;
  }

  public List<String> getAttributes()
  {
    return attributes;
  }

  public void setLabel(String _attributeName, String _label)
  {
    this.labels.put(_attributeName, _label);
  }

  public String getLabel(String _attributeName)
  {
    return this.labels.get(_attributeName);
  }

  public void addGeoNode(GeoNode node)
  {
    this.nodes.add(node);
  }

  public List<GeoNode> getNodes()
  {
    return nodes;
  }

  public boolean isDashboardAttribute(String attributeName)
  {
    return this.dashboardAttributes.contains(attributeName);
  }

}
