package com.runwaysdk.geodashboard.oda.driver.ui.editors;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;

import com.runwaysdk.geodashboard.oda.driver.ui.provider.DataSetType;

public class DataSetTypeLabelProvider extends LabelProvider implements ILabelProvider
{
  @Override
  public String getText(Object element)
  {
    DataSetType type = (DataSetType) element;

    return type.getLabel();
  }
}
