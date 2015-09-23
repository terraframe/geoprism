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
package com.runwaysdk.geodashboard.ontology;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.business.Relationship;
import com.runwaysdk.business.ontology.OntologyStrategyIF;
import com.runwaysdk.business.ontology.Term;
import com.runwaysdk.business.ontology.TermAndRel;
import com.runwaysdk.dataaccess.MdAttributeTermDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.OR;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.system.metadata.ontology.DatabaseAllPathsStrategy;

public class Classifier extends ClassifierBase implements com.runwaysdk.generation.loader.Reloadable
{
  private static final long   serialVersionUID = 1158111601;

  private static final String KEY_CONCATENATOR = ".";

  public Classifier()
  {
    super();
  }

  /**
   * Persists this Classifier, performs validation on its values, and generates a UniqueId from the display label, if
   * necessary.
   */
  @Override
  @Transaction
  public void apply()
  {
    // If they didn't specify a UniqueId we can figure one out from the DisplayLabel
    if (this.getClassifierId() == null || this.getClassifierId().length() == 0)
    {
      String uniqueId = this.getDisplayLabel().getValue().trim().replaceAll("\\s+", "");
      this.setClassifierId(uniqueId);
    }

    super.apply();

    if (this.isNew())
    {
      this.addTerm(ClassifierIsARelationship.CLASS);
    }
  }

  public static String buildKey(String pkg, String id)
  {
    return pkg + KEY_CONCATENATOR + id;
  }

  @Override
  public String buildKey()
  {
    return Classifier.buildKey(this.getClassifierPackage(), this.getClassifierId());
  }

  /**
   * Returns the <code>Classifier</code> object with a label or synonym that matches the given term. Searches all nodes
   * that are children of the given attribute root nodes including the root nodes.
   * 
   * @param sfTermToMatch
   * @param mdAttributeTermDAO
   * @return the <code>Classifier</code> object with a label or synonym that matches the given term.
   */
  public static Classifier findMatchingTerm(String sfTermToMatch, MdAttributeTermDAOIF mdAttributeTermDAOIF)
  {
    QueryFactory qf = new QueryFactory();

    ClassifierQuery classifierRootQ = new ClassifierQuery(qf);
    ClassifierTermAttributeRootQuery carQ = new ClassifierTermAttributeRootQuery(qf);
    ClassifierQuery classifierQ = new ClassifierQuery(qf);
    ClassifierAllPathsTableQuery allPathsQ = new ClassifierAllPathsTableQuery(qf);
    ClassifierSynonymQuery synonymQ = new ClassifierSynonymQuery(qf);

    carQ.WHERE(carQ.getParent().EQ(mdAttributeTermDAOIF));

    classifierRootQ.WHERE(classifierRootQ.classifierTermAttributeRoots(carQ));

    allPathsQ.WHERE(allPathsQ.getParentTerm().EQ(classifierRootQ));

    synonymQ.WHERE(synonymQ.getDisplayLabel().localize().EQ(sfTermToMatch));

    classifierQ.WHERE(OR.get(classifierQ.getDisplayLabel().localize().EQ(sfTermToMatch), classifierQ.hasSynonym(synonymQ)).AND(classifierQ.EQ(allPathsQ.getChildTerm())));
    
    OIterator<? extends Classifier> i = classifierQ.getIterator();
    try
    {
      for (Classifier classifier : i)
      {
        return classifier;
      }
    }
    finally
    {
      i.close();
    }
    return null;
  }

  /**
   * MdMethod used for creating Classifiers.
   * 
   * @param termId
   * @param name
   * @return
   */
  @Transaction
  public static TermAndRel create(Classifier dto, String parentId)
  {
    Classifier parent = Classifier.get(parentId);

    // If they didn't specify a package we can attempt to figure one out for them.
    if (dto.getClassifierPackage() == null || dto.getClassifierPackage().length() == 0)
    {
      String camelUniqueId = StringUtils.uncapitalize(parent.getClassifierId().trim().replaceAll("\\s+", ""));

      if (Classifier.getRoot().equals(parent))
      {
        dto.setClassifierPackage(camelUniqueId);
      }
      else
      {
        dto.setClassifierPackage(parent.getClassifierPackage() + KEY_CONCATENATOR + camelUniqueId);
      }
    }

    dto.apply();

    Relationship rel = dto.addLink(parent, ClassifierIsARelationship.CLASS);

    return new TermAndRel(dto, ClassifierIsARelationship.CLASS, rel.getId());
  }

  public static OntologyStrategyIF createStrategy()
  {
    return new DatabaseAllPathsStrategy();
  }

  public static Classifier getRoot()
  {
    return (Classifier) Term.getRoot(Classifier.CLASS);
  }

  /**
   * Returns the JSONObject representation of the node and all of its children nodes
   * 
   * @return
   */
  public JSONObject getJSONObject()
  {
    try
    {
      JSONArray children = new JSONArray();
      OIterator<Term> iterator = null;

      try
      {
        iterator = this.getDirectDescendants(ClassifierIsARelationship.CLASS);

        List<Term> terms = iterator.getAll();

        for (Term term : terms)
        {
          Classifier child = (Classifier) term;

          children.put(child.getJSONObject());
        }

      }
      finally
      {
        if (iterator != null)
        {
          iterator.close();
        }
      }

      JSONObject object = new JSONObject();
      object.put("label", this.getDisplayLabel().getValue());
      object.put("id", this.getId());
      object.put("type", this.getType());
      object.put("children", children);
      object.put("fetched", true);

      return object;
    }
    catch (JSONException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

  /**
   * Finds the classifier with the given label for the given term attribute. If the classifier does not exist, then it
   * is created.
   * 
   * @param classifierLabel
   * @param mdAttributeTermDAO
   * @return
   */
  @Transaction
  public static Classifier findClassifierAddIfNotExist(String packageString, String classifierLabel, MdAttributeTermDAOIF mdAttributeTermDAO)
  {
    Classifier classifier = findMatchingTerm(classifierLabel, mdAttributeTermDAO);
  
    if (classifier == null)
    {
      classifier = new Classifier();
      classifier.getDisplayLabel().setDefaultValue(classifierLabel);
      classifier.setClassifierId(classifierLabel);
      classifier.setClassifierPackage(packageString);
      classifier.apply();
  
      QueryFactory qf = new QueryFactory();
  
      ClassifierQuery classifierRootQ = new ClassifierQuery(qf);
      ClassifierTermAttributeRootQuery carQ = new ClassifierTermAttributeRootQuery(qf);
  
      carQ.WHERE(carQ.parentId().EQ(mdAttributeTermDAO.getId()));
  
      classifierRootQ.WHERE(classifierRootQ.classifierTermAttributeRoots(carQ));
  
      OIterator<? extends Classifier> i = classifierRootQ.getIterator();
      try
      {
        for (Classifier classifierRoot : i)
        {
          classifier.addLink(classifierRoot, ClassifierIsARelationship.CLASS);
        }
      }
      finally
      {
        i.close();
      }
    }
  
    return classifier;
  }
}
