package net.geoprism.registry.service.business;

import org.springframework.stereotype.Component;

import net.geoprism.registry.model.GeoObjectTypeMetadata;

@Component
public interface GeoObjectTypeMetadataBusinessServiceIF
{

  public void apply(GeoObjectTypeMetadata gotm);

  public boolean hasPrivateParents(GeoObjectTypeMetadata gotm);

  public boolean hasPublicChildren(GeoObjectTypeMetadata gotm);

}