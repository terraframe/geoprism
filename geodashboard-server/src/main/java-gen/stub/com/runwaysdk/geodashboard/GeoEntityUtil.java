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
package com.runwaysdk.geodashboard;

import java.sql.Savepoint;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.business.BusinessFacade;
import com.runwaysdk.business.Relationship;
import com.runwaysdk.business.ontology.Term;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.DuplicateDataException;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.database.BusinessDAOFactory;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.system.gis.geo.GeoEntity;
import com.runwaysdk.system.gis.geo.GeoEntityProblem;
import com.runwaysdk.system.gis.geo.GeoEntityProblemQuery;
import com.runwaysdk.system.gis.geo.GeoEntityProblemView;
import com.runwaysdk.system.gis.geo.GeoEntityQuery;
import com.runwaysdk.system.gis.geo.LocatedIn;
import com.runwaysdk.system.gis.geo.LocatedInQuery;
import com.runwaysdk.system.gis.geo.Synonym;
import com.runwaysdk.system.gis.geo.SynonymRelationship;
import com.runwaysdk.system.gis.geo.SynonymRelationshipQuery;

public class GeoEntityUtil extends GeoEntityUtilBase implements com.runwaysdk.generation.loader.Reloadable
{
  private static final long serialVersionUID = -395452858;

  public GeoEntityUtil()
  {
    super();
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

        ids.add(locatedIn.getParentId());
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
    // Copy over all synonyms
    SynonymRelationshipQuery query = new SynonymRelationshipQuery(new QueryFactory());
    query.WHERE(query.getParent().EQ(source));

    OIterator<? extends SynonymRelationship> it = query.getIterator();

    try
    {
      while (it.hasNext())
      {
        SynonymRelationship sRelationship = it.next();

        Synonym sSynonymn = sRelationship.getChild();
        String synonymName = sSynonymn.getDisplayLabel().getValue();

        createSynonym(destination, synonymName);

        sSynonymn.delete();
      }
    }
    finally
    {
      it.close();
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

    // Copy over any synonyms to the destination and delete the originals
    BusinessDAOIF sourceDAO = (BusinessDAOIF) BusinessFacade.getEntityDAO(source);

    BusinessDAOFactory.floatObjectIdReferences(sourceDAO.getBusinessDAO(), source.getId(), destination.getId(), true);

    Synonym synonym = createSynonym(destination, source.getDisplayLabel().getValue());

    source.delete();

    return synonym;
  }

  private static Synonym createSynonym(GeoEntity destination, String synonymName)
  {
    Savepoint savepoint = Database.setSavepoint();

    try
    {
      Synonym synonym = new Synonym();
      synonym.getDisplayLabel().setValue(synonymName);

      Synonym.create(synonym, destination.getId());

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

        if (!ancestor.getId().equals(root.getId()))
        {
          ids.add(ancestor.getId());
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
    lQuery.AND(lQuery.childId().IN(ids.toArray(new String[ids.size()])));

    GeoEntityQuery query = new GeoEntityQuery(factory);
    query.WHERE(query.locatedIn(lQuery));

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

      if (_ids.contains(_entity.getId()))
      {
        OIterator<? extends Relationship> iterator = null;

        try
        {
          iterator = _entity.getChildRelationships(LocatedIn.CLASS);

          List<? extends Relationship> relationships = iterator.getAll();

          for (Relationship relationship : relationships)
          {
            GeoEntity child = (GeoEntity) relationship.getChild();

            JSONObject parentRecord = new JSONObject();
            parentRecord.put("parentId", relationship.getParentId());
            parentRecord.put("relId", relationship.getId());
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
      object.put("id", _entity.getId());
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

  public static Collection<Term> getOrderedAncestors(Term root, Term term, String relationship)
  {
    Map<String, Term> map = new LinkedHashMap<String, Term>();

    OIterator<Term> iterator = term.getDirectAncestors(relationship);
    try
    {
      List<Term> parents = iterator.getAll();

      for (Term parent : parents)
      {
        if (!parent.getId().equals(root.getId()))
        {
          Collection<Term> ancestors = GeoEntityUtil.getOrderedAncestors(root, parent, relationship);

          for (Term ancestor : ancestors)
          {
            if (!map.containsKey(ancestor.getId()))
            {
              map.put(ancestor.getId(), ancestor);
            }
          }
        }
      }

      map.put(term.getId(), term);
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
      map.put(term.getId(), term);

      List<Term> parents = iterator.getAll();

      for (Term parent : parents)
      {
        Collection<Term> descendants = GeoEntityUtil.getOrderedDescendants(parent, relationship);

        for (Term descendant : descendants)
        {
          if (!map.containsKey(descendant.getId()))
          {
            map.put(descendant.getId(), descendant);
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

}
