
package net.geoprism.rbac;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.runwaysdk.session.Request;
import com.runwaysdk.session.RequestType;
import com.runwaysdk.session.Session;

@Component
public class RoleService implements RoleServiceIF
{
  @Autowired
  protected RoleBusinessServiceIF service;
  
  /**
   * Returns all roles assigned to the user with the provided sessionId. Inherited roles can optionally be included.
   */
  @Request(RequestType.SESSION)
  @Override
  public Set<RoleView> getCurrentRoles(String sessionId, boolean includeInherited)
  {
    return this.service.getUserRoles(Session.getCurrentSession().getUser().getOid(), includeInherited).stream().map(role -> new RoleView(role, true)).collect(Collectors.toCollection(TreeSet::new));
  }
  
  /**
   * Returns all roles assigned to the user with the provided oid. Inherited roles can optionally be included.
   */
  @Request(RequestType.SESSION)
  @Override
  public Set<RoleView> getUserRoles(String sessionId, String userOid, boolean includeInherited)
  {
    return this.service.getUserRoles(userOid, includeInherited).stream().map(role -> new RoleView(role, true)).collect(Collectors.toCollection(TreeSet::new));
  }
  
  /**
   * Returns the role names of all roles which are assigned to the current user, serialized as a json array. Inherited roles can optionally be included.
   */
  @Request(RequestType.SESSION)
  @Override
  public String getCurrentRoleNames(String sessionId, boolean includeInherited)
  {
    return this.service.getCurrentRoleNames(includeInherited);
  }
  
  /**
   * Returns the display labels of all roles which are assigned to the current user, serialized as a json array. Inherited roles can optionally be included.
   */
  @Request(RequestType.SESSION)
  @Override
  public String getCurrentRoleDisplayLabels(String sessionId, boolean includeInherited)
  {
    return this.service.getCurrentRoleDisplayLabels(includeInherited);
  }
  
  /**
   * Used by IDM. Returns all Geoprism roles and calculates whether or not they are assigned to the current user.
   */
  @Request(RequestType.SESSION)
  @Override
  public List<RoleView> getAllAssignableRoles(String sessionId)
  {
    Set<String> authorizedRoles = this.getCurrentRoles(sessionId, true).stream().map(role -> role.getOid()).collect(Collectors.toSet());
    
    return this.service.getAllAssignableRoles().stream().map(role -> new RoleView(role, authorizedRoles.contains(role.getOid()))).collect(Collectors.toList());
  }
  
  @Request(RequestType.SESSION)
  @Override
  public List<RoleView> getAllAssignableRoles(String sessionId, String userOid)
  {
    Set<String> authorizedRoles = this.getUserRoles(sessionId, userOid, true).stream().map(role -> role.getOid()).collect(Collectors.toSet());
    
    return this.service.getAllAssignableRoles().stream().map(role -> new RoleView(role, authorizedRoles.contains(role.getOid()))).collect(Collectors.toList());
  }
}
