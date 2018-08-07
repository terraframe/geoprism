package net.geoprism.data.importer;

import org.opengis.feature.simple.SimpleFeature;



public class ConstantFunction implements ShapefileFunction
{
  private String constant;

  public ConstantFunction(String constant)
  {
    super();
    this.constant = constant;
  }

  @Override
  public String getValue(SimpleFeature feature)
  {
    return this.constant;
  }

}
