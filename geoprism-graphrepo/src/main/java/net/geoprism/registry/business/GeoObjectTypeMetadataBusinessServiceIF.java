package net.geoprism.registry.business;

import net.geoprism.registry.model.GeoObjectTypeMetadata;

public interface GeoObjectTypeMetadataBusinessServiceIF
{

  public void apply(GeoObjectTypeMetadata gotm);

  public boolean hasPrivateParents(GeoObjectTypeMetadata gotm);

  public boolean hasPublicChildren(GeoObjectTypeMetadata gotm);

}