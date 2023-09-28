package net.geoprism.registry.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.geoprism.registry.business.GeoObjectTypeBusinessServiceIF;

@Component
public class GeoObjectTypeService implements GeoObjectTypeServiceIF
{
  @Autowired
  private GeoObjectTypeBusinessServiceIF service;
  
  
}
