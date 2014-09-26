package com.runwaysdk.geodashboard.oda.driver.ui.editors;

import java.util.Map;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;

public class TypeLabelProvider extends LabelProvider implements ILabelProvider
{
  private Map<String, String> map;

  public TypeLabelProvider(Map<String, String> map)
  {
    this.map = map;
  }

  @Override
  public String getText(Object element)
  {
    String key = (String) element;

    return this.map.get(key);
  }
}
