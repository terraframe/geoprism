package com.runwayskd.geodashboard.etl;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.business.ontology.Term;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeCharacterInfo;
import com.runwaysdk.constants.MdAttributeDateInfo;
import com.runwaysdk.constants.MdAttributeDoubleInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdAttributeLongInfo;
import com.runwaysdk.constants.MdAttributeReferenceInfo;
import com.runwaysdk.constants.MdAttributeTextInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.metadata.MdAttributeBooleanDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeCharacterDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDateDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDoubleDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeLongDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeReferenceDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeTextDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdClassDAO;
import com.runwaysdk.geodashboard.DataUploader;
import com.runwaysdk.geodashboard.GeoEntityUtil;
import com.runwaysdk.geodashboard.MappableClass;
import com.runwaysdk.geodashboard.ontology.Classifier;
import com.runwaysdk.gis.constants.MdAttributeMultiPolygonInfo;
import com.runwaysdk.gis.constants.MdAttributePointInfo;
import com.runwaysdk.gis.dataaccess.metadata.MdAttributeMultiPolygonDAO;
import com.runwaysdk.gis.dataaccess.metadata.MdAttributePointDAO;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.system.gis.geo.AllowedIn;
import com.runwaysdk.system.gis.geo.GeoEntity;
import com.runwaysdk.system.gis.geo.GeoNode;
import com.runwaysdk.system.gis.geo.GeoNodeEntity;
import com.runwaysdk.system.gis.geo.GeoNodeGeometry;
import com.runwaysdk.system.gis.geo.Universal;
import com.runwaysdk.system.gis.metadata.MdAttributeMultiPolygon;
import com.runwaysdk.system.gis.metadata.MdAttributePoint;
import com.runwaysdk.system.metadata.MdAttribute;
import com.runwaysdk.system.metadata.MdAttributeReference;
import com.runwaysdk.system.metadata.MdClass;
import com.runwaysdk.system.metadata.MdViewQuery;

public class TargetBuilder
{
  public static final String PACKAGE_NAME = "com.runwaysdk.geodashboard.data.business";

  public static final String EXLUDE       = "EXCLUDE";

  public static final String DERIVED      = "DERIVED";

  private JSONObject         configuration;

  private SourceContextIF    source;

  private TargetContext      target;

  public TargetBuilder(JSONObject configuration, SourceContextIF source, TargetContext target)
  {
    this.configuration = configuration;
    this.source = source;
    this.target = target;
  }

  public void build()
  {
    try
    {
      JSONArray cSheets = configuration.getJSONArray("sheets");

      for (int i = 0; i < cSheets.length(); i++)
      {
        JSONObject sheet = cSheets.getJSONObject(i);

        TargetDefinitionIF definition = this.createMdBusiness(sheet);

        this.target.addSheetDefinition(definition);
      }
    }
    catch (JSONException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

  private TargetDefinitionIF createMdBusiness(JSONObject cSheet) throws JSONException
  {
    String sheetName = cSheet.getString("name");
    String label = cSheet.getString("label");
    String countryId = cSheet.getString("country");
    List<GeoNode> nodes = new LinkedList<GeoNode>();

    GeoEntity country = GeoEntityUtil.getCountryByUniversal(countryId);

    String sourceType = this.source.getType(sheetName);
    String typeName = this.generateBusinessTypeName(label);

    TargetDefinition definition = new TargetDefinition();
    definition.setSourceType(sourceType);
    definition.setTargetType(PACKAGE_NAME + "." + typeName);

    /*
     * Define the new MdBussiness and Mappable class
     */
    MdBusinessDAO mdBusiness = MdBusinessDAO.newInstance();
    mdBusiness.setValue(MdBusinessInfo.PACKAGE, PACKAGE_NAME);
    mdBusiness.setValue(MdBusinessInfo.NAME, typeName);
    mdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, label);
    mdBusiness.setGenerateMdController(false);
    mdBusiness.apply();

    JSONArray cFields = cSheet.getJSONArray("fields");
    JSONArray cAttributes = cSheet.getJSONArray("attributes");
    JSONArray cCoordinates = cSheet.getJSONArray("coordinates");

    /*
     * Add all of the basic fields
     */
    for (int i = 0; i < cFields.length(); i++)
    {
      JSONObject cField = cFields.getJSONObject(i);

      if (this.isValid(cField))
      {
        TargetFieldIF field = this.createMdAttribute(mdBusiness, sheetName, cField);

        definition.addField(field);
      }
    }

    /*
     * Add all of the text location fields
     */

    for (int i = 0; i < cAttributes.length(); i++)
    {
      JSONObject cField = cAttributes.getJSONObject(i);

      TargetFieldIF field = this.createMdGeoEntity(mdBusiness, sheetName, country, cField);

      definition.addField(field);

      // Create the geoNode
      if (cCoordinates.length() == 0)
      {
        String key = field.getKey();

        GeoNodeEntity node = new GeoNodeEntity();
        node.setKeyName(key);
        node.setGeoEntityAttribute(MdAttributeReference.getByKey(key));
        node.apply();

        nodes.add(node);
      }
    }

    /*
     * Add all of the coordinate fields
     */

    for (int i = 0; i < cCoordinates.length(); i++)
    {
      JSONObject cField = cCoordinates.getJSONObject(i);

      TargetFieldIF point = this.createMdPoint(mdBusiness, sheetName, cField);
      TargetFieldIF multiPolygon = this.createMdMultiPolygon(mdBusiness, sheetName, cField);
      TargetFieldIF featureId = this.createFeatureId(mdBusiness, sheetName, cField);
      TargetFieldIF location = this.createLocationField(mdBusiness, sheetName, cField, country, definition);
      TargetFieldIF featureLabel = definition.getFieldByLabel(cField.getString("featureLabel"));

      definition.addField(point);
      definition.addField(multiPolygon);
      definition.addField(featureId);

      // Create the geoNode
      GeoNodeGeometry node = new GeoNodeGeometry();
      node.setKeyName(point.getKey());
      node.setGeoEntityAttribute(MdAttributeReference.getByKey(location.getKey()));
      node.setIdentifierAttribute(MdAttribute.getByKey(featureId.getKey()));
      node.setDisplayLabelAttribute(MdAttribute.getByKey(featureLabel.getKey()));
      node.setGeometryAttribute(MdAttribute.getByKey(point.getKey()));
      node.setMultiPolygonAttribute(MdAttributeMultiPolygon.getByKey(multiPolygon.getKey()));
      node.setPointAttribute(MdAttributePoint.getByKey(point.getKey()));
      node.apply();

      nodes.add(node);
    }

    /*
     * Create the MappableClass
     */
    MappableClass mClass = new MappableClass();
    mClass.setWrappedMdClass(MdClass.getMdClass(mdBusiness.definesType()));
    mClass.apply();

    mClass.addUniversal(country.getUniversal()).apply();

    for (GeoNode node : nodes)
    {
      mClass.addGeoNode(node).apply();
    }

    return definition;
  }

  private TargetFieldIF createLocationField(MdClassDAOIF mdClass, String sheetName, JSONObject cCoordinate, GeoEntity country, TargetDefinition definition) throws JSONException
  {
    String location = cCoordinate.getString("location");

    if (location.equals(DERIVED))
    {
      String label = cCoordinate.getString("label");
      String latitude = cCoordinate.getString("latitude");
      String longitude = cCoordinate.getString("longitude");
      String universalId = cCoordinate.getString("universal");

      String attributeName = this.generateAttributeName(label, "Entity");

      MdAttributeReferenceDAO mdAttribute = MdAttributeReferenceDAO.newInstance();
      mdAttribute.setValue(MdAttributeReferenceInfo.NAME, attributeName);
      mdAttribute.setValue(MdAttributeReferenceInfo.DEFINING_MD_CLASS, mdClass.getId());
      mdAttribute.setStructValue(MdAttributeReferenceInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, label);
      mdAttribute.setValue(MdAttributeReferenceInfo.REF_MD_ENTITY, MdBusinessDAO.getMdBusinessDAO(GeoEntity.CLASS).getId());
      mdAttribute.apply();

      TargetFieldDerived field = new TargetFieldDerived();
      field.setName(attributeName);
      field.setLabel(label);
      field.setKey(mdClass.definesType() + "." + attributeName);
      field.setLatitudeSourceAttributeName(this.source.getFieldByLabel(sheetName, latitude).getAttributeName());
      field.setLongitudeSourceAttributeName(this.source.getFieldByLabel(sheetName, longitude).getAttributeName());
      field.setUniversal(Universal.get(universalId));
      field.setCountry(country);

      return field;
    }

    return definition.getFieldByLabel(location);
  }

  private boolean isValid(JSONObject cField) throws JSONException
  {
    String type = cField.getString("columnType");

    if (type.equals(ColumnType.BOOLEAN))
    {
      return true;
    }
    else if (type.equals(ColumnType.DATE))
    {
      return true;
    }
    else if (type.equals(ColumnType.DOUBLE))
    {
      return true;
    }
    else if (type.equals(ColumnType.LONG))
    {
      return true;
    }
    else if (type.equals(ColumnType.TEXT))
    {
      return true;
    }
    else if (type.equals(ColumnType.CATEGORY))
    {
      return true;
    }

    return false;
  }

  private TargetFieldIF createMdAttribute(MdClassDAO mdClass, String sheetName, JSONObject cField) throws JSONException
  {
    String columnType = cField.getString("columnType");
    String columnName = cField.getString("name");
    String label = cField.getString("label");
    String attributeName = this.generateAttributeName(label);
    String sourceAttributeName = this.source.getFieldByName(sheetName, columnName).getAttributeName();

    // Create the attribute
    if (columnType.equals(ColumnType.CATEGORY))
    {
      MdBusinessDAOIF referenceMdBusiness = MdBusinessDAO.getMdBusinessDAO(Classifier.CLASS);

      MdAttributeReferenceDAO mdAttribute = MdAttributeReferenceDAO.newInstance();
      mdAttribute.setValue(MdAttributeReferenceInfo.NAME, attributeName);
      mdAttribute.setValue(MdAttributeReferenceInfo.DEFINING_MD_CLASS, mdClass.getId());
      mdAttribute.setStructValue(MdAttributeReferenceInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, label);
      mdAttribute.setValue(MdAttributeReferenceInfo.REF_MD_ENTITY, referenceMdBusiness.getId());
      mdAttribute.apply();

      TargetFieldClassifier field = new TargetFieldClassifier();
      field.setName(attributeName);
      field.setLabel(label);
      field.setKey(mdClass.definesType() + "." + attributeName);
      field.setSourceAttributeName(sourceAttributeName);
      field.setPackageName(mdClass.definesType() + "." + attributeName);

      return field;
    }
    else if (columnType.equals(ColumnType.BOOLEAN))
    {
      MdAttributeBooleanDAO mdAttribute = MdAttributeBooleanDAO.newInstance();
      mdAttribute.setValue(MdAttributeBooleanInfo.NAME, attributeName);
      mdAttribute.setValue(MdAttributeBooleanInfo.DEFINING_MD_CLASS, mdClass.getId());
      mdAttribute.setStructValue(MdAttributeBooleanInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, label);
      mdAttribute.setStructValue(MdAttributeBooleanInfo.NEGATIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "False");
      mdAttribute.setStructValue(MdAttributeBooleanInfo.POSITIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "True");
      mdAttribute.apply();
    }
    else if (columnType.equals(ColumnType.DATE))
    {
      MdAttributeDateDAO mdAttribute = MdAttributeDateDAO.newInstance();
      mdAttribute.setValue(MdAttributeDateInfo.NAME, attributeName);
      mdAttribute.setValue(MdAttributeDateInfo.DEFINING_MD_CLASS, mdClass.getId());
      mdAttribute.setStructValue(MdAttributeDateInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, label);
      mdAttribute.apply();
    }
    else if (columnType.equals(ColumnType.DOUBLE))
    {
      int length = cField.getInt("precision");
      int decimal = cField.getInt("scale");

      MdAttributeDoubleDAO mdAttribute = MdAttributeDoubleDAO.newInstance();
      mdAttribute.setValue(MdAttributeDoubleInfo.NAME, attributeName);
      mdAttribute.setValue(MdAttributeDoubleInfo.DEFINING_MD_CLASS, mdClass.getId());
      mdAttribute.setStructValue(MdAttributeDoubleInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, label);
      mdAttribute.setValue(MdAttributeDoubleInfo.LENGTH, length);
      mdAttribute.setValue(MdAttributeDoubleInfo.DECIMAL, decimal);
      mdAttribute.apply();
    }
    else if (columnType.equals(ColumnType.LONG))
    {
      MdAttributeLongDAO mdAttribute = MdAttributeLongDAO.newInstance();
      mdAttribute.setValue(MdAttributeLongInfo.NAME, attributeName);
      mdAttribute.setValue(MdAttributeLongInfo.DEFINING_MD_CLASS, mdClass.getId());
      mdAttribute.setStructValue(MdAttributeLongInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, label);
      mdAttribute.apply();
    }
    else if (columnType.equals(ColumnType.TEXT))
    {
      MdAttributeTextDAO mdAttribute = MdAttributeTextDAO.newInstance();
      mdAttribute.setValue(MdAttributeTextInfo.NAME, attributeName);
      mdAttribute.setValue(MdAttributeTextInfo.DEFINING_MD_CLASS, mdClass.getId());
      mdAttribute.setStructValue(MdAttributeTextInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, label);
      mdAttribute.apply();
    }

    // Create the target field
    TargetFieldBasic field = new TargetFieldBasic();
    field.setName(attributeName);
    field.setLabel(label);
    field.setKey(mdClass.definesType() + "." + attributeName);
    field.setSourceAttributeName(sourceAttributeName);

    return field;
  }

  private TargetFieldIF createMdGeoEntity(MdClassDAO mdClass, String sheetName, GeoEntity country, JSONObject cAttribute) throws JSONException
  {
    String label = cAttribute.getString("label");
    String universalId = cAttribute.getString("universal");
    String attributeName = this.generateAttributeName(label);

    MdAttributeReferenceDAO mdAttribute = MdAttributeReferenceDAO.newInstance();
    mdAttribute.setValue(MdAttributeReferenceInfo.NAME, attributeName);
    mdAttribute.setValue(MdAttributeReferenceInfo.DEFINING_MD_CLASS, mdClass.getId());
    mdAttribute.setStructValue(MdAttributeReferenceInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, label);
    mdAttribute.apply();

    Universal lowest = Universal.get(universalId);

    List<Term> universals = new LinkedList<Term>();
    Universal root = Universal.getRoot();
    universals.addAll(GeoEntityUtil.getOrderedAncestors(root, lowest, AllowedIn.CLASS));

    TargetFieldGeoEntity field = new TargetFieldGeoEntity();
    field.setName(attributeName);
    field.setLabel(label);
    field.setKey(mdClass.definesType() + "." + attributeName);
    field.setRoot(country);

    JSONObject fields = cAttribute.getJSONObject("fields");

    for (Term universal : universals)
    {
      if (fields.has(universal.getId()))
      {
        String sLabel = fields.getString(universal.getId());

        if (!sLabel.equals(EXLUDE))
        {
          SourceFieldIF sField = source.getFieldByLabel(sheetName, sLabel);

          field.addUniversalAttribute(sField.getAttributeName(), (Universal) universal);
        }
      }
    }

    return field;
  }

  private TargetFieldIF createMdMultiPolygon(MdClassDAO mdClass, String sheetName, JSONObject cCoordinate) throws JSONException
  {
    String label = cCoordinate.getString("label");
    String latitude = cCoordinate.getString("latitude");
    String longitude = cCoordinate.getString("longitude");

    String attributeName = this.generateAttributeName(label, "MultiPolygon");

    MdAttributeMultiPolygonDAO mdAttribute = MdAttributeMultiPolygonDAO.newInstance();
    mdAttribute.setValue(MdAttributeMultiPolygonInfo.NAME, attributeName);
    mdAttribute.setValue(MdAttributeMultiPolygonInfo.DEFINING_MD_CLASS, mdClass.getId());
    mdAttribute.setStructValue(MdAttributeMultiPolygonInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, label);
    mdAttribute.setValue(MdAttributeMultiPolygonInfo.DIMENSION, "2");
    mdAttribute.setValue(MdAttributeMultiPolygonInfo.SRID, "4326");
    mdAttribute.apply();

    TargetFieldMultiPolygon field = new TargetFieldMultiPolygon();
    field.setName(attributeName);
    field.setLabel(label);
    field.setKey(mdClass.definesType() + "." + attributeName);
    field.setLatitudeSourceAttributeName(this.source.getFieldByLabel(sheetName, latitude).getAttributeName());
    field.setLongitudeSourceAttributeName(this.source.getFieldByLabel(sheetName, longitude).getAttributeName());

    return field;
  }

  private TargetFieldIF createMdPoint(MdClassDAO mdClass, String sheetName, JSONObject cCoordinate) throws JSONException
  {
    String label = cCoordinate.getString("label");
    String latitude = cCoordinate.getString("latitude");
    String longitude = cCoordinate.getString("longitude");

    String attributeName = this.generateAttributeName(label, "Point");

    MdAttributePointDAO mdAttribute = MdAttributePointDAO.newInstance();
    mdAttribute.setValue(MdAttributePointInfo.NAME, attributeName);
    mdAttribute.setValue(MdAttributePointInfo.DEFINING_MD_CLASS, mdClass.getId());
    mdAttribute.setStructValue(MdAttributePointInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, label);
    mdAttribute.setValue(MdAttributePointInfo.DIMENSION, "2");
    mdAttribute.setValue(MdAttributePointInfo.SRID, "4326");
    mdAttribute.apply();

    TargetFieldMultiPolygon field = new TargetFieldMultiPolygon();
    field.setName(attributeName);
    field.setLabel(label);
    field.setKey(mdClass.definesType() + "." + attributeName);
    field.setLatitudeSourceAttributeName(this.source.getFieldByLabel(sheetName, latitude).getAttributeName());
    field.setLongitudeSourceAttributeName(this.source.getFieldByLabel(sheetName, longitude).getAttributeName());

    return field;
  }

  private TargetFieldIF createFeatureId(MdClassDAO mdClass, String sheetName, JSONObject cCoordinate) throws JSONException
  {
    String label = cCoordinate.getString("label");

    String attributeName = this.generateAttributeName(label, "FeatureId");

    MdAttributeCharacterDAO mdAttribute = MdAttributeCharacterDAO.newInstance();
    mdAttribute.setValue(MdAttributeCharacterInfo.NAME, attributeName);
    mdAttribute.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, mdClass.getId());
    mdAttribute.setValue(MdAttributeCharacterInfo.SIZE, "64");
    mdAttribute.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, label);
    mdAttribute.apply();

    TargetFieldGenerated field = new TargetFieldGenerated();
    field.setName(attributeName);
    field.setLabel(label);
    field.setKey(mdClass.definesType() + "." + attributeName);

    return field;
  }

  private String generateAttributeName(String label)
  {
    return this.generateAttributeName(label, "");
  }

  private String generateAttributeName(String label, String suffix)
  {
    String name = DataUploader.getSystemName(label, suffix, false);

    return name;
  }

  private String generateBusinessTypeName(String label)
  {
    // Create a unique name for the view type based upon the name of the sheet
    String typeName = DataUploader.getSystemName(label);

    Integer suffix = 0;

    while (!this.isUnique(PACKAGE_NAME, typeName, suffix))
    {
      suffix += 1;
    }

    if (suffix > 0)
    {
      return typeName + suffix;
    }

    return typeName;
  }

  private boolean isUnique(String packageName, String typeName, Integer suffix)
  {
    MdViewQuery query = new MdViewQuery(new QueryFactory());
    query.WHERE(query.getTypeName().EQ(typeName + suffix));
    query.AND(query.getPackageName().EQ(packageName));

    return ( query.getCount() == 0 );
  }

}
