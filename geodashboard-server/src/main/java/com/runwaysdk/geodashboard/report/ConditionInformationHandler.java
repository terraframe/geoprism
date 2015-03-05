package com.runwaysdk.geodashboard.report;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;

import com.runwaysdk.dataaccess.MdAttributeBooleanDAOIF;
import com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeMomentDAOIF;
import com.runwaysdk.dataaccess.MdAttributeNumberDAOIF;
import com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.generation.loader.Reloadable;
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
      String localizedOperation = this.getLocalizedOperation(_operation);
      Classifier classifier = Classifier.get(_value);

      String localizedValue = classifier.getDisplayLabel().getValue();

      this.handleCondition(localizedLabel, localizedOperation, localizedValue);
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
    if (_operation.equals(GT))
    {
      return LocalizationFacade.getFromBundles(GT);
    }
    else if (_operation.equals(GE))
    {
      return LocalizationFacade.getFromBundles(GE);
    }
    else if (_operation.equals(LT))
    {
      return LocalizationFacade.getFromBundles(LT);
    }
    else if (_operation.equals(LE))
    {
      return LocalizationFacade.getFromBundles(LE);
    }
    else if (_operation.equals(NEQ))
    {
      return LocalizationFacade.getFromBundles(NEQ);
    }

    return LocalizationFacade.getFromBundles(EQ);
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
