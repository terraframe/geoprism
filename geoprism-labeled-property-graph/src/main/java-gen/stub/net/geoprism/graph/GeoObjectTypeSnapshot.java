package net.geoprism.graph;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.commongeoregistry.adapter.constants.DefaultAttribute;
import org.commongeoregistry.adapter.constants.GeometryType;
import org.commongeoregistry.adapter.dataaccess.LocalizedValue;
import org.commongeoregistry.adapter.metadata.AttributeBooleanType;
import org.commongeoregistry.adapter.metadata.AttributeCharacterType;
import org.commongeoregistry.adapter.metadata.AttributeClassificationType;
import org.commongeoregistry.adapter.metadata.AttributeDateType;
import org.commongeoregistry.adapter.metadata.AttributeFloatType;
import org.commongeoregistry.adapter.metadata.AttributeIntegerType;
import org.commongeoregistry.adapter.metadata.AttributeLocalType;
import org.commongeoregistry.adapter.metadata.AttributeTermType;
import org.commongeoregistry.adapter.metadata.AttributeType;
import org.commongeoregistry.adapter.metadata.GeoObjectType;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.runwaysdk.business.BusinessFacade;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeCharacterInfo;
import com.runwaysdk.constants.MdAttributeConcreteInfo;
import com.runwaysdk.constants.MdAttributeDoubleInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.graph.MdVertexInfo;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.graph.GraphDBService;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.gis.constants.MdAttributeLineStringInfo;
import com.runwaysdk.gis.constants.MdAttributeMultiLineStringInfo;
import com.runwaysdk.gis.constants.MdAttributeMultiPointInfo;
import com.runwaysdk.gis.constants.MdAttributeMultiPolygonInfo;
import com.runwaysdk.gis.constants.MdAttributePointInfo;
import com.runwaysdk.gis.constants.MdAttributePolygonInfo;
import com.runwaysdk.gis.constants.MdAttributeShapeInfo;
import com.runwaysdk.gis.dataaccess.MdAttributeGeometryDAOIF;
import com.runwaysdk.gis.dataaccess.metadata.MdAttributeLineStringDAO;
import com.runwaysdk.gis.dataaccess.metadata.MdAttributeMultiLineStringDAO;
import com.runwaysdk.gis.dataaccess.metadata.MdAttributeMultiPointDAO;
import com.runwaysdk.gis.dataaccess.metadata.MdAttributeMultiPolygonDAO;
import com.runwaysdk.gis.dataaccess.metadata.MdAttributePointDAO;
import com.runwaysdk.gis.dataaccess.metadata.MdAttributePolygonDAO;
import com.runwaysdk.gis.dataaccess.metadata.MdAttributeShapeDAO;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.system.metadata.MdAttributeBoolean;
import com.runwaysdk.system.metadata.MdAttributeCharacter;
import com.runwaysdk.system.metadata.MdAttributeConcrete;
import com.runwaysdk.system.metadata.MdAttributeDateTime;
import com.runwaysdk.system.metadata.MdAttributeDouble;
import com.runwaysdk.system.metadata.MdAttributeIndices;
import com.runwaysdk.system.metadata.MdAttributeLocalCharacterEmbedded;
import com.runwaysdk.system.metadata.MdAttributeLocalText;
import com.runwaysdk.system.metadata.MdAttributeLong;
import com.runwaysdk.system.metadata.MdClass;
import com.runwaysdk.system.metadata.MdGraphClass;
import com.runwaysdk.system.metadata.MdGraphClassQuery;
import com.runwaysdk.system.metadata.MdVertex;

import net.geoprism.graph.conversion.AttributeTypeConverter;
import net.geoprism.graph.conversion.LocalizedValueConverter;
import net.geoprism.graph.service.LabeledPropertyGraphServiceIF;

public class GeoObjectTypeSnapshot extends GeoObjectTypeSnapshotBase
{
  public static final String TABLE_PACKAGE    = "net.geoprism.lpg";

  public static final String PREFIX           = "g_";

  public static final String SPLIT            = "__";

  @SuppressWarnings("unused")
  private static final long  serialVersionUID = -1232639915;

  public GeoObjectTypeSnapshot()
  {
    super();
  }

  @Override
  public String toString()
  {
    return this.getCode();
  }

  public boolean isRoot()
  {
    return StringUtils.isEmpty(this.getGeometryType());
  }

  @Override
  public void delete()
  {
    MdVertex mdVertex = this.getGraphMdVertex();

    super.delete();

    mdVertex.delete();
  }

  public void truncate()
  {
    MdVertex mdVertex = this.getGraphMdVertex();

    GraphDBService service = GraphDBService.getInstance();
    service.command(service.getGraphDBRequest(), "DELETE VERTEX FROM " + mdVertex.getDbClassName(), new HashMap<>());
  }

  public List<AttributeType> getAttributeTypes()
  {
    AttributeTypeConverter converter = new AttributeTypeConverter();

    List<AttributeType> attributes = new LinkedList<>();

    MdVertexDAOIF mdVertex = MdVertexDAO.get(this.getGraphMdVertexOid());
    List<? extends MdAttributeConcreteDAOIF> mdAttributes = mdVertex.definesAttributes();
    mdAttributes.forEach(attribute -> {
      if (! ( attribute instanceof MdAttributeGeometryDAOIF ))
      {
        attributes.add(converter.build(attribute));
      }
    });
    return attributes;
  }

  public GeoObjectType toGeoObjectType()
  {
    String code = this.getCode();
    GeometryType geometryType = GeometryType.valueOf(this.getGeometryType());
    LocalizedValue label = LocalizedValueConverter.convertNoAutoCoalesce(this.getDisplayLabel());
    LocalizedValue description = LocalizedValueConverter.convertNoAutoCoalesce(this.getDescription());

    GeoObjectType type = new GeoObjectType(code, geometryType, label, description, getIsGeometryEditable(), null, null);
    type.setIsAbstract(this.getIsAbstract());
    type.setIsPrivate(this.getIsPrivate());

    List<AttributeType> attributes = getAttributeTypes();

    for (AttributeType attribute : attributes)
    {
      type.addAttribute(attribute);
    }

    return type;
  }

  public JsonObject toJSON()
  {
    JsonArray attributes = new JsonArray();

    this.getAttributeTypes().forEach(attribute -> attributes.add(attribute.toJSON()));

    JsonObject typeObject = new JsonObject();
    typeObject.addProperty(CODE, this.getCode());
    typeObject.add(DISPLAYLABEL, LocalizedValueConverter.convertNoAutoCoalesce(this.getDisplayLabel()).toJSON());
    typeObject.add(DESCRIPTION, LocalizedValueConverter.convertNoAutoCoalesce(this.getDescription()).toJSON());
    typeObject.addProperty(GEOMETRYTYPE, this.getGeometryType());
    typeObject.addProperty(ISABSTRACT, this.getIsAbstract());
    typeObject.addProperty(ISROOT, this.getIsRoot());
    typeObject.addProperty(ISPRIVATE, this.getIsPrivate());
    typeObject.add("attributes", attributes);

    GeoObjectTypeSnapshot parent = this.getParent();

    if (parent != null)
    {
      typeObject.addProperty(PARENT, parent.getCode());
    }

    return typeObject;
  }

  public static void createGeometryAttribute(GeometryType geometryType, MdVertexDAO mdTableDAO)
  {
    // Create the geometry attribute
    if (geometryType.equals(GeometryType.LINE))
    {
      MdAttributeLineStringDAO mdAttributeLineStringDAO = MdAttributeLineStringDAO.newInstance();
      mdAttributeLineStringDAO.setValue(MdAttributeLineStringInfo.NAME, DefaultAttribute.GEOMETRY.getName());
      mdAttributeLineStringDAO.setStructValue(MdAttributeLineStringInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, DefaultAttribute.GEOMETRY.getName());
      mdAttributeLineStringDAO.setStructValue(MdAttributeLineStringInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, DefaultAttribute.GEOMETRY.getName());
      mdAttributeLineStringDAO.setValue(MdAttributeLineStringInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
      mdAttributeLineStringDAO.setValue(MdAttributeLineStringInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
      mdAttributeLineStringDAO.setValue(MdAttributeLineStringInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
      mdAttributeLineStringDAO.setValue(MdAttributeLineStringInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
      mdAttributeLineStringDAO.setValue(MdAttributeLineStringInfo.SRID, "4326");
      mdAttributeLineStringDAO.setValue(MdAttributeLineStringInfo.DEFINING_MD_CLASS, mdTableDAO.getOid());
      mdAttributeLineStringDAO.apply();

    }
    else if (geometryType.equals(GeometryType.MULTILINE))
    {
      MdAttributeMultiLineStringDAO mdAttributeMultiLineStringDAO = MdAttributeMultiLineStringDAO.newInstance();
      mdAttributeMultiLineStringDAO.setValue(MdAttributeMultiLineStringInfo.NAME, DefaultAttribute.GEOMETRY.getName());
      mdAttributeMultiLineStringDAO.setStructValue(MdAttributeMultiLineStringInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, DefaultAttribute.GEOMETRY.getName());
      mdAttributeMultiLineStringDAO.setStructValue(MdAttributeMultiLineStringInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, DefaultAttribute.GEOMETRY.getName());
      mdAttributeMultiLineStringDAO.setValue(MdAttributeMultiLineStringInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
      mdAttributeMultiLineStringDAO.setValue(MdAttributeMultiLineStringInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
      mdAttributeMultiLineStringDAO.setValue(MdAttributeMultiLineStringInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
      mdAttributeMultiLineStringDAO.setValue(MdAttributeMultiLineStringInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
      mdAttributeMultiLineStringDAO.setValue(MdAttributeMultiLineStringInfo.SRID, "4326");
      mdAttributeMultiLineStringDAO.setValue(MdAttributeMultiLineStringInfo.DEFINING_MD_CLASS, mdTableDAO.getOid());
      mdAttributeMultiLineStringDAO.apply();
    }
    else if (geometryType.equals(GeometryType.POINT))
    {
      MdAttributePointDAO mdAttributePointDAO = MdAttributePointDAO.newInstance();
      mdAttributePointDAO.setValue(MdAttributePointInfo.NAME, DefaultAttribute.GEOMETRY.getName());
      mdAttributePointDAO.setStructValue(MdAttributePointInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, DefaultAttribute.GEOMETRY.getName());
      mdAttributePointDAO.setStructValue(MdAttributePointInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, DefaultAttribute.GEOMETRY.getName());
      mdAttributePointDAO.setValue(MdAttributePointInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
      mdAttributePointDAO.setValue(MdAttributePointInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
      mdAttributePointDAO.setValue(MdAttributePointInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
      mdAttributePointDAO.setValue(MdAttributePointInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
      mdAttributePointDAO.setValue(MdAttributePointInfo.SRID, "4326");
      mdAttributePointDAO.setValue(MdAttributePointInfo.DEFINING_MD_CLASS, mdTableDAO.getOid());
      mdAttributePointDAO.apply();
    }
    else if (geometryType.equals(GeometryType.MULTIPOINT))
    {
      MdAttributeMultiPointDAO mdAttributeMultiPointDAO = MdAttributeMultiPointDAO.newInstance();
      mdAttributeMultiPointDAO.setValue(MdAttributeMultiPointInfo.NAME, DefaultAttribute.GEOMETRY.getName());
      mdAttributeMultiPointDAO.setStructValue(MdAttributeMultiPointInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, DefaultAttribute.GEOMETRY.getName());
      mdAttributeMultiPointDAO.setStructValue(MdAttributeMultiPointInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, DefaultAttribute.GEOMETRY.getName());
      mdAttributeMultiPointDAO.setValue(MdAttributeMultiPointInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
      mdAttributeMultiPointDAO.setValue(MdAttributeMultiPointInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
      mdAttributeMultiPointDAO.setValue(MdAttributeMultiPointInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
      mdAttributeMultiPointDAO.setValue(MdAttributeMultiPointInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
      mdAttributeMultiPointDAO.setValue(MdAttributeMultiPointInfo.SRID, "4326");
      mdAttributeMultiPointDAO.setValue(MdAttributeMultiPointInfo.DEFINING_MD_CLASS, mdTableDAO.getOid());
      mdAttributeMultiPointDAO.apply();
    }
    else if (geometryType.equals(GeometryType.POLYGON))
    {
      MdAttributePolygonDAO mdAttributePolygonDAO = MdAttributePolygonDAO.newInstance();
      mdAttributePolygonDAO.setValue(MdAttributePolygonInfo.NAME, DefaultAttribute.GEOMETRY.getName());
      mdAttributePolygonDAO.setStructValue(MdAttributePolygonInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, DefaultAttribute.GEOMETRY.getName());
      mdAttributePolygonDAO.setStructValue(MdAttributePolygonInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, DefaultAttribute.GEOMETRY.getName());
      mdAttributePolygonDAO.setValue(MdAttributePolygonInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
      mdAttributePolygonDAO.setValue(MdAttributePolygonInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
      mdAttributePolygonDAO.setValue(MdAttributePolygonInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
      mdAttributePolygonDAO.setValue(MdAttributePolygonInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
      mdAttributePolygonDAO.setValue(MdAttributePolygonInfo.SRID, "4326");
      mdAttributePolygonDAO.setValue(MdAttributePolygonInfo.DEFINING_MD_CLASS, mdTableDAO.getOid());
      mdAttributePolygonDAO.apply();
    }
    else if (geometryType.equals(GeometryType.MULTIPOLYGON))
    {
      MdAttributeMultiPolygonDAO mdAttributeMultiPolygonDAO = MdAttributeMultiPolygonDAO.newInstance();
      mdAttributeMultiPolygonDAO.setValue(MdAttributeMultiPolygonInfo.NAME, DefaultAttribute.GEOMETRY.getName());
      mdAttributeMultiPolygonDAO.setStructValue(MdAttributeMultiPolygonInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, DefaultAttribute.GEOMETRY.getName());
      mdAttributeMultiPolygonDAO.setStructValue(MdAttributeMultiPolygonInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, DefaultAttribute.GEOMETRY.getName());
      mdAttributeMultiPolygonDAO.setValue(MdAttributeMultiPolygonInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
      mdAttributeMultiPolygonDAO.setValue(MdAttributeMultiPolygonInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
      mdAttributeMultiPolygonDAO.setValue(MdAttributeMultiPolygonInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
      mdAttributeMultiPolygonDAO.setValue(MdAttributeMultiPolygonInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
      mdAttributeMultiPolygonDAO.setValue(MdAttributeMultiPolygonInfo.SRID, "4326");
      mdAttributeMultiPolygonDAO.setValue(MdAttributeMultiPolygonInfo.DEFINING_MD_CLASS, mdTableDAO.getOid());
      mdAttributeMultiPolygonDAO.apply();
    }
    else if (geometryType.equals(GeometryType.MIXED))
    {
      MdAttributeShapeDAO mdAttributeShapeDAO = MdAttributeShapeDAO.newInstance();
      mdAttributeShapeDAO.setValue(MdAttributeShapeInfo.NAME, DefaultAttribute.GEOMETRY.getName());
      mdAttributeShapeDAO.setStructValue(MdAttributeShapeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, DefaultAttribute.GEOMETRY.getName());
      mdAttributeShapeDAO.setStructValue(MdAttributeShapeInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, DefaultAttribute.GEOMETRY.getName());
      mdAttributeShapeDAO.setValue(MdAttributeShapeInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
      mdAttributeShapeDAO.setValue(MdAttributeShapeInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
      mdAttributeShapeDAO.setValue(MdAttributeShapeInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
      mdAttributeShapeDAO.setValue(MdAttributeShapeInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
      mdAttributeShapeDAO.setValue(MdAttributeShapeInfo.SRID, "4326");
      mdAttributeShapeDAO.setValue(MdAttributeShapeInfo.DEFINING_MD_CLASS, mdTableDAO.getOid());
      mdAttributeShapeDAO.apply();
    }
  }

  public static String getTableName(String className)
  {
    int count = 0;

    String name = PREFIX + count + SPLIT + className;

    if (name.length() > 25)
    {
      name = name.substring(0, 25);
    }

    while (isTableNameInUse(name))
    {
      count++;

      name = PREFIX + count + className;

      if (name.length() > 25)
      {
        name = name.substring(0, 25);
      }
    }

    return name;
  }

  private static boolean isTableNameInUse(String name)
  {
    MdGraphClassQuery query = new MdGraphClassQuery(new QueryFactory());
    query.WHERE(query.getDbClassName().EQ(name));

    return query.getCount() > 0;
  }

  @Transaction
  public static GeoObjectTypeSnapshot createRoot(LabeledPropertyGraphTypeVersion version)
  {
    String viewName = getTableName("root_vertex");

    // Create the MdTable
    MdVertexDAO rootMdVertexDAO = MdVertexDAO.newInstance();
    rootMdVertexDAO.setValue(MdVertexInfo.NAME, viewName);
    rootMdVertexDAO.setValue(MdVertexInfo.PACKAGE, TABLE_PACKAGE);
    rootMdVertexDAO.setStructValue(MdVertexInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Root Type");
    rootMdVertexDAO.setValue(MdVertexInfo.DB_CLASS_NAME, viewName);
    rootMdVertexDAO.setValue(MdVertexInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    rootMdVertexDAO.setValue(MdVertexInfo.ENABLE_CHANGE_OVER_TIME, MdAttributeBooleanInfo.FALSE);
    rootMdVertexDAO.setValue(MdVertexInfo.ABSTRACT, MdAttributeBooleanInfo.TRUE);
    rootMdVertexDAO.apply();

    MdVertex graphMdVertex = (MdVertex) BusinessFacade.get(rootMdVertexDAO);

    GeoObjectTypeSnapshot snapshot = new GeoObjectTypeSnapshot();
    snapshot.setVersion(version);
    snapshot.setGraphMdVertex(graphMdVertex);
    snapshot.setCode(viewName);
    snapshot.setIsAbstract(true);
    snapshot.setIsRoot(true);
    snapshot.setIsPrivate(true);
    LocalizedValueConverter.populate(snapshot.getDisplayLabel(), LocalizedValueConverter.convertNoAutoCoalesce(graphMdVertex.getDisplayLabel()));
    LocalizedValueConverter.populate(snapshot.getDescription(), LocalizedValueConverter.convertNoAutoCoalesce(graphMdVertex.getDescription()));
    snapshot.apply();

    return snapshot;
  }

  @Transaction
  public static GeoObjectTypeSnapshot create(LabeledPropertyGraphTypeVersion version, JsonObject type)
  {
    GeoObjectTypeSnapshot parent = GeoObjectTypeSnapshot.get(version, type.get(PARENT).getAsString());

    String code = type.get(CODE).getAsString();
    String viewName = getTableName(code);
    boolean isAbstract = type.get(ISABSTRACT).getAsBoolean();
    boolean isRoot = type.get(ISROOT).getAsBoolean();
    boolean isPrivate = type.get(ISPRIVATE).getAsBoolean();
    GeometryType geometryType = GeometryType.valueOf(type.get(GEOMETRYTYPE).getAsString());
    LocalizedValue label = LocalizedValue.fromJSON(type.get(DISPLAYLABEL).getAsJsonObject());
    LocalizedValue description = LocalizedValue.fromJSON(type.get(DESCRIPTION).getAsJsonObject());

    // Create the MdTable
    MdVertexDAO mdTableDAO = MdVertexDAO.newInstance();
    mdTableDAO.setValue(MdVertexInfo.NAME, viewName);
    mdTableDAO.setValue(MdVertexInfo.PACKAGE, TABLE_PACKAGE);
    LocalizedValueConverter.populate(mdTableDAO, MdVertexInfo.DISPLAY_LABEL, label);
    LocalizedValueConverter.populate(mdTableDAO, MdVertexInfo.DESCRIPTION, description);
    mdTableDAO.setValue(MdVertexInfo.DB_CLASS_NAME, viewName);
    mdTableDAO.setValue(MdVertexInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdTableDAO.setValue(MdVertexInfo.ENABLE_CHANGE_OVER_TIME, MdAttributeBooleanInfo.FALSE);
    mdTableDAO.setValue(MdVertexInfo.SUPER_MD_VERTEX, parent.getGraphMdVertexOid());
    mdTableDAO.apply();

    MdVertex mdTable = (MdVertex) BusinessFacade.get(mdTableDAO);

    if (!isAbstract)
    {
      createGeometryAttribute(geometryType, mdTableDAO);
    }

    JsonArray attributes = type.get("attributes").getAsJsonArray();

    attributes.forEach(joAttr -> {
      AttributeType attributeType = AttributeType.parse(joAttr.getAsJsonObject());

      if (! ( attributeType instanceof AttributeTermType ) && ! ( attributeType instanceof AttributeClassificationType ))
      {
        GeoObjectTypeSnapshot.createMdAttributeFromAttributeType(mdTable, attributeType);
      }
    });

    GeoObjectTypeSnapshot snapshot = new GeoObjectTypeSnapshot();
    snapshot.setVersion(version);
    snapshot.setGraphMdVertex(mdTable);
    snapshot.setCode(code);
    snapshot.setGeometryType(geometryType.name());
    snapshot.setIsAbstract(isAbstract);
    snapshot.setIsRoot(isRoot);
    snapshot.setIsPrivate(isPrivate);
    LocalizedValueConverter.populate(snapshot.getDisplayLabel(), label);
    LocalizedValueConverter.populate(snapshot.getDescription(), description);
    snapshot.setParent(parent);
    snapshot.apply();

    LabeledPropertyGraphServiceIF.getInstance().assignPermissions(mdTableDAO);

    return snapshot;
  }

  public static GeoObjectTypeSnapshot get(LabeledPropertyGraphTypeVersion version, String code)
  {
    GeoObjectTypeSnapshotQuery query = new GeoObjectTypeSnapshotQuery(new QueryFactory());
    query.WHERE(query.getVersion().EQ(version));
    query.AND(query.getCode().EQ(code));

    try (OIterator<? extends GeoObjectTypeSnapshot> it = query.getIterator())
    {
      if (it.hasNext())
      {
        return it.next();
      }
    }

    return null;
  }

  public static GeoObjectTypeSnapshot getRoot(LabeledPropertyGraphTypeVersion version)
  {
    GeoObjectTypeSnapshotQuery query = new GeoObjectTypeSnapshotQuery(new QueryFactory());
    query.WHERE(query.getVersion().EQ(version));
    query.AND(query.getIsRoot().EQ(true));

    try (OIterator<? extends GeoObjectTypeSnapshot> it = query.getIterator())
    {
      if (it.hasNext())
      {
        return it.next();
      }
    }

    return null;
  }

  public static MdAttributeConcrete createMdAttributeFromAttributeType(MdClass mdClass, AttributeType attributeType)
  {
    MdAttributeConcrete mdAttribute = null;

    if (attributeType.getType().equals(AttributeCharacterType.TYPE))
    {
      mdAttribute = new MdAttributeCharacter();
      MdAttributeCharacter mdAttributeCharacter = (MdAttributeCharacter) mdAttribute;
      mdAttributeCharacter.setDatabaseSize(MdAttributeCharacterInfo.MAX_CHARACTER_SIZE);
    }
    else if (attributeType.getType().equals(AttributeDateType.TYPE))
    {
      mdAttribute = new MdAttributeDateTime();
    }
    else if (attributeType.getType().equals(AttributeIntegerType.TYPE))
    {
      mdAttribute = new MdAttributeLong();
    }
    else if (attributeType.getType().equals(AttributeFloatType.TYPE))
    {
      AttributeFloatType attributeFloatType = (AttributeFloatType) attributeType;

      mdAttribute = new MdAttributeDouble();
      mdAttribute.setValue(MdAttributeDoubleInfo.LENGTH, Integer.toString(attributeFloatType.getPrecision()));
      mdAttribute.setValue(MdAttributeDoubleInfo.DECIMAL, Integer.toString(attributeFloatType.getScale()));
    }
    else if (attributeType.getType().equals(AttributeTermType.TYPE))
    {
      // mdAttribute = new MdAttributeTerm();
      // MdAttributeTerm mdAttributeTerm = (MdAttributeTerm) mdAttribute;
      //
      // MdBusiness classifierMdBusiness =
      // MdBusiness.getMdBusiness(Classifier.CLASS);
      // mdAttributeTerm.setMdBusiness(classifierMdBusiness);
    }
    else if (attributeType.getType().equals(AttributeClassificationType.TYPE))
    {
      // AttributeClassificationType attributeClassificationType =
      // (AttributeClassificationType) attributeType;
      // String classificationTypeCode =
      // attributeClassificationType.getClassificationType();
      //
      // ClassificationType classificationType =
      // ClassificationType.getByCode(classificationTypeCode);
      //
      // mdAttribute = new MdAttributeClassification();
      // MdAttributeClassification mdAttributeTerm = (MdAttributeClassification)
      // mdAttribute;
      // mdAttributeTerm.setReferenceMdClassification(classificationType.getMdClassificationObject());
      //
      // Term root = attributeClassificationType.getRootTerm();
      //
      // if (root != null)
      // {
      // Classification classification = Classification.get(classificationType,
      // root.getCode());
      //
      // if (classification == null)
      // {
      // net.geoprism.registry.DataNotFoundException ex = new
      // net.geoprism.registry.DataNotFoundException();
      // ex.setTypeLabel(classificationType.getDisplayLabel().getValue());
      // ex.setDataIdentifier(root.getCode());
      // ex.setAttributeLabel(GeoObjectMetadata.get().getAttributeDisplayLabel(DefaultAttribute.CODE.getName()));
      //
      // throw ex;
      // }
      //
      // mdAttributeTerm.setValue(MdAttributeClassification.ROOT,
      // classification.getOid());
      // }
    }
    else if (attributeType.getType().equals(AttributeBooleanType.TYPE))
    {
      mdAttribute = new MdAttributeBoolean();
    }
    else if (attributeType.getType().equals(AttributeLocalType.TYPE))
    {
      if (mdClass instanceof MdGraphClass)
      {
        mdAttribute = new MdAttributeLocalCharacterEmbedded();
      }
      else
      {
        mdAttribute = new MdAttributeLocalText();
      }
    }
    else
    {
      throw new UnsupportedOperationException();
    }

    mdAttribute.setAttributeName(attributeType.getName());
    mdAttribute.setValue(MdAttributeConcreteInfo.REQUIRED, Boolean.toString(attributeType.isRequired()));

    if (attributeType.isUnique())
    {
      mdAttribute.addIndexType(MdAttributeIndices.UNIQUE_INDEX);
    }

    LocalizedValueConverter.populate(mdAttribute.getDisplayLabel(), attributeType.getLabel());
    LocalizedValueConverter.populate(mdAttribute.getDescription(), attributeType.getDescription());

    mdAttribute.setDefiningMdClass(mdClass);
    mdAttribute.apply();

    if (attributeType.getType().equals(AttributeTermType.TYPE))
    {
      // MdAttributeTerm mdAttributeTerm = (MdAttributeTerm) mdAttribute;
      //
      // // Build the parent class term root if it does not exist.
      // Classifier classTerm =
      // TermConverter.buildIfNotExistdMdBusinessClassifier(mdClass);
      //
      // // Create the root term node for this attribute
      // Classifier attributeTermRoot =
      // TermConverter.buildIfNotExistAttribute(mdClass,
      // mdAttributeTerm.getAttributeName(), classTerm);
      //
      // // Make this the root term of the multi-attribute
      // attributeTermRoot.addClassifierTermAttributeRoots(mdAttributeTerm).apply();
      //
      // AttributeTermType attributeTermType = (AttributeTermType)
      // attributeType;
      //
      // LocalizedValue label =
      // LocalizedValueConverter.convert(attributeTermRoot.getDisplayLabel());
      //
      // org.commongeoregistry.adapter.Term term = new
      // org.commongeoregistry.adapter.Term(attributeTermRoot.getClassifierId(),
      // label, new LocalizedValue(""));
      // attributeTermType.setRootTerm(term);
    }
    return mdAttribute;
  }

}
