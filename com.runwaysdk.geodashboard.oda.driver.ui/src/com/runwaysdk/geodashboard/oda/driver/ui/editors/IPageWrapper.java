package com.runwaysdk.geodashboard.oda.driver.ui.editors;

import org.eclipse.swt.widgets.Shell;

public interface IPageWrapper
{

  public Shell getShell();

  public void setPageComplete(boolean complete);

  public void setMessage(String message);

  public void setMessage(String message, int type);

}
