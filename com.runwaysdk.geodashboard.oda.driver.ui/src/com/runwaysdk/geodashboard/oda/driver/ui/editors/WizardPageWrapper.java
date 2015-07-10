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
