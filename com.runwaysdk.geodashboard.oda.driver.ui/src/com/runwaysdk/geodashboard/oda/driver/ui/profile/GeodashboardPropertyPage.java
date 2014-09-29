package com.runwaysdk.geodashboard.oda.driver.ui.profile;

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
