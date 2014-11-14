package com.runwaysdk.geodashboard.oda.driver.ui.provider;

public class DataSetType
{
  private String id;

  private String label;

  private int    maxDepth;

  public DataSetType(String id, String label, int maxDepth)
  {
    super();
    this.id = id;
    this.label = label;
    this.maxDepth = maxDepth;
  }

  public String getId()
  {
    return id;
  }

  public void setId(String id)
  {
    this.id = id;
  }

  public String getLabel()
  {
    return label;
  }

  public void setLabel(String label)
  {
    this.label = label;
  }

  public int getMaxDepth()
  {
    return maxDepth;
  }

  public void setMaxDepth(int maxDepth)
  {
    this.maxDepth = maxDepth;
  }

}
