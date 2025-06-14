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

import java.util.Date;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.commongeoregistry.adapter.constants.DefaultAttribute;
import org.commongeoregistry.adapter.dataaccess.ChildTreeNode;
import org.commongeoregistry.adapter.dataaccess.GeoObject;
import org.commongeoregistry.adapter.dataaccess.GeoObjectOverTime;
import org.commongeoregistry.adapter.dataaccess.LocalizedValue;
import org.commongeoregistry.adapter.dataaccess.ParentTreeNode;
import org.commongeoregistry.adapter.metadata.CustomSerializer;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.localization.LocalizationFacade;
import com.runwaysdk.session.Request;
import com.runwaysdk.session.RequestType;
import com.runwaysdk.session.Session;

import net.geoprism.registry.lpg.LocaleSerializer;
import net.geoprism.registry.model.ServerChildTreeNode;
import net.geoprism.registry.model.ServerGeoObjectIF;
import net.geoprism.registry.model.ServerGeoObjectType;
import net.geoprism.registry.model.ServerHierarchyType;
import net.geoprism.registry.service.business.GeoObjectBusinessServiceIF;
import net.geoprism.registry.service.business.HierarchyTypeBusinessServiceIF;
import net.geoprism.registry.service.business.ServiceFactory;
import net.geoprism.registry.service.permission.GeoObjectPermissionServiceIF;
import net.geoprism.registry.view.ServerParentTreeNodeOverTime;

@Service
public class GeoObjectService implements GeoObjectServiceIF
{
  @Autowired
  protected GeoObjectBusinessServiceIF     service;

  @Autowired
  protected HierarchyTypeBusinessServiceIF hierarchyService;

  @Autowired
  protected GeoObjectPermissionServiceIF   permissionService;

  @Override
  @Request(RequestType.SESSION)
  public GeoObject getGeoObject(String sessionId, String uid, String geoObjectTypeCode, Date date)
  {
    ServerGeoObjectIF object = this.service.getGeoObject(uid, geoObjectTypeCode);

    this.permissionService.enforceCanRead(object.getType().getOrganization().getCode(), object.getType());

    ServerGeoObjectType type = object.getType();

    GeoObject geoObject = service.toGeoObject(object, date);
    geoObject.setWritable(this.permissionService.canCreateCR(type.getOrganization().getCode(), type));

    return geoObject;
  }

  @Override
  @Request(RequestType.SESSION)
  public GeoObject getGeoObjectByCode(String sessionId, String code, String typeCode, Date date)
  {
    ServerGeoObjectIF object = service.getGeoObjectByCode(code, typeCode, true);

    this.permissionService.enforceCanRead(object.getType().getOrganization().getCode(), object.getType());

    return service.toGeoObject(object, date);
  }

  @Override
  @Request(RequestType.SESSION)
  public GeoObject createGeoObject(String sessionId, String jGeoObj, Date startDate, Date endDate)
  {
    GeoObject geoObject = GeoObject.fromJSON(ServiceFactory.getAdapter(), jGeoObj);

    ServerGeoObjectIF object = service.apply(geoObject, startDate, endDate, true, false);

    return service.toGeoObject(object, startDate);
  }

  @Override
  @Request(RequestType.SESSION)
  public GeoObject updateGeoObject(String sessionId, String jGeoObj, Date startDate, Date endDate)
  {
    GeoObject geoObject = GeoObject.fromJSON(ServiceFactory.getAdapter(), jGeoObj);

    ServerGeoObjectIF object = service.apply(geoObject, startDate, endDate, false, false);

    return service.toGeoObject(object, startDate);
  }

  @Override
  @Request(RequestType.SESSION)
  public String getGeoObjectBounds(String sessionId, GeoObject geoObject)
  {
    return this.service.getGeoObject(geoObject).bbox(null);
  }

  @Override
  @Request(RequestType.SESSION)
  public String getGeoObjectBoundsAtDate(String sessionId, GeoObject geoObject, Date date)
  {
    return this.service.getGeoObject(geoObject).bbox(date);
  }

  @Override
  @Request(RequestType.SESSION)
  public GeoObjectOverTime getGeoObjectOverTimeByCode(String sessionId, String code, String typeCode)
  {
    ServerGeoObjectIF goServer = service.getGeoObjectByCode(code, typeCode, true);

    return service.toGeoObjectOverTime(goServer);
  }

  @Override
  @Request(RequestType.SESSION)
  public GeoObjectOverTime updateGeoObjectOverTime(String sessionId, String jGeoObj)
  {
    GeoObjectOverTime goTime = GeoObjectOverTime.fromJSON(ServiceFactory.getAdapter(), jGeoObj);
    ServerGeoObjectType type = ServerGeoObjectType.get(goTime.getType().getCode());

    this.permissionService.enforceCanWrite(goTime.getType().getOrganizationCode(), type);

    ServerGeoObjectIF object = service.apply(goTime, false, false);

    return service.toGeoObjectOverTime(object);
  }

  @Override
  @Request(RequestType.SESSION)
  public GeoObjectOverTime createGeoObjectOverTime(String sessionId, String jGeoObj)
  {
    GeoObjectOverTime goTime = GeoObjectOverTime.fromJSON(ServiceFactory.getAdapter(), jGeoObj);
    ServerGeoObjectType type = ServerGeoObjectType.get(goTime.getType().getCode());

    this.permissionService.enforceCanCreate(type.getOrganizationCode(), type);

    ServerGeoObjectIF object = service.apply(goTime, true, false);

    return service.toGeoObjectOverTime(object);
  }

  @Override
  @Request(RequestType.SESSION)
  public GeoObjectOverTime getGeoObjectOverTime(String sessionId, String id, String typeCode)
  {
    ServerGeoObjectIF object = this.service.getGeoObject(id, typeCode);

    this.permissionService.enforceCanRead(object.getType().getOrganization().getCode(), object.getType());

    return service.toGeoObjectOverTime(object);
  }

  @Override
  @Request(RequestType.SESSION)
  public ChildTreeNode getChildGeoObjects(String sessionId, String parentCode, String parentGeoObjectTypeCode, String hierarchyCode, String[] childrenTypes, Boolean recursive, Date date)
  {
    ServerGeoObjectIF object = this.service.getGeoObjectByCode(parentCode, parentGeoObjectTypeCode, true);

    if (date != null)
    {
      object.setDate(date);
    }

    ServerHierarchyType sht = null;
    if (!StringUtils.isEmpty(hierarchyCode))
    {
      sht = ServerHierarchyType.get(hierarchyCode);
    }

    ServerChildTreeNode node = service.getChildGeoObjects(object, sht, childrenTypes, recursive, date);

    return node.toNode(true);
  }

  @Override
  @Request(RequestType.SESSION)
  public ParentTreeNode getParentGeoObjects(String sessionId, String childCode, String childGeoObjectTypeCode, String hierarchyCode, String[] parentTypes, boolean recursive, boolean includeInherited, Date date)
  {
    ServerGeoObjectIF object = this.service.getGeoObjectByCode(childCode, childGeoObjectTypeCode, true);

    if (date != null)
    {
      object.setDate(date);
    }

    ServerHierarchyType sht = null;
    if (!StringUtils.isEmpty(hierarchyCode))
    {
      sht = ServerHierarchyType.get(hierarchyCode);
    }

    return service.getParentGeoObjects(object, sht, parentTypes, recursive, includeInherited, date).toNode(true);
  }

  @Override
  @Request(RequestType.SESSION)
  public GeoObject newGeoObjectInstance(String sessionId, String geoObjectTypeCode)
  {
    return ServiceFactory.getAdapter().newGeoObjectInstance(geoObjectTypeCode);
  }

  @Override
  @Request(RequestType.SESSION)
  public String newGeoObjectInstance2(String sessionId, String geoObjectTypeCode)
  {
    CustomSerializer serializer = new LocaleSerializer(Session.getCurrentLocale());
    JSONObject joResp = new JSONObject();

    /**
     * Create a new GeoObject
     */
    GeoObject go = ServiceFactory.getAdapter().newGeoObjectInstance(geoObjectTypeCode);

    /**
     * Add all locales so the front-end knows what are available.
     */
    LocalizedValue label = new LocalizedValue("");
    label.setValue(MdAttributeLocalInfo.DEFAULT_LOCALE, "");

    Set<Locale> locales = LocalizationFacade.getInstalledLocales();

    for (Locale locale : locales)
    {
      label.setValue(locale, "");
    }

    go.setValue(DefaultAttribute.DISPLAY_LABEL.getName(), label);

    /**
     * Serialize the GeoObject and add it to the response
     */
    JsonObject jsonObject = go.toJSON(serializer);
    joResp.put("geoObject", new JSONObject(jsonObject.toString()));

    JsonArray hierarchies = this.hierarchyService.getHierarchiesForType(go.getType().getCode(), true);

    joResp.put("hierarchies", new JSONArray(hierarchies.toString()));

    return joResp.toString();
  }

  @Override
  @Request(RequestType.SESSION)
  public String newGeoObjectInstanceOverTime(String sessionId, String typeCode)
  {
    final ServerGeoObjectType type = ServerGeoObjectType.get(typeCode);

    ServerGeoObjectIF go = service.newInstance(type);

    go.setInvalid(false);

    final GeoObjectOverTime goot = service.toGeoObjectOverTime(go);
    ServerParentTreeNodeOverTime pot = service.getParentsOverTime(go, null, true, true);

    this.hierarchyService.filterHierarchiesFromPermissions(type, pot);

    /**
     * Serialize the GeoObject and add it to the response
     */
    JsonObject response = new JsonObject();

    response.add("geoObject", goot.toJSON());
    response.add("hierarchies", pot.toJSON());

    return response.toString();
  }

  @Override
  @Request(RequestType.SESSION)
  public JsonObject getAll(String sessionId, String typeCode, String hierarchyCode, Date dUpdatedSince, Boolean includeLevel, String format, String externalSystemId, Integer pageNumber, Integer pageSize)
  {
    return this.service.getAll(typeCode, hierarchyCode, dUpdatedSince, includeLevel, format, externalSystemId, pageNumber, pageSize);
  }

  @Override
  @Request(RequestType.SESSION)
  public JsonObject doesGeoObjectExistAtRange(String sessionId, Date startDate, Date endDate, String typeCode, String code)
  {
    return this.service.doesGeoObjectExistAtRange(startDate, endDate, typeCode, code);
  }

  @Override
  @Request(RequestType.SESSION)
  public JsonObject hasDuplicateLabel(String sessionId, Date date, String typeCode, String code, String label)
  {
    return this.service.hasDuplicateLabel(date, typeCode, code, label);
  }

  @Override
  @Request(RequestType.SESSION)
  public ParentTreeNode addChild(String sessionId, String parentCode, String parentTypeCode, String childCode, String childTypeCode, String hierarchyCode, Date startDate, Date endDate)
  {
    return this.service.addChild(parentCode, parentTypeCode, childCode, childTypeCode, hierarchyCode, startDate, endDate);
  }

  @Override
  @Request(RequestType.SESSION)
  public void removeChild(String sessionId, String parentCode, String parentTypeCode, String childCode, String childTypeCode, String hierarchyCode, Date startDate, Date endDate)
  {
    this.service.removeChild(parentCode, parentTypeCode, childCode, childTypeCode, hierarchyCode, startDate, endDate);
  }

  @Override
  @Request(RequestType.SESSION)
  public JsonArray getBusinessObjects(String sessionId, String typeCode, String code, String edgeTypeCode, String direction)
  {
    return this.service.getBusinessObjects(typeCode, code, edgeTypeCode, direction);
  }

}
