package net.geoprism.rbac;

import java.util.List;
import java.util.Set;

import com.runwaysdk.system.Roles;

public interface RoleServiceIF
{
  /**
   * Returns all roles assigned to the user with the provided sessionId. Inherited roles can optionally be included.
   */
  public Set<RoleView> getCurrentRoles(String sessionId, boolean includeInherited);
  
  public Set<RoleView> getUserRoles(String sessionId, String userOid, boolean includeInherited);
  
  /**
   * Returns the role names of all roles which are assigned to the current user, serialized as a json array. Inherited roles can optionally be included.
   */
  public String getCurrentRoleNames(String sessionId, boolean includeInherited);
  
  /**
   * Returns the display labels of all roles which are assigned to the current user, serialized as a json array. Inherited roles can optionally be included.
   */
  public String getCurrentRoleDisplayLabels(String sessionId, boolean includeInherited);
  
  /**
   * Gets all assignable roles for the user logged in with the given sessionId.
   * 
   * @param sessionId
   * @return
   */
  public List<RoleView> getAllAssignableRoles(String sessionId);
  
  /**
   * Gets all assignable roles for the user with the given oid.
   * 
   * @param sessionId
   * @return
   */
  public List<RoleView> getAllAssignableRoles(String sessionId, String userOid);
}
