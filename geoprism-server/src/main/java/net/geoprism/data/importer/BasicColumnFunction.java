package net.geoprism.data.importer;

public class BasicColumnFunction implements ShapefileFunction
{

  private String attributeName;

  public BasicColumnFunction(String attributeName)
  {
    this.attributeName = attributeName;
  }

  @Override
  public Object getValue(FeatureRow feature)
  {
    Object value = feature.getValue(this.attributeName);

    if (value != null)
    {
      return value;
    }

    return null;
  }

  @Override
  public String toJson()
  {
    return this.attributeName;
  }
}
