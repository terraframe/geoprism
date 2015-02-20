package com.runwaysdk.geodashboard.oda.driver.ui.editors;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;

import com.runwaysdk.geodashboard.oda.driver.ui.provider.LabelValuePair;

public class DataSetTypeLabelProvider extends LabelProvider implements ILabelProvider
{
  @Override
  public String getText(Object element)
  {
    LabelValuePair type = (LabelValuePair) element;

    return type.getLabel();
  }
}
