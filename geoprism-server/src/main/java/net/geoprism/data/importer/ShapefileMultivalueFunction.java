package net.geoprism.data.importer;

import java.util.List;

import org.opengis.feature.simple.SimpleFeature;

import com.runwaysdk.generation.loader.Reloadable;

public interface ShapefileMultivalueFunction extends Reloadable
{
  public List<String> getValue(SimpleFeature feature);

}
