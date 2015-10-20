/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
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
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.session.Session;
import com.runwaysdk.session.SessionIF;
import com.runwaysdk.system.AssignmentsQuery;
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

    super.delete();
  }

  @Override
  @Transaction
  public void apply()
  {
    boolean firstApply = this.isNew() && !this.isAppliedToDB();
    this.setSessionLimit(40);

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
    SessionIF session = Session.getCurrentSession();

    if (session != null)
    {
      return GeodashboardUser.get(session.getUser().getId());
    }

    return null;
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

  public Boolean isAssigned(Roles role)
  {
    QueryFactory factory = new QueryFactory();

    AssignmentsQuery query = new AssignmentsQuery(factory);
    query.WHERE(query.getParent().EQ(this));
    query.AND(query.getChild().EQ(role));

    return ( query.getCount() > 0 );
  }

  public static GeodashboardUser[] getAllUsers()
  {
    GeodashboardUserQuery query = new GeodashboardUserQuery(new QueryFactory());
    query.ORDER_BY_ASC(query.getUsername());

    OIterator<? extends GeodashboardUser> it = query.getIterator();

    try
    {
      List<? extends GeodashboardUser> list = it.getAll();

      return list.toArray(new GeodashboardUser[list.size()]);
    }
    finally
    {
      it.close();
    }

  }
}
