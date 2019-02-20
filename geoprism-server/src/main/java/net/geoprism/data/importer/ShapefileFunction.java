package net.geoprism.data.importer;

public interface ShapefileFunction
{

  public Object getValue(FeatureRow feature);

  public String toJson();

}
