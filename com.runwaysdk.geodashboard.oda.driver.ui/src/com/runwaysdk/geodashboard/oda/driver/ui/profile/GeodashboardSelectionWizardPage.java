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
package com.runwaysdk.geodashboard.oda.driver.ui.profile;

import java.util.Properties;

import org.eclipse.datatools.connectivity.oda.design.ui.wizards.DataSourceWizardPage;
import org.eclipse.swt.widgets.Composite;

public class GeodashboardSelectionWizardPage extends DataSourceWizardPage
{
  /**
   * helper object
   */
  private GeodashboardSelectionPageHelper pageHelper;

  private Properties                      folderProperties;

  public GeodashboardSelectionWizardPage(String pageName)
  {
    super(pageName);
    // page title is specified in extension manifest
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.datatools.connectivity.oda.design.ui.profile.wizards. DataSourceWizardPage
   * #createPageCustomControl(org.eclipse.swt.widgets.Composite)
   */
  public void createPageCustomControl(Composite parent)
  {
    if (pageHelper == null)
    {
      pageHelper = new GeodashboardSelectionPageHelper(this);
    }

    pageHelper.createCustomControl(parent);
    pageHelper.initCustomControl(folderProperties); // in case init was
                                                    // called before create
    this.setPingButtonVisible(false);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.datatools.connectivity.oda.design.ui.profile.wizards.
   * DataSourceWizardPage#initPageCustomControl(java.util.Properties)
   */
  public void setInitialProperties(Properties dataSourceProps)
  {
    folderProperties = dataSourceProps;

    if (pageHelper == null)
    {
      return; // ignore, wait till createPageCustomControl to initialize
    }

    pageHelper.initCustomControl(folderProperties);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.datatools.connectivity.oda.design.ui.profile.wizards.
   * DataSourceWizardPage#collectCustomProperties()
   */
  public Properties collectCustomProperties()
  {
    if (pageHelper != null)
    {
      return pageHelper.collectCustomProperties(folderProperties);
    }

    return ( folderProperties != null ) ? folderProperties : new Properties();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.jface.dialogs.DialogPage#setVisible(boolean)
   */
  public void setVisible(boolean visible)
  {
    super.setVisible(visible);
    getControl().setFocus();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.datatools.connectivity.oda.design.ui.wizards.DataSourceWizardPage #refresh()
   */
  public void refresh()
  {
    // enable/disable all controls on page in respect of the editable session
    // state
    enableAllControls(getControl(), isSessionEditable());
    if (pageHelper != null)
    {
      pageHelper.resetTestAndMngButton();
    }
  }

}
