package com.runwaysdk.geodashboard;

import com.runwaysdk.business.rbac.RoleDAO;
import com.runwaysdk.business.rbac.RoleDAOIF;
import com.runwaysdk.business.rbac.UserDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
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
    this.setOwner(Users.get(UserDAO.PUBLIC_USER_ID));
    this.apply();

    super.delete();
  }

  @Override
  @Transaction
  public void applyWithRoles(String[] roleIds)
  {
    boolean firstApply = this.isNew() && !this.isAppliedToDB();

    this.apply();

    if (firstApply)
    {
      this.setOwner(this);

      this.apply();
    }

    /*
     * Assign roles
     */
    for (String roleId : roleIds)
    {
      RoleDAOIF role = RoleDAO.get(roleId);
      role.assignMember(UserDAO.get(this.getId()));
    }
  }

}
