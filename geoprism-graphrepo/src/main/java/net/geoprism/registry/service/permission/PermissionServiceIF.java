package net.geoprism.registry.service.permission;

import org.springframework.stereotype.Component;

import net.geoprism.registry.BusinessType;
import net.geoprism.registry.model.ServerOrganization;

@Component
public interface PermissionServiceIF
{
  boolean canWrite(BusinessType type);

  boolean canRead(BusinessType type);

  boolean isAdmin(ServerOrganization org);

  boolean isMember(ServerOrganization org);


}
