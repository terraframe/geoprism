package net.geoprism.registry.service.permission;

import java.util.Set;

import org.springframework.stereotype.Component;

import net.geoprism.registry.graph.transition.TransitionEvent;
import net.geoprism.registry.service.permission.UserPermissionService.RepoPermissionAction;

@Component
public interface TransitionPermissionServiceIF
{
  public Set<RepoPermissionAction> getPermissions(TransitionEvent event);
}
