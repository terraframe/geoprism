package net.geoprism.graphrepo.permission;

import java.util.Set;

import org.springframework.stereotype.Component;

import net.geoprism.graphrepo.permission.UserPermissionService.RepoPermissionAction;
import net.geoprism.registry.graph.transition.TransitionEvent;

@Component
public interface TransitionPermissionServiceIF
{
  public Set<RepoPermissionAction> getPermissions(TransitionEvent event);
}
