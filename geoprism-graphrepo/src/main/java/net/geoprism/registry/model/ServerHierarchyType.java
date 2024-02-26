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
package net.geoprism.registry.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import org.commongeoregistry.adapter.dataaccess.LocalizedValue;
import org.commongeoregistry.adapter.metadata.HierarchyType;

import com.runwaysdk.business.graph.GraphQuery;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdEdgeDAOIF;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.metadata.MdClassDAO;
import com.runwaysdk.dataaccess.metadata.graph.MdEdgeDAO;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.session.Session;

import net.geoprism.registry.DataNotFoundException;
import net.geoprism.registry.Organization;
import net.geoprism.registry.command.CacheEventType;
import net.geoprism.registry.command.HierarchicalRelationshipTypeCacheEventCommand;
import net.geoprism.registry.conversion.RegistryLocalizedValueConverter;
import net.geoprism.registry.graph.GeoObjectType;
import net.geoprism.registry.graph.HierarchicalRelationshipType;
import net.geoprism.registry.model.graph.GraphStrategy;
import net.geoprism.registry.model.graph.ServerHierarchyStrategy;
import net.geoprism.registry.service.permission.HierarchyTypePermissionServiceIF;
import net.geoprism.registry.service.request.ServiceFactory;

public class ServerHierarchyType extends DirtySoftReference<HierarchicalRelationshipType> implements ServerElement, GraphType
{
  private org.commongeoregistry.adapter.metadata.HierarchyType dto;

  public ServerHierarchyType(HierarchicalRelationshipType hierarchicalRelationship)
  {
    super(hierarchicalRelationship);
  }

  @Transaction
  public void update(HierarchyType dto)
  {
    this.getObject().update(dto);

    new HierarchicalRelationshipTypeCacheEventCommand(this, CacheEventType.UPDATE).doIt();
  }

  @Transaction
  public void delete()
  {
    this.getObject().delete();
  }

  @Override
  protected void refresh(HierarchicalRelationshipType object)
  {
    HierarchicalRelationshipType type = HierarchicalRelationshipType.getByCode(object.getCode());

    this.setObject(type);
    this.dto = null;
  }

  public org.commongeoregistry.adapter.metadata.HierarchyType toDTO()
  {
    if (this.dto == null)
    {
      this.dto = this.getObject().toDTO();
    }

    return this.dto;
  }

  public MdEdgeDAOIF getObjectEdge()
  {
    return MdEdgeDAO.get(this.getObject().getObjectEdgeOid());
  }

  public MdEdgeDAOIF getDefinitionEdge()
  {
    return MdEdgeDAO.get(this.getObject().getDefinitionEdgeOid());
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
    return this.getObject().getCode();
  }

  public String getProgress()
  {
    return this.getObject().getProgress();
  }

  public String getAccessConstraints()
  {
    return this.getObject().getAccessConstraints();
  }

  public String getUseConstraints()
  {
    return this.getObject().getUseConstraints();
  }

  public String getAcknowledgement()
  {
    return this.getObject().getAcknowledgement();
  }

  public String getDisclaimer()
  {
    return this.getObject().getDisclaimer();
  }

  public LocalizedValue getLabel()
  {
    return RegistryLocalizedValueConverter.convert(this.getObject().getEmbeddedComponent(HierarchicalRelationshipType.LABEL));
  }

  public LocalizedValue getDescription()
  {
    return RegistryLocalizedValueConverter.convert(this.getObject().getEmbeddedComponent(HierarchicalRelationshipType.DESCRIPTION));
  }

  /**
   * @return The organization associated with this HierarchyType. If this
   *         HierarchyType is AllowedIn (or constructed incorrectly) this method
   *         will return null.
   */
  public String getOrganizationCode()
  {
    return this.getObject().getOrganizationCode();
  }

  /**
   * @return The organization associated with this HierarchyType. If this
   *         HierarchyType is AllowedIn (or constructed incorrectly) this method
   *         will return null.
   */
  public ServerOrganization getOrganization()
  {
    return ServerOrganization.getByCode(this.getObject().getOrganizationCode());
  }

  @Override
  public String toString()
  {
    return this.getObject().getClassDisplayLabel() + " : " + this.getCode();
  }

  @Override
  public GraphStrategy getStrategy()
  {
    return new ServerHierarchyStrategy(this);
  }

  @Override
  public MdEdgeDAOIF getMdEdgeDAO()
  {
    return this.getObjectEdge();
  }

  public void addToHierarchy(ServerGeoObjectType parentType, ServerGeoObjectType childType)
  {
    parentType.getObject().addChild(childType.getObject(), this.getDefinitionEdge());
  }

  public List<ServerGeoObjectType> getChildren(ServerGeoObjectType parent)
  {
    // We only need the code because we want to use the cached
    // ServerGeoObjectType instead of creating a new one
    StringBuilder statement = new StringBuilder();
    statement.append("SELECT code FROM (");
    statement.append(" SELECT EXPAND(out('" + this.getObject().getDefinitionEdge().getDbClassName() + "')) FROM :rid");
    statement.append(")");

    GraphQuery<String> query = new GraphQuery<String>(statement.toString());
    query.setParameter("rid", parent.getObject().getRID());

    return query.getResults().stream().map(code -> ServerGeoObjectType.get(code)).collect(Collectors.toList());
  }

  public List<ServerGeoObjectType> getRootNodes()
  {
    // We only need the code because we want to use the cached
    // ServerGeoObjectType instead of creating a new one
    MdVertexDAOIF mdVertex = MdVertexDAO.getMdVertexDAO(GeoObjectType.CLASS);

    StringBuilder statement = new StringBuilder();
    statement.append("SELECT code FROM " + mdVertex.getDBClassName());
    statement.append(" WHERE in('" + this.getObject().getDefinitionEdge().getDbClassName() + "').size() = :size");

    GraphQuery<String> query = new GraphQuery<String>(statement.toString());
    query.setParameter("size", 0);

    return query.getResults().stream().map(code -> ServerGeoObjectType.get(code)).collect(Collectors.toList());
  }

  public List<ServerGeoObjectType> getOrderedDescendants(ServerGeoObjectType type)
  {
    // We only need the code because we want to use the cached
    // ServerGeoObjectType instead of creating a new one
    StringBuilder statement = new StringBuilder();
    statement.append("SELECT code FROM (");
    statement.append(" TRAVERSE out('" + this.getObject().getDefinitionEdge().getDbClassName() + "') FROM :rid");
    statement.append(")");

    GraphQuery<String> query = new GraphQuery<String>(statement.toString());
    query.setParameter("rid", type.getObject().getRID());

    return query.getResults().stream().map(code -> ServerGeoObjectType.get(code)).collect(Collectors.toList());
  }

  public List<ServerGeoObjectType> getAncestors(ServerGeoObjectType childType)
  {
    // We only need the code because we want to use the cached
    // ServerGeoObjectType instead of creating a new one
    StringBuilder statement = new StringBuilder();
    statement.append("SELECT code FROM (");
    statement.append(" TRAVERSE in('" + this.getObject().getDefinitionEdge().getDbClassName() + "') FROM :rid");
    statement.append(")");

    GraphQuery<String> query = new GraphQuery<String>(statement.toString());
    query.setParameter("rid", this.getObject().getRID());

    return query.getResults().stream().map(code -> ServerGeoObjectType.get(code)).collect(Collectors.toList());
  }

  @Transaction
  public void removeFromHierarchy(ServerGeoObjectType parentType, ServerGeoObjectType childType, boolean migrateChildren)
  {
    MdEdgeDAOIF mdEdge = this.getDefinitionEdge();

    parentType.getObject().removeChild(childType.getObject(), mdEdge);

    if (migrateChildren)
    {
      this.getChildren(childType).forEach(newChild -> {
        parentType.getObject().addChild(newChild.getObject(), mdEdge).apply();
      });
    }
  }

  public static ServerHierarchyType get(String hierarchyTypeCode)
  {
    Optional<ServerHierarchyType> hierarchyType = ServiceFactory.getMetadataCache().getHierachyType(hierarchyTypeCode);

    if (!hierarchyType.isPresent())
    {
      MdClassDAOIF mdClassDAO = MdClassDAO.getMdClassDAO(HierarchicalRelationshipType.CLASS);
      Locale locale = Session.getCurrentLocale();

      net.geoprism.registry.DataNotFoundException ex = new net.geoprism.registry.DataNotFoundException();
      ex.setTypeLabel(mdClassDAO.getDisplayLabel(locale));
      ex.setDataIdentifier(hierarchyTypeCode);
      ex.setAttributeLabel(mdClassDAO.definesAttribute(HierarchicalRelationshipType.CODE).getDisplayLabel(locale));

      throw ex;
    }

    return hierarchyType.get();
  }

  public static ServerHierarchyType get(HierarchyType hierarchyType)
  {
    return get(hierarchyType.getCode());
  }

  public static ServerHierarchyType get(MdEdgeDAOIF objectEdge)
  {
    return get(HierarchicalRelationshipType.getByMdEdge(objectEdge));
  }

  public static ServerHierarchyType get(HierarchicalRelationshipType hierarchicalRelationship)
  {
    return ServiceFactory.getMetadataCache().getHierachyType(hierarchicalRelationship.getCode()).orElseThrow(() -> {
      throw new DataNotFoundException();
    });
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

}
