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

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.json.JSONArray;

import com.runwaysdk.business.rbac.RoleDAOIF;
import com.runwaysdk.business.rbac.UserDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.session.Session;
import com.runwaysdk.system.Roles;
import com.runwaysdk.system.RolesQuery;
import com.runwaysdk.system.SingleActor;

public class RoleView extends RoleViewBase
{
  private static final long  serialVersionUID    = -875685428;

  public static final String GEOPRISM_NAMESPACE  = "geoprism";

  public static final String ADMIN_NAMESPACE     = GEOPRISM_NAMESPACE + ".admin";

  public static final String DASHBOARD_NAMESPACE = GEOPRISM_NAMESPACE + ".dashboard";

  public RoleView()
  {
    super();
  }

  public static RoleView getView(Roles role, Set<String> roles, String groupName)
  {
    RoleView view = new RoleView();
    view.setDisplayLabel(role.getDisplayLabel().getValue());
    view.setRoleId(role.getOid());
    view.setAssigned(roles.contains(role.getOid()));
    view.setGroupName(groupName);

    return view;
  }

  @Transaction
  public static RoleView[] getRoles(GeoprismUser user)
  {
    List<RoleView> list = new LinkedList<RoleView>();
//    list.addAll(Arrays.asList(RoleView.getAdminRoles(user)));
//    list.addAll(Arrays.asList(RoleView.getDashboardRoles(user)));
    list.addAll(RoleView.getGeoprismRoleViews(user));

    return list.toArray(new RoleView[list.size()]);
  }

  public static String getCurrentRoles()
  {
    JSONArray array = new JSONArray();

    SingleActor user = GeoprismUser.getCurrentUser();

    if (user.isAppliedToDB())
    {
      Set<RoleDAOIF> roles = UserDAO.get(user.getOid()).authorizedRoles();

      for (RoleDAOIF role : roles)
      {
        array.put(role.getRoleName());
      }
    }

    return array.toString();
  }
  
  public static java.lang.String getCurrentRoleDisplayLabels()
  {
    JSONArray array = new JSONArray();

    SingleActor user = GeoprismUser.getCurrentUser();

    if (user.isAppliedToDB())
    {
//      Set<RoleDAOIF> roles = UserDAO.get(user.getOid()).authorizedRoles();
      Set<RoleDAOIF> roles = UserDAO.get(user.getOid()).assignedRoles();

      for (RoleDAOIF role : roles)
      {
        array.put(role.getDisplayLabel(Session.getCurrentLocale()));
      }
    }

    return array.toString();
  }
  
  @Transaction
  public static List<RoleView> getGeoprismRoleViews(GeoprismUser user)
  {
    List<RoleView> list = new LinkedList<RoleView>();
    List<Roles> roles = RoleView.getGeoprismRoles();
    Set<String> authorizedRoles = RoleView.getAuthorizedRoles(user);

    for (Roles role : roles)
    {
      RoleView view = RoleView.getView(role, authorizedRoles, "adminRoles");

      list.add(view);
    }

    return list;
  }

  @Transaction
  public static List<Roles> getGeoprismRoles()
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
   * Deprecated in favour of getGeoprismRoleViews
   */
//  @Transaction
//  public static RoleView[] getAdminRoles(GeoprismUser user)
//  {
//    return RoleView.getRolesViews(user, RoleView.ADMIN_NAMESPACE, "adminRoles");
//  }

  /**
   * Deprecated in favour of getGeoprismRoleViews
   */
//  @Transaction
//  public static RoleView[] getDashboardRoles(GeoprismUser user)
//  {
//    return RoleView.getRolesViews(user, RoleView.DASHBOARD_NAMESPACE, "dashboardRoles");
//  }

  /**
   * Deprecated in favour of getGeoprismRoleViews
   */
//  private static RoleView[] getRolesViews(GeoprismUser user, String namespace, String groupName)
//  {
//    Set<String> roles = RoleView.getAuthorizedRoles(user);
//    List<RoleView> list = new LinkedList<RoleView>();
//
//    RolesQuery query = new RolesQuery(new QueryFactory());
//    query.WHERE(query.getRoleName().LIKE(namespace + "%"));
//    query.ORDER_BY_ASC(query.getRoleName());
//
//    OIterator<? extends Roles> it = query.getIterator();
//
//    try
//    {
//      while (it.hasNext())
//      {
//        Roles role = it.next();
//        RoleView view = RoleView.getView(role, roles, groupName);
//
//        list.add(view);
//      }
//
//      return list.toArray(new RoleView[list.size()]);
//    }
//    finally
//    {
//      it.close();
//    }
//  }

  public static Set<String> getAuthorizedRoles(GeoprismUser user)
  {
    TreeSet<String> set = new TreeSet<String>();

    if (user != null)
    {

      if (user.isAppliedToDB())
      {
        Set<RoleDAOIF> roles = UserDAO.get(user.getOid()).authorizedRoles();

        for (RoleDAOIF role : roles)
        {
          set.add(role.getOid());
        }
      }
    }

    return set;
  }

  /**
   * Deperecated in favour of geoGeoprismRoles
   */
//  public static Roles[] getGeodashboardRoles()
//  {
//    List<Roles> list = new LinkedList<Roles>();
//
//    RolesQuery query = new RolesQuery(new QueryFactory());
//    query.WHERE(query.getRoleName().LIKE(GEOPRISM_NAMESPACE + "%"));
//    query.ORDER_BY_ASC(query.getRoleName());
//
//    OIterator<? extends Roles> it = query.getIterator();
//
//    try
//    {
//      while (it.hasNext())
//      {
//        Roles role = it.next();
//
//        list.add(role);
//      }
//
//      return list.toArray(new Roles[list.size()]);
//    }
//    finally
//    {
//      it.close();
//    }
//  }
}
