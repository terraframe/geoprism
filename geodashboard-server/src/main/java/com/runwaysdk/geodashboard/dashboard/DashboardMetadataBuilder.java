package com.runwaysdk.geodashboard.dashboard;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.geodashboard.AttributeWrapper;
import com.runwaysdk.geodashboard.AttributeWrapperQuery;
import com.runwaysdk.geodashboard.Dashboard;
import com.runwaysdk.geodashboard.DashboardAttributes;
import com.runwaysdk.geodashboard.DashboardMetadata;
import com.runwaysdk.geodashboard.MetadataWrapper;
import com.runwaysdk.geodashboard.MetadataWrapperQuery;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.session.Session;
import com.runwaysdk.system.gis.geo.GeoEntity;
import com.runwaysdk.system.gis.geo.Universal;
import com.runwaysdk.system.metadata.MdAttributeConcrete;
import com.runwaysdk.system.metadata.MdAttributeVirtual;
import com.runwaysdk.system.metadata.MdAttributeVirtualQuery;
import com.runwaysdk.system.metadata.MdView;

public class DashboardMetadataBuilder
{
  private static final String SUB_PACKAGE   = "view";

  private static final String CLASS_POSTFIX = "View";

  public MdView getOrCreateMdView(MdBusinessDAOIF _mdBusiness)
  {
    String packageName = _mdBusiness.getPackage() + "." + SUB_PACKAGE;
    String typeName = _mdBusiness.getTypeName() + CLASS_POSTFIX;
    String label = _mdBusiness.getDisplayLabel(Session.getCurrentLocale());

    return getOrCreateMdView(packageName, typeName, label);
  }

  public MdView getOrCreateMdView(String packageName, String typeName, String label)
  {
    try
    {
      return MdView.getMdView(packageName + "." + typeName);
    }
    catch (Exception e)
    {
      // Make the MdView the Dashboard will reference
      MdView mdView = new MdView();
      mdView.getDisplayLabel().setDefaultValue(label);
      mdView.setPackageName(packageName);
      mdView.setTypeName(typeName);
      mdView.apply();

      return mdView;
    }
  }

  public void add(Dashboard _dashboard, MdView _mdView, Universal _universal, Map<String, DashboardInfo> map)
  {
    Set<Entry<String, DashboardInfo>> entries = map.entrySet();

    for (Entry<String, DashboardInfo> entry : entries)
    {
      MdBusinessDAOIF mdBusinessDAO = MdBusinessDAO.getMdBusinessDAO(entry.getKey());

      DashboardInfo info = entry.getValue();
      String[] attributes = info.getAttributes();

      MetadataWrapper mWrapper = getOrCreateMetadataWrapper(_mdView, _universal);

      DashboardMetadata dm = _dashboard.addMetadata(mWrapper);
      dm.setListOrder(info.getIndex());
      dm.apply();

      int listOrder = 0;

      for (String attributeName : attributes)
      {
        AttributeWrapper unitWrapper = getOrCreateAttributeWrapper(info, _mdView, mdBusinessDAO, attributeName);

        MdAttributeConcreteDAOIF mdAttribute = mdBusinessDAO.definesAttribute(attributeName);

        if (!this.isGeoEntityAttribute(mdAttribute))
        {
          DashboardAttributes unitWrapperRel = mWrapper.addAttributeWrapper(unitWrapper);
          unitWrapperRel.setListOrder(listOrder++);
          unitWrapperRel.apply();
        }
      }
    }
  }

  private boolean isGeoEntityAttribute(MdAttributeConcreteDAOIF mdAttribute)
  {
    if (mdAttribute instanceof MdAttributeReferenceDAOIF)
    {
      MdAttributeReferenceDAOIF mdAttribteAttributeReference = (MdAttributeReferenceDAOIF) mdAttribute;

      if (mdAttribteAttributeReference.getReferenceMdBusinessDAO().definesType().equals(GeoEntity.CLASS))
      {
        return true;
      }
    }

    return false;
  }

  protected MetadataWrapper getOrCreateMetadataWrapper(MdView _mdView, Universal _universal)
  {
    MetadataWrapperQuery query = new MetadataWrapperQuery(new QueryFactory());
    query.WHERE(query.getWrappedMdClass().EQ(_mdView));

    OIterator<? extends MetadataWrapper> iterator = query.getIterator();

    try
    {
      if (iterator.hasNext())
      {
        return iterator.next();
      }
      else
      {
        MetadataWrapper mWrapper = new MetadataWrapper();
        mWrapper.setWrappedMdClass(_mdView);
        mWrapper.setUniversal(_universal);
        mWrapper.apply();

        return mWrapper;
      }
    }
    finally
    {
      iterator.close();
    }

  }

  protected AttributeWrapper getOrCreateAttributeWrapper(DashboardInfo info, MdView mdView, MdBusinessDAOIF mdBusinessDAO, String attributeName)
  {
    MdAttributeVirtual mdAttributeVirtual = getOrCreateMdAttributeVirtual(info, mdView, mdBusinessDAO, attributeName);

    AttributeWrapperQuery query = new AttributeWrapperQuery(new QueryFactory());
    query.WHERE(query.getWrappedMdAttribute().EQ(mdAttributeVirtual));

    OIterator<? extends AttributeWrapper> it = query.getIterator();

    try
    {
      if (it.hasNext())
      {
        return it.next();
      }
      else
      {
        AttributeWrapper wrapper = new AttributeWrapper();
        wrapper.setWrappedMdAttribute(mdAttributeVirtual);
        wrapper.apply();
        return wrapper;
      }
    }
    finally
    {
      it.close();
    }
  }

  private MdAttributeVirtual getOrCreateMdAttributeVirtual(DashboardInfo info, MdView mdView, MdBusinessDAOIF mdBusinessDAO, String attributeName)
  {
    MdAttributeConcrete mdAttributeConcrete = DashboardMetadataBuilder.getMdAttributeConcrete(mdBusinessDAO, attributeName);

    MdAttributeVirtualQuery query = new MdAttributeVirtualQuery(new QueryFactory());
    query.WHERE(query.getMdAttributeConcrete().EQ(mdAttributeConcrete));
    query.AND(query.getDefiningMdView().EQ(mdView));

    OIterator<? extends MdAttributeVirtual> iterator = query.getIterator();

    try
    {
      if (iterator.hasNext())
      {
        return iterator.next();
      }
      else
      {
        MdAttributeVirtual virtual = new MdAttributeVirtual();
        virtual.setMdAttributeConcrete(mdAttributeConcrete);
        virtual.setDefiningMdView(mdView);

        if (info.getLabel(attributeName) != null)
        {
          virtual.getDisplayLabel().setDefaultValue(info.getLabel(attributeName));
        }

        virtual.apply();

        return virtual;
      }
    }
    finally
    {
      iterator.close();
    }
  }

  public static MdAttributeConcrete getMdAttributeConcrete(MdBusinessDAOIF _mdBusiness, String _attributeName)
  {
    MdAttributeConcreteDAOIF mdConcreteDAO = _mdBusiness.definesAttribute(_attributeName);

    return MdAttributeConcrete.getByKey(mdConcreteDAO.getKey());
  }

}
