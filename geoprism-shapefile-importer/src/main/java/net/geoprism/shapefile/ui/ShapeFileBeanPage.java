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
package net.geoprism.shapefile.ui;

import net.geoprism.shapefile.LabeledValueBean;

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




public abstract class ShapeFileBeanPage extends WizardPage 
{
  protected final ShapeFileBean data;

  protected DataBindingContext  bindingContext;

  public ShapeFileBeanPage(String pageName, ShapeFileBean data)
  {
    super(pageName);

    this.data = data;
    this.bindingContext = new DataBindingContext(SWTObservables.getRealm(Display.getDefault()));
  }

  protected void bind(ComboViewer viewer, String attribute)
  {
    class TargetToModel extends UpdateValueStrategy 
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

    class ModelToTarget extends UpdateValueStrategy 
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
