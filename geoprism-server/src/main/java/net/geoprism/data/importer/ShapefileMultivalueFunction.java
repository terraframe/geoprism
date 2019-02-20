package net.geoprism.data.importer;

import java.util.List;

public interface ShapefileMultivalueFunction
{
  public List<String> getValue(FeatureRow feature);

}
