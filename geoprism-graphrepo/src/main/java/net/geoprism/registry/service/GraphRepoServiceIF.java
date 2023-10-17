package net.geoprism.registry.service;

import org.springframework.stereotype.Component;

import net.geoprism.registry.model.ServerElement;

@Component
public interface GraphRepoServiceIF
{
  public void initialize();
  
  public void refreshMetadataCache();
  
  public void deleteObject(ServerElement obj);
}
