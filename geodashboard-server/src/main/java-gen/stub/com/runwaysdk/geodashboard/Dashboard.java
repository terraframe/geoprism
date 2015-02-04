package com.runwaysdk.geodashboard;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.geodashboard.dashboard.DashboardService;
import com.runwaysdk.geodashboard.dashboard.DashboardBuilderIF;
import com.runwaysdk.geodashboard.gis.persist.DashboardMap;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.system.metadata.MdClass;

public class Dashboard extends DashboardBase implements com.runwaysdk.generation.loader.Reloadable
{
  private static class MdClassComparator implements Comparator<MdClass>, Reloadable
  {

    @Override
    public int compare(MdClass m1, MdClass m2)
    {
      return m1.getDisplayLabel().getValue().compareTo(m2.getDisplayLabel().getValue());
    }
  }

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
    for (MetadataWrapper mw : this.getAllMetadata())
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
      while (iter.hasNext())
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

  @Transaction
  public static Dashboard create(Dashboard dashboard)
  {
    dashboard.apply();

    List<DashboardBuilderIF> builders = DashboardService.getDashboardBuilders();

    int i = 0;

    for (DashboardBuilderIF builder : builders)
    {
      dashboard.lock();

      try
      {
        builder.initialize(dashboard, i++);
      }
      finally
      {
        dashboard.unlock();
      }
    }

    return dashboard;
  }

  @Override
  public void apply()
  {

    boolean isNew = isNew();

    if (isNew && this.getMap() == null)
    {
      DashboardMap map = new DashboardMap();
      map.setName(this.getDisplayLabel().getValue());
      map.apply();

      this.setMap(map);
    }

    super.apply();
  }

}
