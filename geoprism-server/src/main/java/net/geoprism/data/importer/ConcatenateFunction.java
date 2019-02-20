package net.geoprism.data.importer;

public class ConcatenateFunction implements ShapefileFunction
{
  private ShapefileFunction f1;

  private ShapefileFunction f2;

  public ConcatenateFunction(ShapefileFunction f1, ShapefileFunction f2)
  {
    super();
    this.f1 = f1;
    this.f2 = f2;
  }


  @Override
  public Object getValue(FeatureRow feature)
  {
    String v1 = (String) this.f1.getValue(feature);
    String v2 = (String) this.f2.getValue(feature);

    if (v1 != null && v2 != null)
    {
      return v1 + v2;
    }
    else if (v1 != null)
    {
      return v1;
    }

    return v2;
  }

  @Override
  public String toJson()
  {
    throw new UnsupportedOperationException();
  }
}
