package net.geoprism.registry.service.permission;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import net.geoprism.registry.graph.transition.TransitionEvent;
import net.geoprism.registry.service.permission.UserPermissionService.RepoPermissionAction;

public class AllowAllTransitionPermissionService implements TransitionPermissionServiceIF
{

  @Override
  public Set<RepoPermissionAction> getPermissions(TransitionEvent event)
  {
    return new HashSet<RepoPermissionAction>(Arrays.asList(RepoPermissionAction.values()));
  }
  
}
