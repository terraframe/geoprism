/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Geoprism Registry(tm).
 *
 * Geoprism Registry(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Geoprism Registry(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Geoprism Registry(tm). If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism.registry.business;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.commongeoregistry.adapter.Optional;
import org.commongeoregistry.adapter.constants.DefaultAttribute;
import org.commongeoregistry.adapter.dataaccess.GeoObject;
import org.commongeoregistry.adapter.dataaccess.LocalizedValue;
import org.commongeoregistry.adapter.metadata.AttributeType;
import org.commongeoregistry.adapter.metadata.GeoObjectType;
import org.commongeoregistry.adapter.metadata.RegistryRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.runwaysdk.ComponentIF;
import com.runwaysdk.business.BusinessFacade;
import com.runwaysdk.business.rbac.Operation;
import com.runwaysdk.business.rbac.RoleDAO;
import com.runwaysdk.constants.ComponentInfo;
import com.runwaysdk.constants.IndexTypes;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeCharacterInfo;
import com.runwaysdk.constants.MdAttributeConcreteInfo;
import com.runwaysdk.constants.MdAttributeLocalCharacterEmbeddedInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdAttributeReferenceInfo;
import com.runwaysdk.dataaccess.AttributeDoesNotExistException;
import com.runwaysdk.dataaccess.DuplicateDataException;
import com.runwaysdk.dataaccess.MdAttributeBlobDAOIF;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeEncryptionDAOIF;
import com.runwaysdk.dataaccess.MdAttributeFileDAOIF;
import com.runwaysdk.dataaccess.MdAttributeIndicatorDAOIF;
import com.runwaysdk.dataaccess.MdAttributeLocalDAOIF;
import com.runwaysdk.dataaccess.MdAttributeMultiTermDAOIF;
import com.runwaysdk.dataaccess.MdAttributeStructDAOIF;
import com.runwaysdk.dataaccess.MdAttributeTermDAOIF;
import com.runwaysdk.dataaccess.MdAttributeTimeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeUUIDDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdGraphClassDAOIF;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.attributes.AttributeValueException;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.dataaccess.metadata.MdAttributeBooleanDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeCharacterDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeConcreteDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeLocalCharacterEmbeddedDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeUUIDDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.gis.dataaccess.MdAttributeGeometryDAOIF;
import com.runwaysdk.gis.dataaccess.metadata.graph.MdGeoVertexDAO;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.session.Request;
import com.runwaysdk.session.RequestType;
import com.runwaysdk.session.Session;
import com.runwaysdk.system.Actor;
import com.runwaysdk.system.Roles;
import com.runwaysdk.system.gis.geo.Universal;
import com.runwaysdk.system.gis.metadata.graph.MdGeoVertex;
import com.runwaysdk.system.gis.metadata.graph.MdGeoVertexQuery;
import com.runwaysdk.system.metadata.MdAttributeCharacter;
import com.runwaysdk.system.metadata.MdAttributeConcrete;
import com.runwaysdk.system.metadata.MdAttributeIndices;
import com.runwaysdk.system.metadata.MdAttributeUUID;
import com.runwaysdk.system.metadata.MdBusiness;

import net.geoprism.ontology.Classifier;
import net.geoprism.ontology.GeoEntityUtil;
import net.geoprism.registry.ChainInheritanceException;
import net.geoprism.registry.CodeLengthException;
import net.geoprism.registry.DuplicateGeoObjectTypeException;
import net.geoprism.registry.GeoObjectTypeAssignmentException;
import net.geoprism.registry.HierarchicalRelationshipType;
import net.geoprism.registry.HierarchyRootException;
import net.geoprism.registry.InheritedHierarchyAnnotation;
import net.geoprism.registry.Organization;
import net.geoprism.registry.RegistryConstants;
import net.geoprism.registry.TypeInUseException;
import net.geoprism.registry.conversion.GeometryTypeFactory;
import net.geoprism.registry.conversion.LocalizedValueConverter;
import net.geoprism.registry.conversion.RegistryAttributeTypeConverter;
import net.geoprism.registry.conversion.RegistryLocalizedValueConverter;
import net.geoprism.registry.conversion.TermConverter;
import net.geoprism.registry.graph.GeoVertex;
import net.geoprism.registry.graph.GeoVertexType;
import net.geoprism.registry.graph.transition.Transition;
import net.geoprism.registry.graph.transition.TransitionEvent;
import net.geoprism.registry.model.GeoObjectTypeMetadata;
import net.geoprism.registry.model.ServerGeoObjectType;
import net.geoprism.registry.model.ServerHierarchyType;
import net.geoprism.registry.permission.PermissionContext;
import net.geoprism.registry.service.ServiceFactory;

@Service
public class GeoObjectTypeBusinessService implements GeoObjectTypeBusinessServiceIF
{
  @Autowired
  private TransitionEventBusinessServiceIF tranEventServ;

  @Autowired
  private TransitionBusinessServiceIF      tranServ;

  @Autowired
  private HierarchyTypeBusinessServiceIF   htServ;

  public GeoObjectTypeBusinessService()
  {
  }

  public List<ServerGeoObjectType> getSubtypes(ServerGeoObjectType sgot)
  {
    List<ServerGeoObjectType> children = new LinkedList<>();

    if (sgot.getIsAbstract())
    {
      MdGeoVertexQuery query = new MdGeoVertexQuery(new QueryFactory());
      query.WHERE(query.getSuperMdVertex().EQ(sgot.getMdVertex().getOid()));

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

  public Set<ServerHierarchyType> getHierarchiesOfSubTypes(ServerGeoObjectType sgot)
  {
    List<ServerGeoObjectType> subtypes = getSubtypes(sgot);
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
      hierarchyTypes.addAll(getHierarchies(type, false));
    }

    return hierarchyTypes;
  }

  /**
   * @param sType
   *          Hierarchy Type
   * 
   * @return If this geo object type is the direct (non-inherited) root of the
   *         given hierarchy
   */
  public boolean isRoot(ServerGeoObjectType sgot, ServerHierarchyType sType)
  {
    List<ServerGeoObjectType> roots = htServ.getDirectRootNodes(sType);

    for (ServerGeoObjectType root : roots)
    {
      if (root.getCode().equals(sgot.getType().getCode()))
      {
        return true;
      }
    }

    return false;
  }

  @Transaction
  public InheritedHierarchyAnnotation setInheritedHierarchy(ServerGeoObjectType sgot, ServerHierarchyType forHierarchy, ServerHierarchyType inheritedHierarchy)
  {
    // Ensure that this geo object type is the root geo object type for the "For
    // Hierarchy"
    if (!this.isRoot(sgot, forHierarchy) || sgot.getIsAbstract())
    {
      throw new HierarchyRootException();
    }

    HierarchicalRelationshipType fhrt = forHierarchy.getHierarchicalRelationshipType();
    HierarchicalRelationshipType ihrt = inheritedHierarchy.getHierarchicalRelationshipType();

    if (InheritedHierarchyAnnotation.getByForHierarchical(fhrt) != null)
    {
      throw new UnsupportedOperationException("A hierarchy cannot inherit from more than one other hierarchy");
    }

    if (isRoot(sgot, inheritedHierarchy))
    {
      throw new UnsupportedOperationException("A root node in a hierarchy cannot be inherited");
    }

    InheritedHierarchyAnnotation annotation = new InheritedHierarchyAnnotation();
    annotation.setUniversal(sgot.getUniversal());
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

  public List<ServerHierarchyType> getHierarchies(ServerGeoObjectType sgot)
  {
    return getHierarchies(sgot, true);
  }

  public List<ServerHierarchyType> getHierarchies(ServerGeoObjectType sgot, boolean includeFromSuperType)
  {
    List<ServerHierarchyType> hierarchies = new LinkedList<ServerHierarchyType>();

    List<ServerHierarchyType> hierarchyTypes = ServiceFactory.getMetadataCache().getAllHierarchyTypes();
    Universal root = Universal.getRoot();

    for (ServerHierarchyType hierarchyType : hierarchyTypes)
    {
      Organization org = hierarchyType.getOrganization();

      if (ServiceFactory.getHierarchyPermissionService().canRead(org.getCode()))
      {
        if (isRoot(sgot, hierarchyType))
        {
          hierarchies.add(hierarchyType);
        }
        else
        {
          // Note: Ordered ancestors always includes self
          Collection<?> parents = GeoEntityUtil.getOrderedAncestors(root, sgot.getUniversal(), hierarchyType.getUniversalType());

          if (parents.size() > 1)
          {
            hierarchies.add(hierarchyType);
          }
        }

      }
    }

    if (includeFromSuperType)
    {
      ServerGeoObjectType superType = sgot.getSuperType();

      if (superType != null)
      {
        hierarchies.addAll(getHierarchies(superType, includeFromSuperType));
      }
    }

    return hierarchies;
  }

  public ServerHierarchyType getInheritedHierarchy(ServerGeoObjectType sgot, ServerHierarchyType hierarchy)
  {
    return this.getInheritedHierarchy(sgot, hierarchy.getHierarchicalRelationshipType());
  }

  public ServerHierarchyType getInheritedHierarchy(ServerGeoObjectType sgot, HierarchicalRelationshipType hierarchicalRelationship)
  {
    InheritedHierarchyAnnotation annotation = InheritedHierarchyAnnotation.get(sgot.getUniversal(), hierarchicalRelationship);

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
  public List<ServerGeoObjectType> getTypeAncestors(ServerGeoObjectType sgot, ServerHierarchyType hierarchyType, Boolean includeInheritedTypes)
  {
    List<ServerGeoObjectType> ancestors = new LinkedList<ServerGeoObjectType>();

    Collection<com.runwaysdk.business.ontology.Term> list = GeoEntityUtil.getOrderedAncestors(Universal.getRoot(), sgot.getUniversal(), hierarchyType.getUniversalType());

    list.forEach(term -> {
      Universal parent = (Universal) term;

      if (!parent.getKeyName().equals(Universal.ROOT) && !parent.getOid().equals(sgot.getUniversal().getOid()))
      {
        ServerGeoObjectType sParent = ServerGeoObjectType.get(parent);

        ancestors.add(sParent);

        if (includeInheritedTypes && isRoot(sParent, hierarchyType))
        {
          ServerHierarchyType inheritedHierarchy = getInheritedHierarchy(sParent, hierarchyType);

          if (inheritedHierarchy != null)
          {
            ancestors.addAll(0, getTypeAncestors(sParent, inheritedHierarchy, includeInheritedTypes));
          }
        }
      }
    });

    if (ancestors.size() == 0)
    {
      ServerGeoObjectType superType = sgot.getSuperType();

      if (superType != null)
      {
        return getTypeAncestors(superType, hierarchyType, includeInheritedTypes);
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
  public ServerHierarchyType findHierarchy(ServerGeoObjectType sgot, ServerHierarchyType hierarchyType, ServerGeoObjectType parent)
  {
    Collection<com.runwaysdk.business.ontology.Term> list = GeoEntityUtil.getOrderedAncestors(Universal.getRoot(), sgot.getUniversal(), hierarchyType.getUniversalType());

    for (Object term : list)
    {
      Universal universal = (Universal) term;

      if (parent.getUniversal().getOid().equals(universal.getOid()))
      {
        return hierarchyType;
      }

      ServerGeoObjectType sParent = ServerGeoObjectType.get(universal);

      if (isRoot(sParent, hierarchyType))
      {
        ServerHierarchyType inheritedHierarchy = getInheritedHierarchy(sParent, hierarchyType);

        if (inheritedHierarchy != null)
        {
          return findHierarchy(sParent, inheritedHierarchy, parent);
        }
      }
    }

    return hierarchyType;
  }

  public List<ServerGeoObjectType> getChildren(ServerGeoObjectType sgot, ServerHierarchyType hierarchy)
  {
    return htServ.getChildren(hierarchy, sgot);
  }

  /**
   * Adds default attributes to the given {@link MdBusinessDAO} according to the
   * Common Geo-Registry specification for {@link GeoObject}.
   * 
   * @param mdBusinessDAO
   *          {@link MdBusinessDAO} that will define the default attributes.
   */
  @Transaction
  public void createDefaultAttributes(Universal universal, MdBusiness definingMdBusiness)
  {
    // DefaultAttribute.UID - Defined on the MdBusiness and the values are from
    // the {@code GeoObject#OID};
    MdAttributeUUID uuidMdAttr = new MdAttributeUUID();
    uuidMdAttr.setAttributeName(RegistryConstants.UUID);
    uuidMdAttr.getDisplayLabel().setValue(RegistryConstants.UUID_LABEL);
    uuidMdAttr.getDescription().setValue("The universal unique identifier of the feature.");
    uuidMdAttr.setDefiningMdClass(definingMdBusiness);
    uuidMdAttr.setRequired(true);
    uuidMdAttr.addIndexType(MdAttributeIndices.UNIQUE_INDEX);
    uuidMdAttr.apply();

    MdAttributeBooleanDAO invalidMdAttr = MdAttributeBooleanDAO.newInstance();
    invalidMdAttr.setValue(MdAttributeConcreteInfo.NAME, DefaultAttribute.INVALID.getName());
    invalidMdAttr.setStructValue(MdAttributeConcreteInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, DefaultAttribute.INVALID.getDefaultLocalizedName());
    invalidMdAttr.setStructValue(MdAttributeConcreteInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, DefaultAttribute.INVALID.getDefaultDescription());
    invalidMdAttr.setValue(MdAttributeConcreteInfo.DEFINING_MD_CLASS, definingMdBusiness.getOid());
    invalidMdAttr.setValue(MdAttributeConcreteInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    invalidMdAttr.setValue(MdAttributeConcreteInfo.DEFAULT_VALUE, MdAttributeBooleanInfo.FALSE);
    invalidMdAttr.addItem(MdAttributeConcreteInfo.INDEX_TYPE, IndexTypes.NON_UNIQUE_INDEX.getOid());
    invalidMdAttr.apply();
  }

  @Transaction
  public void createDefaultAttributes(Universal universal, MdGraphClassDAOIF mdClass)
  {
    MdAttributeUUIDDAO uuidMdAttr = MdAttributeUUIDDAO.newInstance();
    uuidMdAttr.setValue(MdAttributeConcreteInfo.NAME, RegistryConstants.UUID);
    uuidMdAttr.setStructValue(MdAttributeConcreteInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, RegistryConstants.UUID_LABEL);
    uuidMdAttr.setStructValue(MdAttributeConcreteInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, RegistryConstants.UUID_LABEL);
    uuidMdAttr.setValue(MdAttributeConcreteInfo.DEFINING_MD_CLASS, mdClass.getOid());
    uuidMdAttr.setValue(MdAttributeConcreteInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    uuidMdAttr.addItem(MdAttributeConcreteInfo.INDEX_TYPE, IndexTypes.UNIQUE_INDEX.getOid());
    uuidMdAttr.apply();

    MdAttributeBooleanDAO existsMdAttr = MdAttributeBooleanDAO.newInstance();
    existsMdAttr.setValue(MdAttributeConcreteInfo.NAME, DefaultAttribute.EXISTS.getName());
    existsMdAttr.setStructValue(MdAttributeConcreteInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, DefaultAttribute.EXISTS.getDefaultLocalizedName());
    existsMdAttr.setStructValue(MdAttributeConcreteInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, DefaultAttribute.EXISTS.getDefaultDescription());
    existsMdAttr.setValue(MdAttributeConcreteInfo.DEFINING_MD_CLASS, mdClass.getOid());
    existsMdAttr.setValue(MdAttributeConcreteInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    existsMdAttr.setValue(MdAttributeConcreteInfo.DEFAULT_VALUE, MdAttributeBooleanInfo.FALSE);
    existsMdAttr.addItem(MdAttributeConcreteInfo.INDEX_TYPE, IndexTypes.NON_UNIQUE_INDEX.getOid());
    existsMdAttr.apply();

    MdAttributeBooleanDAO invalidMdAttr = MdAttributeBooleanDAO.newInstance();
    invalidMdAttr.setValue(MdAttributeConcreteInfo.NAME, DefaultAttribute.INVALID.getName());
    invalidMdAttr.setStructValue(MdAttributeConcreteInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, DefaultAttribute.INVALID.getDefaultLocalizedName());
    invalidMdAttr.setStructValue(MdAttributeConcreteInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, DefaultAttribute.INVALID.getDefaultDescription());
    invalidMdAttr.setValue(MdAttributeConcreteInfo.DEFINING_MD_CLASS, mdClass.getOid());
    invalidMdAttr.setValue(MdAttributeConcreteInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    invalidMdAttr.setValue(MdAttributeConcreteInfo.DEFAULT_VALUE, MdAttributeBooleanInfo.FALSE);
    invalidMdAttr.addItem(MdAttributeConcreteInfo.INDEX_TYPE, IndexTypes.NON_UNIQUE_INDEX.getOid());
    invalidMdAttr.apply();

    // DefaultAttribute.DISPLAY_LABEL
    MdAttributeLocalCharacterEmbeddedDAO labelMdAttr = MdAttributeLocalCharacterEmbeddedDAO.newInstance();
    labelMdAttr.setValue(MdAttributeLocalCharacterEmbeddedInfo.NAME, DefaultAttribute.DISPLAY_LABEL.getName());
    labelMdAttr.setStructValue(MdAttributeLocalCharacterEmbeddedInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, DefaultAttribute.DISPLAY_LABEL.getDefaultLocalizedName());
    labelMdAttr.setStructValue(MdAttributeLocalCharacterEmbeddedInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, DefaultAttribute.DISPLAY_LABEL.getDefaultDescription());
    labelMdAttr.setValue(MdAttributeLocalCharacterEmbeddedInfo.DEFINING_MD_CLASS, mdClass.getOid());
    labelMdAttr.setValue(MdAttributeLocalCharacterEmbeddedInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    labelMdAttr.apply();
  }

  @Transaction
  public ServerGeoObjectType create(String json)
  {
    GeoObjectType geoObjectType = GeoObjectType.fromJSON(json, ServiceFactory.getAdapter());

    return this.create(geoObjectType);
  }

  @Transaction
  public ServerGeoObjectType create(GeoObjectType geoObjectType)
  {
    if (!isValidName(geoObjectType.getCode()))
    {
      throw new AttributeValueException("The geo object type code has an invalid character", geoObjectType.getCode());
    }

    if (geoObjectType.getCode().length() > 64)
    {
      // Setting the typename on the MdBusiness creates this limitation.
      CodeLengthException ex = new CodeLengthException();
      ex.setLength(64);
      throw ex;
    }

    ServiceFactory.getGeoObjectTypePermissionService().enforceCanCreate(geoObjectType.getOrganizationCode(), geoObjectType.getIsPrivate());

    String superTypeCode = geoObjectType.getSuperTypeCode();
    Boolean isAbstract = geoObjectType.getIsAbstract();

    ServerGeoObjectType superType = null;

    if (superTypeCode != null && superTypeCode.length() > 0)
    {
      superType = ServerGeoObjectType.get(superTypeCode);
      geoObjectType.setGeometryType(superType.getGeometryType());
    }

    if (isAbstract && superType != null)
    {
      throw new ChainInheritanceException();
    }

    if (superType != null && !superType.getIsAbstract())
    {
      throw new GeoObjectTypeAssignmentException();
    }

    Universal universal = new Universal();
    universal.setUniversalId(geoObjectType.getCode());
    universal.setIsLeafType(false);
    universal.setIsGeometryEditable(geoObjectType.isGeometryEditable());

    // Set the owner of the universal to the id of the corresponding role of the
    // responsible organization.
    String organizationCode = geoObjectType.getOrganizationCode();
    RegistryLocalizedValueConverter.setOwner(universal, organizationCode);

    LocalizedValueConverter.populate(universal.getDisplayLabel(), geoObjectType.getLabel());
    LocalizedValueConverter.populate(universal.getDescription(), geoObjectType.getDescription());

    com.runwaysdk.system.gis.geo.GeometryType geometryType = GeometryTypeFactory.get(geoObjectType.getGeometryType());

    // Clear the default value
    universal.clearGeometryType();
    universal.addGeometryType(geometryType);

    MdBusiness mdBusiness = new MdBusiness();
    mdBusiness.setPackageName(RegistryConstants.UNIVERSAL_MDBUSINESS_PACKAGE);

    // The CODE name becomes the class name
    mdBusiness.setTypeName(universal.getUniversalId());
    mdBusiness.setGenerateSource(false);
    mdBusiness.setIsAbstract(isAbstract);
    mdBusiness.setStructValue(MdAttributeConcreteInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, universal.getDisplayLabel().getValue());
    mdBusiness.setStructValue(MdAttributeConcreteInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, universal.getDescription().getValue());

    if (superType != null)
    {
      mdBusiness.setSuperMdBusiness(superType.getMdBusiness());
    }
    else
    {
      mdBusiness.setPublish(false);
    }

    try
    {
      // The DuplicateDataException on code was found to be thrown here.
      // I've created a larger try/catch here just in case.
      mdBusiness.apply();

      // Add the default attributes.
      if (superType == null)
      {
        this.createDefaultAttributes(universal, mdBusiness);
      }

      universal.setMdBusiness(mdBusiness);

      universal.apply();

      GeoObjectTypeMetadata metadata = new GeoObjectTypeMetadata();
      metadata.setIsPrivate(geoObjectType.getIsPrivate());
      metadata.setUniversal(universal);
      metadata.apply();
    }
    catch (DuplicateDataException ex)
    {
      DuplicateGeoObjectTypeException ex2 = new DuplicateGeoObjectTypeException();
      ex2.setDuplicateValue(geoObjectType.getCode());
      throw ex2;
    }

    // Create the MdGeoVertexClass
    MdGeoVertexDAO mdVertex = GeoVertexType.create(universal.getUniversalId(), universal.getOwnerOid(), isAbstract, superType);

    if (superType == null)
    {
      this.createDefaultAttributes(universal, mdVertex);
    }

    if (!isAbstract)
    {
      // DefaultAttribute.CODE
      MdAttributeCharacter businessCodeMdAttr = new MdAttributeCharacter();
      businessCodeMdAttr.setAttributeName(DefaultAttribute.CODE.getName());
      businessCodeMdAttr.getDisplayLabel().setValue(DefaultAttribute.CODE.getDefaultLocalizedName());
      businessCodeMdAttr.getDescription().setValue(DefaultAttribute.CODE.getDefaultDescription());
      businessCodeMdAttr.setDatabaseSize(MdAttributeCharacterInfo.MAX_CHARACTER_SIZE);
      businessCodeMdAttr.setDefiningMdClass(mdBusiness);
      businessCodeMdAttr.setRequired(true);
      businessCodeMdAttr.addIndexType(MdAttributeIndices.UNIQUE_INDEX);
      businessCodeMdAttr.apply();

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
    }

    // Build the parent class term root if it does not exist.
    TermConverter.buildIfNotExistdMdBusinessClassifier(mdBusiness);

    ServerGeoObjectType serverGeoObjectType = this.build(universal);

    return serverGeoObjectType;
  }

  /**
   * Assigns all permissions to the {@link ComponentIF} to the given role.
   * 
   * Precondition: component is either a {@link MdGeoVertex} or a
   * {@link MdBusiness}.
   * 
   * @param component
   * @param role
   */
  protected void assignAllPermissions(ComponentIF component, Roles role)
  {
    RoleDAO roleDAO = (RoleDAO) BusinessFacade.getEntityDAO(role);
    roleDAO.grantPermission(Operation.CREATE, component.getOid());
    roleDAO.grantPermission(Operation.DELETE, component.getOid());
    roleDAO.grantPermission(Operation.WRITE, component.getOid());
    roleDAO.grantPermission(Operation.WRITE_ALL, component.getOid());
  }

  /**
   * The GeoObjectType is a DTO type, which means it contains data which has
   * been localized to a particular user's session. We need to rebuild this
   * object such that it includes relevant request information (like the correct
   * locale).
   */
  public GeoObjectType buildType(ServerGeoObjectType serverType)
  {
    com.runwaysdk.system.gis.geo.GeometryType geoPrismgeometryType = serverType.getUniversal().getGeometryType().get(0);

    org.commongeoregistry.adapter.constants.GeometryType cgrGeometryType = GeometryTypeFactory.get(geoPrismgeometryType);

    LocalizedValue label = RegistryLocalizedValueConverter.convert(serverType.getUniversal().getDisplayLabel());
    LocalizedValue description = RegistryLocalizedValueConverter.convert(serverType.getUniversal().getDescription());

    String ownerActerOid = serverType.getUniversal().getOwnerOid();

    String organizationCode = Organization.getRootOrganizationCode(ownerActerOid);

    MdVertexDAOIF superType = serverType.getMdVertex().getSuperClass();

    GeoObjectType type = new GeoObjectType(serverType.getUniversal().getUniversalId(), cgrGeometryType, label, description, serverType.getUniversal().getIsGeometryEditable(), organizationCode, ServiceFactory.getAdapter());
    type.setIsAbstract(serverType.getMdBusiness().getIsAbstract());

    type = convertAttributeTypes(serverType.getUniversal(), type, serverType.getMdBusiness());

    try
    {
      GeoObjectTypeMetadata metadata = GeoObjectTypeMetadata.getByKey(serverType.getUniversal().getKey());
      type.setIsPrivate(metadata.getIsPrivate());
      metadata.injectDisplayLabels(type);
    }
    catch (DataNotFoundException | AttributeDoesNotExistException e)
    {
      type.setIsPrivate(false);
    }

    if (superType != null && !superType.definesType().equals(GeoVertex.CLASS))
    {
      String parentCode = superType.getTypeName();

      type.setSuperTypeCode(parentCode);
    }

    serverType.setType(type);

    return type;
  }

  /**
   * Creates an {@link MdAttributeConcrete} for the given {@link MdBusiness}
   * from the given {@link AttributeType}
   * 
   * @pre assumes no attribute has been defined on the type with the given name.
   * @param geoObjectType
   *          TODO
   * @param mdBusiness
   *          Type to receive attribute definition
   * @param attributeType
   *          newly defined attribute
   * 
   * @return {@link AttributeType}
   */
  @Transaction
  public MdAttributeConcrete createMdAttributeFromAttributeType(ServerGeoObjectType serverType, AttributeType attributeType)
  {
    MdAttributeConcrete mdAttribute = ServerGeoObjectType.createMdAttributeFromAttributeType(serverType.getMdBusiness(), attributeType);

    ( (MdVertexDAO) serverType.getMdVertex() ).copyAttribute(MdAttributeDAO.get(mdAttribute.getOid()));

    return mdAttribute;
  }

  public void removeAttribute(ServerGeoObjectType serverType, String attributeName)
  {
    deleteMdAttributeFromAttributeType(serverType, attributeName);

    serverType.getType().removeAttribute(attributeName);

    // If this did not error out then add to the cache
    refreshCache(serverType);

    // Refresh the users session
    ( (Session) Session.getCurrentSession() ).reloadPermissions();
  }

  /**
   * Delete the {@link MdAttributeConcreteDAOIF} from the given {
   * 
   * @param type
   *          TODO
   * @param mdBusiness
   * @param attributeName
   */
  @Transaction
  public void deleteMdAttributeFromAttributeType(ServerGeoObjectType serverType, String attributeName)
  {
    MdAttributeConcreteDAOIF mdAttributeConcreteDAOIF = ServerGeoObjectType.getMdAttribute(serverType.getMdBusiness(), attributeName);

    if (mdAttributeConcreteDAOIF != null)
    {
      if (mdAttributeConcreteDAOIF instanceof MdAttributeTermDAOIF || mdAttributeConcreteDAOIF instanceof MdAttributeMultiTermDAOIF)
      {
        String attributeTermKey = TermConverter.buildtAtttributeKey(serverType.getMdBusiness().getTypeName(), mdAttributeConcreteDAOIF.definesAttribute());

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

    MdAttributeDAOIF mdAttributeDAO = serverType.getMdVertex().definesAttribute(attributeName);

    if (mdAttributeDAO != null)
    {
      mdAttributeDAO.getBusinessDAO().delete();
    }
  }

  public AttributeType updateAttributeType(ServerGeoObjectType serverType, String attributeTypeJSON)
  {
    JsonObject attrObj = JsonParser.parseString(attributeTypeJSON).getAsJsonObject();
    AttributeType attrType = AttributeType.parse(attrObj);

    MdAttributeConcrete mdAttribute = ServerGeoObjectType.updateMdAttributeFromAttributeType(serverType.getMdBusiness(), attrType);
    attrType = new RegistryAttributeTypeConverter().build(MdAttributeConcreteDAO.get(mdAttribute.getOid()));

    serverType.getType().addAttribute(attrType);

    // If this did not error out then add to the cache
    refreshCache(serverType);

    return attrType;
  }

  @Override
  public AttributeType createAttributeType(ServerGeoObjectType serverType, AttributeType attributeType)
  {
    MdAttributeConcrete mdAttribute = createMdAttributeFromAttributeType(serverType, attributeType);

    attributeType = new RegistryAttributeTypeConverter().build(MdAttributeConcreteDAO.get(mdAttribute.getOid()));

    serverType.getType().addAttribute(attributeType);

    // If this did not error out then add to the cache
    refreshCache(serverType);

    // Refresh the users session
    if (Session.getCurrentSession() != null)
    {
      ( (Session) Session.getCurrentSession() ).reloadPermissions();
    }

    return attributeType;
  }

  @Override
  public AttributeType createAttributeType(ServerGeoObjectType sgot, String attributeTypeJSON)
  {
    JsonObject attrObj = JsonParser.parseString(attributeTypeJSON).getAsJsonObject();

    AttributeType attrType = AttributeType.parse(attrObj);

    return createAttributeType(sgot, attrType);
  }

  private void refreshCache(ServerGeoObjectType type)
  {
    ServiceFactory.getMetadataCache().addGeoObjectType(type);

    // Refresh all of the subtypes
    List<ServerGeoObjectType> subtypes = getSubtypes(type);
    for (ServerGeoObjectType subtype : subtypes)
    {
      ServerGeoObjectType type2 = build(subtype.getUniversal());

      ServiceFactory.getMetadataCache().addGeoObjectType(type2);
    }
  }

  public ServerGeoObjectType build(Universal universal)
  {
    MdBusiness mdBusiness = universal.getMdBusiness();
    MdGeoVertexDAO mdVertex = GeoVertexType.getMdGeoVertex(universal.getUniversalId());

    ServerGeoObjectType server = new ServerGeoObjectType(null, universal, mdBusiness, mdVertex);
    buildType(server);

    return server;
  }

  public GeoObjectType convertAttributeTypes(Universal uni, GeoObjectType gt, MdBusiness mdBusiness)
  {
    if (mdBusiness != null)
    {
      MdBusinessDAOIF mdBusinessDAOIF = (MdBusinessDAOIF) BusinessFacade.getEntityDAO(mdBusiness);

      // Standard attributes are defined by default on the GeoObjectType

      RegistryAttributeTypeConverter builder = new RegistryAttributeTypeConverter();

      List<? extends MdAttributeConcreteDAOIF> definedMdAttributeList = mdBusinessDAOIF.getAllDefinedMdAttributes();

      for (MdAttributeConcreteDAOIF mdAttribute : definedMdAttributeList)
      {
        if (this.convertMdAttributeToAttributeType(mdAttribute))
        {
          AttributeType attributeType = builder.build(mdAttribute);

          if (attributeType != null)
          {
            gt.addAttribute(attributeType);
          }
        }
      }
    }

    return gt;
  }

  /**
   * True if the given {@link MdAttributeConcreteDAOIF} should be converted to
   * an {@link AttributeType}, false otherwise. Standard attributes such as
   * {@link DefaultAttribute} are already defined on a {@link GeoObjectType} and
   * do not need to be converted. This method also returns true if the attribute
   * is not a system attribute.
   * 
   * @return True if the given {@link MdAttributeConcreteDAOIF} should be
   *         converted to an {@link AttributeType}, false otherwise.
   */
  protected boolean convertMdAttributeToAttributeType(MdAttributeConcreteDAOIF mdAttribute)
  {
    if (mdAttribute.isSystem() || ( mdAttribute instanceof MdAttributeStructDAOIF && ! ( mdAttribute instanceof MdAttributeLocalDAOIF ) ) || mdAttribute instanceof MdAttributeEncryptionDAOIF || mdAttribute instanceof MdAttributeIndicatorDAOIF || mdAttribute instanceof MdAttributeBlobDAOIF || mdAttribute instanceof MdAttributeGeometryDAOIF || mdAttribute instanceof MdAttributeFileDAOIF || mdAttribute instanceof MdAttributeTimeDAOIF || mdAttribute instanceof MdAttributeUUIDDAOIF || mdAttribute.getType().equals(MdAttributeReferenceInfo.CLASS))
    {
      return false;
    }
    else if (mdAttribute.definesAttribute().equals(ComponentInfo.KEY) || mdAttribute.definesAttribute().equals(ComponentInfo.TYPE))
    {
      return false;
    }
    else
    {
      return true;
    }
  }

  @Request(RequestType.SESSION)
  public List<GeoObjectType> getAncestors(String code, String hierarchyCode, Boolean includeInheritedTypes, Boolean includeChild)
  {
    ServerGeoObjectType child = ServerGeoObjectType.get(code);
    ServerHierarchyType hierarchyType = ServerHierarchyType.get(hierarchyCode);

    List<ServerGeoObjectType> ancestors = getTypeAncestors(child, hierarchyType, includeInheritedTypes);

    if (includeChild)
    {
      ancestors.add(child);
    }

    return ancestors.stream().map(stype -> stype.getType()).collect(Collectors.toList());
  }

  /**
   * Deletes the {@link GeoObjectType} with the given code. Do nothing if the
   * type does not exist.
   * 
   * @param sessionId
   * @param code
   *          code of the {@link GeoObjectType} to delete.
   */
  @Request(RequestType.SESSION)
  public void deleteGeoObjectType(String code)
  {
    ServerGeoObjectType type = ServerGeoObjectType.get(code);

    if (type != null)
    {
      delete(type);
    }
  }

  protected void delete(ServerGeoObjectType type)
  {
    ServiceFactory.getGeoObjectTypePermissionService().enforceCanDelete(type.getOrganization().getCode(), type, type.getIsPrivate());

    this.deleteInTransaction(type);

    Session session = (Session) Session.getCurrentSession();

    // If this is being called in a JUnit test scenario then there is no
    // session object in the request.
    if (session != null)
    {
      // Refresh the users session
      session.setUser(Session.getCurrentSession().getUser());
    }

    // If we get here then it was successfully deleted
    // We have to do a full metadata cache
    // refresh because the GeoObjectType is
    // embedded in the HierarchyType
    ServiceFactory.getGraphRepoService().refreshMetadataCache();
  }

  @Transaction
  protected void deleteInTransaction(ServerGeoObjectType type)
  {
    List<ServerHierarchyType> hierarchies = getHierarchies(type, true);

    if (hierarchies.size() > 0)
    {
      StringBuilder codes = hierarchies.stream().collect(StringBuilder::new, (x, y) -> x.append(y.getCode()), (a, b) -> a.append(",").append(b));

      throw new TypeInUseException("Cannot delete a GeoObjectType used in the hierarchies: " + codes);
    }

    /*
     * Delete all subtypes
     */
    List<ServerGeoObjectType> subtypes = getSubtypes(type);

    for (ServerGeoObjectType subtype : subtypes)
    {
      deleteInTransaction(subtype);
    }

    /*
     * Delete all inherited hierarchies
     */
    List<? extends InheritedHierarchyAnnotation> annotations = InheritedHierarchyAnnotation.getByUniversal(type.getUniversal());

    for (InheritedHierarchyAnnotation annotation : annotations)
    {
      annotation.delete();
    }

    GeoVertexType.remove(type.getUniversal().getUniversalId());

    /*
     * Delete all Attribute references
     */
    // AttributeHierarchy.deleteByUniversal(this.universal);

    type.getMetadata().delete();

    // This deletes the {@link MdBusiness} as well
    type.getUniversal().delete(false);

    // Delete the term root
    Classifier classRootTerm = TermConverter.buildIfNotExistdMdBusinessClassifier(type.getMdBusiness());
    classRootTerm.delete();

    // Delete the roles. Sub types don't have direct roles, they only have the
    // roles specified on the super type.
    if (type.getSuperType() == null)
    {
      Actor ownerActor = type.getUniversal().getOwner();

      if (ownerActor instanceof Roles)
      {
        Roles ownerRole = (Roles) ownerActor;
        String roleName = ownerRole.getRoleName();

        if (RegistryRole.Type.isOrgRole(roleName))
        {
          String organizationCode = RegistryRole.Type.parseOrgCode(roleName);

          String geoObjectTypeCode = type.getCode();

          String rmRoleName = RegistryRole.Type.getRM_RoleName(organizationCode, geoObjectTypeCode);
          Roles role = Roles.findRoleByName(rmRoleName);
          role.delete();

          String rcRoleName = RegistryRole.Type.getRC_RoleName(organizationCode, geoObjectTypeCode);
          role = Roles.findRoleByName(rcRoleName);
          role.delete();

          String acRoleName = RegistryRole.Type.getAC_RoleName(organizationCode, geoObjectTypeCode);
          role = Roles.findRoleByName(acRoleName);
          role.delete();
        }
      }
    }

    // Delete the transition and transition events
    tranEventServ.removeAll(type);

    tranServ.removeAll(type);
  }

  /**
   * Adds an attribute to the given {@link GeoObjectType}.
   * 
   * @pre given {@link GeoObjectType} must already exist.
   * 
   * @param sessionId
   *
   * @param geoObjectTypeCode
   *          string of the {@link GeoObjectType} to be updated.
   * @param attributeTypeJSON
   *          AttributeType to be added to the GeoObjectType
   * @return updated {@link GeoObjectType}
   */
  @Request(RequestType.SESSION)
  public AttributeType createAttributeType(String geoObjectTypeCode, String attributeTypeJSON)
  {
    ServerGeoObjectType got = ServerGeoObjectType.get(geoObjectTypeCode);

    ServiceFactory.getGeoObjectTypePermissionService().enforceCanWrite(got.getOrganization().getCode(), got, got.getIsPrivate());

    AttributeType attrType = createAttributeType(got, attributeTypeJSON);

    return attrType;
  }

  /**
   * Updates an attribute in the given {@link GeoObjectType}.
   * 
   * @pre given {@link GeoObjectType} must already exist.
   * 
   * @param sessionId
   * @param geoObjectTypeCode
   *          string of the {@link GeoObjectType} to be updated.
   * @param attributeTypeJSON
   *          AttributeType to be added to the GeoObjectType
   * @return updated {@link AttributeType}
   */
  @Request(RequestType.SESSION)
  public AttributeType updateAttributeType(String geoObjectTypeCode, String attributeTypeJSON)
  {
    ServerGeoObjectType got = ServerGeoObjectType.get(geoObjectTypeCode);

    ServiceFactory.getGeoObjectTypePermissionService().enforceCanWrite(got.getOrganization().getCode(), got, got.getIsPrivate());

    AttributeType attrType = updateAttributeType(got, attributeTypeJSON);

    return attrType;
  }

  /**
   * Deletes an attribute from the given {@link GeoObjectType}.
   * 
   * @pre given {@link GeoObjectType} must already exist.
   * @pre given {@link GeoObjectType} must already exist.
   * 
   * @param sessionId
   * @param gtId
   *          string of the {@link GeoObjectType} to be updated.
   * @param attributeName
   *          Name of the attribute to be removed from the GeoObjectType
   * @return updated {@link GeoObjectType}
   */
  @Request(RequestType.SESSION)
  public void deleteAttributeType(String gtId, String attributeName)
  {
    ServerGeoObjectType got = ServerGeoObjectType.get(gtId);

    ServiceFactory.getGeoObjectTypePermissionService().enforceCanWrite(got.getOrganization().getCode(), got, got.getIsPrivate());

    removeAttribute(got, attributeName);
  }

  /**
   * Updates the given {@link GeoObjectType} represented as JSON.
   * 
   * @pre given {@link GeoObjectType} must already exist.
   * 
   * @param sessionId
   * @param gtJSON
   *          JSON of the {@link GeoObjectType} to be updated.
   * @return updated {@link GeoObjectType}
   */
  @Request(RequestType.SESSION)
  public GeoObjectType updateGeoObjectType(String gtJSON)
  {
    GeoObjectType geoObjectType = GeoObjectType.fromJSON(gtJSON, ServiceFactory.getAdapter());
    ServerGeoObjectType serverGeoObjectType = ServerGeoObjectType.get(geoObjectType.getCode());

    ServiceFactory.getGeoObjectTypePermissionService().enforceCanWrite(geoObjectType.getOrganizationCode(), serverGeoObjectType, geoObjectType.getIsPrivate());

    update(serverGeoObjectType, geoObjectType);

    return serverGeoObjectType.getType();
  }

  protected void update(ServerGeoObjectType serverGeoObjectType, GeoObjectType geoObjectTypeNew)
  {
    GeoObjectType geoObjectTypeModified = serverGeoObjectType.getType().copy(geoObjectTypeNew);

    Universal universal = updateInTrans(serverGeoObjectType, geoObjectTypeModified);

    ServerGeoObjectType geoObjectTypeModifiedApplied = build(universal);

    // If this did not error out then add to the cache
    ServiceFactory.getMetadataCache().refreshGeoObjectType(geoObjectTypeModifiedApplied);

    // Modifications to supertypes can affect subtypes (i.e. changing
    // isPrivate). We should refresh them as well.
    if (geoObjectTypeModifiedApplied.getIsAbstract())
    {
      List<ServerGeoObjectType> subtypes = getSubtypes(geoObjectTypeModifiedApplied);

      for (ServerGeoObjectType subtype : subtypes)
      {
        ServerGeoObjectType refreshedSubtype = build(subtype.getUniversal());
        ServiceFactory.getMetadataCache().refreshGeoObjectType(refreshedSubtype);
      }
    }
    serverGeoObjectType.setType(geoObjectTypeModifiedApplied.getType());
    serverGeoObjectType.setUniversal(geoObjectTypeModifiedApplied.getUniversal());
    serverGeoObjectType.setMdBusiness(geoObjectTypeModifiedApplied.getMdBusiness());
  }

  @Transaction
  protected Universal updateInTrans(ServerGeoObjectType serverGeoObjectType, GeoObjectType geoObjectType)
  {
    serverGeoObjectType.getUniversal().lock();

    serverGeoObjectType.getUniversal().setIsGeometryEditable(geoObjectType.isGeometryEditable());
    RegistryLocalizedValueConverter.populate(serverGeoObjectType.getUniversal().getDisplayLabel(), geoObjectType.getLabel());
    RegistryLocalizedValueConverter.populate(serverGeoObjectType.getUniversal().getDescription(), geoObjectType.getDescription());

    serverGeoObjectType.getUniversal().apply();

    MdBusiness mdBusiness = serverGeoObjectType.getUniversal().getMdBusiness();

    mdBusiness.lock();
    mdBusiness.getDisplayLabel().setValue(serverGeoObjectType.getUniversal().getDisplayLabel().getValue());
    mdBusiness.getDescription().setValue(serverGeoObjectType.getUniversal().getDescription().getValue());
    mdBusiness.apply();

    GeoObjectTypeMetadata metadata = serverGeoObjectType.getMetadata();
    if (!metadata.getIsPrivate().equals(geoObjectType.getIsPrivate()))
    {
      metadata.appLock();
      metadata.setIsPrivate(geoObjectType.getIsPrivate());
      metadata.apply();
    }

    mdBusiness.unlock();

    serverGeoObjectType.getUniversal().unlock();

    return serverGeoObjectType.getUniversal();
  }

  /**
   * Creates a {@link GeoObjectType} from the given JSON.
   * 
   * @param sessionId
   * @param gtJSON
   *          JSON of the {@link GeoObjectType} to be created.
   * @return newly created {@link GeoObjectType}
   */

  public GeoObjectType createGeoObjectType(String gtJSON)
  {
    ServerGeoObjectType type = null;

    type = create(gtJSON);

    // Refresh the users session
    ( (Session) Session.getCurrentSession() ).reloadPermissions();

    // If this did not error out then add to the cache
    ServiceFactory.getMetadataCache().addGeoObjectType(type);

    return type.getType();
  }

  /**
   * Returns the {@link GeoObjectType}s with the given codes or all
   * {@link GeoObjectType}s if no codes are provided.
   * 
   * @param codes
   *          codes of the {@link GeoObjectType}s.
   * @param context
   * @return the {@link GeoObjectType}s with the given codes or all
   *         {@link GeoObjectType}s if no codes are provided.
   */
  public List<GeoObjectType> getGeoObjectTypes(String[] codes, PermissionContext context)
  {
    List<ServerGeoObjectType> gots;

    if (codes == null || codes.length == 0)
    {
      gots = ServiceFactory.getMetadataCache().getAllGeoObjectTypes();
    }
    else
    {
      gots = new ArrayList<ServerGeoObjectType>(codes.length);

      for (int i = 0; i < codes.length; ++i)
      {
        Optional<ServerGeoObjectType> optional = ServiceFactory.getMetadataCache().getGeoObjectType(codes[i]);

        if (optional.isPresent())
        {
          gots.add(optional.get());
        }
        else
        {
          net.geoprism.registry.DataNotFoundException ex = new net.geoprism.registry.DataNotFoundException();
          ex.setTypeLabel(GeoObjectTypeMetadata.sGetClassDisplayLabel());
          ex.setDataIdentifier(codes[i]);
          ex.setAttributeLabel(GeoObjectTypeMetadata.getAttributeDisplayLabel(DefaultAttribute.CODE.getName()));
          throw ex;
        }
      }
    }

    Iterator<ServerGeoObjectType> it = gots.iterator();
    while (it.hasNext())
    {
      ServerGeoObjectType serverGot = it.next();

      // Filter ones that they can't see due to permissions
      if (context.equals(PermissionContext.READ))
      {
        if (!ServiceFactory.getGeoObjectTypePermissionService().canRead(serverGot.getOrganization().getCode(), serverGot, serverGot.getIsPrivate()))
        {
          it.remove();
          continue; // If we don't have continue here, then it could invoke
                    // it.remove twice which throws an error.
        }
      }
      else
      {
        if (!ServiceFactory.getGeoObjectTypePermissionService().canWrite(serverGot.getOrganization().getCode(), serverGot, serverGot.getIsPrivate()))
        {
          it.remove();
          continue; // If we don't have continue here, then it could invoke
                    // it.remove twice which throws an error.
        }
      }
    }

    return gots.stream().map(server -> buildType(server)).collect(Collectors.toList());
  }

  public static boolean isValidName(String name)
  {
    if (name.contains(" ") || name.contains("<") || name.contains(">") || name.contains("-") || name.contains("+") || name.contains("=") || name.contains("!") || name.contains("@") || name.contains("#") || name.contains("$") || name.contains("%") || name.contains("^") || name.contains("&") || name.contains("*") || name.contains("?") || name.contains(";") || name.contains(":") || name.contains(",") || name.contains("^") || name.contains("{") || name.contains("}") || name.contains("]") || name.contains("[") || name.contains("`") || name.contains("~") || name.contains("|") || name.contains("/") || name.contains("\\"))
    {
      return false;
    }

    return true;
  }

}
