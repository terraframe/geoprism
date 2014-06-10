package com.runwaysdk.geodashboard;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.metadata.MdClassDAO;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
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
    
    QueryFactory f = new QueryFactory();
    AttributeWrapperQuery awQ = new AttributeWrapperQuery(f);
//    MetadataWrapperQuery mwQ = new MetadataWrapperQuery(f);
    DashboardAttributesQuery daQ = new DashboardAttributesQuery(f);
    
    // restrict by this wrapper and order the attributes
    daQ.WHERE(daQ.parentId().EQ(this.getId()));
    daQ.ORDER_BY_ASC(daQ.getListOrder());
    
    awQ.WHERE(awQ.dashboardMetadata(daQ));
    
    OIterator<? extends AttributeWrapper> iter = awQ.getIterator();
    try
    {
      while(iter.hasNext())
      {
        AttributeWrapper aWrapper = iter.next();
        
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
    }
    finally
    {
      iter.close();
    }

    return mdAttr.toArray(new MdAttributeView[mdAttr.size()]);
  }

}
