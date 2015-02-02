package com.runwaysdk.geodashboard.dashboard;

import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.geodashboard.Dashboard;

/**
 * Interface for dependency injection in order to initialize the queriable types for a dashboard
 * 
 * @author terraframe
 *
 */
public interface DashboardBuilderIF extends Reloadable
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
}
