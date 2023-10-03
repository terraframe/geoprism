package net.geoprism.graphrepo.permission;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import net.geoprism.graphrepo.permission.UserPermissionService.RepoPermissionAction;
import net.geoprism.registry.graph.transition.TransitionEvent;

public class AllowAllTransitionPermissionService implements TransitionPermissionServiceIF
{

  @Override
  public Set<RepoPermissionAction> getPermissions(TransitionEvent event)
  {
    return new HashSet<RepoPermissionAction>(Arrays.asList(RepoPermissionAction.values()));
  }
  
}
