package dss.vector.solutions.gis.shapefile;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.runwaysdk.generation.loader.Reloadable;

import dss.vector.solutions.gis.Localizer;
import dss.vector.solutions.gis.LabeledValueBean;

public class AttributeContentProvider implements IStructuredContentProvider, Reloadable
{
  private boolean required;

  public AttributeContentProvider(boolean required)
  {
    this.required = required;
  }

  @Override
  public Object[] getElements(Object object)
  {
    Collection<LabeledValueBean> collection = new LinkedList<LabeledValueBean>();

    if (object instanceof ShapeFileBean)
    {
      ShapeFileBean data = (ShapeFileBean) object;

      if (required)
      {
        collection.add(new LabeledValueBean(null, Localizer.getMessage("CHOOSE_OPTION")));
      }
      else
      {
        collection.add(new LabeledValueBean(null, ""));
      }

      List<String> attributes = data.getAttributes();

      if (attributes != null)
      {
        for (String attribute : attributes)
        {
          collection.add(new LabeledValueBean(attribute));
        }
      }
    }

    return collection.toArray(new LabeledValueBean[collection.size()]);
  }

  @Override
  public void dispose()
  {
  }

  @Override
  public void inputChanged(Viewer arg0, Object arg1, Object arg2)
  {
  }

}
