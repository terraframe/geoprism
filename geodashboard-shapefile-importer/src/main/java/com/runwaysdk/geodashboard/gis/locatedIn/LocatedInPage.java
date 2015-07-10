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
package com.runwaysdk.geodashboard.gis.locatedIn;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Scale;

import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.geodashboard.gis.LabelProvider;
import com.runwaysdk.geodashboard.gis.LabeledValueBean;
import com.runwaysdk.geodashboard.gis.Localizer;
import com.runwaysdk.geodashboard.gis.locatedIn.LocatedInBean.BuildTypes;


public class LocatedInPage extends WizardPage implements PropertyChangeListener, Reloadable
{
  class OverlapListener implements Listener, Reloadable
  {
    public void handleEvent(Event event)
    {
      currentPercent.setText(overlapPercent.getSelection() + " %");
    }
  }

  public static String       PAGE_NAME = "LocatedInPage";

  private LocatedInBean      bean;

  private ComboViewer        option;

  private Scale              overlapPercent;

  private Label              currentPercent;

  private DataBindingContext bindingContext;

  public LocatedInPage(LocatedInBean bean)
  {
    super(PAGE_NAME);

    setTitle(Localizer.getMessage("LOCATED_IN_WIZARD"));
    setDescription(Localizer.getMessage("LOCATED_IN_WIZARD_OPTIONS"));

    this.bean = bean;
    this.bean.addPropertyChangeListener("option", this);
    this.bindingContext = new DataBindingContext(SWTObservables.getRealm(Display.getDefault()));

    setPageComplete(bean.getOption() != null);
  }

  @Override
  public void createControl(Composite parent)
  {
    Composite composite = new Composite(parent, SWT.NULL);
    composite.setLayout(new GridLayout(2, false));

    new Label(composite, SWT.NULL).setText(Localizer.getMessage("ALGORITHM") + ": ");
    option = new ComboViewer(composite, SWT.BORDER | SWT.READ_ONLY);
    option.getControl().setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    option.setContentProvider(new OptionContentProvider());
    option.setLabelProvider(new LabelProvider());
    option.setInput(bean);

    new Label(composite, SWT.NULL).setText(Localizer.getMessage("OVERLAP_PERCENT") + ": ");
    overlapPercent = new Scale(composite, SWT.HORIZONTAL);
    overlapPercent.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    overlapPercent.setMinimum(0);
    overlapPercent.setMaximum(100);
    overlapPercent.setIncrement(1);
    overlapPercent.setPageIncrement(10);

    currentPercent = new Label(composite, SWT.LEFT | SWT.FILL);
    currentPercent.setText(this.bean.getOverlapPercent() + "%     ");

    overlapPercent.addListener(SWT.Selection, new OverlapListener());

    setControl(composite);

    this.bind(option, "option");
    this.bind(overlapPercent, "overlapPercent");
  }

  private void bind(ComboViewer viewer, String attribute)
  {
    class TargetToModel extends UpdateValueStrategy implements Reloadable
    {
      @Override
      protected IStatus doSet(IObservableValue observableValue, Object value)
      {
        if (value instanceof LabeledValueBean)
        {
          String _value = ( (LabeledValueBean) value ).getValue();

          if (_value != null)
          {
            BuildTypes _option = LocatedInBean.BuildTypes.valueOf(_value);

            return super.doSet(observableValue, _option);
          }

          return super.doSet(observableValue, null);
        }

        return super.doSet(observableValue, value);
      }
    }

    class ModelToTarget extends UpdateValueStrategy implements Reloadable
    {
      @Override
      protected IStatus doSet(IObservableValue observableValue, Object value)
      {
        if (value instanceof LocatedInBean.BuildTypes)
        {
          BuildTypes _option = (LocatedInBean.BuildTypes) value;

          return super.doSet(observableValue, new LabeledValueBean(_option.name()));
        }

        return super.doSet(observableValue, new LabeledValueBean((String) value));
      }
    }

    IObservableValue uiElement = ViewersObservables.observeSingleSelection(viewer);
    IObservableValue modelElement = BeanProperties.value(LocatedInBean.class, attribute).observe(bean);

    UpdateValueStrategy targetToModel = new TargetToModel();
    UpdateValueStrategy modelToTarget = new ModelToTarget();

    bindingContext.bindValue(uiElement, modelElement, targetToModel, modelToTarget);
  }

  private void bind(Scale scale, String attribute)
  {
    IObservableValue uiElement = SWTObservables.observeSelection(scale);
    IObservableValue modelElement = BeanProperties.value(LocatedInBean.class, attribute).observe(bean);

    bindingContext.bindValue(uiElement, modelElement, null, null);
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt)
  {
    setPageComplete(bean.getOption() != null);
  }
}
