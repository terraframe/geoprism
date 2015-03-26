package com.runwaysdk.geodashboard.report;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;

import com.runwaysdk.dataaccess.MdAttributeBooleanDAOIF;
import com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeMomentDAOIF;
import com.runwaysdk.dataaccess.MdAttributeNumberDAOIF;
import com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.geodashboard.gis.persist.condition.DashboardEqual;
import com.runwaysdk.geodashboard.gis.persist.condition.DashboardGreaterThan;
import com.runwaysdk.geodashboard.gis.persist.condition.DashboardGreaterThanOrEqual;
import com.runwaysdk.geodashboard.gis.persist.condition.DashboardLessThan;
import com.runwaysdk.geodashboard.gis.persist.condition.DashboardLessThanOrEqual;
import com.runwaysdk.geodashboard.gis.persist.condition.DashboardNotEqual;
import com.runwaysdk.geodashboard.localization.LocalizationFacade;
import com.runwaysdk.geodashboard.ontology.Classifier;
import com.runwaysdk.system.gis.geo.GeoEntity;

public class ConditionInformationHandler implements Reloadable, ReportConditionHandlerIF
{
  private JSONArray array;

  public ConditionInformationHandler()
  {
    this.array = new JSONArray();
  }

  @Override
  public void handleAttributeCondition(MdAttributeDAOIF _mdAttribute, String _operation, String _value)
  {
    if (_mdAttribute instanceof MdAttributeNumberDAOIF)
    {
      handleNumberCondition(_operation, _value, (MdAttributeNumberDAOIF) _mdAttribute);
    }
    else if (_mdAttribute instanceof MdAttributeBooleanDAOIF)
    {
      handleBooleanCondition(_operation, _value, (MdAttributeBooleanDAOIF) _mdAttribute);
    }
    else if (_mdAttribute instanceof MdAttributeMomentDAOIF)
    {
      handleMomentCondition(_operation, _value, (MdAttributeMomentDAOIF) _mdAttribute);
    }
    else if (_mdAttribute instanceof MdAttributeCharacterDAOIF)
    {
      handleCharacterCondition(_operation, _value, (MdAttributeCharacterDAOIF) _mdAttribute);
    }
    else if (_mdAttribute instanceof MdAttributeReferenceDAOIF)
    {
      handleReferenceCondition(_operation, _value, (MdAttributeReferenceDAOIF) _mdAttribute);
    }
  }

  private void handleReferenceCondition(String _operation, String _value, MdAttributeReferenceDAOIF _mdAttribute)
  {
    MdBusinessDAOIF mdBusinessDAO = _mdAttribute.getReferenceMdBusinessDAO();

    if (mdBusinessDAO.definesType().equals(Classifier.CLASS))
    {
      Locale locale = LocalizationFacade.getLocale();

      String localizedLabel = _mdAttribute.getDisplayLabel(locale);
      String localizedOperation = LocalizationFacade.getFromBundles("classifier.operation");

      try
      {
        JSONArray array = new JSONArray(_value);

        for (int i = 0; i < array.length(); i++)
        {
          String termId = array.getString(i);

          try
          {
            Classifier classifier = Classifier.get(termId);

            String localizedValue = classifier.getDisplayLabel().getValue();

            this.handleCondition(localizedLabel, localizedOperation, localizedValue);
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
    }
    else
    {
      throw new ProgrammingErrorException("Condition on the reference type [" + mdBusinessDAO.definesType() + "] is not supported.");
    }
  }

  private void handleCharacterCondition(String _operation, String _value, MdAttributeCharacterDAOIF _mdAttribute)
  {
    Locale locale = LocalizationFacade.getLocale();

    String localizedLabel = _mdAttribute.getDisplayLabel(locale);
    String localizedOperation = this.getLocalizedOperation(_operation);

    this.handleCondition(localizedLabel, localizedOperation, _value);
  }

  private void handleMomentCondition(String _operation, String _value, MdAttributeMomentDAOIF _mdAttribute)
  {
    Locale locale = LocalizationFacade.getLocale();

    String localizedLabel = _mdAttribute.getDisplayLabel(locale);
    String localizedOperation = this.getLocalizedOperation(_operation);

    Date date = ReportProviderUtil.parseDate(_value);
    DateFormat format = DateFormat.getDateInstance(DateFormat.SHORT, locale);
    String localizedValue = format.format(date);

    this.handleCondition(localizedLabel, localizedOperation, localizedValue);
  }

  private void handleBooleanCondition(String _operation, String _value, MdAttributeBooleanDAOIF _mdAttribute)
  {
    Locale locale = LocalizationFacade.getLocale();

    String localizedLabel = _mdAttribute.getDisplayLabel(locale);
    String localizedOperation = this.getLocalizedOperation(_operation);

    boolean result = Boolean.parseBoolean(_value);

    if (result)
    {
      String localizedValue = _mdAttribute.getPositiveDisplayLabel(locale);

      this.handleCondition(localizedLabel, localizedOperation, localizedValue);
    }
    else
    {
      String localizedValue = _mdAttribute.getNegativeDisplayLabel(locale);

      this.handleCondition(localizedLabel, localizedOperation, localizedValue);
    }
  }

  private void handleNumberCondition(String _operation, String _value, MdAttributeNumberDAOIF _mdAttribute)
  {
    Locale locale = LocalizationFacade.getLocale();
    NumberFormat format = NumberFormat.getInstance(locale);

    String localizedLabel = _mdAttribute.getDisplayLabel(locale);
    String localizedOperation = this.getLocalizedOperation(_operation);
    String localizedValue = format.format(Double.parseDouble(_value));

    this.handleCondition(localizedLabel, localizedOperation, localizedValue);
  }

  private String getLocalizedOperation(String _operation)
  {
    if (_operation.equals(DashboardGreaterThan.OPERATION))
    {
      return LocalizationFacade.getFromBundles(DashboardGreaterThan.OPERATION);
    }
    else if (_operation.equals(DashboardGreaterThanOrEqual.OPERATION))
    {
      return LocalizationFacade.getFromBundles(DashboardGreaterThanOrEqual.OPERATION);
    }
    else if (_operation.equals(DashboardLessThan.OPERATION))
    {
      return LocalizationFacade.getFromBundles(DashboardLessThan.OPERATION);
    }
    else if (_operation.equals(DashboardLessThanOrEqual.OPERATION))
    {
      return LocalizationFacade.getFromBundles(DashboardLessThanOrEqual.OPERATION);
    }
    else if (_operation.equals(DashboardNotEqual.OPERATION))
    {
      return LocalizationFacade.getFromBundles(DashboardNotEqual.OPERATION);
    }

    return LocalizationFacade.getFromBundles(DashboardEqual.OPERATION);
  }

  private void handleCondition(String localizedLabel, String localizedOperation, String localizedValue)
  {
    String message = LocalizationFacade.getFromBundles("attribute.condition");
    message = message.replace("{0}", localizedLabel);
    message = message.replace("{1}", localizedOperation);
    message = message.replace("{2}", localizedValue);

    this.array.put(message);
  }

  @Override
  public void handleLocationCondition(String value)
  {
    String localizedValue = GeoEntity.get(value).getDisplayLabel().getValue();

    String message = LocalizationFacade.getFromBundles("location.condition");
    message = message.replace("{0}", localizedValue);

    this.array.put(message);
  }

  public String getInformation()
  {
    return this.array.toString();
  }
}
