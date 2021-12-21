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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.FileUtils;
import org.geotools.data.FeatureSource;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;

import com.runwaysdk.business.Business;
import com.runwaysdk.business.BusinessFacade;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeTermDAOIF;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.gis.dataaccess.MdAttributeMultiPolygonDAOIF;
import com.runwaysdk.gis.dataaccess.MdAttributePointDAOIF;
import com.runwaysdk.gis.geometry.GeometryHelper;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.OR;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.session.Request;
import com.runwaysdk.system.gis.geo.GeoEntity;
import com.runwaysdk.system.gis.geo.GeoEntityProblem;
import com.runwaysdk.system.gis.geo.GeoEntityProblemType;
import com.runwaysdk.system.gis.geo.GeoEntityQuery;
import com.runwaysdk.system.gis.geo.LocatedIn;
import com.runwaysdk.system.gis.geo.LocatedInQuery;
import com.runwaysdk.system.gis.geo.SynonymQuery;
import com.runwaysdk.system.gis.geo.Universal;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;

import net.geoprism.KeyGeneratorIF;
import net.geoprism.ontology.Classifier;

/**
 * Class responsible for importing GeoEntity definitions from a shapefile.
 * 
 * @author Justin Smethie
 */
public class ShapefileImporter 
{
  /**
   * Configuration object containing shapefile column to type attribute mapping
   */
  private ShapefileImportConfiguration configuration;

  private GeometryHelper               geometryHelper;

  public ShapefileImporter(ShapefileImportConfiguration configuration)
  {
    this.configuration = configuration;

    this.geometryHelper = new GeometryHelper();
  }

  @Request
  public void run(InputStream iStream) throws InvocationTargetException
  {
    // create a buffer to improve copy performance later.
    byte[] buffer = new byte[2048];

    File directory = null;

    File first = null;

    try
    {
      directory = new File(FileUtils.getTempDirectory(), new Long(configuration.getGenerator().next()).toString());
      directory.mkdirs();

      ZipInputStream zstream = new ZipInputStream(iStream);

      ZipEntry entry;

      while ( ( entry = zstream.getNextEntry() ) != null)
      {
        File file = new File(directory, entry.getName());

        if (first == null && file.getName().endsWith("dbf"))
        {
          first = file;
        }

        FileOutputStream output = null;

        try
        {
          output = new FileOutputStream(file);

          int len = 0;

          while ( ( len = zstream.read(buffer) ) > 0)
          {
            output.write(buffer, 0, len);
          }
        }
        finally
        {
          if (output != null)
          {
            output.close();
          }
        }
      }

      if (first != null)
      {
        this.createFeatures(first.toURI().toURL());
      }
      else
      {
        // TODO Change exception type
        throw new RuntimeException("Empty zip file");
      }
    }
    catch (IOException e1)
    {
      throw new RuntimeException(e1);
    }
    finally
    {
      if (directory != null)
      {
        try
        {
          FileUtils.deleteDirectory(directory);
        }
        catch (IOException e)
        {
          // TODO Auto-generated catch block
          throw new RuntimeException(e);
        }
      }
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
  private void createFeatures(URL url) throws InvocationTargetException
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

          SimpleFeatureType schema = source.getSchema();

          List<AttributeDescriptor> descriptors = schema.getAttributeDescriptors();

          /*
           * Iterate over the features
           */
          FeatureCollection<SimpleFeatureType, SimpleFeature> features = source.getFeatures();
          FeatureIterator<SimpleFeature> iterator = features.features();

          try
          {
            while (iterator.hasNext())
            {
              SimpleFeature feature = iterator.next();

              this.createFeature(descriptors, feature);
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

  private void createFeature(List<AttributeDescriptor> descriptors, SimpleFeature feature)
  {
    // Create a new object
    Business business = BusinessFacade.newBusiness(this.configuration.getType());

    Geometry geometry = null;
    String entityAttribute = null;

    Map<LocationColumn, String> locations = new HashMap<LocationColumn, String>();

    for (AttributeDescriptor descriptor : descriptors)
    {
      Object value = this.getValue(feature, descriptor);

      if (value != null)
      {
        if (value instanceof Geometry)
        {
          geometry = (Geometry) value;
        }
        else if (this.configuration.isLocationColumn(descriptor))
        {
          /*
           * All location columns will map to the same attribute
           */
          entityAttribute = configuration.getAttributeName(descriptor);
          LocationColumn column = this.configuration.getLocationColumn(descriptor);

          locations.put(column, value.toString());
        }
        else
        {
          String attributeName = configuration.getAttributeName(descriptor);

          if (attributeName != null)
          {
            MdAttributeConcreteDAOIF mdAttribute = business.getMdAttributeDAO(attributeName);

            if (mdAttribute instanceof MdAttributeTermDAOIF)
            {
              MdAttributeTermDAOIF mdAttributeTerm = (MdAttributeTermDAOIF) mdAttribute;

              String packageString = this.configuration.getClassifierPackage(mdAttributeTerm);

              Classifier classifier = Classifier.findClassifierAddIfNotExist(packageString, value.toString(), mdAttributeTerm);

              this.setValue(business, attributeName, classifier.getOid());
            }
            else
            {
              this.setValue(business, attributeName, value);
            }
          }
        }
      }
    }

    GeoEntity entity = this.getOrCreateLocation(configuration.getRootEntity(), locations);

    if (entity != null)
    {
      this.setValue(business, entityAttribute, entity.getOid());
    }

    if (geometry != null)
    {
      MultiPolygon multipolygon = this.geometryHelper.getGeoMultiPolygon(geometry);
      Point point = this.geometryHelper.getGeoPoint(geometry);

      List<? extends MdAttributeConcreteDAOIF> mdAttributes = business.getMdAttributeDAOs();

      for (MdAttributeConcreteDAOIF mdAttribute : mdAttributes)
      {
        if (mdAttribute instanceof MdAttributePointDAOIF)
        {
          this.setValue(business, mdAttribute.definesAttribute(), point);
        }
        else if (mdAttribute instanceof MdAttributeMultiPolygonDAOIF)
        {
          this.setValue(business, mdAttribute.definesAttribute(), multipolygon);
        }
      }
    }

    String oid = feature.getID();

    ShapefileAttributeHandler handler = this.configuration.getOidHandler();

    if (handler != null)
    {
      handler.handle(business, null, oid);
    }

    business.apply();
  }

  private Object getValue(SimpleFeature feature, AttributeDescriptor descriptor)
  {
    Object value = feature.getAttribute(descriptor.getName());

    String attributeName = this.configuration.getAttributeName(descriptor);

    if (attributeName != null)
    {
      ShapefileAttributeHandler handler = this.configuration.getHandler(attributeName);

      if (handler != null)
      {
        return handler.transform(value);
      }
    }

    return value;
  }

  private void setValue(Business business, String attributeName, Object value)
  {
    ShapefileAttributeHandler handler = this.configuration.getHandler(attributeName);

    if (handler != null)
    {
      handler.handle(business, attributeName, value);
    }
    else
    {
      business.setValue(attributeName, value.toString());
    }
  }

  /**
   * 
   * 
   * @param locations
   * @return
   */
  private GeoEntity getOrCreateLocation(GeoEntity root, Map<LocationColumn, String> locations)
  {
    List<LocationColumn> columns = new LinkedList<LocationColumn>(locations.keySet());

    Collections.sort(columns);

    Universal rootUniversal = root.getUniversal();

    GeoEntity parent = root;

    for (LocationColumn column : columns)
    {
      String universal = column.getUniversalType();
      String label = locations.get(column);

      if (label != null && label.length() > 0)
      {

        if (rootUniversal.getKey().equals(universal))
        {
          parent = root;
        }
        else
        {
          GeoEntity entity = this.findGeoEntity(parent, universal, label);

          if (entity == null)
          {
            entity = new GeoEntity();
            entity.setUniversal(Universal.getByKey(universal));
            entity.setGeoId(this.generateGeoId());
            entity.getDisplayLabel().setDefaultValue(label);
            entity.apply();

            entity.addLink(parent, LocatedIn.CLASS);

            // Create a new geo entity problem
            GeoEntityProblem.createProblems(entity, GeoEntityProblemType.UNMATCHED);
          }

          parent = entity;
        }
      }
    }

    return parent;
  }

  private GeoEntity findGeoEntity(GeoEntity parent, String universal, String label)
  {
    QueryFactory factory = new QueryFactory();

    LocatedInQuery lQuery = new LocatedInQuery(factory);
    lQuery.WHERE(lQuery.parentOid().EQ(parent.getOid()));

    SynonymQuery synonymQuery = new SynonymQuery(factory);
    synonymQuery.WHERE(synonymQuery.getDisplayLabel().localize().EQ(label));

    GeoEntityQuery query = new GeoEntityQuery(factory);
    query.WHERE(query.getUniversal().getKeyName().EQ(universal));
    query.AND(query.locatedIn(lQuery));
    query.AND(OR.get(query.getDisplayLabel().localize().EQ(label), query.synonym(synonymQuery)));

    OIterator<? extends GeoEntity> iterator = query.getIterator();

    try
    {
      if (iterator.hasNext())
      {
        GeoEntity entity = iterator.next();

        if (iterator.hasNext())
        {
          throw new RuntimeException("Ambigious entity with the label [" + label + "] and universal [" + universal + "]");
        }

        return entity;
      }

      return null;
    }
    finally
    {
      iterator.close();
    }
  }

  /**
   * Returns the geo oid of the
   * 
   * @return
   */
  public String generateGeoId()
  {
    String prefix = this.configuration.getRootEntity().getKey();

    KeyGeneratorIF generator = this.configuration.getGenerator();
    return generator.generateKey(prefix);
  }
}
