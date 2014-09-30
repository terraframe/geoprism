package com.runwaysdk.geodashboard.oda.driver.ui.profile;

import java.util.Properties;

import org.eclipse.datatools.connectivity.oda.design.ui.wizards.DataSourceWizardPage;
import org.eclipse.swt.widgets.Composite;

public class GeodashboardSelectionWizardPage extends DataSourceWizardPage
{
  /**
   * helper object
   */
  private GeodashboardSelectionPageHelper m_pageHelper;

  private Properties                      m_folderProperties;

  public GeodashboardSelectionWizardPage(String pageName)
  {
    super(pageName);
    // page title is specified in extension manifest
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.datatools.connectivity.oda.design.ui.profile.wizards.
   * DataSourceWizardPage
   * #createPageCustomControl(org.eclipse.swt.widgets.Composite)
   */
  public void createPageCustomControl(Composite parent)
  {
    if (m_pageHelper == null)
    {
      m_pageHelper = new GeodashboardSelectionPageHelper(this);
    }

    m_pageHelper.createCustomControl(parent);
    m_pageHelper.initCustomControl(m_folderProperties); // in case init was
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
    m_folderProperties = dataSourceProps;

    if (m_pageHelper == null)
    {
      return; // ignore, wait till createPageCustomControl to initialize
    }

    m_pageHelper.initCustomControl(m_folderProperties);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.datatools.connectivity.oda.design.ui.profile.wizards.
   * DataSourceWizardPage#collectCustomProperties()
   */
  public Properties collectCustomProperties()
  {
    if (m_pageHelper != null)
    {
      return m_pageHelper.collectCustomProperties(m_folderProperties);
    }

    return ( m_folderProperties != null ) ? m_folderProperties : new Properties();
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
   * @see
   * org.eclipse.datatools.connectivity.oda.design.ui.wizards.DataSourceWizardPage
   * #refresh()
   */
  public void refresh()
  {
    // enable/disable all controls on page in respect of the editable session
    // state
    enableAllControls(getControl(), isSessionEditable());
    if (m_pageHelper != null)
    {
      m_pageHelper.resetTestAndMngButton();
    }
  }

}
