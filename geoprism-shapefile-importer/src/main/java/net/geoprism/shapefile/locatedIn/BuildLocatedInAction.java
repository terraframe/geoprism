/**
 * Copyright (c) 2023 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Geoprism(tm).
 *
 * Geoprism(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Geoprism(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Geoprism(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism.shapefile.locatedIn;

import net.geoprism.shapefile.LocalizedWizardDialog;
import net.geoprism.shapefile.Localizer;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;



public class BuildLocatedInAction extends Action 
{
  public BuildLocatedInAction()
  {
    this.setText(Localizer.getMessage("BUILD_LOCATED_IN"));
  }

  @Override
  public void run()
  {
    Shell shell = Display.getCurrent().getActiveShell();
    LocatedInWizard wizard = new LocatedInWizard();

    LocalizedWizardDialog dialog = new LocalizedWizardDialog(shell, wizard);
    dialog.setBlockOnOpen(true);
    dialog.open();
  }
}
