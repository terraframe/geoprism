package com.runwayskd.geodashboard.etl;

import com.runwaysdk.business.Transient;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeTermDAOIF;
import com.runwaysdk.geodashboard.ontology.Classifier;

public class TargetFieldClassifier extends TargetFieldBasic implements TargetFieldIF
{
  private String packageName;

  public void setPackageName(String packageName)
  {
    this.packageName = packageName;
  }

  public String getPackageName()
  {
    return packageName;
  }

  @Override
  public Object getValue(MdAttributeConcreteDAOIF mdAttribute, Transient source)
  {
    String value = source.getValue(this.getSourceAttributeName());

    if (value != null && value.length() > 0)
    {
      MdAttributeTermDAOIF mdAttributeTerm = (MdAttributeTermDAOIF) mdAttribute;

      Classifier classifier = Classifier.findClassifierAddIfNotExist(this.packageName, value.toString(), mdAttributeTerm);

      return classifier.getId();
    }

    return null;
  }
}
