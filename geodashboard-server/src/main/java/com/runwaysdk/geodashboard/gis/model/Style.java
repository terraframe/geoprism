package com.runwaysdk.geodashboard.gis.model;


public interface Style extends Component
{
  
  public String getName();
  
  public void setName(String name);
  
  public Integer getPointSize();
  
  public String getPointStroke();

  public Integer getPointStrokeWidth();

  public String getPointWellKnownName();

  public String getPolygonFill();

  public String getPolygonStroke();

  public Integer getPolygonStrokeWidth();
  
  public void setPointSize(Integer pointSize);
  
  public void setPointStroke(String pointStroke);
  
  public void setPointStrokeWidth(Integer pointStrokeWidth);
  
  public void setPointWellKnownName(String pointWellKnownName);
  
  public void setPolygonFill(String polygonFill);
  
  public void setPolygonStroke(String polygonStroke);
  
  public void setPolygonStrokeWidth(Integer polygonStrokeWidth);

  public void setPointFill(String fill);
  
  public String getPointFill();

  public void setPointOpacity(Double opacity);
  
  public Double getPointOpacity();

  public void setPointRotation(Integer rotation);
  
  public Integer getPointRotation();

}
