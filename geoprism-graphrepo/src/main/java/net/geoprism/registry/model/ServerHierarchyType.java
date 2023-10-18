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

import java.util.LinkedList;
import java.util.List;

import org.commongeoregistry.adapter.Optional;
import org.commongeoregistry.adapter.constants.DefaultAttribute;
import org.commongeoregistry.adapter.dataaccess.LocalizedValue;
import org.commongeoregistry.adapter.metadata.HierarchyType;

import com.runwaysdk.business.BusinessFacade;
import com.runwaysdk.business.LocalStruct;
import com.runwaysdk.dataaccess.MdEdgeDAOIF;
import com.runwaysdk.gis.constants.GISConstants;
import com.runwaysdk.system.gis.geo.AllowedIn;
import com.runwaysdk.system.gis.geo.GeoEntity;
import com.runwaysdk.system.gis.geo.LocatedIn;
import com.runwaysdk.system.gis.geo.Universal;
import com.runwaysdk.system.metadata.MdTermRelationship;

import net.geoprism.registry.HierarchicalRelationshipType;
import net.geoprism.registry.HierarchyMetadata;
import net.geoprism.registry.Organization;
import net.geoprism.registry.RegistryConstants;
import net.geoprism.registry.conversion.RegistryLocalizedValueConverter;
import net.geoprism.registry.model.graph.GraphStrategy;
import net.geoprism.registry.model.graph.ServerHierarchyStrategy;
import net.geoprism.registry.service.permission.HierarchyTypePermissionServiceIF;
import net.geoprism.registry.service.request.ServiceFactory;

public class ServerHierarchyType implements ServerElement, GraphType
{
  private HierarchicalRelationshipType hierarchicalRelationship;

  public ServerHierarchyType(HierarchicalRelationshipType hierarchicalRelationship)
  {
    this.hierarchicalRelationship = hierarchicalRelationship;
  }

  public HierarchicalRelationshipType getHierarchicalRelationshipType()
  {
    return hierarchicalRelationship;
  }

  public MdTermRelationship getMdTermRelationship()
  {
    return this.hierarchicalRelationship.getMdTermRelationship();
  }

  public MdEdgeDAOIF getMdEdge()
  {
    return (MdEdgeDAOIF) BusinessFacade.getEntityDAO(this.hierarchicalRelationship.getMdEdge());
  }

  // public HierarchyType getType()
  // {
  // return type;
  // }
  //
  // public void setType(HierarchyType type)
  // {
  // this.type = type;
  // }

  public String getCode()
  {
    return this.hierarchicalRelationship.getCode();
  }

  public String getProgress()
  {
    return this.hierarchicalRelationship.getProgress();
  }

  public String getAccessConstraints()
  {
    return this.hierarchicalRelationship.getAccessConstraints();
  }

  public String getUseConstraints()
  {
    return this.hierarchicalRelationship.getUseConstraints();
  }

  public String getAcknowledgement()
  {
    return this.hierarchicalRelationship.getAcknowledgement();
  }

  public String getDisclaimer()
  {
    return this.hierarchicalRelationship.getDisclaimer();
  }

  public LocalStruct getDescription()
  {
    return this.hierarchicalRelationship.getDescription();
  }

  public LocalStruct getDisplayLabel()
  {
    return this.hierarchicalRelationship.getDisplayLabel();
  }

  public LocalizedValue getLabel()
  {
    return RegistryLocalizedValueConverter.convert(this.getDisplayLabel());
  }

  public String getUniversalType()
  {
    return this.hierarchicalRelationship.getMdTermRelationship().definesType();
  }

  public void setHierarchicalRelationshipType(HierarchicalRelationshipType hierarchicalRelationship)
  {
    this.hierarchicalRelationship = hierarchicalRelationship;
  }

  /**
   * @return The organization associated with this HierarchyType. If this
   *         HierarchyType is AllowedIn (or constructed incorrectly) this method
   *         will return null.
   */
  public String getOrganizationCode()
  {
    return this.getOrganization().getCode();
  }

  /**
   * @return The organization associated with this HierarchyType. If this
   *         HierarchyType is AllowedIn (or constructed incorrectly) this method
   *         will return null.
   */
  public Organization getOrganization()
  {
    return this.hierarchicalRelationship.getOrganization();
  }

  @Override
  public String toString()
  {
    return HierarchyMetadata.sGetClassDisplayLabel() + " : " + this.getCode();
  }

  @Override
  public GraphStrategy getStrategy()
  {
    return new ServerHierarchyStrategy(this);
  }

  public static ServerHierarchyType get(String hierarchyTypeCode)
  {
    Optional<ServerHierarchyType> hierarchyType = ServiceFactory.getMetadataCache().getHierachyType(hierarchyTypeCode);

    if (!hierarchyType.isPresent())
    {
      net.geoprism.registry.DataNotFoundException ex = new net.geoprism.registry.DataNotFoundException();
      ex.setTypeLabel(HierarchyMetadata.sGetClassDisplayLabel());
      ex.setDataIdentifier(hierarchyTypeCode);
      ex.setAttributeLabel(HierarchyMetadata.getAttributeDisplayLabel(DefaultAttribute.CODE.getName()));
      throw ex;
    }

    return hierarchyType.get();
  }

  public static ServerHierarchyType get(HierarchyType hierarchyType)
  {
    return get(hierarchyType.getCode());
  }

  public static ServerHierarchyType get(HierarchicalRelationshipType hierarchicalRelationship)
  {
    return ServiceFactory.getMetadataCache().getHierachyType(hierarchicalRelationship.getCode()).get();
  }

  public static ServerHierarchyType get(MdTermRelationship universalRelationship)
  {
    String code = buildHierarchyKeyFromMdTermRelUniversal(universalRelationship.getKey());
    return ServiceFactory.getMetadataCache().getHierachyType(code).get();
  }

  public static ServerHierarchyType get(MdEdgeDAOIF mdEdge)
  {
    String code = buildHierarchyKeyFromMdEdge(mdEdge);

    return ServiceFactory.getMetadataCache().getHierachyType(code).get();
  }

  /**
   * Turns the given {@link HierarchyType} code into the corresponding
   * {@link MdTermRelationship} key for the {@link Universal} relationship.
   * 
   * @param hierarchyCode
   *          {@link HierarchyType} code
   * @return corresponding {@link MdTermRelationship} key.
   */
  public static String buildMdTermRelUniversalKey(String hierarchyCode)
  {
    // If the code is for the LocatedIn hierarchy, then the relationship that
    // defines the
    // Universals for that relationship is AllowedIn.
    if (hierarchyCode.trim().equals(LocatedIn.class.getSimpleName()))
    {
      return AllowedIn.CLASS;
    }
    else
    {
      return GISConstants.GEO_PACKAGE + "." + hierarchyCode + RegistryConstants.UNIVERSAL_RELATIONSHIP_POST;
    }
  }

  /**
   * Convert the given {@link MdTermRelationShip} key for {@link Universal}s
   * into a {@link HierarchyType} key.
   * 
   * @param mdTermRelKey
   *          {@link MdTermRelationShip} key
   * @return a {@link HierarchyType} key.
   */
  public static String buildHierarchyKeyFromMdTermRelUniversal(String mdTermRelKey)
  {
    // the hierarchyType code for the allowed in relationship is the located in
    // relationship
    if (mdTermRelKey.trim().equals(AllowedIn.CLASS))
    {
      return LocatedIn.class.getSimpleName();
    }
    else
    {
      int startIndex = GISConstants.GEO_PACKAGE.length() + 1;

      int endIndex = mdTermRelKey.indexOf(RegistryConstants.UNIVERSAL_RELATIONSHIP_POST);

      String hierarchyKey;
      if (endIndex > -1)
      {
        hierarchyKey = mdTermRelKey.substring(startIndex, endIndex);
      }
      else
      {
        hierarchyKey = mdTermRelKey.substring(startIndex, mdTermRelKey.length());
      }

      return hierarchyKey;
    }
  }

  public static String buildHierarchyKeyFromMdEdge(MdEdgeDAOIF mdEdge)
  {
    return mdEdge.getTypeName();
  }

  /**
   * Turns the given {@link MdTermRelationShip} key for a {@link Universal} into
   * the corresponding {@link MdTermRelationship} key for the {@link GeoEntity}
   * relationship.
   * 
   * @param hierarchyCode
   *          {@link HierarchyType} code
   * @return corresponding {@link MdTermRelationship} key.
   */
  public static String buildMdTermRelGeoEntityKey(String hierarchyCode)
  {
    // Check for existing GeoPrism hierarchyTypes
    if (hierarchyCode.trim().equals(LocatedIn.class.getSimpleName()))
    {
      return LocatedIn.CLASS;
    }
    else
    {
      return GISConstants.GEO_PACKAGE + "." + hierarchyCode;
    }
  }

  public static String buildMdEdgeKey(String hierarchyCode)
  {
    return RegistryConstants.UNIVERSAL_GRAPH_PACKAGE + "." + hierarchyCode;
  }

  /**
   * Convert the given {@link MdTermRelationShip} key for a {@link GeoEntities}
   * into a {@link HierarchyType} key.
   * 
   * @param mdTermRelKey
   *          {@link MdTermRelationShip} key
   * @return a {@link HierarchyType} key.
   */
  public static String buildHierarchyKeyFromMdTermRelGeoEntity(String mdTermRelKey)
  {
    int startIndex = GISConstants.GEO_PACKAGE.length() + 1;

    return mdTermRelKey.substring(startIndex, mdTermRelKey.length());
  }

  public static List<ServerHierarchyType> getForOrganization(Organization organization)
  {
    final HierarchyTypePermissionServiceIF service = ServiceFactory.getHierarchyPermissionService();
    final List<ServerHierarchyType> list = new LinkedList<ServerHierarchyType>();

    List<ServerHierarchyType> lHt = ServiceFactory.getMetadataCache().getAllHierarchyTypes();
    // Filter out what they're not allowed to see

    lHt.forEach(ht -> {
      if (service.canWrite(organization.getCode()))
      {
        list.add(ht);
      }
    });

    return list;
  }

  public static List<ServerHierarchyType> getAll()
  {
    return ServiceFactory.getMetadataCache().getAllHierarchyTypes();
  }

  @Override
  public MdEdgeDAOIF getMdEdgeDAO()
  {
    return this.getMdEdge();
  }

}
