package net.geoprism.rbac;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.springframework.stereotype.Component;

import com.runwaysdk.business.rbac.RoleDAO;
import com.runwaysdk.business.rbac.RoleDAOIF;
import com.runwaysdk.business.rbac.SingleActorDAO;
import com.runwaysdk.business.rbac.SingleActorDAOIF;
import com.runwaysdk.business.rbac.UserDAO;
import com.runwaysdk.business.rbac.UserDAOIF;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.session.Request;
import com.runwaysdk.session.RequestType;
import com.runwaysdk.session.Session;
import com.runwaysdk.session.SessionIF;
import com.runwaysdk.system.Roles;
import com.runwaysdk.system.RolesQuery;
import com.runwaysdk.system.SingleActor;

import net.geoprism.GeoprismUser;

@Component
public class RoleBusinessService implements RoleBusinessServiceIF
{
  // TODO: Don't autowire components here without removing direct instantiation
  
  @Request(RequestType.SESSION)
  public static String getCurrentRoleDisplayLabels(String sessionId)
  {
    return new RoleBusinessService().getCurrentRoleDisplayLabels(true);
  }
  
  @Request(RequestType.SESSION)
  public static String getCurrentRoles(String sessionId)
  {
    return new RoleBusinessService().getCurrentRoleNames(true);
  }
  
  public static SingleActor getCurrentUser()
  {
    SessionIF session = Session.getCurrentSession();

    if (session != null)
    {
      SingleActorDAOIF user = session.getUser();
      
      return SingleActor.get(user.getOid());
    }

    return null;
  }
  
  public static SingleActorDAOIF getCurrentUserDAO()
  {
    SessionIF session = Session.getCurrentSession();

    if (session != null)
    {
      SingleActorDAOIF user = session.getUser();
      
      return user;
    }

    return null;
  }
  
  @Override
  public Set<Roles> getUserRoles(String oid, boolean includeInherited)
  {
    SingleActorDAOIF user = SingleActorDAO.get(oid);

    if (user.isAppliedToDB())
    {
      if (includeInherited)
      {
        return user.authorizedRoles().stream().map(role -> Roles.get(role.getOid())).collect(Collectors.toCollection(HashSet::new));
      }
      else
      {
        return user.assignedRoles().stream().map(role -> Roles.get(role.getOid())).collect(Collectors.toCollection(HashSet::new));
      }
    }
    
    return new HashSet<Roles>();
  }
  
  /**
   * Returns the role names of all roles which are assigned to the current user, serialized as a json array. Inherited roles can optionally be included.
   */
  @Override
  public String getCurrentRoleNames(boolean includeInherited)
  {
    JSONArray array = new JSONArray();

    if (Session.getCurrentSession() != null && Session.getCurrentSession().getUser() != null)
    {
      this.getUserRoles(Session.getCurrentSession().getUser().getOid(), includeInherited).stream().forEach(role -> array.put(role.getRoleName()));
    }

    return array.toString();
  }
  
  /**
   * Returns the display labels of all roles which are assigned to the current user, serialized as a json array. Inherited roles can optionally be included.
   */
  @Override
  public String getCurrentRoleDisplayLabels(boolean includeInherited)
  {
    JSONArray array = new JSONArray();

    if (Session.getCurrentSession() != null && Session.getCurrentSession().getUser() != null)
    {
      this.getUserRoles(Session.getCurrentSession().getUser().getOid(), includeInherited).stream().forEach(role -> array.put(role.getDisplayLabel()));
    }

    return array.toString();
  }
  
  @Override
  public List<Roles> getAllAssignableRoles()
  {
    String[] excludedRoles = new String[] { "AdminScreenAccess", "Administrator", "Developer", "OWNER", "PUBLIC", "RoleAdministrator", "geoprism.DecisionMaker" };

    List<Roles> list = new LinkedList<Roles>();

    RolesQuery query = new RolesQuery(new QueryFactory());
    for (String excludedRole : excludedRoles)
    {
      query.WHERE(query.getRoleName().NLIKE(excludedRole + "%"));
    }
    query.ORDER_BY_ASC(query.getRoleName());

    OIterator<? extends Roles> it = query.getIterator();

    try
    {
      while (it.hasNext())
      {
        Roles role = it.next();

        list.add(role);
      }

      return list;
    }
    finally
    {
      it.close();
    }
  }
  
  /**
   * Check to make sure that the roleIds, if empty, at least contains a default role. 
   * 
   * @param roleIds
   */
  @Override
  public void addDefaultRole(Set<String> roleIds)
  {
    RoleDAOIF admin = RoleDAO.findRole(RoleConstants.ADMIN);
    RoleDAOIF builder = RoleDAO.findRole(RoleConstants.DASHBOARD_BUILDER);

    if (! ( roleIds.contains(admin.getOid()) || roleIds.contains(builder.getOid()) ))
    {
      RoleDAOIF role = RoleDAO.findRole(RoleConstants.DASHBOARD_BUILDER);

      roleIds.add(role.getOid());
    }
  }
  
  @Override
  public void assignRoles(SingleActor user, Set<String> roleIds)
  {
    if (roleIds != null)
    {
      List<Roles> roles = this.getAllAssignableRoles();
      Set<String> set = new HashSet<String>(roleIds);
  
      SingleActorDAOIF userDAO = SingleActorDAO.get(user.getOid());
      
      this.addDefaultRole(set);
  
      /*
       * Assign roles
       */
      for (Roles role : roles)
      {
        RoleDAO roleDAO = RoleDAO.get(role.getOid()).getBusinessDAO();
  
        if (set.contains(role.getOid()))
        {
          roleDAO.assignMember(userDAO);
        }
        else
        {
          roleDAO.deassignMember(userDAO);
        }
      }
    }
  }
}
