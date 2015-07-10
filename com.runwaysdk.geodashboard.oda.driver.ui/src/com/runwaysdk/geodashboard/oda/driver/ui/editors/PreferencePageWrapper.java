package com.runwaysdk.geodashboard.oda.driver.ui.editors;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.widgets.Shell;

public class PreferencePageWrapper implements IPageWrapper
{
  private PreferencePage page;

  public PreferencePageWrapper(PreferencePage page)
  {
    this.page = page;
  }

  @Override
  public Shell getShell()
  {
    return this.page.getShell();
  }

  @Override
  public void setPageComplete(boolean complete)
  {
    this.page.setValid(complete);
  }

  @Override
  public void setMessage(String message)
  {
    this.page.setMessage(message);
  }

  @Override
  public void setMessage(String message, int type)
  {
    this.page.setMessage(message, type);
  }

}
