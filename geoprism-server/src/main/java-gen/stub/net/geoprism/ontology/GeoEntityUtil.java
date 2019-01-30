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
package net.geoprism.ontology;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONWriter;

import com.runwaysdk.business.Business;
import com.runwaysdk.business.BusinessFacade;
import com.runwaysdk.business.ontology.Term;
import com.runwaysdk.business.rbac.Authenticate;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.DuplicateDataException;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.database.BusinessDAOFactory;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.generated.system.gis.geo.LocatedInAllPathsTableQuery;
import com.runwaysdk.gis.geometry.GeometryHelper;
import com.runwaysdk.query.AttributeReference;
import com.runwaysdk.query.CONCAT;
import com.runwaysdk.query.Coalesce;
import com.runwaysdk.query.F;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.query.SelectableChar;
import com.runwaysdk.query.SelectableUUID;
import com.runwaysdk.query.ValueQuery;
import com.runwaysdk.system.gis.geo.GeoEntity;
import com.runwaysdk.system.gis.geo.GeoEntityProblem;
import com.runwaysdk.system.gis.geo.GeoEntityProblemQuery;
import com.runwaysdk.system.gis.geo.GeoEntityProblemView;
import com.runwaysdk.system.gis.geo.GeoEntityQuery;
import com.runwaysdk.system.gis.geo.LocatedIn;
import com.runwaysdk.system.gis.geo.LocatedInQuery;
import com.runwaysdk.system.gis.geo.Synonym;
import com.runwaysdk.system.gis.geo.SynonymQuery;
import com.runwaysdk.system.gis.geo.SynonymRelationshipQuery;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;

import net.geoprism.ConfigurationIF;
import net.geoprism.ConfigurationService;
import net.geoprism.KeyGeneratorIF;
import net.geoprism.TermSynonymRelationship;
import net.geoprism.data.DatabaseUtil;
import net.geoprism.data.GeometrySerializationUtil;
import net.geoprism.data.importer.SeedKeyGenerator;

public class GeoEntityUtil extends GeoEntityUtilBase 
{
  private static final KeyGeneratorIF generator        = new SeedKeyGenerator();

  private static final long           serialVersionUID = -395452858;

  public GeoEntityUtil()
  {
    // Test
    super();
  }

  /**
   * Helper method, returns all GeoEntities that match the given criteria.
   * 
   * @param config
   * @return
   */
  private static OIterator<? extends GeoEntity> getAllGeoentities(String config)
  {
    JSONObject jObj = new JSONObject(config);

    String universalId = jObj.has("universalId") ? jObj.getString("universalId") : null;
    String parentOid = jObj.getString("oid");

    QueryFactory qf = new QueryFactory();
    GeoEntityQuery geq = new GeoEntityQuery(qf);
    LocatedInQuery liq = new LocatedInQuery(qf);

    if (universalId != null)
    {
      geq.WHERE(geq.getUniversal().EQ(universalId));
    }

    liq.WHERE(liq.getParent().EQ(parentOid));
    geq.AND(geq.locatedIn(liq));

    return geq.getIterator();
  }

  /**
   * MdMethod
   * 
   * Accepts a JSON configuration object (Similar to getData) and locks all geoentities to be edited.
   * 
   * @param config
   * @return VectorTile data
   */
  @Transaction
  public static java.io.InputStream openEditingSession(java.lang.String config)
  {
    OIterator<? extends GeoEntity> lockMes = getAllGeoentities(config);
    for (GeoEntity lockMe : lockMes)
    {
      lockMe.lock();
    }

    // Uncomment this if you want to play with vector tiles
    // return GeoEntityUtil.getData(config);

    JSONObject jObj = new JSONObject(config);
    String oid = jObj.getString("oid");
    String universalId = jObj.has("universalId") ? jObj.getString("universalId") : null;

    LocationTargetPublisher publisher = new LocationTargetPublisher(oid, universalId, "");

    StringWriter writer = new StringWriter();
    JSONWriter jw = new JSONWriter(writer);

    try
    {
      publisher.writeGeojson(jw);
      byte[] bytes = writer.toString().getBytes();

      return new ByteArrayInputStream(bytes);
    }
    finally
    {
      try
      {
        writer.close();
      }
      catch (IOException e)
      {
        throw new ProgrammingErrorException("Could not close IO stream.", e);
      }
    }
  }

  /**
   * MdMethod
   * 
   * Cancels an editing session by unlocking all GeoEntities at a given universal level.
   * 
   * @param config
   * @return VectorTile data
   */
  @Transaction
  public static void cancelEditingSession(java.lang.String config)
  {
    OIterator<? extends GeoEntity> unlockMes = getAllGeoentities(config);
    for (GeoEntity unlockMe : unlockMes)
    {
      unlockMe.unlock();
    }
  }

  /**
   * MdMethod
   * 
   * Accepts a JSON FeatureCollection from the client. This JSON is directly produced by the Mapbox GL Draw plugin.
   */
  @Transaction
  public static void applyGeometries(java.lang.String featureCollection)
  {
    GeometryFactory geometryFactory = new GeometryFactory();
    GeometryHelper geometryHelper = new GeometryHelper();
    GeometrySerializationUtil serializer = new GeometrySerializationUtil(geometryFactory);

    HashMap<String, List<Polygon>> multiPolyMap = new HashMap<String, List<Polygon>>();

    try
    {
      JSONObject json = new JSONObject(featureCollection);

      JSONArray features = json.getJSONArray("features");

      for (int i = 0; i < features.length(); ++i)
      {
        JSONObject feature = features.getJSONObject(i);

        String type = feature.getString("type").toLowerCase();

        if (type.equals("feature"))
        {
          JSONObject geometry = feature.getJSONObject("geometry");
          String featureType = geometry.getString("type").toLowerCase();

          JSONObject properties = feature.getJSONObject("properties");
          String geoEntId = properties.getString("oid");
          JSONArray coordinates = geometry.getJSONArray("coordinates");

          if (!multiPolyMap.containsKey(geoEntId))
          {
            multiPolyMap.put(geoEntId, new ArrayList<Polygon>());
          }
          List<Polygon> polygons = multiPolyMap.get(geoEntId);

          if (featureType.equals("polygon"))
          {
            coordinates = coordinates.getJSONArray(0);

            Polygon polygon = serializer.jsonToPolygon(coordinates);
            polygons.add(polygon);
          }
          else if (featureType.equals("multipolygon"))
          {
            for (int p = 0; p < coordinates.length(); ++p)
            {
              JSONArray jsonP = coordinates.getJSONArray(p).getJSONArray(0);

              Polygon polygon = serializer.jsonToPolygon(jsonP);
              polygons.add(polygon);
            }
          }
          else
          {
            throw new UnsupportedOperationException();
          }
        }
        else if (type.equals("unlock"))
        {
          GeoEntity.get(feature.getString("oid")).unlock();
        }
        else if (type.equals("delete"))
        {
          GeoEntity.get(feature.getString("oid")).delete();
        }
      }
    }
    catch (JSONException e)
    {
      e.printStackTrace();
    }

    Set<String> ids = multiPolyMap.keySet();
    for (String oid : ids)
    {
      List<Polygon> listPoly = multiPolyMap.get(oid);

      MultiPolygon multiPoly = geometryFactory.createMultiPolygon(listPoly.toArray(new Polygon[listPoly.size()]));

      // Make sure we're in 4326 projection
      Geometry geom = multiPoly;
      // try
      // {
      // CoordinateReferenceSystem sourceCRS = CRS.decode("EPSG:3857", true);
      // CoordinateReferenceSystem targetCRS = CRS.decode("EPSG:4326", true);
      // MathTransform transform = CRS.findMathTransform(sourceCRS, targetCRS);
      // geom = JTS.transform(multiPoly, transform);
      // }
      // catch (FactoryException | MismatchedDimensionException | TransformException e)
      // {
      // throw new RuntimeException(e);
      // }

      GeoEntity geo = GeoEntity.lock(oid);
      geo.setGeoPoint(geometryHelper.getGeoPoint(geom));
      geo.setGeoMultiPolygon(geometryHelper.getGeoMultiPolygon(geom));
      geo.setWkt(geom.toText());
      geo.apply();
    }
  }

  /**
   * Merges the source geo entity into the destination geo entity and creates a new synonym with the name of the source
   * geo entity.
   * 
   * @param sourceId
   * @param destinationId
   * 
   * @return Returns a list of all geo entity ids which may need to be refreshed in the browser
   */
  @Transaction
  public static String[] makeSynonym(String sourceId, String destinationId)
  {
    GeoEntity source = GeoEntity.get(sourceId);
    GeoEntity destination = GeoEntity.get(destinationId);

    LocatedInQuery query = new LocatedInQuery(new QueryFactory());
    query.WHERE(query.getChild().EQ(source));

    OIterator<? extends LocatedIn> iterator = query.getIterator();

    List<String> ids = new LinkedList<String>();

    try
    {
      while (iterator.hasNext())
      {
        LocatedIn locatedIn = iterator.next();

        ids.add(locatedIn.getParentOid());
      }
    }
    finally
    {
      iterator.close();
    }

    makeSynonym(source, destination);

    ids.add(destinationId);

    return ids.toArray(new String[ids.size()]);
  }

  @Transaction
  public static Synonym makeSynonym(GeoEntity source, GeoEntity destination)
  {
    /*
     * Ensure that the source destination doesn't have any geometry data
     */
    if (source.getGeoMultiPolygon() != null)
    {
      GeometrySynonymException exception = new GeometrySynonymException();
      exception.setEntityLabel(source.getDisplayLabel().getValue());

      throw exception;
    }

    // Delete all problems for the source geo entity so that they aren't transfered over to the destination entity
    GeoEntityProblemQuery problemQuery = new GeoEntityProblemQuery(new QueryFactory());
    problemQuery.WHERE(problemQuery.getGeoEntity().EQ(source));

    OIterator<? extends GeoEntityProblem> iterator = problemQuery.getIterator();

    try
    {
      while (iterator.hasNext())
      {
        GeoEntityProblem problem = iterator.next();
        problem.delete();
      }
    }
    finally
    {
      iterator.close();
    }

    Synonym synonym = createSynonym(destination, source.getDisplayLabel().getValue());

    /*
     * Log the original synonym value in the data records in case of a role back
     */
    if (synonym != null)
    {
      TermSynonymRelationship.logSynonymData(source, synonym.getOid(), GeoEntity.CLASS);
    }
    else
    {
      String label = source.getDisplayLabel().getValue();

      List<? extends Synonym> synonyms = GeoEntityUtil.getSynonyms(destination, label);

      for (Synonym existingSynonym : synonyms)
      {
        TermSynonymRelationship.logSynonymData(source, existingSynonym.getOid(), GeoEntity.CLASS);
      }
    }

    // Copy over any synonyms to the destination and delete the originals
    BusinessDAOIF sourceDAO = (BusinessDAOIF) BusinessFacade.getEntityDAO(source);

    /*
     * Remove the source from the allpaths table so we don't violate allpaths uniqueness constraints
     */
    List<? extends Business> parents = source.getParents(LocatedIn.CLASS).getAll();
    for (Business parent : parents)
    {
      source.removeLink((Term) parent, LocatedIn.CLASS);
    }
    GeoEntity.getStrategy().removeTerm(source, LocatedIn.CLASS);

    BusinessDAOFactory.floatObjectIdReferencesDatabase(sourceDAO.getBusinessDAO(), source.getOid(), destination.getOid(), true);

    source.delete();

    /*
     * Add the dest to the allpaths table
     */
    GeoEntity.getStrategy().add(destination, LocatedIn.CLASS);
    for (Business parent : parents)
    {
      Savepoint savepoint = Database.setSavepoint();

      try
      {
        destination.addLink((Term) parent, LocatedIn.CLASS);
      }
      catch (DuplicateDataException e)
      {
        // Rollback the savepoint
        Database.rollbackSavepoint(savepoint);

        savepoint = null;
      }
      finally
      {
        if (savepoint != null)
        {
          Database.releaseSavepoint(savepoint);
        }
      }
    }

    return synonym;
  }

  @Transaction
  public static String[] restoreSynonym(String synonymId)
  {
    List<String> ids = new LinkedList<String>();

    Synonym synonym = Synonym.get(synonymId);
    String value = synonym.getDisplayLabel().getValue();

    List<? extends GeoEntity> sources = synonym.getAllGeoEntity().getAll();

    for (GeoEntity source : sources)
    {
      GeoEntity entity = new GeoEntity();
      entity.setUniversal(source.getUniversal());
      entity.setGeoId(generator.generateKey(""));
      entity.getDisplayLabel().setValue(value);
      entity.apply();

      List<? extends GeoEntity> parents = source.getAllLocatedIn().getAll();

      for (GeoEntity parent : parents)
      {
        entity.addLink(parent, LocatedIn.CLASS);

        ids.add(parent.getOid());
      }

      /*
       * Restore the original value in the data records in case of a role back
       */
      TermSynonymRelationship.restoreSynonymData(entity, synonym.getOid(), GeoEntity.CLASS);
    }

    synonym.delete();

    return ids.toArray(new String[ids.size()]);
  }

  private static Synonym createSynonym(GeoEntity destination, String synonymName)
  {
    Savepoint savepoint = Database.setSavepoint();

    try
    {
      Synonym synonym = new Synonym();
      synonym.getDisplayLabel().setValue(synonymName);

      Synonym.create(synonym, destination.getOid());

      return synonym;
    }
    catch (DuplicateDataException e)
    {
      // Rollback the savepoint
      Database.rollbackSavepoint(savepoint);

      savepoint = null;
    }
    finally
    {
      if (savepoint != null)
      {
        Database.releaseSavepoint(savepoint);
      }
    }

    return null;
  }

  public static String getGeoEntityTree(String geoEntityId)
  {
    GeoEntity root = GeoEntity.getRoot();

    GeoEntity entity = GeoEntity.get(geoEntityId);

    OIterator<Term> anscestorIt = entity.getAllAncestors(LocatedIn.CLASS);
    Set<String> ids = new HashSet<String>();

    try
    {
      while (anscestorIt.hasNext())
      {
        GeoEntity ancestor = (GeoEntity) anscestorIt.next();

        if (!ancestor.getOid().equals(root.getOid()))
        {
          ids.add(ancestor.getOid());
        }
      }
    }
    finally
    {
      anscestorIt.close();
    }

    JSONArray array = new JSONArray();

    QueryFactory factory = new QueryFactory();

    LocatedInQuery lQuery = new LocatedInQuery(factory);
    lQuery.WHERE(lQuery.getParent().EQ(root));
    lQuery.AND(lQuery.childOid().IN(ids.toArray(new String[ids.size()])));

    GeoEntityQuery query = new GeoEntityQuery(factory);
    query.WHERE(query.locatedIn(lQuery));
    query.ORDER_BY_ASC(query.getDisplayLabel().localize());

    OIterator<? extends GeoEntity> entityIt = query.getIterator();

    try
    {
      while (entityIt.hasNext())
      {
        GeoEntity parent = entityIt.next();

        array.put(GeoEntityUtil.getJSONObject(parent, ids, true));
      }
    }
    finally
    {
      entityIt.close();
    }

    return array.toString();
  }

  /**
   * Returns the JSONObject representation of the node and all of its children nodes
   * 
   * @param _entity
   *          Entity to serialize into a JSONObject
   * @param _ids
   *          List of entity ids in which to include the children in the serialization
   * @param _isRoot
   *          Flag indicating if the entity represents a root node of the tree
   * @return
   */
  private static JSONObject getJSONObject(GeoEntity _entity, Set<String> _ids, boolean _isRoot)
  {
    try
    {
      JSONArray children = new JSONArray();

      if (_ids.contains(_entity.getOid()))
      {
        OIterator<? extends LocatedIn> iterator = null;

        try
        {
          // Get the relationships where this object is the parent

          LocatedInQuery query = new LocatedInQuery(new QueryFactory());
          query.WHERE(query.getParent().EQ(_entity));
          query.ORDER_BY_ASC( ( (AttributeReference) query.getChild() ).aLocalCharacter(GeoEntity.DISPLAYLABEL).localize());
          iterator = query.getIterator();

          List<? extends LocatedIn> relationships = iterator.getAll();

          for (LocatedIn relationship : relationships)
          {
            GeoEntity child = relationship.getChild();

            JSONObject parentRecord = new JSONObject();
            parentRecord.put("parentOid", relationship.getParentOid());
            parentRecord.put("relId", relationship.getOid());
            parentRecord.put("relType", LocatedIn.CLASS);

            JSONObject object = GeoEntityUtil.getJSONObject(child, _ids, false);
            object.put("parentRecord", parentRecord);

            children.put(object);
          }

        }
        finally
        {
          if (iterator != null)
          {
            iterator.close();
          }
        }
      }

      String label = _entity.getDisplayLabel().getValue();

      if (!_isRoot)
      {
        label += " [" + _entity.getUniversal().getDisplayLabel().getValue() + "]";
      }

      JSONObject object = new JSONObject();
      object.put("label", label);
      object.put("oid", _entity.getOid());
      object.put("type", _entity.getType());
      object.put("children", children);
      object.put("fetched", ( children.length() > 0 ));

      return object;
    }
    catch (JSONException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

  public static GeoEntityProblemView[] getAllProblems()
  {
    List<GeoEntityProblemView> list = new LinkedList<GeoEntityProblemView>();

    GeoEntityProblemQuery query = new GeoEntityProblemQuery(new QueryFactory());
    query.ORDER_BY_DESC(query.getProblemType().getDisplayLabel().localize());
    query.ORDER_BY_ASC(query.getGeoEntity().getDisplayLabel().localize());

    OIterator<? extends GeoEntityProblem> iterator = query.getIterator();

    try
    {
      while (iterator.hasNext())
      {
        GeoEntityProblem problem = iterator.next();

        list.addAll(problem.getViews());
      }
    }
    finally
    {
      iterator.close();
    }

    return list.toArray(new GeoEntityProblemView[list.size()]);
  }

  @Transaction
  public static void deleteEntityProblem(String problemId)
  {
    GeoEntityProblem problem = GeoEntityProblem.get(problemId);
    problem.delete();
  }

  public static GeoEntity[] getOrderedAncestors(String oid)
  {
    GeoEntity root = GeoEntity.getRoot();
    GeoEntity entity = GeoEntity.get(oid);

    Collection<Term> ancestors = GeoEntityUtil.getOrderedAncestors(root, entity, LocatedIn.CLASS);

    return ancestors.toArray(new GeoEntity[ancestors.size()]);
  }

  public static Collection<Term> getOrderedAncestors(Term root, Term term, String relationship)
  {
    Map<String, Term> map = new LinkedHashMap<String, Term>();

    OIterator<Term> iterator = term.getDirectAncestors(relationship);
    try
    {
      List<Term> parents = iterator.getAll();

      for (Term parent : parents)
      {
        if (!parent.getOid().equals(root.getOid()))
        {
          Collection<Term> ancestors = GeoEntityUtil.getOrderedAncestors(root, parent, relationship);

          for (Term ancestor : ancestors)
          {
            if (!map.containsKey(ancestor.getOid()))
            {
              map.put(ancestor.getOid(), ancestor);
            }
          }
        }
      }

      map.put(term.getOid(), term);
    }
    finally
    {
      iterator.close();
    }

    return map.values();
  }

  public static Collection<Term> getOrderedDescendants(Term term, String relationship)
  {
    Map<String, Term> map = new LinkedHashMap<String, Term>();

    OIterator<Term> iterator = term.getDirectDescendants(relationship);

    try
    {
      map.put(term.getOid(), term);

      List<Term> parents = iterator.getAll();

      for (Term parent : parents)
      {
        Collection<Term> descendants = GeoEntityUtil.getOrderedDescendants(parent, relationship);

        for (Term descendant : descendants)
        {
          if (!map.containsKey(descendant.getOid()))
          {
            map.put(descendant.getOid(), descendant);
          }
        }
      }
    }
    finally
    {
      iterator.close();
    }

    return map.values();
  }

  public static String getEntityLabel(String entityId)
  {
    GeoEntity entity = GeoEntity.get(entityId);

    return GeoEntityUtil.getEntityLabel(entity);
  }

  public static String getEntityLabel(GeoEntity entity)
  {
    if (entity != null)
    {
      StringBuffer buffer = new StringBuffer();
      buffer.append(entity.getDisplayLabel().getValue());
      buffer.append(" (" + entity.getUniversal().getDisplayLabel().getValue() + ")");
      buffer.append(" : " + entity.getGeoId());

      return buffer.toString();
    }

    return "";
  }

  public static GeoEntity getCountryByUniversal(String universalId)
  {
    GeoEntityQuery query = new GeoEntityQuery(new QueryFactory());
    query.WHERE(query.getUniversal().EQ(universalId));

    if (query.getCount() == 1)
    {
      OIterator<? extends GeoEntity> iterator = query.getIterator();

      try
      {
        GeoEntity entity = iterator.next();

        return entity;
      }
      finally
      {
        iterator.close();
      }
    }

    throw new ProgrammingErrorException("Country universal has more than one corresponding geo entity");
  }

  public static ValueQuery getGeoEntitySuggestions(String parentOid, String universalId, String text, Integer limit)
  {
    ValueQuery query = new ValueQuery(new QueryFactory());

    GeoEntityQuery entityQuery = new GeoEntityQuery(query);

    SelectableUUID oid = entityQuery.getOid();
    Coalesce universalLabel = entityQuery.getUniversal().getDisplayLabel().localize();
    Coalesce geoLabel = entityQuery.getDisplayLabel().localize();
    SelectableChar geoId = entityQuery.getGeoId();

    CONCAT label = F.CONCAT(F.CONCAT(F.CONCAT(F.CONCAT(geoLabel, " ("), F.CONCAT(universalLabel, ")")), " : "), geoId);
    label.setColumnAlias(GeoEntity.DISPLAYLABEL);
    label.setUserDefinedAlias(GeoEntity.DISPLAYLABEL);
    label.setUserDefinedDisplayLabel(GeoEntity.DISPLAYLABEL);

    query.SELECT(oid, label);
    query.WHERE(label.LIKEi("%" + text + "%"));

    if (universalId != null && universalId.length() > 0)
    {
      query.AND(entityQuery.getUniversal().EQ(universalId));
    }

    if (parentOid != null && parentOid.length() > 0)
    {
      LocatedInAllPathsTableQuery aptQuery = new LocatedInAllPathsTableQuery(query);

      query.AND(entityQuery.EQ(aptQuery.getChildTerm()));
      query.AND(aptQuery.getParentTerm().EQ(parentOid));
    }

    query.ORDER_BY_ASC(geoLabel);

    query.restrictRows(limit, 1);

    return query;
  }

  public static List<Synonym> getSynonyms(GeoEntity destination, String label)
  {
    QueryFactory factory = new QueryFactory();

    SynonymRelationshipQuery rQuery = new SynonymRelationshipQuery(factory);
    rQuery.WHERE(rQuery.parentOid().EQ(destination.getOid()));

    SynonymQuery sQuery = new SynonymQuery(factory);
    sQuery.WHERE(sQuery.geoEntity(rQuery));
    sQuery.AND(sQuery.getDisplayLabel().localize().EQ(label));

    OIterator<? extends Synonym> iterator = null;

    try
    {
      iterator = sQuery.getIterator();

      return new LinkedList<Synonym>(iterator.getAll());
    }
    finally
    {
      if (iterator != null)
      {
        iterator.close();
      }
    }
  }

  public static String getAncestorsAsJSON(String entityId)
  {
    try
    {
      GeoEntity root = GeoEntity.getRoot();
      GeoEntity entity = GeoEntity.get(entityId);

      JSONArray array = new JSONArray();

      List<Term> ancestors = entity.getAllAncestors(LocatedIn.CLASS).getAll();

      Collections.reverse(ancestors);

      for (Term ancestor : ancestors)
      {
        if (!ancestor.getOid().equals(root.getOid()))
        {
          JSONObject object = new JSONObject();
          object.put("label", ancestor.getDisplayLabel().getValue());

          array.put(object);
        }
      }

      return array.toString();
    }
    catch (JSONException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }
  
  public static String getEntitiesBBOX(String[] ids)
  {
	  if(ids.length > 0)
	  {
		  StringBuffer geoIdsStr = new StringBuffer();
		  for(int i=0; i<ids.length; i++)
		  {
			  String oid = ids[i];
			  
			  if(i > 0){
				  geoIdsStr.append(",");
			  }
			  geoIdsStr.append("'").append(oid).append("'");
		  }
		  
		  
		  ValueQuery vQuery = new ValueQuery(new QueryFactory());
		  GeoEntityQuery query = new GeoEntityQuery(vQuery);
	
		  StringBuffer sql = new StringBuffer();
		  sql.append("SELECT ST_AsText(ST_Extent(" + query.getGeoMultiPolygon().getDbColumnName() + ")) AS bbox");
		  sql.append(" FROM geo_entity WHERE geo_entity.oid = any (array["+geoIdsStr+"]);");
		  
		    	
		  ResultSet bboxResult = Database.query(sql.toString());
	
		  return formatBBox(bboxResult).toString();
	  }
	  
	  return null;
  }
  
  
  public static String getChildrenBBOX(String oid, String universalId)
  {
    GeoEntity entity = GeoEntity.get(oid);

    ValueQuery vQuery = new ValueQuery(new QueryFactory());
    LocatedInQuery liQuery = new LocatedInQuery(vQuery);
    GeoEntityQuery query = new GeoEntityQuery(vQuery);

    StringBuffer sql = new StringBuffer();
    sql.append("SELECT ST_AsText(ST_Extent(" + query.getGeoMultiPolygon().getDbColumnName() + ")) AS bbox");
    sql.append(" FROM universal, geo_entity, located_in WHERE geo_entity.universal = universal.oid");
    sql.append(" AND ((located_in.parent_oid ='" + entity.getOid() + "'");
    sql.append(" AND geo_entity.oid = located_in.child_oid)");
    sql.append(" AND geo_entity.universal = '" + universalId + "');");
    
    ResultSet bboxResult = Database.query(sql.toString());

    return formatBBox(bboxResult).toString();
  }
  
  
  /**
   * Format the bounding box result set returned from a PostGIS database query
   * 
   * @param resultSet
   * @return
   */
  public static JSONArray formatBBox(ResultSet resultSet)
  {
    JSONArray bboxArr = new JSONArray();

    try
    {
      if (resultSet.next())
      {
        String bbox = resultSet.getString("bbox");
        if (bbox != null)
        {
          Pattern p = Pattern.compile("POLYGON\\(\\((.*)\\)\\)");
          Matcher m = p.matcher(bbox);

          if (m.matches())
          {
            String coordinates = m.group(1);
            List<Coordinate> coords = new LinkedList<Coordinate>();

            for (String c : coordinates.split(","))
            {
              String[] xAndY = c.split(" ");
              double x = Double.valueOf(xAndY[0]);
              double y = Double.valueOf(xAndY[1]);

              coords.add(new Coordinate(x, y));
            }

            Envelope e = new Envelope(coords.get(0), coords.get(2));

            try
            {
              bboxArr.put(e.getMinX());
              bboxArr.put(e.getMinY());
              bboxArr.put(e.getMaxX());
              bboxArr.put(e.getMaxY());
            }
            catch (JSONException ex)
            {
              throw new ProgrammingErrorException(ex);
            }
          }
          else
          {
            String error = "Could not create a valid bounding box";

            throw new ProgrammingErrorException(error);
          }
        }
      }
    }
    catch (SQLException sqlEx1)
    {
      Database.throwDatabaseException(sqlEx1);
    }
    finally
    {
      try
      {
        java.sql.Statement statement = resultSet.getStatement();
        resultSet.close();
        statement.close();
      }
      catch (SQLException sqlEx2)
      {
        Database.throwDatabaseException(sqlEx2);
      }
    }

    return bboxArr;
  }
  

  public static ValueQuery getChildren(String oid, String universalId, Integer limit)
  {
    GeoEntity entity = GeoEntity.get(oid);

    ValueQuery vQuery = new ValueQuery(new QueryFactory());
    LocatedInQuery liQuery = new LocatedInQuery(vQuery);
    GeoEntityQuery query = new GeoEntityQuery(vQuery);

    Coalesce label = query.getDisplayLabel().localize(GeoEntity.DISPLAYLABEL);

    vQuery.SELECT(query.oid());
    vQuery.SELECT(label);
    vQuery.SELECT(query.getGeoId());
    vQuery.SELECT(query.getUniversal().getDisplayLabel().localize(GeoEntity.UNIVERSAL));
    vQuery.WHERE(liQuery.parentOid().EQ(entity.getOid()));
    vQuery.AND(query.locatedIn(liQuery));

    if (universalId != null)
    {
      vQuery.AND(query.getUniversal().EQ(universalId));
    }

    vQuery.ORDER_BY_ASC(label);

    if (limit != null)
    {
      vQuery.restrictRows(limit, 1);
    }
    
    return vQuery;
  }

  public static GeoEntity getEntity(String oid)
  {
    if (oid == null)
    {
      try
      {
        List<ConfigurationIF> configurations = ConfigurationService.getConfigurations();

        for (ConfigurationIF configuration : configurations)
        {
          GeoEntity entity = configuration.getDefaultGeoEntity();

          if (entity != null)
          {
            return entity;
          }
        }
      }
      catch (Exception e)
      {
        // Ignore and just return the first child of root
      }

      GeoEntity root = GeoEntity.getRoot();
      OIterator<? extends Business> children = root.getChildren(LocatedIn.CLASS);

      try
      {
        if (children.hasNext())
        {
          return (GeoEntity) children.next();
        }
        else
        {
          throw new RuntimeException("No entities have been defined");
        }
      }
      finally
      {
        children.close();
      }
    }

    return GeoEntity.get(oid);
  }

  public static String publishLayers(String oid, String universalId, String existingLayerNames)
  {
    LocationTargetPublisher publisher = new LocationTargetPublisher(oid, universalId, existingLayerNames);

    StringWriter writer = new StringWriter();
    JSONWriter jw = new JSONWriter(writer);

    try
    {
      publisher.writeGeojson(jw);

      return writer.toString();
    }
    finally
    {
      try
      {
        writer.close();
      }
      catch (IOException e)
      {
        throw new ProgrammingErrorException("Could not close IO stream.", e);
      }
    }

  }

  @Transaction
  public static void refreshViews(String layers)
  {
    try
    {
      if (layers != null)
      {
        JSONArray deserialized = new JSONArray(layers);

        for (int i = 0; i < deserialized.length(); i++)
        {
          JSONObject layer = deserialized.getJSONObject(i);
          String layerName = layer.getString("layerName");

          DatabaseUtil.refreshView(layerName);
        }
      }
    }
    catch (JSONException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

  public static InputStream getData(String config)
  {
    try
    {
      JSONObject object = new JSONObject(config);
      String type = object.getString("type");

      List<ConfigurationIF> configurations = ConfigurationService.getConfigurations();

      for (ConfigurationIF configuration : configurations)
      {
        if (configuration.hasLocationData(type))
        {
          return configuration.getLocationData(type, object);
        }
      }

      throw new ProgrammingErrorException("Unsupported type [" + type + "]");
    }
    catch (Exception e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

  @Transaction
  @Authenticate
  public static void deleteGeoEntity(String oid)
  {
    GeoEntity entity = GeoEntity.get(oid);

    List<ConfigurationIF> configurations = ConfigurationService.getConfigurations();

    for (ConfigurationIF configuration : configurations)
    {
      configuration.onEntityDelete(entity);
    }

    entity.delete();
  }
}
