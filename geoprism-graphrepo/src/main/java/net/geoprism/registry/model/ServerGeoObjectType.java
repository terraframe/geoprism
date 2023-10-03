/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Geoprism Registry(tm).
 *
 * Geoprism Registry(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Geoprism Registry(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Geoprism Registry(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism.registry.model;

import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.commongeoregistry.adapter.Optional;
import org.commongeoregistry.adapter.Term;
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
import org.commongeoregistry.adapter.metadata.CustomSerializer;
import org.commongeoregistry.adapter.metadata.GeoObjectType;
import org.commongeoregistry.adapter.metadata.RegistryRole;

import com.google.gson.JsonObject;
import com.runwaysdk.business.BusinessFacade;
import com.runwaysdk.constants.MdAttributeCharacterInfo;
import com.runwaysdk.constants.MdAttributeConcreteInfo;
import com.runwaysdk.constants.MdAttributeDoubleInfo;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.dataaccess.transaction.TransactionState;
import com.runwaysdk.gis.dataaccess.metadata.graph.MdGeoVertexDAO;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.system.Actor;
import com.runwaysdk.system.Roles;
import com.runwaysdk.system.gis.geo.Universal;
import com.runwaysdk.system.gis.metadata.graph.MdGeoVertex;
import com.runwaysdk.system.gis.metadata.graph.MdGeoVertexQuery;
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

import net.geoprism.ontology.Classifier;
import net.geoprism.ontology.GeoEntityUtil;
import net.geoprism.registry.HierarchicalRelationshipType;
import net.geoprism.registry.HierarchyRootException;
import net.geoprism.registry.InheritedHierarchyAnnotation;
import net.geoprism.registry.Organization;
import net.geoprism.registry.conversion.RegistryLocalizedValueConverter;
import net.geoprism.registry.conversion.TermConverter;
import net.geoprism.registry.service.ServiceFactory;
//import net.geoprism.registry.service.ChangeRequestService;
//import net.geoprism.registry.service.SearchService;
//import net.geoprism.registry.service.SerializedListTypeCache;
//import net.geoprism.registry.service.ServiceFactory;
//import net.geoprism.registry.ws.GlobalNotificationMessage;
//import net.geoprism.registry.ws.MessageType;
//import net.geoprism.registry.ws.NotificationFacade;

public class ServerGeoObjectType implements ServerElement
{
  // private Logger logger = LoggerFactory.getLogger(ServerLeafGeoObject.class);

  GeoObjectType type;

  private Universal     universal;

  private MdBusiness    mdBusiness;

  private MdVertexDAOIF mdVertex;

  public ServerGeoObjectType(GeoObjectType go, Universal universal, MdBusiness mdBusiness, MdVertexDAOIF mdVertex)
  {
    this.type = go;
    this.universal = universal;
    this.mdBusiness = mdBusiness;
    this.mdVertex = mdVertex;
  }

  public GeoObjectType getType()
  {
    return type;
  }
  
  public void setType(GeoObjectType type)
  {
    this.type = type;
  }

  public Universal getUniversal()
  {
    return universal;
  }

  public void setUniversal(Universal universal)
  {
    this.universal = universal;
  }

  public MdBusiness getMdBusiness()
  {
    return mdBusiness;
  }

  public MdBusinessDAOIF getMdBusinessDAO()
  {
    return (MdBusinessDAOIF) BusinessFacade.getEntityDAO(this.mdBusiness);
  }

  public void setMdBusiness(MdBusiness mdBusiness)
  {
    this.mdBusiness = mdBusiness;
  }

  public MdVertexDAOIF getMdVertex()
  {
    return mdVertex;
  }

  public void setMdVertex(MdVertexDAOIF mdVertex)
  {
    this.mdVertex = mdVertex;
  }

  public String getCode()
  {
    return this.type.getCode();
  }

  public GeometryType getGeometryType()
  {
    return this.type.getGeometryType();
  }

  public boolean isGeometryEditable()
  {
    return this.type.isGeometryEditable();
  }

  public LocalizedValue getLabel()
  {
    return this.type.getLabel();
  }

  public LocalizedValue getDescription()
  {
    return this.type.getDescription();
  }
  
  public boolean getIsAbstract()
  {
    return this.type.getIsAbstract();
  }

  public JsonObject toJSON(CustomSerializer serializer)
  {
    return this.type.toJSON(serializer);
  }

  public Map<String, AttributeType> getAttributeMap()
  {
    return this.type.getAttributeMap();
  }

  public Optional<AttributeType> getAttribute(String name)
  {
    return this.type.getAttribute(name);
  }

  public String definesType()
  {
    return this.mdBusiness.definesType();
  }

  public List<? extends MdAttributeConcreteDAOIF> definesAttributes()
  {
    return this.getMdBusinessDAO().definesAttributes();
  }

  public void deleteAllRecords()
  {
    this.getMdBusinessDAO().getBusinessDAO().deleteAllRecords();
  }

  public GeoObjectTypeMetadata getMetadata()
  {
    return GeoObjectTypeMetadata.getByKey(this.universal.getKey());
  }

  /**
   * @return The organization associated with this GeoObjectType.
   */
  public Organization getOrganization()
  {
    Actor owner = this.universal.getOwner();

    if (! ( owner instanceof Roles ))
    {
      return null; // If we get here, then the GeoObjectType was not created
                   // correctly.
    }
    else
    {
      Roles uniRole = (Roles) owner;
      String myOrgCode = RegistryRole.Type.parseOrgCode(uniRole.getRoleName());

      return Organization.getByCode(myOrgCode);
    }
  }

  public String getOrganizationCode()
  {
    return this.getOrganization().getCode();
  }

  public List<ServerGeoObjectType> getChildren(ServerHierarchyType hierarchy)
  {
    return hierarchy.getChildren(this);
  }

  public ServerGeoObjectType getSuperType()
  {
    if (this.type.getSuperTypeCode() != null && this.type.getSuperTypeCode().length() > 0)
    {
      return ServerGeoObjectType.get(this.type.getSuperTypeCode());
    }

    return null;
  }

  public List<ServerGeoObjectType> getSubtypes()
  {
    List<ServerGeoObjectType> children = new LinkedList<>();

    if (this.getIsAbstract())
    {
      MdGeoVertexQuery query = new MdGeoVertexQuery(new QueryFactory());
      query.WHERE(query.getSuperMdVertex().EQ(this.getMdVertex().getOid()));

      try (OIterator<? extends MdGeoVertex> iterator = query.getIterator())
      {
        while (iterator.hasNext())
        {
          MdGeoVertex cUniversal = (MdGeoVertex) iterator.next();

          children.add(ServerGeoObjectType.get(MdGeoVertexDAO.get(cUniversal.getOid())));
        }

      }
    }

    return children;
  }

  public Set<ServerHierarchyType> getHierarchiesOfSubTypes()
  {
    List<ServerGeoObjectType> subtypes = this.getSubtypes();
    Set<ServerHierarchyType> hierarchyTypes = new TreeSet<ServerHierarchyType>(new Comparator<ServerHierarchyType>()
    {
      @Override
      public int compare(ServerHierarchyType o1, ServerHierarchyType o2)
      {
        return o1.getCode().compareTo(o2.getCode());
      }
    });

    for (ServerGeoObjectType type : subtypes)
    {
      hierarchyTypes.addAll(type.getHierarchies(false));
    }

    return hierarchyTypes;
  }

  public List<ServerHierarchyType> getHierarchies()
  {
    return getHierarchies(true);
  }

  public List<ServerHierarchyType> getHierarchies(boolean includeFromSuperType)
  {
    List<ServerHierarchyType> hierarchies = new LinkedList<ServerHierarchyType>();

    List<ServerHierarchyType> hierarchyTypes = ServiceFactory.getMetadataCache().getAllHierarchyTypes();
    Universal root = Universal.getRoot();

    for (ServerHierarchyType hierarchyType : hierarchyTypes)
    {
      Organization org = hierarchyType.getOrganization();

      if (ServiceFactory.getHierarchyPermissionService().canRead(org.getCode()))
      {

        if (this.isRoot(hierarchyType))
        {
          hierarchies.add(hierarchyType);
        }
        else
        {
          // Note: Ordered ancestors always includes self
          Collection<?> parents = GeoEntityUtil.getOrderedAncestors(root, this.getUniversal(), hierarchyType.getUniversalType());

          if (parents.size() > 1)
          {
            hierarchies.add(hierarchyType);
          }
        }

      }
    }

    if (includeFromSuperType)
    {
      ServerGeoObjectType superType = this.getSuperType();

      if (superType != null)
      {
        hierarchies.addAll(superType.getHierarchies(includeFromSuperType));
      }
    }

    return hierarchies;
  }

  /**
   * @param sType
   *          Hierarchy Type
   * 
   * @return If this geo object type is the direct (non-inherited) root of the
   *         given hierarchy
   */
  public boolean isRoot(ServerHierarchyType sType)
  {
    List<ServerGeoObjectType> roots = sType.getDirectRootNodes();

    for (ServerGeoObjectType root : roots)
    {
      if (root.getCode().equals(this.type.getCode()))
      {
        return true;
      }
    }

    return false;
  }

  @Transaction
  public InheritedHierarchyAnnotation setInheritedHierarchy(ServerHierarchyType forHierarchy, ServerHierarchyType inheritedHierarchy)
  {
    // Ensure that this geo object type is the root geo object type for the "For
    // Hierarchy"
    if (!this.isRoot(forHierarchy) || this.getIsAbstract())
    {
      throw new HierarchyRootException();
    }

    HierarchicalRelationshipType fhrt = forHierarchy.getHierarchicalRelationshipType();
    HierarchicalRelationshipType ihrt = inheritedHierarchy.getHierarchicalRelationshipType();

    if (InheritedHierarchyAnnotation.getByForHierarchical(fhrt) != null)
    {
      throw new UnsupportedOperationException("A hierarchy cannot inherit from more than one other hierarchy");
    }

    if (this.isRoot(inheritedHierarchy))
    {
      throw new UnsupportedOperationException("A root node in a hierarchy cannot be inherited");
    }

    InheritedHierarchyAnnotation annotation = new InheritedHierarchyAnnotation();
    annotation.setUniversal(this.universal);
    annotation.setInheritedHierarchicalRelationshipType(ihrt);
    annotation.setForHierarchicalRelationshipType(fhrt);
    annotation.apply();

    return annotation;
  }

  @Transaction
  public void removeInheritedHierarchy(ServerHierarchyType forHierarchy)
  {
    InheritedHierarchyAnnotation annotation = InheritedHierarchyAnnotation.getByForHierarchical(forHierarchy.getHierarchicalRelationshipType());

    if (annotation != null)
    {
      annotation.delete();
    }
  }

  public ServerHierarchyType getInheritedHierarchy(ServerHierarchyType hierarchy)
  {
    return this.getInheritedHierarchy(hierarchy.getHierarchicalRelationshipType());
  }

  public ServerHierarchyType getInheritedHierarchy(HierarchicalRelationshipType hierarchicalRelationship)
  {
    InheritedHierarchyAnnotation annotation = InheritedHierarchyAnnotation.get(this.universal, hierarchicalRelationship);

    if (annotation != null)
    {
      HierarchicalRelationshipType inheritedHierarchicalRelationshipType = annotation.getInheritedHierarchicalRelationshipType();

      return ServerHierarchyType.get(inheritedHierarchicalRelationshipType);
    }

    return null;
  }

  /**
   * Returns all ancestors of a GeoObjectType
   * 
   * @param hierarchyType
   *          The Hierarchy code
   * @param includeInheritedTypes
   *          TODO
   * @param GeoObjectType
   *          child
   * 
   * @return
   */
  public List<ServerGeoObjectType> getTypeAncestors(ServerHierarchyType hierarchyType, Boolean includeInheritedTypes)
  {
    List<ServerGeoObjectType> ancestors = new LinkedList<ServerGeoObjectType>();

    Collection<com.runwaysdk.business.ontology.Term> list = GeoEntityUtil.getOrderedAncestors(Universal.getRoot(), this.getUniversal(), hierarchyType.getUniversalType());

    list.forEach(term -> {
      Universal parent = (Universal) term;

      if (!parent.getKeyName().equals(Universal.ROOT) && !parent.getOid().equals(this.getUniversal().getOid()))
      {
        ServerGeoObjectType sParent = ServerGeoObjectType.get(parent);

        ancestors.add(sParent);

        if (includeInheritedTypes && sParent.isRoot(hierarchyType))
        {
          ServerHierarchyType inheritedHierarchy = sParent.getInheritedHierarchy(hierarchyType);

          if (inheritedHierarchy != null)
          {
            ancestors.addAll(0, sParent.getTypeAncestors(inheritedHierarchy, includeInheritedTypes));
          }
        }
      }
    });

    if (ancestors.size() == 0)
    {
      ServerGeoObjectType superType = this.getSuperType();

      if (superType != null)
      {
        return superType.getTypeAncestors(hierarchyType, includeInheritedTypes);
      }
    }

    return ancestors;
  }

  /**
   * Finds the actual hierarchy used for the parent type if the parent type is
   * inherited from a different hierarchy
   * 
   * @param hierarchyType
   * @param parent
   * @return
   */
  public ServerHierarchyType findHierarchy(ServerHierarchyType hierarchyType, ServerGeoObjectType parent)
  {
    Collection<com.runwaysdk.business.ontology.Term> list = GeoEntityUtil.getOrderedAncestors(Universal.getRoot(), this.getUniversal(), hierarchyType.getUniversalType());

    for (Object term : list)
    {
      Universal universal = (Universal) term;

      if (parent.getUniversal().getOid().equals(universal.getOid()))
      {
        return hierarchyType;
      }

      ServerGeoObjectType sParent = ServerGeoObjectType.get(universal);

      if (sParent.isRoot(hierarchyType))
      {
        ServerHierarchyType inheritedHierarchy = sParent.getInheritedHierarchy(hierarchyType);

        if (inheritedHierarchy != null)
        {
          return sParent.findHierarchy(inheritedHierarchy, parent);
        }
      }
    }

    return hierarchyType;
  }

  public boolean getIsPrivate()
  {
    return this.type.getIsPrivate();
  }

  public void setIsPrivate(Boolean isPrivate)
  {
    this.type.setIsPrivate(isPrivate);
  }

  @Override
  public boolean equals(Object obj)
  {
    if (obj instanceof ServerGeoObjectType)
    {
      return ( (ServerGeoObjectType) obj ).getCode().equals(this.getCode());
    }
    else if (obj instanceof GeoObjectType)
    {
      return ( (GeoObjectType) obj ).getCode().equals(this.getCode());
    }

    return false;
  }

  @Override
  public int hashCode()
  {
    return this.getCode().hashCode();
  }

  @Override
  public String toString()
  {
    return GeoObjectTypeMetadata.sGetClassDisplayLabel() + " : " + this.getCode();
  }

  public String getMaintainerRoleName()
  {
    ServerGeoObjectType superType = this.getSuperType();

    if (superType != null)
    {
      return superType.getMaintainerRoleName();
    }

    return RegistryRole.Type.getRM_RoleName(this.getOrganization().getCode(), this.getCode());
  }

  /**
   * Returns a {@link Universal} from the code value on the given
   * {@link GeoObjectType}.
   * 
   * @param got
   * @return a {@link Universal} from the code value on the given
   *         {@link GeoObjectType}.
   */
  public static Universal geoObjectTypeToUniversal(GeoObjectType got)
  {
    return Universal.getByKey(got.getCode());
  }

  public static ServerGeoObjectType get(String code)
  {
    return ServerGeoObjectType.get(code, false);
  }

  @SuppressWarnings("unchecked")
  public static ServerGeoObjectType get(String code, boolean nullIfNotFound)
  {
    if (code == null || code.equals(Universal.ROOT))
    {
      return RootGeoObjectType.INSTANCE;
    }

    TransactionState state = TransactionState.getCurrentTransactionState();

    if (state != null)
    {
      Object transactionCache = state.getTransactionObject("transaction-state");

      if (transactionCache != null)
      {
        Map<String, ServerElement> cache = (Map<String, ServerElement>) transactionCache;
        ServerElement element = cache.get(code);

        if (element != null && element instanceof ServerGeoObjectType)
        {
          return (ServerGeoObjectType) element;
        }
      }
    }

    Optional<ServerGeoObjectType> geoObjectType = ServiceFactory.getMetadataCache().getGeoObjectType(code);

    if (geoObjectType.isPresent())
    {
      return geoObjectType.get();
    }
    else if (!nullIfNotFound)
    {
      net.geoprism.registry.DataNotFoundException ex = new net.geoprism.registry.DataNotFoundException();
      ex.setTypeLabel(GeoObjectTypeMetadata.sGetClassDisplayLabel());
      ex.setDataIdentifier(code);
      ex.setAttributeLabel(GeoObjectTypeMetadata.getAttributeDisplayLabel(DefaultAttribute.CODE.getName()));
      throw ex;
    }

    return null;
  }

  public static ServerGeoObjectType get(Universal universal)
  {
    String code = universal.getKey();

    return getFromCache(code);
  }

  public static ServerGeoObjectType get(GeoObjectType geoObjectType)
  {
    String code = geoObjectType.getCode();

    return getFromCache(code);
  }

  public static ServerGeoObjectType get(MdVertexDAOIF mdVertex)
  {
    String code = mdVertex.getTypeName();

    return getFromCache(code);
  }

  @SuppressWarnings("unchecked")
  private static ServerGeoObjectType getFromCache(String code)
  {
    TransactionState state = TransactionState.getCurrentTransactionState();

    if (state != null)
    {
      Object transactionCache = state.getTransactionObject("transaction-state");

      if (transactionCache != null)
      {
        Map<String, ServerElement> cache = (Map<String, ServerElement>) transactionCache;
        ServerElement element = cache.get(code);

        if (element != null && element instanceof ServerGeoObjectType)
        {
          return (ServerGeoObjectType) element;
        }
      }
    }

    return ServiceFactory.getMetadataCache().getGeoObjectType(code).get();
  }

  // public String buildRMRoleName()
  // {
  // String ownerActorOid = this.universal.getOwnerOid();
  // Organization.getRootOrganization(ownerActorOid)
  // }

  /**
   * Returns the {link MdAttributeConcreteDAOIF} for the given
   * {@link AttributeType} defined on the given {@link MdBusiness} or null no
   * such attribute is defined.
   * 
   * @param attributeName
   * 
   * @return
   */
  public static MdAttributeConcreteDAOIF getMdAttribute(MdClass mdClass, String attributeName)
  {
    MdClassDAOIF mdClassDAO = (MdClassDAOIF) BusinessFacade.getEntityDAO(mdClass);

    return (MdAttributeConcreteDAOIF) mdClassDAO.definesAttribute(attributeName);
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

      ClassificationType classificationType = ClassificationType.getByCode(classificationTypeCode);

      mdAttribute = new MdAttributeClassification();
      MdAttributeClassification mdAttributeTerm = (MdAttributeClassification) mdAttribute;
      mdAttributeTerm.setReferenceMdClassification(classificationType.getMdClassificationObject());

      Term root = attributeClassificationType.getRootTerm();

      if (root != null)
      {
        Classification classification = Classification.get(classificationType, root.getCode());

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
      Classifier classTerm = TermConverter.buildIfNotExistdMdBusinessClassifier(mdClass);

      // Create the root term node for this attribute
      Classifier attributeTermRoot = TermConverter.buildIfNotExistAttribute(mdClass, mdAttributeTerm.getAttributeName(), classTerm);

      // Make this the root term of the multi-attribute
      attributeTermRoot.addClassifierTermAttributeRoots(mdAttributeTerm).apply();

      AttributeTermType attributeTermType = (AttributeTermType) attributeType;

      LocalizedValue label = RegistryLocalizedValueConverter.convert(attributeTermRoot.getDisplayLabel());

      org.commongeoregistry.adapter.Term term = new org.commongeoregistry.adapter.Term(attributeTermRoot.getClassifierId(), label, new LocalizedValue(""));
      attributeTermType.setRootTerm(term);
    }
    return mdAttribute;
  }

  /**
   * Creates an {@link MdAttributeConcrete} for the given {@link MdBusiness}
   * from the given {@link AttributeType}
   * 
   * @pre assumes no attribute has been defined on the type with the given name.
   * 
   * @param mdBusiness
   *          Type to receive attribute definition
   * @param attributeType
   *          newly defined attribute
   * 
   * @return {@link AttributeType}
   */
  @Transaction
  public static MdAttributeConcrete updateMdAttributeFromAttributeType(MdClass mdClass, AttributeType attributeType)
  {
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

          ClassificationType classificationType = ClassificationType.getByCode(classificationTypeCode);

          Term root = attributeClassificationType.getRootTerm();

          if (root != null)
          {
            Classification classification = Classification.get(classificationType, root.getCode());

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
