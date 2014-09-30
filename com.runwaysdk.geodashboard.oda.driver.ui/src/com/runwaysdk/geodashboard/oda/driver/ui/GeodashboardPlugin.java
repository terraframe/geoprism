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
