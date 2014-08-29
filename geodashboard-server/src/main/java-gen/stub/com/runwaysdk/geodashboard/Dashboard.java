package com.runwaysdk.geodashboard;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import com.runwaysdk.dataaccess.DuplicateDataException;
import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.geodashboard.gis.persist.DashboardMap;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.system.metadata.MdAttributeDate;
import com.runwaysdk.system.metadata.MdAttributeDouble;
import com.runwaysdk.system.metadata.MdAttributeVirtual;
import com.runwaysdk.system.metadata.MdClass;
import com.runwaysdk.system.metadata.MdView;

public class Dashboard extends DashboardBase implements com.runwaysdk.generation.loader.Reloadable
{
  private static final long serialVersionUID = 2043512251;
  
  public Dashboard()
  {
    super();
  }
  
  public static DashboardQuery getSortedDashboards()
  {
    QueryFactory f = new QueryFactory();
    DashboardQuery q = new DashboardQuery(f);
    
    q.ORDER_BY_DESC(q.getDisplayLabel().localize());
    
    return q;
  }
  
  @Override
  public void delete()
  {
    for(MetadataWrapper mw : this.getAllMetadata())
    {
      mw.delete();
    }
    
    super.delete();
  }
  
  public MdClass[] getSortedTypes()
  {
    // This operation should use only cached objects
    OIterator<? extends MetadataWrapper> iter = this.getAllMetadata();
    
    
    List<MdClass> mdClasses = new LinkedList<MdClass>();
    try
    {
      while(iter.hasNext())
      {
        MetadataWrapper mw = iter.next();
        mdClasses.add(mw.getWrappedMdClass());
      }
    }
    finally
    {
      iter.close();
    }
    
    Collections.sort(mdClasses, new MdClassComparator());
    
    return mdClasses.toArray(new MdClass[mdClasses.size()]);
  }
  
  private static class MdClassComparator implements Comparator<MdClass>, Reloadable
  {

    @Override
    public int compare(MdClass m1, MdClass m2)
    {
      return m1.getDisplayLabel().getValue().compareTo(m2.getDisplayLabel().getValue());
    }
    
  }
  
  public static Dashboard create(Dashboard dto) {
    dto.apply();
    
    dto.lock();
    dto.linkMetadata();
    dto.unlock();
    
    return dto;
  }
  
  public void linkMetadata() {
    String pack = "org.ideorg.iq";
    String type = "SalesTransactionView";

    try {
      // Make the MdView the Dashboard will reference
      MdView demoView = new MdView();
      demoView.getDisplayLabel().setDefaultValue("Sale Statistics");
      demoView.setTypeName(type);
      demoView.setPackageName(pack);
      demoView.apply();
      
      //SalesTransaction.NUMBEROFUNITS
      String unitsKey = "org.ideorg.iq.SalesTransaction.numberOfUnits";
      MdAttributeDouble units = MdAttributeDouble.getByKey(unitsKey);
      
      MdAttributeVirtual virtualUnits = new MdAttributeVirtual();
      virtualUnits.setMdAttributeConcrete(units);
      virtualUnits.setDefiningMdView(demoView);
      virtualUnits.apply();
      
      // Delivery Date
      String deliveryDateKey = "org.ideorg.iq.SalesTransaction.deliveryDate";
      MdAttributeDate date = MdAttributeDate.getByKey(deliveryDateKey);
      
      MdAttributeVirtual virtualDate = new MdAttributeVirtual();
      virtualDate.setMdAttributeConcrete(date);
      virtualDate.setDefiningMdView(demoView);
      virtualDate.apply();
    }
    catch (DuplicateDataException e) {
      
    }
    
    MdView view = MdView.getMdView("org.ideorg.iq.SalesTransactionView");

    MetadataWrapper mWrapper = new MetadataWrapper();
    mWrapper.setWrappedMdClass(view);
    mWrapper.apply();

    DashboardMetadata dm = this.addMetadata(mWrapper);
    dm.setListOrder(0);
    dm.apply();

    // Number of Units
    MdAttributeVirtual virtualUnits = MdAttributeVirtual.getByKey("org.ideorg.iq.SalesTransactionView.numberOfUnits");
    
    AttributeWrapper unitWrapper = new AttributeWrapper();
    unitWrapper.setWrappedMdAttribute(virtualUnits);
    unitWrapper.apply();

    DashboardAttributes unitWrapperRel = mWrapper.addAttributeWrapper(unitWrapper);
    unitWrapperRel.setListOrder(1);
    unitWrapperRel.apply();
    
    // Delivery Date
    MdAttributeVirtual virtualDate = MdAttributeVirtual.getByKey("org.ideorg.iq.SalesTransactionView.deliveryDate");
    
    AttributeWrapper dateWrapper = new AttributeWrapper();
    dateWrapper.setWrappedMdAttribute(virtualDate);
    dateWrapper.apply();
    
    DashboardAttributes dateWrapperRel = mWrapper.addAttributeWrapper(dateWrapper);
    dateWrapperRel.setListOrder(2);
    dateWrapperRel.apply();
    
    apply();
  }
  
  @Override
  public void apply()
  {
    
    boolean isNew = isNew();
    
    if (isNew && this.getMap() == null) {
      DashboardMap map = new DashboardMap();
      map.setName(this.getDisplayLabel().getValue());
      map.apply();

      this.setMap(map);
    }
    
    super.apply();
  }
  
}
