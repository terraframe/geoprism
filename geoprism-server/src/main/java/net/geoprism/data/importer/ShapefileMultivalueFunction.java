package net.geoprism.data.importer;

import java.util.List;

import org.opengis.feature.simple.SimpleFeature;



public interface ShapefileMultivalueFunction 
{
  public List<String> getValue(SimpleFeature feature);

}
