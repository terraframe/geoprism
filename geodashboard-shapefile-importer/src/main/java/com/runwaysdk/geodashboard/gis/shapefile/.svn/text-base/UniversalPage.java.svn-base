package dss.vector.solutions.gis.shapefile;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.runwaysdk.generation.loader.Reloadable;

import dss.vector.solutions.geo.GeoHierarchyView;
import dss.vector.solutions.gis.Localizer;
import dss.vector.solutions.gis.IInitPage;
import dss.vector.solutions.gis.LabelProvider;

public class UniversalPage extends ShapefileBeanPage implements IInitPage, PropertyChangeListener, Reloadable
{
  public static final String PAGE_NAME = "UniversalPage";

  private ComboViewer        universal;

  private ComboViewer        type;

  private ComboViewer        subtype;

  private GeoHierarchyView[] views;

  public UniversalPage(ShapeFileBean data, GeoHierarchyView[] views)
  {
    super(PAGE_NAME, data);

    setTitle(Localizer.getMessage("UNIVERSAL_TITLE"));
    setDescription(Localizer.getMessage("UNIVERSAL_DESC"));
    setPageComplete(false);

    this.views = views;
    this.data.addPropertyChangeListener("universal", this);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets
   * .Composite)
   */
  public void createControl(Composite parent)
  {
    Composite composite = new Composite(parent, SWT.NULL);
    composite.setLayout(new GridLayout(2, false));

    new Label(composite, SWT.NULL).setText(Localizer.getMessage("UNIVERSAL") + ": ");
    universal = new ComboViewer(composite, SWT.BORDER | SWT.READ_ONLY);
    universal.getControl().setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    universal.setContentProvider(new UniversalContentProvider(views));
    universal.setLabelProvider(new LabelProvider());
    universal.setInput(data);

    new Label(composite, SWT.NULL).setText(Localizer.getMessage("TYPE") + ": ");
    type = new ComboViewer(composite, SWT.BORDER | SWT.READ_ONLY);
    type.getControl().setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    type.setContentProvider(new AttributeContentProvider(false));
    type.setLabelProvider(new LabelProvider());

    new Label(composite, SWT.NULL).setText(Localizer.getMessage("SUBTYPE") + ": ");
    subtype = new ComboViewer(composite, SWT.BORDER | SWT.READ_ONLY);
    subtype.getControl().setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    subtype.setContentProvider(new AttributeContentProvider(false));
    subtype.setLabelProvider(new LabelProvider());

    setControl(composite);

    this.bind(universal, "universal");
    this.bind(type, "type");
    this.bind(subtype, "subtype");
  }

  @Override
  public void init()
  {
    type.setInput(data);
    subtype.setInput(data);

    bindingContext.updateTargets();
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt)
  {
    boolean complete = data.getUniversal() != null;

    if (complete)
    {
      IWizardPage page = this.getNextPage();

      if (page instanceof IInitPage)
      {
        ( (IInitPage) page ).init();
      }

      setPageComplete(complete);
    }
  }
}
