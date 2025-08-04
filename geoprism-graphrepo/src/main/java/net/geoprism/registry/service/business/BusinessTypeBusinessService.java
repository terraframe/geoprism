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

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
import com.runwaysdk.business.BusinessFacade;
import com.runwaysdk.constants.IndexTypes;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeCharacterInfo;
import com.runwaysdk.constants.MdAttributeConcreteInfo;
import com.runwaysdk.constants.MdAttributeDoubleInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdAttributeLongInfo;
import com.runwaysdk.constants.graph.MdVertexInfo;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeMultiTermDAOIF;
import com.runwaysdk.dataaccess.MdAttributeTermDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.dataaccess.metadata.MdAttributeCharacterDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeConcreteDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeLongDAO;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.gis.constants.MdGeoVertexInfo;
import com.runwaysdk.localization.LocalizationFacade;
import com.runwaysdk.localization.SupportedLocaleIF;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.session.Session;
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
import com.runwaysdk.system.metadata.MdAttributeTerm;
import com.runwaysdk.system.metadata.MdBusiness;
import com.runwaysdk.system.metadata.MdClass;
import com.runwaysdk.system.metadata.MdGraphClass;
import com.runwaysdk.system.metadata.MdVertex;

import net.geoprism.configuration.GeoprismProperties;
import net.geoprism.ontology.Classifier;
import net.geoprism.registry.BusinessEdgeType;
import net.geoprism.registry.BusinessEdgeTypeQuery;
import net.geoprism.registry.BusinessType;
import net.geoprism.registry.BusinessTypeQuery;
import net.geoprism.registry.CodeLengthException;
import net.geoprism.registry.Organization;
import net.geoprism.registry.RegistryConstants;
import net.geoprism.registry.conversion.RegistryAttributeTypeConverter;
import net.geoprism.registry.conversion.RegistryLocalizedValueConverter;
import net.geoprism.registry.conversion.TermConverter;
import net.geoprism.registry.model.Classification;
import net.geoprism.registry.model.ClassificationType;
import net.geoprism.registry.model.GeoObjectMetadata;
import net.geoprism.registry.model.ServerOrganization;
import net.geoprism.registry.query.graph.BusinessObjectPageQuery;
import net.geoprism.registry.service.permission.PermissionServiceIF;
import net.geoprism.registry.view.JsonSerializable;
import net.geoprism.registry.view.Page;

@Service
public class BusinessTypeBusinessService implements BusinessTypeBusinessServiceIF
{
  @Autowired
  private ClassificationTypeBusinessServiceIF cTypeService;

  @Autowired
  private ClassificationBusinessServiceIF     cService;

  @Autowired
  private GeoObjectTypeBusinessServiceIF      typeService;

  @Autowired
  private PermissionServiceIF                 permissions;

  @Override
  @Transaction
  public void delete(BusinessType type)
  {
    // Delete the term root
    Classifier classRootTerm = TermConverter.buildIfNotExistGeoObjectTypeClassifier(type);
    classRootTerm.delete();

    MdVertex mdVertex = type.getMdVertex();

    type.delete();

    mdVertex.delete();
  }

  @Override
  public AttributeType createAttributeType(BusinessType type, AttributeType attributeType)
  {
    MdAttributeConcrete mdAttribute = this.createMdAttributeFromAttributeType(type, attributeType);

    // Refresh the users session
    if (Session.getCurrentSession() != null)
    {
      // Refresh the users session
      ( (Session) Session.getCurrentSession() ).reloadPermissions();
    }

    return new RegistryAttributeTypeConverter().build(MdAttributeConcreteDAO.get(mdAttribute.getOid()));
  }

  @Override
  public AttributeType createAttributeType(BusinessType type, JsonObject attrObj)
  {
    AttributeType attrType = AttributeType.parse(attrObj);

    return createAttributeType(type, attrType);
  }

  @Override
  public AttributeType updateAttributeType(BusinessType type, JsonObject attrObj)
  {
    AttributeType attrType = AttributeType.parse(attrObj);

    return updateAttributeType(type, attrType);
  }

  @Override
  public void setLabelAttribute(BusinessType type, String attributeName)
  {
    MdVertexDAOIF mdVertex = type.getMdVertexDAO();
    MdAttributeDAOIF mdAttribute = mdVertex.definesAttribute(attributeName);

    type.setLabelAttributeId(mdAttribute.getOid());
  }

  @Override
  public AttributeType updateAttributeType(BusinessType type, AttributeType attrType)
  {
    MdAttributeConcrete mdAttribute = this.updateMdAttributeFromAttributeType(type, attrType);

    return new RegistryAttributeTypeConverter().build(MdAttributeConcreteDAO.get(mdAttribute.getOid()));
  }

  @Override
  public void removeAttribute(BusinessType type, String attributeName)
  {
    this.deleteMdAttributeFromAttributeType(type, attributeName);

    // Update the sequence number of the type
    if (type.getOrigin().equals(GeoprismProperties.getOrigin()))
    {
      type.setSequence(type.getSequence() + 1);
      type.apply();
    }

    // Refresh the users session
    if (Session.getCurrentSession() != null)
    {
      // Refresh the users session
      ( (Session) Session.getCurrentSession() ).reloadPermissions();
    }
  }

  /**
   * Delete the {@link MdAttributeConcreteDAOIF} from the given {
   * 
   * @param type
   *          TODO
   * @param mdBusiness
   * @param attributeName
   */
  @Override
  @Transaction
  public void deleteMdAttributeFromAttributeType(BusinessType type, String attributeName)
  {
    MdAttributeConcreteDAOIF mdAttributeConcreteDAOIF = this.typeService.getMdAttribute(type.getMdVertex(), attributeName);

    if (mdAttributeConcreteDAOIF != null)
    {
      if (mdAttributeConcreteDAOIF instanceof MdAttributeTermDAOIF || mdAttributeConcreteDAOIF instanceof MdAttributeMultiTermDAOIF)
      {
        String attributeTermKey = TermConverter.buildtAtttributeKey(type.getMdVertex().getTypeName(), mdAttributeConcreteDAOIF.definesAttribute());

        try
        {
          Classifier attributeTerm = Classifier.getByKey(attributeTermKey);
          attributeTerm.delete();
        }
        catch (DataNotFoundException e)
        {
        }
      }

      mdAttributeConcreteDAOIF.getBusinessDAO().delete();
    }
  }

  @Override
  public JsonObject toJSON(BusinessType type)
  {
    return toJSON(type, false, false);
  }

  @Override
  public JsonObject toJSON(BusinessType type, boolean includeAttribute, boolean flattenLocalAttributes)
  {
    Organization organization = type.getOrganization();

    JsonObject object = new JsonObject();
    object.addProperty(BusinessType.CODE, type.getCode());
    object.addProperty(BusinessType.ORGANIZATION, organization.getCode());
    object.addProperty(BusinessType.ORIGIN, type.getOrigin());
    object.addProperty(BusinessType.SEQUENCE, type.getSequence());
    object.addProperty("organizationLabel", organization.getDisplayLabel().getValue());
    object.add(BusinessType.DISPLAYLABEL, RegistryLocalizedValueConverter.convertNoAutoCoalesce(type.getDisplayLabel()).toJSON());

    if (type.getLabelAttributeOid() != null && type.getLabelAttributeOid().length() > 0)
    {
      object.addProperty(BusinessType.LABELATTRIBUTE, type.getLabelAttribute().getAttributeName());
    }
    else
    {
      object.addProperty(BusinessType.LABELATTRIBUTE, BusinessType.CODE);
    }

    if (type.isAppliedToDB())
    {
      object.addProperty(BusinessType.OID, type.getOid());
    }

    if (includeAttribute)
    {
      Collector<Object, JsonArray, JsonArray> collector = Collector.of(() -> new JsonArray(), (r, t) -> r.add((JsonObject) t), (x1, x2) -> {
        x1.addAll(x2);
        return x1;
      });

      JsonArray attributes = type.getAttributeMap().values().stream().sorted((a, b) -> {
        return a.getName().compareTo(b.getName());
      }).flatMap(attr -> {
        if (flattenLocalAttributes && attr instanceof AttributeLocalType)
        {
          List<JsonObject> list = new LinkedList<>();
          list.add(this.serializeLocale(type, attr, LocalizedValue.DEFAULT_LOCALE, LocalizationFacade.localize(DefaultAttribute.DISPLAY_LABEL.getName())));

          for (SupportedLocaleIF locale : LocalizationFacade.getSupportedLocales())
          {
            list.add(this.serializeLocale(type, attr, locale.getLocale().toString(), locale.getDisplayLabel().getValue()));
          }

          return list.stream();
        }

        return Stream.of(attr.toJSON());
      }).collect(collector);

      object.add(BusinessType.JSON_ATTRIBUTES, attributes);
    }

    return object;
  }

  private JsonObject serializeLocale(BusinessType type, AttributeType attributeType, String key, String label)
  {
    JsonObject object = attributeType.toJSON();
    object.addProperty("locale", key);
    object.addProperty(AttributeType.JSON_CODE, attributeType.getName());

    JsonObject jaLabel = object.get(AttributeType.JSON_LOCALIZED_LABEL).getAsJsonObject();
    String value = jaLabel.get(LocalizedValue.LOCALIZED_VALUE).getAsString();
    value += " (" + label + ")";
    jaLabel.addProperty(LocalizedValue.LOCALIZED_VALUE, value);

    return object;
  }

  @Override
  public Page<JsonSerializable> data(BusinessType type, JsonObject criteria)
  {
    return new BusinessObjectPageQuery(type, criteria).getPage();
  }

  @Transaction
  @Override
  public BusinessType apply(JsonObject object)
  {
    String code = object.get(BusinessType.CODE).getAsString();
    String organizationCode = object.get(BusinessType.ORGANIZATION).getAsString();
    Organization organization = Organization.getByCode(organizationCode);
    String origin = object.has(BusinessType.ORIGIN) ? object.get(BusinessType.ORIGIN).getAsString() : GeoprismProperties.getOrigin();

    ServiceFactory.getGeoObjectTypePermissionService().enforceCanCreate(organization.getCode(), false);

    cTypeService.validateName(code);

    if (code.length() > 64)
    {
      // Setting the typename on the MdBusiness creates this limitation.
      CodeLengthException ex = new CodeLengthException();
      ex.setLength(64);
      throw ex;
    }

    LocalizedValue localizedValue = LocalizedValue.fromJSON(object.get(BusinessType.DISPLAYLABEL).getAsJsonObject());

    BusinessType businessType = ( object.has(BusinessType.OID) && !object.get(BusinessType.OID).isJsonNull() ) ? BusinessType.get(object.get(BusinessType.OID).getAsString()) : new BusinessType();
    businessType.setCode(code);
    businessType.setOrganization(organization);
    RegistryLocalizedValueConverter.populate(businessType.getDisplayLabel(), localizedValue);

    boolean isNew = businessType.isNew();

    if (isNew)
    {
      MdVertexDAO mdVertex = MdVertexDAO.newInstance();
      mdVertex.setValue(MdGeoVertexInfo.PACKAGE, RegistryConstants.BUSINESS_PACKAGE);
      mdVertex.setValue(MdGeoVertexInfo.NAME, code);
      mdVertex.setValue(MdGeoVertexInfo.ENABLE_CHANGE_OVER_TIME, MdAttributeBooleanInfo.FALSE);
      mdVertex.setValue(MdGeoVertexInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
      RegistryLocalizedValueConverter.populate(mdVertex, MdVertexInfo.DISPLAY_LABEL, localizedValue);
      mdVertex.apply();

      // DefaultAttribute.CODE
      MdAttributeCharacterDAO vertexCodeMdAttr = MdAttributeCharacterDAO.newInstance();
      vertexCodeMdAttr.setValue(MdAttributeConcreteInfo.NAME, DefaultAttribute.CODE.getName());
      vertexCodeMdAttr.setStructValue(MdAttributeConcreteInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, DefaultAttribute.CODE.getDefaultLocalizedName());
      vertexCodeMdAttr.setStructValue(MdAttributeConcreteInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, DefaultAttribute.CODE.getDefaultDescription());
      vertexCodeMdAttr.setValue(MdAttributeCharacterInfo.SIZE, MdAttributeCharacterInfo.MAX_CHARACTER_SIZE);
      vertexCodeMdAttr.setValue(MdAttributeConcreteInfo.DEFINING_MD_CLASS, mdVertex.getOid());
      vertexCodeMdAttr.setValue(MdAttributeConcreteInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
      vertexCodeMdAttr.addItem(MdAttributeConcreteInfo.INDEX_TYPE, IndexTypes.UNIQUE_INDEX.getOid());
      vertexCodeMdAttr.apply();

      businessType.setMdVertexId(mdVertex.getOid());
      businessType.setOrigin(origin);
      businessType.setSequence(object.has(BusinessType.SEQUENCE) ? object.get(BusinessType.SEQUENCE).getAsLong() : 0L);
    }
    else
    {
      if (businessType.getOrigin().equals(GeoprismProperties.getOrigin()))
      {
        businessType.setSequence(businessType.getSequence() + 1);
      }
      else if (object.has(BusinessType.SEQUENCE))
      {
        businessType.setSequence(object.get(BusinessType.SEQUENCE).getAsLong());
      }
    }

    if (object.has(BusinessType.LABELATTRIBUTE) && !object.get(BusinessType.LABELATTRIBUTE).isJsonNull())
    {
      String attributeName = object.get(BusinessType.LABELATTRIBUTE).getAsString();

      if (!StringUtils.isEmpty(attributeName))
      {
        this.setLabelAttribute(businessType, attributeName);
      }
    }

    businessType.apply();

    return businessType;
  }

  @Override
  public List<BusinessEdgeType> getParentEdgeTypes(BusinessType type)
  {
    BusinessEdgeTypeQuery query = new BusinessEdgeTypeQuery(new QueryFactory());
    query.WHERE(query.getParentType().EQ(type.getMdVertex()));
    query.ORDER_BY_DESC(query.getDisplayLabel().localize());

    try (OIterator<? extends BusinessEdgeType> iterator = query.getIterator())
    {
      List<? extends BusinessEdgeType> results = iterator.getAll();

      return new LinkedList<BusinessEdgeType>(results);
    }
  }

  @Override
  public List<BusinessEdgeType> getChildEdgeTypes(BusinessType type)
  {
    BusinessEdgeTypeQuery query = new BusinessEdgeTypeQuery(new QueryFactory());
    query.WHERE(query.getChildType().EQ(type.getMdVertex()));
    query.ORDER_BY_DESC(query.getDisplayLabel().localize());

    try (OIterator<? extends BusinessEdgeType> iterator = query.getIterator())
    {
      List<? extends BusinessEdgeType> results = iterator.getAll();

      return new LinkedList<BusinessEdgeType>(results);
    }
  }

  @Override
  public List<BusinessEdgeType> getEdgeTypes(BusinessType type)
  {
    BusinessEdgeTypeQuery query = new BusinessEdgeTypeQuery(new QueryFactory());
    query.WHERE(query.getParentType().EQ(type.getMdVertex()));
    query.OR(query.getChildType().EQ(type.getMdVertex()));
    query.ORDER_BY_DESC(query.getDisplayLabel().localize());

    try (OIterator<? extends BusinessEdgeType> iterator = query.getIterator())
    {
      List<? extends BusinessEdgeType> results = iterator.getAll();

      return new LinkedList<BusinessEdgeType>(results);
    }
  }

  @Override
  public BusinessType getByCode(String code)
  {
    BusinessTypeQuery query = new BusinessTypeQuery(new QueryFactory());
    query.WHERE(query.getCode().EQ(code));

    try (OIterator<? extends BusinessType> it = query.getIterator())
    {
      if (it.hasNext())
      {
        return it.next();
      }
    }

    return null;
  }

  @Override
  public JsonArray listByOrg()
  {
    JsonArray response = new JsonArray();

    List<ServerOrganization> organizations = ServerOrganization.getSortedOrganizations().stream().filter(org -> org.getEnabled()).collect(Collectors.toList());

    for (ServerOrganization org : organizations)
    {
      BusinessTypeQuery query = new BusinessTypeQuery(new QueryFactory());
      query.WHERE(query.getOrganization().EQ(org.getOrganization()));
      query.ORDER_BY_DESC(query.getDisplayLabel().localize());

      JsonArray types = new JsonArray();

      try (OIterator<? extends BusinessType> it = query.getIterator())
      {
        while (it.hasNext())
        {
          BusinessType type = it.next();

          if (this.permissions.canRead(type))
          {
            types.add(this.toJSON(type));
          }
        }
      }

      JsonObject object = new JsonObject();
      object.addProperty("oid", org.getOid());
      object.addProperty("code", org.getCode());
      object.addProperty("label", org.getDisplayLabel().getValue());
      object.addProperty("write", this.permissions.isAdmin(org));
      object.add("types", types);

      response.add(object);
    }

    return response;
  }

  @Override
  public List<BusinessType> getAll()
  {
    List<BusinessType> response = new LinkedList<>();

    ServerOrganization.getSortedOrganizations().stream().filter(o -> this.permissions.isMember(o)).forEach(org -> {

      BusinessTypeQuery query = new BusinessTypeQuery(new QueryFactory());
      query.WHERE(query.getOrganization().EQ(org.getOrganization()));
      query.ORDER_BY_DESC(query.getDisplayLabel().localize());

      try (OIterator<? extends BusinessType> it = query.getIterator())
      {
        while (it.hasNext())
        {
          response.add(it.next());
        }
      }
    });

    return response;
  }

  @Override
  public List<BusinessType> getForOrganization(ServerOrganization organization)
  {
    return this.getForOrganization(organization.getOrganization());
  }

  @Override
  public List<BusinessType> getForOrganization(Organization organization)
  {
    BusinessTypeQuery query = new BusinessTypeQuery(new QueryFactory());
    query.WHERE(query.getOrganization().EQ(organization));
    query.ORDER_BY_DESC(query.getDisplayLabel().localize());

    try (OIterator<? extends BusinessType> it = query.getIterator())
    {
      return it.getAll().stream().map(type -> (BusinessType) type).collect(Collectors.toList());
    }
  }

  @Override
  public BusinessType getByMdVertex(MdVertexDAOIF mdVertex)
  {
    BusinessTypeQuery query = new BusinessTypeQuery(new QueryFactory());
    query.WHERE(query.getMdVertex().EQ(mdVertex.getOid()));

    try (OIterator<? extends BusinessType> it = query.getIterator())
    {
      if (it.hasNext())
      {
        return it.next();
      }
    }

    return null;
  }

  private MdAttributeConcreteDAOIF getMdAttribute(MdClass mdClass, String attributeName)
  {
    MdClassDAOIF mdClassDAO = (MdClassDAOIF) BusinessFacade.getEntityDAO(mdClass);

    return (MdAttributeConcreteDAOIF) mdClassDAO.definesAttribute(attributeName);
  }

  @Transaction
  private MdAttributeConcrete createMdAttributeFromAttributeType(BusinessType type, AttributeType attributeType)
  {
    MdVertex mdClass = type.getMdVertex();

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
      mdAttribute = new MdAttributeTerm();
      MdAttributeTerm mdAttributeTerm = (MdAttributeTerm) mdAttribute;

      MdBusiness classifierMdBusiness = MdBusiness.getMdBusiness(Classifier.CLASS);
      mdAttributeTerm.setMdBusiness(classifierMdBusiness);
      // TODO implement support for multi-term
      // mdAttribute = new MdAttributeMultiTerm();
      // MdAttributeMultiTerm mdAttributeMultiTerm =
      // (MdAttributeMultiTerm)mdAttribute;
      //
      // MdBusiness classifierMdBusiness =
      // MdBusiness.getMdBusiness(Classifier.CLASS);
      // mdAttributeMultiTerm.setMdBusiness(classifierMdBusiness);
    }
    else if (attributeType.getType().equals(AttributeClassificationType.TYPE))
    {
      AttributeClassificationType attributeClassificationType = (AttributeClassificationType) attributeType;
      String classificationTypeCode = attributeClassificationType.getClassificationType();

      ClassificationType classificationType = this.cTypeService.getByCode(classificationTypeCode);

      mdAttribute = new MdAttributeClassification();
      MdAttributeClassification mdAttributeTerm = (MdAttributeClassification) mdAttribute;
      mdAttributeTerm.setReferenceMdClassification(classificationType.getMdClassificationObject());

      Term root = attributeClassificationType.getRootTerm();

      if (root != null)
      {
        Classification classification = this.cService.get(classificationType, root.getCode());

        if (classification == null)
        {
          net.geoprism.registry.DataNotFoundException ex = new net.geoprism.registry.DataNotFoundException();
          ex.setTypeLabel(classificationType.getDisplayLabel().getValue());
          ex.setDataIdentifier(root.getCode());
          ex.setAttributeLabel(GeoObjectMetadata.get().getAttributeDisplayLabel(DefaultAttribute.CODE.getName()));

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

    RegistryLocalizedValueConverter.populate(mdAttribute.getDisplayLabel(), attributeType.getLabel());
    RegistryLocalizedValueConverter.populate(mdAttribute.getDescription(), attributeType.getDescription());

    mdAttribute.setDefiningMdClass(mdClass);
    mdAttribute.apply();

    if (attributeType.getType().equals(AttributeTermType.TYPE))
    {
      MdAttributeTerm mdAttributeTerm = (MdAttributeTerm) mdAttribute;

      // Build the parent class term root if it does not exist.
      Classifier classTerm = TermConverter.buildIfNotExistGeoObjectTypeClassifier(type);

      // Create the root term node for this attribute
      Classifier attributeTermRoot = TermConverter.buildIfNotExistAttribute(type, mdAttributeTerm.getAttributeName(), classTerm);

      // Make this the root term of the multi-attribute
      attributeTermRoot.addClassifierTermAttributeRoots(mdAttributeTerm).apply();

      AttributeTermType attributeTermType = (AttributeTermType) attributeType;

      LocalizedValue label = RegistryLocalizedValueConverter.convertNoAutoCoalesce(attributeTermRoot.getDisplayLabel());

      org.commongeoregistry.adapter.Term term = new org.commongeoregistry.adapter.Term(attributeTermRoot.getClassifierId(), label, new LocalizedValue(""));
      attributeTermType.setRootTerm(term);
    }

    // Update the sequence number of the type
    if (type.getOrigin().equals(GeoprismProperties.getOrigin()))
    {
      type.setSequence(type.getSequence() + 1);
      type.apply();
    }

    return mdAttribute;
  }

  @Transaction
  private MdAttributeConcrete updateMdAttributeFromAttributeType(BusinessType type, AttributeType attributeType)
  {
    MdVertex mdClass = type.getMdVertex();

    MdAttributeConcreteDAOIF mdAttributeConcreteDAOIF = getMdAttribute(mdClass, attributeType.getName());

    if (mdAttributeConcreteDAOIF != null)
    {
      // Get the type safe version
      MdAttributeConcrete mdAttribute = (MdAttributeConcrete) BusinessFacade.get(mdAttributeConcreteDAOIF);
      mdAttribute.lock();

      try
      {
        // The name cannot be updated
        // mdAttribute.setAttributeName(attributeType.getName());
        RegistryLocalizedValueConverter.populate(mdAttribute.getDisplayLabel(), attributeType.getLabel());
        RegistryLocalizedValueConverter.populate(mdAttribute.getDescription(), attributeType.getDescription());

        if (attributeType instanceof AttributeFloatType)
        {
          // Refresh the terms
          AttributeFloatType attributeFloatType = (AttributeFloatType) attributeType;

          mdAttribute.setValue(MdAttributeDoubleInfo.LENGTH, Integer.toString(attributeFloatType.getPrecision()));
          mdAttribute.setValue(MdAttributeDoubleInfo.DECIMAL, Integer.toString(attributeFloatType.getScale()));
        }
        else if (attributeType instanceof AttributeClassificationType)
        {
          MdAttributeClassification mdAttributeTerm = (MdAttributeClassification) mdAttribute;

          AttributeClassificationType attributeClassificationType = (AttributeClassificationType) attributeType;
          String classificationTypeCode = attributeClassificationType.getClassificationType();

          ClassificationType classificationType = this.cTypeService.getByCode(classificationTypeCode);

          Term root = attributeClassificationType.getRootTerm();

          if (root != null)
          {
            Classification classification = this.cService.get(classificationType, root.getCode());

            mdAttributeTerm.setValue(MdAttributeClassification.ROOT, classification.getOid());
          }
        }

        mdAttribute.apply();
      }
      finally
      {
        mdAttribute.unlock();
      }

      if (attributeType instanceof AttributeTermType)
      {
        // Refresh the terms
        AttributeTermType attributeTermType = (AttributeTermType) attributeType;

        org.commongeoregistry.adapter.Term getRootTerm = attributeTermType.getRootTerm();
        String classifierKey = TermConverter.buildClassifierKeyFromTermCode(getRootTerm.getCode());

        TermConverter termBuilder = new TermConverter(classifierKey);
        attributeTermType.setRootTerm(termBuilder.build());
      }

      return mdAttribute;
    }

    return null;
  }

}
