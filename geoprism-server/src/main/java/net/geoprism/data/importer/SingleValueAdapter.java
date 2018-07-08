package net.geoprism.data.importer;

import java.util.LinkedList;
import java.util.List;

import org.opengis.feature.simple.SimpleFeature;

public class SingleValueAdapter implements ShapefileMultivalueFunction
{
  private ShapefileFunction function;

  public SingleValueAdapter(ShapefileFunction function)
  {
    this.function = function;
  }

  @Override
  public List<String> getValue(SimpleFeature feature)
  {
    List<String> values = new LinkedList<String>();

    String value = this.function.getValue(feature);

    if (value != null)
    {
      values.add(value);
    }

    return values;
  }

}
