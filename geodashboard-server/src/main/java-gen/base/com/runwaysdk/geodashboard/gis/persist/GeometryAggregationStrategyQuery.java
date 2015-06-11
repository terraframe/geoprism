package com.runwaysdk.geodashboard.gis.persist;

@com.runwaysdk.business.ClassSignature(hash = 783543233)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to GeometryAggregationStrategy.java
 *
 * @author Autogenerated by RunwaySDK
 */
public  class GeometryAggregationStrategyQuery extends com.runwaysdk.geodashboard.gis.persist.AggregationStrategyQuery
 implements com.runwaysdk.generation.loader.Reloadable
{

  public GeometryAggregationStrategyQuery(com.runwaysdk.query.QueryFactory componentQueryFactory)
  {
    super(componentQueryFactory);
    if (this.getComponentQuery() == null)
    {
      com.runwaysdk.business.BusinessQuery businessQuery = componentQueryFactory.businessQuery(this.getClassType());

       this.setBusinessQuery(businessQuery);
    }
  }

  public GeometryAggregationStrategyQuery(com.runwaysdk.query.ValueQuery valueQuery)
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
    return com.runwaysdk.geodashboard.gis.persist.GeometryAggregationStrategy.CLASS;
  }
  /**  
   * Returns an iterator of Business objects that match the query criteria specified
   * on this query object. 
   * @return iterator of Business objects that match the query criteria specified
   * on this query object.
   */
  public com.runwaysdk.query.OIterator<? extends GeometryAggregationStrategy> getIterator()
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
    return new com.runwaysdk.business.BusinessIterator<GeometryAggregationStrategy>(this.getComponentQuery().getMdEntityIF(), columnInfoMap, results);
  }


/**
 * Interface that masks all type unsafe query methods and defines all type safe methods.
 * This type is used when a join is performed on this class as a reference.
 **/
  public interface GeometryAggregationStrategyQueryReferenceIF extends com.runwaysdk.generation.loader.Reloadable, com.runwaysdk.geodashboard.gis.persist.AggregationStrategyQuery.AggregationStrategyQueryReferenceIF
  {


    public com.runwaysdk.query.BasicCondition EQ(com.runwaysdk.geodashboard.gis.persist.GeometryAggregationStrategy geometryAggregationStrategy);

    public com.runwaysdk.query.BasicCondition NE(com.runwaysdk.geodashboard.gis.persist.GeometryAggregationStrategy geometryAggregationStrategy);

  }

/**
 * Implements type safe query methods.
 * This type is used when a join is performed on this class as a reference.
 **/
  public static class GeometryAggregationStrategyQueryReference extends com.runwaysdk.geodashboard.gis.persist.AggregationStrategyQuery.AggregationStrategyQueryReference
 implements GeometryAggregationStrategyQueryReferenceIF
, com.runwaysdk.generation.loader.Reloadable
  {

  public GeometryAggregationStrategyQueryReference(com.runwaysdk.dataaccess.MdAttributeRefDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias, com.runwaysdk.dataaccess.MdBusinessDAOIF referenceMdBusinessIF, String referenceTableAlias, com.runwaysdk.query.ComponentQuery rootQuery, java.util.Set<com.runwaysdk.query.Join> tableJoinSet, String alias, String displayLabel)
  {
    super(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, referenceMdBusinessIF, referenceTableAlias, rootQuery, tableJoinSet, alias, displayLabel);

  }


    public com.runwaysdk.query.BasicCondition EQ(com.runwaysdk.geodashboard.gis.persist.GeometryAggregationStrategy geometryAggregationStrategy)
    {
      if(geometryAggregationStrategy == null) return this.EQ((java.lang.String)null);
      return this.EQ(geometryAggregationStrategy.getId());
    }

    public com.runwaysdk.query.BasicCondition NE(com.runwaysdk.geodashboard.gis.persist.GeometryAggregationStrategy geometryAggregationStrategy)
    {
      if(geometryAggregationStrategy == null) return this.NE((java.lang.String)null);
      return this.NE(geometryAggregationStrategy.getId());
    }

  }

/**
 * Interface that masks all type unsafe query methods and defines all type safe methods.
 * This type is used when a join is performed on this class as a reference.
 **/
  public interface GeometryAggregationStrategyQueryMultiReferenceIF extends com.runwaysdk.generation.loader.Reloadable, com.runwaysdk.geodashboard.gis.persist.AggregationStrategyQuery.AggregationStrategyQueryMultiReferenceIF
  {


    public com.runwaysdk.query.Condition containsAny(com.runwaysdk.geodashboard.gis.persist.GeometryAggregationStrategy ... geometryAggregationStrategy);
    public com.runwaysdk.query.Condition notContainsAny(com.runwaysdk.geodashboard.gis.persist.GeometryAggregationStrategy ... geometryAggregationStrategy);
    public com.runwaysdk.query.Condition containsAll(com.runwaysdk.geodashboard.gis.persist.GeometryAggregationStrategy ... geometryAggregationStrategy);
    public com.runwaysdk.query.Condition notContainsAll(com.runwaysdk.geodashboard.gis.persist.GeometryAggregationStrategy ... geometryAggregationStrategy);
    public com.runwaysdk.query.Condition containsExactly(com.runwaysdk.geodashboard.gis.persist.GeometryAggregationStrategy ... geometryAggregationStrategy);
  }

/**
 * Implements type safe query methods.
 * This type is used when a join is performed on this class as a reference.
 **/
  public static class GeometryAggregationStrategyQueryMultiReference extends com.runwaysdk.geodashboard.gis.persist.AggregationStrategyQuery.AggregationStrategyQueryMultiReference
 implements GeometryAggregationStrategyQueryMultiReferenceIF
, com.runwaysdk.generation.loader.Reloadable
  {

  public GeometryAggregationStrategyQueryMultiReference(com.runwaysdk.dataaccess.MdAttributeMultiReferenceDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias, String mdMultiReferenceTableName, com.runwaysdk.dataaccess.MdBusinessDAOIF referenceMdBusinessIF, String referenceTableAlias, com.runwaysdk.query.ComponentQuery rootQuery, java.util.Set<com.runwaysdk.query.Join> tableJoinSet, String alias, String displayLabel)
  {
    super(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, mdMultiReferenceTableName, referenceMdBusinessIF, referenceTableAlias, rootQuery, tableJoinSet, alias, displayLabel);

  }



    public com.runwaysdk.query.Condition containsAny(com.runwaysdk.geodashboard.gis.persist.GeometryAggregationStrategy ... geometryAggregationStrategy)  {

      String[] itemIdArray = new String[geometryAggregationStrategy.length]; 

      for (int i=0; i<geometryAggregationStrategy.length; i++)
      {
        itemIdArray[i] = geometryAggregationStrategy[i].getId();
      }

      return this.containsAny(itemIdArray);
  }

    public com.runwaysdk.query.Condition notContainsAny(com.runwaysdk.geodashboard.gis.persist.GeometryAggregationStrategy ... geometryAggregationStrategy)  {

      String[] itemIdArray = new String[geometryAggregationStrategy.length]; 

      for (int i=0; i<geometryAggregationStrategy.length; i++)
      {
        itemIdArray[i] = geometryAggregationStrategy[i].getId();
      }

      return this.notContainsAny(itemIdArray);
  }

    public com.runwaysdk.query.Condition containsAll(com.runwaysdk.geodashboard.gis.persist.GeometryAggregationStrategy ... geometryAggregationStrategy)  {

      String[] itemIdArray = new String[geometryAggregationStrategy.length]; 

      for (int i=0; i<geometryAggregationStrategy.length; i++)
      {
        itemIdArray[i] = geometryAggregationStrategy[i].getId();
      }

      return this.containsAll(itemIdArray);
  }

    public com.runwaysdk.query.Condition notContainsAll(com.runwaysdk.geodashboard.gis.persist.GeometryAggregationStrategy ... geometryAggregationStrategy)  {

      String[] itemIdArray = new String[geometryAggregationStrategy.length]; 

      for (int i=0; i<geometryAggregationStrategy.length; i++)
      {
        itemIdArray[i] = geometryAggregationStrategy[i].getId();
      }

      return this.notContainsAll(itemIdArray);
  }

    public com.runwaysdk.query.Condition containsExactly(com.runwaysdk.geodashboard.gis.persist.GeometryAggregationStrategy ... geometryAggregationStrategy)  {

      String[] itemIdArray = new String[geometryAggregationStrategy.length]; 

      for (int i=0; i<geometryAggregationStrategy.length; i++)
      {
        itemIdArray[i] = geometryAggregationStrategy[i].getId();
      }

      return this.containsExactly(itemIdArray);
  }
  }
}
