package net.geoprism.registry.service.business;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.commongeoregistry.adapter.Term;
import org.commongeoregistry.adapter.constants.DefaultAttribute;
import org.commongeoregistry.adapter.constants.GeometryType;
import org.commongeoregistry.adapter.metadata.AttributeBooleanType;
import org.commongeoregistry.adapter.metadata.AttributeCharacterType;
import org.commongeoregistry.adapter.metadata.AttributeClassificationType;
import org.commongeoregistry.adapter.metadata.AttributeDateType;
import org.commongeoregistry.adapter.metadata.AttributeFloatType;
import org.commongeoregistry.adapter.metadata.AttributeIntegerType;
import org.commongeoregistry.adapter.metadata.AttributeLocalType;
import org.commongeoregistry.adapter.metadata.AttributeTermType;
import org.commongeoregistry.adapter.metadata.AttributeType;
import org.springframework.beans.factory.annotation.Autowired;

import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeCharacterInfo;
import com.runwaysdk.constants.MdAttributeConcreteInfo;
import com.runwaysdk.constants.MdAttributeDoubleInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.graph.GraphDBService;
import com.runwaysdk.dataaccess.metadata.graph.MdGraphClassDAO;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.gis.constants.MdAttributeLineStringInfo;
import com.runwaysdk.gis.constants.MdAttributeMultiLineStringInfo;
import com.runwaysdk.gis.constants.MdAttributeMultiPointInfo;
import com.runwaysdk.gis.constants.MdAttributeMultiPolygonInfo;
import com.runwaysdk.gis.constants.MdAttributePointInfo;
import com.runwaysdk.gis.constants.MdAttributePolygonInfo;
import com.runwaysdk.gis.constants.MdAttributeShapeInfo;
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
import com.runwaysdk.system.metadata.MdAttributeClassification;
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

import net.geoprism.graph.AttributeBooleanTypeSnapshot;
import net.geoprism.graph.AttributeCharacterTypeSnapshot;
import net.geoprism.graph.AttributeClassificationTypeSnapshot;
import net.geoprism.graph.AttributeDateTypeSnapshot;
import net.geoprism.graph.AttributeDoubleTypeSnapshot;
import net.geoprism.graph.AttributeGeometryTypeSnapshot;
import net.geoprism.graph.AttributeLocalTypeSnapshot;
import net.geoprism.graph.AttributeLongTypeSnapshot;
import net.geoprism.graph.AttributeTypeSnapshot;
import net.geoprism.graph.AttributeTypeSnapshotQuery;
import net.geoprism.graph.ObjectTypeSnapshot;
import net.geoprism.graph.SnapshotHasAttributeQuery;
import net.geoprism.registry.conversion.LocalizedValueConverter;
import net.geoprism.registry.model.Classification;
import net.geoprism.registry.model.ClassificationType;

public abstract class ObjectTypeBusinessService<T extends ObjectTypeSnapshot> implements ObjectTypeBusinessServiceIF<T>
{
  public final String                         TABLE_PACKAGE = "net.geoprism.lpg";

  public final String                         PREFIX        = "g_";

  public final String                         SPLIT         = "__";

  @Autowired
  private ClassificationBusinessServiceIF     classificationService;

  @Autowired
  private ClassificationTypeBusinessServiceIF typeService;

  @Transaction
  public void delete(T snapshot)
  {
    String mdVertexOid = snapshot.getGraphMdVertexOid();

    this.getAttributeTypes(snapshot).forEach(attribute -> attribute.delete());

    snapshot.delete();

    if (!StringUtils.isBlank(mdVertexOid))
    {
      MdGraphClassDAO.get(mdVertexOid).getBusinessDAO().delete();
    }
  }

  @Transaction
  public void truncate(T snapshot)
  {
    String mdVertexOid = snapshot.getGraphMdVertexOid();

    if (!StringUtils.isBlank(mdVertexOid))
    {
      MdVertexDAOIF mdVertex = (MdVertexDAOIF) MdGraphClassDAO.get(mdVertexOid);

      GraphDBService service = GraphDBService.getInstance();
      service.command(service.getGraphDBRequest(), "DELETE VERTEX FROM " + mdVertex.getDBClassName(), new HashMap<>());
    }
  }

  public List<AttributeTypeSnapshot> getAttributeTypes(T snapshot)
  {
    QueryFactory factory = new QueryFactory();

    SnapshotHasAttributeQuery vQuery = new SnapshotHasAttributeQuery(factory);
    vQuery.WHERE(vQuery.getParent().EQ(snapshot));

    AttributeTypeSnapshotQuery query = new AttributeTypeSnapshotQuery(factory);
    query.LEFT_JOIN_EQ(vQuery.getChild());

    try (OIterator<? extends AttributeTypeSnapshot> it = query.getIterator())
    {
      return new LinkedList<AttributeTypeSnapshot>(it.getAll());
    }
  }

  public AttributeTypeSnapshot createAttributeTypeSnapshot(T type, AttributeType attributeType)
  {
    AttributeTypeSnapshot attributeTypeSnapshot = null;

    if (attributeType.getType().equals(AttributeCharacterType.TYPE))
    {
      attributeTypeSnapshot = new AttributeCharacterTypeSnapshot();
    }
    else if (attributeType.getType().equals(AttributeDateType.TYPE))
    {
      attributeTypeSnapshot = new AttributeDateTypeSnapshot();
    }
    else if (attributeType.getType().equals(AttributeIntegerType.TYPE))
    {
      attributeTypeSnapshot = new AttributeLongTypeSnapshot();
    }
    else if (attributeType.getType().equals(AttributeFloatType.TYPE))
    {
      AttributeFloatType attributeFloatType = (AttributeFloatType) attributeType;

      attributeTypeSnapshot = new AttributeDoubleTypeSnapshot();
      attributeTypeSnapshot.setValue(AttributeDoubleTypeSnapshot.PRECISION, Integer.toString(attributeFloatType.getPrecision()));
      attributeTypeSnapshot.setValue(AttributeDoubleTypeSnapshot.SCALE, Integer.toString(attributeFloatType.getScale()));
    }
    else if (attributeType.getType().equals(AttributeTermType.TYPE))
    {
    }
    else if (attributeType.getType().equals(AttributeClassificationType.TYPE))
    {
      AttributeClassificationType attributeClassificationType = (AttributeClassificationType) attributeType;
      String classificationTypeCode = attributeClassificationType.getClassificationType();

      AttributeClassificationTypeSnapshot attributeSnapshot = new AttributeClassificationTypeSnapshot();
      attributeSnapshot.setClassificationType(classificationTypeCode);
      attributeSnapshot.setRootTerm(attributeClassificationType.getRootTerm().getCode());
      
      attributeTypeSnapshot = attributeSnapshot;
    }
    else if (attributeType.getType().equals(AttributeBooleanType.TYPE))
    {
      attributeTypeSnapshot = new AttributeBooleanTypeSnapshot();
    }
    else if (attributeType.getType().equals(AttributeLocalType.TYPE))
    {
      attributeTypeSnapshot = new AttributeLocalTypeSnapshot();
    }
    else
    {
      throw new UnsupportedOperationException();
    }

    attributeTypeSnapshot.setCode(attributeType.getName());
    attributeTypeSnapshot.setIsRequired(attributeType.isRequired());
    attributeTypeSnapshot.setIsUnique(attributeType.isUnique());

    LocalizedValueConverter.populate(attributeTypeSnapshot.getLabel(), attributeType.getLabel());
    LocalizedValueConverter.populate(attributeTypeSnapshot.getDescription(), attributeType.getDescription());

    attributeTypeSnapshot.apply();

    attributeTypeSnapshot.addSnapshot(type).apply();

    return attributeTypeSnapshot;
  }

  public AttributeGeometryTypeSnapshot createAttributeTypeSnapshot(T snapshot, GeometryType geometryType)
  {
    AttributeGeometryTypeSnapshot attributeType = new AttributeGeometryTypeSnapshot();
    attributeType.setGeometryType(geometryType.name());
    attributeType.setCode(DefaultAttribute.GEOMETRY.getName());
    attributeType.getLabel().setDefaultValue(DefaultAttribute.GEOMETRY.getName());
    attributeType.getDescription().setDefaultValue(DefaultAttribute.GEOMETRY.getName());
    attributeType.apply();

    attributeType.addSnapshot(snapshot).apply();

    return attributeType;
  }

  public MdAttributeConcrete createMdAttributeFromAttributeType(MdClass mdClass, AttributeType attributeType)
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
      AttributeClassificationType attributeClassificationType = (AttributeClassificationType) attributeType;
      String classificationTypeCode = attributeClassificationType.getClassificationType();

      ClassificationType classificationType = this.typeService.getByCode(classificationTypeCode);

      mdAttribute = new MdAttributeClassification();
      MdAttributeClassification mdAttributeTerm = (MdAttributeClassification) mdAttribute;
      mdAttributeTerm.setReferenceMdClassification(classificationType.getMdClassificationObject());

      Term root = attributeClassificationType.getRootTerm();

      if (root != null)
      {
        Classification classification = this.classificationService.get(classificationType, root.getCode());

        if (classification == null)
        {
          net.geoprism.registry.DataNotFoundException ex = new net.geoprism.registry.DataNotFoundException();
          ex.setTypeLabel(classificationType.getDisplayLabel().getValue());
          ex.setDataIdentifier(root.getCode());
          ex.setAttributeLabel("geoObjectType.attr.code");

          throw ex;

        }

        mdAttributeTerm.setValue(MdAttributeClassification.ROOT, classification.getOid());
      }
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

  public void createGeometryAttribute(GeometryType geometryType, MdVertexDAO mdTableDAO)
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

  protected boolean isTableNameInUse(String name)
  {
    MdGraphClassQuery query = new MdGraphClassQuery(new QueryFactory());
    query.WHERE(query.getDbClassName().EQ(name));

    return query.getCount() > 0;
  }

  public String getTableName(String className)
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

}
