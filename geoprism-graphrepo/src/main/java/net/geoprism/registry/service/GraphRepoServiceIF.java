package net.geoprism.registry.service;

import org.springframework.stereotype.Component;

@Component
public interface GraphRepoServiceIF
{
  public void initialize();
  
  public void refreshMetadataCache();
}
