package net.geoprism.data.importer;

import org.opengis.feature.simple.SimpleFeature;

public class SimpleFeatureRow implements FeatureRow
{
  private SimpleFeature feature;

  public SimpleFeatureRow(SimpleFeature feature)
  {
    this.feature = feature;
  }

  public SimpleFeature getFeature()
  {
    return feature;
  }

  @Override
  public Object getValue(String attributeName)
  {
    return this.feature.getAttribute(attributeName);
  }
}
