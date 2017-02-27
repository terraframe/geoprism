package net.geoprism.data;

import com.runwaysdk.system.metadata.MdClass;

/**
 * Allows for IOC pluggable export utilites on MappableClass. Originally designed for the DHIS2Exporter.
 * 
 * @author rrowlands
 */
public interface GeoprismDatasetExporterIF
{
  public void xport(MdClass mdClass);
}
