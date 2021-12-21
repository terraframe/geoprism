/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
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
package net.geoprism.oda.driver.ui;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import net.geoprism.oda.driver.ui.ssl.SecureKeystoreManager;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class GeoprismPlugin extends AbstractUIPlugin
{
  // The plug-in OID
  public static final String        PLUGIN_ID = "com.runwaysdk.oda.driver.ui"; //$NON-NLS-1$

  // The shared instance.
  private static GeoprismPlugin plugin;

  // Resource bundle.
  private ResourceBundle            resourceBundle;

  /**
   * The constructor.
   */
  public GeoprismPlugin()
  {
    super();

    plugin = this;

    try
    {
      resourceBundle = ResourceBundle.getBundle("net.geoprism.oda.driver.ui.nls.GeodashboardPluginResources"); //$NON-NLS-1$
    }
    catch (MissingResourceException x)
    {
      resourceBundle = null;
    }
  }

  @Override
  public void start(BundleContext context) throws Exception
  {
    super.start(context);

    /*
     * Before a connection can be attempted we must ensure that the SSLContext has been configured
     */
    SecureKeystoreManager.getInstance().configureSSLContext();
  }

  /**
   * This method is called when the plug-in is stopped
   */
  public void stop(BundleContext context) throws Exception
  {
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
  public static GeoprismPlugin getDefault()
  {
    return plugin;
  }

  /**
   * Returns the string from the plugin's resource bundle, or 'key' if not found.
   */
  public static String getResourceString(String key)
  {
    ResourceBundle bundle = GeoprismPlugin.getDefault().getResourceBundle();

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
   * Returns the string from the Resource bundle, formatted according to the arguments specified
   */
  public static String getFormattedString(String key, Object[] arguments)
  {
    return MessageFormat.format(getResourceString(key), arguments);
  }

}
