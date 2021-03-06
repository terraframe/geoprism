/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
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

@com.runwaysdk.business.ClassSignature(hash = -1551614695)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to DashboardThematicLayer.java
 *
 * @author Autogenerated by RunwaySDK
 */
public  class DashboardThematicLayerQuery extends net.geoprism.dashboard.layer.DashboardLayerQuery
 
{

  public DashboardThematicLayerQuery(com.runwaysdk.query.QueryFactory componentQueryFactory)
  {
    super(componentQueryFactory);
    if (this.getComponentQuery() == null)
    {
      com.runwaysdk.business.BusinessQuery businessQuery = componentQueryFactory.businessQuery(this.getClassType());

       this.setBusinessQuery(businessQuery);
    }
  }

  public DashboardThematicLayerQuery(com.runwaysdk.query.ValueQuery valueQuery)
  {
    super(valueQuery);
    if (this.getComponentQuery() == null)
    {
      com.runwaysdk.business.BusinessQuery businessQuery = new com.runwaysdk.business.BusinessQuery(valueQuery, this.getClassType());

       this.setBusinessQuery(businessQuery);
    }
  }

  public String getClassType()
  {
    return net.geoprism.dashboard.layer.DashboardThematicLayer.CLASS;
  }
  public net.geoprism.dashboard.AggregationStrategyQuery.AggregationStrategyQueryReferenceIF getAggregationStrategy()
  {
    return getAggregationStrategy(null);

  }
 
  public net.geoprism.dashboard.AggregationStrategyQuery.AggregationStrategyQueryReferenceIF getAggregationStrategy(String alias)
  {

    com.runwaysdk.dataaccess.MdAttributeDAOIF mdAttributeIF = this.getComponentQuery().getMdAttributeROfromMap(net.geoprism.dashboard.layer.DashboardThematicLayer.AGGREGATIONSTRATEGY);

    return (net.geoprism.dashboard.AggregationStrategyQuery.AggregationStrategyQueryReferenceIF)this.getComponentQuery().internalAttributeFactory(net.geoprism.dashboard.layer.DashboardThematicLayer.AGGREGATIONSTRATEGY, mdAttributeIF, this, alias, null);

  }
 
  public net.geoprism.dashboard.AggregationStrategyQuery.AggregationStrategyQueryReferenceIF getAggregationStrategy(String alias, String displayLabel)
  {

    com.runwaysdk.dataaccess.MdAttributeDAOIF mdAttributeIF = this.getComponentQuery().getMdAttributeROfromMap(net.geoprism.dashboard.layer.DashboardThematicLayer.AGGREGATIONSTRATEGY);

    return (net.geoprism.dashboard.AggregationStrategyQuery.AggregationStrategyQueryReferenceIF)this.getComponentQuery().internalAttributeFactory(net.geoprism.dashboard.layer.DashboardThematicLayer.AGGREGATIONSTRATEGY, mdAttributeIF, this, alias, displayLabel);

  }
  public net.geoprism.dashboard.AggregationTypeQuery.AllAggregationTypeQueryIF getAggregationType()
  {
    return getAggregationType(null);

  }
 
  public net.geoprism.dashboard.AggregationTypeQuery.AllAggregationTypeQueryIF getAggregationType(String alias)
  {

    com.runwaysdk.dataaccess.MdAttributeDAOIF mdAttributeIF = this.getComponentQuery().getMdAttributeROfromMap(net.geoprism.dashboard.layer.DashboardThematicLayer.AGGREGATIONTYPE);

    return (net.geoprism.dashboard.AggregationTypeQuery.AllAggregationTypeQueryIF)this.getComponentQuery().internalAttributeFactory(net.geoprism.dashboard.layer.DashboardThematicLayer.AGGREGATIONTYPE, mdAttributeIF, this, alias, null);

  }
 
  public net.geoprism.dashboard.AggregationTypeQuery.AllAggregationTypeQueryIF getAggregationType(String alias, String displayLabel)
  {

    com.runwaysdk.dataaccess.MdAttributeDAOIF mdAttributeIF = this.getComponentQuery().getMdAttributeROfromMap(net.geoprism.dashboard.layer.DashboardThematicLayer.AGGREGATIONTYPE);

    return (net.geoprism.dashboard.AggregationTypeQuery.AllAggregationTypeQueryIF)this.getComponentQuery().internalAttributeFactory(net.geoprism.dashboard.layer.DashboardThematicLayer.AGGREGATIONTYPE, mdAttributeIF, this, alias, displayLabel);

  }
  public com.runwaysdk.system.gis.geo.GeoNodeQuery.GeoNodeQueryReferenceIF getGeoNode()
  {
    return getGeoNode(null);

  }
 
  public com.runwaysdk.system.gis.geo.GeoNodeQuery.GeoNodeQueryReferenceIF getGeoNode(String alias)
  {

    com.runwaysdk.dataaccess.MdAttributeDAOIF mdAttributeIF = this.getComponentQuery().getMdAttributeROfromMap(net.geoprism.dashboard.layer.DashboardThematicLayer.GEONODE);

    return (com.runwaysdk.system.gis.geo.GeoNodeQuery.GeoNodeQueryReferenceIF)this.getComponentQuery().internalAttributeFactory(net.geoprism.dashboard.layer.DashboardThematicLayer.GEONODE, mdAttributeIF, this, alias, null);

  }
 
  public com.runwaysdk.system.gis.geo.GeoNodeQuery.GeoNodeQueryReferenceIF getGeoNode(String alias, String displayLabel)
  {

    com.runwaysdk.dataaccess.MdAttributeDAOIF mdAttributeIF = this.getComponentQuery().getMdAttributeROfromMap(net.geoprism.dashboard.layer.DashboardThematicLayer.GEONODE);

    return (com.runwaysdk.system.gis.geo.GeoNodeQuery.GeoNodeQueryReferenceIF)this.getComponentQuery().internalAttributeFactory(net.geoprism.dashboard.layer.DashboardThematicLayer.GEONODE, mdAttributeIF, this, alias, displayLabel);

  }
  public com.runwaysdk.system.metadata.MdAttributeQuery.MdAttributeQueryReferenceIF getMdAttribute()
  {
    return getMdAttribute(null);

  }
 
  public com.runwaysdk.system.metadata.MdAttributeQuery.MdAttributeQueryReferenceIF getMdAttribute(String alias)
  {

    com.runwaysdk.dataaccess.MdAttributeDAOIF mdAttributeIF = this.getComponentQuery().getMdAttributeROfromMap(net.geoprism.dashboard.layer.DashboardThematicLayer.MDATTRIBUTE);

    return (com.runwaysdk.system.metadata.MdAttributeQuery.MdAttributeQueryReferenceIF)this.getComponentQuery().internalAttributeFactory(net.geoprism.dashboard.layer.DashboardThematicLayer.MDATTRIBUTE, mdAttributeIF, this, alias, null);

  }
 
  public com.runwaysdk.system.metadata.MdAttributeQuery.MdAttributeQueryReferenceIF getMdAttribute(String alias, String displayLabel)
  {

    com.runwaysdk.dataaccess.MdAttributeDAOIF mdAttributeIF = this.getComponentQuery().getMdAttributeROfromMap(net.geoprism.dashboard.layer.DashboardThematicLayer.MDATTRIBUTE);

    return (com.runwaysdk.system.metadata.MdAttributeQuery.MdAttributeQueryReferenceIF)this.getComponentQuery().internalAttributeFactory(net.geoprism.dashboard.layer.DashboardThematicLayer.MDATTRIBUTE, mdAttributeIF, this, alias, displayLabel);

  }
  protected com.runwaysdk.query.AttributeReference referenceFactory( com.runwaysdk.dataaccess.MdAttributeRefDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias,  com.runwaysdk.dataaccess.MdBusinessDAOIF referenceMdBusinessIF, String referenceTableAlias, com.runwaysdk.query.ComponentQuery rootQuery, java.util.Set<com.runwaysdk.query.Join> tableJoinSet, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    String name = mdAttributeIF.definesAttribute();
    
    if (name.equals(net.geoprism.dashboard.layer.DashboardThematicLayer.AGGREGATIONSTRATEGY)) 
    {
       return new net.geoprism.dashboard.AggregationStrategyQuery.AggregationStrategyQueryReference((com.runwaysdk.dataaccess.MdAttributeRefDAOIF)mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, referenceMdBusinessIF, referenceTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (name.equals(net.geoprism.dashboard.layer.DashboardThematicLayer.GEONODE)) 
    {
       return new com.runwaysdk.system.gis.geo.GeoNodeQuery.GeoNodeQueryReference((com.runwaysdk.dataaccess.MdAttributeRefDAOIF)mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, referenceMdBusinessIF, referenceTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (name.equals(net.geoprism.dashboard.layer.DashboardThematicLayer.MDATTRIBUTE)) 
    {
       return new com.runwaysdk.system.metadata.MdAttributeQuery.MdAttributeQueryReference((com.runwaysdk.dataaccess.MdAttributeRefDAOIF)mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, referenceMdBusinessIF, referenceTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else 
    {
      return super.referenceFactory(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, referenceMdBusinessIF, referenceTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
  }

  protected com.runwaysdk.query.AttributeEnumeration enumerationFactory( com.runwaysdk.dataaccess.MdAttributeEnumerationDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias,  String mdEnumerationTableName, com.runwaysdk.dataaccess.MdBusinessDAOIF masterListMdBusinessIF, String masterListTalbeAlias, com.runwaysdk.query.ComponentQuery rootQuery, java.util.Set<com.runwaysdk.query.Join> tableJoinSet, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    String name = mdAttributeIF.definesAttribute();
    
    if (name.equals(net.geoprism.dashboard.layer.DashboardThematicLayer.AGGREGATIONTYPE)) 
    {
       return new net.geoprism.dashboard.AggregationTypeQuery.AllAggregationTypeQuery((com.runwaysdk.dataaccess.MdAttributeEnumerationDAOIF)mdAttributeIF,  attributeNamespace, definingTableName, definingTableAlias, mdEnumerationTableName, masterListMdBusinessIF, masterListTalbeAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else 
    {
      return super.enumerationFactory(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, mdEnumerationTableName, masterListMdBusinessIF, masterListTalbeAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
  }

  /**  
   * Returns an iterator of Business objects that match the query criteria specified
   * on this query object. 
   * @return iterator of Business objects that match the query criteria specified
   * on this query object.
   */
  public com.runwaysdk.query.OIterator<? extends DashboardThematicLayer> getIterator()
  {
    this.checkNotUsedInValueQuery();
    String sqlStmt;
    if (_limit != null && _skip != null)
    {
      sqlStmt = this.getComponentQuery().getSQL(_limit, _skip);
    }
    else
    {
      sqlStmt = this.getComponentQuery().getSQL();
    }
    java.util.Map<String, com.runwaysdk.query.ColumnInfo> columnInfoMap = this.getComponentQuery().getColumnInfoMap();

    java.sql.ResultSet results = com.runwaysdk.dataaccess.database.Database.query(sqlStmt);
    return new com.runwaysdk.business.BusinessIterator<DashboardThematicLayer>(this.getComponentQuery().getMdEntityIF(), columnInfoMap, results);
  }


/**
 * Interface that masks all type unsafe query methods and defines all type safe methods.
 * This type is used when a join is performed on this class as a reference.
 **/
  public interface DashboardThematicLayerQueryReferenceIF extends  net.geoprism.dashboard.layer.DashboardLayerQuery.DashboardLayerQueryReferenceIF
  {

    public net.geoprism.dashboard.AggregationStrategyQuery.AggregationStrategyQueryReferenceIF getAggregationStrategy();
    public net.geoprism.dashboard.AggregationStrategyQuery.AggregationStrategyQueryReferenceIF getAggregationStrategy(String alias);
    public net.geoprism.dashboard.AggregationStrategyQuery.AggregationStrategyQueryReferenceIF getAggregationStrategy(String alias, String displayLabel);
  public net.geoprism.dashboard.AggregationTypeQuery.AllAggregationTypeQueryIF getAggregationType();
  public net.geoprism.dashboard.AggregationTypeQuery.AllAggregationTypeQueryIF getAggregationType(String alias);
  public net.geoprism.dashboard.AggregationTypeQuery.AllAggregationTypeQueryIF getAggregationType(String alias, String displayLabel);
    public com.runwaysdk.system.gis.geo.GeoNodeQuery.GeoNodeQueryReferenceIF getGeoNode();
    public com.runwaysdk.system.gis.geo.GeoNodeQuery.GeoNodeQueryReferenceIF getGeoNode(String alias);
    public com.runwaysdk.system.gis.geo.GeoNodeQuery.GeoNodeQueryReferenceIF getGeoNode(String alias, String displayLabel);
    public com.runwaysdk.system.metadata.MdAttributeQuery.MdAttributeQueryReferenceIF getMdAttribute();
    public com.runwaysdk.system.metadata.MdAttributeQuery.MdAttributeQueryReferenceIF getMdAttribute(String alias);
    public com.runwaysdk.system.metadata.MdAttributeQuery.MdAttributeQueryReferenceIF getMdAttribute(String alias, String displayLabel);

    public com.runwaysdk.query.BasicCondition EQ(net.geoprism.dashboard.layer.DashboardThematicLayer dashboardThematicLayer);

    public com.runwaysdk.query.BasicCondition NE(net.geoprism.dashboard.layer.DashboardThematicLayer dashboardThematicLayer);

  }

/**
 * Implements type safe query methods.
 * This type is used when a join is performed on this class as a reference.
 **/
  public static class DashboardThematicLayerQueryReference extends net.geoprism.dashboard.layer.DashboardLayerQuery.DashboardLayerQueryReference
 implements DashboardThematicLayerQueryReferenceIF

  {

  public DashboardThematicLayerQueryReference(com.runwaysdk.dataaccess.MdAttributeRefDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias, com.runwaysdk.dataaccess.MdBusinessDAOIF referenceMdBusinessIF, String referenceTableAlias, com.runwaysdk.query.ComponentQuery rootQuery, java.util.Set<com.runwaysdk.query.Join> tableJoinSet, String alias, String displayLabel)
  {
    super(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, referenceMdBusinessIF, referenceTableAlias, rootQuery, tableJoinSet, alias, displayLabel);

  }


    public com.runwaysdk.query.BasicCondition EQ(net.geoprism.dashboard.layer.DashboardThematicLayer dashboardThematicLayer)
    {
      if(dashboardThematicLayer == null) return this.EQ((java.lang.String)null);
      return this.EQ(dashboardThematicLayer.getOid());
    }

    public com.runwaysdk.query.BasicCondition NE(net.geoprism.dashboard.layer.DashboardThematicLayer dashboardThematicLayer)
    {
      if(dashboardThematicLayer == null) return this.NE((java.lang.String)null);
      return this.NE(dashboardThematicLayer.getOid());
    }

  public net.geoprism.dashboard.AggregationStrategyQuery.AggregationStrategyQueryReferenceIF getAggregationStrategy()
  {
    return getAggregationStrategy(null);

  }
 
  public net.geoprism.dashboard.AggregationStrategyQuery.AggregationStrategyQueryReferenceIF getAggregationStrategy(String alias)
  {
    return (net.geoprism.dashboard.AggregationStrategyQuery.AggregationStrategyQueryReferenceIF)this.get(net.geoprism.dashboard.layer.DashboardThematicLayer.AGGREGATIONSTRATEGY, alias, null);

  }
 
  public net.geoprism.dashboard.AggregationStrategyQuery.AggregationStrategyQueryReferenceIF getAggregationStrategy(String alias, String displayLabel)
  {
    return (net.geoprism.dashboard.AggregationStrategyQuery.AggregationStrategyQueryReferenceIF)this.get(net.geoprism.dashboard.layer.DashboardThematicLayer.AGGREGATIONSTRATEGY,  alias, displayLabel);

  }
  public net.geoprism.dashboard.AggregationTypeQuery.AllAggregationTypeQueryIF getAggregationType()
  {
    return getAggregationType(null);

  }
 
  public net.geoprism.dashboard.AggregationTypeQuery.AllAggregationTypeQueryIF getAggregationType(String alias)
  {
    return (net.geoprism.dashboard.AggregationTypeQuery.AllAggregationTypeQueryIF)this.get(net.geoprism.dashboard.layer.DashboardThematicLayer.AGGREGATIONTYPE, alias, null);

  }
 
  public net.geoprism.dashboard.AggregationTypeQuery.AllAggregationTypeQueryIF getAggregationType(String alias, String displayLabel)
  {
    return (net.geoprism.dashboard.AggregationTypeQuery.AllAggregationTypeQueryIF)this.get(net.geoprism.dashboard.layer.DashboardThematicLayer.AGGREGATIONTYPE, alias, displayLabel);

  }
  public com.runwaysdk.system.gis.geo.GeoNodeQuery.GeoNodeQueryReferenceIF getGeoNode()
  {
    return getGeoNode(null);

  }
 
  public com.runwaysdk.system.gis.geo.GeoNodeQuery.GeoNodeQueryReferenceIF getGeoNode(String alias)
  {
    return (com.runwaysdk.system.gis.geo.GeoNodeQuery.GeoNodeQueryReferenceIF)this.get(net.geoprism.dashboard.layer.DashboardThematicLayer.GEONODE, alias, null);

  }
 
  public com.runwaysdk.system.gis.geo.GeoNodeQuery.GeoNodeQueryReferenceIF getGeoNode(String alias, String displayLabel)
  {
    return (com.runwaysdk.system.gis.geo.GeoNodeQuery.GeoNodeQueryReferenceIF)this.get(net.geoprism.dashboard.layer.DashboardThematicLayer.GEONODE,  alias, displayLabel);

  }
  public com.runwaysdk.system.metadata.MdAttributeQuery.MdAttributeQueryReferenceIF getMdAttribute()
  {
    return getMdAttribute(null);

  }
 
  public com.runwaysdk.system.metadata.MdAttributeQuery.MdAttributeQueryReferenceIF getMdAttribute(String alias)
  {
    return (com.runwaysdk.system.metadata.MdAttributeQuery.MdAttributeQueryReferenceIF)this.get(net.geoprism.dashboard.layer.DashboardThematicLayer.MDATTRIBUTE, alias, null);

  }
 
  public com.runwaysdk.system.metadata.MdAttributeQuery.MdAttributeQueryReferenceIF getMdAttribute(String alias, String displayLabel)
  {
    return (com.runwaysdk.system.metadata.MdAttributeQuery.MdAttributeQueryReferenceIF)this.get(net.geoprism.dashboard.layer.DashboardThematicLayer.MDATTRIBUTE,  alias, displayLabel);

  }
  protected com.runwaysdk.query.AttributeReference referenceFactory( com.runwaysdk.dataaccess.MdAttributeRefDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias,  com.runwaysdk.dataaccess.MdBusinessDAOIF referenceMdBusinessIF, String referenceTableAlias, com.runwaysdk.query.ComponentQuery rootQuery, java.util.Set<com.runwaysdk.query.Join> tableJoinSet, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    String name = mdAttributeIF.definesAttribute();
    
    if (name.equals(net.geoprism.dashboard.layer.DashboardThematicLayer.AGGREGATIONSTRATEGY)) 
    {
       return new net.geoprism.dashboard.AggregationStrategyQuery.AggregationStrategyQueryReference((com.runwaysdk.dataaccess.MdAttributeRefDAOIF)mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, referenceMdBusinessIF, referenceTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (name.equals(net.geoprism.dashboard.layer.DashboardThematicLayer.GEONODE)) 
    {
       return new com.runwaysdk.system.gis.geo.GeoNodeQuery.GeoNodeQueryReference((com.runwaysdk.dataaccess.MdAttributeRefDAOIF)mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, referenceMdBusinessIF, referenceTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (name.equals(net.geoprism.dashboard.layer.DashboardThematicLayer.MDATTRIBUTE)) 
    {
       return new com.runwaysdk.system.metadata.MdAttributeQuery.MdAttributeQueryReference((com.runwaysdk.dataaccess.MdAttributeRefDAOIF)mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, referenceMdBusinessIF, referenceTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else 
    {
      return super.referenceFactory(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, referenceMdBusinessIF, referenceTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
  }

  protected com.runwaysdk.query.AttributeEnumeration enumerationFactory( com.runwaysdk.dataaccess.MdAttributeEnumerationDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias,  String mdEnumerationTableName, com.runwaysdk.dataaccess.MdBusinessDAOIF masterListMdBusinessIF, String masterListTalbeAlias, com.runwaysdk.query.ComponentQuery rootQuery, java.util.Set<com.runwaysdk.query.Join> tableJoinSet, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    String name = mdAttributeIF.definesAttribute();
    
    if (name.equals(net.geoprism.dashboard.layer.DashboardThematicLayer.AGGREGATIONTYPE)) 
    {
       return new net.geoprism.dashboard.AggregationTypeQuery.AllAggregationTypeQuery((com.runwaysdk.dataaccess.MdAttributeEnumerationDAOIF)mdAttributeIF,  attributeNamespace, definingTableName, definingTableAlias, mdEnumerationTableName, masterListMdBusinessIF, masterListTalbeAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else 
    {
      return super.enumerationFactory(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, mdEnumerationTableName, masterListMdBusinessIF, masterListTalbeAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
  }

  }

/**
 * Interface that masks all type unsafe query methods and defines all type safe methods.
 * This type is used when a join is performed on this class as a reference.
 **/
  public interface DashboardThematicLayerQueryMultiReferenceIF extends  net.geoprism.dashboard.layer.DashboardLayerQuery.DashboardLayerQueryMultiReferenceIF
  {

    public net.geoprism.dashboard.AggregationStrategyQuery.AggregationStrategyQueryReferenceIF getAggregationStrategy();
    public net.geoprism.dashboard.AggregationStrategyQuery.AggregationStrategyQueryReferenceIF getAggregationStrategy(String alias);
    public net.geoprism.dashboard.AggregationStrategyQuery.AggregationStrategyQueryReferenceIF getAggregationStrategy(String alias, String displayLabel);
  public net.geoprism.dashboard.AggregationTypeQuery.AllAggregationTypeQueryIF getAggregationType();
  public net.geoprism.dashboard.AggregationTypeQuery.AllAggregationTypeQueryIF getAggregationType(String alias);
  public net.geoprism.dashboard.AggregationTypeQuery.AllAggregationTypeQueryIF getAggregationType(String alias, String displayLabel);
    public com.runwaysdk.system.gis.geo.GeoNodeQuery.GeoNodeQueryReferenceIF getGeoNode();
    public com.runwaysdk.system.gis.geo.GeoNodeQuery.GeoNodeQueryReferenceIF getGeoNode(String alias);
    public com.runwaysdk.system.gis.geo.GeoNodeQuery.GeoNodeQueryReferenceIF getGeoNode(String alias, String displayLabel);
    public com.runwaysdk.system.metadata.MdAttributeQuery.MdAttributeQueryReferenceIF getMdAttribute();
    public com.runwaysdk.system.metadata.MdAttributeQuery.MdAttributeQueryReferenceIF getMdAttribute(String alias);
    public com.runwaysdk.system.metadata.MdAttributeQuery.MdAttributeQueryReferenceIF getMdAttribute(String alias, String displayLabel);

    public com.runwaysdk.query.Condition containsAny(net.geoprism.dashboard.layer.DashboardThematicLayer ... dashboardThematicLayer);
    public com.runwaysdk.query.Condition notContainsAny(net.geoprism.dashboard.layer.DashboardThematicLayer ... dashboardThematicLayer);
    public com.runwaysdk.query.Condition containsAll(net.geoprism.dashboard.layer.DashboardThematicLayer ... dashboardThematicLayer);
    public com.runwaysdk.query.Condition notContainsAll(net.geoprism.dashboard.layer.DashboardThematicLayer ... dashboardThematicLayer);
    public com.runwaysdk.query.Condition containsExactly(net.geoprism.dashboard.layer.DashboardThematicLayer ... dashboardThematicLayer);
  }

/**
 * Implements type safe query methods.
 * This type is used when a join is performed on this class as a reference.
 **/
  public static class DashboardThematicLayerQueryMultiReference extends net.geoprism.dashboard.layer.DashboardLayerQuery.DashboardLayerQueryMultiReference
 implements DashboardThematicLayerQueryMultiReferenceIF

  {

  public DashboardThematicLayerQueryMultiReference(com.runwaysdk.dataaccess.MdAttributeMultiReferenceDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias, String mdMultiReferenceTableName, com.runwaysdk.dataaccess.MdBusinessDAOIF referenceMdBusinessIF, String referenceTableAlias, com.runwaysdk.query.ComponentQuery rootQuery, java.util.Set<com.runwaysdk.query.Join> tableJoinSet, String alias, String displayLabel)
  {
    super(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, mdMultiReferenceTableName, referenceMdBusinessIF, referenceTableAlias, rootQuery, tableJoinSet, alias, displayLabel);

  }



    public com.runwaysdk.query.Condition containsAny(net.geoprism.dashboard.layer.DashboardThematicLayer ... dashboardThematicLayer)  {

      String[] itemIdArray = new String[dashboardThematicLayer.length]; 

      for (int i=0; i<dashboardThematicLayer.length; i++)
      {
        itemIdArray[i] = dashboardThematicLayer[i].getOid();
      }

      return this.containsAny(itemIdArray);
  }

    public com.runwaysdk.query.Condition notContainsAny(net.geoprism.dashboard.layer.DashboardThematicLayer ... dashboardThematicLayer)  {

      String[] itemIdArray = new String[dashboardThematicLayer.length]; 

      for (int i=0; i<dashboardThematicLayer.length; i++)
      {
        itemIdArray[i] = dashboardThematicLayer[i].getOid();
      }

      return this.notContainsAny(itemIdArray);
  }

    public com.runwaysdk.query.Condition containsAll(net.geoprism.dashboard.layer.DashboardThematicLayer ... dashboardThematicLayer)  {

      String[] itemIdArray = new String[dashboardThematicLayer.length]; 

      for (int i=0; i<dashboardThematicLayer.length; i++)
      {
        itemIdArray[i] = dashboardThematicLayer[i].getOid();
      }

      return this.containsAll(itemIdArray);
  }

    public com.runwaysdk.query.Condition notContainsAll(net.geoprism.dashboard.layer.DashboardThematicLayer ... dashboardThematicLayer)  {

      String[] itemIdArray = new String[dashboardThematicLayer.length]; 

      for (int i=0; i<dashboardThematicLayer.length; i++)
      {
        itemIdArray[i] = dashboardThematicLayer[i].getOid();
      }

      return this.notContainsAll(itemIdArray);
  }

    public com.runwaysdk.query.Condition containsExactly(net.geoprism.dashboard.layer.DashboardThematicLayer ... dashboardThematicLayer)  {

      String[] itemIdArray = new String[dashboardThematicLayer.length]; 

      for (int i=0; i<dashboardThematicLayer.length; i++)
      {
        itemIdArray[i] = dashboardThematicLayer[i].getOid();
      }

      return this.containsExactly(itemIdArray);
  }
  public net.geoprism.dashboard.AggregationStrategyQuery.AggregationStrategyQueryReferenceIF getAggregationStrategy()
  {
    return getAggregationStrategy(null);

  }
 
  public net.geoprism.dashboard.AggregationStrategyQuery.AggregationStrategyQueryReferenceIF getAggregationStrategy(String alias)
  {
    return (net.geoprism.dashboard.AggregationStrategyQuery.AggregationStrategyQueryReferenceIF)this.get(net.geoprism.dashboard.layer.DashboardThematicLayer.AGGREGATIONSTRATEGY, alias, null);

  }
 
  public net.geoprism.dashboard.AggregationStrategyQuery.AggregationStrategyQueryReferenceIF getAggregationStrategy(String alias, String displayLabel)
  {
    return (net.geoprism.dashboard.AggregationStrategyQuery.AggregationStrategyQueryReferenceIF)this.get(net.geoprism.dashboard.layer.DashboardThematicLayer.AGGREGATIONSTRATEGY,  alias, displayLabel);

  }
  public net.geoprism.dashboard.AggregationTypeQuery.AllAggregationTypeQueryIF getAggregationType()
  {
    return getAggregationType(null);

  }
 
  public net.geoprism.dashboard.AggregationTypeQuery.AllAggregationTypeQueryIF getAggregationType(String alias)
  {
    return (net.geoprism.dashboard.AggregationTypeQuery.AllAggregationTypeQueryIF)this.get(net.geoprism.dashboard.layer.DashboardThematicLayer.AGGREGATIONTYPE, alias, null);

  }
 
  public net.geoprism.dashboard.AggregationTypeQuery.AllAggregationTypeQueryIF getAggregationType(String alias, String displayLabel)
  {
    return (net.geoprism.dashboard.AggregationTypeQuery.AllAggregationTypeQueryIF)this.get(net.geoprism.dashboard.layer.DashboardThematicLayer.AGGREGATIONTYPE, alias, displayLabel);

  }
  public com.runwaysdk.system.gis.geo.GeoNodeQuery.GeoNodeQueryReferenceIF getGeoNode()
  {
    return getGeoNode(null);

  }
 
  public com.runwaysdk.system.gis.geo.GeoNodeQuery.GeoNodeQueryReferenceIF getGeoNode(String alias)
  {
    return (com.runwaysdk.system.gis.geo.GeoNodeQuery.GeoNodeQueryReferenceIF)this.get(net.geoprism.dashboard.layer.DashboardThematicLayer.GEONODE, alias, null);

  }
 
  public com.runwaysdk.system.gis.geo.GeoNodeQuery.GeoNodeQueryReferenceIF getGeoNode(String alias, String displayLabel)
  {
    return (com.runwaysdk.system.gis.geo.GeoNodeQuery.GeoNodeQueryReferenceIF)this.get(net.geoprism.dashboard.layer.DashboardThematicLayer.GEONODE,  alias, displayLabel);

  }
  public com.runwaysdk.system.metadata.MdAttributeQuery.MdAttributeQueryReferenceIF getMdAttribute()
  {
    return getMdAttribute(null);

  }
 
  public com.runwaysdk.system.metadata.MdAttributeQuery.MdAttributeQueryReferenceIF getMdAttribute(String alias)
  {
    return (com.runwaysdk.system.metadata.MdAttributeQuery.MdAttributeQueryReferenceIF)this.get(net.geoprism.dashboard.layer.DashboardThematicLayer.MDATTRIBUTE, alias, null);

  }
 
  public com.runwaysdk.system.metadata.MdAttributeQuery.MdAttributeQueryReferenceIF getMdAttribute(String alias, String displayLabel)
  {
    return (com.runwaysdk.system.metadata.MdAttributeQuery.MdAttributeQueryReferenceIF)this.get(net.geoprism.dashboard.layer.DashboardThematicLayer.MDATTRIBUTE,  alias, displayLabel);

  }
  protected com.runwaysdk.query.AttributeReference referenceFactory( com.runwaysdk.dataaccess.MdAttributeRefDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias,  com.runwaysdk.dataaccess.MdBusinessDAOIF referenceMdBusinessIF, String referenceTableAlias, com.runwaysdk.query.ComponentQuery rootQuery, java.util.Set<com.runwaysdk.query.Join> tableJoinSet, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    String name = mdAttributeIF.definesAttribute();
    
    if (name.equals(net.geoprism.dashboard.layer.DashboardThematicLayer.AGGREGATIONSTRATEGY)) 
    {
       return new net.geoprism.dashboard.AggregationStrategyQuery.AggregationStrategyQueryReference((com.runwaysdk.dataaccess.MdAttributeRefDAOIF)mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, referenceMdBusinessIF, referenceTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (name.equals(net.geoprism.dashboard.layer.DashboardThematicLayer.GEONODE)) 
    {
       return new com.runwaysdk.system.gis.geo.GeoNodeQuery.GeoNodeQueryReference((com.runwaysdk.dataaccess.MdAttributeRefDAOIF)mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, referenceMdBusinessIF, referenceTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (name.equals(net.geoprism.dashboard.layer.DashboardThematicLayer.MDATTRIBUTE)) 
    {
       return new com.runwaysdk.system.metadata.MdAttributeQuery.MdAttributeQueryReference((com.runwaysdk.dataaccess.MdAttributeRefDAOIF)mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, referenceMdBusinessIF, referenceTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else 
    {
      return super.referenceFactory(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, referenceMdBusinessIF, referenceTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
  }

  protected com.runwaysdk.query.AttributeEnumeration enumerationFactory( com.runwaysdk.dataaccess.MdAttributeEnumerationDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias,  String mdEnumerationTableName, com.runwaysdk.dataaccess.MdBusinessDAOIF masterListMdBusinessIF, String masterListTalbeAlias, com.runwaysdk.query.ComponentQuery rootQuery, java.util.Set<com.runwaysdk.query.Join> tableJoinSet, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    String name = mdAttributeIF.definesAttribute();
    
    if (name.equals(net.geoprism.dashboard.layer.DashboardThematicLayer.AGGREGATIONTYPE)) 
    {
       return new net.geoprism.dashboard.AggregationTypeQuery.AllAggregationTypeQuery((com.runwaysdk.dataaccess.MdAttributeEnumerationDAOIF)mdAttributeIF,  attributeNamespace, definingTableName, definingTableAlias, mdEnumerationTableName, masterListMdBusinessIF, masterListTalbeAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else 
    {
      return super.enumerationFactory(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, mdEnumerationTableName, masterListMdBusinessIF, masterListTalbeAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
  }

  }
}
