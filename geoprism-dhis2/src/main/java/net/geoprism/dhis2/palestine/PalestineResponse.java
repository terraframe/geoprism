package net.geoprism.dhis2.palestine;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import net.geoprism.ContentStream;
import net.geoprism.data.etl.CategoryProblem;
import net.geoprism.data.etl.ImportProblemIF;
import net.geoprism.data.etl.ImportResponseIF;
import net.geoprism.data.etl.LocationProblem;
import net.geoprism.ontology.Classifier;
import net.geoprism.ontology.ClassifierIsARelationship;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.business.ontology.Term;
import com.runwaysdk.dataaccess.MdAttributeTermDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.metadata.MdAttributeTermDAO;

public class PalestineResponse implements ImportResponseIF
{
  private Collection<ImportProblemIF> problems;
  
  private int                         total;
  
  private int                         failures;

  private String                      fileId;
  
  public PalestineResponse()
  {
    this.problems = new LinkedList<>();
  }
  
  @Override
  public JSONObject toJSON() throws JSONException
  {
    JSONObject object = new JSONObject();
    object.put("success", true);
    object.put("datasets", new JSONArray());
    object.put("total", this.total);
    object.put("failures", this.failures);

    if (this.problems.size() > 0)
    {
      object.put("problems", this.getProblemsJSON());
      object.put("sheets", new JSONArray());
    }

    if (this.fileId != null)
    {
      object.put("fileId", this.fileId);
    }

    return object;
  }

  @Override
  public boolean hasProblems()
  {
    return this.problems.size() > 0;
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

  @Override
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
  public InputStream getStream()
  {
    byte[] bytes = this.getJSON().getBytes(Charset.forName("UTF-8"));

    return new ContentStream(new ByteArrayInputStream(bytes), "application/json");
  }

}
