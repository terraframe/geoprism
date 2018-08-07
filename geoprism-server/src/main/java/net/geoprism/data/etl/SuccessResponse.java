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
import java.util.Map.Entry;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.business.ontology.Term;
import com.runwaysdk.dataaccess.MdAttributeTermDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.metadata.MdAttributeTermDAO;

import net.geoprism.ContentStream;
import net.geoprism.ontology.Classifier;
import net.geoprism.ontology.ClassifierIsARelationship;

public class SuccessResponse implements ImportResponseIF
{

  private int                         total;

  private int                         failures;

  private String                      fileId;

  private Collection<ImportProblemIF> problems;

  private JSONArray                   datasets;

  private SourceContextIF             sContext;

  private TargetContextIF             tContext;

  public SuccessResponse(SourceContextIF sContext, TargetContextIF tContext)
  {
    this.problems = new LinkedList<>();
    this.sContext = sContext;
    this.tContext = tContext;
  }

  public JSONObject toJSON() throws JSONException
  {
    JSONObject object = new JSONObject();
    object.put("success", true);
    object.put("datasets", datasets);
    object.put("total", this.total);
    object.put("failures", this.failures);

    if (this.problems.size() > 0)
    {
      object.put("problems", this.getProblemsJSON());
      object.put("sheets", this.getSheetsJSON());
    }

    if (this.fileId != null)
    {
      object.put("fileId", this.fileId);
    }

    return object;
  }

  public JSONObject getProblemsJSON() throws JSONException
  {
    Map<String, JSONArray> map = new HashMap<String, JSONArray>();
    map.put(LocationProblem.TYPE, new JSONArray());
    map.put(CategoryProblem.TYPE, new JSONArray());

    JSONObject options = new JSONObject();

    for (ImportProblemIF problem : this.problems)
    {
      map.putIfAbsent(problem.getType(), new JSONArray());

      map.get(problem.getType()).put(problem.toJSON());
      
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

    JSONObject object = new JSONObject();
    object.put("options", options);

    Set<Entry<String, JSONArray>> entries = map.entrySet();

    for (Entry<String, JSONArray> entry : entries)
    {
      object.put(entry.getKey(), entry.getValue());
    }

    return object;
  }

  @Override
  public boolean hasProblems()
  {
    return this.problems.size() > 0;
  }

  public void setTotal(int total)
  {
    this.total = total;
  }

  public int getTotal()
  {
    return total;
  }

  public void setFailures(int failures)
  {
    this.failures = failures;
  }

  public int getFailures()
  {
    return failures;
  }

  public void setFileId(String fileId)
  {
    this.fileId = fileId;
  }

  public String getFileId()
  {
    return fileId;
  }

  public void setProblems(Collection<ImportProblemIF> problems)
  {
    this.problems = problems;
  }

  public Collection<ImportProblemIF> getProblems()
  {
    return problems;
  }

  public void setDatasets(JSONArray datasets)
  {
    this.datasets = datasets;
  }

  public JSONArray getDatasets()
  {
    return datasets;
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
  
  @Override
  public ContentStream getStream()
  {
    byte[] bytes = this.getJSON().getBytes(Charset.forName("UTF-8"));

    return new ContentStream(new ByteArrayInputStream(bytes), "application/json");
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
}
