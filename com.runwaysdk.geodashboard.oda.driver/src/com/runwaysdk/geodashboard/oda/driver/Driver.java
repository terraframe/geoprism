/*
 * ************************************************************************
 * Copyright (c) 2013 <<Your Company Name here>>
 * 
 * ************************************************************************
 */

package com.runwaysdk.geodashboard.oda.driver;

import org.eclipse.datatools.connectivity.oda.IConnection;
import org.eclipse.datatools.connectivity.oda.IDriver;
import org.eclipse.datatools.connectivity.oda.LogConfiguration;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.connectivity.oda.util.manifest.DataTypeMapping;
import org.eclipse.datatools.connectivity.oda.util.manifest.ExtensionManifest;
import org.eclipse.datatools.connectivity.oda.util.manifest.ManifestExplorer;

/**
 * Implementation class of IDriver for an ODA runtime driver.
 */
public class Driver implements IDriver
{
  static String ODA_DATA_SOURCE_ID = "com.runwaysdk.geodashboard.oda.driver"; //$NON-NLS-1$

  /*
   * @see
   * org.eclipse.datatools.connectivity.oda.IDriver#getConnection(java.lang.
   * String)
   */
  public IConnection getConnection(String dataSourceType) throws OdaException
  {
    // Only one type
    return new Connection();
  }

  /*
   * @see
   * org.eclipse.datatools.connectivity.oda.IDriver#setLogConfiguration(org.
   * eclipse.datatools.connectivity.oda.LogConfiguration)
   */
  public void setLogConfiguration(LogConfiguration logConfig) throws OdaException
  {
    // do nothing; assumes simple driver has no logging
  }

  /*
   * @see org.eclipse.datatools.connectivity.oda.IDriver#getMaxConnections()
   */
  public int getMaxConnections() throws OdaException
  {
    return 0; // no limit
  }

  /*
   * @see
   * org.eclipse.datatools.connectivity.oda.IDriver#setAppContext(java.lang.
   * Object)
   */
  public void setAppContext(Object context) throws OdaException
  {
    // do nothing; assumes no support for pass-through context
  }

  /**
   * Returns the object that represents this extension's manifest.
   * 
   * @throws OdaException
   */
  static ExtensionManifest getManifest() throws OdaException
  {
    return ManifestExplorer.getInstance().getExtensionManifest(ODA_DATA_SOURCE_ID);
  }

  /**
   * Returns the native data type name of the specified code, as defined in this
   * data source extension's manifest.
   * 
   * @param nativeTypeCode
   *          the native data type code
   * @return corresponding native data type name
   * @throws OdaException
   *           if lookup fails
   */
  static String getNativeDataTypeName(int nativeDataTypeCode) throws OdaException
  {
    DataTypeMapping typeMapping = getManifest().getDataSetType(null).getDataTypeMapping(nativeDataTypeCode);

    if (typeMapping != null)
    {
      return typeMapping.getNativeType();
    }

    return "Non-defined";
  }

}
