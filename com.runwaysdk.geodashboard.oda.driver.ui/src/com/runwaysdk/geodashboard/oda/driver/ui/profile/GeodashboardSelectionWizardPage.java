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
