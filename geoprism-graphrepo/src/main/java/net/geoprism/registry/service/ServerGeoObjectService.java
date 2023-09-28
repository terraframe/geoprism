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
package net.geoprism.registry.service;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.AbstractAction;

import org.apache.commons.lang3.StringUtils;
import org.commongeoregistry.adapter.constants.CGRAdapterProperties;
import org.commongeoregistry.adapter.constants.DefaultAttribute;
import org.commongeoregistry.adapter.dataaccess.GeoObject;
import org.commongeoregistry.adapter.dataaccess.GeoObjectOverTime;
import org.commongeoregistry.adapter.dataaccess.ParentTreeNode;
import org.springframework.stereotype.Component;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.runwaysdk.business.graph.VertexObject;
import com.runwaysdk.dataaccess.DuplicateDataException;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.session.Request;
import com.runwaysdk.session.RequestType;
import com.runwaysdk.session.Session;

import net.geoprism.graph.service.LocaleSerializer;
import net.geoprism.graphrepo.permission.AllowAllGeoObjectPermissionService;
import net.geoprism.graphrepo.permission.GeoObjectPermissionServiceIF;
import net.geoprism.registry.BusinessType;
import net.geoprism.registry.DataNotFoundException;
import net.geoprism.registry.DuplicateGeoObjectCodeException;
import net.geoprism.registry.DuplicateGeoObjectException;
import net.geoprism.registry.DuplicateGeoObjectMultipleException;
import net.geoprism.registry.conversion.RegistryLocalizedValueConverter;
import net.geoprism.registry.conversion.ServerGeoObjectStrategyIF;
import net.geoprism.registry.conversion.VertexGeoObjectStrategy;
import net.geoprism.registry.etl.export.GeoObjectExportFormat;
import net.geoprism.registry.etl.export.GeoObjectJsonExporter;
import net.geoprism.registry.model.BusinessObject;
import net.geoprism.registry.model.GeoObjectMetadata;
import net.geoprism.registry.model.ServerGeoObjectIF;
import net.geoprism.registry.model.ServerGeoObjectType;
import net.geoprism.registry.model.ServerHierarchyType;
import net.geoprism.registry.model.ServerParentTreeNode;
import net.geoprism.registry.model.graph.VertexServerGeoObject;
import net.geoprism.registry.query.ServerGeoObjectQuery;
import net.geoprism.registry.query.graph.VertexGeoObjectQuery;
import net.geoprism.registry.view.GeoObjectSplitView;

@Component
public class ServerGeoObjectService extends RegistryLocalizedValueConverter implements ServerGeoObjectServiceIF
{
  protected GeoObjectPermissionServiceIF permissionService;

  public ServerGeoObjectService()
  {
    this(new AllowAllGeoObjectPermissionService());
  }

  public ServerGeoObjectService(GeoObjectPermissionServiceIF permissionService)
  {
    this.permissionService = permissionService;
  }

  @Request(RequestType.SESSION)
  public JsonObject getAll(String sessionId, String gotCode, String hierarchyCode, Date since, Boolean includeLevel, String format, String externalSystemId, Integer pageNumber, Integer pageSize)
  {
    GeoObjectExportFormat goef = null;
    if (format != null && format.length() > 0)
    {
      goef = GeoObjectExportFormat.valueOf(format);
    }

    GeoObjectJsonExporter exporter = new GeoObjectJsonExporter(gotCode, hierarchyCode, since, includeLevel, goef, null, pageSize, pageNumber);

    try
    {
      return exporter.export();
    }
    catch (IOException e)
    {
      throw new RuntimeException(e);
    }
  }

  @Request(RequestType.SESSION)
  public ParentTreeNode addChild(String sessionId, String parentCode, String parentGeoObjectTypeCode, String childCode, String childGeoObjectTypeCode, String hierarchyCode, Date startDate, Date endDate)
  {
    ServerGeoObjectIF parent = this.getGeoObjectByCode(parentCode, parentGeoObjectTypeCode, true);
    ServerGeoObjectIF child = this.getGeoObjectByCode(childCode, childGeoObjectTypeCode, true);
    ServerHierarchyType ht = ServerHierarchyType.get(hierarchyCode);

    ServiceFactory.getGeoObjectRelationshipPermissionService().enforceCanAddChild(ht.getOrganization().getCode(), parent.getType(), child.getType());

    return parent.addChild(child, ht, startDate, endDate).toNode(false);
  }

  @Request(RequestType.SESSION)
  public void removeChild(String sessionId, String parentCode, String parentGeoObjectTypeCode, String childCode, String childGeoObjectTypeCode, String hierarchyCode, Date startDate, Date endDate)
  {
    ServerGeoObjectIF parent = this.getGeoObjectByCode(parentCode, parentGeoObjectTypeCode, true);
    ServerGeoObjectIF child = this.getGeoObjectByCode(childCode, childGeoObjectTypeCode, true);
    ServerHierarchyType ht = ServerHierarchyType.get(hierarchyCode);

    ServiceFactory.getGeoObjectRelationshipPermissionService().enforceCanRemoveChild(ht.getOrganization().getCode(), parent.getType(), child.getType());

    parent.removeChild(child, hierarchyCode, startDate, endDate);
  }

  @Transaction
  public ServerGeoObjectIF apply(GeoObject object, Date startDate, Date endDate, boolean isNew, boolean isImport)
  {
    ServerGeoObjectType type = ServerGeoObjectType.get(object.getType());
    ServerGeoObjectStrategyIF strategy = this.getStrategy(type);

    if (isNew)
    {
      permissionService.enforceCanCreate(type.getOrganization().getCode(), type);
    }
    else
    {
      permissionService.enforceCanWrite(type.getOrganization().getCode(), type);
    }

    ServerGeoObjectIF geoObject = strategy.constructFromGeoObject(object, isNew);
    geoObject.setDate(startDate);

    if (!isNew)
    {
      geoObject.lock();
    }

    geoObject.populate(object, startDate, endDate);

    try
    {
      geoObject.apply(isImport);

      // Return the refreshed copy of the geoObject
      return this.build(type, geoObject.getRunwayId());
    }
    catch (DuplicateDataException e)
    {
      handleDuplicateDataException(type, e);

      throw e;
    }
  }
  
  public void handleDuplicateDataException(ServerGeoObjectType type, DuplicateDataException e)
  {
    if (e.getAttributes().size() == 0)
    {
      throw e;
    }
    else if (e.getAttributes().size() == 1)
    {
      MdAttributeDAOIF attr = e.getAttributes().get(0);

      if (VertexServerGeoObject.isCodeAttribute(attr))
      {
        DuplicateGeoObjectCodeException ex = new DuplicateGeoObjectCodeException();
        ex.setGeoObjectType(VertexServerGeoObject.findTypeLabelFromGeoObjectCode(e.getValues().get(0), type));
        ex.setValue(e.getValues().get(0));
        throw ex;
      }
      else
      {
        DuplicateGeoObjectException ex = new DuplicateGeoObjectException();
        ex.setGeoObjectType(type.getLabel().getValue());
        ex.setValue(e.getValues().get(0));
        ex.setAttributeName(VertexServerGeoObject.getAttributeLabel(type, attr));
        throw ex;
      }
    }
    else
    {
      List<String> attrLabels = new ArrayList<String>();

      for (MdAttributeDAOIF attr : e.getAttributes())
      {
        attrLabels.add(VertexServerGeoObject.getAttributeLabel(type, attr));
      }

      DuplicateGeoObjectMultipleException ex = new DuplicateGeoObjectMultipleException();
      ex.setAttributeLabels(StringUtils.join(attrLabels, ", "));
      throw ex;
    }
  }

  @Transaction
  public ServerGeoObjectIF apply(GeoObject object, boolean isNew, boolean isImport)
  {
    return this.apply(object, null, null, isNew, isImport);
  }

  @Transaction
  public ServerGeoObjectIF apply(GeoObjectOverTime goTime, boolean isNew, boolean isImport)
  {
    ServerGeoObjectType type = ServerGeoObjectType.get(goTime.getType());
    ServerGeoObjectStrategyIF strategy = this.getStrategy(type);

    if (isNew)
    {
      permissionService.enforceCanCreate(type.getOrganization().getCode(), type);
    }
    else
    {
      permissionService.enforceCanWrite(type.getOrganization().getCode(), type);
    }

    ServerGeoObjectIF goServer = strategy.constructFromGeoObjectOverTime(goTime, isNew);

    if (!isNew)
    {
      goServer.lock();
    }

    goServer.populate(goTime);

    try
    {
      goServer.apply(isImport);

      // Return the refreshed copy of the geoObject
      return this.build(type, goServer.getRunwayId());
    }
    catch (DuplicateDataException e)
    {
      handleDuplicateDataException(type, e);

      throw e;
    }
  }

  @Transaction
  public ServerGeoObjectIF split(GeoObjectSplitView view)
  {
    ServerGeoObjectType type = ServerGeoObjectType.get(view.getTypeCode());
    ServerGeoObjectStrategyIF strategy = this.getStrategy(type);

    final ServerGeoObjectIF source = strategy.getGeoObjectByCode(view.getSourceCode());
    source.setDate(view.getDate());

    ServerGeoObjectIF target = strategy.newInstance();
    target.setDate(view.getDate());
    target.populate(source.toGeoObject(view.getDate()), view.getDate(), view.getDate());
    target.setCode(view.getTargetCode());
    target.setDisplayLabel(view.getLabel());
    target.apply(false);

    final ServerParentTreeNode sNode = source.getParentGeoObjects(null, null, false, false, view.getDate());

    final List<ServerParentTreeNode> sParents = sNode.getParents();

    for (ServerParentTreeNode sParent : sParents)
    {
      final ServerGeoObjectIF parent = sParent.getGeoObject();
      final ServerHierarchyType hierarchyType = sParent.getHierarchyType();

      target.addParent(parent, hierarchyType, view.getDate(), null);
    }

    return target;
  }

  public ServerGeoObjectIF newInstance(ServerGeoObjectType type)
  {
    ServerGeoObjectStrategyIF strategy = this.getStrategy(type);

    return strategy.newInstance();
  }

  public ServerGeoObjectIF getGeoObject(GeoObject go)
  {
    ServerGeoObjectType type = ServerGeoObjectType.get(go.getType());

    ServerGeoObjectStrategyIF strategy = this.getStrategy(type);

    return strategy.constructFromGeoObject(go, false);
  }

  public ServerGeoObjectIF getGeoObject(GeoObjectOverTime timeGO)
  {
    ServerGeoObjectType type = ServerGeoObjectType.get(timeGO.getType());

    ServerGeoObjectStrategyIF strategy = this.getStrategy(type);

    return strategy.constructFromGeoObjectOverTime(timeGO, false);
  }

  public ServerGeoObjectIF getGeoObjectByCode(String code, String typeCode)
  {
    return this.getGeoObjectByCode(code, ServerGeoObjectType.get(typeCode));
  }

  public ServerGeoObjectIF getGeoObjectByCode(String code, String typeCode, boolean throwException)
  {
    return this.getGeoObjectByCode(code, ServerGeoObjectType.get(typeCode), throwException);
  }

  public ServerGeoObjectIF getGeoObjectByCode(String code, ServerGeoObjectType type)
  {
    return this.getGeoObjectByCode(code, type, true);
  }

  public ServerGeoObjectIF getGeoObjectByCode(String code, ServerGeoObjectType type, boolean throwException)
  {
    this.permissionService.enforceCanRead(type.getOrganization().getCode(), type);

    ServerGeoObjectStrategyIF strategy = this.getStrategy(type);

    ServerGeoObjectIF geoObject = strategy.getGeoObjectByCode(code);

    if (geoObject == null && throwException)
    {
      DataNotFoundException ex = new DataNotFoundException("Could not find a GeoObject with code [" + code + "].");
      ex.setTypeLabel(GeoObjectMetadata.get().getClassDisplayLabel());
      ex.setDataIdentifier(code);
      ex.setAttributeLabel(GeoObjectMetadata.get().getAttributeDisplayLabel(DefaultAttribute.CODE.getName()));

      throw ex;
    }

    return geoObject;
  }

  public ServerGeoObjectIF getGeoObject(String uid, String typeCode)
  {
    ServerGeoObjectType type = ServerGeoObjectType.get(typeCode);

    this.permissionService.enforceCanRead(type.getOrganization().getCode(), type);

    ServerGeoObjectStrategyIF strategy = this.getStrategy(type);
    ServerGeoObjectIF object = strategy.getGeoObjectByUid(uid);

    if (object == null)
    {
      DataNotFoundException ex = new DataNotFoundException();
      ex.setTypeLabel(type.getLabel().getValue());
      ex.setAttributeLabel(GeoObjectMetadata.get().getAttributeDisplayLabel(DefaultAttribute.UID.getName()));
      ex.setDataIdentifier(uid);
      throw ex;
    }

    return object;
  }

  public ServerGeoObjectStrategyIF getStrategy(ServerGeoObjectType type)
  {
    return new VertexGeoObjectStrategy(type);
  }

  public ServerGeoObjectIF build(ServerGeoObjectType type, String runwayId)
  {
    ServerGeoObjectStrategyIF strategy = this.getStrategy(type);
    VertexObject vertex = VertexObject.get(type.getMdVertex(), runwayId);

    return strategy.constructFromDB(vertex);
  }

  public ServerGeoObjectIF build(ServerGeoObjectType type, Object dbObject)
  {
    ServerGeoObjectStrategyIF strategy = this.getStrategy(type);
    return strategy.constructFromDB(dbObject);
  }

  public ServerGeoObjectQuery createQuery(ServerGeoObjectType type, Date date)
  {
    return new VertexGeoObjectQuery(type, date);
  }

  public boolean hasData(ServerHierarchyType serverHierarchyType, ServerGeoObjectType childType)
  {
    return VertexServerGeoObject.hasData(serverHierarchyType, childType);
  }

  public void removeAllEdges(ServerHierarchyType hierarchyType, ServerGeoObjectType childType)
  {
    VertexServerGeoObject.removeAllEdges(hierarchyType, childType);
  }

  @Request(RequestType.SESSION)
  public JsonObject doesGeoObjectExistAtRange(String sessionId, Date startDate, Date endDate, String typeCode, String code)
  {
    VertexServerGeoObject vsgo = (VertexServerGeoObject) new ServerGeoObjectService().getGeoObjectByCode(code, typeCode);

    JsonObject jo = new JsonObject();

    jo.addProperty("exists", vsgo.existsAtRange(startDate, endDate));
    jo.addProperty("invalid", vsgo.getInvalid());

    return jo;
  }

  @Request(RequestType.SESSION)
  public JsonObject hasDuplicateLabel(String sessionId, Date date, String typeCode, String code, String label)
  {
    boolean inUse = VertexServerGeoObject.hasDuplicateLabel(date, typeCode, code, label);
    
    JsonObject jo = new JsonObject();   
    jo.addProperty("labelInUse", inUse);
    
    return jo;
  }
  
  @Request(RequestType.SESSION)
  public JsonArray getBusinessObjects(String sessionId, String typeCode, String code, String businessTypeCode)
  {
    VertexServerGeoObject vsgo = (VertexServerGeoObject) new ServerGeoObjectService().getGeoObjectByCode(code, typeCode);

    List<BusinessObject> objects = null;

    if (businessTypeCode != null && businessTypeCode.length() > 0)
    {
      BusinessType businessType = BusinessType.getByCode(businessTypeCode);

      objects = vsgo.getBusinessObjects(businessType);
    }
    else
    {
      objects = vsgo.getBusinessObjects();
    }

    return objects.stream().map(object -> object.toJSON()).collect(() -> new JsonArray(), (array, element) -> array.add(element), (listA, listB) -> {
    });
  }
}
