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

import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.geoprism.ContentStream;
import net.geoprism.MappableClass;
import net.geoprism.ontology.Classifier;
import net.geoprism.ontology.ClassifierIsARelationship;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.business.ontology.Term;
import com.runwaysdk.dataaccess.MdAttributeTermDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.metadata.MdAttributeTermDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;

public class ProblemResponse implements ImportResponseIF
{
  private Collection<ImportProblemIF> problems;

  private SourceContextIF             sContext;

  private TargetContextIF             tContext;

  public ProblemResponse(Collection<ImportProblemIF> problems, SourceContextIF sContext, TargetContextIF tContext)
  {
    this.problems = problems;
    this.sContext = sContext;
    this.tContext = tContext;
  }

  public String getJSON()
  {
    try
    {
      JSONObject object = this.toJSON();

      return object.toString();
    }
    catch (JSONException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

  public JSONObject toJSON() throws JSONException
  {
    /*
     * Return a JSONArray of the datasets which were created a part of the import. Do not include datasets which have
     * already been created.
     */
    JSONArray datasets = new JSONArray();

    List<TargetDefinitionIF> definitions = tContext.getDefinitions();

    for (TargetDefinitionIF definition : definitions)
    {
      if (definition.isNew())
      {
        String type = definition.getTargetType();

        MdBusinessDAOIF mdBusiness = MdBusinessDAO.getMdBusinessDAO(type);
        MappableClass mClass = MappableClass.getMappableClass(mdBusiness);

        datasets.put(mClass.toJSON());
      }
    }

    JSONObject object = new JSONObject();
    object.put("success", false);
    object.put("sheets", this.getSheetsJSON());
    object.put("problems", this.getProblemsJSON());
    object.put("datasets", datasets);

    return object;
  }

  @Override
  public boolean hasProblems()
  {
    return true;
  }

  private JSONObject getProblemsJSON() throws JSONException
  {
    Map<String, List<JSONObject>> map = new HashMap<String, List<JSONObject>>();
    map.put(LocationProblem.TYPE, new LinkedList<JSONObject>());
    map.put(CategoryProblem.TYPE, new LinkedList<JSONObject>());

    JSONObject options = new JSONObject();

    for (ImportProblemIF problem : this.problems)
    {
      map.putIfAbsent(problem.getType(), new LinkedList<JSONObject>());

      map.get(problem.getType()).add(problem.toJSON());

      if (problem instanceof CategoryProblem)
      {
        CategoryProblem cProblem = (CategoryProblem) problem;

        /*
         * Load all of the options for this attribute
         */
        if (!options.has(cProblem.getMdAttributeId()))
        {
          // Serialized JSON array of all the classifier options for this mdAttribute
          JSONArray array = new JSONArray();

          MdAttributeTermDAOIF mdAttributeTerm = MdAttributeTermDAO.get(cProblem.getMdAttributeId());
          Classifier root = Classifier.findClassifierRoot(mdAttributeTerm);

          List<Term> children = root.getAllDescendants(ClassifierIsARelationship.CLASS).getAll();

          Collections.sort(children, new Comparator<Term>()
          {
            @Override
            public int compare(Term o1, Term o2)
            {
              return o1.getDisplayLabel().getValue().compareTo(o2.getDisplayLabel().getValue());
            }
          });

          for (Term child : children)
          {
            JSONObject option = new JSONObject();
            option.put("label", child.getDisplayLabel().getValue());
            option.put("id", child.getId());

            array.put(option);
          }

          options.put(cProblem.getMdAttributeId(), array);
        }

      }
    }

    JSONObject object = new JSONObject(map);
    object.put("options", options);

    return object;
  }

  private JSONArray getSheetsJSON() throws JSONException
  {
    JSONArray sheets = new JSONArray();

    Collection<SourceDefinitionIF> definitions = this.sContext.getDefinitions();

    for (SourceDefinitionIF sDefinition : definitions)
    {
      TargetDefinitionIF tDefinition = this.tContext.getDefinition(sDefinition.getType());

      DefinitionBuilder builder = new DefinitionBuilder(sDefinition, tDefinition);
      JSONObject sheet = builder.getConfiguration();

      sheets.put(sheet);
    }
    return sheets;
  }

  @Override
  public ContentStream getStream()
  {
    byte[] bytes = this.getJSON().getBytes(Charset.forName("UTF-8"));

    return new ContentStream(new ByteArrayInputStream(bytes), "application/json");
  }
}
