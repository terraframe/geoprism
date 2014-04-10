package dss.vector.solutions.gis.locatedIn;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.runwaysdk.generation.loader.Reloadable;

import dss.vector.solutions.gis.LocalizedWizardDialog;
import dss.vector.solutions.gis.Localizer;

public class BuildLocatedInAction extends Action implements Reloadable
{
  private String appName;

  public BuildLocatedInAction(String appName)
  {
    this.appName = appName;

    this.setText(Localizer.getMessage("BUILD_LOCATED_IN"));
  }

  @Override
  public void run()
  {
    Shell shell = Display.getCurrent().getActiveShell();
    LocatedInWizard wizard = new LocatedInWizard(appName);

    LocalizedWizardDialog dialog = new LocalizedWizardDialog(shell, wizard);
    dialog.setBlockOnOpen(true);
    dialog.open();

    System.out.println(wizard.getData().toString());
  }
}
