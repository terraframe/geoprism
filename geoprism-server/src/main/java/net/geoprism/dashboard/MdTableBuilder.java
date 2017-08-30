package net.geoprism.dashboard;

import java.util.List;
import java.util.Map;

import org.json.JSONException;

import com.runwaysdk.business.ontology.Term;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeCharacterInfo;
import com.runwaysdk.constants.MdAttributeConcreteInfo;
import com.runwaysdk.constants.MdAttributeDateInfo;
import com.runwaysdk.constants.MdAttributeDateTimeInfo;
import com.runwaysdk.constants.MdAttributeDecimalInfo;
import com.runwaysdk.constants.MdAttributeDoubleInfo;
import com.runwaysdk.constants.MdAttributeFloatInfo;
import com.runwaysdk.constants.MdAttributeIntegerInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdAttributeLongInfo;
import com.runwaysdk.constants.MdAttributeReferenceInfo;
import com.runwaysdk.constants.MdAttributeTextInfo;
import com.runwaysdk.constants.MdAttributeTimeInfo;
import com.runwaysdk.constants.MdTableInfo;
import com.runwaysdk.constants.MetadataInfo;
import com.runwaysdk.dataaccess.MdAttributeBooleanDAOIF;
import com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDateDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDateTimeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDecimalDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDoubleDAOIF;
import com.runwaysdk.dataaccess.MdAttributeFloatDAOIF;
import com.runwaysdk.dataaccess.MdAttributeIntegerDAOIF;
import com.runwaysdk.dataaccess.MdAttributeLongDAOIF;
import com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF;
import com.runwaysdk.dataaccess.MdAttributeTextDAOIF;
import com.runwaysdk.dataaccess.MdAttributeTimeDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.metadata.MdAttributeBooleanDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeCharacterDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDateDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDateTimeDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDecimalDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDoubleDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeFloatDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeIntegerDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeLongDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeReferenceDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeTextDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeTimeDAO;
import com.runwaysdk.dataaccess.metadata.MdTableDAO;
import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.gis.constants.MdAttributeMultiPointInfo;
import com.runwaysdk.gis.constants.MdAttributeMultiPolygonInfo;
import com.runwaysdk.gis.constants.MdAttributePointInfo;
import com.runwaysdk.gis.constants.MdAttributePolygonInfo;
import com.runwaysdk.gis.dataaccess.metadata.MdAttributeMultiPointDAO;
import com.runwaysdk.gis.dataaccess.metadata.MdAttributeMultiPolygonDAO;
import com.runwaysdk.gis.dataaccess.metadata.MdAttributePointDAO;
import com.runwaysdk.gis.dataaccess.metadata.MdAttributePolygonDAO;
import com.runwaysdk.query.Selectable;
import com.runwaysdk.query.SelectableAggregate;
import com.runwaysdk.query.ValueQuery;
import com.runwaysdk.session.Session;
import com.runwaysdk.system.gis.metadata.MdAttributeMultiPoint;
import com.runwaysdk.system.gis.metadata.MdAttributeMultiPolygon;
import com.runwaysdk.system.gis.metadata.MdAttributePoint;
import com.runwaysdk.system.gis.metadata.MdAttributePolygon;

public class MdTableBuilder implements Reloadable
{
  public static final String PACKAGE_NAME = "net.geoprism.data.table";

  public MdTableDAO build(String label, String viewName, ValueQuery query) throws JSONException
  {
    // Create the MdTable
    MdTableDAO mdTableDAO = MdTableDAO.newInstance();
    mdTableDAO.setValue(MdTableInfo.NAME, viewName);
    mdTableDAO.setValue(MdTableInfo.PACKAGE, PACKAGE_NAME);
    mdTableDAO.setStructValue(MdTableInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, label);
    mdTableDAO.setValue(MdTableInfo.TABLE_NAME, viewName);
    mdTableDAO.apply();

    this.defineMdAttributes(mdTableDAO, query.getSelectableRefs());

    return mdTableDAO;
  }

  @SuppressWarnings("unchecked")
  private void defineMdAttributes(MdTableDAO mdTableDAO, List<Selectable> selectables)
  {
    for (Selectable selectable : selectables)
    {
      Map<String, Object> data = (Map<String, Object>) selectable.getData();
      MdAttributeConcreteDAOIF mdAttributeIF = (MdAttributeConcreteDAOIF) data.get(MetadataInfo.CLASS);

      String attributeName = mdAttributeIF.definesAttribute();
      attributeName = attributeName.substring(Math.max(0, attributeName.length() - 64));

      if (mdAttributeIF instanceof MdAttributeReferenceDAOIF)
      {
        MdAttributeReferenceDAOIF mdAttributeReference = (MdAttributeReferenceDAOIF) mdAttributeIF;
        MdBusinessDAOIF mdBusiness = mdAttributeReference.getReferenceMdBusinessDAO();

        if (mdBusiness.definesType().equals(Term.CLASS))
        {
          MdAttributeReferenceDAO mdAttribute = MdAttributeReferenceDAO.newInstance();
          mdAttribute.setValue(MdAttributeConcreteInfo.NAME, attributeName);
          mdAttribute.setStructValue(MdAttributeConcreteInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, mdAttributeReference.getDisplayLabel(Session.getCurrentLocale()));
          mdAttribute.setValue(MdAttributeConcreteInfo.DEFINING_MD_CLASS, mdTableDAO.getId());
          mdAttribute.setValue(MdAttributeReferenceInfo.REF_MD_ENTITY, mdBusiness.getId());
          mdAttribute.getAttribute(MdAttributeConcreteInfo.COLUMN_NAME).setValueNoValidation(selectable.getDbColumnName());
          mdAttribute.apply();

        }
      }
      else if (mdAttributeIF instanceof MdAttributeBooleanDAOIF)
      {
        Boolean isAggregateFunction = data.containsKey(SelectableAggregate.class.getName()) ? (Boolean) data.get(SelectableAggregate.class.getName()) : false;

        if (!isAggregateFunction)
        {
          MdAttributeBooleanDAO mdAttribute = MdAttributeBooleanDAO.newInstance();
          mdAttribute.setValue(MdAttributeBooleanInfo.NAME, attributeName);
          mdAttribute.setStructValue(MdAttributeBooleanInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, mdAttributeIF.getDisplayLabel(Session.getCurrentLocale()));
          mdAttribute.setStructValue(MdAttributeBooleanInfo.POSITIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.TRUE);
          mdAttribute.setStructValue(MdAttributeBooleanInfo.NEGATIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.FALSE);
          mdAttribute.setValue(MdAttributeBooleanInfo.DEFINING_MD_CLASS, mdTableDAO.getId());
          mdAttribute.getAttribute(MdAttributeConcreteInfo.COLUMN_NAME).setValueNoValidation(selectable.getDbColumnName());
          mdAttribute.apply();
        }
        else
        {
          MdAttributeDoubleDAO mdAttribute = MdAttributeDoubleDAO.newInstance();
          mdAttribute.setValue(MdAttributeDoubleInfo.NAME, attributeName);
          mdAttribute.setStructValue(MdAttributeDoubleInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, mdAttributeIF.getDisplayLabel(Session.getCurrentLocale()));
          mdAttribute.setValue(MdAttributeDoubleInfo.DEFINING_MD_CLASS, mdTableDAO.getId());
          mdAttribute.getAttribute(MdAttributeConcreteInfo.COLUMN_NAME).setValueNoValidation(selectable.getDbColumnName());
          mdAttribute.getAttribute(MdAttributeDoubleInfo.LENGTH).setValue("20");
          mdAttribute.getAttribute(MdAttributeDoubleInfo.DECIMAL).setValue("2");
          mdAttribute.apply();
        }
      }
      else if (mdAttributeIF instanceof MdAttributeCharacterDAOIF || mdAttributeIF instanceof MdAttributeTextDAOIF)
      {
        if (mdAttributeIF instanceof MdAttributeTextDAOIF)
        {
          MdAttributeTextDAO mdAttribute = MdAttributeTextDAO.newInstance();
          mdAttribute.setValue(MdAttributeTextInfo.NAME, attributeName);
          mdAttribute.setStructValue(MdAttributeTextInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, mdAttributeIF.getDisplayLabel(Session.getCurrentLocale()));
          mdAttribute.setValue(MdAttributeTextInfo.DEFINING_MD_CLASS, mdTableDAO.getId());
          mdAttribute.getAttribute(MdAttributeConcreteInfo.COLUMN_NAME).setValueNoValidation(selectable.getDbColumnName());
          mdAttribute.apply();
        }
        else
        {
          /*
           * SIZE is spoofed because the MdAttributeCharacterDAOIF is an instanceof MdAttributeCharacter_Q which doesn't
           * support SIZE
           */
          if (!attributeName.contains("__"))
          {
            MdAttributeCharacterDAO mdAttribute = MdAttributeCharacterDAO.newInstance();
            mdAttribute.setValue(MdAttributeCharacterInfo.NAME, attributeName);
            mdAttribute.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, mdAttributeIF.getDisplayLabel(Session.getCurrentLocale()));
            mdAttribute.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, mdTableDAO.getId());
            mdAttribute.getAttribute(MdAttributeConcreteInfo.COLUMN_NAME).setValueNoValidation(selectable.getDbColumnName());
            mdAttribute.setValue(MdAttributeCharacterInfo.SIZE, "4000");
            mdAttribute.apply();
          }
        }
      }
      else if (mdAttributeIF instanceof MdAttributeDateDAOIF)
      {
        MdAttributeDateDAO mdAttribute = MdAttributeDateDAO.newInstance();
        mdAttribute.setValue(MdAttributeDateInfo.NAME, attributeName);
        mdAttribute.setStructValue(MdAttributeDateInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, mdAttributeIF.getDisplayLabel(Session.getCurrentLocale()));
        mdAttribute.setValue(MdAttributeDateInfo.DEFINING_MD_CLASS, mdTableDAO.getId());
        mdAttribute.getAttribute(MdAttributeConcreteInfo.COLUMN_NAME).setValueNoValidation(selectable.getDbColumnName());
        mdAttribute.apply();
      }
      else if (mdAttributeIF instanceof MdAttributeDateTimeDAOIF)
      {
        MdAttributeDateTimeDAO mdAttribute = MdAttributeDateTimeDAO.newInstance();
        mdAttribute.setValue(MdAttributeDateTimeInfo.NAME, attributeName);
        mdAttribute.setStructValue(MdAttributeDateTimeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, mdAttributeIF.getDisplayLabel(Session.getCurrentLocale()));
        mdAttribute.setValue(MdAttributeDateTimeInfo.DEFINING_MD_CLASS, mdTableDAO.getId());
        mdAttribute.getAttribute(MdAttributeConcreteInfo.COLUMN_NAME).setValueNoValidation(selectable.getDbColumnName());
        mdAttribute.apply();
      }
      else if (mdAttributeIF instanceof MdAttributeTimeDAOIF)
      {
        MdAttributeTimeDAO mdAttribute = MdAttributeTimeDAO.newInstance();
        mdAttribute.setValue(MdAttributeTimeInfo.NAME, attributeName);
        mdAttribute.setStructValue(MdAttributeTimeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, mdAttributeIF.getDisplayLabel(Session.getCurrentLocale()));
        mdAttribute.setValue(MdAttributeTimeInfo.DEFINING_MD_CLASS, mdTableDAO.getId());
        mdAttribute.getAttribute(MdAttributeConcreteInfo.COLUMN_NAME).setValueNoValidation(selectable.getDbColumnName());
        mdAttribute.apply();
      }
      else if (mdAttributeIF instanceof MdAttributeDecimalDAOIF)
      {
        MdAttributeDecimalDAO mdAttribute = MdAttributeDecimalDAO.newInstance();
        mdAttribute.setValue(MdAttributeDecimalInfo.NAME, attributeName);
        mdAttribute.setStructValue(MdAttributeDecimalInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, mdAttributeIF.getDisplayLabel(Session.getCurrentLocale()));
        mdAttribute.setValue(MdAttributeDecimalInfo.DEFINING_MD_CLASS, mdTableDAO.getId());
        mdAttribute.getAttribute(MdAttributeConcreteInfo.COLUMN_NAME).setValueNoValidation(selectable.getDbColumnName());
        mdAttribute.getAttribute(MdAttributeDecimalInfo.LENGTH).setValue("20");
        mdAttribute.getAttribute(MdAttributeDecimalInfo.DECIMAL).setValue("2");
        mdAttribute.apply();
      }
      else if (mdAttributeIF instanceof MdAttributeDoubleDAOIF)
      {
        MdAttributeDoubleDAO mdAttribute = MdAttributeDoubleDAO.newInstance();
        mdAttribute.setValue(MdAttributeDoubleInfo.NAME, attributeName);
        mdAttribute.setStructValue(MdAttributeDoubleInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, mdAttributeIF.getDisplayLabel(Session.getCurrentLocale()));
        mdAttribute.setValue(MdAttributeDoubleInfo.DEFINING_MD_CLASS, mdTableDAO.getId());
        mdAttribute.getAttribute(MdAttributeConcreteInfo.COLUMN_NAME).setValueNoValidation(selectable.getDbColumnName());
        mdAttribute.getAttribute(MdAttributeDoubleInfo.LENGTH).setValue("20");
        mdAttribute.getAttribute(MdAttributeDoubleInfo.DECIMAL).setValue("2");
        mdAttribute.apply();
      }
      else if (mdAttributeIF instanceof MdAttributeFloatDAOIF)
      {
        MdAttributeFloatDAO mdAttribute = MdAttributeFloatDAO.newInstance();
        mdAttribute.setValue(MdAttributeFloatInfo.NAME, attributeName);
        mdAttribute.setStructValue(MdAttributeFloatInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, mdAttributeIF.getDisplayLabel(Session.getCurrentLocale()));
        mdAttribute.setValue(MdAttributeFloatInfo.DEFINING_MD_CLASS, mdTableDAO.getId());
        mdAttribute.getAttribute(MdAttributeConcreteInfo.COLUMN_NAME).setValueNoValidation(selectable.getDbColumnName());
        mdAttribute.getAttribute(MdAttributeFloatInfo.LENGTH).setValue("20");
        mdAttribute.getAttribute(MdAttributeFloatInfo.DECIMAL).setValue("2");
        mdAttribute.apply();
      }
      else if (mdAttributeIF instanceof MdAttributeIntegerDAOIF)
      {
        Boolean isAggregateFunction = data.containsKey(SelectableAggregate.class.getName()) ? (Boolean) data.get(SelectableAggregate.class.getName()) : false;

        if (!isAggregateFunction)
        {
          MdAttributeIntegerDAO mdAttribute = MdAttributeIntegerDAO.newInstance();
          mdAttribute.setValue(MdAttributeIntegerInfo.NAME, attributeName);
          mdAttribute.setStructValue(MdAttributeIntegerInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, mdAttributeIF.getDisplayLabel(Session.getCurrentLocale()));
          mdAttribute.setValue(MdAttributeIntegerInfo.DEFINING_MD_CLASS, mdTableDAO.getId());
          mdAttribute.getAttribute(MdAttributeConcreteInfo.COLUMN_NAME).setValueNoValidation(selectable.getDbColumnName());
          mdAttribute.apply();
        }
        else
        {
          MdAttributeDoubleDAO mdAttribute = MdAttributeDoubleDAO.newInstance();
          mdAttribute.setValue(MdAttributeDoubleInfo.NAME, attributeName);
          mdAttribute.setStructValue(MdAttributeDoubleInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, mdAttributeIF.getDisplayLabel(Session.getCurrentLocale()));
          mdAttribute.setValue(MdAttributeDoubleInfo.DEFINING_MD_CLASS, mdTableDAO.getId());
          mdAttribute.getAttribute(MdAttributeConcreteInfo.COLUMN_NAME).setValueNoValidation(selectable.getDbColumnName());
          mdAttribute.getAttribute(MdAttributeDoubleInfo.LENGTH).setValue("20");
          mdAttribute.getAttribute(MdAttributeDoubleInfo.DECIMAL).setValue("2");
          mdAttribute.apply();
        }
      }
      else if (mdAttributeIF instanceof MdAttributeLongDAOIF)
      {
        Boolean isAggregateFunction = data.containsKey(SelectableAggregate.class.getName()) ? (Boolean) data.get(SelectableAggregate.class.getName()) : false;

        if (!isAggregateFunction)
        {
          MdAttributeLongDAO mdAttribute = MdAttributeLongDAO.newInstance();
          mdAttribute.setValue(MdAttributeLongInfo.NAME, attributeName);
          mdAttribute.setStructValue(MdAttributeLongInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, mdAttributeIF.getDisplayLabel(Session.getCurrentLocale()));
          mdAttribute.setValue(MdAttributeLongInfo.DEFINING_MD_CLASS, mdTableDAO.getId());
          mdAttribute.getAttribute(MdAttributeConcreteInfo.COLUMN_NAME).setValueNoValidation(selectable.getDbColumnName());
          mdAttribute.apply();
        }
        else
        {
          MdAttributeDoubleDAO mdAttribute = MdAttributeDoubleDAO.newInstance();
          mdAttribute.setValue(MdAttributeDoubleInfo.NAME, attributeName);
          mdAttribute.setStructValue(MdAttributeDoubleInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, mdAttributeIF.getDisplayLabel(Session.getCurrentLocale()));
          mdAttribute.setValue(MdAttributeDoubleInfo.DEFINING_MD_CLASS, mdTableDAO.getId());
          mdAttribute.getAttribute(MdAttributeConcreteInfo.COLUMN_NAME).setValueNoValidation(selectable.getDbColumnName());
          mdAttribute.getAttribute(MdAttributeDoubleInfo.LENGTH).setValue("20");
          mdAttribute.getAttribute(MdAttributeDoubleInfo.DECIMAL).setValue("2");
          mdAttribute.apply();
        }
      }
      else if (mdAttributeIF instanceof MdAttributeMultiPolygon)
      {
        MdAttributeMultiPolygonDAO mdAttribute = MdAttributeMultiPolygonDAO.newInstance();
        mdAttribute.setValue(MdAttributeMultiPolygonInfo.NAME, attributeName);
        mdAttribute.setStructValue(MdAttributeMultiPolygonInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, mdAttributeIF.getDisplayLabel(Session.getCurrentLocale()));
        mdAttribute.setValue(MdAttributeMultiPolygonInfo.DEFINING_MD_CLASS, mdTableDAO.getId());
        mdAttribute.getAttribute(MdAttributeConcreteInfo.COLUMN_NAME).setValueNoValidation(selectable.getDbColumnName());
        mdAttribute.apply();
      }      
      else if (mdAttributeIF instanceof MdAttributePolygon)
      {
        MdAttributePolygonDAO mdAttribute = MdAttributePolygonDAO.newInstance();
        mdAttribute.setValue(MdAttributePolygonInfo.NAME, attributeName);
        mdAttribute.setStructValue(MdAttributePolygonInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, mdAttributeIF.getDisplayLabel(Session.getCurrentLocale()));
        mdAttribute.setValue(MdAttributePolygonInfo.DEFINING_MD_CLASS, mdTableDAO.getId());
        mdAttribute.getAttribute(MdAttributeConcreteInfo.COLUMN_NAME).setValueNoValidation(selectable.getDbColumnName());
        mdAttribute.apply();
      }      
      else if (mdAttributeIF instanceof MdAttributeMultiPoint)
      {
        MdAttributeMultiPointDAO mdAttribute = MdAttributeMultiPointDAO.newInstance();
        mdAttribute.setValue(MdAttributeMultiPointInfo.NAME, attributeName);
        mdAttribute.setStructValue(MdAttributeMultiPointInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, mdAttributeIF.getDisplayLabel(Session.getCurrentLocale()));
        mdAttribute.setValue(MdAttributeMultiPointInfo.DEFINING_MD_CLASS, mdTableDAO.getId());
        mdAttribute.getAttribute(MdAttributeConcreteInfo.COLUMN_NAME).setValueNoValidation(selectable.getDbColumnName());
        mdAttribute.apply();
      }      
      else if (mdAttributeIF instanceof MdAttributePoint)
      {
        MdAttributePointDAO mdAttribute = MdAttributePointDAO.newInstance();
        mdAttribute.setValue(MdAttributePointInfo.NAME, attributeName);
        mdAttribute.setStructValue(MdAttributePointInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, mdAttributeIF.getDisplayLabel(Session.getCurrentLocale()));
        mdAttribute.setValue(MdAttributePointInfo.DEFINING_MD_CLASS, mdTableDAO.getId());
        mdAttribute.getAttribute(MdAttributeConcreteInfo.COLUMN_NAME).setValueNoValidation(selectable.getDbColumnName());
        mdAttribute.apply();
      }      
      else
      {
        throw new ProgrammingErrorException("Unsupported selectable type: " + mdAttributeIF.getType());
      }
    }
  }
}
