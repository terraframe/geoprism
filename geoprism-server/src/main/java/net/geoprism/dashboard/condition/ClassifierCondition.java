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
package net.geoprism.dashboard.condition;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.dataaccess.metadata.MdAttributeDAO;
import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.query.AttributeReference;
import com.runwaysdk.query.Selectable;
import com.runwaysdk.query.ValueQuery;

import net.geoprism.localization.LocalizationFacade;
import net.geoprism.ontology.Classifier;
import net.geoprism.ontology.ClassifierAllPathsTableQuery;

public class ClassifierCondition extends DashboardPrimitiveCondition implements Reloadable
{
  /**
   * Equal comparison
   */
  public static final String OPERATION      = "eq";

  /**
   * Condition type for restricting on an attribute
   */
  public static final String CONDITION_TYPE = "CLASSIFIER_CONDITION";

  public ClassifierCondition(String mdAttributeId, String comparisonValue)
  {
    super(mdAttributeId, comparisonValue);
  }

  @Override
  public void restrictQuery(ValueQuery vQuery, Selectable attribute)
  {
    AttributeReference attributeTerm = (AttributeReference) attribute;
    MdAttributeReferenceDAOIF mdAttributeTerm = (MdAttributeReferenceDAOIF) attributeTerm.getMdAttributeIF();
    MdBusinessDAOIF mdBusinessDAO = mdAttributeTerm.getReferenceMdBusinessDAO();

    if (mdBusinessDAO.definesType().equals(Classifier.CLASS))
    {
      try
      {
        ClassifierAllPathsTableQuery allPathQuery = new ClassifierAllPathsTableQuery(vQuery);

        JSONArray array = new JSONArray(this.getComparisonValue());

        for (int i = 0; i < array.length(); i++)
        {
          String termId = array.getString(i);

          allPathQuery.OR(allPathQuery.getParentTerm().EQ(termId));
        }

        vQuery.AND(attributeTerm.EQ(allPathQuery.getChildTerm()));
      }
      catch (JSONException e)
      {
        throw new ProgrammingErrorException(e);
      }
    }
  }

  @Override
  public String getOperation()
  {
    return OPERATION;
  }

  @Override
  public JSONObject getJSON()
  {
    try
    {
      String value = this.getComparisonValue();

      JSONObject object = new JSONObject();
      object.put(TYPE_KEY, CONDITION_TYPE);
      object.put(MD_ATTRIBUTE_KEY, this.getMdAttributeId());

      if (value != null && value.length() > 0)
      {
        object.put(VALUE_KEY, new JSONArray(value));
      }
      else
      {
        object.put(VALUE_KEY, new JSONArray());
      }

      return object;
    }
    catch (JSONException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

  @Override
  public List<String> getConditionInformation()
  {
    List<String> messages = new LinkedList<String>();

    MdAttributeDAOIF mdAttribute = MdAttributeDAO.get(this.getMdAttributeId());
    Locale locale = LocalizationFacade.getLocale();

    String localizedLabel = mdAttribute.getDisplayLabel(locale);
    String localizedOperation = LocalizationFacade.getFromBundles("classifier.operation");
    String value = this.getComparisonValue();

    try
    {
      JSONArray array = new JSONArray(value);

      for (int i = 0; i < array.length(); i++)
      {
        String termId = array.getString(i);

        try
        {
          Classifier classifier = Classifier.get(termId);

          String localizedValue = classifier.getDisplayLabel().getValue();

          messages.add(this.handleCondition(localizedLabel, localizedOperation, localizedValue));
        }
        catch (DataNotFoundException e)
        {
          // Do nothing. There was criteria specified on a classifier which no longer exists or the id of the
          // classifier was changed.
        }
      }
    }
    catch (JSONException e)
    {
      throw new ProgrammingErrorException(e);
    }
    
    return messages;
  }
}
