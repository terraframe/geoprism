package com.runwaysdk.geodashboard;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.metadata.MdClassDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.session.Session;
import com.runwaysdk.system.metadata.MdAttribute;
import com.runwaysdk.system.metadata.MdAttributeVirtual;
import com.runwaysdk.system.metadata.MdClass;
import com.runwaysdk.system.metadata.MdView;

public class MetadataWrapper extends MetadataWrapperBase implements
    com.runwaysdk.generation.loader.Reloadable
{
  private static final long serialVersionUID = -1121470685;

  public MetadataWrapper()
  {
    super();
  }
  
  @Override
  public void delete()
  {
    for(AttributeWrapper aw : this.getAllAttributeWrapper())
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
        String attrType;
        if(attr instanceof MdAttributeVirtual)
        {
          MdAttributeVirtual vAttr = (MdAttributeVirtual) attr;
          attrId = vAttr.getMdAttributeConcreteId();
          attrType = vAttr.getMdAttributeConcrete().getType();
        }
        else
        {
          attrId = attr.getId();
          attrType = attr.getType();
        }
        
        MdAttributeView view = new MdAttributeView();

        String label = attr.getDisplayLabel(locale);

        view.setMdClassId(mdId);
        view.setMdAttributeId(attrId);
        view.setDisplayLabel(label);
        view.setAttributeName(attr.getAttributeName());
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
