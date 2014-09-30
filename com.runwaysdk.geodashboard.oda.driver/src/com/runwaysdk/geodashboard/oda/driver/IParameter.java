package com.runwaysdk.geodashboard.oda.driver;

public interface IParameter
{
  public int getParameterType();

  public int getPrecision();

  public int getScale();

  public int isNullable();

  public String getParameterName();

  public int getParameterMode();
}
