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
package net.geoprism.oda.driver.ui.profile;

import java.util.Properties;

import org.eclipse.datatools.connectivity.oda.design.ui.wizards.DataSourceEditorPage;
import org.eclipse.swt.widgets.Composite;

public class GeodashboardPropertyPage extends DataSourceEditorPage
{
  private GeodashboardSelectionPageHelper m_pageHelper;

  public GeodashboardPropertyPage()
  {
    super();
  }

  @Override
  public Properties collectCustomProperties(Properties profileProps)
  {
    if (m_pageHelper == null)
    {
      return profileProps;
    }

    return m_pageHelper.collectCustomProperties(profileProps);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.datatools.connectivity.oda.design.ui.profile.wizards.
   * DataSourceEditorPage
   * #createAndInitCustomControl(org.eclipse.swt.widgets.Composite,
   * java.util.Properties)
   */
  protected void createAndInitCustomControl(Composite parent, Properties profileProps)
  {
    if (m_pageHelper == null)
    {
      m_pageHelper = new GeodashboardSelectionPageHelper(this);
    }

    m_pageHelper.createCustomControl(parent);

    this.setPingButtonVisible(false);

    m_pageHelper.initCustomControl(profileProps);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.datatools.connectivity.oda.design.internal.ui.
   * DataSourceEditorPageCore#refresh()
   */
  public void refresh(Properties customConnectionProps)
  {
    m_pageHelper.initCustomControl(customConnectionProps);

    // enable/disable all controls on page in respect of the editable session
    // state
    enableAllControls(getControl(), isSessionEditable());

    m_pageHelper.resetTestAndMngButton();
  }
}
