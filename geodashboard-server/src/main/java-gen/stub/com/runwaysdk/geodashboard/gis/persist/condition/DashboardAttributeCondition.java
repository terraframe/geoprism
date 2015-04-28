package com.runwaysdk.geodashboard.gis.persist.condition;

public abstract class DashboardAttributeCondition extends DashboardAttributeConditionBase implements com.runwaysdk.generation.loader.Reloadable
{

  /**
   * Condition type for restricting on an attribute
   */
  public static final String CONDITION_TYPE   = "ATTRIBUTE_CONDITION";

  /**
   * Magic value for the json attribute name which specifies the id of the MdAttribute
   */
  public static final String MD_ATTRIBUTE_KEY = "mdAttribute";

  private static final long  serialVersionUID = -1180566371;

  public DashboardAttributeCondition()
  {
    super();
  }

  @Override
  public String getJSONKey()
  {
    return this.getDefiningMdAttributeId();
  }

  @Override
  protected void populate(DashboardCondition source)
  {
    super.populate(source);

    this.setDefiningMdAttribute( ( (DashboardAttributeCondition) source ).getDefiningMdAttribute());
  }
}
