package net.geoprism.graphrepo.permission;

import java.util.Set;

import net.geoprism.graphrepo.permission.UserPermissionService.RepoPermissionAction;
import net.geoprism.registry.graph.transition.TransitionEvent;

public interface TransitionPermissionServiceIF
{
  public Set<RepoPermissionAction> getPermissions(TransitionEvent event);
}
