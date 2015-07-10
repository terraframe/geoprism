package com.runwaysdk.geodashboard.oda.driver.ui.editors;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Shell;

public class WizardPageWrapper implements IPageWrapper
{
  private WizardPage page;

  public WizardPageWrapper(WizardPage page)
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
    this.page.setPageComplete(complete);
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
