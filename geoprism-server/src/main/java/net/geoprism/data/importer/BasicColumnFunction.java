package net.geoprism.data.importer;

import org.opengis.feature.simple.SimpleFeature;

import com.runwaysdk.generation.loader.Reloadable;

public class BasicColumnFunction implements ShapefileFunction, Reloadable
{

  private String attributeName;

  public BasicColumnFunction(String attributeName)
  {
    this.attributeName = attributeName;
  }

  @Override
  public String getValue(SimpleFeature feature)
  {
    Object value = feature.getAttribute(this.attributeName);

    if (value != null)
    {
      return value.toString();
    }

    return null;
  }
}
