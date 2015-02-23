package com.runwaysdk.geodashboard.report;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.metadata.MdAttributeDAO;
import com.runwaysdk.generated.system.gis.geo.GeoEntityAllPathsTableQuery;
import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.geodashboard.QueryUtil;
import com.runwaysdk.geodashboard.localization.LocalizationFacade;
import com.runwaysdk.geodashboard.ontology.Classifier;
import com.runwaysdk.geodashboard.ontology.ClassifierAllPathsTableQuery;
import com.runwaysdk.geodashboard.parse.DateParseException;
import com.runwaysdk.query.Attribute;
import com.runwaysdk.query.AttributeBoolean;
import com.runwaysdk.query.AttributeCharacter;
import com.runwaysdk.query.AttributeMoment;
import com.runwaysdk.query.AttributeNumber;
import com.runwaysdk.query.AttributeReference;
import com.runwaysdk.query.GeneratedBusinessQuery;
import com.runwaysdk.query.ValueQuery;
import com.runwaysdk.system.gis.geo.GeoEntity;

public class ReportProviderUtil implements Reloadable
{
  /**
   * Condition type for restricting on global location
   */
  private static final String LOCATION_CONDITION  = "LOCATION_CONDITION";

  /**
   * Condition type for restricting on an attribute
   */
  private static final String ATTRIBUTE_CONDITION = "ATTRIBUTE_CONDITION";

  /**
   * Magic value for the json attribute name which specifies the id of the MdAttribute
   */
  private static final String MD_ATTRIBUTE        = "mdAttribute";

  /**
   * Magic value for the json attribute name which specifies the operation type
   */
  private static final String OPERATION           = "operation";

  /**
   * Magic value for the json attribute name which specifies the value
   */
  private static final String VALUE               = "value";

  /**
   * Magic value for the json attribute name which specifies the value
   */
  private static final String TYPE                = "type";

  /**
   * Greater than comparison
   */
  public static final String  GT                  = "gt";

  /**
   * Greater than or equal comparison
   */
  public static final String  GE                  = "ge";

  /**
   * Less than comparison
   */
  public static final String  LT                  = "lt";

  /**
   * Less than or equal comparison
   */
  public static final String  LE                  = "le";

  /**
   * Equal comparison
   */
  public static final String  EQ                  = "eq";

  /**
   * Equal comparison
   */
  public static final String  NEQ                 = "neq";

  public static GeoEntity getGeoEntity(String category, String defaultGeoId)
  {
    if (category != null && category.length() > 0)
    {
      try
      {
        return GeoEntity.getByKey(category);
      }
      catch (Exception e)
      {
        // Incoming data is bad, just use the default geo id
      }
    }

    return GeoEntity.getByKey(defaultGeoId);
  }

  public static void addConditions(String criteria, String type, GeneratedBusinessQuery query, ValueQuery vQuery)
  {
    try
    {
      if (criteria != null)
      {
        JSONArray conditions = new JSONArray(criteria);

        int length = conditions.length();

        for (int i = 0; i < length; i++)
        {
          JSONObject condition = conditions.getJSONObject(i);
          String operation = condition.getString(OPERATION);
          String value = condition.getString(VALUE);
          String conditionType = condition.getString(TYPE);

          if (conditionType.equals(ATTRIBUTE_CONDITION))
          {
            String mdAttributeId = condition.getString(MD_ATTRIBUTE);
            MdAttributeDAOIF mdAttribute = MdAttributeDAO.get(mdAttributeId).getMdAttributeConcrete();

            String attributeName = mdAttribute.definesAttribute();
            String key = mdAttribute.getKey();

            if (key.startsWith(type))
            {
              Attribute attribute = QueryUtil.get(query, attributeName);

              if (attribute instanceof AttributeNumber)
              {
                addNumberCondition(vQuery, operation, value, (AttributeNumber) attribute);
              }
              else if (attribute instanceof AttributeBoolean)
              {
                addBooleanCondition(vQuery, operation, value, (AttributeBoolean) attribute);
              }
              else if (attribute instanceof AttributeMoment)
              {
                addMomentCondition(vQuery, operation, value, (AttributeMoment) attribute);
              }
              else if (attribute instanceof AttributeCharacter)
              {
                addCharacterCondition(vQuery, operation, value, (AttributeCharacter) attribute);
              }
              else if (attribute instanceof AttributeReference)
              {
                addTermCondition(vQuery, operation, value, (AttributeReference) attribute);
              }
            }
          }
          else if (conditionType.equals(LOCATION_CONDITION))
          {
            MdClassDAOIF mdClass = query.getMdClassIF();
            MdAttributeDAOIF mdAttribute = QueryUtil.getGeoEntityAttribute(mdClass);

            if (mdAttribute != null)
            {
              AttributeReference attribute = (AttributeReference) QueryUtil.get(query, mdAttribute.definesAttribute());

              addGeoEntityCondition(vQuery, value, attribute);
            }
          }
        }
      }
    }
    catch (JSONException e)
    {
      // Invalid JSON: do nothing
    }
  }

  public static Date parseDate(String source)
  {
    Locale locale = LocalizationFacade.getLocale();
    SimpleDateFormat format = new SimpleDateFormat("dd/mm/yyyy", locale);

    try
    {
      Date date = format.parse(source);

      return date;
    }
    catch (ParseException cause)
    {
      DateParseException e = new DateParseException(cause);
      e.setInput(source);
      e.setPattern(format.toLocalizedPattern());
      throw e;
    }
  }

  private static void addBooleanCondition(ValueQuery vQuery, String operation, String value, AttributeBoolean attribute)
  {
    Boolean bool = new Boolean(value);

    if (operation.equals(EQ))
    {
      vQuery.AND(attribute.EQ(bool));
    }
    else
    {
      UnsupportedComparisonException e = new UnsupportedComparisonException();
      e.setComparison(operation);

      throw e;
    }
  }

  public static void addMomentCondition(ValueQuery vQuery, String operation, String value, AttributeMoment attribute)
  {
    Date date = ReportProviderUtil.parseDate(value);

    if (operation.equals(GT))
    {
      vQuery.AND(attribute.GT(date));
    }
    else if (operation.equals(GE))
    {
      vQuery.AND(attribute.GE(date));
    }
    else if (operation.equals(LT))
    {
      vQuery.AND(attribute.LT(date));
    }
    else if (operation.equals(LE))
    {
      vQuery.AND(attribute.LE(date));
    }
    else if (operation.equals(EQ))
    {
      vQuery.AND(attribute.EQ(date));
    }
    else
    {
      UnsupportedComparisonException e = new UnsupportedComparisonException();
      e.setComparison(operation);

      throw e;
    }
  }

  public static void addNumberCondition(ValueQuery vQuery, String operation, String value, AttributeNumber attribute)
  {
    if (operation.equals(GT))
    {
      vQuery.AND(attribute.GT(value));
    }
    else if (operation.equals(GE))
    {
      vQuery.AND(attribute.GE(value));
    }
    else if (operation.equals(LT))
    {
      vQuery.AND(attribute.LT(value));
    }
    else if (operation.equals(LE))
    {
      vQuery.AND(attribute.LE(value));
    }
    else if (operation.equals(EQ))
    {
      vQuery.AND(attribute.EQ(value));
    }
    else
    {
      UnsupportedComparisonException e = new UnsupportedComparisonException();
      e.setComparison(operation);

      throw e;
    }
  }

  public static void addCharacterCondition(ValueQuery vQuery, String operation, String value, AttributeCharacter attribute)
  {
    if (operation.equals(EQ))
    {
      vQuery.AND(attribute.EQ(value));
    }
    else if (operation.equals(NEQ))
    {
      vQuery.AND(attribute.NE(value));
    }
    else
    {
      UnsupportedComparisonException e = new UnsupportedComparisonException();
      e.setComparison(operation);

      throw e;
    }
  }

  private static void addGeoEntityCondition(ValueQuery vQuery, String entityId, AttributeReference attribute)
  {
    GeoEntity entity = GeoEntity.get(entityId);

    GeoEntityAllPathsTableQuery aptQuery = new GeoEntityAllPathsTableQuery(vQuery);

    vQuery.WHERE(aptQuery.getParentTerm().EQ(entity));
    vQuery.AND(attribute.EQ(aptQuery.getChildTerm()));
  }

  private static void addTermCondition(ValueQuery vQuery, String operation, String value, AttributeReference attribute)
  {
    MdAttributeReferenceDAOIF mdAttributeTerm = (MdAttributeReferenceDAOIF) attribute.getMdAttributeIF();
    MdBusinessDAOIF mdBusinessDAO = mdAttributeTerm.getReferenceMdBusinessDAO();

    if (mdBusinessDAO.definesType().equals(Classifier.CLASS))
    {
      if (operation.equals(EQ))
      {
        ClassifierAllPathsTableQuery allPathQuery = new ClassifierAllPathsTableQuery(vQuery);
        allPathQuery.WHERE(allPathQuery.getParentTerm().EQ(value));

        vQuery.AND(attribute.EQ(allPathQuery.getChildTerm()));
      }
      else
      {
        UnsupportedComparisonException e = new UnsupportedComparisonException();
        e.setComparison(operation);

        throw e;
      }
    }
    else
    {
      throw new ProgrammingErrorException("Condition on the reference type [" + mdBusinessDAO.definesType() + "] is not supported.");
    }
  }

}
