package net.geoprism.registry.service.permission;

import org.springframework.stereotype.Service;

import net.geoprism.registry.BusinessType;
import net.geoprism.registry.model.ServerOrganization;

@Service
public class PermissionService implements PermissionServiceIF
{
  @Override
  public boolean canRead(BusinessType type)
  {
    return false;
  }

  @Override
  public boolean canWrite(BusinessType type)
  {
    return false;
  }

  @Override
  public boolean isAdmin(ServerOrganization org)
  {
    return false;
  }

  @Override
  public boolean isMember(ServerOrganization org)
  {
    return false;
  }
  
}
