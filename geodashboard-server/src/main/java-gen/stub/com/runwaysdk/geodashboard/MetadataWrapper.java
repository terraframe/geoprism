package com.runwaysdk.geodashboard;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.metadata.MdClassDAO;
import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.session.Session;
import com.runwaysdk.system.metadata.MdClass;

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

    for (MdAttributeDAOIF attr : md.definesAttributesOrdered())
    {
      if (!attr.isSystem())
      {
        MdAttributeView view = new MdAttributeView();

        String id = attr.getId();
        String label = attr.getDisplayLabel(locale);

        view.setMdClassId(mdId);
        view.setMdAttributeId(id);
        view.setDisplayLabel(label);

        mdAttr.add(view);
      }
    }

    Collections.sort(mdAttr, new MdAttributeViewComparator());

    return mdAttr.toArray(new MdAttributeView[mdAttr.size()]);
  }

  public static class MdAttributeViewComparator implements Comparator<MdAttributeView>, Reloadable
  {

    @Override
    public int compare(MdAttributeView m1, MdAttributeView m2)
    {
      return m1.getDisplayLabel().compareTo(m2.getDisplayLabel());
    }

  }

}
