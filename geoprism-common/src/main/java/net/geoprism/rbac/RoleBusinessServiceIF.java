package net.geoprism.rbac;

import java.util.List;
import java.util.Set;

import com.runwaysdk.system.Roles;
import com.runwaysdk.system.SingleActor;

public interface RoleBusinessServiceIF
{
  public Set<Roles> getUserRoles(String oid, boolean includeInherited);
  
  public String getCurrentRoleNames(boolean includeInherited);
  
  public String getCurrentRoleDisplayLabels(boolean includeInherited);
  
  public List<Roles> getAllAssignableRoles();
  
  public void assignRoles(SingleActor user, Set<String> roleIds);
  
  public void addDefaultRole(Set<String> roleIds);
}
