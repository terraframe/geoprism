package com.runwaysdk.geodashboard.gis.persist.condition;

@com.runwaysdk.business.ClassSignature(hash = -2104714789)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to DashboardPrimitive.java
 *
 * @author Autogenerated by RunwaySDK
 */
public abstract class DashboardPrimitiveBase extends com.runwaysdk.geodashboard.gis.persist.condition.DashboardCondition implements com.runwaysdk.generation.loader.Reloadable
{
  public final static String CLASS = "com.runwaysdk.geodashboard.gis.persist.condition.DashboardPrimitive";
  public static java.lang.String COMPARISONVALUE = "comparisonValue";
  private static final long serialVersionUID = -2104714789;
  
  public DashboardPrimitiveBase()
  {
    super();
  }
  
  public String getComparisonValue()
  {
    return getValue(COMPARISONVALUE);
  }
  
  public void validateComparisonValue()
  {
    this.validateAttribute(COMPARISONVALUE);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeDAOIF getComparisonValueMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.geodashboard.gis.persist.condition.DashboardPrimitive.CLASS);
    return mdClassIF.definesAttribute(COMPARISONVALUE);
  }
  
  public void setComparisonValue(String value)
  {
    if(value == null)
    {
      setValue(COMPARISONVALUE, "");
    }
    else
    {
      setValue(COMPARISONVALUE, value);
    }
  }
  
  protected String getDeclaredType()
  {
    return CLASS;
  }
  
  public static DashboardPrimitiveQuery getAllInstances(String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    DashboardPrimitiveQuery query = new DashboardPrimitiveQuery(new com.runwaysdk.query.QueryFactory());
    com.runwaysdk.business.Entity.getAllInstances(query, sortAttribute, ascending, pageSize, pageNumber);
    return query;
  }
  
  public static DashboardPrimitive get(String id)
  {
    return (DashboardPrimitive) com.runwaysdk.business.Business.get(id);
  }
  
  public static DashboardPrimitive getByKey(String key)
  {
    return (DashboardPrimitive) com.runwaysdk.business.Business.get(CLASS, key);
  }
  
  public static DashboardPrimitive lock(java.lang.String id)
  {
    DashboardPrimitive _instance = DashboardPrimitive.get(id);
    _instance.lock();
    
    return _instance;
  }
  
  public static DashboardPrimitive unlock(java.lang.String id)
  {
    DashboardPrimitive _instance = DashboardPrimitive.get(id);
    _instance.unlock();
    
    return _instance;
  }
  
  public String toString()
  {
    if (this.isNew())
    {
      return "New: "+ this.getClassDisplayLabel();
    }
    else
    {
      return super.toString();
    }
  }
}
