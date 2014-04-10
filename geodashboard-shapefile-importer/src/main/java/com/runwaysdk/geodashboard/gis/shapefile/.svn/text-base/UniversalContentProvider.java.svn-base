package dss.vector.solutions.gis.shapefile;

import java.util.Collection;
import java.util.LinkedList;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.runwaysdk.generation.loader.Reloadable;

import dss.vector.solutions.geo.GeoHierarchyView;
import dss.vector.solutions.gis.Localizer;
import dss.vector.solutions.gis.LabeledValueBean;

public class UniversalContentProvider implements IStructuredContentProvider, Reloadable
{
  private Collection<LabeledValueBean> universals;

  public UniversalContentProvider(GeoHierarchyView[] views)
  {
    this.universals = new LinkedList<LabeledValueBean>();
    this.universals.add(new LabeledValueBean(null, Localizer.getMessage("CHOOSE_OPTION")));

    for (GeoHierarchyView view : views)
    {
      this.universals.add(new LabeledValueBean(view.getGeneratedType(), view.getDisplayLabel()));
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
