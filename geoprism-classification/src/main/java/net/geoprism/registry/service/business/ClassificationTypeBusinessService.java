package net.geoprism.registry.service.business;

import org.commongeoregistry.adapter.constants.DefaultAttribute;
import org.commongeoregistry.adapter.dataaccess.LocalizedValue;
import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.graph.MdClassificationInfo;
import com.runwaysdk.dataaccess.MdClassificationDAOIF;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.dataaccess.metadata.graph.MdClassificationDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;

import net.geoprism.registry.RegistryConstants;
import net.geoprism.registry.conversion.LocalizedValueConverter;
import net.geoprism.registry.model.ClassificationType;
import net.geoprism.registry.query.ClassificationTypePageQuery;
import net.geoprism.registry.view.Page;

@Service
public class ClassificationTypeBusinessService implements ClassificationTypeBusinessServiceIF
{

  @Override
  public void assignPermissions(ClassificationType type)
  {
  }

  @Override
  public void validateName(String name)
  {
  }

  @Transaction
  @Override
  public void delete(ClassificationType type)
  {
    type.getMdClassification().getBusinessDAO().delete();
  }

  @Override
  public ClassificationType apply(JsonObject json)
  {
    String oid = ( json.has(MdClassificationInfo.OID) && !json.get(MdClassificationInfo.OID).isJsonNull() ) ? json.get(MdClassificationInfo.OID).getAsString() : null;
    String code = json.get(DefaultAttribute.CODE.getName()).getAsString();
    LocalizedValue displayLabel = LocalizedValue.fromJSON(json.get(MdClassificationInfo.DISPLAY_LABEL).getAsJsonObject());
    LocalizedValue description = LocalizedValue.fromJSON(json.get(MdClassificationInfo.DESCRIPTION).getAsJsonObject());

    return apply(oid, code, displayLabel, description);
  }

  @Override
  @Transaction
  public ClassificationType apply(String oid, String code, LocalizedValue displayLabel, LocalizedValue description)
  {
    MdClassificationDAO mdClassification = null;

    if (oid != null)
    {
      mdClassification = (MdClassificationDAO) MdClassificationDAO.get(oid).getBusinessDAO();
    }
    else
    {
      this.validateName(code);

      mdClassification = MdClassificationDAO.newInstance();
      mdClassification.setValue(MdClassificationInfo.PACKAGE, RegistryConstants.CLASSIFICATION_PACKAGE);
      mdClassification.setValue(MdClassificationInfo.TYPE_NAME, code);
      mdClassification.setValue(MdClassificationInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    }

    LocalizedValueConverter.populate(mdClassification, MdClassificationInfo.DISPLAY_LABEL, displayLabel);

    LocalizedValueConverter.populate(mdClassification, MdClassificationInfo.DESCRIPTION, description);

    boolean isNew = mdClassification.isNew() && !mdClassification.isAppliedToDB();

    mdClassification.apply();

    ClassificationType classificationType = new ClassificationType(mdClassification);

    if (isNew)
    {
      this.assignPermissions(classificationType);
    }

    return classificationType;
  }

  @Override
  public Page<ClassificationType> page(JsonObject criteria)
  {
    return new ClassificationTypePageQuery(criteria).getPage();
  }

  @Override
  public ClassificationType get(String oid)
  {
    return new ClassificationType((MdClassificationDAOIF) MdClassificationDAO.get(oid));
  }

  @Override
  public ClassificationType getByCode(String code)
  {
    return this.getByCode(code, true);
  }

  @Override
  public ClassificationType getByCode(String code, boolean throwException)
  {
    String classificationType = RegistryConstants.CLASSIFICATION_PACKAGE + "." + code;

    try
    {
      MdClassificationDAOIF mdClassification = (MdClassificationDAOIF) MdClassificationDAO.get(MdClassificationInfo.CLASS, classificationType);

      return new ClassificationType(mdClassification);
    }
    catch (DataNotFoundException e)
    {
      if (throwException)
      {
        throw e;
      }

      return null;
    }
  }

}
