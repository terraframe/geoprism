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

import java.util.List;

import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
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
import com.runwaysdk.system.gis.geo.GeoEntity;
import com.runwaysdk.system.gis.geo.GeoNode;
import com.runwaysdk.system.gis.geo.Universal;

public class DashboardBuilder
{

  private boolean isGeoEntityAttribute(MdAttributeDAOIF mdAttribute)
  {
    mdAttribute = mdAttribute.getMdAttributeConcrete();

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

  protected MetadataWrapper getOrCreateMetadataWrapper(MdClassDAOIF mdClass, Universal _universal)
  {
    MetadataWrapperQuery query = new MetadataWrapperQuery(new QueryFactory());
    query.WHERE(query.getWrappedMdClass().EQ(mdClass));

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
        mWrapper.setValue(MetadataWrapper.WRAPPEDMDCLASS, mdClass.getId());
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

  protected AttributeWrapper getOrCreateAttributeWrapper(DashboardTypeInfo info, MdClassDAOIF mdClass, String attributeName)
  {
    MdAttributeDAOIF mdAttribute = mdClass.definesAttribute(attributeName);

    AttributeWrapperQuery query = new AttributeWrapperQuery(new QueryFactory());
    query.WHERE(query.getWrappedMdAttribute().EQ(mdAttribute));

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
        wrapper.setValue(AttributeWrapper.WRAPPEDMDATTRIBUTE, mdAttribute.getId());
        wrapper.apply();

        return wrapper;
      }
    }
    finally
    {
      it.close();
    }
  }

  public void build(Dashboard _dashboard, MdClassDAOIF mdClass, Universal uni, DashboardTypeInfo info)
  {
    List<String> attributes = info.getAttributes();

    MetadataWrapper mWrapper = this.getOrCreateMetadataWrapper(mdClass, uni);

    DashboardMetadata dm = _dashboard.addMetadata(mWrapper);
    dm.setListOrder(info.getIndex());
    dm.apply();

    int listOrder = 0;

    for (String attributeName : attributes)
    {
      AttributeWrapper unitWrapper = this.getOrCreateAttributeWrapper(info, mdClass, attributeName);

      MdAttributeDAOIF mdAttribute = mdClass.definesAttribute(attributeName);

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
