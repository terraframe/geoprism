/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
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
package net.geoprism.data;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.business.ValueObjectDTO;
import com.runwaysdk.business.ValueQueryDTO;
import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.mvc.Controller;
import com.runwaysdk.mvc.Endpoint;
import com.runwaysdk.mvc.ErrorSerialization;
import com.runwaysdk.mvc.ParseType;
import com.runwaysdk.mvc.RequestParamter;
import com.runwaysdk.mvc.ResponseIF;
import com.runwaysdk.mvc.RestBodyResponse;
import com.runwaysdk.mvc.RestResponse;
import com.runwaysdk.system.gis.geo.GeoEntityDTO;
import com.runwaysdk.system.gis.geo.GeoEntityViewDTO;
import com.runwaysdk.system.gis.geo.GeometryTypeDTO;
import com.runwaysdk.system.gis.geo.LocatedInDTO;
import com.runwaysdk.system.gis.geo.SynonymDTO;
import com.runwaysdk.system.gis.geo.UniversalDTO;
import com.runwaysdk.system.metadata.MdRelationshipDTO;
import com.runwaysdk.util.IDGenerator;

import net.geoprism.ExcludeConfiguration;
import net.geoprism.GeoprismUserDTO;
import net.geoprism.InputStreamResponse;
import net.geoprism.ListSerializable;
import net.geoprism.RoleViewDTO;
import net.geoprism.gis.geoserver.GeoserverProperties;
import net.geoprism.ontology.GeoEntityUtilDTO;

@Controller(url = "location")
public class LocationController
{
  @Endpoint(error = ErrorSerialization.JSON)
  public ResponseIF select(ClientRequestIF request, @RequestParamter(name = "oid") String oid, @RequestParamter(name = "universalId") String universalId, @RequestParamter(name = "existingLayers") String existingLayers, @RequestParamter(name = "mdRelationshipId") String mdRelationshipId) throws JSONException
  {
    GeoEntityDTO entity = GeoEntityUtilDTO.getEntity(request, oid);

    return this.getLocationInformation(request, entity, universalId, existingLayers, mdRelationshipId);
  }

  @Endpoint(error = ErrorSerialization.JSON)
  public ResponseIF open(ClientRequestIF request, @RequestParamter(name = "oid") String oid, @RequestParamter(name = "existingLayers") String existingLayers, @RequestParamter(name = "mdRelationshipId") String mdRelationshipId) throws JSONException
  {
    GeoEntityDTO entity = GeoEntityUtilDTO.getEntity(request, oid);

    if (mdRelationshipId == null || mdRelationshipId.length() == 0)
    {
      mdRelationshipId = GeoEntityUtilDTO.findValidMdRelationship(request, entity.getOid());
    }

    List<GeoEntityDTO> ancestors = Arrays.asList(GeoEntityUtilDTO.getOrderedAncestors(request, entity.getOid(), mdRelationshipId));

    RestResponse response = this.getLocationInformation(request, entity, null, existingLayers, mdRelationshipId);
    response.set("ancestors", new ListSerializable(ancestors), new GeoEntityJsonConfiguration());

    return response;
  }

  private RestResponse getLocationInformation(ClientRequestIF request, GeoEntityDTO entity, String universalId, String existingLayers, String mdRelationshipId) throws JSONException
  {
    UniversalDTO universal = entity.getUniversal();
    MdRelationshipDTO[] hierarchies = GeoEntityUtilDTO.getHierarchies(request, universal.getOid());
    String universalLabel = "";

    MdRelationshipDTO mdRelationshipDTO = null;

    if (mdRelationshipId == null || mdRelationshipId.length() == 0)
    {
      mdRelationshipDTO = hierarchies[0];
    }
    else
    {
      mdRelationshipDTO = MdRelationshipDTO.get(request, mdRelationshipId);
    }

    String entityRelationshipId = GeoEntityUtilDTO.getGeoEntityRelationship(request, mdRelationshipDTO.getOid());

    UniversalDTO[] universals = GeoEntityUtilDTO.getUniversals(request, universal.getOid(), mdRelationshipDTO.getOid());

    if ( ( universalId == null || universalId.length() == 0 ) && universals.length > 0)
    {
      universalId = universals[0].getOid();
      universalLabel = universals[0].getDisplayLabel().getValue();
    }

    // String geometries = GeoEntityUtilDTO.publishLayers(request,
    // entity.getOid(), universalId, existingLayers);

    ValueQueryDTO children = GeoEntityUtilDTO.getChildren(request, entity.getOid(), universalId, 200, entityRelationshipId);

    RestResponse response = new RestResponse();

    // if (children.getCount() > 0)
    // {
    // JSONArray jaRoles = new JSONArray();
    // RoleViewDTO[] roles = RoleViewDTO.getRoles(request, (GeoprismUserDTO)
    // net.geoprism.GeoprismUserDTO.getCurrentUser(request));
    // for (RoleViewDTO role : roles)
    // {
    // JSONObject jo = new JSONObject();
    // jo.put("assigned", role.getAssigned());
    // jo.put("label", role.getDisplayLabel());
    // jaRoles.put(jo);
    // }
    response.set("roles", new JSONArray(RoleViewDTO.getCurrentRoles(request)));

    response.set("children", children);

    response.set("universals", new ListSerializable(Arrays.asList(universals)));
    response.set("hierarchies", new ListSerializable(Arrays.asList(hierarchies)));
    response.set("hierarchy", mdRelationshipDTO.getOid());
    response.set("entityRelationship", entityRelationshipId);
    response.set("entity", new GeoEntitySerializable(entity), new GeoEntityJsonConfiguration());
    response.set("universal", ( universalId != null && universalId.length() > 0 ) ? universalId : "");
    response.set("universalLabel", universalLabel);
    response.set("workspace", GeoserverProperties.getWorkspace());
    response.set("geometryType", universal.getGeometryType().get(0).getName());

//    if (children.getCount() > 0)
//    {
//      response.set("bbox", GeoEntityUtilDTO.getChildrenBBOX(request, entity.getOid(), universalId));
//    }
//    else
//    {
      response.set("bbox", GeoEntityUtilDTO.getEntitiesBBOX(request, new String[] { entity.getOid() }));
//    }

    if (universalId != null)
    {
      UniversalDTO childUniversal = UniversalDTO.get(request, universalId);
      response.set("childType", childUniversal.getGeometryType().get(0).getName());
    }
    else
    {
      response.set("childType", GeometryTypeDTO.MULTIPOLYGON);
    }

    // response.set("geometries", new JSONStringImpl(geometries));
    // response.set("layers", object.get("layers"));

    return response;
    // }
    //
    // return response;
  }

  @Endpoint(error = ErrorSerialization.JSON)
  public ResponseIF suggestions(ClientRequestIF request, @RequestParamter(name = "text") String text, @RequestParamter(name = "limit") Integer limit, @RequestParamter(name = "mdRelationshipId") String mdRelationshipId) throws JSONException
  {
    ValueQueryDTO results = GeoEntityUtilDTO.getGeoEntitySuggestions(request, null, null, text, limit, mdRelationshipId);

    return new RestBodyResponse(results);
  }

  @Endpoint(error = ErrorSerialization.JSON)
  public ResponseIF apply(ClientRequestIF request, @RequestParamter(name = "entity", parser = ParseType.BASIC_JSON) GeoEntityDTO entity, @RequestParamter(name = "parentOid") String parentOid, @RequestParamter(name = "existingLayers") String existingLayers, @RequestParamter(name = "mdRelationshipId") String mdRelationshipId) throws JSONException
  {
    if (entity.getGeoId() == null || entity.getGeoId().length() == 0)
    {
      entity.setGeoId(IDGenerator.nextID());
    }

    if (entity.isNewInstance())
    {
      String relationshipType = LocatedInDTO.CLASS;

      if (mdRelationshipId != null && mdRelationshipId.length() > 0)
      {
        String geoEntityRelationship = GeoEntityUtilDTO.getGeoEntityRelationship(request, mdRelationshipId);
        MdRelationshipDTO mdRelationship = MdRelationshipDTO.get(request, geoEntityRelationship);

        relationshipType = mdRelationship.getKeyName();
      }

      GeoEntityViewDTO dto = GeoEntityDTO.create(request, entity, parentOid, relationshipType);

      GeoEntityUtilDTO.refreshViews(request, existingLayers);

      JSONObject object = new JSONObject();
      object.put(GeoEntityDTO.TYPE, ValueObjectDTO.CLASS);
      object.put(GeoEntityDTO.OID, dto.getGeoEntityId());
      object.put(GeoEntityDTO.DISPLAYLABEL, dto.getGeoEntityDisplayLabel());
      object.put(GeoEntityDTO.GEOID, entity.getGeoId());
      object.put(GeoEntityDTO.UNIVERSAL, dto.getUniversalDisplayLabel());

      return new RestBodyResponse(object);
    }
    else
    {
      String oid = entity.getOid();

      entity.apply();

      GeoEntityUtilDTO.refreshViews(request, existingLayers);

      JSONObject object = new JSONObject();
      object.put(GeoEntityDTO.TYPE, ValueObjectDTO.CLASS);
      object.put(GeoEntityDTO.OID, entity.getOid());
      object.put(GeoEntityDTO.DISPLAYLABEL, entity.getDisplayLabel().getValue());
      object.put(GeoEntityDTO.GEOID, entity.getGeoId());
      object.put(GeoEntityDTO.UNIVERSAL, entity.getUniversal().getDisplayLabel().getValue());
      object.put("oid", oid);

      return new RestBodyResponse(object);
    }
  }

  @Endpoint(error = ErrorSerialization.JSON)
  public ResponseIF applyGeometries(ClientRequestIF request, @RequestParamter(name = "featureCollection") String featureCollection)
  {
    GeoEntityUtilDTO.applyGeometries(request, featureCollection);

    return new RestBodyResponse("");
  }

  @Endpoint(error = ErrorSerialization.JSON)
  public ResponseIF openEditingSession(ClientRequestIF request, @RequestParamter(name = "config") String config)
  {
    InputStream istream = GeoEntityUtilDTO.openEditingSession(request, config.toString());

    return new InputStreamResponse(istream, "application/x-protobuf", null);
  }

  @Endpoint(error = ErrorSerialization.JSON)
  public ResponseIF cancelEditingSession(ClientRequestIF request, @RequestParamter(name = "config") String config)
  {
    GeoEntityUtilDTO.cancelEditingSession(request, config.toString());

    return new RestBodyResponse("");
  }

  @Endpoint(error = ErrorSerialization.JSON)
  public ResponseIF viewSynonyms(ClientRequestIF request, @RequestParamter(name = "entityId") String entityId) throws JSONException
  {
    GeoEntityDTO entity = GeoEntityDTO.get(request, entityId);

    List<? extends SynonymDTO> synonyms = entity.getAllSynonym();
    for (SynonymDTO syn : synonyms)
    {
      syn.lock();
    }

    ListSerializable list = new ListSerializable(synonyms);

    return new RestBodyResponse(list);
  }

  @Endpoint(error = ErrorSerialization.JSON)
  public ResponseIF applyEditSynonyms(ClientRequestIF request, @RequestParamter(name = "synonyms") String sjsonSynonyms) throws JSONException
  {
    JSONObject jobjSynonyms = new JSONObject(sjsonSynonyms);

    String sParent = jobjSynonyms.getString("parent");

    JSONArray synonyms = jobjSynonyms.getJSONArray("synonyms");

    for (int i = 0; i < synonyms.length(); ++i)
    {
      JSONObject synonym = synonyms.getJSONObject(i);

      String oid = synonym.getString("oid");
      if (oid.length() == 36)
      {
        SynonymDTO syn = SynonymDTO.get(request, oid);
        syn.getDisplayLabel().setValue(synonym.getString("displayLabel"));
        syn.apply();
      }
      else
      {
        SynonymDTO syn = new SynonymDTO(request);
        syn.getDisplayLabel().setValue(synonym.getString("displayLabel"));

        SynonymDTO.create(request, syn, sParent);
      }
    }

    JSONArray deleted = jobjSynonyms.getJSONArray("deleted");

    for (int i = 0; i < deleted.length(); ++i)
    {
      String delId = deleted.getString(i);

      SynonymDTO.get(request, delId).delete();
    }

    return new RestBodyResponse("");
  }

  @Endpoint(error = ErrorSerialization.JSON)
  public ResponseIF cancelEditSynonyms(ClientRequestIF request, @RequestParamter(name = "synonyms") String synonymsJSONArray) throws JSONException
  {
    JSONArray synonyms = new JSONArray(synonymsJSONArray);

    for (int i = 0; i < synonyms.length(); ++i)
    {
      SynonymDTO.get(request, synonyms.getString(i)).unlock();
    }

    return new RestBodyResponse("");
  }

  @Endpoint(error = ErrorSerialization.JSON)
  public ResponseIF edit(ClientRequestIF request, @RequestParamter(name = "entityId") String entityId) throws JSONException
  {
    GeoEntityDTO entity = GeoEntityDTO.lock(request, entityId);

    return new RestBodyResponse(entity, new ExcludeConfiguration(GeoEntityDTO.class, GeoEntityDTO.WKT));
  }

  @Endpoint(error = ErrorSerialization.JSON)
  public ResponseIF unlock(ClientRequestIF request, @RequestParamter(name = "entityId") String entityId) throws JSONException
  {
    GeoEntityDTO.unlock(request, entityId);

    return new RestBodyResponse("");
  }

  @Endpoint(error = ErrorSerialization.JSON)
  public ResponseIF remove(ClientRequestIF request, @RequestParamter(name = "entityId") String entityId, @RequestParamter(name = "existingLayers") String existingLayers) throws JSONException
  {
    GeoEntityUtilDTO.deleteGeoEntity(request, entityId);

    GeoEntityUtilDTO.refreshViews(request, existingLayers);

    return new RestBodyResponse("");
  }

  @Endpoint(error = ErrorSerialization.JSON)
  public ResponseIF data(ClientRequestIF request, @RequestParamter(name = "x") Integer x, @RequestParamter(name = "y") Integer y, @RequestParamter(name = "z") Integer z, @RequestParamter(name = "config") String config) throws JSONException
  {
    JSONObject object = new JSONObject(config);
    object.put("x", x);
    object.put("y", y);
    object.put("z", z);

    InputStream istream = GeoEntityUtilDTO.getData(request, object.toString());

    return new InputStreamResponse(istream, "application/x-protobuf", null);
  }
}
