/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Geoprism(tm).
 *
 * Geoprism(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Geoprism(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Geoprism(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism.data.importer;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.geotools.data.FeatureSource;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeType;

import com.runwaysdk.ProblemException;
import com.runwaysdk.ProblemIF;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.gis.geometry.GeometryHelper;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.OR;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.session.Request;
import com.runwaysdk.session.RequestState;
import com.runwaysdk.system.gis.geo.AllowedIn;
import com.runwaysdk.system.gis.geo.GeoEntity;
import com.runwaysdk.system.gis.geo.GeoEntityQuery;
import com.runwaysdk.system.gis.geo.LocatedIn;
import com.runwaysdk.system.gis.geo.Synonym;
import com.runwaysdk.system.gis.geo.Universal;
import com.vividsolutions.jts.geom.Geometry;

import net.geoprism.localization.LocalizationFacade;

/**
 * Class responsible for importing GeoEntity definitions from a shapefile.
 * 
 * @author Justin Smethie
 */
public class GeoEntityShapefileImporter extends TaskObservable
{
  /**
   * URL of the file being imported
   */
  private URL                         url;

  /**
   * Name of shapefile attribute which is used to derive the type of the entity
   */
  private ShapefileFunction           type;

  /**
   * Name of shapefile attribute which is used to derive the entity name
   */
  private ShapefileFunction           name;

  /**
   * Optional name of the shapefile attribute which is used to derive the geo
   * oid. If this value is null then the geo oid is auto-generated.
   */
  private ShapefileFunction           oid;

  /**
   * Optional name of the shapefile attribute which is used to specify the
   * entity name or geo oid of the parent entity for the entity being imported.
   * If this value is null than the parent is assumed to be Earth.
   */
  private ShapefileMultivalueFunction parent;

  /**
   * Optional name of the shapefile attribute which is used to restrict the
   * parent to a specific universal type when searching for the parent entity.
   * If this value is null than the search does not restrict by type.
   */
  private ShapefileFunction           parentType;

  /**
   * Name of the default universal
   */
  private String                      universalId;

  /**
   * Loaded default universal
   */
  private Universal                   universal;

  /**
   * Helper used to convert Geometry data to Point and MultiPolygon data.
   */
  private GeometryHelper              helper;

  /**
   * Cached instance of Root.
   */
  private GeoEntity                   root;

  /**
   * Map between a feature oid and its geo entity
   */
  private Map<String, String>         entityIdMap;

  /**
   * Map between the parent feature value and the resolved parent GeoEntity
   */
  private Map<String, GeoEntity>      parentCache;

  /**
   * Synonym for the entity
   */
  private List<ShapefileFunction>     synonyms;

  /**
   * @param url
   *          URL of the shapefile
   */
  public GeoEntityShapefileImporter(URL url)
  {
    super();

    this.url = url;
    this.helper = new GeometryHelper();
    this.root = GeoEntity.getRoot();
    this.entityIdMap = new HashMap<String, String>();
    this.parentCache = new HashMap<String, GeoEntity>();
    this.synonyms = new ArrayList<ShapefileFunction>();
  }

  public GeoEntityShapefileImporter(File file) throws MalformedURLException
  {
    this(file.toURI().toURL());
  }

  public ShapefileFunction getType()
  {
    return type;
  }

  public void setType(ShapefileFunction type)
  {
    this.type = type;
  }

  public void setType(String type)
  {
    this.setType(new BasicColumnFunction(type));
  }

  public void setUniversalId(String universalId)
  {
    this.universalId = universalId;
  }

  public String getUniversalId()
  {
    return universalId;
  }

  public ShapefileFunction getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.setName(new BasicColumnFunction(name));
  }

  public void setName(ShapefileFunction name)
  {
    this.name = name;
  }

  public ShapefileFunction getOid()
  {
    return oid;
  }

  public void setId(String oid)
  {
    this.setId(new BasicColumnFunction(oid));
  }

  public void setId(ShapefileFunction oid)
  {
    this.oid = oid;
  }

  public ShapefileMultivalueFunction getParent()
  {
    return parent;
  }

  public void setParent(String parent)
  {
    this.setParent(new SingleValueAdapter(new BasicColumnFunction(parent)));
  }

  public void setParent(ShapefileMultivalueFunction parent)
  {
    this.parent = parent;
  }

  public ShapefileFunction getParentType()
  {
    return parentType;
  }

  public void setParentType(String parentType)
  {
    this.setParentType(new BasicColumnFunction(parentType));
  }

  public void setParentType(ShapefileFunction parentType)
  {
    this.parentType = parentType;
  }

  public List<ShapefileFunction> getSynonms()
  {
    return synonyms;
  }

  public void setSynonym(String synonym)
  {
    this.synonyms.add(new BasicColumnFunction(synonym));
  }

  public void setSynonym(ShapefileFunction synonym)
  {
    this.synonyms.add(synonym);
  }

  /**
   * @return Map between Shapefile Feature OID and the imported Entity oid.
   */
  public Map<String, String> getEntityIdMap()
  {
    return entityIdMap;
  }

  @Request
  public void run(GISImportLoggerIF logger) throws InvocationTargetException
  {
    try
    {
      try
      {
        this.fireStart();

        // If the strategy has not be initialized then do so
        if (!GeoEntity.getStrategy().isInitialized())
        {
          GeoEntity.getStrategy().initialize(LocatedIn.CLASS);
        }

        if (!Universal.getStrategy().isInitialized())
        {
          Universal.getStrategy().initialize(AllowedIn.CLASS);
        }

        this.createEntities(logger);

        this.fireStartTask(LocalizationFacade.getFromBundles("REBUILD_ALL_PATHS"), -1);

        // Rebuild the all paths table
        // GeoEntity.getStrategy().reinitialize(LocatedIn.CLASS);

        this.fireTaskDone(true);
      }
      finally
      {
        logger.close();
      }
    }
    catch (RuntimeException e)
    {
      this.fireTaskDone(false);

      throw e;
    }
    catch (Exception e)
    {
      this.fireTaskDone(false);

      throw new InvocationTargetException(e);
    }
  }

  /**
   * Imports the entities from the shapefile
   * 
   * @param writer
   *          Log file writer
   * @throws InvocationTargetException
   */
  @Transaction
  private void createEntities(GISImportLoggerIF logger) throws InvocationTargetException
  {
    try
    {
      ShapefileDataStore store = new ShapefileDataStore(url);

      try
      {
        String[] typeNames = store.getTypeNames();

        for (String typeName : typeNames)
        {
          FeatureSource<SimpleFeatureType, SimpleFeature> source = store.getFeatureSource(typeName);

          // Display the geo entity information about each row
          FeatureCollection<SimpleFeatureType, SimpleFeature> collection = source.getFeatures();

          FeatureIterator<SimpleFeature> iterator = collection.features();

          this.fireStartTask(LocalizationFacade.getFromBundles("IMPORT_ENTITIES"), collection.size());

          try
          {
            while (iterator.hasNext())
            {
              SimpleFeature feature = iterator.next();

              // try
              // {
              importEntity(feature);
              // }
              // catch (Exception e)
              // {
              // logger.log(feature.getOid(), e);
              // }

              this.fireTaskProgress(1);
            }
          }
          finally
          {
            iterator.close();
          }
        }
      }
      finally
      {
        store.dispose();
      }
    }
    catch (RuntimeException e)
    {
      throw e;
    }
    catch (Exception e)
    {
      throw new InvocationTargetException(e);
    }
  }

  /**
   * Imports a GeoEntity based on the given SimpleFeature. If a matching
   * GeoEntity already exists then it is simply updated.
   * 
   * @param feature
   * @throws Exception
   */
  private void importEntity(SimpleFeature feature) throws Exception
  {
    SimpleFeatureRow row = new SimpleFeatureRow(feature);
    
    String geoId = this.getGeoId(row);

    GeoEntity entity;
    boolean isNew = false;

    if (geoId != null && geoId.length() > 0)
    {
      try
      {
        // try an update
        isNew = false;
        entity = GeoEntity.getByKey(geoId);
      }
      catch (DataNotFoundException e)
      {
        // create a new entity
        isNew = true;
        entity = new GeoEntity();
      }
    }
    else
    {
      // create a new entity
      isNew = true;
      entity = new GeoEntity();
    }

    Geometry geometry = (Geometry) feature.getDefaultGeometry();
    String entityName = this.getName(row);

    if (entityName != null)
    {
      Universal universal = this.getUniversal(row);

      entity.setWkt(geometry.toText());
      entity.setGeoPoint(helper.getGeoPoint(geometry));
      entity.setGeoMultiPolygon(helper.getGeoMultiPolygon(geometry));
      entity.getDisplayLabel().setValue(entityName);
      entity.setUniversal(universal);
      entity.setGeoId(geoId);

      try
      {
        entity.applyInternal(false);

        Set<String> set = new TreeSet<String>();

        for (ShapefileFunction synonym : this.synonyms)
        {
          String value = (String) synonym.getValue(row);

          if (value != null && value.length() > 0)
          {
            set.add(value);
          }
        }

        for (String synonymName : set)
        {
          Synonym syn = new Synonym();
          syn.getDisplayLabel().setDefaultValue(synonymName);
          syn.getDisplayLabel().setValue(synonymName);
          Synonym.create(syn, entity.getOid());
        }
      }
      catch (Exception e)
      {
        throw new RuntimeException(e);
      }

      if (isNew)
      {
        List<GeoEntity> parents = this.getParent(row);

        for (GeoEntity parent : parents)
        {
          entity.addLink(parent, LocatedIn.CLASS);
        }
      }

      // We must ensure that any problems created during the transaction are
      // logged now instead of when the request returns. As such, if any
      // problems
      // exist immediately throw a ProblemException so that normal exception
      // handling can occur.
      List<ProblemIF> problems = RequestState.getProblemsInCurrentRequest();

      if (problems.size() != 0)
      {
        throw new ProblemException(null, problems);
      }

      // The entity was succesfully applied without any problems or exceptions
      this.entityIdMap.put(feature.getID(), entity.getOid());
    }
  }

  /**
   * @return A list of attributes defined in the shapefile
   */
  public List<String> getAttributes()
  {
    List<String> attributes = new LinkedList<String>();

    try
    {
      // get feature results
      ShapefileDataStore store = new ShapefileDataStore(url);

      try
      {
        String[] typeNames = store.getTypeNames();

        for (String name : typeNames)
        {
          FeatureSource<SimpleFeatureType, SimpleFeature> source = store.getFeatureSource(name);

          // print out a feature type header and wait for user input
          SimpleFeatureType schema = source.getSchema();

          List<AttributeType> types = schema.getTypes();

          for (AttributeType type : types)
          {
            // Filter out the geometry columns
            if (!Geometry.class.isAssignableFrom(type.getBinding()))
            {
              attributes.add(type.getName().toString());
            }
          }
        }
      }
      finally
      {
        store.dispose();
      }
    }
    catch (RuntimeException e)
    {
      throw e;
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }

    return attributes;
  }

  @Request
  private Universal getUniversal(FeatureRow feature)
  {
    if (this.type != null)
    {
      String label = (String) this.type.getValue(feature);

      if (label != null && label.trim().length() > 0)
      {
        Universal universal = Universal.getByKey(label);

        if (universal != null)
        {
          return universal;
        }
        else
        {
          throw new UnknownUniversalException(label);
        }
      }
    }

    if (this.universal == null)
    {
      this.universal = Universal.get(this.universalId);
    }

    return this.universal;
  }

  /**
   * Returns the entity as defined by the 'parent' and 'parentType' attributes
   * of the given feature. If an entity is not found then Earth is returned by
   * default. The 'parent' value of the feature must define an entity name or a
   * geo oid. The 'parentType' value of the feature must define the localized
   * display label of the universal.
   * 
   * @param feature
   *          Shapefile feature used to determine the parent
   * @return Parent entity
   */
  private List<GeoEntity> getParent(FeatureRow feature)
  {
    List<GeoEntity> parents = new LinkedList<GeoEntity>();

    if (this.parent != null && this.parentType != null)
    {
      List<String> _entityNames = this.parent.getValue(feature);
      String _type = (String) this.parentType.getValue(feature);

      if (_type != null)
      {
        String type = _type.toString();

        for (String entityName : _entityNames)
        {
          GeoEntity parent = GeoEntityShapefileImporter.getByEntityNameAndUniversal(entityName, type);

          if (parent != null)
          {
            parents.add(parent);
          }
        }
      }
    }

    if (this.parent != null)
    {
      List<String> _entityNames = this.parent.getValue(feature);

      for (String entityName : _entityNames)
      {
        if (this.parentCache.containsKey(entityName))
        {
          parents.add(this.parentCache.get(entityName));
        }
        else
        {
          GeoEntity parent = GeoEntityShapefileImporter.getByEntityName(entityName);

          if (parent != null)
          {
            this.parentCache.put(entityName, parent);

            parents.add(this.parentCache.get(entityName));
          }
          else
          {
            System.out.println("Unable to find parent [" + entityName + "] for type [" + this.universal.getKey() + "]");
          }
        }
      }
    }

    if (parents.size() == 0)
    {
      parents.add(this.root);
    }

    return parents;
  }

  /**
   * @param feature
   *          Shapefile feature
   * 
   * @return The geoId as defined by the 'oid' attribute on the feature. If the
   *         geoId is null then a blank geoId is returned.
   */
  private String getGeoId(FeatureRow feature)
  {
    if (this.oid != null)
    {
      Object geoId = this.oid.getValue(feature);

      if (geoId != null)
      {
        return geoId.toString();
      }
    }

    return "";
  }

  /**
   * @param feature
   * @return The entityName as defined by the 'name' attribute of the feature
   */
  private String getName(FeatureRow feature)
  {
    Object attribute = this.name.getValue(feature);

    if (attribute != null)
    {
      return attribute.toString();
    }

    return null;
  }

  /**
   * @param entityName
   *          Entity name or geo Id.
   * @param type
   *          Localized display label of the entity type
   * @return GeoEntity which satisfies the search criteria, or null of no
   *         entities meet the criteria.
   */
  @Request
  public static GeoEntity getByEntityNameAndUniversal(String entityName, String type)
  {
    QueryFactory factory = new QueryFactory();
    GeoEntityQuery entityQuery = new GeoEntityQuery(factory);

    entityQuery.WHERE(OR.get(entityQuery.getDisplayLabel().localize().EQ(entityName), entityQuery.getGeoId().EQ(entityName)));
    entityQuery.AND(OR.get(entityQuery.getUniversal().getDisplayLabel().localize().EQ(type)));

    long count = entityQuery.getCount();

    if (count > 1)
    {
      String message = "Multiple geo entities of the name [" + entityName + "] and type [" + type + "].";

      throw new AmbigiousGeoEntityException(message, entityName, type);
    }

    OIterator<? extends GeoEntity> it = entityQuery.getIterator();

    try
    {
      if (it.hasNext())
      {
        return it.next();
      }

      return null;
    }
    finally
    {
      it.close();
    }
  }

  /**
   * @param entityName
   *          Entity name or geo Id.
   * @return GeoEntity which satisfies the search criteria, or null of no
   *         entities meet the criteria.
   */
  @Request
  public static GeoEntity getByEntityName(String entityName)
  {
    GeoEntityQuery query = new GeoEntityQuery(new QueryFactory());
    query.WHERE(query.getDisplayLabel().localize().EQ(entityName));
    query.OR(query.getGeoId().EQ(entityName));

    long count = query.getCount();

    if (count > 1)
    {
      String message = "Multiple geo entities of the name [" + entityName + "].";

      throw new AmbigiousGeoEntityException(message, entityName, null);
    }

    OIterator<? extends GeoEntity> it = query.getIterator();

    try
    {
      if (it.hasNext())
      {
        return it.next();
      }

      return null;
    }
    finally
    {
      it.close();
    }
  }
}
