package com.runwaysdk.geodashboard.dashboard;

import java.util.Collection;

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
}
