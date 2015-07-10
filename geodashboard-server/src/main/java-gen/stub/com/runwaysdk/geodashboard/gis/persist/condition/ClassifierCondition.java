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
package com.runwaysdk.geodashboard.gis.persist.condition;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.geodashboard.gis.model.MapVisitor;
import com.runwaysdk.geodashboard.ontology.Classifier;
import com.runwaysdk.geodashboard.ontology.ClassifierAllPathsTableQuery;
import com.runwaysdk.query.Attribute;
import com.runwaysdk.query.AttributeReference;
import com.runwaysdk.query.ValueQuery;

public class ClassifierCondition extends ClassifierConditionBase implements com.runwaysdk.generation.loader.Reloadable
{

  /**
   * Equal comparison
   */
  public static final String OPERATION        = "eq";

  /**
   * Condition type for restricting on an attribute
   */
  public static final String CONDITION_TYPE   = "CLASSIFIER_CONDITION";

  private static final long  serialVersionUID = 204657777;

  public ClassifierCondition()
  {
    super();
  }

  @Override
  public void restrictQuery(ValueQuery query, Attribute attr)
  {
    AttributeReference attributeTerm = (AttributeReference) attr;
    MdAttributeReferenceDAOIF mdAttributeTerm = (MdAttributeReferenceDAOIF) attributeTerm.getMdAttributeIF();
    MdBusinessDAOIF mdBusinessDAO = mdAttributeTerm.getReferenceMdBusinessDAO();

    if (mdBusinessDAO.definesType().equals(Classifier.CLASS))
    {
      try
      {
        ClassifierAllPathsTableQuery allPathQuery = new ClassifierAllPathsTableQuery(query);

        JSONArray array = new JSONArray(this.getComparisonValue());

        for (int i = 0; i < array.length(); i++)
        {
          String termId = array.getString(i);

          allPathQuery.OR(allPathQuery.getParentTerm().EQ(termId));
        }

        query.AND(attributeTerm.EQ(allPathQuery.getChildTerm()));
      }
      catch (JSONException e)
      {
        throw new ProgrammingErrorException(e);
      }
    }
  }

  @Override
  public void accepts(MapVisitor visitor)
  {
    // TODO Auto-generated method stub

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
      JSONObject object = new JSONObject();
      object.put(TYPE_KEY, CONDITION_TYPE);
      object.put(MD_ATTRIBUTE_KEY, this.getDefiningMdAttributeId());
      object.put(OPERATION_KEY, this.getOperation());
      object.put(VALUE_KEY, this.getComparisonValue());

      return object;
    }
    catch (JSONException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

  @Override
  protected DashboardCondition newInstance()
  {
    return new ClassifierCondition();
  }

}
