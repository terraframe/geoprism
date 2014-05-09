package com.runwaysdk.geodashboard.gis.shapefile;

import java.util.Collection;
import java.util.LinkedList;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.geodashboard.gis.LabeledValueBean;
import com.runwaysdk.geodashboard.gis.Localizer;
import com.runwaysdk.system.gis.geo.Universal;

public class UniversalContentProvider implements IStructuredContentProvider, Reloadable
{
  private Collection<LabeledValueBean> universals;

  public UniversalContentProvider(Universal[] universals)
  {
    this.universals = new LinkedList<LabeledValueBean>();
    this.universals.add(new LabeledValueBean(null, Localizer.getMessage("CHOOSE_OPTION")));

    for (Universal universal : universals)
    {
      this.universals.add(new LabeledValueBean(universal.getId(), universal.getDisplayLabel().getValue()));
    }
  }

  @Override
  public Object[] getElements(Object arg0)
  {
    return universals.toArray(new LabeledValueBean[universals.size()]);
  }

  @Override
  public void dispose()
  {
    // Do nothing
  }

  @Override
  public void inputChanged(Viewer arg0, Object arg1, Object arg2)
  {
    // Do nothing
  }

}
