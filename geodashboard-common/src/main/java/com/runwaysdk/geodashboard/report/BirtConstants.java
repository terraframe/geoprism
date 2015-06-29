package com.runwaysdk.geodashboard.report;

import java.io.File;

import com.runwaysdk.constants.DeployProperties;

public interface BirtConstants
{
  public static final String CATEGORY       = "category";

  public static final String CRITERIA       = "criteria";

  public static final String AGGREGATION    = "aggregation";

  public static final String LAYER_ID       = "layerId";

  public static final String DEFAULT_GEO_ID = "defaultGeoId";

  public static final String BIRT_SUFFIX    = "imgs/birt";

  /**
   * rptdocument cache directory
   */
  public static final String CACHE_DIR      = DeployProperties.getJspDir() + File.separator + "reportcache";

  /**
   * birt temp imgs directory
   */
  public static final String IMGS_DIR       = DeployProperties.getDeployPath() + File.separator + "imgs" + File.separator + "birt";

}
