/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk.geodashboard.dashboard;

import java.util.HashMap;
import java.util.List;
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
import com.runwaysdk.geodashboard.MetadataGeoNode;
import com.runwaysdk.geodashboard.MetadataWrapper;
import com.runwaysdk.geodashboard.MetadataWrapperQuery;
import com.runwaysdk.gis.dataaccess.MdAttributeGeometryDAOIF;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.session.Session;
import com.runwaysdk.system.gis.geo.GeoEntity;
import com.runwaysdk.system.gis.geo.GeoNode;
import com.runwaysdk.system.gis.geo.Universal;
import com.runwaysdk.system.metadata.MdAttributeConcrete;
import com.runwaysdk.system.metadata.MdAttributeVirtual;
import com.runwaysdk.system.metadata.MdAttributeVirtualQuery;
import com.runwaysdk.system.metadata.MdView;

public class DashboardMetadataBuilder
{
  private static final String                SUB_PACKAGE   = "view";

  private static final String                CLASS_POSTFIX = "View";

  private HashMap<String, DashboardTypeInfo> typeInformation;

  private Universal                          universal;

  private String                             packageName;

  private String                             label;

  private String                             viewName;

  public DashboardMetadataBuilder(MdBusinessDAOIF _mdBusiness)
  {
    this.packageName = _mdBusiness.getPackage() + "." + SUB_PACKAGE;
    this.viewName = _mdBusiness.getTypeName() + CLASS_POSTFIX;
    this.label = _mdBusiness.getDisplayLabel(Session.getCurrentLocale());
    this.typeInformation = new HashMap<String, DashboardTypeInfo>();
  }

  public DashboardMetadataBuilder(String packageName, String viewName, String label)
  {
    this.packageName = packageName;
    this.viewName = viewName;
    this.label = label;
    this.typeInformation = new HashMap<String, DashboardTypeInfo>();
  }

  private MdView getOrCreateMdView()
  {
    try
    {
      return MdView.getMdView(packageName + "." + viewName);
    }
    catch (Exception e)
    {
      // Make the MdView the Dashboard will reference
      MdView mdView = new MdView();
      mdView.getDisplayLabel().setDefaultValue(label);
      mdView.setPackageName(packageName);
      mdView.setTypeName(viewName);
      mdView.apply();

      return mdView;
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
    else if (mdAttribute instanceof MdAttributeGeometryDAOIF)
    {
      return true;
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

  protected AttributeWrapper getOrCreateAttributeWrapper(DashboardTypeInfo info, MdView mdView, MdBusinessDAOIF mdBusinessDAO, String attributeName)
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

  private MdAttributeVirtual getOrCreateMdAttributeVirtual(DashboardTypeInfo info, MdView mdView, MdBusinessDAOIF mdBusinessDAO, String attributeName)
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

  public void addType(String type, DashboardTypeInfo info)
  {
    this.typeInformation.put(type, info);
  }

  public void setUniversal(Universal universal)
  {
    this.universal = universal;
  }

  public void build(Dashboard _dashboard)
  {
    MdView mdView = this.getOrCreateMdView();

    Set<Entry<String, DashboardTypeInfo>> entries = this.typeInformation.entrySet();

    for (Entry<String, DashboardTypeInfo> entry : entries)
    {
      MdBusinessDAOIF mdBusinessDAO = MdBusinessDAO.getMdBusinessDAO(entry.getKey());

      DashboardTypeInfo info = entry.getValue();
      List<String> attributes = info.getAttributes();

      MetadataWrapper mWrapper = this.getOrCreateMetadataWrapper(mdView, this.universal);

      DashboardMetadata dm = _dashboard.addMetadata(mWrapper);
      dm.setListOrder(info.getIndex());
      dm.apply();

      int listOrder = 0;

      for (String attributeName : attributes)
      {
        AttributeWrapper unitWrapper = this.getOrCreateAttributeWrapper(info, mdView, mdBusinessDAO, attributeName);

        MdAttributeConcreteDAOIF mdAttribute = mdBusinessDAO.definesAttribute(attributeName);

        if (info.isDashboardAttribute(attributeName) && !this.isGeoEntityAttribute(mdAttribute))
        {
          DashboardAttributes unitWrapperRel = mWrapper.addAttributeWrapper(unitWrapper);
          unitWrapperRel.setListOrder(listOrder++);
          unitWrapperRel.apply();
        }
      }

      List<GeoNode> nodes = info.getNodes();

      for (GeoNode node : nodes)
      {
        // Associate the node with the MetadataWrapper
        MetadataGeoNode relationship = new MetadataGeoNode(mWrapper, node);
        relationship.apply();
      }
    }
  }
}
