package net.geoprism.registry.service.request;

import org.commongeoregistry.adapter.RegistryAdapter;
import org.commongeoregistry.adapter.RegistryAdapterServer;
import org.commongeoregistry.adapter.metadata.MetadataCache;
import org.springframework.stereotype.Service;

import net.geoprism.registry.cache.ServerOrganizationCache;

@Service
public class CacheProvider implements CacheProviderIF
{
  private RegistryIdService       idService;

  private RegistryAdapter         adapter;

  private ServerOrganizationCache metadataCache;

  public CacheProvider()
  {
    this.idService = new RegistryIdService();

    this.adapter = new RegistryAdapterServer(this.idService);

    this.metadataCache = new ServerOrganizationCache(this.adapter);
    this.metadataCache.rebuild();
  }

  @Override
  public MetadataCache getAdapterCache()
  {
    return this.adapter.getMetadataCache();
  }

  @Override
  public ServerOrganizationCache getServerCache()
  {
    return this.metadataCache;
  }

}
