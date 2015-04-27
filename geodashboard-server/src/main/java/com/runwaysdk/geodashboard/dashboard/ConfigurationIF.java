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
