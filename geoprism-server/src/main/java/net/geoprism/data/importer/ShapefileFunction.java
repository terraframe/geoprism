package net.geoprism.data.importer;

import org.opengis.feature.simple.SimpleFeature;

import com.runwaysdk.generation.loader.Reloadable;

public interface ShapefileFunction extends Reloadable
{

  public String getValue(SimpleFeature feature);

}
