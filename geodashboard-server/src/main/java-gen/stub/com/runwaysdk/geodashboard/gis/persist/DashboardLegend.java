package com.runwaysdk.geodashboard.gis.persist;

public class DashboardLegend extends DashboardLegendBase implements com.runwaysdk.generation.loader.Reloadable
{
  private static final long serialVersionUID = 1447016216;

  public DashboardLegend()
  {
    super();
  }

  public DashboardLegend(com.runwaysdk.business.MutableWithStructs entity, String structName)
  {
    super(entity, structName);
  }

  public void populate(DashboardLegend source)
  {
    this.setGroupedInLegend(source.getGroupedInLegend());
    this.setLegendXPosition(source.getLegendXPosition());
    this.setLegendYPosition(source.getLegendYPosition());
  }

}
