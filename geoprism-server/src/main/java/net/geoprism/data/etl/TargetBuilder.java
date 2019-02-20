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
package net.geoprism.data.etl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import net.geoprism.ConfigurationIF;
import net.geoprism.ConfigurationService;
import net.geoprism.DataUploader;
import net.geoprism.MappableAttribute;
import net.geoprism.MappableClass;
import net.geoprism.TermSynonymRelationship;
import net.geoprism.data.browser.DataBrowserUtil;
import net.geoprism.ontology.Classifier;
import net.geoprism.ontology.ClassifierIsARelationship;
import net.geoprism.ontology.ClassifierSynonym;
import net.geoprism.ontology.GeoEntityUtil;

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
import com.runwaysdk.constants.MdViewInfo;
import com.runwaysdk.constants.RelationshipInfo;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.RelationshipDAO;
import com.runwaysdk.dataaccess.TermAttributeDAOIF;
import com.runwaysdk.dataaccess.metadata.MdAttributeBooleanDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeCharacterDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDateDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDoubleDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeLongDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeReferenceDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeTermDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeTextDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdClassDAO;
import com.runwaysdk.generated.system.gis.geo.AllowedInAllPathsTableQuery;
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
import com.runwaysdk.system.metadata.MdBusinessQuery;
import com.runwaysdk.system.metadata.MdClass;
import com.runwaysdk.util.IDGenerator;

public class TargetBuilder
{
  public static final String PACKAGE_NAME = "net.geoprism.data.business";

  public static final String EXLUDE       = "EXCLUDE";

  public static final String DERIVE       = "DERIVE";

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

//      for (int i = 0; i < cSheets.length(); i++)
      {
        JSONObject sheet = cSheets.getJSONObject(0);

        if (sheet.has("existing"))
        {
          String bindingId = sheet.getString("existing");

          ExcelSourceBinding sBinding = ExcelSourceBinding.get(bindingId);
          TargetBinding tBinding = sBinding.getTargetBinding();

          this.target.addDefinition(tBinding.getDefinition());

          /*
           * Delete the existing data if required
           */
          boolean replace = sheet.has("replaceExisting") && sheet.getBoolean("replaceExisting");

          if (replace)
          {
            MdClass mdClass = tBinding.getTargetBusiness();

            DataBrowserUtil.deleteData(mdClass.definesType());
          }
        }
        else
        {
          TargetDefinitionIF definition = this.createMdBusiness(sheet);

          this.target.addDefinition(definition);
        }
      }

      this.setupLocationExclusions();
      this.setupCategoryExclusions();
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
    String description = ( cSheet.has("description") ? cSheet.getString("description") : "" );
    String dSource = ( cSheet.has("source") ? cSheet.getString("source") : "" );
    String countryId = cSheet.getString("country");
    List<GeoNode> nodes = new LinkedList<GeoNode>();

    GeoEntity country = GeoEntityUtil.getCountryByUniversal(countryId);
    Universal lowest = country.getUniversal();

    String sourceType = this.source.getType(sheetName);
    String typeName = this.generateBusinessTypeName(label);

    TargetDefinition definition = new TargetDefinition();
    definition.setSourceType(sourceType);
    definition.setTargetType(PACKAGE_NAME + "." + typeName);

    /*
     * Define the new MdView
     */
    MdBusinessDAO mdBusiness = MdBusinessDAO.newInstance();
    mdBusiness.setValue(MdBusinessInfo.PACKAGE, PACKAGE_NAME);
    mdBusiness.setValue(MdBusinessInfo.NAME, typeName);
    mdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, label);
    mdBusiness.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, description);
    mdBusiness.setValue(MdViewInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness.apply();

    /*
     * Create the default classifier root for the MdBusiness
     */
    Classifier root = Classifier.findClassifier(mdBusiness.definesType(), typeName);

    if (root == null)
    {
      root = new Classifier();
      root.setClassifierId(typeName);
      root.setClassifierPackage(mdBusiness.definesType());
      root.setKeyName(mdBusiness.definesType());
      root.getDisplayLabel().setValue(label);
      root.apply();

      root.addLink(Classifier.getRoot(), ClassifierIsARelationship.CLASS).apply();
    }

    /*
     * Add all of the basic fields
     */
    if (cSheet.has("fields"))
    {
      JSONArray cFields = cSheet.getJSONArray("fields");

      for (int i = 0; i < cFields.length(); i++)
      {
        JSONObject cField = cFields.getJSONObject(i);

        if (this.isValid(cField))
        {
          TargetFieldIF field = this.createMdAttribute(mdBusiness, sheetName, cField, root);

          definition.addField(field);
        }
      }
    }

    /*
     * Add all of the text location fields
     */
    if (cSheet.has("attributes"))
    {
      JSONObject cAttributes = cSheet.getJSONObject("attributes");
      JSONObject values = cAttributes.getJSONObject("values");
      JSONArray ids = cAttributes.getJSONArray("ids");

      Set<String> references = this.getReferencedLocationAttributes(cSheet);

      for (int i = 0; i < ids.length(); i++)
      {
        String oid = ids.getString(i);
        JSONObject cField = values.getJSONObject(oid);
        String universalId = cField.getString("universal");

        lowest = this.setLowest(lowest, universalId);
        
        TargetFieldIF field = this.createMdGeoEntity(mdBusiness, sheetName, country, cField);

        definition.addField(field);

        // Create the geoNode
        if (!references.contains(field.getLabel()))
        {
          String key = field.getKey();

          GeoNodeEntity node = new GeoNodeEntity();
          node.setKeyName(key);
          node.setGeoEntityAttribute(MdAttributeReference.getByKey(key));
          node.apply();

          nodes.add(node);
        }
      }
    }

    /*
     * Add all of the coordinate fields
     */
    if (cSheet.has("coordinates"))
    {
      Object object = cSheet.get("coordinates");

      if (object instanceof JSONObject)
      {
        JSONObject cCoordinates = (JSONObject) object;
        JSONObject values = cCoordinates.getJSONObject("values");
        JSONArray ids = cCoordinates.getJSONArray("ids");

        for (int i = 0; i < ids.length(); i++)
        {
          String oid = ids.getString(i);
          JSONObject cField = values.getJSONObject(oid);
          
          lowest = this.createGeoNodeGeometry(sheetName, nodes, country, lowest, definition, mdBusiness, cField);
        }
      }
      else
      {
        JSONArray cCoordinates = (JSONArray) object;

        for (int i = 0; i < cCoordinates.length(); i++)
        {
          JSONObject cField = cCoordinates.getJSONObject(i);
          
          lowest = this.createGeoNodeGeometry(sheetName, nodes, country, lowest, definition, mdBusiness, cField);
        }
      }
        
    }

    /*
     * Create the MappableClass
     */
    MdClass mdClass = MdClass.getMdClass(mdBusiness.definesType());

    MappableClass mClass = new MappableClass();
    mClass.setWrappedMdClass(mdClass);
    mClass.setDataSource(dSource);
    mClass.apply();

    mClass.addUniversal(lowest).apply();

    for (GeoNode node : nodes)
    {
      mClass.addGeoNode(node).apply();
    }

    List<TargetFieldIF> fields = definition.getFields();

    for (TargetFieldIF field : fields)
    {
      MdAttribute mdAttribute = MdAttribute.getByKey(field.getKey());

      MappableAttribute mAttribute = new MappableAttribute();
      mAttribute.setWrappedMdAttribute(mdAttribute);
      mAttribute.setAggregatable(field.getAggregatable());
      mAttribute.apply();
    }

    /*
     * Assign permissions
     */
    List<ConfigurationIF> configurations = ConfigurationService.getConfigurations();

    for (ConfigurationIF configuration : configurations)
    {
      configuration.configurePermissions(mdBusiness);
    }

    /*
     * Create the persistence strategy
     */
    LocalPersistenceStrategy strategy = new LocalPersistenceStrategy();
    strategy.apply();

    definition.setStrategy(strategy);

    return definition;
  }

  private Universal createGeoNodeGeometry(String sheetName, List<GeoNode> nodes, GeoEntity country, Universal lowest, TargetDefinition definition, MdBusinessDAO mdBusiness, JSONObject cField) throws JSONException
  {
    String universalId = cField.getString("universal");

    if (universalId != null && universalId.length() > 0)
    {
      lowest = this.setLowest(lowest, universalId);
    }

    TargetFieldIF point = this.createMdPoint(mdBusiness, sheetName, cField);
    TargetFieldIF multiPolygon = this.createMdMultiPolygon(mdBusiness, sheetName, cField);
    TargetFieldIF featureId = this.createFeatureId(mdBusiness, sheetName, cField);
    TargetFieldIF location = this.createLocationField(mdBusiness, sheetName, cField, country, definition);
    TargetFieldIF featureLabel = definition.getFieldByLabel(cField.getString("featureLabel"));

    definition.addField(point);
    definition.addField(multiPolygon);
    definition.addField(featureId);
    definition.addField(location);

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
    
    return lowest;
  }

  private Set<String> getReferencedLocationAttributes(JSONObject cSheet) throws JSONException
  {
    Set<String> locations = new TreeSet<String>();

    if (cSheet.has("coordinates"))
    {
      Object object = cSheet.get("coordinates");

      if (object instanceof JSONObject)
      {
        JSONObject cCoordinates = (JSONObject) object;
        JSONObject values = cCoordinates.getJSONObject("values");
        JSONArray ids = cCoordinates.getJSONArray("ids");

        for (int i = 0; i < ids.length(); i++)
        {
          String oid = ids.getString(i);
          JSONObject cField = values.getJSONObject(oid);
          String location = cField.getString("location");

          locations.add(location);
        }
      }
      else
      {
        JSONArray cCoordinates = (JSONArray) object;

        for (int i = 0; i < cCoordinates.length(); i++)
        {
          JSONObject cField = cCoordinates.getJSONObject(i);
          String location = cField.getString("location");

          locations.add(location);
        }
      }
    }

    return locations;
  }

  private Universal setLowest(Universal current, String universalId)
  {
    Universal universal = Universal.get(universalId);

    AllowedInAllPathsTableQuery query = new AllowedInAllPathsTableQuery(new QueryFactory());
    query.WHERE(query.getParentTerm().EQ(current));
    query.AND(query.getChildTerm().EQ(universal));

    if (query.getCount() > 0)
    {
      return universal;
    }

    return current;
  }

  private TargetFieldIF createLocationField(MdClassDAOIF mdClass, String sheetName, JSONObject cCoordinate, GeoEntity country, TargetDefinition definition) throws JSONException
  {
    String location = cCoordinate.getString("location");

    if (location.equals(DERIVE))
    {
      String label = cCoordinate.getString("label");
      String latitude = cCoordinate.getString("latitude");
      String longitude = cCoordinate.getString("longitude");
      String universalId = cCoordinate.getString("universal");
      Boolean aggregatable = this.getAggregatable(cCoordinate);

      String attributeName = this.generateAttributeName(label) + "Entity";

      MdAttributeReferenceDAO mdAttribute = MdAttributeReferenceDAO.newInstance();
      mdAttribute.setValue(MdAttributeReferenceInfo.NAME, attributeName);
      mdAttribute.setValue(MdAttributeReferenceInfo.DEFINING_MD_CLASS, mdClass.getOid());
      mdAttribute.setStructValue(MdAttributeReferenceInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, label);
      mdAttribute.setValue(MdAttributeReferenceInfo.REF_MD_ENTITY, MdBusinessDAO.getMdBusinessDAO(GeoEntity.CLASS).getOid());
      mdAttribute.apply();

      TargetFieldDerived field = new TargetFieldDerived();
      field.setName(attributeName);
      field.setLabel(label);
      field.setKey(mdClass.definesType() + "." + attributeName);
      field.setLatitudeSourceAttributeName(this.source.getFieldByLabel(sheetName, latitude).getAttributeName());
      field.setLongitudeSourceAttributeName(this.source.getFieldByLabel(sheetName, longitude).getAttributeName());
      field.setUniversal(Universal.get(universalId));
      field.setCountry(country);
      field.setAggregatable(aggregatable);

      /*
       * Create the synonym restore attribute
       */
      MdAttributeReferenceDAO synonymAttribute = MdAttributeReferenceDAO.newInstance();
      synonymAttribute.setValue(MdAttributeReferenceInfo.NAME, attributeName + "Synonym");
      synonymAttribute.setValue(MdAttributeReferenceInfo.DEFINING_MD_CLASS, mdClass.getOid());
      synonymAttribute.setStructValue(MdAttributeReferenceInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, label + " Synonym");
      synonymAttribute.setValue(MdAttributeReferenceInfo.REF_MD_ENTITY, MdClassDAO.getMdTypeDAO(ClassifierSynonym.CLASS).getOid());
      synonymAttribute.apply();

      RelationshipDAO synonymRelationship = RelationshipDAO.newInstance(mdAttribute.getOid(), synonymAttribute.getOid(), TermSynonymRelationship.CLASS);
      synonymRelationship.setValue(RelationshipInfo.KEY, mdAttribute.getKey() + "-" + synonymAttribute.getKey());
      synonymRelationship.apply();

      return field;
    }

    return definition.getFieldByLabel(location);
  }

  private boolean isValid(JSONObject cField) throws JSONException
  {
    String type = cField.getString("type");

    if (type.equals(ColumnType.BOOLEAN.name()))
    {
      return true;
    }
    else if (type.equals(ColumnType.DATE.name()))
    {
      return true;
    }
    else if (type.equals(ColumnType.DOUBLE.name()))
    {
      return true;
    }
    else if (type.equals(ColumnType.LONG.name()))
    {
      return true;
    }
    else if (type.equals(ColumnType.TEXT.name()))
    {
      return true;
    }
    else if (type.equals(ColumnType.CATEGORY.name()))
    {
      return true;
    }
    else if (type.equals(ColumnType.DOMAIN.name()))
    {
      return true;
    }

    return false;
  }

  private TargetFieldIF createMdAttribute(MdClassDAO mdClass, String sheetName, JSONObject cField, Classifier root) throws JSONException
  {
    String columnType = cField.getString("type");
    String columnName = cField.getString("name");
    String label = cField.getString("label");
    String attributeName = this.generateAttributeName(label);
    String sourceAttributeName = this.source.getFieldByName(sheetName, columnName).getAttributeName();
    String key = mdClass.definesType() + "." + attributeName;
    Boolean aggregatable = this.getAggregatable(cField);

    // Create the attribute
    if (columnType.equals(ColumnType.CATEGORY.name()))
    {
      if (!cField.has("root") || cField.getString("root").length() == 0 || cField.getString("root").equals("new"))
      {
        MdAttributeTermDAO mdAttribute = createMdAttributeTerm(mdClass, label, attributeName);

        /*
         * Create the root term for the options
         */
        Classifier classifier = Classifier.findClassifierRoot(mdAttribute);

        if (classifier == null)
        {
          String categoryLabel = cField.getString("categoryLabel");

          classifier = new Classifier();
          classifier.setClassifierId(attributeName);
          classifier.setClassifierPackage(IDGenerator.nextID());
          classifier.getDisplayLabel().setValue(categoryLabel);
          classifier.setCategory(true);
          classifier.apply();

          classifier.addLink(root, ClassifierIsARelationship.CLASS).apply();
        }

        /*
         * Add the root as an option to the MdAttributeTerm
         */
        String relationshipType = ( (TermAttributeDAOIF) mdAttribute ).getAttributeRootRelationshipType();

        RelationshipDAO relationship = RelationshipDAO.newInstance(mdAttribute.getOid(), classifier.getOid(), relationshipType);
        relationship.setValue(RelationshipInfo.KEY, mdAttribute.getKey() + "-" + classifier.getKey());
        relationship.apply();

        TargetFieldClassifier field = new TargetFieldClassifier();
        field.setName(attributeName);
        field.setLabel(label);
        field.setKey(key);
        field.setSourceAttributeName(sourceAttributeName);
        field.setPackageName(classifier.getClassifierPackage());
        field.setAggregatable(aggregatable);

        return field;
      }
      else
      {
        MdAttributeTermDAO mdAttribute = createMdAttributeTerm(mdClass, label, attributeName);

        String classifierId = cField.getString("root");

        Classifier classifier = Classifier.get(classifierId);

        /*
         * Add the root as an option to the MdAttributeTerm
         */
        String relationshipType = ( (TermAttributeDAOIF) mdAttribute ).getAttributeRootRelationshipType();

        RelationshipDAO relationship = RelationshipDAO.newInstance(mdAttribute.getOid(), classifier.getOid(), relationshipType);
        relationship.setValue(RelationshipInfo.KEY, mdAttribute.getKey() + "-" + classifier.getKey());
        relationship.apply();

        TargetFieldDomain field = new TargetFieldDomain();
        field.setName(attributeName);
        field.setLabel(label);
        field.setKey(key);
        field.setSourceAttributeName(sourceAttributeName);
        field.setAggregatable(aggregatable);

        return field;
      }
    }
    else if (columnType.equals(ColumnType.BOOLEAN.name()))
    {
      MdAttributeBooleanDAO mdAttribute = MdAttributeBooleanDAO.newInstance();
      mdAttribute.setValue(MdAttributeBooleanInfo.NAME, attributeName);
      mdAttribute.setValue(MdAttributeBooleanInfo.DEFINING_MD_CLASS, mdClass.getOid());
      mdAttribute.setStructValue(MdAttributeBooleanInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, label);
      mdAttribute.setStructValue(MdAttributeBooleanInfo.NEGATIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "False");
      mdAttribute.setStructValue(MdAttributeBooleanInfo.POSITIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "True");
      mdAttribute.apply();
    }
    else if (columnType.equals(ColumnType.DATE.name()))
    {
      MdAttributeDateDAO mdAttribute = MdAttributeDateDAO.newInstance();
      mdAttribute.setValue(MdAttributeDateInfo.NAME, attributeName);
      mdAttribute.setValue(MdAttributeDateInfo.DEFINING_MD_CLASS, mdClass.getOid());
      mdAttribute.setStructValue(MdAttributeDateInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, label);
      mdAttribute.apply();
    }
    else if (columnType.equals(ColumnType.DOUBLE.name()))
    {
      int length = cField.getInt("precision");
      int decimal = cField.getInt("scale");

      MdAttributeDoubleDAO mdAttribute = MdAttributeDoubleDAO.newInstance();
      mdAttribute.setValue(MdAttributeDoubleInfo.NAME, attributeName);
      mdAttribute.setValue(MdAttributeDoubleInfo.DEFINING_MD_CLASS, mdClass.getOid());
      mdAttribute.setStructValue(MdAttributeDoubleInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, label);
      mdAttribute.setValue(MdAttributeDoubleInfo.LENGTH, new Integer(length).toString());
      mdAttribute.setValue(MdAttributeDoubleInfo.DECIMAL, new Integer(decimal).toString());
      mdAttribute.apply();
    }
    else if (columnType.equals(ColumnType.LONG.name()))
    {
      MdAttributeLongDAO mdAttribute = MdAttributeLongDAO.newInstance();
      mdAttribute.setValue(MdAttributeLongInfo.NAME, attributeName);
      mdAttribute.setValue(MdAttributeLongInfo.DEFINING_MD_CLASS, mdClass.getOid());
      mdAttribute.setStructValue(MdAttributeLongInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, label);
      mdAttribute.apply();
    }
    else if (columnType.equals(ColumnType.TEXT.name()))
    {
      MdAttributeTextDAO mdAttribute = MdAttributeTextDAO.newInstance();
      mdAttribute.setValue(MdAttributeTextInfo.NAME, attributeName);
      mdAttribute.setValue(MdAttributeTextInfo.DEFINING_MD_CLASS, mdClass.getOid());
      mdAttribute.setStructValue(MdAttributeTextInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, label);
      // mdAttribute.setValue(MdAttributeTextInfo.SIZE, "6000");
      mdAttribute.apply();
    }

    // Create the target field
    TargetFieldBasic field = new TargetFieldBasic();
    field.setName(attributeName);
    field.setLabel(label);
    field.setKey(key);
    field.setSourceAttributeName(sourceAttributeName);
    field.setAggregatable(aggregatable);

    return field;
  }

  private MdAttributeTermDAO createMdAttributeTerm(MdClassDAO mdClass, String label, String attributeName)
  {
    MdBusinessDAOIF referenceMdBusiness = MdBusinessDAO.getMdBusinessDAO(Classifier.CLASS);

    MdAttributeTermDAO mdAttribute = MdAttributeTermDAO.newInstance();
    mdAttribute.setValue(MdAttributeReferenceInfo.NAME, attributeName);
    mdAttribute.setValue(MdAttributeReferenceInfo.DEFINING_MD_CLASS, mdClass.getOid());
    mdAttribute.setStructValue(MdAttributeReferenceInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, label);
    mdAttribute.setValue(MdAttributeReferenceInfo.REF_MD_ENTITY, referenceMdBusiness.getOid());
    mdAttribute.apply();

    /*
     * Create the synonym restore attribute
     */
    MdAttributeReferenceDAO synonymAttribute = MdAttributeReferenceDAO.newInstance();
    synonymAttribute.setValue(MdAttributeReferenceInfo.NAME, attributeName + "Synonym");
    synonymAttribute.setValue(MdAttributeReferenceInfo.DEFINING_MD_CLASS, mdClass.getOid());
    synonymAttribute.setStructValue(MdAttributeReferenceInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, label + " Synonym");
    synonymAttribute.setValue(MdAttributeReferenceInfo.REF_MD_ENTITY, MdClassDAO.getMdTypeDAO(ClassifierSynonym.CLASS).getOid());
    synonymAttribute.apply();

    RelationshipDAO synonymRelationship = RelationshipDAO.newInstance(mdAttribute.getOid(), synonymAttribute.getOid(), TermSynonymRelationship.CLASS);
    synonymRelationship.setValue(RelationshipInfo.KEY, mdAttribute.getKey() + "-" + synonymAttribute.getKey());
    synonymRelationship.apply();
    return mdAttribute;
  }

  private Boolean getAggregatable(JSONObject cField) throws JSONException
  {
    return cField.has("ratio") ? !cField.getBoolean("ratio") : true;
  }

  private TargetFieldIF createMdGeoEntity(MdClassDAO mdClass, String sheetName, GeoEntity country, JSONObject cAttribute) throws JSONException
  {
    String label = cAttribute.getString("label");
    String universalId = cAttribute.getString("universal");
    boolean useCoordinatesForLocationAssignment = false;
    if(cAttribute.has("useCoordinatesForLocationAssignment"))
    {
      useCoordinatesForLocationAssignment = cAttribute.getBoolean("useCoordinatesForLocationAssignment");
    }
    Boolean aggregatable = this.getAggregatable(cAttribute);
    String attributeName = this.generateAttributeName(label);
    

    MdAttributeTermDAO mdAttribute = MdAttributeTermDAO.newInstance();
    mdAttribute.setValue(MdAttributeReferenceInfo.NAME, attributeName);
    mdAttribute.setValue(MdAttributeReferenceInfo.DEFINING_MD_CLASS, mdClass.getOid());
    mdAttribute.setValue(MdAttributeReferenceInfo.REF_MD_ENTITY, MdBusinessDAO.getMdBusinessDAO(GeoEntity.CLASS).getOid());
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
    field.setAggregatable(aggregatable);
    field.setUseCoordinatesForLocationAssignment(useCoordinatesForLocationAssignment);
    
    if(useCoordinatesForLocationAssignment)
    {
    	String latForLocationAssignment = cAttribute.getString("latForLocationAssignment");
    	String longForLocationAssignment = cAttribute.getString("longForLocationAssignment");
    	
    	field.setCoordinateObject(latForLocationAssignment, longForLocationAssignment);
    }

    JSONObject fields = cAttribute.getJSONObject("fields");

    for (Term universal : universals)
    {
      if (fields.has(universal.getOid()))
      {
        String sLabel = fields.getString(universal.getOid());

        if (!sLabel.equals(EXLUDE))
        {
          SourceFieldIF sField = source.getFieldByLabel(sheetName, sLabel);

          field.addUniversalAttribute(sField.getAttributeName(), sLabel, (Universal) universal);
        }
      }
    }

    /*
     * Create the synonym restore attribute
     */
    MdAttributeReferenceDAO synonymAttribute = MdAttributeReferenceDAO.newInstance();
    synonymAttribute.setValue(MdAttributeReferenceInfo.NAME, attributeName + "Synonym");
    synonymAttribute.setValue(MdAttributeReferenceInfo.DEFINING_MD_CLASS, mdClass.getOid());
    synonymAttribute.setStructValue(MdAttributeReferenceInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, label + " Synonym");
    synonymAttribute.setValue(MdAttributeReferenceInfo.REF_MD_ENTITY, MdClassDAO.getMdTypeDAO(ClassifierSynonym.CLASS).getOid());
    synonymAttribute.apply();

    RelationshipDAO synonymRelationship = RelationshipDAO.newInstance(mdAttribute.getOid(), synonymAttribute.getOid(), TermSynonymRelationship.CLASS);
    synonymRelationship.setValue(RelationshipInfo.KEY, mdAttribute.getKey() + "-" + synonymAttribute.getKey());
    synonymRelationship.apply();

    return field;
  }

  private TargetFieldIF createMdMultiPolygon(MdClassDAO mdClass, String sheetName, JSONObject cCoordinate) throws JSONException
  {
    String label = cCoordinate.getString("label");
    String latitude = cCoordinate.getString("latitude");
    String longitude = cCoordinate.getString("longitude");
    Boolean aggregatable = this.getAggregatable(cCoordinate);

    String attributeName = this.generateAttributeName(label) + "MultiPolygon";

    MdAttributeMultiPolygonDAO mdAttribute = MdAttributeMultiPolygonDAO.newInstance();
    mdAttribute.setValue(MdAttributeMultiPolygonInfo.NAME, attributeName);
    mdAttribute.setValue(MdAttributeMultiPolygonInfo.DEFINING_MD_CLASS, mdClass.getOid());
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
    field.setAggregatable(aggregatable);

    return field;
  }

  private TargetFieldIF createMdPoint(MdClassDAO mdClass, String sheetName, JSONObject cCoordinate) throws JSONException
  {
    String label = cCoordinate.getString("label");
    String latitude = cCoordinate.getString("latitude");
    String longitude = cCoordinate.getString("longitude");
    Boolean aggregatable = this.getAggregatable(cCoordinate);

    String attributeName = this.generateAttributeName(label) + "Point";

    MdAttributePointDAO mdAttribute = MdAttributePointDAO.newInstance();
    mdAttribute.setValue(MdAttributePointInfo.NAME, attributeName);
    mdAttribute.setValue(MdAttributePointInfo.DEFINING_MD_CLASS, mdClass.getOid());
    mdAttribute.setStructValue(MdAttributePointInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, label);
    mdAttribute.setValue(MdAttributePointInfo.DIMENSION, "2");
    mdAttribute.setValue(MdAttributePointInfo.SRID, "4326");
    mdAttribute.apply();

    SourceFieldIF latField = this.source.getFieldByLabel(sheetName, latitude);
    SourceFieldIF longField = this.source.getFieldByLabel(sheetName, longitude);

    TargetFieldPoint field = new TargetFieldPoint();
    field.setName(attributeName);
    field.setLabel(label);
    field.setKey(mdClass.definesType() + "." + attributeName);
    field.setLatitudeSourceAttributeName(latField.getAttributeName());
    field.setLatitudeLabel(latField.getLabel());
    field.setLongitudeSourceAttributeName(longField.getAttributeName());
    field.setLongitudeLabel(latField.getLabel());
    field.setAggregatable(aggregatable);

    return field;
  }

  private TargetFieldIF createFeatureId(MdClassDAO mdClass, String sheetName, JSONObject cCoordinate) throws JSONException
  {
    String label = cCoordinate.getString("label");

    String attributeName = this.generateAttributeName(label) + "FeatureId";

    MdAttributeCharacterDAO mdAttribute = MdAttributeCharacterDAO.newInstance();
    mdAttribute.setValue(MdAttributeCharacterInfo.NAME, attributeName);
    mdAttribute.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, mdClass.getOid());
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
    String name = DataUploader.getSystemName(label, "Attribute", false);

    return name;
  }

  private String generateBusinessTypeName(String label)
  {
    // Create a unique name for the view type based upon the name of the sheet
    String typeName = DataUploader.getSystemName(label, "Type", true);

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
    if (suffix > 0)
    {
      typeName = typeName + suffix;
    }

    MdBusinessQuery query = new MdBusinessQuery(new QueryFactory());
    query.WHERE(query.getTypeName().EQ(typeName));
    query.AND(query.getPackageName().EQ(packageName));

    return ( query.getCount() == 0 );
  }

  public void setupLocationExclusions()
  {
    try
    {
      Map<String, Set<String>> exclusions = new HashMap<String, Set<String>>();

      if (this.configuration.has("locationExclusions"))
      {
        JSONArray locationExclusionsArr = this.configuration.getJSONArray("locationExclusions");

        // convert to native Java data structure rather than JSON
        for (int i = 0; i < locationExclusionsArr.length(); i++)
        {
          JSONObject object = locationExclusionsArr.getJSONObject(i);

          String universal = object.getString("universal");
          String parentOid = object.getString("parentOid");
          String label = object.getString("locationLabel");

          String key = universal + "-" + parentOid;

          exclusions.putIfAbsent(key, new HashSet<String>());
          exclusions.get(key).add(label);
        }
      }

      this.target.setLocationExclusions(exclusions);
    }
    catch (JSONException e)
    {
      String devMsg = "Could not parse dataset configuration string to JSON.";
      throw new ProgrammingErrorException(devMsg, e);
    }
  }

  @SuppressWarnings("unchecked")
  public void setupCategoryExclusions()
  {
    try
    {
      Map<String, Set<String>> exclusions = new HashMap<String, Set<String>>();

      if (this.configuration.has("categoryExclusion"))
      {
        JSONObject categoryExclusion = this.configuration.getJSONObject("categoryExclusion");

        Iterator<String> keys = categoryExclusion.keys();

        while (keys.hasNext())
        {
          String mdAttributeId = keys.next();

          exclusions.putIfAbsent(mdAttributeId, new HashSet<String>());

          JSONArray labels = categoryExclusion.getJSONArray(mdAttributeId);

          for (int i = 0; i < labels.length(); i++)
          {
            String label = labels.getString(i);

            exclusions.get(mdAttributeId).add(label);
          }
        }
      }

      this.target.setCategoryExclusions(exclusions);
    }
    catch (JSONException e)
    {
      String devMsg = "Could not parse dataset configuration string to JSON.";
      throw new ProgrammingErrorException(devMsg, e);
    }
  }
}
