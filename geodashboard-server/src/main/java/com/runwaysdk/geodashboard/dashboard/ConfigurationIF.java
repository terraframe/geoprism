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
package com.runwaysdk.geodashboard.dashboard;

import java.util.Collection;
import java.util.Set;

import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.geodashboard.Dashboard;

/**
 * Interface for dependency injection for configuring the system
 * 
 * @author terraframe
 *
 */
public interface ConfigurationIF extends Reloadable
{
  /**
   * Initialize a dashboard with views of its default types
   * 
   * @param _dashboard
   *          The dashboard being initialized
   * @param _index
   *          The index of the builder
   */
  public void initialize(Dashboard _dashboard, int _index);

  /**
   * Returns the fully qualified package for types to include in the databrowser
   * 
   * @return
   */
  public Collection<String> getDatabrowserPackages();

  /**
   * Returns the fully qualified types to include in the databrowser
   * 
   * @return
   */
  public Collection<String> getDatabrowserTypes();

  /**
   * Filter the user roles. This method is called when a user is applied. It can be used to remove or add roles on roles
   * assignment.
   * 
   * @param roleIds
   */
  public void configureUserRoles(Set<String> roleIds);

  /**
   * Check is the user has access to some system functionality
   * 
   * @param functionality
   * @return
   */
  public boolean hasAccess(String functionality);
}
