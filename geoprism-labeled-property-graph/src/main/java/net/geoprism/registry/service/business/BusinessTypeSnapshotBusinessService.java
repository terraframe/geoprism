/**
 * Copyright (c) 2023 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Geoprism(tm).
 *
 * Geoprism(tm) is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * Geoprism(tm) is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Geoprism(tm). If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism.registry.service.business;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.commongeoregistry.adapter.Term;
import org.commongeoregistry.adapter.constants.DefaultAttribute;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.runwaysdk.ComponentIF;
import com.runwaysdk.business.BusinessFacade;
import com.runwaysdk.business.graph.VertexObject;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeCharacterInfo;
import com.runwaysdk.constants.MdAttributeConcreteInfo;
import com.runwaysdk.constants.MdAttributeDoubleInfo;
import com.runwaysdk.constants.graph.MdVertexInfo;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeTermDAOIF;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.graph.GraphDBService;
import com.runwaysdk.dataaccess.metadata.graph.MdGraphClassDAO;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
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
import com.runwaysdk.system.metadata.MdVertex;

import net.geoprism.graph.BusinessTypeSnapshot;
import net.geoprism.graph.BusinessTypeSnapshotQuery;
import net.geoprism.graph.LabeledPropertyGraphTypeSnapshotQuery;
import net.geoprism.graph.LabeledPropertyGraphTypeVersion;
import net.geoprism.registry.DateFormatter;
import net.geoprism.registry.conversion.LocalizedValueConverter;
import net.geoprism.registry.model.Classification;
import net.geoprism.registry.model.ClassificationType;

@Service
public class BusinessTypeSnapshotBusinessService implements BusinessTypeSnapshotBusinessServiceIF
{
  public final String                         TABLE_PACKAGE = "net.geoprism.lpg";

  public final String                         PREFIX        = "g_";

  public final String                         SPLIT         = "__";

  public final String                         ROOT          = "__ROOT__";

  @Autowired
  private ClassificationBusinessServiceIF     classificationService;

  @Autowired
  private ClassificationTypeBusinessServiceIF typeService;

  @Override
  @Transaction
  public void delete(BusinessTypeSnapshot snapshot)
  {
    String mdVertexOid = snapshot.getGraphMdVertexOid();

    snapshot.delete();

    if (!StringUtils.isBlank(mdVertexOid))
    {
      MdGraphClassDAO.get(mdVertexOid).getBusinessDAO().delete();
    }
  }

  @Override
  public void truncate(BusinessTypeSnapshot snapshot)
  {
    MdVertex mdVertex = snapshot.getGraphMdVertex();

    GraphDBService service = GraphDBService.getInstance();
    service.command(service.getGraphDBRequest(), "DELETE VERTEX FROM " + mdVertex.getDbClassName(), new HashMap<>());
  }

  @Override
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

  private boolean isTableNameInUse(String name)
  {
    MdGraphClassQuery query = new MdGraphClassQuery(new QueryFactory());
    query.WHERE(query.getDbClassName().EQ(name));

    return query.getCount() > 0;
  }

  @Transaction
  @Override
  public BusinessTypeSnapshot create(LabeledPropertyGraphTypeVersion version, JsonObject typeDto)
  {
    String code = typeDto.get(BusinessTypeSnapshot.CODE).getAsString();
    String orgCode = typeDto.get(BusinessTypeSnapshot.ORGCODE).getAsString();
    String viewName = getTableName(code);
    LocalizedValue label = LocalizedValue.fromJSON(typeDto.get(BusinessTypeSnapshot.DISPLAYLABEL).getAsJsonObject());

    // Create the MdTable
    MdVertexDAO mdTableDAO = MdVertexDAO.newInstance();
    mdTableDAO.setValue(MdVertexInfo.NAME, viewName);
    mdTableDAO.setValue(MdVertexInfo.PACKAGE, TABLE_PACKAGE);
    LocalizedValueConverter.populate(mdTableDAO, MdVertexInfo.DISPLAY_LABEL, label);
    mdTableDAO.setValue(MdVertexInfo.DB_CLASS_NAME, viewName);
    mdTableDAO.setValue(MdVertexInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdTableDAO.setValue(MdVertexInfo.ENABLE_CHANGE_OVER_TIME, MdAttributeBooleanInfo.FALSE);
    mdTableDAO.apply();

    MdVertex mdTable = (MdVertex) BusinessFacade.get(mdTableDAO);

    JsonArray attributes = typeDto.get("attributes").getAsJsonArray();

    attributes.forEach(joAttr -> {
      AttributeType attributeType = AttributeType.parse(joAttr.getAsJsonObject());

      if (! ( attributeType instanceof AttributeTermType ))
      {
        this.createMdAttributeFromAttributeType(mdTable, attributeType);
      }
    });

    BusinessTypeSnapshot snapshot = new BusinessTypeSnapshot();
    snapshot.setGraphMdVertex(mdTable);
    snapshot.setLabelAttribute(typeDto.get(BusinessTypeSnapshot.LABELATTRIBUTE).getAsString());
    snapshot.setCode(code);
    snapshot.setOrgCode(orgCode);
    LocalizedValueConverter.populate(snapshot.getDisplayLabel(), label);
    snapshot.apply();
    
    snapshot.addversion(version).apply();

    this.assignPermissions(mdTableDAO);

    return snapshot;
  }

  @Override
  public BusinessTypeSnapshot get(LabeledPropertyGraphTypeVersion version, String code)
  {
    QueryFactory factory = new QueryFactory();

    LabeledPropertyGraphTypeSnapshotQuery vQuery = new LabeledPropertyGraphTypeSnapshotQuery(factory);
    vQuery.WHERE(vQuery.getParent().EQ(version));

    BusinessTypeSnapshotQuery query = new BusinessTypeSnapshotQuery(factory);
    query.WHERE(query.EQ(vQuery.getChild()));
    query.AND(query.getCode().EQ(code));

    try (OIterator<? extends BusinessTypeSnapshot> it = query.getIterator())
    {
      if (it.hasNext())
      {
        return it.next();
      }
    }

    return null;
  }

  @Override
  public BusinessTypeSnapshot get(LabeledPropertyGraphTypeVersion version, MdVertexDAOIF mdVertex)
  {
    QueryFactory factory = new QueryFactory();

    LabeledPropertyGraphTypeSnapshotQuery vQuery = new LabeledPropertyGraphTypeSnapshotQuery(factory);
    vQuery.WHERE(vQuery.getParent().EQ(version));

    BusinessTypeSnapshotQuery query = new BusinessTypeSnapshotQuery(factory);
    query.LEFT_JOIN_EQ(vQuery.getChild());
    query.AND(query.getGraphMdVertex().EQ(mdVertex.getOid()));

    try (OIterator<? extends BusinessTypeSnapshot> it = query.getIterator())
    {
      if (it.hasNext())
      {
        return it.next();
      }
    }

    return null;
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
          ex.setAttributeLabel("BusinessType.attr.code");

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

  @Override
  public JsonObject toDTO(BusinessTypeSnapshot snapshot, VertexObject vertex)
  {
    JsonObject data = new JsonObject();

    List<? extends MdAttributeConcreteDAOIF> mdAttributes = MdVertexDAO.get(snapshot.getGraphMdVertexOid()).definesAttributes();

    for (MdAttributeConcreteDAOIF mdAttribute : mdAttributes)
    {
      String attributeName = mdAttribute.definesAttribute();

      if (!attributeName.equals("code"))
      {

        Object value = vertex.getObjectValue(attributeName);

        if (value != null)
        {
          if (mdAttribute instanceof MdAttributeTermDAOIF)
          {
            // throw new UnsupportedOperationException();
          }
          else if (value instanceof Number)
          {
            data.addProperty(mdAttribute.definesAttribute(), (Number) value);
          }
          else if (value instanceof Boolean)
          {
            data.addProperty(mdAttribute.definesAttribute(), (Boolean) value);
          }
          else if (value instanceof String)
          {
            data.addProperty(mdAttribute.definesAttribute(), (String) value);
          }
          else if (value instanceof Character)
          {
            data.addProperty(mdAttribute.definesAttribute(), (Character) value);
          }
          else if (value instanceof Date)
          {
            data.addProperty(mdAttribute.definesAttribute(), DateFormatter.formatDate((Date) value, false));
          }
        }
      }
    }

    JsonObject json = new JsonObject();
    json.addProperty("code", vertex.getValue("code"));
    json.add("label", LocalizedValueConverter.convert(vertex.getEmbeddedComponent(DefaultAttribute.DISPLAY_LABEL.getName())).toJSON());
    json.add("data", data);

    return json;
  }

  protected void assignPermissions(ComponentIF component)
  {
  }
}
