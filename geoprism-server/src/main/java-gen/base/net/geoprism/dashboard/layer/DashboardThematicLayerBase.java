/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Geoprism(tm).
 *
 * Geoprism(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Geoprism(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Geoprism(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism.dashboard.layer;

@com.runwaysdk.business.ClassSignature(hash = 316762480)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to DashboardThematicLayer.java
 *
 * @author Autogenerated by RunwaySDK
 */
public abstract class DashboardThematicLayerBase extends net.geoprism.dashboard.layer.DashboardLayer 
{
  public final static String CLASS = "net.geoprism.dashboard.layer.DashboardThematicLayer";
  public static java.lang.String AGGREGATIONSTRATEGY = "aggregationStrategy";
  public static java.lang.String AGGREGATIONTYPE = "aggregationType";
  public static java.lang.String GEONODE = "geoNode";
  public static java.lang.String MDATTRIBUTE = "mdAttribute";
  private static final long serialVersionUID = 316762480;
  
  public DashboardThematicLayerBase()
  {
    super();
  }
  
  public net.geoprism.dashboard.AggregationStrategy getAggregationStrategy()
  {
    if (getValue(AGGREGATIONSTRATEGY).trim().equals(""))
    {
      return null;
    }
    else
    {
      return net.geoprism.dashboard.AggregationStrategy.get(getValue(AGGREGATIONSTRATEGY));
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
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.dashboard.layer.DashboardThematicLayer.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF)mdClassIF.definesAttribute(AGGREGATIONSTRATEGY);
  }
  
  public void setAggregationStrategy(net.geoprism.dashboard.AggregationStrategy value)
  {
    if(value == null)
    {
      setValue(AGGREGATIONSTRATEGY, "");
    }
    else
    {
      setValue(AGGREGATIONSTRATEGY, value.getOid());
    }
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<net.geoprism.dashboard.AllAggregationType> getAggregationType()
  {
    return (java.util.List<net.geoprism.dashboard.AllAggregationType>) getEnumValues(AGGREGATIONTYPE);
  }
  
  public void addAggregationType(net.geoprism.dashboard.AllAggregationType value)
  {
    if(value != null)
    {
      addEnumItem(AGGREGATIONTYPE, value.getOid());
    }
  }
  
  public void removeAggregationType(net.geoprism.dashboard.AllAggregationType value)
  {
    if(value != null)
    {
      removeEnumItem(AGGREGATIONTYPE, value.getOid());
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
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.dashboard.layer.DashboardThematicLayer.CLASS);
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
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.dashboard.layer.DashboardThematicLayer.CLASS);
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
      setValue(GEONODE, value.getOid());
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
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.dashboard.layer.DashboardThematicLayer.CLASS);
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
      setValue(MDATTRIBUTE, value.getOid());
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
  
  public static DashboardThematicLayer get(String oid)
  {
    return (DashboardThematicLayer) com.runwaysdk.business.Business.get(oid);
  }
  
  public static DashboardThematicLayer getByKey(String key)
  {
    return (DashboardThematicLayer) com.runwaysdk.business.Business.get(CLASS, key);
  }
  
  public java.lang.String applyWithStyleAndStrategy(net.geoprism.dashboard.DashboardStyle style, java.lang.String mapId, net.geoprism.dashboard.AggregationStrategy strategy, java.lang.String state)
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.dashboard.layer.DashboardThematicLayer.java";
    throw new com.runwaysdk.dataaccess.metadata.ForbiddenMethodException(msg);
  }
  
  public static final java.lang.String applyWithStyleAndStrategy(java.lang.String oid, net.geoprism.dashboard.DashboardStyle style, java.lang.String mapId, net.geoprism.dashboard.AggregationStrategy strategy, java.lang.String state)
  {
    DashboardThematicLayer _instance = DashboardThematicLayer.get(oid);
    return _instance.applyWithStyleAndStrategy(style, mapId, strategy, state);
  }
  
  public java.lang.String getFeatureInformation(java.lang.String featureId)
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.dashboard.layer.DashboardThematicLayer.java";
    throw new com.runwaysdk.dataaccess.metadata.ForbiddenMethodException(msg);
  }
  
  public static final java.lang.String getFeatureInformation(java.lang.String oid, java.lang.String featureId)
  {
    DashboardThematicLayer _instance = DashboardThematicLayer.get(oid);
    return _instance.getFeatureInformation(featureId);
  }
  
  public static java.lang.String getGeoNodeGeometryTypesJSON(java.lang.String geoNodeId)
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.dashboard.layer.DashboardThematicLayer.java";
    throw new com.runwaysdk.dataaccess.metadata.ForbiddenMethodException(msg);
  }
  
  public static java.lang.String getOptionsJSON(java.lang.String thematicAttributeId, java.lang.String dashboardId)
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.dashboard.layer.DashboardThematicLayer.java";
    throw new com.runwaysdk.dataaccess.metadata.ForbiddenMethodException(msg);
  }
  
  public static DashboardThematicLayer lock(java.lang.String oid)
  {
    DashboardThematicLayer _instance = DashboardThematicLayer.get(oid);
    _instance.lock();
    
    return _instance;
  }
  
  public static DashboardThematicLayer unlock(java.lang.String oid)
  {
    DashboardThematicLayer _instance = DashboardThematicLayer.get(oid);
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
