package net.geoprism.data.importer;

import java.util.LinkedList;
import java.util.List;

public class SingleValueAdapter implements ShapefileMultivalueFunction
{
  private ShapefileFunction function;

  public SingleValueAdapter(ShapefileFunction function)
  {
    this.function = function;
  }

  @Override
  public List<String> getValue(FeatureRow feature)
  {
    List<String> values = new LinkedList<String>();
    Object value = this.function.getValue(feature);

    if (value != null)
    {
      values.add(value.toString());
    }

    return values;
  }

}
