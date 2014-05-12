package com.runwaysdk.geodashboard;

import java.util.Arrays;
import java.util.List;

import com.runwaysdk.business.rbac.RoleDAO;
import com.runwaysdk.business.rbac.UserDAO;
import com.runwaysdk.business.rbac.UserDAOIF;
import com.runwaysdk.dataaccess.transaction.Transaction;
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
    this.appLock();
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
    List<String> list = Arrays.asList(roleIds);

    UserDAOIF user = UserDAO.get(this.getId());

    /*
     * Assign roles
     */
    for (Roles role : roles)
    {
      RoleDAO roleDAO = RoleDAO.get(role.getId()).getBusinessDAO();

      if (list.contains(role.getId()))
      {
        roleDAO.assignMember(user);
      }
      else
      {
        roleDAO.deassignMember(user);
      }
    }
  }

  public static GeodashboardUser getCurrentUser()
  {
    return GeodashboardUser.get(Session.getCurrentSession().getUser().getId());
  }

}
