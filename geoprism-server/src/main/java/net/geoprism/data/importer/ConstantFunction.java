package net.geoprism.data.importer;

public class ConstantFunction implements ShapefileFunction
{
  private String constant;

  public ConstantFunction(String constant)
  {
    super();
    this.constant = constant;
  }
  

  @Override
  public Object getValue(FeatureRow feature)
  {
    return this.constant;
  }

  @Override
  public String toJson()
  {
    throw new UnsupportedOperationException();
  }
}
