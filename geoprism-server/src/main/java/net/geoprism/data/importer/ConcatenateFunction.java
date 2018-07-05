package net.geoprism.data.importer;

import org.opengis.feature.simple.SimpleFeature;

import com.runwaysdk.generation.loader.Reloadable;

public class ConcatenateFunction implements ShapefileFunction, Reloadable
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
  public String getValue(SimpleFeature feature)
  {
    String v1 = this.f1.getValue(feature);
    String v2 = this.f2.getValue(feature);

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

}
