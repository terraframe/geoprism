/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism.data.etl;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.StringJoiner;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.util.ClientUtils;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.business.Business;
import com.runwaysdk.business.Transient;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.database.DatabaseException;
import com.runwaysdk.generated.system.gis.geo.GeoEntityAllPathsTableQuery;
import com.runwaysdk.query.F;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.OR;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.system.gis.geo.AllowedIn;
import com.runwaysdk.system.gis.geo.GeoEntity;
import com.runwaysdk.system.gis.geo.GeoEntityQuery;
import com.runwaysdk.system.gis.geo.SynonymQuery;
import com.runwaysdk.system.gis.geo.Universal;
import com.runwaysdk.system.metadata.MdAttribute;
import com.runwaysdk.system.metadata.ontology.SolrOntolgyStrategy;
import com.runwaysdk.system.metadata.ontology.SolrProperties;
import com.runwaysdk.util.IDGenerator;

import net.geoprism.GeoprismProperties;
import net.geoprism.data.importer.ExclusionException;
import net.geoprism.ontology.NonUniqueEntityResultException;

public class TargetFieldGeoEntity extends TargetField implements TargetFieldGeoEntityIF, TargetFieldValidationIF
{
  public static class UniversalAttribute
  {
    private String    attributeName;

    private String    label;

    private Universal universal;

    public UniversalAttribute(String attributeName, String label, Universal universal)
    {
      this.attributeName = attributeName;
      this.label = label;
      this.universal = universal;
    }

    public String getAttributeName()
    {
      return attributeName;
    }

    public void setAttributeName(String attributeName)
    {
      this.attributeName = attributeName;
    }

    public Universal getUniversal()
    {
      return universal;
    }

    public void setUniversal(Universal universal)
    {
      this.universal = universal;
    }

    public String getLabel()
    {
      return label;
    }
  }

  private ArrayList<UniversalAttribute> attributes;

  private String                        id;

  private GeoEntity                     root;

  private Universal                     rootUniversal;

  private boolean                       useCoordinatesForLocationAssignment = false;

  private JSONObject                    coordinateObject;

  public TargetFieldGeoEntity()
  {
    this.attributes = new ArrayList<UniversalAttribute>();
    this.id = IDGenerator.nextID();
  }

  public String getId()
  {
    return id;
  }

  public GeoEntity getRoot()
  {
    return root;
  }

  public void setRoot(GeoEntity root)
  {
    this.root = root;
    this.rootUniversal = root.getUniversal();
  }

  public void setUseCoordinatesForLocationAssignment(boolean useCoords)
  {
    this.useCoordinatesForLocationAssignment = useCoords;
  }

  public boolean getUseCoordinatesForLocationAssignment()
  {
    return this.useCoordinatesForLocationAssignment;
  }

  public void setCoordinateObject(String latitude, String longitude)
  {
    if (latitude != null && latitude != "")
    {
      try
      {
        JSONObject coordsObj = new JSONObject();
        coordsObj.put("latitude", latitude);
        coordsObj.put("longitude", longitude);

        this.coordinateObject = coordsObj;
      }
      catch (JSONException e)
      {
        throw new ProgrammingErrorException(e);
      }
    }
  }

  public JSONObject getCoordinateObject()
  {
    return this.coordinateObject;
  }

  public String getCoordinateObjectLatitude()
  {
    JSONObject coordObj = this.getCoordinateObject();

    if (coordObj != null)
    {
      try
      {
        String lat = coordObj.getString("latitude");

        return lat;
      }
      catch (JSONException e)
      {
        throw new ProgrammingErrorException(e);
      }
    }

    return null;
  }

  public String getCoordinateObjectLatitude(JSONObject coordinateObject)
  {
    String sourceLat = null;
    try
    {
      sourceLat = coordinateObject.getString("latitude");
    }
    catch (JSONException e)
    {
      throw new ProgrammingErrorException(e);
    }

    return sourceLat;
  }

  public String getCoordinateObjectLongitude()
  {
    JSONObject coordObj = this.getCoordinateObject();

    if (coordObj != null)
    {
      try
      {
        String longitude = coordObj.getString("longitude");

        return longitude;
      }
      catch (JSONException e)
      {
        throw new ProgrammingErrorException(e);
      }
    }
    return null;
  }

  public String getCoordinateObjectLongitude(JSONObject coordinateObject)
  {
    String sourceLong = null;
    try
    {
      sourceLong = coordinateObject.getString("longitude");
    }
    catch (JSONException e)
    {
      throw new ProgrammingErrorException(e);
    }

    return sourceLong;
  }

  public void addUniversalAttribute(String attributeName, String label, Universal universal)
  {
    this.attributes.add(new UniversalAttribute(attributeName, label, universal));
  }

  @Override
  public Map<String, String> getUniversalAttributes()
  {
    Map<String, String> map = new HashMap<String, String>();

    for (UniversalAttribute attribute : this.attributes)
    {
      map.put(attribute.getAttributeName(), attribute.getUniversal().getId());
    }

    return map;
  }

  @Override
  public FieldValue getValue(MdAttributeConcreteDAOIF mdAttribute, Transient source)
  {
    List<String> labels = new ArrayList<String>();

    for (UniversalAttribute attribute : attributes)
    {
      labels.add(source.getValue(attribute.getAttributeName()));
    }

    JSONObject coordObj = this.getCoordinates(source);

    String entityId = this.getLocation(this.root, labels, coordObj);

    return new FieldValue(entityId);
  }

  /**
   * 
   * 
   * @param labels
   * @return
   */
  private String getLocation(GeoEntity root, List<String> labels, JSONObject spatialRefCoordObj)
  {
    try
    {
      Collection<String> ids = this.findSolrGeoEntities(labels);

      if (ids.size() == 1)
      {
        return ids.iterator().next();
      }
      else if (!GeoprismProperties.getSolrLookup() || ids.size() > 1)
      {
        GeoEntity parent = root;

        for (int i = 0; i < attributes.size(); i++)
        {
          UniversalAttribute attribute = attributes.get(i);
          String label = labels.get(i);

          if (label != null && label.length() > 0)
          {
            Universal universal = attribute.getUniversal();

            if (rootUniversal.getKey().equals(universal.getKey()))
            {
              parent = root;
            }
            else
            {
              GeoEntity entity = this.findGeoEntity(parent, universal, label, spatialRefCoordObj);

              if (entity == null)
              {
                throw new ExclusionException("Location not found in system.");
              }

              parent = entity;
            }
          }
        }

        return parent.getId();
      }
      else
      {
        throw new ExclusionException("Location not found in system.");
      }
    }
    catch (SolrServerException e)
    {
      throw new ProgrammingErrorException(e);
    }
    catch (IOException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

  private Collection<String> findSolrGeoEntities(List<String> labels) throws SolrServerException, IOException
  {
    if (!GeoprismProperties.getSolrLookup())
    {
      return new LinkedList<String>();
    }

    List<String> conditions = new LinkedList<String>();

    Universal uni = null;

    for (int i = 0; i < attributes.size(); i++)
    {
      UniversalAttribute attribute = attributes.get(i);
      String label = labels.get(i);

      if (label != null && label.length() > 0)
      {
        Universal universal = attribute.getUniversal();

        conditions.add(universal.getId() + "%%%" + label);

        uni = universal;
      }
    }

    Collections.reverse(conditions);

    String qText = SolrOntolgyStrategy.QUALIFIER + ":(" + uni.getId() + ")";

    for (int i = 0; i < conditions.size(); i++)
    {
      String condition = conditions.get(i);

      qText += " AND " + SolrOntolgyStrategy.RELATIONSHIPS + ":(" + ClientUtils.escapeQueryChars(condition) + ")";
    }

    HttpSolrClient client = new HttpSolrClient.Builder(SolrProperties.getUrl()).build();

    try
    {
      SolrQuery query = new SolrQuery();
      query.setQuery(qText);
      query.setFields(SolrOntolgyStrategy.ENTITY);
      // query.setRows(0);

      QueryResponse response = client.query(query);

      SolrDocumentList list = response.getResults();

      Iterator<SolrDocument> iterator = list.iterator();

      Set<String> ids = new TreeSet<String>();

      while (iterator.hasNext())
      {
        SolrDocument document = iterator.next();

        String entityId = (String) document.getFieldValue(SolrOntolgyStrategy.ENTITY);

        ids.add(entityId);
      }

      return ids;
    }
    finally
    {
      client.close();
    }
  }

  private GeoEntity findGeoEntity(GeoEntity parent, Universal universal, String label, JSONObject spatialRefCoordObj)
  {
    QueryFactory factory = new QueryFactory();

    GeoEntityAllPathsTableQuery aptQuery = new GeoEntityAllPathsTableQuery(factory);
    aptQuery.WHERE(aptQuery.getParentTerm().EQ(parent));

    SynonymQuery synonymQuery = new SynonymQuery(factory);
    synonymQuery.WHERE(F.TRIM(synonymQuery.getDisplayLabel().localize()).EQi(label));

    GeoEntityQuery query = new GeoEntityQuery(factory);
    query.WHERE(query.getUniversal().EQ(universal));
    query.AND(query.getId().EQ(aptQuery.getChildTerm().getId()));
    query.AND(OR.get(F.TRIM(query.getDisplayLabel().localize()).EQi(label), query.synonym(synonymQuery)));

    OIterator<? extends GeoEntity> iterator = query.getIterator();
    ArrayList<GeoEntity> possibleEntities = new ArrayList<GeoEntity>();

    try
    {
      while (iterator.hasNext())
      {
        GeoEntity ge = iterator.next();
        possibleEntities.add(ge);
      }
    }
    finally
    {
      iterator.close();
    }

    if (possibleEntities.size() == 1)
    {
      return possibleEntities.get(0);
    }
    // If there's another entity there is more than one location found given the criteria (ambiguous)
    else if (possibleEntities.size() > 1)
    {
      if (this.getUseCoordinatesForLocationAssignment())
      {
        // try determining if one of the ambiguous entities are within the parent as determined by spatial st_within
        // from
        // coordinates in the spreadsheet
        GeoEntity inferredEntity = null;

        Stack<Universal> stack = new Stack<Universal>();
        stack.push(universal);
        while (stack.size() > 0)
        {
          Universal current = stack.pop();

          inferredEntity = findGeoEntityByParentComparison(label, current, spatialRefCoordObj, possibleEntities);
          if (inferredEntity != null)
          {
            break;
          }

          OIterator<? extends Business> it = current.getParents(AllowedIn.CLASS);
          try
          {
            if (!it.hasNext())
            {
              continue;
            }

            for (Business parentOfCurrent : it)
            {
              stack.push((Universal) parentOfCurrent);
            }
          }
          finally
          {
            it.close();
          }
        }

        if (inferredEntity != null)
        {
          return inferredEntity;
        }
        else
        {
          // try coordinate (from spreadsheet) based nearest neighbor analysis
          inferredEntity = findGeoEntityByNearestNeighbor(label, universal, spatialRefCoordObj, possibleEntities);
          if (inferredEntity != null)
          {
            return inferredEntity;
          }
          else
          {
            NonUniqueEntityResultException e = new NonUniqueEntityResultException();
            e.setLabel(label);
            e.setUniversal(universal.getDisplayLabel().getValue());
            e.setParent(parent.getDisplayLabel().getValue());

            throw e;
          }
        }
      }
      else
      {
        NonUniqueEntityResultException e = new NonUniqueEntityResultException();
        e.setLabel(label);
        e.setUniversal(universal.getDisplayLabel().getValue());
        e.setParent(parent.getDisplayLabel().getValue());

        throw e;
      }
    }

    return null;
  }

  private GeoEntity findGeoEntityByNearestNeighbor(String locationName, Universal universal, JSONObject spatialRefCoordObj, ArrayList<GeoEntity> geoEntities)
  {
    if (spatialRefCoordObj != null)
    {
      String comparativeTargetGeomColumnName = "geo_multi_polygon"; // use geo_multi_polygon because all geonodes have
                                                                    // point and polygon representation

      String sourceLat = this.getCoordinateObjectLatitude(spatialRefCoordObj);
      String sourceLong = this.getCoordinateObjectLongitude(spatialRefCoordObj);

      StringJoiner geoIdString = new StringJoiner(",");
      for (int i = 0; i < geoEntities.size(); i++)
      {
        GeoEntity entity = geoEntities.get(i);

        // geometries are needed for doing st_distance calculations
        if (entity.getGeoMultiPolygon() != null)
        {
          geoIdString.add("'".concat( ( entity.getGeoId() ).concat("'")));
        }
      }

      if (geoIdString != null && geoIdString.length() > 0)
      {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT g.id, ST_Distance(g.".concat(comparativeTargetGeomColumnName).concat(", ST_SetSRID(st_pointfromtext('POINT(".concat(sourceLong).concat(" ").concat(sourceLat).concat(")'), 4326))")));
        sql.append(" FROM geo_entity g");
        sql.append(" WHERE g.geo_id IN (".concat(geoIdString.toString()).concat(")"));
        sql.append(" ORDER BY 2 ASC");
        sql.append(" LIMIT 1;");

        ResultSet resultSet = Database.query(sql.toString());

        try
        {
          GeoEntity geoEntity = null;
          if (resultSet != null)
          {
            try
            {
              if (resultSet.next())
              {
                String entityId = resultSet.getString(1);
                geoEntity = GeoEntity.get(entityId);

                return geoEntity;
              }
            }
            catch (SQLException e)
            {
              throw new ProgrammingErrorException(e);
            }
          }
        }
        finally
        {
          try
          {
            resultSet.close();
          }
          catch (SQLException e)
          {
            throw new DatabaseException(e);
          }
        }
      }
    }

    return null;
  }

  private GeoEntity findGeoEntityByParentComparison(String locationName, Universal universal, JSONObject spatialRefCoordObj, ArrayList<GeoEntity> geoEntities)
  {
    String sourceLat = this.getCoordinateObjectLatitude(spatialRefCoordObj);
    String sourceLong = this.getCoordinateObjectLongitude(spatialRefCoordObj);

    ArrayList<String> geoIdList = new ArrayList<String>();
    for (GeoEntity entity : geoEntities)
    {
      geoIdList.add(entity.getGeoId());
    }

    OIterator<? extends Business> parentUniversals = universal.getParents(AllowedIn.CLASS);
    try
    {
      while (parentUniversals.hasNext())
      {
        Universal parentUniversal = (Universal) parentUniversals.next();

        if (parentUniversals.hasNext())
        {
          return null; // We can't support multi-parent hierarchies for this process
        }
        else
        {
          ArrayList<String> childIds = new ArrayList<String>();
          for (GeoEntity geo : geoEntities)
          {
            childIds.add(geo.getId());
          }

          StringBuffer sql = new StringBuffer();
          sql.append("SELECT g.id, apt.child_term ");
          sql.append("FROM geo_entity g, geo_entity_all_paths_table apt ");
          sql.append("WHERE ST_Within(ST_SetSRID(st_pointfromtext('POINT(".concat(sourceLong).concat(" ").concat(sourceLat).concat(")'), 4326)").concat(", g.geo_multi_polygon) "));
          sql.append("AND g.universal = '" + parentUniversal.getId() + "' ");
          sql.append("AND apt.parent_term=g.id ");
          sql.append("AND apt.child_term = ANY('{" + StringUtils.join(childIds, ",") + "}')");
          ResultSet resultSet = Database.query(sql.toString());

          try
          {
            if (resultSet != null)
            {
              try
              {
                if (resultSet.next())
                {
                  String childTerm = resultSet.getString(2);

                  if (resultSet.next()) // Our query should only return 1 result
                  {
                    return null;
                  }
                  return GeoEntity.get(childTerm);
                }
                return null;
              }
              catch (SQLException e)
              {
                throw new ProgrammingErrorException(e);
              }
            }
          }
          finally
          {
            try
            {
              resultSet.close();
            }
            catch (SQLException e)
            {
              throw new DatabaseException(e);
            }
          }
        }
      }
    }
    finally
    {
      parentUniversals.close();
    }

    return null;
  }

  @Override
  public void persist(TargetBinding binding)
  {
    MdAttribute targetAttribute = MdAttribute.getByKey(this.getKey());

    TargetFieldGeoEntityBinding field = new TargetFieldGeoEntityBinding();
    field.setTarget(binding);
    field.setTargetAttribute(targetAttribute);
    field.setColumnLabel(this.getLabel());
    field.setGeoEntity(this.root);
    field.setUseCoordinatesForLocationAssignment(this.getUseCoordinatesForLocationAssignment());
    field.setLatitudeAttributeName(this.getCoordinateObjectLatitude());
    field.setLongitudeAttributeName(this.getCoordinateObjectLongitude());
    field.apply();

    for (UniversalAttribute attribute : this.attributes)
    {
      MdAttribute sourceAttribute = MdAttribute.getByKey(binding.getSourceView().definesType() + "." + attribute.getAttributeName());

      UniversalAttributeBinding uAttribute = new UniversalAttributeBinding();
      uAttribute.setField(field);
      uAttribute.setSourceAttribute(sourceAttribute);
      uAttribute.setUniversal(attribute.getUniversal());
      uAttribute.apply();
    }
  }

  @Override
  public JSONObject toJSON() throws JSONException
  {
    JSONObject fields = new JSONObject();

    for (UniversalAttribute attribute : this.attributes)
    {
      fields.put(attribute.getUniversal().getId(), attribute.getLabel());
    }

    UniversalAttribute attribute = this.attributes.get(this.attributes.size() - 1);

    JSONObject object = new JSONObject();
    object.put("label", this.getLabel());
    object.put("universal", attribute.getUniversal().getId());
    object.put("fields", fields);
    object.put("id", this.id);
    object.put("useCoordinatesForLocationAssignment", this.getUseCoordinatesForLocationAssignment());

    return object;
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.geoprism.data.etl.TargetFieldGeoEntityIF#getLocationProblem(com.runwaysdk.business.Transient,
   * com.runwaysdk.system.gis.geo.Universal)
   */
  @SuppressWarnings("unchecked")
  @Override
  public ImportProblemIF validate(Transient source, Map<String, Object> parameters)
  {
    try
    {
      List<String> labels = new ArrayList<String>();

      for (UniversalAttribute attribute : attributes)
      {
        labels.add(source.getValue(attribute.getAttributeName()));
      }

      Collection<String> ids = this.findSolrGeoEntities(labels);

      if (ids.size() != 1)
      {
        GeoEntity parent = this.root;

        Map<String, Set<String>> locationExclusions = (Map<String, Set<String>>) parameters.get("locationExclusions");

        List<JSONObject> context = new LinkedList<JSONObject>();

        for (UniversalAttribute attribute : attributes)
        {
          String label = source.getValue(attribute.getAttributeName());
          Universal entityUniversal = attribute.getUniversal();

          if (label != null && label.length() > 0)
          {
            if (this.isExcluded(locationExclusions, entityUniversal, parent, label))
            {
              return null;
            }

            JSONObject coordObj = this.getCoordinates(source);

            if (parent.getUniversalId().equals(entityUniversal.getId()))
            {
              GeoEntity entity = this.findGeoEntity(GeoEntity.getRoot(), entityUniversal, label, coordObj);

              if (entity == null)
              {
                return new LocationProblem(label, context, GeoEntity.getRoot(), entityUniversal);
              }
            }
            else
            {
              GeoEntity entity = this.findGeoEntity(parent, entityUniversal, label, coordObj);

              if (entity == null)
              {
                return new LocationProblem(label, context, parent, entityUniversal);
              }
              else
              {
                parent = entity;
              }
            }
          }

          JSONObject object = new JSONObject();
          object.put("label", label);
          object.put("universal", parent.getUniversal().getDisplayLabel().getValue());

          context.add(object);
        }
      }

      return null;
    }
    catch (SolrServerException | IOException e)
    {
      throw new ProgrammingErrorException(e);
    }
    catch (JSONException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

  private JSONObject getCoordinates(Transient source)
  {
    JSONObject cObject = this.getCoordinateObject();
    if (cObject != null)
    {
      String latitudeFieldName = cObject.get("latitude").toString().toLowerCase();
      String longitudeFieldName = cObject.get("longitude").toString().toLowerCase();

      if (longitudeFieldName.equals("long"))
      {
        longitudeFieldName = "longAttribute"; // long is a keyword so we need to change to what it is coerced to
                                              // in
                                              // TransientDAO
      }

      String latitude = source.getValue(latitudeFieldName);
      String longitude = source.getValue(longitudeFieldName);

      JSONObject coordinates = new JSONObject();
      coordinates.put("latitude", latitude);
      coordinates.put("longitude", longitude);

      return coordinates;
    }

    return null;
  }

  private boolean isExcluded(Map<String, Set<String>> locationExclusions, Universal universal, GeoEntity parent, String label)
  {
    String key = universal.getId() + "-" + parent.getId();

    if (universal.getId().equals(this.rootUniversal.getId()))
    {
      key = universal.getId() + "-" + GeoEntity.getRoot().getId();
    }

    if (locationExclusions.containsKey(key))
    {
      Set<String> labels = locationExclusions.get(key);

      return labels.contains(label);
    }

    return false;
  }
}
