package dss.vector.solutions.gis.shapefile;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Display;

import com.runwaysdk.generation.loader.Reloadable;

import dss.vector.solutions.gis.LabeledValueBean;

public abstract class ShapefileBeanPage extends WizardPage implements Reloadable
{
  protected final ShapeFileBean data;

  protected DataBindingContext  bindingContext;

  public ShapefileBeanPage(String pageName, ShapeFileBean data)
  {
    super(pageName);

    this.data = data;
    this.bindingContext = new DataBindingContext(SWTObservables.getRealm(Display.getDefault()));
  }

  protected void bind(ComboViewer viewer, String attribute)
  {
    class TargetToModel extends UpdateValueStrategy implements Reloadable
    {
      @Override
      protected IStatus doSet(IObservableValue observableValue, Object value)
      {
        if (value instanceof LabeledValueBean)
        {
          return super.doSet(observableValue, ( (LabeledValueBean) value ).getValue());
        }
        else
        {
          return super.doSet(observableValue, value);
        }
      }
    }

    class ModelToTarget extends UpdateValueStrategy implements Reloadable
    {
      @Override
      protected IStatus doSet(IObservableValue observableValue, Object value)
      {
        return super.doSet(observableValue, new LabeledValueBean((String) value));
      }
    }

    IObservableValue uiElement = ViewersObservables.observeSingleSelection(viewer);
    IObservableValue modelElement = BeanProperties.value(ShapeFileBean.class, attribute).observe(data);

    UpdateValueStrategy targetToModel = new TargetToModel();

    UpdateValueStrategy modelToTarget = new ModelToTarget();

    bindingContext.bindValue(uiElement, modelElement, targetToModel, modelToTarget);
  }
}
