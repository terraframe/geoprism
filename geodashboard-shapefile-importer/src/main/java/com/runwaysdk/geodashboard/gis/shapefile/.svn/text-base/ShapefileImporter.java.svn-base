package dss.vector.solutions.gis.shapefile;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
import com.runwaysdk.generation.loader.LoaderDecorator;
import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.query.F;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.OR;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.session.Request;
import com.runwaysdk.session.RequestState;
import com.runwaysdk.system.metadata.MdBusinessQuery;
import com.vividsolutions.jts.geom.Geometry;

import dss.vector.solutions.AmbigiousGeoEntityException;
import dss.vector.solutions.AmbigiousTermException;
import dss.vector.solutions.LocalProperty;
import dss.vector.solutions.geo.GeoHierarchy;
import dss.vector.solutions.geo.GeoHierarchyQuery;
import dss.vector.solutions.geo.GeoHierarchyView;
import dss.vector.solutions.geo.LocatedIn;
import dss.vector.solutions.geo.generated.Earth;
import dss.vector.solutions.geo.generated.GeoEntity;
import dss.vector.solutions.geo.generated.GeoEntityQuery;
import dss.vector.solutions.gis.Localizer;
import dss.vector.solutions.gis.GISImportLoggerIF;
import dss.vector.solutions.gis.TaskObservable;
import dss.vector.solutions.ontology.AllPathsQuery;
import dss.vector.solutions.ontology.Term;
import dss.vector.solutions.ontology.TermQuery;
import dss.vector.solutions.util.GeometryHelper;

/**
 * Class responsible for importing GeoEntity definitions from a shapefile.
 * 
 * @author Justin Smethie
 */
public class ShapefileImporter extends TaskObservable implements Reloadable
{
  /**
   * URL of the file being imported
   */
  private URL                 url;

  /**
   * Class which extends GeoEntity which is being imported
   */
  private Class<GeoEntity>    clazz;

  /**
   * Name of shapefile attribute which is used to derive the type of the entity
   */
  private String              type;

  /**
   * Name of shapefile attribute which is used to derive the subtype of the
   * entity
   */
  private String              subtype;

  /**
   * Name of shapefile attribute which is used to derive the entity name
   */
  private String              name;

  /**
   * Optional name of the shapefile attribute which is used to derive the geo
   * id. If this value is null then the geo id is auto-generated.
   */
  private String              id;

  /**
   * Optional name of the shapefile attribute which is used to specify the
   * entity name or geo id of the parent entity for the entity being imported.
   * If this value is null than the parent is assumed to be Earth.
   */
  private String              parent;

  /**
   * Optional name of the shapefile attribute which is used to restrict the
   * parent to a specific universal type when searching for the parent entity.
   * If this value is null than the search does not restrict by type.
   */
  private String              parentType;

  /**
   * Helper used to convert Geometry data to Point and MultiPolygon data.
   */
  private GeometryHelper      helper;

  /**
   * Cached instance of Earth.
   */
  private Earth               earth;

  /**
   * Map between a feature id and its geo entity
   */
  private Map<String, String> entityIdMap;

  /**
   * @param url
   *          URL of the shapefile
   */
  public ShapefileImporter(URL url)
  {
    super();

    this.url = url;
    this.helper = new GeometryHelper();
    this.earth = Earth.getEarthInstance();
    this.entityIdMap = new HashMap<String, String>();
  }

  public ShapefileImporter(File file) throws MalformedURLException
  {
    this(file.toURI().toURL());
  }

  public String getType()
  {
    return type;
  }

  public void setType(String type)
  {
    this.type = type;
  }

  public String getSubtype()
  {
    return subtype;
  }

  public void setSubtype(String subtype)
  {
    this.subtype = subtype;
  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public String getId()
  {
    return id;
  }

  public void setId(String id)
  {
    this.id = id;
  }

  public String getParent()
  {
    return parent;
  }

  public void setParent(String parent)
  {
    this.parent = parent;
  }

  public String getParentType()
  {
    return parentType;
  }

  public void setParentType(String parentType)
  {
    this.parentType = parentType;
  }

  /**
   * @return Map between Shapefile Feature ID and the imported Entity id.
   */
  public Map<String, String> getEntityIdMap()
  {
    return entityIdMap;
  }

  /**
   * @param type
   *          Fully qualified type of the entities being imported.
   */
  @SuppressWarnings("unchecked")
  public void setUniversal(String type)
  {
    this.clazz = (Class<GeoEntity>) LoaderDecorator.load(type);
  }

  @Request
  public void run(GISImportLoggerIF logger) throws InvocationTargetException
  {
    try
    {
      try
      {
        this.fireStart();

        this.createEntities(logger);

        this.fireStartTask(Localizer.getMessage("REBUILD_ALL_PATHS"), -1);

        // Rebuild the all paths table
        GeoEntity.buildAllPathsFast();

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

          this.fireStartTask(Localizer.getMessage("IMPORT_ENTITIES"), collection.size());

          try
          {
            while (iterator.hasNext())
            {
              SimpleFeature feature = iterator.next();

              try
              {
                importEntity(feature);
              }
              catch (Exception e)
              {
                logger.log(feature.getID(), e);
              }

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
  @Transaction
  private void importEntity(SimpleFeature feature) throws Exception
  {
    String geoId = this.getGeoId(feature);

    GeoEntity entity;
    boolean isNew = false;
    try
    {
      // try an update
      isNew = false;
      entity = GeoEntity.getByKey(geoId);
    }
    catch(DataNotFoundException e)
    {
      // create a new entity
      isNew = true;
      entity = this.newInstance(feature);
    }

    Geometry geometry = (Geometry) feature.getDefaultGeometry();
    String entityName = this.getName(feature);
    Term term = this.getSubtype(feature, entity);
    
    entity.setGeoData(geometry.toText());
    entity.setGeoPoint(helper.getGeoPoint(geometry));
    entity.setGeoMultiPolygon(helper.getGeoMultiPolygon(geometry));
    entity.getEntityLabel().setValue(entityName);
    entity.setGeoId(geoId);
    entity.setTerm(term);
    
    entity.apply();
    
    if(isNew)
    {
      GeoEntity parent = this.getParent(feature);
      
      LocatedIn locIn = entity.addLocatedInGeoEntity(parent);
      locIn.applyWithoutCreatingAllPaths();
    }

    // We must ensure that any problems created during the transaction are
    // logged now instead of when the request returns. As such, if any problems
    // exist immediately throw a ProblemException so that normal exception
    // handling can occur.
    List<ProblemIF> problems = RequestState.getProblemsInCurrentRequest();

    if (problems.size() != 0)
    {
      throw new ProblemException(null, problems);
    }

    // The entity was succesfully applied without any problems or exceptions
    this.entityIdMap.put(feature.getID(), entity.getId());
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

  @SuppressWarnings("unchecked")
  @Request
  private Class<GeoEntity> getType(SimpleFeature feature)
  {
    Object label = feature.getAttribute(this.type);

    if (label != null && label.toString().trim().length() > 0)
    {
      GeoHierarchy geoHierarchy = GeoHierarchy.getGeoHierarchyFromLabel(label.toString());

      if (geoHierarchy != null)
      {
        GeoHierarchyView view = geoHierarchy.getViewForGeoHierarchy();

        return (Class<GeoEntity>) LoaderDecorator.load(view.getGeneratedType());
      }

      String msg = Localizer.getMessage("UNKNOWN_GEO_HIERARCHY" + " " + label.toString());
      throw new RuntimeException(msg);
    }

    return clazz;
  }

  /**
   * Returns the entity as defined by the 'parent' and 'parentType' attributes
   * of the given feature. If an entity is not found then Earth is returned by
   * default. The 'parent' value of the feature must define an entity name or a
   * geo id. The 'parentType' value of the feature must define the localized
   * display label of the universal.
   * 
   * @param feature
   *          Shapefile feature used to determine the parent
   * @return Parent entity
   */
  private GeoEntity getParent(SimpleFeature feature)
  {
    if (this.parent != null && this.parentType != null)
    {
      Object _entityName = feature.getAttribute(this.parent);
      Object _type = feature.getAttribute(this.parentType);

      if (_entityName != null && _type != null)
      {

        String type = _type.toString();
        String entityName = _entityName.toString();

        GeoEntity parent = ShapefileImporter.getByEntityNameAndType(entityName, type);

        if (parent != null)
        {
          return parent;
        }
      }
    }

    if (this.parent != null)
    {
      Object _entityName = feature.getAttribute(this.parent);

      if (_entityName != null)
      {
        String entityName = _entityName.toString();

        GeoEntity parent = ShapefileImporter.getByEntityName(entityName);

        if (parent != null)
        {
          return parent;
        }
      }
    }

    return earth;
  }

  /**
   * Returns the subtype as defined by the 'subtype'attributes of the given
   * feature. If an subtype is not found an exception is thrown.
   * 
   * @param feature
   *          Shapefile feature used to determine the subtype
   * @return Subtype of the entity
   */
  private Term getSubtype(SimpleFeature feature, GeoEntity entity)
  {
    if (this.subtype != null)
    {
      Object _subtype = feature.getAttribute(this.subtype);

      if (_subtype != null)
      {
        String termName = _subtype.toString();

        if (termName.length() > 0)
        {
          Term term = ShapefileImporter.getSubtype(termName, entity);

          if (term == null)
          {
            String message = Localizer.getMessage("UNKNOWN_SUBTYPE");
            throw new RuntimeException(message);
          }

          return term;
        }
      }
    }

    return null;
  }

  /**
   * @param feature
   *          Shapefile feature
   * 
   * @return The geoId as defined by the 'id' attribute on the feature. If the
   *         geoId is null than a generated geoId is returned.
   */
  private String getGeoId(SimpleFeature feature)
  {
    if (this.id != null)
    {
      Object geoId = feature.getAttribute(this.id);

      if (geoId != null)
      {
        return geoId.toString();
      }
    }

    return LocalProperty.getNextId();
  }

  /**
   * @param feature
   * @return The entityName as defined by the 'name' attribute of the feature
   */
  private String getName(SimpleFeature feature)
  {
    return feature.getAttribute(this.name).toString();
  }

  /**
   * @param feature
   *          TODO
   * @return A new instance of the desired class which extends GeoEntity.
   * 
   * @throws InstantiationException
   * @throws IllegalAccessException
   */
  private GeoEntity newInstance(SimpleFeature feature) throws InstantiationException, IllegalAccessException
  {
    if (this.type != null)
    {
      return this.getType(feature).newInstance();
    }

    return clazz.newInstance();
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
  public static GeoEntity getByEntityNameAndType(String entityName, String type)
  {
    QueryFactory factory = new QueryFactory();
    GeoEntityQuery entityQuery = new GeoEntityQuery(factory);
    MdBusinessQuery mdBusinessQuery = new MdBusinessQuery(factory);

    entityQuery.WHERE(OR.get(entityQuery.getEntityLabel().localize().EQ(entityName), entityQuery.getGeoId().EQ(entityName)));
    mdBusinessQuery.AND(F.CONCAT(F.CONCAT(mdBusinessQuery.getPackageName(), "."), mdBusinessQuery.getTypeName()).EQ(entityQuery.getType()));
    mdBusinessQuery.AND(mdBusinessQuery.getDisplayLabel().localize().EQ(type));

    long count = entityQuery.getCount();

    if (count > 1)
    {
      String message = "Multiple geo entities of the name [" + entityName + "] and type [" + type + "].";

      AmbigiousGeoEntityException e = new AmbigiousGeoEntityException(message);
      e.setEntityName(entityName);
      e.apply();

      throw e;
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

  public static Term getSubtype(String value, String packageName, String className)
  {
    QueryFactory factory = new QueryFactory();
    TermQuery termQuery = new TermQuery(factory);
    GeoHierarchyQuery hierarchyQuery = new GeoHierarchyQuery(factory);
    AllPathsQuery pathsQuery = new AllPathsQuery(factory);

    hierarchyQuery.WHERE(hierarchyQuery.getGeoEntityClass().getPackageName().EQ(packageName));
    hierarchyQuery.AND(hierarchyQuery.getGeoEntityClass().getTypeName().EQ(className));

    pathsQuery.WHERE(pathsQuery.getParentTerm().EQ(hierarchyQuery.getTerm()));

    termQuery.WHERE(termQuery.getId().EQ(pathsQuery.getChildTerm().getId()));
    termQuery.AND(OR.get(termQuery.getTermId().EQ(value), termQuery.getName().EQ(value), termQuery.getTermDisplayLabel().localize().EQ(value)));

    long count = termQuery.getCount();

    if (count > 1)
    {
      AmbigiousTermException exception = new AmbigiousTermException();
      exception.setTermName(value);
      exception.apply();

      throw exception;
    }

    OIterator<? extends Term> it = termQuery.getIterator();

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

  @Request
  public static Term getSubtype(String value, GeoEntity entity)
  {
    Class<? extends GeoEntity> clazz = entity.getClass();

    return ShapefileImporter.getSubtype(value, clazz.getPackage().getName(), clazz.getSimpleName());

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
    query.WHERE(query.getEntityLabel().localize().EQ(entityName));
    query.OR(query.getGeoId().EQ(entityName));

    long count = query.getCount();

    if (count > 1)
    {
      String message = "Multiple geo entities of the name [" + entityName + "].";

      AmbigiousGeoEntityException e = new AmbigiousGeoEntityException(message);
      e.setEntityName(entityName);
      e.apply();

      throw e;
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
