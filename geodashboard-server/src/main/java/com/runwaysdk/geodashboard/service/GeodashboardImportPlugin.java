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
package com.runwaysdk.geodashboard.service;

import org.xml.sax.Attributes;

import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.io.dataDefinition.CreateHandler;
import com.runwaysdk.dataaccess.io.dataDefinition.CreateOrUpdateHandler;
import com.runwaysdk.dataaccess.io.dataDefinition.HandlerFactory;
import com.runwaysdk.dataaccess.io.dataDefinition.HandlerFactoryIF;
import com.runwaysdk.dataaccess.io.dataDefinition.ImportPluginIF;
import com.runwaysdk.dataaccess.io.dataDefinition.SearchHandler;
import com.runwaysdk.dataaccess.io.dataDefinition.TagContext;
import com.runwaysdk.dataaccess.io.dataDefinition.TagHandler;
import com.runwaysdk.dataaccess.io.dataDefinition.UpdateHandler;
import com.runwaysdk.dataaccess.io.dataDefinition.XMLTags;
import com.runwaysdk.dataaccess.metadata.MdAttributeDAO;
import com.runwaysdk.dataaccess.metadata.MdClassDAO;
import com.runwaysdk.dataaccess.metadata.MdTypeDAO;
import com.runwaysdk.geodashboard.Dashboard;
import com.runwaysdk.geodashboard.DashboardQuery;
import com.runwaysdk.geodashboard.dashboard.DashboardBuilder;
import com.runwaysdk.geodashboard.dashboard.DashboardTypeInfo;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.system.gis.geo.GeoEntity;
import com.runwaysdk.system.gis.geo.GeoNode;
import com.runwaysdk.system.gis.geo.GeoNodeGeometry;
import com.runwaysdk.system.gis.geo.Universal;

public class GeodashboardImportPlugin implements ImportPluginIF
{
  private static class TypeNodeHandler extends TagHandler
  {
    protected static final String ATTRIBUTE = "geoEntityAttribute";

    public TypeNodeHandler(ImportManager manager)
    {
      super(manager);
    }

    @Override
    public void onStartElement(String localName, Attributes attributes, TagContext context)
    {
      DashboardTypeInfo info = (DashboardTypeInfo) context.getObject(DashboardTypeInfo.class.getName());
      String type = info.getType();
      String attribute = attributes.getValue(ATTRIBUTE);

      if (type != null)
      {
        String key = type + "." + attribute;
        GeoNode node = GeoNode.getByKey(key);

        if (node == null)
        {
          String[] search_tags = { "geoEntityNode", "geoEntityGeometry" };
          String cause = "GeoNode:" + key;

          SearchHandler.searchEntity(this.getManager(), search_tags, XMLTags.NAME_ATTRIBUTE, type, cause);

          node = GeoNode.getByKey(key);
        }

        if (node != null)
        {
          info.addGeoNode(node);

          this.setViewOnlyAttribute(info, node, GeoNode.GEOENTITYATTRIBUTE);

          if (node instanceof GeoNodeGeometry)
          {
            this.setViewOnlyAttribute(info, node, GeoNodeGeometry.GEOENTITYATTRIBUTE);
            this.setViewOnlyAttribute(info, node, GeoNodeGeometry.GEOMETRYATTRIBUTE);
            this.setViewOnlyAttribute(info, node, GeoNodeGeometry.DISPLAYLABELATTRIBUTE);
            this.setViewOnlyAttribute(info, node, GeoNodeGeometry.IDENTIFIERATTRIBUTE);
            this.setViewOnlyAttribute(info, node, GeoNodeGeometry.MULTIPOLYGONATTRIBUTE);
            this.setViewOnlyAttribute(info, node, GeoNodeGeometry.POINTATTRIBUTE);
          }
        }
        else
        {
          throw new RuntimeException("Unable to find the attribute metadata for [" + key + "]");
        }
      }

    }

    private void setViewOnlyAttribute(DashboardTypeInfo info, GeoNode node, String nodeAttribute)
    {
      String id = node.getValue(nodeAttribute);

      if (id != null && id.length() > 0)
      {
        MdAttributeDAOIF mdAttribute = MdAttributeDAO.get(id);

        info.addViewOnlyAttribute(mdAttribute.definesAttribute());
      }
    }
  }

  private static class TypeAttributeHandler extends TagHandler
  {
    protected static final String NAME = "name";

    public TypeAttributeHandler(ImportManager manager)
    {
      super(manager);
    }

    @Override
    public void onStartElement(String localName, Attributes attributes, TagContext context)
    {
      DashboardTypeInfo info = (DashboardTypeInfo) context.getObject(DashboardTypeInfo.class.getName());
      String type = info.getType();

      String name = attributes.getValue(NAME);

      if (type != null)
      {
        if (!MdTypeDAO.isDefined(type))
        {
          String[] search_tags = XMLTags.TYPE_TAGS;
          SearchHandler.searchEntity(this.getManager(), search_tags, XMLTags.NAME_ATTRIBUTE, type, "DataSet:" + type);
        }

        MdClassDAOIF mdClass = MdClassDAO.getMdClassDAO(type);
        MdAttributeDAOIF mdAttribute = mdClass.definesAttribute(name);

        // IMPORTANT: It is possible that the concrete type is defined before this
        // schema was imported. However, the definition of the concrete attribute
        // may not be defined until an update statement in the current xml file.
        // As such it is possible to have the type defined but not have the
        // concrete attribute defined. Therefore, if the attribute is not defined
        // then we need to search and import the definition of the class which
        // exists in the current xml file.
        if (mdAttribute == null)
        {
          String[] search_tags = XMLTags.TYPE_TAGS;
          SearchHandler.searchEntity(this.getManager(), search_tags, XMLTags.NAME_ATTRIBUTE, type, mdClass.definesType());

          mdAttribute = mdClass.definesAttribute(name);
        }

        if (mdAttribute != null)
        {
          info.add(mdAttribute.definesAttribute());
        }
        else
        {
          throw new RuntimeException("Unable to find the attribute metadata for [" + type + "." + name + "]");
        }
      }

    }
  }

  private static class TypeHandler extends TagHandler
  {

    private static final String TYPE          = "type";

    private static final String INDEX         = "index";

    private static final String UNIVERSAL     = "universal";

    private static final String GEONODE_TAG   = "geoNode";

    private static final String ATTRIBUTE_TAG = "attribute";

    public TypeHandler(ImportManager manager)
    {
      super(manager);

      this.addHandler(GEONODE_TAG, new TypeNodeHandler(manager));
      this.addHandler(ATTRIBUTE_TAG, new TypeAttributeHandler(manager));
    }

    @Override
    public void onStartElement(String localName, Attributes attributes, TagContext context)
    {
      String type = attributes.getValue(TYPE);
      Integer index = new Integer(attributes.getValue(INDEX));
      String universal = attributes.getValue(UNIVERSAL);

      DashboardTypeInfo info = new DashboardTypeInfo(type, index, universal);

      context.setObject(DashboardTypeInfo.class.getName(), info);
    }

    @Override
    public void onEndElement(String uri, String localName, String name, TagContext context)
    {
      Dashboard dashboard = (Dashboard) context.getObject(Dashboard.CLASS);
      DashboardTypeInfo info = (DashboardTypeInfo) context.getObject(DashboardTypeInfo.class.getName());

      MdClassDAOIF mdClass = MdClassDAO.getMdClassDAO(info.getType());
      Universal universal = Universal.getByKey(info.getUniversal());

      DashboardBuilder builder = new DashboardBuilder();
      builder.build(dashboard, mdClass, universal, info);
    }
  }

  private static class DashboardHandler extends TagHandler
  {
    private static final String LABEL    = "label";

    private static final String COUNTRY  = "country";

    private static final String TYPE_TAG = "type";

    public DashboardHandler(ImportManager manager)
    {
      super(manager);

      this.addHandler(TYPE_TAG, new TypeHandler(manager));
    }

    @Override
    public void onStartElement(String localName, Attributes attributes, TagContext context)
    {
      String label = attributes.getValue(LABEL);
      String country = attributes.getValue(COUNTRY);

      Dashboard dashboard = this.getOrCreateDashboard(label);
      dashboard.getDisplayLabel().setDefaultValue(label);
      dashboard.setCountry(GeoEntity.getByKey(country));
      dashboard.apply();

      context.setObject(Dashboard.CLASS, dashboard);
    }

    private Dashboard getOrCreateDashboard(String label)
    {
      if (this.getManager().isUpdateState() || this.getManager().isCreateOrUpdateState())
      {
        Dashboard dashboard = this.getDashboard(label);

        if (dashboard != null)
        {
          dashboard.lock();

          return dashboard;
        }
        else if (this.getManager().isUpdateState())
        {
          String message = "Unable to find a [" + Dashboard.CLASS + "] with a default locale of [" + label + "]";
          MdClassDAOIF mdClass = MdClassDAO.getMdClassDAO(Dashboard.CLASS);

          throw new DataNotFoundException(message, mdClass);
        }
      }

      return new Dashboard();
    }

    private Dashboard getDashboard(String label)
    {
      DashboardQuery query = new DashboardQuery(new QueryFactory());
      query.WHERE(query.getDisplayLabel().getDefaultLocale().EQ(label));
      OIterator<? extends Dashboard> iterator = query.getIterator();

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
  }

  private static class CreateHandlerFactory extends HandlerFactory implements HandlerFactoryIF
  {
    private static final String DASHBOARD_TAG         = "dashboard";

    public CreateHandlerFactory(ImportManager manager)
    {
      this.addHandler(DASHBOARD_TAG, new DashboardHandler(manager));
    }
  }

  @Override
  public void register(ImportManager manager)
  {
    manager.register(CreateHandler.class.getName(), new CreateHandlerFactory(manager));
    manager.register(CreateOrUpdateHandler.class.getName(), new CreateHandlerFactory(manager));
    manager.register(UpdateHandler.class.getName(), new CreateHandlerFactory(manager));
  }

  @Override
  public String getModuleIdentifier()
  {
    return "Geodashboard";
  }

}
