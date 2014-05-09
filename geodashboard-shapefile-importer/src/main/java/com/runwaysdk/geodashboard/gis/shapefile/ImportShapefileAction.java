package com.runwaysdk.geodashboard.gis.shapefile;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.geodashboard.gis.LocalizedWizardDialog;
import com.runwaysdk.geodashboard.gis.Localizer;
import com.runwaysdk.system.gis.geo.Universal;

public class ImportShapefileAction extends Action implements Reloadable
{
  private Universal[] universal;

  public ImportShapefileAction(Universal[] universal)
  {
    this.universal = universal;

    this.setText(Localizer.getMessage("IMPORT_SHAPE_FILE"));
  }

  @Override
  public void run()
  {
    Shell shell = Display.getCurrent().getActiveShell();
    ShapeFileWizard wizard = new ShapeFileWizard(universal);

    LocalizedWizardDialog dialog = new LocalizedWizardDialog(shell, wizard);
    dialog.setBlockOnOpen(true);
    dialog.open();
  }
}
