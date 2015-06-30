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
package com.runwaysdk.geodashboard.report;

import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;

import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.generated.system.gis.geo.GeoEntityAllPathsTableQuery;
import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.geodashboard.QueryUtil;
import com.runwaysdk.geodashboard.gis.persist.condition.DashboardEqual;
import com.runwaysdk.geodashboard.gis.persist.condition.DashboardGreaterThan;
import com.runwaysdk.geodashboard.gis.persist.condition.DashboardGreaterThanOrEqual;
import com.runwaysdk.geodashboard.gis.persist.condition.DashboardLessThan;
import com.runwaysdk.geodashboard.gis.persist.condition.DashboardLessThanOrEqual;
import com.runwaysdk.geodashboard.gis.persist.condition.DashboardNotEqual;
import com.runwaysdk.geodashboard.ontology.Classifier;
import com.runwaysdk.geodashboard.ontology.ClassifierAllPathsTableQuery;
import com.runwaysdk.query.Attribute;
import com.runwaysdk.query.AttributeBoolean;
import com.runwaysdk.query.AttributeCharacter;
import com.runwaysdk.query.AttributeMoment;
import com.runwaysdk.query.AttributeNumber;
import com.runwaysdk.query.AttributeReference;
import com.runwaysdk.query.GeneratedComponentQuery;
import com.runwaysdk.query.ValueQuery;
import com.runwaysdk.system.gis.geo.GeoEntity;

public class ReportConditionHandler implements ReportConditionHandlerIF, Reloadable
{
  private String                  type;

  private ValueQuery              vQuery;

  private GeneratedComponentQuery query;

  public ReportConditionHandler(String _type, ValueQuery _vQuery, GeneratedComponentQuery _query)
  {
    this.type = _type;
    this.vQuery = _vQuery;
    this.query = _query;
  }

  @Override
  public void handleAttributeCondition(MdAttributeDAOIF _mdAttribute, String _operation, String _value)
  {
    String attributeName = _mdAttribute.definesAttribute();
    String key = _mdAttribute.getKey();

    if (key.startsWith(type))
    {
      Attribute attribute = query.get(attributeName);

      if (attribute instanceof AttributeNumber)
      {
        addNumberCondition(_operation, _value, (AttributeNumber) attribute);
      }
      else if (attribute instanceof AttributeBoolean)
      {
        addBooleanCondition(_operation, _value, (AttributeBoolean) attribute);
      }
      else if (attribute instanceof AttributeMoment)
      {
        addMomentCondition(_operation, _value, (AttributeMoment) attribute);
      }
      else if (attribute instanceof AttributeCharacter)
      {
        addCharacterCondition(_operation, _value, (AttributeCharacter) attribute);
      }
      else if (attribute instanceof AttributeReference)
      {
        addTermCondition(_operation, _value, (AttributeReference) attribute);
      }
    }
  }

  @Override
  public void handleLocationCondition(String _value)
  {
    MdClassDAOIF mdClass = query.getMdClassIF();
    MdAttributeDAOIF mdAttribute = QueryUtil.getGeoEntityAttribute(mdClass);

    if (mdAttribute != null)
    {
      AttributeReference attribute = (AttributeReference) query.get(mdAttribute.definesAttribute());

      addGeoEntityCondition(_value, attribute);
    }
  }

  private void addBooleanCondition(String _operation, String _value, AttributeBoolean _attribute)
  {
    Boolean bool = new Boolean(_value);

    if (_operation.equals(DashboardEqual.OPERATION))
    {
      vQuery.AND(_attribute.EQ(bool));
    }
    else
    {
      UnsupportedComparisonException e = new UnsupportedComparisonException();
      e.setComparison(_operation);

      throw e;
    }
  }

  private void addMomentCondition(String _operation, String _value, AttributeMoment _attribute)
  {
    Date date = ReportProviderUtil.parseDate(_value);

    if (_operation.equals(DashboardGreaterThan.OPERATION))
    {
      vQuery.AND(_attribute.GT(date));
    }
    else if (_operation.equals(DashboardGreaterThanOrEqual.OPERATION))
    {
      vQuery.AND(_attribute.GE(date));
    }
    else if (_operation.equals(DashboardLessThan.OPERATION))
    {
      vQuery.AND(_attribute.LT(date));
    }
    else if (_operation.equals(DashboardLessThanOrEqual.OPERATION))
    {
      vQuery.AND(_attribute.LE(date));
    }
    else if (_operation.equals(DashboardEqual.OPERATION))
    {
      vQuery.AND(_attribute.EQ(date));
    }
    else
    {
      UnsupportedComparisonException e = new UnsupportedComparisonException();
      e.setComparison(_operation);

      throw e;
    }
  }

  private void addNumberCondition(String _operation, String _value, AttributeNumber _attribute)
  {
    if (_operation.equals(DashboardGreaterThan.OPERATION))
    {
      vQuery.AND(_attribute.GT(_value));
    }
    else if (_operation.equals(DashboardGreaterThanOrEqual.OPERATION))
    {
      vQuery.AND(_attribute.GE(_value));
    }
    else if (_operation.equals(DashboardLessThan.OPERATION))
    {
      vQuery.AND(_attribute.LT(_value));
    }
    else if (_operation.equals(DashboardLessThanOrEqual.OPERATION))
    {
      vQuery.AND(_attribute.LE(_value));
    }
    else if (_operation.equals(DashboardEqual.OPERATION))
    {
      vQuery.AND(_attribute.EQ(_value));
    }
    else
    {
      UnsupportedComparisonException e = new UnsupportedComparisonException();
      e.setComparison(_operation);

      throw e;
    }
  }

  private void addCharacterCondition(String _operation, String _value, AttributeCharacter _attribute)
  {
    if (_operation.equals(DashboardEqual.OPERATION))
    {
      vQuery.AND(_attribute.EQ(_value));
    }
    else if (_operation.equals(DashboardNotEqual.OPERATION))
    {
      vQuery.AND(_attribute.NE(_value));
    }
    else
    {
      UnsupportedComparisonException e = new UnsupportedComparisonException();
      e.setComparison(_operation);

      throw e;
    }
  }

  private void addTermCondition(String _operation, String _value, AttributeReference _attribute)
  {
    MdAttributeReferenceDAOIF mdAttributeTerm = (MdAttributeReferenceDAOIF) _attribute.getMdAttributeIF();
    MdBusinessDAOIF mdBusinessDAO = mdAttributeTerm.getReferenceMdBusinessDAO();

    if (mdBusinessDAO.definesType().equals(Classifier.CLASS))
    {
      if (_operation.equals(DashboardEqual.OPERATION))
      {

        try
        {
          ClassifierAllPathsTableQuery allPathQuery = new ClassifierAllPathsTableQuery(vQuery);

          JSONArray array = new JSONArray(_value);

          for (int i = 0; i < array.length(); i++)
          {
            String termId = array.getString(i);

            allPathQuery.OR(allPathQuery.getParentTerm().EQ(termId));
          }

          vQuery.AND(_attribute.EQ(allPathQuery.getChildTerm()));
        }
        catch (JSONException e)
        {
          throw new ProgrammingErrorException(e);
        }
      }
      else
      {
        UnsupportedComparisonException e = new UnsupportedComparisonException();
        e.setComparison(_operation);

        throw e;
      }
    }
    else
    {
      throw new ProgrammingErrorException("Condition on the reference type [" + mdBusinessDAO.definesType() + "] is not supported.");
    }
  }

  private void addGeoEntityCondition(String _entityId, AttributeReference _attribute)
  {
    GeoEntity entity = GeoEntity.get(_entityId);

    GeoEntityAllPathsTableQuery aptQuery = new GeoEntityAllPathsTableQuery(vQuery);

    vQuery.WHERE(aptQuery.getParentTerm().EQ(entity));
    vQuery.AND(_attribute.EQ(aptQuery.getChildTerm()));
  }

}
