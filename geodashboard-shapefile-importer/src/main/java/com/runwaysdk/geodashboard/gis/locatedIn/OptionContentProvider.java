package com.runwaysdk.geodashboard.gis.locatedIn;

import java.util.Collection;
import java.util.LinkedList;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.geodashboard.gis.LabeledValueBean;
import com.runwaysdk.geodashboard.gis.Localizer;


public class OptionContentProvider implements IStructuredContentProvider, Reloadable
{
  private Collection<LabeledValueBean> options;

  public OptionContentProvider()
  {
    this.options = new LinkedList<LabeledValueBean>();
    this.options.add(new LabeledValueBean(null, Localizer.getMessage("CHOOSE_OPTION")));
    this.options.add(new LabeledValueBean(LocatedInBean.BuildTypes.REBUILD_ALL.name(), Localizer.getMessage("DELETE_EXISTING")));
    this.options.add(new LabeledValueBean(LocatedInBean.BuildTypes.ORPHANED_ONLY.name(), Localizer.getMessage("BUILD_ORPHANED")));
  }

  @Override
  public Object[] getElements(Object element)
  {
    return options.toArray(new LabeledValueBean[options.size()]);
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
