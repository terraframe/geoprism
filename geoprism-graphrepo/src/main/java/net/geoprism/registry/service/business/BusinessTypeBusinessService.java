package net.geoprism.registry.service.business;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang.StringUtils;
import org.commongeoregistry.adapter.constants.DefaultAttribute;
import org.commongeoregistry.adapter.dataaccess.LocalizedValue;
import org.commongeoregistry.adapter.metadata.AttributeLocalType;
import org.commongeoregistry.adapter.metadata.AttributeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.runwaysdk.constants.IndexTypes;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeCharacterInfo;
import com.runwaysdk.constants.MdAttributeConcreteInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.graph.MdEdgeInfo;
import com.runwaysdk.constants.graph.MdVertexInfo;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeMultiTermDAOIF;
import com.runwaysdk.dataaccess.MdAttributeTermDAOIF;
import com.runwaysdk.dataaccess.MdEdgeDAOIF;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.dataaccess.metadata.MdAttributeCharacterDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeConcreteDAO;
import com.runwaysdk.dataaccess.metadata.graph.MdEdgeDAO;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.gis.constants.MdGeoVertexInfo;
import com.runwaysdk.localization.LocalizationFacade;
import com.runwaysdk.localization.SupportedLocaleIF;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.session.Session;
import com.runwaysdk.system.metadata.MdAttributeConcrete;
import com.runwaysdk.system.metadata.MdEdge;
import com.runwaysdk.system.metadata.MdVertex;

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
import net.geoprism.registry.graph.GeoVertex;
import net.geoprism.registry.model.ServerOrganization;
import net.geoprism.registry.query.graph.BusinessObjectPageQuery;
import net.geoprism.registry.service.permission.PermissionServiceIF;
import net.geoprism.registry.service.request.ServiceFactory;
import net.geoprism.registry.view.JsonSerializable;
import net.geoprism.registry.view.Page;

@Service
public class BusinessTypeBusinessService implements BusinessTypeBusinessServiceIF
{
  @Autowired
  private ClassificationTypeBusinessServiceIF cTypeService;

  @Autowired
  private GeoObjectTypeBusinessServiceIF      typeService;

  @Autowired
  private PermissionServiceIF                 permissions;

  @Override
  @Transaction
  public void delete(BusinessType type)
  {
    // Delete the term root
    Classifier classRootTerm = TermConverter.buildIfNotExistdMdBusinessClassifier(type.getMdVertex());
    classRootTerm.delete();

    MdVertex mdVertex = type.getMdVertex();
    MdEdge mdEdge = type.getMdEdge();

    type.delete();

    mdEdge.delete();
    mdVertex.delete();
  }

  @Override
  public AttributeType createAttributeType(BusinessType type, AttributeType attributeType)
  {
    MdAttributeConcrete mdAttribute = this.typeService.createMdAttributeFromAttributeType(type.getMdVertex(), attributeType);

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
    MdAttributeConcrete mdAttribute = this.typeService.updateMdAttributeFromAttributeType(type.getMdVertex(), attrType);
    return new RegistryAttributeTypeConverter().build(MdAttributeConcreteDAO.get(mdAttribute.getOid()));
  }

  @Override
  public void removeAttribute(BusinessType type, String attributeName)
  {
    this.deleteMdAttributeFromAttributeType(type, attributeName);

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

      // TODO CREATE the edge between this class and GeoVertex??
      MdVertexDAOIF mdGeoVertexDAO = MdVertexDAO.getMdVertexDAO(GeoVertex.CLASS);

      MdEdgeDAO mdEdge = MdEdgeDAO.newInstance();
      mdEdge.setValue(MdEdgeInfo.PACKAGE, RegistryConstants.BUSINESS_GRAPH_PACKAGE);
      mdEdge.setValue(MdEdgeInfo.NAME, code + "Edge");
      mdEdge.setValue(MdEdgeInfo.PARENT_MD_VERTEX, mdVertex.getOid());
      mdEdge.setValue(MdEdgeInfo.CHILD_MD_VERTEX, mdGeoVertexDAO.getOid());
      RegistryLocalizedValueConverter.populate(mdEdge, MdEdgeInfo.DISPLAY_LABEL, localizedValue);
      mdEdge.setValue(MdEdgeInfo.ENABLE_CHANGE_OVER_TIME, MdAttributeBooleanInfo.FALSE);
      mdEdge.apply();

      // MdAttributeGraphReferenceDAO mdGeoObject =
      // MdAttributeGraphReferenceDAO.newInstance();
      // mdGeoObject.setValue(MdAttributeGraphReferenceInfo.REFERENCE_MD_VERTEX,
      // mdGeoVertexDAO.getOid());
      // mdGeoObject.setValue(MdAttributeGraphReferenceInfo.DEFINING_MD_CLASS,
      // mdVertex.getOid());
      // mdGeoObject.setValue(MdAttributeGraphReferenceInfo.NAME, GEO_OBJECT);
      // mdGeoObject.setStructValue(MdAttributeGraphReferenceInfo.DESCRIPTION,
      // MdAttributeLocalInfo.DEFAULT_LOCALE, "Geo Object");
      // mdGeoObject.apply();

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
      businessType.setMdEdgeId(mdEdge.getOid());
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
  public boolean isEdgeABusinessType(MdEdgeDAOIF mdEdge)
  {
    return ( this.getByMdEdge(mdEdge) != null );
  }

  @Override
  public BusinessType getByMdEdge(MdEdgeDAOIF mdEdge)
  {
    BusinessTypeQuery query = new BusinessTypeQuery(new QueryFactory());
    query.WHERE(query.getMdEdge().EQ(mdEdge.getOid()));

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

}
