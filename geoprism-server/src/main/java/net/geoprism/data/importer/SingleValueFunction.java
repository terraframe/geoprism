package net.geoprism.data.importer;

import java.util.LinkedList;
import java.util.List;

import org.opengis.feature.simple.SimpleFeature;

public class SingleValueFunction implements ShapefileMultivalueFunction
{

  private String attributeName;

  public SingleValueFunction(String attributeName)
  {
    this.attributeName = attributeName;
  }

  @Override
  public List<String> getValue(SimpleFeature feature)
  {
    List<String> values = new LinkedList<String>();

    Object value = feature.getAttribute(this.attributeName);

    if (value != null)
    {
      values.add(value.toString());
    }

    return values;
  }

}
