package com.runwaysdk.geodashboard.gis.model;


public interface Style extends Component
{
  
  public String getName();
  
  public void setName(String name);
  
  // Value styles
  
  /**
   * Enable the display of the attribute value.
   * @param enable
   */
  public void setEnableValue(Boolean enable);

  public Boolean getEnableValue();
  
  public void setValueFont(String font);

  public String getValueFont();
  
  public void setValueSize(Integer size);
  
  public Integer getValueSize();
  
  public void setValueColor(String color);
  
  public String getValueColor();
  
  public void setValueHalo(String halo);
  
  public String getValueHalo();
  
  public void setValueHaloWidth(Integer width);
  
  public Integer getValueHaloWidth();
  
  // Label styles
  
  /**
   * Enable the display of the attribute label.
   * @param enable
   */
  public void setEnableLabel(Boolean enable);
  
  public Boolean getEnableLabel();
  
  public void setLabelFont(String font);

  public String getLabelFont();
  
  public void setLabelSize(Integer size);
  
  public Integer getLabelSize();
  
  public void setLabelColor(String color);
  
  public String getLabelColor();
  
  public void setLabelHalo(String halo);
  
  public String getLabelHalo();
  
  public void setLabelHaloWidth(Integer width);
  
  public Integer getLabelHaloWidth();
  
  // Point styles
  
  public Integer getPointSize();
  
  public String getPointStroke();

  public Integer getPointStrokeWidth();
  
  public Double getPointStrokeOpacity();

  public String getPointWellKnownName();

  public void setPointSize(Integer size);
  
  public void setPointStroke(String stroke);
  
  public void setPointStrokeWidth(Integer width);
  
  public void setPointWellKnownName(String pointWellKnownName);
  
  public void setPointFill(String fill);
  
  public String getPointFill();

  public void setPointOpacity(Double opacity);
  
  public Double getPointOpacity();

  public void setPointRotation(Integer rotation);
  
  public Integer getPointRotation();

  // Polygon styles
  
  public String getPolygonFill();

  public void setPolygonFill(String fill);

  public Double getPolygonFillOpacity();
  
  public void setPolygonFillOpacity(Double opacity);

  public Double getPolygonStrokeOpacity();
  
  public void setPolygonStrokeOpacity(Double opacity);
  
  public String getPolygonStroke();

  public void setPolygonStroke(String stroke);

  public Integer getPolygonStrokeWidth();
  
  public void setPolygonStrokeWidth(Integer width);
  
    
}
