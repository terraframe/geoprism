package com.runwaysdk.geodashboard.gis.shapefile;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.geotools.data.FeatureWriter;
import org.geotools.data.Transaction;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.runwaysdk.system.gis.geo.GeoEntity;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

/**
 * Builds a shapefile of 100 features. Should only be used for testing the
 * import of Shapefiles.
 * 
 * @author Justin Smethie
 */
public class ShapeFileBuilder
{
  public static final String NAME_ATTRIBUTE        = "EntityName";

  public static final String ID_ATTRIBUTE          = "Id";

  public static final String GEOMETRY_ATTRIBUTE    = "Location";

  public static final String PARENT_ATTRIBUTE      = "Parent";

  public static final String PARENT_TYPE_ATTRIBUTE = "ParentType";

  public static final String TYPE_ATTRIBUTE        = "Type";

  private URL                url;

  protected WKTReader        reader;

  private GeoEntity          parent;

  public ShapeFileBuilder(URL url, GeoEntity parent)
  {
    this.url = url;
    this.parent = parent;
    this.reader = new WKTReader(JTSFactoryFinder.getGeometryFactory(null));
  }

  public ShapeFileBuilder(String path, GeoEntity parent) throws MalformedURLException
  {
    this(new File(path).toURI().toURL(), parent);
  }

  public void build() throws IOException, ParseException
  {
    // Build the test shapefile
    SimpleFeatureTypeBuilder schemaBuilder = new SimpleFeatureTypeBuilder();
    schemaBuilder.setName("Flag");
    schemaBuilder.setNamespaceURI("http://localhost/");
    schemaBuilder.setCRS(DefaultGeographicCRS.WGS84);

    // add attributes in order
    this.addSchemaAttributes(schemaBuilder);

    SimpleFeatureType schema = schemaBuilder.buildFeatureType();

    ShapefileDataStoreFactory dataStoreFactory = new ShapefileDataStoreFactory();

    Map<String, Serializable> params = new HashMap<String, Serializable>();
    params.put("url", url);
    params.put("create spatial index", Boolean.TRUE);

    ShapefileDataStore store = (ShapefileDataStore) dataStoreFactory.createNewDataStore(params);
    store.createSchema(schema);

    FeatureWriter<SimpleFeatureType, SimpleFeature> writer = store.getFeatureWriter(Transaction.AUTO_COMMIT);

    try
    {
      for (int i = 0; i < 100; i++)
      {
        SimpleFeature feature = writer.next();

        populateFeature(feature, i);

        writer.write();
      }
    }
    finally
    {
      writer.close();
    }
  }

  protected void populateFeature(SimpleFeature feature, int i) throws ParseException
  {
    Geometry geometry = this.getGeometry(i);
    String name = this.getName(i);
    String geoId = this.getGeoId(i);
    String parentAttribute = this.getParentEntityName(i);
    String parentType = this.getParentType(i);
    String type = this.getType(i);

    feature.setAttribute(0, geometry);
    feature.setAttribute(ID_ATTRIBUTE, geoId);
    feature.setAttribute(NAME_ATTRIBUTE, name);
    feature.setAttribute(PARENT_ATTRIBUTE, parentAttribute);
    feature.setAttribute(PARENT_TYPE_ATTRIBUTE, parentType);
    feature.setAttribute(TYPE_ATTRIBUTE, type);
  }

  protected void addSchemaAttributes(SimpleFeatureTypeBuilder schemaBuilder)
  {
    schemaBuilder.add(GEOMETRY_ATTRIBUTE, Point.class);
    schemaBuilder.add(ID_ATTRIBUTE, String.class);
    schemaBuilder.add(NAME_ATTRIBUTE, String.class);
    schemaBuilder.add(PARENT_ATTRIBUTE, String.class);
    schemaBuilder.add(PARENT_TYPE_ATTRIBUTE, String.class);
    schemaBuilder.add(TYPE_ATTRIBUTE, String.class);
  }

  protected Geometry getGeometry(int i) throws ParseException
  {
    return reader.read("POINT (" + i + " " + i + ")");
  }

  public String getName(int i)
  {
    return "Geo Entitz " + i;
  }

  public String getGeoId(int i)
  {
    return "Entity Idz " + i;
  }

  public String getParentEntityName(int i)
  {
    return parent.getDisplayLabel().getValue();
  }

  public String getParentType(int i)
  {
    if (parent != null)
    {
      return parent.getUniversal().getDisplayLabel().getValue();
    }

    return "Country";
  }

  public String getType(int i)
  {
    return "Health Facility";
  }
}
