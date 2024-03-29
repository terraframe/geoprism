/**
 * Copyright (c) 2023 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Geoprism(tm).
 *
 * Geoprism(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Geoprism(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Geoprism(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism.registry.model;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.commongeoregistry.adapter.dataaccess.LocalizedValue;
import org.commongeoregistry.adapter.metadata.OrganizationDTO;
import org.commongeoregistry.adapter.metadata.RegistryRole;

import com.google.gson.JsonObject;
import com.runwaysdk.business.BusinessFacade;
import com.runwaysdk.business.rbac.RoleDAO;
import com.runwaysdk.business.rbac.RoleDAOIF;
import com.runwaysdk.dataaccess.EntityDAOIF;
import com.runwaysdk.dataaccess.cache.ObjectCache;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.session.Session;
import com.runwaysdk.session.SessionIF;
import com.runwaysdk.system.Actor;
import com.runwaysdk.system.Roles;
import com.runwaysdk.system.SingleActor;

import net.geoprism.GeoprismUser;
import net.geoprism.registry.Organization;
import net.geoprism.registry.OrganizationContactInfo;
import net.geoprism.registry.OrganizationDisplayLabel;
import net.geoprism.registry.OrganizationQuery;
import net.geoprism.registry.OrganizationUser;
import net.geoprism.registry.OrganizationUtil;
import net.geoprism.registry.conversion.LocalizedValueConverter;
import net.geoprism.registry.graph.GraphOrganization;
import net.geoprism.registry.view.JsonSerializable;
import net.geoprism.registry.view.Page;

public class ServerOrganization implements JsonSerializable
{
  private Organization      organization;

  private GraphOrganization graphOrganization;

  public ServerOrganization()
  {
    this(new Organization(), new GraphOrganization());
  }

  public ServerOrganization(Organization organization, GraphOrganization graphOrganization)
  {
    this.organization = organization;
    this.graphOrganization = graphOrganization;
  }

  public GraphOrganization getGraphOrganization()
  {
    return graphOrganization;
  }

  public Organization getOrganization()
  {
    return organization;
  }

  public OrganizationDisplayLabel getDisplayLabel()
  {
    return this.organization.getDisplayLabel();
  }

  public String getOid()
  {
    return this.organization.getOid();
  }

  public String getCode()
  {
    return this.organization.getCode();
  }

  public Boolean getEnabled()
  {
    return this.organization.getEnabled() == null || this.organization.getEnabled();
  }

  public OrganizationContactInfo getContactInfo()
  {
    return this.organization.getContactInfo();
  }

  public void setCode(String code)
  {
    this.organization.setCode(code);
    this.graphOrganization.setCode(code);
  }

  public void setEnabled(Boolean enabled)
  {
    this.organization.setEnabled(enabled);
    this.graphOrganization.setEnabled(enabled);
  }

  public void setDisplayLabel(LocalizedValue label)
  {
    LocalizedValueConverter.populate(this.organization.getDisplayLabel(), label);
    LocalizedValueConverter.populate(this.graphOrganization, GraphOrganization.DISPLAYLABEL, label);
  }

  public void setContactInfo(LocalizedValue contactInfo)
  {
    LocalizedValueConverter.populate(this.organization.getContactInfo(), contactInfo);
    LocalizedValueConverter.populate(this.graphOrganization, GraphOrganization.CONTACTINFO, contactInfo);
  }

  public void lock()
  {
    this.organization.lock();
  }

  public void unlock()
  {
    this.organization.unlock();
  }

  public boolean isNew()
  {
    return this.organization.isNew();
  }
  
  public void appLock()
  {
    this.organization.appLock();
  }
  
  public void releaseAppLock()
  {
    this.organization.releaseAppLock();
  }
  
  /**
   * Creates a {@link RoleDAO} for this {@link Organization} and a Registry
   * Administrator {@link RoleDAO} for this {@link Organization}.
   */
  @Transaction
  public void apply()
  {
    this.organization.apply();

    if (this.graphOrganization.getOrganizationOid() == null || !this.graphOrganization.getOrganizationOid().equals(this.organization.getOid()))
    {
      this.graphOrganization.setOrganization(this.organization);
    }

    this.graphOrganization.apply();
  }

  /**
   * Removes the {@link RoleDAO}s for this {@link Organization} and Registry
   * Administrator for this {@link Organization}.
   */
  @Transaction
  public void delete()
  {
    this.graphOrganization.delete();
    this.organization.delete();
  }

  /**
   * Returns the role name for this {@link Organization}.
   * 
   * @return role name for this {@link Organization}.
   */
  public String getRoleName()
  {
    return RegistryRole.Type.getRootOrgRoleName(this.getCode());
  }

  /**
   * Returns the {@link Roles} for this {@link Organization}.
   * 
   * @return the {@link Roles} for this {@link Organization}.
   */
  public Roles getRole()
  {
    return Roles.findRoleByName(this.getRoleName());
  }

  /**
   * Returns the {@link RoleDAOIF} for the {@link Organization} with the given
   * code.
   * 
   * @param organizationCode
   * 
   * @return the {@link RoleDAOIF} for the {@link Organization} with the given
   *         code.
   */
  public static RoleDAOIF getRole(String organizationCode)
  {
    return RoleDAO.findRole(RegistryRole.Type.getRootOrgRoleName(organizationCode));
  }

  /**
   * Returns the {@link RoleDAOIF} name for the Registry Administrator for this
   * {@link Organization}.
   * 
   * @return the {@link RoleDAOIF} name for the Registry Administrator for this
   *         {@link Organization}.
   */
  public String getRegistryAdminRoleName()
  {
    return RegistryRole.Type.getRA_RoleName(this.getCode());
  }

  /**
   * Returns the Registry Administrator {@link Roles} for this
   * {@link Organization}.
   * 
   * @return the Registry Administrator {@link Roles} for this
   *         {@link Organization}.
   */
  public Roles getRegistryAdminiRole()
  {
    return Roles.findRoleByName(this.getRegistryAdminRoleName());
  }

  /**
   * Returns the Registry Administrator {@link Roles} for this
   * {@link Organization}.
   * 
   * @param organizationCode
   * 
   * @return the Registry Administrator {@link Roles} for this
   *         {@link Organization}.
   */
  public static Roles getRegistryAdminiRole(String organizationCode)
  {
    return Roles.findRoleByName(RegistryRole.Type.getRA_RoleName(organizationCode));
  }

  public void addChild(ServerOrganization child)
  {
    this.graphOrganization.addChild(child.graphOrganization);
  }

  public void removeChild(ServerOrganization child)
  {
    this.graphOrganization.removeChild(child.graphOrganization);
  }

  public void addParent(ServerOrganization parent)
  {
    this.graphOrganization.addParent(parent.graphOrganization);
  }

  public void removeParent(ServerOrganization parent)
  {
    this.graphOrganization.removeParent(parent.graphOrganization);
  }

  @Transaction
  public void removeAllParents()
  {
    ServerOrganization parent = this.getParent();

    if (parent != null)
    {
      this.removeParent(parent);
    }

  }

  public ServerOrganization getParent()
  {
    GraphOrganization parent = this.graphOrganization.getParent();

    if (parent != null)
    {
      return new ServerOrganization(parent.getOrganization(), parent);
    }

    return null;
  }

  public Page<ServerOrganization> getChildren()
  {
    return this.getChildren(20, 1);
  }

  public Page<ServerOrganization> getChildren(Integer pageSize, Integer pageNumber)
  {
    Integer count = this.graphOrganization.getCount();

    List<GraphOrganization> children = this.graphOrganization.getChildren(pageSize, pageNumber);

    List<ServerOrganization> results = children.stream().map(vertex -> {
      return new ServerOrganization(vertex.getOrganization(), vertex);
    }).collect(Collectors.toList());

    return new Page<ServerOrganization>(count, pageNumber, pageSize, results);
  }

  public List<ServerOrganization> getAncestors(String code)
  {
    List<GraphOrganization> ancestors = this.graphOrganization.getAncestors(code);

    List<ServerOrganization> results = ancestors.stream().map(vertex -> {
      return new ServerOrganization(vertex.getOrganization(), vertex);
    }).collect(Collectors.toList());

    return results;
  }

  public GraphNode<ServerOrganization> getAncestorTree(String code, Integer pageSize)
  {
    List<GraphOrganization> ancestors = this.graphOrganization.getAncestors(code);
    Integer count = this.graphOrganization.getCount();

    GraphNode<ServerOrganization> prev = null;

    for (GraphOrganization ancestor : ancestors)
    {
      List<GraphOrganization> results = ancestor.getChildren(pageSize, 1);

      List<GraphNode<ServerOrganization>> transform = results.stream().map(r -> {
        ServerOrganization object = new ServerOrganization(r.getOrganization(), r);
        return new GraphNode<ServerOrganization>(object);
      }).collect(Collectors.toList());

      if (prev != null)
      {
        int index = transform.indexOf(prev);

        if (index != -1)
        {
          transform.set(index, prev);
        }
        else
        {
          transform.add(prev);
        }
      }

      Page<GraphNode<ServerOrganization>> page = new Page<GraphNode<ServerOrganization>>(count, 1, pageSize, transform);

      GraphNode<ServerOrganization> node = new GraphNode<ServerOrganization>();
      node.setObject(new ServerOrganization(ancestor.getOrganization(), ancestor));
      node.setChildren(page);

      prev = node;
    }

    return prev;
  }

  public void move(ServerOrganization newParent)
  {
    this.graphOrganization.move(newParent.graphOrganization);
  }

  public OrganizationDTO toDTO()
  {
    LocalizedValue label = LocalizedValueConverter.convertNoAutoCoalesce(this.getDisplayLabel());
    LocalizedValue info = LocalizedValueConverter.convertNoAutoCoalesce(this.getContactInfo());
    ServerOrganization parent = this.getParent();

    OrganizationDTO dto = new OrganizationDTO(this.getCode(), label, info);
    dto.setEnabled(this.getEnabled());

    if (parent != null)
    {
      String parentCode = parent.getCode();
      LocalizedValue parentLabel = LocalizedValueConverter.convertNoAutoCoalesce(parent.getDisplayLabel());

      dto.setParentCode(parentCode);
      dto.setParentLabel(parentLabel);
    }

    return dto;
  }

  @Override
  public JsonObject toJSON()
  {
    return this.toDTO().toJSON();
  }

  /**
   * If the given actor OID is a role that represents an {@link Organization},
   * then return the corresponding {@link Organization} code or NULL otherwise.
   * 
   * Precondition: Assumes that the actor id, if it is associated with an
   * organization, is the root organization role and not a sub-role of the root
   * organization.
   * 
   * @param actorOid
   *          OID of an actor.
   * 
   * @return the corresponding {@link Organization} code or NULL otherwise.
   */
  public static String getRootOrganizationCode(String actorOid)
  {
    ServerOrganization organization = getRootOrganization(actorOid);

    if (organization == null)
    {
      return null;
    }
    else
    {
      return organization.getCode();
    }
  }

  /**
   * If the given actor OID is a role that represents an {@link Organization},
   * then return the corresponding {@link Organization} or NULL otherwise.
   * 
   * Precondition: Assumes that the actor id, if it is associated with an
   * organization, is the root organization role and not a sub-role of the root
   * organization.
   * 
   * @param actorOid
   *          OID of an actor.
   * 
   * @return the corresponding {@link Organization} or NULL otherwise.
   */
  public static ServerOrganization getRootOrganization(String actorOid)
  {
    Actor actor = null;

    try
    {
      actor = Actor.get(actorOid);
    }
    catch (com.runwaysdk.dataaccess.cache.DataNotFoundException e)
    {
      return null;
    }

    // If the actor is not a role, then it does not represent an organization
    if (! ( actor instanceof Roles ))
    {
      return null;
    }

    Roles role = (Roles) actor;
    String roleName = role.getRoleName();

    // If the role name does not contain the organization root name, then it is
    // not a role that pertains to an organization.
    if (roleName.indexOf(RegistryRole.Type.REGISTRY_ROOT_ORG_ROLE) <= -1)
    {
      return null;
    }

    String organizationCode = roleName.substring(RegistryRole.Type.REGISTRY_ROOT_ORG_ROLE.length() + 1, roleName.length());

    try
    {
      return ServerOrganization.getByCode(organizationCode);
    }
    catch (com.runwaysdk.dataaccess.cache.DataNotFoundException e)
    {
      return null;
    }
  }

  public static ServerOrganization get(Organization organization)
  {
    if (organization != null)
    {
      GraphOrganization graphOrganization = GraphOrganization.get(organization);

      return new ServerOrganization(organization, graphOrganization);
    }

    return null;
  }

  public static ServerOrganization getByGraphId(String graphId)
  {
    if (!StringUtils.isBlank(graphId))
    {
      GraphOrganization graphOrganization = GraphOrganization.get(graphId);
      Organization organization = Organization.getByCode(graphOrganization.getCode());

      return new ServerOrganization(organization, graphOrganization);
    }

    return null;
  }

  public static List<ServerOrganization> getSortedOrganizations()
  {
    OrganizationQuery query = new OrganizationQuery(new QueryFactory());
    query.ORDER_BY_ASC(query.getDisplayLabel().localize());

    return OrganizationUtil.sort(query.getIterator().getAll().stream().map(org -> ServerOrganization.get(org)).collect(Collectors.toList()));
  }

  public static List<ServerOrganization> getOrganizations()
  {
    OrganizationQuery query = new OrganizationQuery(new QueryFactory());
    query.ORDER_BY_ASC(query.getDisplayLabel().localize());

    return query.getIterator().getAll().stream().map(org -> ServerOrganization.get(org)).collect(Collectors.toList());
  }

  /**
   * Returns all of the organizations as {@link Organization} objects from the
   * cache unsorted instead of fetching from the database.
   * 
   * @return
   */
  public static List<ServerOrganization> getOrganizationsFromCache()
  {
    // For performance, get all of the universals defined
    List<? extends EntityDAOIF> organizationDAOs = ObjectCache.getCachedEntityDAOs(Organization.CLASS);

    List<ServerOrganization> organizationList = new LinkedList<ServerOrganization>();

    for (EntityDAOIF entityDAOIF : organizationDAOs)
    {
      organizationList.add(ServerOrganization.get((Organization) BusinessFacade.get(entityDAOIF)));
    }

    return organizationList;
  }

  public static List<ServerOrganization> getUserOrganizations()
  {
    return Organization.getUserOrganizations().stream().map(org -> ServerOrganization.get(org)).collect(Collectors.toList());
  }

  /**
   * @param org
   * @return If the current user is a member of the given organization
   */
  public static boolean isMember(ServerOrganization org)
  {
    final SessionIF session = Session.getCurrentSession();

    if (session != null)
    {
      final SingleActor user = GeoprismUser.getCurrentUser();

      return OrganizationUser.exists(org.getOrganization(), user);
    }

    return false;
  }

  /**
   * Returns the [@link Organization} with the given code
   * 
   * @return
   */
  public static ServerOrganization getByCode(String code)
  {
    return getByCode(code, true);
  }

  public static ServerOrganization getByCode(String code, boolean throwException)
  {
    return ServerOrganization.get(Organization.getByCode(code, throwException));
  }

  public static List<ServerOrganization> getRoots()
  {
    List<GraphOrganization> children = GraphOrganization.getRoots();

    List<ServerOrganization> results = children.stream().map(vertex -> {
      return new ServerOrganization(vertex.getOrganization(), vertex);
    }).collect(Collectors.toList());

    return results;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (obj instanceof ServerOrganization)
    {
      return ( (ServerOrganization) obj ).getCode().equals(this.getCode());
    }

    return super.equals(obj);
  }

}
