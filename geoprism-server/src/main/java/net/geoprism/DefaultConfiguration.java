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
package net.geoprism;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import net.geoprism.data.etl.TargetBuilder;

import org.apache.commons.lang.StringUtils;

import com.runwaysdk.business.rbac.Operation;
import com.runwaysdk.business.rbac.RoleDAO;
import com.runwaysdk.business.rbac.RoleDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdRelationshipDAOIF;
import com.runwaysdk.system.gis.geo.GeoEntity;

public class DefaultConfiguration extends DefaultCommonConfiguration implements ConfigurationIF
{
  private static final String ADMIN             = "geoprism.admin.Administrator";

  private static final String DASHBOARD_BUILDER = "geoprism.admin.DashboardBuilder";

  private static final String DECISION_MAKER    = "geoprism.DecisionMaker";

  @Override
  public Collection<String> getDatabrowserPackages()
  {
    List<String> packages = new LinkedList<String>();

    packages.add(TargetBuilder.PACKAGE_NAME);

    return packages;
  }

  @Override
  public Collection<String> getDatabrowserTypes()
  {
    List<String> types = new LinkedList<String>();
    types.add("com.runwaysdk.system.gis.geo.Universal");
    types.add("com.runwaysdk.system.gis.geo.GeoEntity");

    return types;
  }

  @Override
  public void configureUserRoles(Set<String> roleIds)
  {
    RoleDAOIF admin = RoleDAO.findRole(ADMIN);
    RoleDAOIF builder = RoleDAO.findRole(DASHBOARD_BUILDER);

    if (! ( roleIds.contains(admin.getId()) || roleIds.contains(builder.getId()) ))
    {
      RoleDAOIF role = RoleDAO.findRole(DECISION_MAKER);

      roleIds.add(role.getId());
    }
  }

  @Override
  public void configurePermissions(MdClassDAOIF mdClass)
  {
    this.grantAllPermissions(RoleDAO.findRole(ADMIN).getBusinessDAO(), mdClass);
    this.grantAllPermissions(RoleDAO.findRole(DASHBOARD_BUILDER).getBusinessDAO(), mdClass);
    this.grantReadPermissions(RoleDAO.findRole(DECISION_MAKER).getBusinessDAO(), mdClass);
  }

  private void grantAllPermissions(RoleDAO role, MdClassDAOIF mdClass)
  {
    role.grantPermission(Operation.CREATE, mdClass.getId());
    role.grantPermission(Operation.DELETE, mdClass.getId());
    role.grantPermission(Operation.READ, mdClass.getId());
    role.grantPermission(Operation.READ_ALL, mdClass.getId());
    role.grantPermission(Operation.WRITE, mdClass.getId());
    role.grantPermission(Operation.WRITE_ALL, mdClass.getId());

    if (mdClass instanceof MdRelationshipDAOIF)
    {
      role.grantPermission(Operation.ADD_CHILD, mdClass.getId());
      role.grantPermission(Operation.ADD_PARENT, mdClass.getId());

      role.grantPermission(Operation.DELETE_CHILD, mdClass.getId());
      role.grantPermission(Operation.DELETE_PARENT, mdClass.getId());

      role.grantPermission(Operation.READ_CHILD, mdClass.getId());
      role.grantPermission(Operation.READ_PARENT, mdClass.getId());

      role.grantPermission(Operation.WRITE_CHILD, mdClass.getId());
      role.grantPermission(Operation.WRITE_PARENT, mdClass.getId());
    }
  }

  private void grantReadPermissions(RoleDAO role, MdClassDAOIF mdClass)
  {
    role.grantPermission(Operation.READ, mdClass.getId());
    role.grantPermission(Operation.READ_ALL, mdClass.getId());

    if (mdClass instanceof MdRelationshipDAOIF)
    {
      role.grantPermission(Operation.READ_CHILD, mdClass.getId());
      role.grantPermission(Operation.READ_PARENT, mdClass.getId());
    }
  }

  @Override
  public boolean hasAccess(String functionality)
  {
    if (functionality.equals(AccessConstants.EDIT_DATA))
    {
      return false;
    }
    if (functionality.equals(AccessConstants.EDIT_DASHBOARD))
    {
      String roles = StringUtils.join(new String[] { ADMIN, DASHBOARD_BUILDER }, ",");

      return GeoprismUser.isRoleMemeber(roles);
    }
    else if (functionality.equals(AccessConstants.ADMIN))
    {
      String roles = StringUtils.join(new String[] { ADMIN }, ",");

      return GeoprismUser.isRoleMemeber(roles);
    }

    return false;
  }

  @Override
  public GeoEntity getDefaultGeoEntity()
  {
    return null;
  }
}
