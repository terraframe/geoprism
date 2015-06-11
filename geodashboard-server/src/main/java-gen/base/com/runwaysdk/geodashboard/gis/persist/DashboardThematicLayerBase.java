package com.runwaysdk.geodashboard.gis.persist;

@com.runwaysdk.business.ClassSignature(hash = 1124591320)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to DashboardThematicLayer.java
 *
 * @author Autogenerated by RunwaySDK
 */
public abstract class DashboardThematicLayerBase extends com.runwaysdk.geodashboard.gis.persist.DashboardLayer implements com.runwaysdk.generation.loader.Reloadable
{
  public final static String CLASS = "com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayer";
  public static java.lang.String AGGREGATIONSTRATEGY = "aggregationStrategy";
  public static java.lang.String AGGREGATIONTYPE = "aggregationType";
  public static java.lang.String GEONODE = "geoNode";
  public static java.lang.String MDATTRIBUTE = "mdAttribute";
  private static final long serialVersionUID = 1124591320;
  
  public DashboardThematicLayerBase()
  {
    super();
  }
  
  public com.runwaysdk.geodashboard.gis.persist.AggregationStrategy getAggregationStrategy()
  {
    if (getValue(AGGREGATIONSTRATEGY).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.geodashboard.gis.persist.AggregationStrategy.get(getValue(AGGREGATIONSTRATEGY));
    }
  }
  
  public String getAggregationStrategyId()
  {
    return getValue(AGGREGATIONSTRATEGY);
  }
  
  public void validateAggregationStrategy()
  {
    this.validateAttribute(AGGREGATIONSTRATEGY);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF getAggregationStrategyMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayer.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF)mdClassIF.definesAttribute(AGGREGATIONSTRATEGY);
  }
  
  public void setAggregationStrategy(com.runwaysdk.geodashboard.gis.persist.AggregationStrategy value)
  {
    if(value == null)
    {
      setValue(AGGREGATIONSTRATEGY, "");
    }
    else
    {
      setValue(AGGREGATIONSTRATEGY, value.getId());
    }
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<com.runwaysdk.geodashboard.gis.persist.AllAggregationType> getAggregationType()
  {
    return (java.util.List<com.runwaysdk.geodashboard.gis.persist.AllAggregationType>) getEnumValues(AGGREGATIONTYPE);
  }
  
  public void addAggregationType(com.runwaysdk.geodashboard.gis.persist.AllAggregationType value)
  {
    if(value != null)
    {
      addEnumItem(AGGREGATIONTYPE, value.getId());
    }
  }
  
  public void removeAggregationType(com.runwaysdk.geodashboard.gis.persist.AllAggregationType value)
  {
    if(value != null)
    {
      removeEnumItem(AGGREGATIONTYPE, value.getId());
    }
  }
  
  public void clearAggregationType()
  {
    clearEnum(AGGREGATIONTYPE);
  }
  
  public void validateAggregationType()
  {
    this.validateAttribute(AGGREGATIONTYPE);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeEnumerationDAOIF getAggregationTypeMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayer.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeEnumerationDAOIF)mdClassIF.definesAttribute(AGGREGATIONTYPE);
  }
  
  public com.runwaysdk.system.gis.geo.GeoNode getGeoNode()
  {
    if (getValue(GEONODE).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.gis.geo.GeoNode.get(getValue(GEONODE));
    }
  }
  
  public String getGeoNodeId()
  {
    return getValue(GEONODE);
  }
  
  public void validateGeoNode()
  {
    this.validateAttribute(GEONODE);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF getGeoNodeMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayer.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF)mdClassIF.definesAttribute(GEONODE);
  }
  
  public void setGeoNode(com.runwaysdk.system.gis.geo.GeoNode value)
  {
    if(value == null)
    {
      setValue(GEONODE, "");
    }
    else
    {
      setValue(GEONODE, value.getId());
    }
  }
  
  public com.runwaysdk.system.metadata.MdAttribute getMdAttribute()
  {
    if (getValue(MDATTRIBUTE).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.metadata.MdAttribute.get(getValue(MDATTRIBUTE));
    }
  }
  
  public String getMdAttributeId()
  {
    return getValue(MDATTRIBUTE);
  }
  
  public void validateMdAttribute()
  {
    this.validateAttribute(MDATTRIBUTE);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF getMdAttributeMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayer.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF)mdClassIF.definesAttribute(MDATTRIBUTE);
  }
  
  public void setMdAttribute(com.runwaysdk.system.metadata.MdAttribute value)
  {
    if(value == null)
    {
      setValue(MDATTRIBUTE, "");
    }
    else
    {
      setValue(MDATTRIBUTE, value.getId());
    }
  }
  
  protected String getDeclaredType()
  {
    return CLASS;
  }
  
  public static DashboardThematicLayerQuery getAllInstances(String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    DashboardThematicLayerQuery query = new DashboardThematicLayerQuery(new com.runwaysdk.query.QueryFactory());
    com.runwaysdk.business.Entity.getAllInstances(query, sortAttribute, ascending, pageSize, pageNumber);
    return query;
  }
  
  public static DashboardThematicLayer get(String id)
  {
    return (DashboardThematicLayer) com.runwaysdk.business.Business.get(id);
  }
  
  public static DashboardThematicLayer getByKey(String key)
  {
    return (DashboardThematicLayer) com.runwaysdk.business.Business.get(CLASS, key);
  }
  
  public static DashboardThematicLayer lock(java.lang.String id)
  {
    DashboardThematicLayer _instance = DashboardThematicLayer.get(id);
    _instance.lock();
    
    return _instance;
  }
  
  public static DashboardThematicLayer unlock(java.lang.String id)
  {
    DashboardThematicLayer _instance = DashboardThematicLayer.get(id);
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
