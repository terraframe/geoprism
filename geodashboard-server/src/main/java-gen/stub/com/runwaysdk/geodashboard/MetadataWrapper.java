package com.runwaysdk.geodashboard;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.metadata.MdClassDAO;
import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.session.Session;
import com.runwaysdk.system.metadata.MdAttribute;
import com.runwaysdk.system.metadata.MdAttributeVirtual;

public class MetadataWrapper extends MetadataWrapperBase implements
    com.runwaysdk.generation.loader.Reloadable
{
  private static final long serialVersionUID = -1121470685;

  public MetadataWrapper()
  {
    super();
  }

  public MdAttributeView[] getSortedAttributes()
  {
    String mdId = this.getWrappedMdClassId();

    MdClassDAOIF md = (MdClassDAOIF) MdClassDAO.get(mdId);

    List<MdAttributeView> mdAttr = new LinkedList<MdAttributeView>();
    Locale locale = Session.getCurrentLocale();

    for (AttributeWrapper aWrapper : this.getAllAttributeWrapper())
    {
      MdAttribute attr = aWrapper.getWrappedMdAttribute();
      String attrId;
      if(attr instanceof MdAttributeVirtual)
      {
        MdAttributeVirtual vAttr = (MdAttributeVirtual) attr;
        attrId = vAttr.getMdAttributeConcreteId();
      }
      else
      {
        attrId = attr.getId();
      }
      
      MdAttributeView view = new MdAttributeView();

      String label = attr.getDisplayLabel(locale);

      view.setMdClassId(mdId);
      view.setMdAttributeId(attrId);
      view.setDisplayLabel(label);

      mdAttr.add(view);
    }

    Collections.sort(mdAttr, new MdAttributeViewComparator());

    return mdAttr.toArray(new MdAttributeView[mdAttr.size()]);
  }

  private static class MdAttributeViewComparator implements Comparator<MdAttributeView>, Reloadable
  {

    @Override
    public int compare(MdAttributeView m1, MdAttributeView m2)
    {
      return m1.getDisplayLabel().compareTo(m2.getDisplayLabel());
    }

  }

}
