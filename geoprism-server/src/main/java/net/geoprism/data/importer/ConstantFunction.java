package net.geoprism.data.importer;

import org.opengis.feature.simple.SimpleFeature;

import com.runwaysdk.generation.loader.Reloadable;

public class ConstantFunction implements ShapefileFunction, Reloadable
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
