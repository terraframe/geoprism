package net.geoprism.registry.service;

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
import org.springframework.stereotype.Component;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.localization.LocalizationFacade;
import com.runwaysdk.session.Request;
import com.runwaysdk.session.RequestType;
import com.runwaysdk.session.Session;

import net.geoprism.graph.service.LocaleSerializer;
import net.geoprism.graphrepo.permission.GeoObjectPermissionServiceIF;
import net.geoprism.registry.business.GeoObjectBusinessService;
import net.geoprism.registry.hierarchy.HierarchyService;
import net.geoprism.registry.model.ServerChildTreeNode;
import net.geoprism.registry.model.ServerGeoObjectIF;
import net.geoprism.registry.model.ServerGeoObjectType;
import net.geoprism.registry.model.ServerHierarchyType;
import net.geoprism.registry.view.ServerParentTreeNodeOverTime;

@Component
public class GeoObjectService implements GeoObjectServiceIF
{
  @Autowired
  protected GeoObjectBusinessService service;
  
  @Request(RequestType.SESSION)
  public GeoObject getGeoObject(String sessionId, String uid, String geoObjectTypeCode, Date date)
  {
    ServerGeoObjectIF object = this.service.getGeoObject(uid, geoObjectTypeCode);

    final GeoObjectPermissionServiceIF pService = ServiceFactory.getGeoObjectPermissionService();
    pService.enforceCanRead(object.getType().getOrganization().getCode(), object.getType());

    ServerGeoObjectType type = object.getType();

    GeoObject geoObject = service.toGeoObject(object, date);
    geoObject.setWritable(pService.canCreateCR(type.getOrganization().getCode(), type));

    return geoObject;
  }

  @Request(RequestType.SESSION)
  public GeoObject getGeoObjectByCode(String sessionId, String code, String typeCode, Date date)
  {
    ServerGeoObjectIF object = service.getGeoObjectByCode(code, typeCode, true);

    ServiceFactory.getGeoObjectPermissionService().enforceCanRead(object.getType().getOrganization().getCode(), object.getType());

    return service.toGeoObject(object, date);
  }

  @Request(RequestType.SESSION)
  public GeoObject createGeoObject(String sessionId, String jGeoObj, Date startDate, Date endDate)
  {
    GeoObject geoObject = GeoObject.fromJSON(ServiceFactory.getAdapter(), jGeoObj);

    ServerGeoObjectIF object = service.apply(geoObject, startDate, endDate, true, false);

    return service.toGeoObject(object, startDate);
  }

  @Request(RequestType.SESSION)
  public GeoObject updateGeoObject(String sessionId, String jGeoObj, Date startDate, Date endDate)
  {
    GeoObject geoObject = GeoObject.fromJSON(ServiceFactory.getAdapter(), jGeoObj);

    ServerGeoObjectIF object = service.apply(geoObject, startDate, endDate, false, false);

    return service.toGeoObject(object, startDate);
  }
  
  @Request(RequestType.SESSION)
  public String getGeoObjectBounds(String sessionId, GeoObject geoObject)
  {
    return this.service.getGeoObject(geoObject).bbox(null);
  }

  @Request(RequestType.SESSION)
  public String getGeoObjectBoundsAtDate(String sessionId, GeoObject geoObject, Date date)
  {
    return this.service.getGeoObject(geoObject).bbox(date);
  }

  @Request(RequestType.SESSION)
  public GeoObjectOverTime getGeoObjectOverTimeByCode(String sessionId, String code, String typeCode)
  {
    ServerGeoObjectIF goServer = service.getGeoObjectByCode(code, typeCode, true);

    return service.toGeoObjectOverTime(goServer);
  }

  @Request(RequestType.SESSION)
  public GeoObjectOverTime updateGeoObjectOverTime(String sessionId, String jGeoObj)
  {
    GeoObjectOverTime goTime = GeoObjectOverTime.fromJSON(ServiceFactory.getAdapter(), jGeoObj);
    ServerGeoObjectType type = ServerGeoObjectType.get(goTime.getType().getCode());

    ServiceFactory.getGeoObjectPermissionService().enforceCanWrite(goTime.getType().getOrganizationCode(), type);

    ServerGeoObjectIF object = service.apply(goTime, false, false);

    return service.toGeoObjectOverTime(object);
  }

  @Request(RequestType.SESSION)
  public GeoObjectOverTime createGeoObjectOverTime(String sessionId, String jGeoObj)
  {
    GeoObjectOverTime goTime = GeoObjectOverTime.fromJSON(ServiceFactory.getAdapter(), jGeoObj);
    ServerGeoObjectType type = ServerGeoObjectType.get(goTime.getType().getCode());

    ServiceFactory.getGeoObjectPermissionService().enforceCanCreate(goTime.getType().getOrganizationCode(), type);

    ServerGeoObjectIF object = service.apply(goTime, true, false);

    return service.toGeoObjectOverTime(object);
  }

  @Request(RequestType.SESSION)
  public GeoObjectOverTime getGeoObjectOverTime(String sessionId, String id, String typeCode)
  {
    ServerGeoObjectIF object = this.service.getGeoObject(id, typeCode);

    ServiceFactory.getGeoObjectPermissionService().enforceCanRead(object.getType().getOrganization().getCode(), object.getType());

    return service.toGeoObjectOverTime(object);
  }
  
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
  
  @Request(RequestType.SESSION)
  public GeoObject newGeoObjectInstance(String sessionId, String geoObjectTypeCode)
  {
    return ServiceFactory.getAdapter().newGeoObjectInstance(geoObjectTypeCode);
  }

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

    JsonArray hierarchies = ServiceFactory.getHierarchyService().getHierarchiesForType(sessionId, go.getType().getCode(), true);

    joResp.put("hierarchies", new JSONArray(hierarchies.toString()));

    return joResp.toString();
  }

  @Request(RequestType.SESSION)
  public String newGeoObjectInstanceOverTime(String sessionId, String typeCode)
  {
    final ServerGeoObjectType type = ServerGeoObjectType.get(typeCode);

    ServerGeoObjectIF go = service.newInstance(type);

    go.setInvalid(false);

    final GeoObjectOverTime goot = service.toGeoObjectOverTime(go);
    ServerParentTreeNodeOverTime pot = service.getParentsOverTime(go, null, true, true);

    HierarchyService.filterHierarchiesFromPermissions(type, pot);

    /**
     * Serialize the GeoObject and add it to the response
     */
    JsonObject response = new JsonObject();

    response.add("geoObject", goot.toJSON());
    response.add("hierarchies", pot.toJSON());

    return response.toString();
  }
}
