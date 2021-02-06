/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Geoprism(tm).
 *
 * Geoprism(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Geoprism(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Geoprism(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.runwaysdk.business.rbac.RoleDAO;
import com.runwaysdk.business.rbac.SingleActorDAOIF;
import com.runwaysdk.business.rbac.UserDAO;
import com.runwaysdk.business.rbac.UserDAOIF;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.session.Session;
import com.runwaysdk.session.SessionIF;
import com.runwaysdk.system.AssignmentsQuery;
import com.runwaysdk.system.Roles;
import com.runwaysdk.system.SingleActor;
import com.runwaysdk.system.Users;

public class GeoprismUser extends GeoprismUserBase implements GeoprismActorIF
{
  private static final long serialVersionUID = 394889520;

  public GeoprismUser()
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

    List<Roles> roles = RoleView.getGeoprismRoles();
    Set<String> set = new HashSet<String>(Arrays.asList(roleIds));

    List<ConfigurationIF> configurations = ConfigurationService.getConfigurations();

    for (ConfigurationIF configuration : configurations)
    {
      configuration.configureUserRoles(set);
    }

    UserDAOIF user = UserDAO.get(this.getOid());

    /*
     * Assign roles
     */
    for (Roles role : roles)
    {
      RoleDAO roleDAO = RoleDAO.get(role.getOid()).getBusinessDAO();

      if (set.contains(role.getOid()))
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

  public static GeoprismUser getByUsername(String username)
  {
    GeoprismUserQuery query = new GeoprismUserQuery(new QueryFactory());
    query.WHERE(query.getUsername().EQ(username));

    OIterator<? extends GeoprismUser> it = query.getIterator();

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

  public static Boolean isRoleMemeber(String roles)
  {
    SessionIF session = Session.getCurrentSession();

    if (session != null)
    {
      Map<String, String> map = session.getUserRoles();
      Set<String> keySet = map.keySet();

      String[] roleNames = roles.split(",");

      for (String roleName : roleNames)
      {
        if (keySet.contains(roleName))
        {
          return true;
        }
      }
    }

    return false;
  }

  public Boolean isAssigned(Roles role)
  {
    if (role != null)
    {
      QueryFactory factory = new QueryFactory();

      AssignmentsQuery query = new AssignmentsQuery(factory);
      query.WHERE(query.getParent().EQ(this));
      query.AND(query.getChild().EQ(role));

      return ( query.getCount() > 0 );
    }

    return false;
  }

  public static GeoprismUser[] getAllUsers()
  {
    GeoprismUserQuery query = new GeoprismUserQuery(new QueryFactory());
    query.ORDER_BY_ASC(query.getUsername());

    OIterator<? extends GeoprismUser> it = query.getIterator();

    try
    {
      List<? extends GeoprismUser> list = it.getAll();

      return list.toArray(new GeoprismUser[list.size()]);
    }
    finally
    {
      it.close();
    }

  }
}
