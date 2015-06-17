package com.runwaysdk.geodashboard.gis.persist;

@com.runwaysdk.business.ClassSignature(hash = 1563334564)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to DashboardThematicLayer.java
 *
 * @author Autogenerated by RunwaySDK
 */
public  class DashboardThematicLayerQuery extends com.runwaysdk.geodashboard.gis.persist.DashboardLayerQuery
 implements com.runwaysdk.generation.loader.Reloadable
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
    return com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayer.CLASS;
  }
  public com.runwaysdk.geodashboard.gis.persist.AggregationStrategyQuery.AggregationStrategyQueryReferenceIF getAggregationStrategy()
  {
    return getAggregationStrategy(null);

  }
 
  public com.runwaysdk.geodashboard.gis.persist.AggregationStrategyQuery.AggregationStrategyQueryReferenceIF getAggregationStrategy(String alias)
  {

    com.runwaysdk.dataaccess.MdAttributeDAOIF mdAttributeIF = this.getComponentQuery().getMdAttributeROfromMap(com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayer.AGGREGATIONSTRATEGY);

    return (com.runwaysdk.geodashboard.gis.persist.AggregationStrategyQuery.AggregationStrategyQueryReferenceIF)this.getComponentQuery().internalAttributeFactory(com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayer.AGGREGATIONSTRATEGY, mdAttributeIF, this, alias, null);

  }
 
  public com.runwaysdk.geodashboard.gis.persist.AggregationStrategyQuery.AggregationStrategyQueryReferenceIF getAggregationStrategy(String alias, String displayLabel)
  {

    com.runwaysdk.dataaccess.MdAttributeDAOIF mdAttributeIF = this.getComponentQuery().getMdAttributeROfromMap(com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayer.AGGREGATIONSTRATEGY);

    return (com.runwaysdk.geodashboard.gis.persist.AggregationStrategyQuery.AggregationStrategyQueryReferenceIF)this.getComponentQuery().internalAttributeFactory(com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayer.AGGREGATIONSTRATEGY, mdAttributeIF, this, alias, displayLabel);

  }
  public com.runwaysdk.geodashboard.gis.persist.AggregationTypeQuery.AllAggregationTypeQueryIF getAggregationType()
  {
    return getAggregationType(null);

  }
 
  public com.runwaysdk.geodashboard.gis.persist.AggregationTypeQuery.AllAggregationTypeQueryIF getAggregationType(String alias)
  {

    com.runwaysdk.dataaccess.MdAttributeDAOIF mdAttributeIF = this.getComponentQuery().getMdAttributeROfromMap(com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayer.AGGREGATIONTYPE);

    return (com.runwaysdk.geodashboard.gis.persist.AggregationTypeQuery.AllAggregationTypeQueryIF)this.getComponentQuery().internalAttributeFactory(com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayer.AGGREGATIONTYPE, mdAttributeIF, this, alias, null);

  }
 
  public com.runwaysdk.geodashboard.gis.persist.AggregationTypeQuery.AllAggregationTypeQueryIF getAggregationType(String alias, String displayLabel)
  {

    com.runwaysdk.dataaccess.MdAttributeDAOIF mdAttributeIF = this.getComponentQuery().getMdAttributeROfromMap(com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayer.AGGREGATIONTYPE);

    return (com.runwaysdk.geodashboard.gis.persist.AggregationTypeQuery.AllAggregationTypeQueryIF)this.getComponentQuery().internalAttributeFactory(com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayer.AGGREGATIONTYPE, mdAttributeIF, this, alias, displayLabel);

  }
  public com.runwaysdk.system.gis.geo.GeoNodeQuery.GeoNodeQueryReferenceIF getGeoNode()
  {
    return getGeoNode(null);

  }
 
  public com.runwaysdk.system.gis.geo.GeoNodeQuery.GeoNodeQueryReferenceIF getGeoNode(String alias)
  {

    com.runwaysdk.dataaccess.MdAttributeDAOIF mdAttributeIF = this.getComponentQuery().getMdAttributeROfromMap(com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayer.GEONODE);

    return (com.runwaysdk.system.gis.geo.GeoNodeQuery.GeoNodeQueryReferenceIF)this.getComponentQuery().internalAttributeFactory(com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayer.GEONODE, mdAttributeIF, this, alias, null);

  }
 
  public com.runwaysdk.system.gis.geo.GeoNodeQuery.GeoNodeQueryReferenceIF getGeoNode(String alias, String displayLabel)
  {

    com.runwaysdk.dataaccess.MdAttributeDAOIF mdAttributeIF = this.getComponentQuery().getMdAttributeROfromMap(com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayer.GEONODE);

    return (com.runwaysdk.system.gis.geo.GeoNodeQuery.GeoNodeQueryReferenceIF)this.getComponentQuery().internalAttributeFactory(com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayer.GEONODE, mdAttributeIF, this, alias, displayLabel);

  }
  public com.runwaysdk.system.metadata.MdAttributeQuery.MdAttributeQueryReferenceIF getMdAttribute()
  {
    return getMdAttribute(null);

  }
 
  public com.runwaysdk.system.metadata.MdAttributeQuery.MdAttributeQueryReferenceIF getMdAttribute(String alias)
  {

    com.runwaysdk.dataaccess.MdAttributeDAOIF mdAttributeIF = this.getComponentQuery().getMdAttributeROfromMap(com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayer.MDATTRIBUTE);

    return (com.runwaysdk.system.metadata.MdAttributeQuery.MdAttributeQueryReferenceIF)this.getComponentQuery().internalAttributeFactory(com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayer.MDATTRIBUTE, mdAttributeIF, this, alias, null);

  }
 
  public com.runwaysdk.system.metadata.MdAttributeQuery.MdAttributeQueryReferenceIF getMdAttribute(String alias, String displayLabel)
  {

    com.runwaysdk.dataaccess.MdAttributeDAOIF mdAttributeIF = this.getComponentQuery().getMdAttributeROfromMap(com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayer.MDATTRIBUTE);

    return (com.runwaysdk.system.metadata.MdAttributeQuery.MdAttributeQueryReferenceIF)this.getComponentQuery().internalAttributeFactory(com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayer.MDATTRIBUTE, mdAttributeIF, this, alias, displayLabel);

  }
  protected com.runwaysdk.query.AttributeReference referenceFactory( com.runwaysdk.dataaccess.MdAttributeRefDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias,  com.runwaysdk.dataaccess.MdBusinessDAOIF referenceMdBusinessIF, String referenceTableAlias, com.runwaysdk.query.ComponentQuery rootQuery, java.util.Set<com.runwaysdk.query.Join> tableJoinSet, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    String name = mdAttributeIF.definesAttribute();
    
    if (name.equals(com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayer.AGGREGATIONSTRATEGY)) 
    {
       return new com.runwaysdk.geodashboard.gis.persist.AggregationStrategyQuery.AggregationStrategyQueryReference((com.runwaysdk.dataaccess.MdAttributeRefDAOIF)mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, referenceMdBusinessIF, referenceTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (name.equals(com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayer.GEONODE)) 
    {
       return new com.runwaysdk.system.gis.geo.GeoNodeQuery.GeoNodeQueryReference((com.runwaysdk.dataaccess.MdAttributeRefDAOIF)mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, referenceMdBusinessIF, referenceTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (name.equals(com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayer.MDATTRIBUTE)) 
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
    
    if (name.equals(com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayer.AGGREGATIONTYPE)) 
    {
       return new com.runwaysdk.geodashboard.gis.persist.AggregationTypeQuery.AllAggregationTypeQuery((com.runwaysdk.dataaccess.MdAttributeEnumerationDAOIF)mdAttributeIF,  attributeNamespace, definingTableName, definingTableAlias, mdEnumerationTableName, masterListMdBusinessIF, masterListTalbeAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
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
  public interface DashboardThematicLayerQueryReferenceIF extends com.runwaysdk.generation.loader.Reloadable, com.runwaysdk.geodashboard.gis.persist.DashboardLayerQuery.DashboardLayerQueryReferenceIF
  {

    public com.runwaysdk.geodashboard.gis.persist.AggregationStrategyQuery.AggregationStrategyQueryReferenceIF getAggregationStrategy();
    public com.runwaysdk.geodashboard.gis.persist.AggregationStrategyQuery.AggregationStrategyQueryReferenceIF getAggregationStrategy(String alias);
    public com.runwaysdk.geodashboard.gis.persist.AggregationStrategyQuery.AggregationStrategyQueryReferenceIF getAggregationStrategy(String alias, String displayLabel);
  public com.runwaysdk.geodashboard.gis.persist.AggregationTypeQuery.AllAggregationTypeQueryIF getAggregationType();
  public com.runwaysdk.geodashboard.gis.persist.AggregationTypeQuery.AllAggregationTypeQueryIF getAggregationType(String alias);
  public com.runwaysdk.geodashboard.gis.persist.AggregationTypeQuery.AllAggregationTypeQueryIF getAggregationType(String alias, String displayLabel);
    public com.runwaysdk.system.gis.geo.GeoNodeQuery.GeoNodeQueryReferenceIF getGeoNode();
    public com.runwaysdk.system.gis.geo.GeoNodeQuery.GeoNodeQueryReferenceIF getGeoNode(String alias);
    public com.runwaysdk.system.gis.geo.GeoNodeQuery.GeoNodeQueryReferenceIF getGeoNode(String alias, String displayLabel);
    public com.runwaysdk.system.metadata.MdAttributeQuery.MdAttributeQueryReferenceIF getMdAttribute();
    public com.runwaysdk.system.metadata.MdAttributeQuery.MdAttributeQueryReferenceIF getMdAttribute(String alias);
    public com.runwaysdk.system.metadata.MdAttributeQuery.MdAttributeQueryReferenceIF getMdAttribute(String alias, String displayLabel);

    public com.runwaysdk.query.BasicCondition EQ(com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayer dashboardThematicLayer);

    public com.runwaysdk.query.BasicCondition NE(com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayer dashboardThematicLayer);

  }

/**
 * Implements type safe query methods.
 * This type is used when a join is performed on this class as a reference.
 **/
  public static class DashboardThematicLayerQueryReference extends com.runwaysdk.geodashboard.gis.persist.DashboardLayerQuery.DashboardLayerQueryReference
 implements DashboardThematicLayerQueryReferenceIF
, com.runwaysdk.generation.loader.Reloadable
  {

  public DashboardThematicLayerQueryReference(com.runwaysdk.dataaccess.MdAttributeRefDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias, com.runwaysdk.dataaccess.MdBusinessDAOIF referenceMdBusinessIF, String referenceTableAlias, com.runwaysdk.query.ComponentQuery rootQuery, java.util.Set<com.runwaysdk.query.Join> tableJoinSet, String alias, String displayLabel)
  {
    super(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, referenceMdBusinessIF, referenceTableAlias, rootQuery, tableJoinSet, alias, displayLabel);

  }


    public com.runwaysdk.query.BasicCondition EQ(com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayer dashboardThematicLayer)
    {
      if(dashboardThematicLayer == null) return this.EQ((java.lang.String)null);
      return this.EQ(dashboardThematicLayer.getId());
    }

    public com.runwaysdk.query.BasicCondition NE(com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayer dashboardThematicLayer)
    {
      if(dashboardThematicLayer == null) return this.NE((java.lang.String)null);
      return this.NE(dashboardThematicLayer.getId());
    }

  public com.runwaysdk.geodashboard.gis.persist.AggregationStrategyQuery.AggregationStrategyQueryReferenceIF getAggregationStrategy()
  {
    return getAggregationStrategy(null);

  }
 
  public com.runwaysdk.geodashboard.gis.persist.AggregationStrategyQuery.AggregationStrategyQueryReferenceIF getAggregationStrategy(String alias)
  {
    return (com.runwaysdk.geodashboard.gis.persist.AggregationStrategyQuery.AggregationStrategyQueryReferenceIF)this.get(com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayer.AGGREGATIONSTRATEGY, alias, null);

  }
 
  public com.runwaysdk.geodashboard.gis.persist.AggregationStrategyQuery.AggregationStrategyQueryReferenceIF getAggregationStrategy(String alias, String displayLabel)
  {
    return (com.runwaysdk.geodashboard.gis.persist.AggregationStrategyQuery.AggregationStrategyQueryReferenceIF)this.get(com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayer.AGGREGATIONSTRATEGY,  alias, displayLabel);

  }
  public com.runwaysdk.geodashboard.gis.persist.AggregationTypeQuery.AllAggregationTypeQueryIF getAggregationType()
  {
    return getAggregationType(null);

  }
 
  public com.runwaysdk.geodashboard.gis.persist.AggregationTypeQuery.AllAggregationTypeQueryIF getAggregationType(String alias)
  {
    return (com.runwaysdk.geodashboard.gis.persist.AggregationTypeQuery.AllAggregationTypeQueryIF)this.get(com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayer.AGGREGATIONTYPE, alias, null);

  }
 
  public com.runwaysdk.geodashboard.gis.persist.AggregationTypeQuery.AllAggregationTypeQueryIF getAggregationType(String alias, String displayLabel)
  {
    return (com.runwaysdk.geodashboard.gis.persist.AggregationTypeQuery.AllAggregationTypeQueryIF)this.get(com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayer.AGGREGATIONTYPE, alias, displayLabel);

  }
  public com.runwaysdk.system.gis.geo.GeoNodeQuery.GeoNodeQueryReferenceIF getGeoNode()
  {
    return getGeoNode(null);

  }
 
  public com.runwaysdk.system.gis.geo.GeoNodeQuery.GeoNodeQueryReferenceIF getGeoNode(String alias)
  {
    return (com.runwaysdk.system.gis.geo.GeoNodeQuery.GeoNodeQueryReferenceIF)this.get(com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayer.GEONODE, alias, null);

  }
 
  public com.runwaysdk.system.gis.geo.GeoNodeQuery.GeoNodeQueryReferenceIF getGeoNode(String alias, String displayLabel)
  {
    return (com.runwaysdk.system.gis.geo.GeoNodeQuery.GeoNodeQueryReferenceIF)this.get(com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayer.GEONODE,  alias, displayLabel);

  }
  public com.runwaysdk.system.metadata.MdAttributeQuery.MdAttributeQueryReferenceIF getMdAttribute()
  {
    return getMdAttribute(null);

  }
 
  public com.runwaysdk.system.metadata.MdAttributeQuery.MdAttributeQueryReferenceIF getMdAttribute(String alias)
  {
    return (com.runwaysdk.system.metadata.MdAttributeQuery.MdAttributeQueryReferenceIF)this.get(com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayer.MDATTRIBUTE, alias, null);

  }
 
  public com.runwaysdk.system.metadata.MdAttributeQuery.MdAttributeQueryReferenceIF getMdAttribute(String alias, String displayLabel)
  {
    return (com.runwaysdk.system.metadata.MdAttributeQuery.MdAttributeQueryReferenceIF)this.get(com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayer.MDATTRIBUTE,  alias, displayLabel);

  }
  protected com.runwaysdk.query.AttributeReference referenceFactory( com.runwaysdk.dataaccess.MdAttributeRefDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias,  com.runwaysdk.dataaccess.MdBusinessDAOIF referenceMdBusinessIF, String referenceTableAlias, com.runwaysdk.query.ComponentQuery rootQuery, java.util.Set<com.runwaysdk.query.Join> tableJoinSet, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    String name = mdAttributeIF.definesAttribute();
    
    if (name.equals(com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayer.AGGREGATIONSTRATEGY)) 
    {
       return new com.runwaysdk.geodashboard.gis.persist.AggregationStrategyQuery.AggregationStrategyQueryReference((com.runwaysdk.dataaccess.MdAttributeRefDAOIF)mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, referenceMdBusinessIF, referenceTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (name.equals(com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayer.GEONODE)) 
    {
       return new com.runwaysdk.system.gis.geo.GeoNodeQuery.GeoNodeQueryReference((com.runwaysdk.dataaccess.MdAttributeRefDAOIF)mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, referenceMdBusinessIF, referenceTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (name.equals(com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayer.MDATTRIBUTE)) 
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
    
    if (name.equals(com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayer.AGGREGATIONTYPE)) 
    {
       return new com.runwaysdk.geodashboard.gis.persist.AggregationTypeQuery.AllAggregationTypeQuery((com.runwaysdk.dataaccess.MdAttributeEnumerationDAOIF)mdAttributeIF,  attributeNamespace, definingTableName, definingTableAlias, mdEnumerationTableName, masterListMdBusinessIF, masterListTalbeAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
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
  public interface DashboardThematicLayerQueryMultiReferenceIF extends com.runwaysdk.generation.loader.Reloadable, com.runwaysdk.geodashboard.gis.persist.DashboardLayerQuery.DashboardLayerQueryMultiReferenceIF
  {

    public com.runwaysdk.geodashboard.gis.persist.AggregationStrategyQuery.AggregationStrategyQueryReferenceIF getAggregationStrategy();
    public com.runwaysdk.geodashboard.gis.persist.AggregationStrategyQuery.AggregationStrategyQueryReferenceIF getAggregationStrategy(String alias);
    public com.runwaysdk.geodashboard.gis.persist.AggregationStrategyQuery.AggregationStrategyQueryReferenceIF getAggregationStrategy(String alias, String displayLabel);
  public com.runwaysdk.geodashboard.gis.persist.AggregationTypeQuery.AllAggregationTypeQueryIF getAggregationType();
  public com.runwaysdk.geodashboard.gis.persist.AggregationTypeQuery.AllAggregationTypeQueryIF getAggregationType(String alias);
  public com.runwaysdk.geodashboard.gis.persist.AggregationTypeQuery.AllAggregationTypeQueryIF getAggregationType(String alias, String displayLabel);
    public com.runwaysdk.system.gis.geo.GeoNodeQuery.GeoNodeQueryReferenceIF getGeoNode();
    public com.runwaysdk.system.gis.geo.GeoNodeQuery.GeoNodeQueryReferenceIF getGeoNode(String alias);
    public com.runwaysdk.system.gis.geo.GeoNodeQuery.GeoNodeQueryReferenceIF getGeoNode(String alias, String displayLabel);
    public com.runwaysdk.system.metadata.MdAttributeQuery.MdAttributeQueryReferenceIF getMdAttribute();
    public com.runwaysdk.system.metadata.MdAttributeQuery.MdAttributeQueryReferenceIF getMdAttribute(String alias);
    public com.runwaysdk.system.metadata.MdAttributeQuery.MdAttributeQueryReferenceIF getMdAttribute(String alias, String displayLabel);

    public com.runwaysdk.query.Condition containsAny(com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayer ... dashboardThematicLayer);
    public com.runwaysdk.query.Condition notContainsAny(com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayer ... dashboardThematicLayer);
    public com.runwaysdk.query.Condition containsAll(com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayer ... dashboardThematicLayer);
    public com.runwaysdk.query.Condition notContainsAll(com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayer ... dashboardThematicLayer);
    public com.runwaysdk.query.Condition containsExactly(com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayer ... dashboardThematicLayer);
  }

/**
 * Implements type safe query methods.
 * This type is used when a join is performed on this class as a reference.
 **/
  public static class DashboardThematicLayerQueryMultiReference extends com.runwaysdk.geodashboard.gis.persist.DashboardLayerQuery.DashboardLayerQueryMultiReference
 implements DashboardThematicLayerQueryMultiReferenceIF
, com.runwaysdk.generation.loader.Reloadable
  {

  public DashboardThematicLayerQueryMultiReference(com.runwaysdk.dataaccess.MdAttributeMultiReferenceDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias, String mdMultiReferenceTableName, com.runwaysdk.dataaccess.MdBusinessDAOIF referenceMdBusinessIF, String referenceTableAlias, com.runwaysdk.query.ComponentQuery rootQuery, java.util.Set<com.runwaysdk.query.Join> tableJoinSet, String alias, String displayLabel)
  {
    super(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, mdMultiReferenceTableName, referenceMdBusinessIF, referenceTableAlias, rootQuery, tableJoinSet, alias, displayLabel);

  }



    public com.runwaysdk.query.Condition containsAny(com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayer ... dashboardThematicLayer)  {

      String[] itemIdArray = new String[dashboardThematicLayer.length]; 

      for (int i=0; i<dashboardThematicLayer.length; i++)
      {
        itemIdArray[i] = dashboardThematicLayer[i].getId();
      }

      return this.containsAny(itemIdArray);
  }

    public com.runwaysdk.query.Condition notContainsAny(com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayer ... dashboardThematicLayer)  {

      String[] itemIdArray = new String[dashboardThematicLayer.length]; 

      for (int i=0; i<dashboardThematicLayer.length; i++)
      {
        itemIdArray[i] = dashboardThematicLayer[i].getId();
      }

      return this.notContainsAny(itemIdArray);
  }

    public com.runwaysdk.query.Condition containsAll(com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayer ... dashboardThematicLayer)  {

      String[] itemIdArray = new String[dashboardThematicLayer.length]; 

      for (int i=0; i<dashboardThematicLayer.length; i++)
      {
        itemIdArray[i] = dashboardThematicLayer[i].getId();
      }

      return this.containsAll(itemIdArray);
  }

    public com.runwaysdk.query.Condition notContainsAll(com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayer ... dashboardThematicLayer)  {

      String[] itemIdArray = new String[dashboardThematicLayer.length]; 

      for (int i=0; i<dashboardThematicLayer.length; i++)
      {
        itemIdArray[i] = dashboardThematicLayer[i].getId();
      }

      return this.notContainsAll(itemIdArray);
  }

    public com.runwaysdk.query.Condition containsExactly(com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayer ... dashboardThematicLayer)  {

      String[] itemIdArray = new String[dashboardThematicLayer.length]; 

      for (int i=0; i<dashboardThematicLayer.length; i++)
      {
        itemIdArray[i] = dashboardThematicLayer[i].getId();
      }

      return this.containsExactly(itemIdArray);
  }
  public com.runwaysdk.geodashboard.gis.persist.AggregationStrategyQuery.AggregationStrategyQueryReferenceIF getAggregationStrategy()
  {
    return getAggregationStrategy(null);

  }
 
  public com.runwaysdk.geodashboard.gis.persist.AggregationStrategyQuery.AggregationStrategyQueryReferenceIF getAggregationStrategy(String alias)
  {
    return (com.runwaysdk.geodashboard.gis.persist.AggregationStrategyQuery.AggregationStrategyQueryReferenceIF)this.get(com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayer.AGGREGATIONSTRATEGY, alias, null);

  }
 
  public com.runwaysdk.geodashboard.gis.persist.AggregationStrategyQuery.AggregationStrategyQueryReferenceIF getAggregationStrategy(String alias, String displayLabel)
  {
    return (com.runwaysdk.geodashboard.gis.persist.AggregationStrategyQuery.AggregationStrategyQueryReferenceIF)this.get(com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayer.AGGREGATIONSTRATEGY,  alias, displayLabel);

  }
  public com.runwaysdk.geodashboard.gis.persist.AggregationTypeQuery.AllAggregationTypeQueryIF getAggregationType()
  {
    return getAggregationType(null);

  }
 
  public com.runwaysdk.geodashboard.gis.persist.AggregationTypeQuery.AllAggregationTypeQueryIF getAggregationType(String alias)
  {
    return (com.runwaysdk.geodashboard.gis.persist.AggregationTypeQuery.AllAggregationTypeQueryIF)this.get(com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayer.AGGREGATIONTYPE, alias, null);

  }
 
  public com.runwaysdk.geodashboard.gis.persist.AggregationTypeQuery.AllAggregationTypeQueryIF getAggregationType(String alias, String displayLabel)
  {
    return (com.runwaysdk.geodashboard.gis.persist.AggregationTypeQuery.AllAggregationTypeQueryIF)this.get(com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayer.AGGREGATIONTYPE, alias, displayLabel);

  }
  public com.runwaysdk.system.gis.geo.GeoNodeQuery.GeoNodeQueryReferenceIF getGeoNode()
  {
    return getGeoNode(null);

  }
 
  public com.runwaysdk.system.gis.geo.GeoNodeQuery.GeoNodeQueryReferenceIF getGeoNode(String alias)
  {
    return (com.runwaysdk.system.gis.geo.GeoNodeQuery.GeoNodeQueryReferenceIF)this.get(com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayer.GEONODE, alias, null);

  }
 
  public com.runwaysdk.system.gis.geo.GeoNodeQuery.GeoNodeQueryReferenceIF getGeoNode(String alias, String displayLabel)
  {
    return (com.runwaysdk.system.gis.geo.GeoNodeQuery.GeoNodeQueryReferenceIF)this.get(com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayer.GEONODE,  alias, displayLabel);

  }
  public com.runwaysdk.system.metadata.MdAttributeQuery.MdAttributeQueryReferenceIF getMdAttribute()
  {
    return getMdAttribute(null);

  }
 
  public com.runwaysdk.system.metadata.MdAttributeQuery.MdAttributeQueryReferenceIF getMdAttribute(String alias)
  {
    return (com.runwaysdk.system.metadata.MdAttributeQuery.MdAttributeQueryReferenceIF)this.get(com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayer.MDATTRIBUTE, alias, null);

  }
 
  public com.runwaysdk.system.metadata.MdAttributeQuery.MdAttributeQueryReferenceIF getMdAttribute(String alias, String displayLabel)
  {
    return (com.runwaysdk.system.metadata.MdAttributeQuery.MdAttributeQueryReferenceIF)this.get(com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayer.MDATTRIBUTE,  alias, displayLabel);

  }
  protected com.runwaysdk.query.AttributeReference referenceFactory( com.runwaysdk.dataaccess.MdAttributeRefDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias,  com.runwaysdk.dataaccess.MdBusinessDAOIF referenceMdBusinessIF, String referenceTableAlias, com.runwaysdk.query.ComponentQuery rootQuery, java.util.Set<com.runwaysdk.query.Join> tableJoinSet, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    String name = mdAttributeIF.definesAttribute();
    
    if (name.equals(com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayer.AGGREGATIONSTRATEGY)) 
    {
       return new com.runwaysdk.geodashboard.gis.persist.AggregationStrategyQuery.AggregationStrategyQueryReference((com.runwaysdk.dataaccess.MdAttributeRefDAOIF)mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, referenceMdBusinessIF, referenceTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (name.equals(com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayer.GEONODE)) 
    {
       return new com.runwaysdk.system.gis.geo.GeoNodeQuery.GeoNodeQueryReference((com.runwaysdk.dataaccess.MdAttributeRefDAOIF)mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, referenceMdBusinessIF, referenceTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (name.equals(com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayer.MDATTRIBUTE)) 
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
    
    if (name.equals(com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayer.AGGREGATIONTYPE)) 
    {
       return new com.runwaysdk.geodashboard.gis.persist.AggregationTypeQuery.AllAggregationTypeQuery((com.runwaysdk.dataaccess.MdAttributeEnumerationDAOIF)mdAttributeIF,  attributeNamespace, definingTableName, definingTableAlias, mdEnumerationTableName, masterListMdBusinessIF, masterListTalbeAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else 
    {
      return super.enumerationFactory(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, mdEnumerationTableName, masterListMdBusinessIF, masterListTalbeAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
  }

  }
}
