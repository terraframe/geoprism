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
package net.geoprism.data.importer;

import java.io.File;
import java.net.URI;
import java.util.List;

import net.geoprism.ClassUniversalQuery;
import net.geoprism.MappableAttribute;
import net.geoprism.MappableClass;
import net.geoprism.MappableClassGeoNodeQuery;
import net.geoprism.MappableClassQuery;
import net.geoprism.context.ProjectDataConfiguration;
import net.geoprism.dashboard.Dashboard;
import net.geoprism.dashboard.DashboardBuilder;
import net.geoprism.dashboard.DashboardQuery;
import net.geoprism.dashboard.DashboardState;
import net.geoprism.dashboard.DashboardTypeInfo;
import net.geoprism.data.CachedEndpoint;
import net.geoprism.data.LocationImporter;
import net.geoprism.data.XMLEndpoint;
import net.geoprism.data.XMLLocationImporter;
import net.geoprism.data.aws.AmazonEndpoint;
import net.geoprism.ontology.Classifier;
import net.geoprism.ontology.ClassifierIsARelationship;
import net.geoprism.ontology.ClassifierQuery;
import net.geoprism.ontology.ClassifierTermAttributeRootQuery;

import org.xml.sax.Attributes;

import com.runwaysdk.constants.LocalProperties;
import com.runwaysdk.constants.MdAttributeConcreteInfo;
import com.runwaysdk.constants.MdAttributeReferenceInfo;
import com.runwaysdk.constants.MdElementInfo;
import com.runwaysdk.constants.ServerProperties;
import com.runwaysdk.constants.SingleActorInfo;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
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
import com.runwaysdk.dataaccess.io.instance.InstanceImporterUnzipper;
import com.runwaysdk.dataaccess.metadata.MdAttributeConcreteDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdClassDAO;
import com.runwaysdk.dataaccess.metadata.MdTypeDAO;
import com.runwaysdk.query.BusinessDAOQuery;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.OR;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.system.gis.geo.AllowedIn;
import com.runwaysdk.system.gis.geo.GeoEntity;
import com.runwaysdk.system.gis.geo.GeoNode;
import com.runwaysdk.system.gis.geo.GeoNodeGeometry;
import com.runwaysdk.system.gis.geo.LocatedIn;
import com.runwaysdk.system.gis.geo.Universal;
import com.runwaysdk.system.metadata.MdAttribute;
import com.runwaysdk.system.metadata.MdAttributeTermQuery;
import com.runwaysdk.system.metadata.MdClass;
import com.runwaysdk.system.metadata.MdClassQuery;

public class GeoprismImportPlugin implements ImportPluginIF
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
      MappableClass mClass = (MappableClass) context.getObject(MappableClass.CLASS);
      String type = mClass.getWrappedMdClass().definesType();

      String attribute = attributes.getValue(ATTRIBUTE);

      String key = type + "." + attribute;
      GeoNode node = GeoNode.getByKey(key);

      if (node == null)
      {
        String[] search_tags = { "geoEntityNode", "geoEntityGeometry" };
        String cause = "GeoNode:" + key;

        SearchHandler.searchEntity(this.getManager(), search_tags, XMLTags.NAME_ATTRIBUTE, type, cause);

        node = GeoNode.getByKey(key);
      }

      MappableClassGeoNodeQuery query = new MappableClassGeoNodeQuery(new QueryFactory());
      query.WHERE(query.getParent().EQ(mClass));
      query.AND(query.getChild().EQ(node));

      if (query.getCount() == 0)
      {
        mClass.addGeoNode(node).apply();
      }
    }
  }

  private static class UniversalHandler extends TagHandler
  {
    protected static final String KEY = "key";

    public UniversalHandler(ImportManager manager)
    {
      super(manager);
    }

    @Override
    public void onStartElement(String localName, Attributes attributes, TagContext context)
    {
      MappableClass mClass = (MappableClass) context.getObject(MappableClass.CLASS);

      String key = attributes.getValue(KEY);

      Universal universal = Universal.getByKey(key);

      ClassUniversalQuery query = new ClassUniversalQuery(new QueryFactory());
      query.WHERE(query.getParent().EQ(mClass));
      query.AND(query.getChild().EQ(universal));

      if (query.getCount() == 0)
      {
        mClass.addUniversal(universal).apply();
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

    private static final String ATTRIBUTE_TAG = "attribute";

    public TypeHandler(ImportManager manager)
    {
      super(manager);

      this.addHandler(ATTRIBUTE_TAG, new TypeAttributeHandler(manager));
    }

    @Override
    public void onStartElement(String localName, Attributes attributes, TagContext context)
    {
      String type = attributes.getValue(TYPE);
      Integer index = new Integer(attributes.getValue(INDEX));

      DashboardTypeInfo info = new DashboardTypeInfo(type, index);

      MappableClass mClass = this.getMappableClass(type);

      List<? extends GeoNode> nodes = mClass.getAllGeoNode().getAll();

      for (GeoNode node : nodes)
      {
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

      context.setObject(DashboardTypeInfo.class.getName(), info);

    }

    private MappableClass getMappableClass(String type)
    {
      MappableClass mClass = null;

      if (type != null)
      {
        mClass = MappableClass.getMappableClass(type);

        if (mClass == null)
        {
          String[] search_tags = new String[] { "mappableClass" };
          SearchHandler.searchEntity(this.getManager(), search_tags, XMLTags.TYPE_ATTRIBUTE, type, "Type:" + type);
        }

        mClass = MappableClass.getMappableClass(type);

      }

      if (mClass == null)
      {
        throw new ProgrammingErrorException("Unable to find Mappable Class metadata for type [" + type + "]");
      }

      return mClass;
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

    @Override
    public void onEndElement(String uri, String localName, String name, TagContext context)
    {
      Dashboard dashboard = (Dashboard) context.getObject(Dashboard.CLASS);
      DashboardTypeInfo info = (DashboardTypeInfo) context.getObject(DashboardTypeInfo.class.getName());

      MdClassDAOIF mdClass = MdClassDAO.getMdClassDAO(info.getType());

      DashboardBuilder builder = new DashboardBuilder();
      builder.build(dashboard, mdClass, info);
    }
  }

  private static class DashboardHandler extends TagHandler
  {
    private static final String NAME      = "name";

    private static final String LABEL     = "label";

    private static final String TYPE_TAG  = "type";

    private static final String REMOVABLE = "removable";

    public DashboardHandler(ImportManager manager)
    {
      super(manager);

      this.addHandler(TYPE_TAG, new TypeHandler(manager));
    }

    @Override
    public void onStartElement(String localName, Attributes attributes, TagContext context)
    {
      String name = attributes.getValue(NAME);
      String label = attributes.getValue(LABEL);
      String removable = attributes.getValue(REMOVABLE);

      Dashboard dashboard = this.getOrCreateDashboard(name);

      if (label != null && label.length() > 0)
      {
        dashboard.getDisplayLabel().setDefaultValue(label);
      }

      if (removable != null && removable.length() > 0)
      {
        dashboard.setRemovable(new Boolean(removable));
      }

      dashboard.apply();

      context.setObject(Dashboard.CLASS, dashboard);
    }

    private Dashboard getOrCreateDashboard(String name)
    {
      if (this.getManager().isUpdateState() || this.getManager().isCreateOrUpdateState())
      {
        Dashboard dashboard = this.getDashboard(name);

        if (dashboard != null)
        {
          dashboard.lock();

          return dashboard;
        }
        else if (this.getManager().isUpdateState())
        {
          String message = "Unable to find a [" + Dashboard.CLASS + "] with a name of [" + name + "]";
          MdClassDAOIF mdClass = MdClassDAO.getMdClassDAO(Dashboard.CLASS);

          throw new DataNotFoundException(message, mdClass);
        }
      }

      Dashboard dashboard = new Dashboard();
      dashboard.setName(name);
      return dashboard;
    }

    private Dashboard getDashboard(String name)
    {
      DashboardQuery query = new DashboardQuery(new QueryFactory());
      query.WHERE(query.getName().EQ(name));
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

  private static class MappableClassHandler extends TagHandler
  {
    private static final String UNIVERSAL_TAG = "universal";

    private static final String GEONODE_TAG   = "geoNode";

    private static final String TYPE          = "type";

    public MappableClassHandler(ImportManager manager)
    {
      super(manager);

      this.addHandler(UNIVERSAL_TAG, new UniversalHandler(manager));
      this.addHandler(GEONODE_TAG, new TypeNodeHandler(manager));
    }

    @Override
    public void onStartElement(String localName, Attributes attributes, TagContext context)
    {
      String type = attributes.getValue(TYPE);

      if (type != null)
      {
        if (!MdTypeDAO.isDefined(type))
        {
          String[] search_tags = XMLTags.TYPE_TAGS;
          SearchHandler.searchEntity(this.getManager(), search_tags, XMLTags.NAME_ATTRIBUTE, type, "DataSet:" + type);
        }

        MdClassDAOIF mdClass = MdClassDAO.getMdClassDAO(type);

        MappableClass mClass = this.getOrCreateMappableClass(mdClass);
        mClass.apply();

        context.setObject(MappableClass.CLASS, mClass);
      }
    }

    private MappableClass getOrCreateMappableClass(MdClassDAOIF _mdClass)
    {
      if (this.getManager().isUpdateState() || this.getManager().isCreateOrUpdateState())
      {
        MappableClass mClass = MappableClass.getMappableClass(_mdClass);

        if (mClass != null)
        {
          mClass.lock();

          return mClass;
        }
        else if (this.getManager().isUpdateState())
        {
          String message = "Unable to find a [" + MappableClass.CLASS + "] with a type of [" + _mdClass.definesType() + "]";

          throw new DataNotFoundException(message, MdClassDAO.getMdClassDAO(MappableClass.CLASS));
        }
      }

      MdClass mdClass = MdClass.get(_mdClass.getId());

      MappableClass mClass = new MappableClass();
      mClass.setWrappedMdClass(mdClass);
      mClass.apply();

      OIterator<? extends MdAttribute> mdAttributes = mdClass.getAllAttribute();

      try
      {
        for (MdAttribute mdAttribute : mdAttributes)
        {
          MappableAttribute mAttribute = new MappableAttribute();
          mAttribute.setWrappedMdAttribute(mdAttribute);
          mAttribute.setAggregatable(true);
          mAttribute.apply();
        }
      }
      finally
      {
        mdAttributes.close();
      }

      return mClass;
    }

  }

  private static class UnzipperTaskHandler extends TagHandler
  {
    private static final String PATH = "path";

    public UnzipperTaskHandler(ImportManager manager)
    {
      super(manager);
    }

    @Override
    public void onStartElement(String localName, Attributes attributes, TagContext context)
    {
      URI uri = this.getManager().getStreamSource().toURI();
      File xml = new File(uri);

      String path = attributes.getValue(PATH);

      File file = new File(xml.getParentFile(), path);

      if (file.exists())
      {
        InstanceImporterUnzipper.processZipDir(file.getAbsolutePath());
      }
    }
  }

  private static class LocationTaskHandler extends TagHandler
  {
    public LocationTaskHandler(ImportManager manager)
    {
      super(manager);
    }

    @Override
    public void onStartElement(String localName, Attributes attributes, TagContext context)
    {
      LocalProperties.setSkipCodeGenAndCompile(true);

      ProjectDataConfiguration configuration = new ProjectDataConfiguration();

      XMLEndpoint endpoint = this.getEndpoint();

      LocationImporter importer = new XMLLocationImporter(endpoint, false);
      importer.loadProjectData(configuration);
    }

    private XMLEndpoint getEndpoint()
    {
      String cacheDirectory = System.getProperty("endpoint.cache");

      if (cacheDirectory != null)
      {
        return new CachedEndpoint(new AmazonEndpoint(), new File(cacheDirectory));
      }

      return new AmazonEndpoint();

      // return new LocalEndpoint(new File("/home/terraframe/Documents/geoprism/DSEDP/cache"));
    }
  }

  private static class InitializeTaskHandler extends TagHandler
  {
    public InitializeTaskHandler(ImportManager manager)
    {
      super(manager);
    }

    @Override
    public void onStartElement(String localName, Attributes attributes, TagContext context)
    {
      Universal.getStrategy().initialize(AllowedIn.CLASS);

      GeoEntity.getStrategy().initialize(LocatedIn.CLASS);

      Classifier.getStrategy().initialize(ClassifierIsARelationship.CLASS);
    }
  }

  private static class PatchMappableClassTaskHandler extends TagHandler
  {
    public PatchMappableClassTaskHandler(ImportManager manager)
    {
      super(manager);
    }

    @Override
    public void onStartElement(String localName, Attributes attributes, TagContext context)
    {
      QueryFactory factory = new QueryFactory();

      MappableClassQuery mClassQuery = new MappableClassQuery(factory);

      OIterator<? extends MappableClass> it = mClassQuery.getIterator();

      try
      {
        while (it.hasNext())
        {
          MappableClass mClass = it.next();
          MdClass mdClass = mClass.getWrappedMdClass();

          OIterator<? extends MdAttribute> mdAttributes = mdClass.getAllAttribute();

          try
          {
            for (MdAttribute mdAttribute : mdAttributes)
            {
              MappableAttribute mAttribute = new MappableAttribute();
              mAttribute.setWrappedMdAttribute(mdAttribute);
              mAttribute.setAggregatable(true);
              mAttribute.apply();
            }
          }
          finally
          {
            mdAttributes.close();
          }
        }
      }
      finally
      {
        it.close();
      }

    }
  }

  private static class PatchCategoryTaskHandler extends TagHandler
  {
    public PatchCategoryTaskHandler(ImportManager manager)
    {
      super(manager);
    }

    @Override
    public void onStartElement(String localName, Attributes attributes, TagContext context)
    {
      QueryFactory factory = new QueryFactory();

      MappableClassQuery mcQuery = new MappableClassQuery(factory);

      MdClassQuery mdClassQuery = new MdClassQuery(factory);
      mdClassQuery.WHERE(mdClassQuery.EQ(mcQuery.getWrappedMdClass()));

      MdAttributeTermQuery mdAttributeTermQuery = new MdAttributeTermQuery(factory);
      mdAttributeTermQuery.WHERE(mdAttributeTermQuery.definingClass(mdClassQuery));

      ClassifierTermAttributeRootQuery rootQuery = new ClassifierTermAttributeRootQuery(factory);
      rootQuery.WHERE(rootQuery.getParent().EQ(mdAttributeTermQuery));

      ClassifierQuery classifierRootQ = new ClassifierQuery(factory);
      classifierRootQ.WHERE(classifierRootQ.classifierTermAttributeRoots(rootQuery));
      classifierRootQ.AND(OR.get(classifierRootQ.getCategory().EQ(false), classifierRootQ.getCategory().EQ((Boolean) null)));

      OIterator<? extends Classifier> iterator = classifierRootQ.getIterator();

      try
      {
        while (iterator.hasNext())
        {
          Classifier classifier = iterator.next();

          System.out.println("Upgrading classifier [" + classifier.getDisplayLabel().getValue() + "]");

          classifier.lock();
          classifier.setCategory(true);
          classifier.apply();
        }
      }
      finally
      {
        iterator.close();
      }
    }
  }

  private static class PatchLockedByTypeTaskHandler extends TagHandler
  {
    public PatchLockedByTypeTaskHandler(ImportManager manager)
    {
      super(manager);
    }

    @Override
    public void onStartElement(String localName, Attributes attributes, TagContext context)
    {
      ServerProperties.setAllowModificationOfMdAttribute(true);
      ServerProperties.setIgnoreSiteMaster(true);

      try
      {
        /*
         * Update all lockedBy fields to type SingleActor
         */
        MdBusinessDAOIF mdSingleActor = MdBusinessDAO.getMdBusinessDAO(SingleActorInfo.CLASS);

        QueryFactory factory = new QueryFactory();

        BusinessDAOQuery query = factory.businessDAOQuery(MdAttributeReferenceInfo.CLASS);
        query.WHERE(query.aCharacter(MdAttributeConcreteInfo.NAME).EQ(MdElementInfo.LOCKED_BY));
        query.AND(query.aReference(MdAttributeReferenceInfo.REF_MD_ENTITY).NE(mdSingleActor));

        OIterator<BusinessDAOIF> iterator = query.getIterator();

        while (iterator.hasNext())
        {
          MdAttributeConcreteDAO lockedBy = (MdAttributeConcreteDAO) iterator.next().getBusinessDAO();
          lockedBy.setValue(MdAttributeReferenceInfo.REF_MD_ENTITY, mdSingleActor.getId());
          lockedBy.apply();
        }

        /*
         * Update the attribute type of DashboardState.geoprismUser to SingleActor
         */
        MdBusinessDAOIF mdBusiness = MdBusinessDAO.getMdBusinessDAO(DashboardState.CLASS);

        MdAttributeConcreteDAOIF mdAttributeReference = mdBusiness.definesAttribute(DashboardState.GEOPRISMUSER);
        
        if (mdAttributeReference != null)
        {
          MdAttributeConcreteDAO lockedBy = (MdAttributeConcreteDAO) mdAttributeReference.getBusinessDAO();
          lockedBy.setValue(MdAttributeReferenceInfo.REF_MD_ENTITY, mdSingleActor.getId());
          lockedBy.apply();
        }
      }
      finally
      {
        ServerProperties.setAllowModificationOfMdAttribute(false);
        ServerProperties.setIgnoreSiteMaster(false);
      }
    }
  }

  private static class PluginHandlerFactory extends HandlerFactory implements HandlerFactoryIF
  {
    private static final String DASHBOARD_TAG                 = "dashboard";

    private static final String MAPPABLE_CLASS_TAG            = "mappableClass";

    private static final String UNZIPPER_TASK_TAG             = "unzipperTask";

    private static final String LOCATION_TASK_TAG             = "locationTask";

    private static final String INITIALIZE_TASK_TAG           = "initializeTask";

    private static final String PATCH_MAPPABLE_CLASS_TASK_TAG = "patchMappableClassTask";

    private static final String PATCH_CATEGORY_TASK_TAG       = "patchCategoryTask";

    private static final String PATCH_LOCKED_BY_TASK_TAG      = "patchLockedByTask";

    public PluginHandlerFactory(ImportManager manager)
    {
      this.addHandler(DASHBOARD_TAG, new DashboardHandler(manager));
      this.addHandler(MAPPABLE_CLASS_TAG, new MappableClassHandler(manager));
      this.addHandler(UNZIPPER_TASK_TAG, new UnzipperTaskHandler(manager));
      this.addHandler(LOCATION_TASK_TAG, new LocationTaskHandler(manager));
      this.addHandler(INITIALIZE_TASK_TAG, new InitializeTaskHandler(manager));
      this.addHandler(PATCH_MAPPABLE_CLASS_TASK_TAG, new PatchMappableClassTaskHandler(manager));
      this.addHandler(PATCH_CATEGORY_TASK_TAG, new PatchCategoryTaskHandler(manager));
      this.addHandler(PATCH_LOCKED_BY_TASK_TAG, new PatchLockedByTypeTaskHandler(manager));
    }
  }

  @Override
  public void register(ImportManager manager)
  {
    manager.register(CreateHandler.class.getName(), new PluginHandlerFactory(manager));
    manager.register(CreateOrUpdateHandler.class.getName(), new PluginHandlerFactory(manager));
    manager.register(UpdateHandler.class.getName(), new PluginHandlerFactory(manager));
  }

  @Override
  public String getModuleIdentifier()
  {
    return "Geoprism";
  }

}
