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
package net.geoprism.registry.service.request;

import java.util.List;

import org.commongeoregistry.adapter.metadata.AttributeType;

import com.runwaysdk.session.Request;
import com.runwaysdk.session.RequestType;
import com.runwaysdk.session.Session;

import net.geoprism.configuration.GeoprismProperties;
import net.geoprism.registry.OriginException;
import net.geoprism.registry.graph.ObjectClass;
import net.geoprism.registry.service.business.ObjectClassBusinessServiceIF;
import net.geoprism.registry.view.ObjectClassDTO;
import net.geoprism.registry.view.OrganizationGroup;

public abstract class ObjectClassService<T extends ObjectClass, D extends ObjectClassDTO> implements ObjectClassServiceIF<T, D>
{
  protected abstract ObjectClassBusinessServiceIF<T, D> getTypeService();

  /**
   * Creates a {@link BusinessType} from the given JSON.
   * 
   * @param sessionId
   * @param ptJSON
   *          JSON of the {@link BusinessType} to be created.
   * @return newly created {@link BusinessType}
   */
  @Override
  @Request(RequestType.SESSION)
  public D apply(String sessionId, D dto)
  {
    T type = this.getTypeService().apply(dto);

    // Refresh the users session
    ( (Session) Session.getCurrentSession() ).reloadPermissions();

    return this.getTypeService().toDTO(type, true, false);
  }

  @Override
  @Request(RequestType.SESSION)
  public void remove(String sessionId, String oid)
  {
    T type = this.getTypeService().get(oid);

    if (!type.getOrigin().equals(GeoprismProperties.getOrigin()))
    {
      throw new OriginException();
    }

    this.getTypeService().delete(type);

    // Refresh the users session
    ( (Session) Session.getCurrentSession() ).reloadPermissions();
  }

  @Override
  @Request(RequestType.SESSION)
  public D edit(String sessionId, String oid)
  {
    T type = this.getTypeService().get(oid);

    if (!type.getOrigin().equals(GeoprismProperties.getOrigin()))
    {
      throw new OriginException();
    }

    return this.getTypeService().toDTO(type, true, false);
  }

  @Override
  @Request(RequestType.SESSION)
  public void unlock(String sessionId, String oid)
  {
  }

  @Override
  @Request(RequestType.SESSION)
  public List<OrganizationGroup<D>> listByOrg(String sessionId)
  {
    return this.getTypeService().listByOrg();
  }

  @Override
  @Request(RequestType.SESSION)
  public List<D> getAll(String sessionId)
  {
    return this.getTypeService().getAll().stream().map(object -> {
      return this.getTypeService().toDTO(object);
    }).toList();
  }

  @Override
  @Request(RequestType.SESSION)
  public D get(String sessionId, String oid)
  {
    T type = this.getTypeService().get(oid);

    return this.getTypeService().toDTO(type, true, false);
  }

  /**
   * Adds an attribute to the given {@link T}.
   * 
   * @pre given {@link T} must already exist.
   * 
   * @param sessionId
   *
   * @param businessTypeCode
   *          string of the {@link T} to be updated.
   * @param attributeTypeJSON
   *          AttributeType to be added to the T
   * @return updated {@link T}
   */
  @Override
  @Request(RequestType.SESSION)
  public AttributeType createAttributeType(String sessionId, String businessTypeCode, AttributeType attributeType)
  {
    T type = this.getTypeService().getByCodeOrThrow(businessTypeCode);

    if (!type.getOrigin().equals(GeoprismProperties.getOrigin()))
    {
      throw new OriginException();
    }

    AttributeType attrType = this.getTypeService().createAttributeType(type, attributeType);

    return attrType;
  }

  /**
   * Updates an attribute in the given {@link T}.
   * 
   * @pre given {@link T} must already exist.
   * 
   * @param sessionId
   * @param businessTypeCode
   *          string of the {@link T} to be updated.
   * @param attributeTypeJSON
   *          AttributeType to be added to the T
   * @return updated {@link AttributeType}
   */
  @Override
  @Request(RequestType.SESSION)
  public AttributeType updateAttributeType(String sessionId, String businessTypeCode, AttributeType attributeType)
  {
    T type = this.getTypeService().getByCodeOrThrow(businessTypeCode);

    if (!type.getOrigin().equals(GeoprismProperties.getOrigin()))
    {
      throw new OriginException();
    }

    // ServiceFactory.getTPermissionService().enforceCanWrite(type.getOrganization().getCode(),
    // type, type.getIsPrivate());

    AttributeType attrType = this.getTypeService().updateAttributeType(type, attributeType);

    return attrType;
  }

  /**
   * Deletes an attribute from the given {@link T}.
   * 
   * @pre given {@link T} must already exist.
   * @pre given {@link T} must already exist.
   * 
   * @param sessionId
   * @param code
   *          string of the {@link T} to be updated.
   * @param attributeName
   *          Name of the attribute to be removed from the T
   * @return updated {@link T}
   */
  @Override
  @Request(RequestType.SESSION)
  public void removeAttributeType(String sessionId, String businessTypeCode, String attributeName)
  {
    T type = this.getTypeService().getByCodeOrThrow(businessTypeCode);

    if (!type.getOrigin().equals(GeoprismProperties.getOrigin()))
    {
      throw new OriginException();
    }

    this.getTypeService().removeAttributeType(type, attributeName);
  }

}
