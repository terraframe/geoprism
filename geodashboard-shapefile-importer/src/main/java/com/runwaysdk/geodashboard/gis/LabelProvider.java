package com.runwaysdk.geodashboard.gis;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;


public class LabelProvider implements ILabelProvider
{

  @Override
  public Image getImage(Object arg0)
  {
    return null;
  }

  @Override
  public String getText(Object object)
  {
    if (object instanceof LabeledValueBean)
    {
      return ( (LabeledValueBean) object ).getLabel();
    }

    return null;
  }

  @Override
  public void addListener(ILabelProviderListener arg0)
  {
  }

  @Override
  public void dispose()
  {
  }

  @Override
  public boolean isLabelProperty(Object arg0, String arg1)
  {
    return false;
  }

  @Override
  public void removeListener(ILabelProviderListener arg0)
  {
  }

}
