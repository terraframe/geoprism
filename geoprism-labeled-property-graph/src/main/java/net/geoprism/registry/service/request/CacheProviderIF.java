package net.geoprism.registry.service.request;

import org.commongeoregistry.adapter.metadata.MetadataCache;
import org.springframework.stereotype.Component;

import net.geoprism.registry.cache.ServerOrganizationCache;

@Component
public interface CacheProviderIF
{
  public MetadataCache getAdapterCache();

  public ServerOrganizationCache getServerCache();
}
