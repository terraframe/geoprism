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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.runwaysdk.session.Request;
import com.runwaysdk.session.RequestType;
import com.runwaysdk.session.Session;

import net.geoprism.configuration.GeoprismProperties;
import net.geoprism.registry.BusinessEdgeType;
import net.geoprism.registry.BusinessType;
import net.geoprism.registry.JsonCollectors;
import net.geoprism.registry.OriginException;
import net.geoprism.registry.service.business.BusinessEdgeTypeBusinessServiceIF;
import net.geoprism.registry.service.business.BusinessTypeBusinessServiceIF;

@Service
public class BusinessTypeService
{
  @Autowired
  private BusinessTypeBusinessServiceIF     typeService;

  @Autowired
  private BusinessEdgeTypeBusinessServiceIF edgeService;

  /**
   * Creates a {@link BusinessType} from the given JSON.
   * 
   * @param sessionId
   * @param ptJSON
   *          JSON of the {@link BusinessType} to be created.
   * @return newly created {@link BusinessType}
   */
  @Request(RequestType.SESSION)
  public JsonObject apply(String sessionId, String ptJSON)
  {
    JsonObject json = JsonParser.parseString(ptJSON).getAsJsonObject();
    BusinessType type = this.typeService.apply(json.get("type").getAsJsonObject());

    // Refresh the users session
    ( (Session) Session.getCurrentSession() ).reloadPermissions();

    return this.typeService.toJSON(type);
  }

  @Request(RequestType.SESSION)
  public JsonArray listByOrg(String sessionId)
  {
    return this.typeService.listByOrg();
  }

  @Request(RequestType.SESSION)
  public JsonArray getAll(String sessionId)
  {
    return this.typeService.getAll().stream().map(object -> {
      return this.typeService.toJSON(object);
    }).collect(JsonCollectors.toJsonArray());
  }

  @Request(RequestType.SESSION)
  public JsonObject get(String sessionId, String oid)
  {
    BusinessType type = BusinessType.get(oid);
    return this.typeService.toJSON(type, true, false);
  }

  @Request(RequestType.SESSION)
  public void remove(String sessionId, String oid)
  {
    BusinessType type = BusinessType.get(oid);
    
    if (!type.getOrigin().equals(GeoprismProperties.getOrigin()))
    {
      throw new OriginException();
    }

    this.typeService.delete(type);

    // Refresh the users session
    ( (Session) Session.getCurrentSession() ).reloadPermissions();
  }

  @Request(RequestType.SESSION)
  public JsonObject edit(String sessionId, String oid)
  {
    BusinessType type = BusinessType.get(oid);
    
    if (!type.getOrigin().equals(GeoprismProperties.getOrigin()))
    {
      throw new OriginException();
    }

    type.lock();

    return this.typeService.toJSON(type, true, false);
  }

  @Request(RequestType.SESSION)
  public void unlock(String sessionId, String oid)
  {
    BusinessType type = BusinessType.get(oid);
    type.unlock();
  }

  /**
   * Adds an attribute to the given {@link BusinessType}.
   * 
   * @pre given {@link BusinessType} must already exist.
   * 
   * @param sessionId
   *
   * @param businessTypeCode
   *          string of the {@link BusinessType} to be updated.
   * @param attributeTypeJSON
   *          AttributeType to be added to the BusinessType
   * @return updated {@link BusinessType}
   */
  @Request(RequestType.SESSION)
  public AttributeType createAttributeType(String sessionId, String businessTypeCode, JsonObject attributeType)
  {
    BusinessType type = this.typeService.getByCode(businessTypeCode);
    
    if (!type.getOrigin().equals(GeoprismProperties.getOrigin()))
    {
      throw new OriginException();
    }

    // ServiceFactory.getBusinessTypePermissionService().enforceCanWrite(type.getOrganization().getCode(),
    // type, type.getIsPrivate());

    AttributeType attrType = this.typeService.createAttributeType(type, attributeType);

    return attrType;
  }

  /**
   * Updates an attribute in the given {@link BusinessType}.
   * 
   * @pre given {@link BusinessType} must already exist.
   * 
   * @param sessionId
   * @param businessTypeCode
   *          string of the {@link BusinessType} to be updated.
   * @param attributeTypeJSON
   *          AttributeType to be added to the BusinessType
   * @return updated {@link AttributeType}
   */
  @Request(RequestType.SESSION)
  public AttributeType updateAttributeType(String sessionId, String businessTypeCode, JsonObject attributeType)
  {
    BusinessType type = this.typeService.getByCode(businessTypeCode);

    if (!type.getOrigin().equals(GeoprismProperties.getOrigin()))
    {
      throw new OriginException();
    }

    // ServiceFactory.getBusinessTypePermissionService().enforceCanWrite(type.getOrganization().getCode(),
    // type, type.getIsPrivate());

    AttributeType attrType = this.typeService.updateAttributeType(type, attributeType);

    return attrType;
  }

  /**
   * Deletes an attribute from the given {@link BusinessType}.
   * 
   * @pre given {@link BusinessType} must already exist.
   * @pre given {@link BusinessType} must already exist.
   * 
   * @param sessionId
   * @param code
   *          string of the {@link BusinessType} to be updated.
   * @param attributeName
   *          Name of the attribute to be removed from the BusinessType
   * @return updated {@link BusinessType}
   */
  @Request(RequestType.SESSION)
  public void removeAttributeType(String sessionId, String businessTypeCode, String attributeName)
  {
    BusinessType type = this.typeService.getByCode(businessTypeCode);

    if (!type.getOrigin().equals(GeoprismProperties.getOrigin()))
    {
      throw new OriginException();
    }

    // ServiceFactory.getBusinessTypePermissionService().enforceCanWrite(type.getOrganization().getCode(),
    // type, type.getIsPrivate());

    this.typeService.removeAttribute(type, attributeName);
  }

  @Request(RequestType.SESSION)
  public JsonObject data(String sessionId, String businessTypeCode, String json)
  {
    BusinessType type = this.typeService.getByCode(businessTypeCode);

    return this.typeService.data(type, JsonParser.parseString(json).getAsJsonObject()).toJSON();
  }

  @Request(RequestType.SESSION)
  public JsonArray getEdgeTypes(String sessionId, String businessTypeCode)
  {
    BusinessType type = this.typeService.getByCode(businessTypeCode);
    List<BusinessEdgeType> edgeTypes = this.typeService.getEdgeTypes(type);

    return edgeTypes.stream().map(object -> this.edgeService.toJSON(object)).collect(JsonCollectors.toJsonArray());
  }

}
