package com.runwaysdk.geodashboard;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.runwaysdk.business.rbac.RoleDAO;
import com.runwaysdk.business.rbac.UserDAO;
import com.runwaysdk.business.rbac.UserDAOIF;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.geodashboard.dashboard.ConfigurationIF;
import com.runwaysdk.geodashboard.dashboard.ConfigurationService;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.session.Session;
import com.runwaysdk.system.Roles;
import com.runwaysdk.system.Users;

public class GeodashboardUser extends GeodashboardUserBase implements com.runwaysdk.generation.loader.Reloadable
{
  private static final long serialVersionUID = 394889520;

  public GeodashboardUser()
  {
    super();
  }

  @Override
  @Transaction
  public void delete()
  {
    this.lock();
    this.setOwner(Users.get(UserDAO.PUBLIC_USER_ID));
    this.apply();

    SessionEntry.deleteByUser(this);

    super.delete();
  }

  @Override
  @Transaction
  public void apply()
  {
    boolean firstApply = this.isNew() && !this.isAppliedToDB();
    this.setSessionLimit(40);

    SessionEntry.deleteByUser(this);

    super.apply();

    if (firstApply)
    {
      this.appLock();
      this.setOwner(this);
      super.apply();
    }
  }

  @Override
  @Transaction
  public void applyWithRoles(String[] roleIds)
  {
    this.apply();

    Roles[] roles = RoleView.getGeodashboardRoles();
    Set<String> set = new HashSet<String>(Arrays.asList(roleIds));

    List<ConfigurationIF> configurations = ConfigurationService.getConfigurations();

    for (ConfigurationIF configuration : configurations)
    {
      configuration.configureUserRoles(set);
    }

    UserDAOIF user = UserDAO.get(this.getId());

    /*
     * Assign roles
     */
    for (Roles role : roles)
    {
      RoleDAO roleDAO = RoleDAO.get(role.getId()).getBusinessDAO();

      if (set.contains(role.getId()))
      {
        roleDAO.assignMember(user);
      }
      else
      {
        roleDAO.deassignMember(user);
      }
    }
  }

  public static Boolean hasAccess(String functionality)
  {
    boolean hasAccess = false;

    List<ConfigurationIF> configurations = ConfigurationService.getConfigurations();

    for (ConfigurationIF configuration : configurations)
    {
      hasAccess = configuration.hasAccess(functionality) || hasAccess;
    }

    return hasAccess;
  }

  public static GeodashboardUser getByUsername(String username)
  {
    GeodashboardUserQuery query = new GeodashboardUserQuery(new QueryFactory());
    query.WHERE(query.getUsername().EQ(username));

    OIterator<? extends GeodashboardUser> it = query.getIterator();

    try
    {
      if (it.hasNext())
      {
        return it.next();
      }

      // TODO Change exception type
      throw new RuntimeException("Unknown user [" + username + "]");
    }
    finally
    {
      it.close();
    }
  }

  public static GeodashboardUser getCurrentUser()
  {
    return GeodashboardUser.get(Session.getCurrentSession().getUser().getId());
  }

  public static Boolean isRoleMemeber(String roles)
  {
    Map<String, String> map = Session.getCurrentSession().getUserRoles();
    Set<String> keySet = map.keySet();

    String[] roleNames = roles.split(",");

    for (String roleName : roleNames)
    {
      if (keySet.contains(roleName))
      {
        return true;
      }
    }

    return false;
  }

}
