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
package net.geoprism.dashboard;

import java.util.List;

import net.geoprism.AttributeWrapper;
import net.geoprism.AttributeWrapperQuery;
import net.geoprism.Dashboard;
import net.geoprism.DashboardAttributes;
import net.geoprism.DashboardAttributesQuery;
import net.geoprism.DashboardMetadata;
import net.geoprism.DashboardMetadataQuery;
import net.geoprism.MetadataWrapper;
import net.geoprism.MetadataWrapperQuery;

import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.gis.dataaccess.MdAttributeGeometryDAOIF;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.system.gis.geo.GeoEntity;

public class DashboardBuilder implements Reloadable
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

  protected MetadataWrapper getOrCreateMetadataWrapper(Dashboard _dashboard, MdClassDAOIF _mdClass)
  {
    MetadataWrapper mWrapper = this.getMetadataWrapper(_dashboard, _mdClass);

    // if (mWrapper == null)
    // {
    // mWrapper = this.getMetadataWrapper(null, _mdClass);
    // }

    if (mWrapper == null)
    {
      mWrapper = new MetadataWrapper();
      mWrapper.setValue(MetadataWrapper.DASHBOARD, _dashboard.getId());
      mWrapper.setValue(MetadataWrapper.WRAPPEDMDCLASS, _mdClass.getId());
      mWrapper.setDashboard(_dashboard);
      mWrapper.apply();
    }

    return mWrapper;
  }

  private MetadataWrapper getMetadataWrapper(Dashboard _dashboard, MdClassDAOIF _mdClass)
  {
    MetadataWrapperQuery query = new MetadataWrapperQuery(new QueryFactory());
    query.WHERE(query.getWrappedMdClass().EQ(_mdClass));

    if (_dashboard != null)
    {
      query.AND(query.getDashboard().EQ(_dashboard));
    }

    OIterator<? extends MetadataWrapper> iterator = query.getIterator();

    try
    {
      if (iterator.hasNext())
      {
        return iterator.next();
      }

      return null;
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

  public void build(Dashboard _dashboard, MdClassDAOIF mdClass, DashboardTypeInfo info)
  {
    List<String> attributes = info.getAttributes();

    MetadataWrapper mWrapper = this.getOrCreateMetadataWrapper(_dashboard, mdClass);

    this.createOrUpdateDashboardMetadata(_dashboard, mWrapper, info);

    int listOrder = 0;

    for (String attributeName : attributes)
    {
      AttributeWrapper unitWrapper = this.getOrCreateAttributeWrapper(info, mdClass, attributeName);

      MdAttributeDAOIF mdAttribute = mdClass.definesAttribute(attributeName);

      if (info.isDashboardAttribute(attributeName) && !this.isGeoEntityAttribute(mdAttribute))
      {
        this.createOrUpdateDashboardAttributes(mWrapper, unitWrapper, listOrder++);
      }
    }
  }

  private void createOrUpdateDashboardAttributes(MetadataWrapper _mWrapper, AttributeWrapper _unitWrapper, int _listOrder)
  {
    DashboardAttributesQuery query = new DashboardAttributesQuery(new QueryFactory());
    query.WHERE(query.getParent().EQ(_mWrapper));
    query.AND(query.getChild().EQ(_unitWrapper));

    OIterator<? extends DashboardAttributes> iterator = query.getIterator();

    try
    {
      if (iterator.hasNext())
      {
        DashboardAttributes dm = iterator.next();
        dm.lock();
        dm.setListOrder(_listOrder);
        dm.apply();
      }
      else
      {
        DashboardAttributes unitWrapperRel = _mWrapper.addAttributeWrapper(_unitWrapper);
        unitWrapperRel.setListOrder(_listOrder);
        unitWrapperRel.apply();
      }
    }
    finally
    {
      iterator.close();
    }
  }

  private void createOrUpdateDashboardMetadata(Dashboard _dashboard, MetadataWrapper _mWrapper, DashboardTypeInfo _info)
  {
    DashboardMetadataQuery query = new DashboardMetadataQuery(new QueryFactory());
    query.WHERE(query.getParent().EQ(_dashboard));
    query.AND(query.getChild().EQ(_mWrapper));
    OIterator<? extends DashboardMetadata> iterator = query.getIterator();

    try
    {
      if (iterator.hasNext())
      {
        DashboardMetadata dm = iterator.next();
        dm.lock();
        dm.setListOrder(_info.getIndex());
        dm.apply();
      }
      else
      {
        DashboardMetadata dm = _dashboard.addMetadata(_mWrapper);
        dm.setListOrder(_info.getIndex());
        dm.apply();
      }
    }
    finally
    {
      iterator.close();
    }
  }
}
