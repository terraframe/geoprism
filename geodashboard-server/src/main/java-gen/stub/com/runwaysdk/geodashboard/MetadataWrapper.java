package com.runwaysdk.geodashboard;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.metadata.MdAttributeDAO;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.session.Session;

public class MetadataWrapper extends MetadataWrapperBase implements com.runwaysdk.generation.loader.Reloadable
{
  private static final long serialVersionUID = -1121470685;

  public MetadataWrapper()
  {
    super();
  }

  @Override
  public void delete()
  {
    for (AttributeWrapper aw : this.getAllAttributeWrapper())
    {
      aw.delete();
    }

    super.delete();

    this.getWrappedMdClass().delete();
  }

  public MdAttributeView[] getSortedAttributes()
  {
    String mdId = this.getWrappedMdClassId();

    List<MdAttributeView> mdAttr = new LinkedList<MdAttributeView>();
    Locale locale = Session.getCurrentLocale();

    QueryFactory f = new QueryFactory();
    AttributeWrapperQuery awQ = new AttributeWrapperQuery(f);
    // MetadataWrapperQuery mwQ = new MetadataWrapperQuery(f);
    DashboardAttributesQuery daQ = new DashboardAttributesQuery(f);

    // restrict by this wrapper and order the attributes
    daQ.WHERE(daQ.parentId().EQ(this.getId()));
    daQ.ORDER_BY_ASC(daQ.getListOrder());

    awQ.WHERE(awQ.dashboardMetadata(daQ));

    OIterator<? extends AttributeWrapper> iter = awQ.getIterator();
    try
    {
      while (iter.hasNext())
      {
        AttributeWrapper aWrapper = iter.next();

        MdAttributeDAOIF attr = MdAttributeDAO.get(aWrapper.getWrappedMdAttributeId());
        MdAttributeConcreteDAOIF mdAttributeConcrete = attr.getMdAttributeConcrete();

        String attrId = attr.getId();
        String attrType = mdAttributeConcrete.getType();

        MdAttributeView view = new MdAttributeView();

        String label = attr.getDisplayLabel(locale);

        view.setMdClassId(mdId);
        view.setMdAttributeId(attrId);
        view.setDisplayLabel(label);
        view.setAttributeName(attr.definesAttribute());
        view.setAttributeType(attrType);

        mdAttr.add(view);
      }
    }
    finally
    {
      iter.close();
    }

    return mdAttr.toArray(new MdAttributeView[mdAttr.size()]);
  }

}
