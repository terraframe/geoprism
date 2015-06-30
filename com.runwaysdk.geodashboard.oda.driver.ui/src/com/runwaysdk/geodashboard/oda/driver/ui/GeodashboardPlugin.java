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
package com.runwaysdk.geodashboard.oda.driver.ui;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class GeodashboardPlugin extends AbstractUIPlugin
{
  // The plug-in ID
  public static final String        PLUGIN_ID = "com.runwaysdk.oda.driver.ui"; //$NON-NLS-1$

  // The shared instance.
  private static GeodashboardPlugin plugin;

  // Resource bundle.
  private ResourceBundle            resourceBundle;

  /**
   * The constructor.
   */
  public GeodashboardPlugin()
  {
    super();

    plugin = this;

    try
    {
      resourceBundle = ResourceBundle.getBundle("com.runwaysdk.geodashboard.oda.driver.ui.nls.GeodashboardPluginResources");
    }
    catch (MissingResourceException x)
    {
      resourceBundle = null;
    }
  }

  /**
   * This method is called when the plug-in is stopped
   */
  public void stop(BundleContext context) throws Exception
  {
    // ConnectionMetaDataManager.getInstance().clearCache();

    super.stop(context);
  }

  /**
   * Returns the plugin's resource bundle,
   */
  public ResourceBundle getResourceBundle()
  {
    return resourceBundle;
  }

  /**
   * Returns the shared instance.
   */
  public static GeodashboardPlugin getDefault()
  {
    return plugin;
  }

  /**
   * Returns the string from the plugin's resource bundle, or 'key' if not
   * found.
   */
  public static String getResourceString(String key)
  {
    ResourceBundle bundle = GeodashboardPlugin.getDefault().getResourceBundle();

    try
    {
      return ( bundle != null ) ? bundle.getString(key) : key;
    }
    catch (MissingResourceException e)
    {
      return key;
    }
  }

  /**
   * Returns the string from the Resource bundle, formatted according to the
   * arguments specified
   */
  public static String getFormattedString(String key, Object[] arguments)
  {
    return MessageFormat.format(getResourceString(key), arguments);
  }

}
