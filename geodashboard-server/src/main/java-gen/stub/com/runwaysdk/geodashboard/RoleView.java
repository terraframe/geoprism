package com.runwaysdk.geodashboard;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.runwaysdk.business.rbac.RoleDAOIF;
import com.runwaysdk.business.rbac.UserDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.system.Roles;
import com.runwaysdk.system.RolesQuery;

public class RoleView extends RoleViewBase implements com.runwaysdk.generation.loader.Reloadable
{
  private static final long  serialVersionUID       = -875685428;

  public static final String GEODASHBOARD_NAMESPACE = "geodashboard";

  public static final String ADMIN_NAMESPACE        = GEODASHBOARD_NAMESPACE + ".admin";

  public static final String DASHBOARD_NAMESPACE    = GEODASHBOARD_NAMESPACE + ".dashboard";

  public RoleView()
  {
    super();
  }

  public static RoleView getView(Roles role, Set<String> roles, String groupName)
  {
    RoleView view = new RoleView();
    view.setDisplayLabel(role.getDisplayLabel().getValue());
    view.setRoleId(role.getId());
    view.setAssigned(roles.contains(role.getId()));
    view.setGroupName(groupName);

    return view;
  }

  @Transaction
  public static RoleView[] getRoles(GeodashboardUser user)
  {
    List<RoleView> list = new LinkedList<RoleView>();
    list.addAll(Arrays.asList(RoleView.getAdminRoles(user)));
    list.addAll(Arrays.asList(RoleView.getDashboardRoles(user)));

    return list.toArray(new RoleView[list.size()]);
  }

  @Transaction
  public static RoleView[] getAdminRoles(GeodashboardUser user)
  {
    return RoleView.getRolesViews(user, RoleView.ADMIN_NAMESPACE, "adminRoles");
  }

  @Transaction
  public static RoleView[] getDashboardRoles(GeodashboardUser user)
  {
    return RoleView.getRolesViews(user, RoleView.DASHBOARD_NAMESPACE, "dashboardRoles");
  }

  private static RoleView[] getRolesViews(GeodashboardUser user, String namespace, String groupName)
  {
    Set<String> roles = RoleView.getAuthorizedRoles(user);
    List<RoleView> list = new LinkedList<RoleView>();

    RolesQuery query = new RolesQuery(new QueryFactory());
    query.WHERE(query.getRoleName().LIKE(namespace + "%"));
    query.ORDER_BY_ASC(query.getRoleName());

    OIterator<? extends Roles> it = query.getIterator();

    try
    {
      while (it.hasNext())
      {
        Roles role = it.next();
        RoleView view = RoleView.getView(role, roles, groupName);

        list.add(view);
      }

      return list.toArray(new RoleView[list.size()]);
    }
    finally
    {
      it.close();
    }
  }

  public static Set<String> getAuthorizedRoles(GeodashboardUser user)
  {
    TreeSet<String> set = new TreeSet<String>();

    if (user.isAppliedToDB())
    {
      Set<RoleDAOIF> roles = UserDAO.get(user.getId()).authorizedRoles();

      for (RoleDAOIF role : roles)
      {
        set.add(role.getId());
      }
    }

    return set;
  }

  public static Roles[] getGeodashboardRoles()
  {
    List<Roles> list = new LinkedList<Roles>();

    RolesQuery query = new RolesQuery(new QueryFactory());
    query.WHERE(query.getRoleName().LIKE(GEODASHBOARD_NAMESPACE + "%"));
    query.ORDER_BY_ASC(query.getRoleName());

    OIterator<? extends Roles> it = query.getIterator();

    try
    {
      while (it.hasNext())
      {
        Roles role = it.next();

        list.add(role);
      }

      return list.toArray(new Roles[list.size()]);
    }
    finally
    {
      it.close();
    }
  }
}
