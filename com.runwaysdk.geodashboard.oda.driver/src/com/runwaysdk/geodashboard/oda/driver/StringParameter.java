package com.runwaysdk.geodashboard.oda.driver;

import org.eclipse.datatools.connectivity.oda.IParameterMetaData;

public class StringParameter implements IParameter
{
  private String parameterName;

  private int    isNullable;

  public StringParameter(String parameterName, int isNullable)
  {
    this.parameterName = parameterName;
    this.isNullable = isNullable;
  }

  @Override
  public int getParameterType()
  {
    return MetaDataTypeInfo.STRING_PARAMETER;
  }

  @Override
  public int getPrecision()
  {
    return -1;
  }

  @Override
  public int getScale()
  {
    return -1;
  }

  @Override
  public int isNullable()
  {
    return this.isNullable;
  }

  @Override
  public String getParameterName()
  {
    return this.parameterName;
  }

  @Override
  public int getParameterMode()
  {
    return IParameterMetaData.parameterModeIn;
  }

}
