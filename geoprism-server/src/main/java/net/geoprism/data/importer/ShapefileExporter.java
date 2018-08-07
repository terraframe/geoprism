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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import net.geoprism.ontology.Classifier;

import org.geotools.data.DataStore;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.FileDataStoreFactorySpi;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;

import com.runwaysdk.business.Business;
import com.runwaysdk.business.BusinessQuery;
import com.runwaysdk.business.Entity;
import com.runwaysdk.business.ontology.Term;
import com.runwaysdk.dataaccess.MdAttributeBooleanDAOIF;
import com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF;
import com.runwaysdk.dataaccess.MdAttributeClobDAOIF;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDateDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDateTimeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDoubleDAOIF;
import com.runwaysdk.dataaccess.MdAttributeFloatDAOIF;
import com.runwaysdk.dataaccess.MdAttributeIntegerDAOIF;
import com.runwaysdk.dataaccess.MdAttributeLocalDAOIF;
import com.runwaysdk.dataaccess.MdAttributeLongDAOIF;
import com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF;
import com.runwaysdk.dataaccess.MdAttributeTimeDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;

import com.runwaysdk.gis.dataaccess.MdAttributeGeometryDAOIF;
import com.runwaysdk.gis.dataaccess.MdAttributeLineStringDAOIF;
import com.runwaysdk.gis.dataaccess.MdAttributeMultiLineStringDAOIF;
import com.runwaysdk.gis.dataaccess.MdAttributeMultiPointDAOIF;
import com.runwaysdk.gis.dataaccess.MdAttributeMultiPolygonDAOIF;
import com.runwaysdk.gis.dataaccess.MdAttributePointDAOIF;
import com.runwaysdk.gis.dataaccess.MdAttributePolygonDAOIF;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.system.gis.geo.GeoEntity;
import com.runwaysdk.system.gis.geo.LocatedIn;
import com.runwaysdk.system.gis.geo.Universal;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

public class ShapefileExporter 
{
  private static class AttributeDescription 
  {
    private String   name;

    private Class<?> binding;

    private Integer  srid;

    public AttributeDescription(String name, Class<?> binding)
    {
      super();
      this.name = name;
      this.binding = binding;
      this.srid = null;
    }

    public AttributeDescription(String name, Class<?> binding, Integer srid)
    {
      super();
      this.name = name;
      this.binding = binding;
      this.srid = srid;
    }

    public String getName()
    {
      return name;
    }

    public Class<?> getBinding()
    {
      return binding;
    }

    public Integer getSrid()
    {
      return srid;
    }
  }

  private File                         directory;

  private ShapefileExportConfiguration config;

  public ShapefileExporter(File directory, ShapefileExportConfiguration config)
  {
    super();
    this.directory = directory;
    this.config = config;
  }

  public File run()
  {
    try
    {
      String typeName = config.getMdBusiness().getTableName();
      File file = new File(directory, typeName + ".shp");

      Map<String, Serializable> map = new HashMap<String, Serializable>(Collections.singletonMap("url", file.toURI().toURL()));

      SimpleFeatureTypeBuilder builder = this.getFeatureBuilder();
      builder.setName(typeName);

      SimpleFeatureType featureType = builder.buildFeatureType();

      FileDataStoreFactorySpi factory = new ShapefileDataStoreFactory();
      DataStore datastore = factory.createNewDataStore(map);
      datastore.createSchema(featureType);

      SimpleFeatureStore featureStore = (SimpleFeatureStore) datastore.getFeatureSource(typeName);

      DefaultTransaction transaction = new DefaultTransaction("transaction");
      featureStore.setTransaction(transaction);

      BusinessQuery query = this.config.createQuery();
      OIterator<Business> iterator = this.getIterator(query);

      try
      {
        List<? extends MdAttributeDAOIF> attributes = config.getMdBusiness().getAllDefinedMdAttributes();

        while (iterator.hasNext())
        {
          Business business = iterator.next();

          List<Object> values = this.getFeatureValues(featureType, business, attributes);

          SimpleFeature feature = SimpleFeatureBuilder.build(featureType, values, business.getOid());
          SimpleFeatureCollection collection = DataUtilities.collection(feature);
          featureStore.addFeatures(collection);
        }

      }
      finally
      {
        iterator.close();
      }

      transaction.commit();

      return this.createZip();
    }
    catch (RuntimeException e)
    {
      throw e;
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
    finally
    {

    }
  }

  public ShapefileImportConfiguration getShapefileImportConfiguration()
  {
    ShapefileImportConfiguration config = new ShapefileImportConfiguration(this.config.getMdBusiness().definesType());

    List<? extends MdAttributeDAOIF> mdAttributes = this.config.getMdBusiness().getAllDefinedMdAttributes();

    for (MdAttributeDAOIF mdAttribute : mdAttributes)
    {
      if (isValid(mdAttribute))
      {
        MdAttributeConcreteDAOIF mdAttributeConcrete = mdAttribute.getMdAttributeConcrete();
        String name = ShapefileExporter.getShapefileName(mdAttribute.definesAttribute());

        if (mdAttributeConcrete instanceof MdAttributeReferenceDAOIF)
        {
          MdAttributeReferenceDAOIF mdAttributeReference = (MdAttributeReferenceDAOIF) mdAttributeConcrete;
          MdBusinessDAOIF referenceMdBusiness = mdAttributeReference.getReferenceMdBusinessDAO();

          if (referenceMdBusiness.definesType().equals(GeoEntity.CLASS))
          {
            List<String> types = this.config.getLocationTypes();
            for (int i = 0; i < types.size(); i++)
            {
              String type = types.get(i);
              Universal universal = Universal.getByKey(type);
              String columnName = ShapefileExporter.getShapefileName(universal.getDisplayLabel().getValue());

              config.addAttributeMapping(columnName, mdAttribute.definesAttribute());
              config.addLocationColumn(new LocationColumn(columnName, type, i));
            }
          }
          else
          {
            config.addAttributeMapping(name, mdAttribute.definesAttribute());
          }
        }
        else
        {
          config.addAttributeMapping(name, mdAttribute.definesAttribute());
        }
      }
    }

    return config;
  }

  private OIterator<Business> getIterator(BusinessQuery query)
  {
    if (this.config.getFeatureLimit() != null)
    {
      return query.getIterator(this.config.getFeatureLimit(), 1);
    }
    else
    {
      return query.getIterator();
    }
  }

  private List<Object> getFeatureValues(SimpleFeatureType featureType, Business business, List<? extends MdAttributeDAOIF> attributes)
  {
    Map<String, Object> map = new HashMap<String, Object>();

    for (MdAttributeDAOIF mdAttribute : attributes)
    {
      MdAttributeConcreteDAOIF mdAttributeConcrete = mdAttribute.getMdAttributeConcrete();

      String name = ShapefileExporter.getShapefileName(mdAttributeConcrete.definesAttribute());

      if (isValid(mdAttribute))
      {
        if (mdAttributeConcrete instanceof MdAttributeReferenceDAOIF)
        {
          MdAttributeReferenceDAOIF mdAttributeReference = (MdAttributeReferenceDAOIF) mdAttributeConcrete;
          MdBusinessDAOIF referenceMdBusiness = mdAttributeReference.getReferenceMdBusinessDAO();

          if (referenceMdBusiness.definesType().equals(GeoEntity.CLASS))
          {
            String oid = business.getValue(mdAttribute.definesAttribute());

            if (oid != null && oid.length() > 0)
            {
              GeoEntity geoEntity = GeoEntity.get(oid);

              OIterator<Term> iterator = geoEntity.getAllAncestors(LocatedIn.CLASS);

              try
              {
                List<Term> terms = iterator.getAll();

                for (String type : this.config.getLocationTypes())
                {
                  Universal universal = Universal.getByKey(type);
                  String columnName = ShapefileExporter.getShapefileName(universal.getDisplayLabel().getValue());

                  GeoEntity parent = this.getAncestorOfType(geoEntity, terms, universal);

                  String value = null;

                  if (parent != null)
                  {
                    value = parent.getDisplayLabel().getValue();
                  }
                  else
                  {
                    value = "";
                  }

                  map.put(columnName, value);
                }
              }
              finally
              {
                iterator.close();
              }

              if (this.config.getGeometryAttribue() != null)
              {
                Geometry geometry = (Geometry) business.getObjectValue(this.config.getGeometryAttribue());

                map.put("the_geom", geometry);
              }
              else
              {
                map.put("the_geom", geoEntity.getGeoMultiPolygon());
              }
            }
          }
          else if (referenceMdBusiness.definesType().equals(Classifier.CLASS))
          {
            String oid = business.getValue(mdAttribute.definesAttribute());

            if (oid != null && oid.length() > 0)
            {
              Classifier classifier = Classifier.get(oid);

              map.put(name, classifier.getDisplayLabel().getValue());
            }
          }
        }
        else
        {
          Object value = business.getObjectValue(mdAttribute.definesAttribute());

          map.put(name, value);
        }
      }
    }

    List<Object> values = new LinkedList<Object>();

    int count = featureType.getAttributeCount();

    for (int i = 0; i < count; i++)
    {
      AttributeDescriptor descriptor = featureType.getDescriptor(i);
      String name = descriptor.getLocalName();

      values.add(map.get(name));
    }

    return values;
  }

  private GeoEntity getAncestorOfType(GeoEntity entity, List<Term> terms, Universal universal)
  {
    if (entity.getUniversalId().equals(universal.getOid()))
    {
      return entity;
    }

    for (Term term : terms)
    {
      GeoEntity ancestor = (GeoEntity) term;

      if (ancestor.getUniversalId().equals(universal.getOid()))
      {
        return ancestor;
      }
    }

    return null;
  }

  private File createZip()
  {
    File zipFile = new File(this.directory.getParentFile(), this.directory.getName() + ".zip");

    try
    {
      // create byte buffer
      byte[] buffer = new byte[1024];

      System.out.println("Creating Zip file: " + zipFile.getAbsolutePath());

      FileOutputStream fos = new FileOutputStream(zipFile);

      try
      {
        ZipOutputStream zos = new ZipOutputStream(fos);

        try
        {
          File[] files = this.directory.listFiles();

          for (int i = 0; i < files.length; i++)
          {
            System.out.println("Adding file: " + files[i].getName());

            FileInputStream fis = new FileInputStream(files[i]);

            try
            {

              // begin writing a new ZIP entry, positions the stream to the start of the entry data
              zos.putNextEntry(new ZipEntry(files[i].getName()));

              int length;

              while ( ( length = fis.read(buffer) ) > 0)
              {
                zos.write(buffer, 0, length);
              }

              zos.closeEntry();
            }
            finally
            {
              // close the InputStream
              fis.close();
            }
          }
        }
        finally
        {
          // close the ZipOutputStream
          zos.close();
        }
      }
      finally
      {
        fos.close();
      }

    }
    catch (IOException ioe)
    {
      System.out.println("Error creating zip file" + ioe);
    }

    return zipFile;
  }

  private SimpleFeatureTypeBuilder getFeatureBuilder()
  {
    SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();

    List<? extends MdAttributeDAOIF> attributes = this.config.getMdBusiness().getAllDefinedMdAttributes();

    for (MdAttributeDAOIF attribute : attributes)
    {
      if (isValid(attribute))
      {
        List<AttributeDescription> descriptions = this.getDescriptions(attribute);

        for (AttributeDescription description : descriptions)
        {
          if (description.getSrid() != null)
          {
            builder.add(description.getName(), description.getBinding(), description.getSrid());
          }
          else
          {
            builder.add(description.getName(), description.getBinding());
          }
        }
      }
    }

    return builder;
  }

  private List<AttributeDescription> getDescriptions(MdAttributeDAOIF attribute)
  {
    List<AttributeDescription> descriptions = new LinkedList<ShapefileExporter.AttributeDescription>();

    String name = ShapefileExporter.getShapefileName(attribute.definesAttribute());

    attribute = attribute.getMdAttributeConcrete();

    if ( ( attribute instanceof MdAttributeCharacterDAOIF ) || ( attribute instanceof MdAttributeClobDAOIF ) || ( attribute instanceof MdAttributeLocalDAOIF ))
    {
      descriptions.add(new AttributeDescription(name, String.class));
    }
    else if (attribute instanceof MdAttributeIntegerDAOIF)
    {
      descriptions.add(new AttributeDescription(name, Integer.class));
    }
    else if (attribute instanceof MdAttributeLongDAOIF)
    {
      descriptions.add(new AttributeDescription(name, Long.class));
    }
    else if (attribute instanceof MdAttributeDoubleDAOIF)
    {
      descriptions.add(new AttributeDescription(name, Double.class));
    }
    else if (attribute instanceof MdAttributeFloatDAOIF)
    {
      descriptions.add(new AttributeDescription(name, Float.class));
    }
    else if (attribute instanceof MdAttributeBooleanDAOIF)
    {
      descriptions.add(new AttributeDescription(name, Boolean.class));
    }
    else if (attribute instanceof MdAttributePointDAOIF)
    {
      descriptions.add(new AttributeDescription(name, Point.class));
    }
    else if (attribute instanceof MdAttributeLineStringDAOIF)
    {
      descriptions.add(new AttributeDescription(name, LineString.class));
    }
    else if (attribute instanceof MdAttributePolygonDAOIF)
    {
      descriptions.add(new AttributeDescription(name, Polygon.class));
    }
    else if (attribute instanceof MdAttributeMultiPointDAOIF)
    {
      descriptions.add(new AttributeDescription(name, MultiPoint.class));
    }
    else if (attribute instanceof MdAttributeMultiLineStringDAOIF)
    {
      descriptions.add(new AttributeDescription(name, MultiLineString.class));
    }
    else if (attribute instanceof MdAttributeMultiPolygonDAOIF)
    {
      descriptions.add(new AttributeDescription(name, MultiPolygon.class));
    }
    else if ( ( attribute instanceof MdAttributeDateDAOIF ) || ( attribute instanceof MdAttributeDateTimeDAOIF ) || ( attribute instanceof MdAttributeTimeDAOIF ))
    {
      descriptions.add(new AttributeDescription(name, Date.class));
    }
    else if (attribute instanceof MdAttributeReferenceDAOIF)
    {
      MdAttributeReferenceDAOIF mdAttributeReference = (MdAttributeReferenceDAOIF) attribute;
      MdBusinessDAOIF referenceMdBusiness = mdAttributeReference.getReferenceMdBusinessDAO();

      if (referenceMdBusiness.definesType().equals(GeoEntity.CLASS))
      {
        for (String type : this.config.getLocationTypes())
        {
          Universal universal = Universal.getByKey(type);

          String value = ShapefileExporter.getShapefileName(universal.getDisplayLabel().getValue());

          descriptions.add(new AttributeDescription(value, String.class));
        }

        descriptions.add(new AttributeDescription("the_geom", MultiPolygon.class, 4326));
      }
      else if (referenceMdBusiness.definesType().equals(Classifier.CLASS))
      {
        descriptions.add(new AttributeDescription(name, String.class));
      }
    }
    else
    {
      throw new RuntimeException("Unknown shapefile type for MdAttribute [" + attribute.definesAttribute() + "] of type [" + attribute.getType() + "]");
    }

    return descriptions;
  }

  private boolean isValid(MdAttributeDAOIF attribute)
  {

    if (attribute.isSystem() || attribute.definesAttribute().equals(Entity.KEYNAME) || ( attribute instanceof MdAttributeGeometryDAOIF ))
    {
      return false;
    }
    else
    {
      attribute = attribute.getMdAttributeConcrete();

      if (attribute instanceof MdAttributeReferenceDAOIF)
      {
        MdAttributeReferenceDAOIF mdAttributeReference = (MdAttributeReferenceDAOIF) attribute;
        MdBusinessDAOIF referenceMdBusiness = mdAttributeReference.getReferenceMdBusinessDAO();

        if (referenceMdBusiness.definesType().equals(GeoEntity.CLASS))
        {
          return true;
        }
        else if (referenceMdBusiness.definesType().equals(Classifier.CLASS))
        {
          return true;
        }

        return false;
      }
    }

    return true;
  }

  public static String getShapefileName(String attributeName)
  {
    attributeName = attributeName.toLowerCase().replaceAll("\\s+", "_");

    int length = attributeName.length();

    if (length > 10)
    {
      return attributeName.substring(0, 5) + "_" + attributeName.substring(length - 4, length);
    }

    return attributeName;
  }

}
