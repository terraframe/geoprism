package net.geoprism.registry.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.commongeoregistry.adapter.Optional;
import org.commongeoregistry.adapter.RegistryAdapter;

import net.geoprism.registry.model.ServerOrganization;

public class ServerOrganizationCache
{
  private Map<String, ServerOrganization> organizationMap;

  private RegistryAdapter                 adapter;

  public ServerOrganizationCache(RegistryAdapter adapter)
  {
    this.adapter = adapter;
    this.organizationMap = new ConcurrentHashMap<String, ServerOrganization>();
  }

  public RegistryAdapter getAdapter()
  {
    return this.adapter;
  }

  public Map<String, ServerOrganization> getOrganizationMap()
  {
    return organizationMap;
  }

  public void setOrganizationMap(Map<String, ServerOrganization> organizationMap)
  {
    this.organizationMap = organizationMap;
  }

  /**
   * Clears the metadata cache.
   */
  public void rebuild()
  {
    this.organizationMap = new ConcurrentHashMap<String, ServerOrganization>();
  }

  public void addOrganization(ServerOrganization organization)
  {
    this.organizationMap.put(organization.getCode(), organization);

    getAdapter().getMetadataCache().addOrganization(organization.toDTO());
  }

  public Optional<ServerOrganization> getOrganization(String code)
  {
    return Optional.of(this.organizationMap.get(code));
  }

  public List<ServerOrganization> getAllOrganizations()
  {
    // return this.organizationMap.values().toArray(new
    // Organization[this.organizationMap.values().size()]);

    return new ArrayList<ServerOrganization>(this.organizationMap.values());
  }

  public void removeOrganization(String code)
  {
    this.organizationMap.remove(code);

    getAdapter().getMetadataCache().removeOrganization(code);
  }

  public List<ServerOrganization> getAllOrganizationsTypes()
  {
    return new ArrayList<ServerOrganization>(this.organizationMap.values());
  }

  public List<String> getAllOrganizationCodes()
  {
    List<ServerOrganization> organizations = this.getAllOrganizationsTypes();

    List<String> codes = new ArrayList<String>(organizations.size());

    for (int i = 0; i < organizations.size(); ++i)
    {
      codes.add(organizations.get(i).getCode());
    }

    return codes;
  }

}
