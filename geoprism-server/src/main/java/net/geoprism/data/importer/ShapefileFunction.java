package net.geoprism.data.importer;

import org.opengis.feature.simple.SimpleFeature;



public interface ShapefileFunction 
{

  public String getValue(SimpleFeature feature);

}
