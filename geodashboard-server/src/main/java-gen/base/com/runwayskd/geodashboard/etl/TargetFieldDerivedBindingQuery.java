/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwayskd.geodashboard.etl;

@com.runwaysdk.business.ClassSignature(hash = -296319181)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to TargetFieldDerivedBinding.java
 *
 * @author Autogenerated by RunwaySDK
 */
public  class TargetFieldDerivedBindingQuery extends com.runwayskd.geodashboard.etl.TargetFieldCoordinateBindingQuery
 implements com.runwaysdk.generation.loader.Reloadable
{

  public TargetFieldDerivedBindingQuery(com.runwaysdk.query.QueryFactory componentQueryFactory)
  {
    super(componentQueryFactory);
    if (this.getComponentQuery() == null)
    {
      com.runwaysdk.business.BusinessQuery businessQuery = componentQueryFactory.businessQuery(this.getClassType());

       this.setBusinessQuery(businessQuery);
    }
  }

  public TargetFieldDerivedBindingQuery(com.runwaysdk.query.ValueQuery valueQuery)
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
    return com.runwayskd.geodashboard.etl.TargetFieldDerivedBinding.CLASS;
  }
  public com.runwaysdk.system.gis.geo.GeoEntityQuery.GeoEntityQueryReferenceIF getGeoEntity()
  {
    return getGeoEntity(null);

  }
 
  public com.runwaysdk.system.gis.geo.GeoEntityQuery.GeoEntityQueryReferenceIF getGeoEntity(String alias)
  {

    com.runwaysdk.dataaccess.MdAttributeDAOIF mdAttributeIF = this.getComponentQuery().getMdAttributeROfromMap(com.runwayskd.geodashboard.etl.TargetFieldDerivedBinding.GEOENTITY);

    return (com.runwaysdk.system.gis.geo.GeoEntityQuery.GeoEntityQueryReferenceIF)this.getComponentQuery().internalAttributeFactory(com.runwayskd.geodashboard.etl.TargetFieldDerivedBinding.GEOENTITY, mdAttributeIF, this, alias, null);

  }
 
  public com.runwaysdk.system.gis.geo.GeoEntityQuery.GeoEntityQueryReferenceIF getGeoEntity(String alias, String displayLabel)
  {

    com.runwaysdk.dataaccess.MdAttributeDAOIF mdAttributeIF = this.getComponentQuery().getMdAttributeROfromMap(com.runwayskd.geodashboard.etl.TargetFieldDerivedBinding.GEOENTITY);

    return (com.runwaysdk.system.gis.geo.GeoEntityQuery.GeoEntityQueryReferenceIF)this.getComponentQuery().internalAttributeFactory(com.runwayskd.geodashboard.etl.TargetFieldDerivedBinding.GEOENTITY, mdAttributeIF, this, alias, displayLabel);

  }
  public com.runwaysdk.system.gis.geo.UniversalQuery.UniversalQueryReferenceIF getUniversal()
  {
    return getUniversal(null);

  }
 
  public com.runwaysdk.system.gis.geo.UniversalQuery.UniversalQueryReferenceIF getUniversal(String alias)
  {

    com.runwaysdk.dataaccess.MdAttributeDAOIF mdAttributeIF = this.getComponentQuery().getMdAttributeROfromMap(com.runwayskd.geodashboard.etl.TargetFieldDerivedBinding.UNIVERSAL);

    return (com.runwaysdk.system.gis.geo.UniversalQuery.UniversalQueryReferenceIF)this.getComponentQuery().internalAttributeFactory(com.runwayskd.geodashboard.etl.TargetFieldDerivedBinding.UNIVERSAL, mdAttributeIF, this, alias, null);

  }
 
  public com.runwaysdk.system.gis.geo.UniversalQuery.UniversalQueryReferenceIF getUniversal(String alias, String displayLabel)
  {

    com.runwaysdk.dataaccess.MdAttributeDAOIF mdAttributeIF = this.getComponentQuery().getMdAttributeROfromMap(com.runwayskd.geodashboard.etl.TargetFieldDerivedBinding.UNIVERSAL);

    return (com.runwaysdk.system.gis.geo.UniversalQuery.UniversalQueryReferenceIF)this.getComponentQuery().internalAttributeFactory(com.runwayskd.geodashboard.etl.TargetFieldDerivedBinding.UNIVERSAL, mdAttributeIF, this, alias, displayLabel);

  }
  protected com.runwaysdk.query.AttributeReference referenceFactory( com.runwaysdk.dataaccess.MdAttributeRefDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias,  com.runwaysdk.dataaccess.MdBusinessDAOIF referenceMdBusinessIF, String referenceTableAlias, com.runwaysdk.query.ComponentQuery rootQuery, java.util.Set<com.runwaysdk.query.Join> tableJoinSet, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    String name = mdAttributeIF.definesAttribute();
    
    if (name.equals(com.runwayskd.geodashboard.etl.TargetFieldDerivedBinding.GEOENTITY)) 
    {
       return new com.runwaysdk.system.gis.geo.GeoEntityQuery.GeoEntityQueryReference((com.runwaysdk.dataaccess.MdAttributeRefDAOIF)mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, referenceMdBusinessIF, referenceTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (name.equals(com.runwayskd.geodashboard.etl.TargetFieldDerivedBinding.UNIVERSAL)) 
    {
       return new com.runwaysdk.system.gis.geo.UniversalQuery.UniversalQueryReference((com.runwaysdk.dataaccess.MdAttributeRefDAOIF)mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, referenceMdBusinessIF, referenceTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else 
    {
      return super.referenceFactory(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, referenceMdBusinessIF, referenceTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
  }

  /**  
   * Returns an iterator of Business objects that match the query criteria specified
   * on this query object. 
   * @return iterator of Business objects that match the query criteria specified
   * on this query object.
   */
  public com.runwaysdk.query.OIterator<? extends TargetFieldDerivedBinding> getIterator()
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
    return new com.runwaysdk.business.BusinessIterator<TargetFieldDerivedBinding>(this.getComponentQuery().getMdEntityIF(), columnInfoMap, results);
  }


/**
 * Interface that masks all type unsafe query methods and defines all type safe methods.
 * This type is used when a join is performed on this class as a reference.
 **/
  public interface TargetFieldDerivedBindingQueryReferenceIF extends com.runwaysdk.generation.loader.Reloadable, com.runwayskd.geodashboard.etl.TargetFieldCoordinateBindingQuery.TargetFieldCoordinateBindingQueryReferenceIF
  {

    public com.runwaysdk.system.gis.geo.GeoEntityQuery.GeoEntityQueryReferenceIF getGeoEntity();
    public com.runwaysdk.system.gis.geo.GeoEntityQuery.GeoEntityQueryReferenceIF getGeoEntity(String alias);
    public com.runwaysdk.system.gis.geo.GeoEntityQuery.GeoEntityQueryReferenceIF getGeoEntity(String alias, String displayLabel);
    public com.runwaysdk.system.gis.geo.UniversalQuery.UniversalQueryReferenceIF getUniversal();
    public com.runwaysdk.system.gis.geo.UniversalQuery.UniversalQueryReferenceIF getUniversal(String alias);
    public com.runwaysdk.system.gis.geo.UniversalQuery.UniversalQueryReferenceIF getUniversal(String alias, String displayLabel);

    public com.runwaysdk.query.BasicCondition EQ(com.runwayskd.geodashboard.etl.TargetFieldDerivedBinding targetFieldDerivedBinding);

    public com.runwaysdk.query.BasicCondition NE(com.runwayskd.geodashboard.etl.TargetFieldDerivedBinding targetFieldDerivedBinding);

  }

/**
 * Implements type safe query methods.
 * This type is used when a join is performed on this class as a reference.
 **/
  public static class TargetFieldDerivedBindingQueryReference extends com.runwayskd.geodashboard.etl.TargetFieldCoordinateBindingQuery.TargetFieldCoordinateBindingQueryReference
 implements TargetFieldDerivedBindingQueryReferenceIF
, com.runwaysdk.generation.loader.Reloadable
  {

  public TargetFieldDerivedBindingQueryReference(com.runwaysdk.dataaccess.MdAttributeRefDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias, com.runwaysdk.dataaccess.MdBusinessDAOIF referenceMdBusinessIF, String referenceTableAlias, com.runwaysdk.query.ComponentQuery rootQuery, java.util.Set<com.runwaysdk.query.Join> tableJoinSet, String alias, String displayLabel)
  {
    super(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, referenceMdBusinessIF, referenceTableAlias, rootQuery, tableJoinSet, alias, displayLabel);

  }


    public com.runwaysdk.query.BasicCondition EQ(com.runwayskd.geodashboard.etl.TargetFieldDerivedBinding targetFieldDerivedBinding)
    {
      if(targetFieldDerivedBinding == null) return this.EQ((java.lang.String)null);
      return this.EQ(targetFieldDerivedBinding.getId());
    }

    public com.runwaysdk.query.BasicCondition NE(com.runwayskd.geodashboard.etl.TargetFieldDerivedBinding targetFieldDerivedBinding)
    {
      if(targetFieldDerivedBinding == null) return this.NE((java.lang.String)null);
      return this.NE(targetFieldDerivedBinding.getId());
    }

  public com.runwaysdk.system.gis.geo.GeoEntityQuery.GeoEntityQueryReferenceIF getGeoEntity()
  {
    return getGeoEntity(null);

  }
 
  public com.runwaysdk.system.gis.geo.GeoEntityQuery.GeoEntityQueryReferenceIF getGeoEntity(String alias)
  {
    return (com.runwaysdk.system.gis.geo.GeoEntityQuery.GeoEntityQueryReferenceIF)this.get(com.runwayskd.geodashboard.etl.TargetFieldDerivedBinding.GEOENTITY, alias, null);

  }
 
  public com.runwaysdk.system.gis.geo.GeoEntityQuery.GeoEntityQueryReferenceIF getGeoEntity(String alias, String displayLabel)
  {
    return (com.runwaysdk.system.gis.geo.GeoEntityQuery.GeoEntityQueryReferenceIF)this.get(com.runwayskd.geodashboard.etl.TargetFieldDerivedBinding.GEOENTITY,  alias, displayLabel);

  }
  public com.runwaysdk.system.gis.geo.UniversalQuery.UniversalQueryReferenceIF getUniversal()
  {
    return getUniversal(null);

  }
 
  public com.runwaysdk.system.gis.geo.UniversalQuery.UniversalQueryReferenceIF getUniversal(String alias)
  {
    return (com.runwaysdk.system.gis.geo.UniversalQuery.UniversalQueryReferenceIF)this.get(com.runwayskd.geodashboard.etl.TargetFieldDerivedBinding.UNIVERSAL, alias, null);

  }
 
  public com.runwaysdk.system.gis.geo.UniversalQuery.UniversalQueryReferenceIF getUniversal(String alias, String displayLabel)
  {
    return (com.runwaysdk.system.gis.geo.UniversalQuery.UniversalQueryReferenceIF)this.get(com.runwayskd.geodashboard.etl.TargetFieldDerivedBinding.UNIVERSAL,  alias, displayLabel);

  }
  protected com.runwaysdk.query.AttributeReference referenceFactory( com.runwaysdk.dataaccess.MdAttributeRefDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias,  com.runwaysdk.dataaccess.MdBusinessDAOIF referenceMdBusinessIF, String referenceTableAlias, com.runwaysdk.query.ComponentQuery rootQuery, java.util.Set<com.runwaysdk.query.Join> tableJoinSet, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    String name = mdAttributeIF.definesAttribute();
    
    if (name.equals(com.runwayskd.geodashboard.etl.TargetFieldDerivedBinding.GEOENTITY)) 
    {
       return new com.runwaysdk.system.gis.geo.GeoEntityQuery.GeoEntityQueryReference((com.runwaysdk.dataaccess.MdAttributeRefDAOIF)mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, referenceMdBusinessIF, referenceTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (name.equals(com.runwayskd.geodashboard.etl.TargetFieldDerivedBinding.UNIVERSAL)) 
    {
       return new com.runwaysdk.system.gis.geo.UniversalQuery.UniversalQueryReference((com.runwaysdk.dataaccess.MdAttributeRefDAOIF)mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, referenceMdBusinessIF, referenceTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else 
    {
      return super.referenceFactory(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, referenceMdBusinessIF, referenceTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
  }

  }

/**
 * Interface that masks all type unsafe query methods and defines all type safe methods.
 * This type is used when a join is performed on this class as a reference.
 **/
  public interface TargetFieldDerivedBindingQueryMultiReferenceIF extends com.runwaysdk.generation.loader.Reloadable, com.runwayskd.geodashboard.etl.TargetFieldCoordinateBindingQuery.TargetFieldCoordinateBindingQueryMultiReferenceIF
  {

    public com.runwaysdk.system.gis.geo.GeoEntityQuery.GeoEntityQueryReferenceIF getGeoEntity();
    public com.runwaysdk.system.gis.geo.GeoEntityQuery.GeoEntityQueryReferenceIF getGeoEntity(String alias);
    public com.runwaysdk.system.gis.geo.GeoEntityQuery.GeoEntityQueryReferenceIF getGeoEntity(String alias, String displayLabel);
    public com.runwaysdk.system.gis.geo.UniversalQuery.UniversalQueryReferenceIF getUniversal();
    public com.runwaysdk.system.gis.geo.UniversalQuery.UniversalQueryReferenceIF getUniversal(String alias);
    public com.runwaysdk.system.gis.geo.UniversalQuery.UniversalQueryReferenceIF getUniversal(String alias, String displayLabel);

    public com.runwaysdk.query.Condition containsAny(com.runwayskd.geodashboard.etl.TargetFieldDerivedBinding ... targetFieldDerivedBinding);
    public com.runwaysdk.query.Condition notContainsAny(com.runwayskd.geodashboard.etl.TargetFieldDerivedBinding ... targetFieldDerivedBinding);
    public com.runwaysdk.query.Condition containsAll(com.runwayskd.geodashboard.etl.TargetFieldDerivedBinding ... targetFieldDerivedBinding);
    public com.runwaysdk.query.Condition notContainsAll(com.runwayskd.geodashboard.etl.TargetFieldDerivedBinding ... targetFieldDerivedBinding);
    public com.runwaysdk.query.Condition containsExactly(com.runwayskd.geodashboard.etl.TargetFieldDerivedBinding ... targetFieldDerivedBinding);
  }

/**
 * Implements type safe query methods.
 * This type is used when a join is performed on this class as a reference.
 **/
  public static class TargetFieldDerivedBindingQueryMultiReference extends com.runwayskd.geodashboard.etl.TargetFieldCoordinateBindingQuery.TargetFieldCoordinateBindingQueryMultiReference
 implements TargetFieldDerivedBindingQueryMultiReferenceIF
, com.runwaysdk.generation.loader.Reloadable
  {

  public TargetFieldDerivedBindingQueryMultiReference(com.runwaysdk.dataaccess.MdAttributeMultiReferenceDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias, String mdMultiReferenceTableName, com.runwaysdk.dataaccess.MdBusinessDAOIF referenceMdBusinessIF, String referenceTableAlias, com.runwaysdk.query.ComponentQuery rootQuery, java.util.Set<com.runwaysdk.query.Join> tableJoinSet, String alias, String displayLabel)
  {
    super(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, mdMultiReferenceTableName, referenceMdBusinessIF, referenceTableAlias, rootQuery, tableJoinSet, alias, displayLabel);

  }



    public com.runwaysdk.query.Condition containsAny(com.runwayskd.geodashboard.etl.TargetFieldDerivedBinding ... targetFieldDerivedBinding)  {

      String[] itemIdArray = new String[targetFieldDerivedBinding.length]; 

      for (int i=0; i<targetFieldDerivedBinding.length; i++)
      {
        itemIdArray[i] = targetFieldDerivedBinding[i].getId();
      }

      return this.containsAny(itemIdArray);
  }

    public com.runwaysdk.query.Condition notContainsAny(com.runwayskd.geodashboard.etl.TargetFieldDerivedBinding ... targetFieldDerivedBinding)  {

      String[] itemIdArray = new String[targetFieldDerivedBinding.length]; 

      for (int i=0; i<targetFieldDerivedBinding.length; i++)
      {
        itemIdArray[i] = targetFieldDerivedBinding[i].getId();
      }

      return this.notContainsAny(itemIdArray);
  }

    public com.runwaysdk.query.Condition containsAll(com.runwayskd.geodashboard.etl.TargetFieldDerivedBinding ... targetFieldDerivedBinding)  {

      String[] itemIdArray = new String[targetFieldDerivedBinding.length]; 

      for (int i=0; i<targetFieldDerivedBinding.length; i++)
      {
        itemIdArray[i] = targetFieldDerivedBinding[i].getId();
      }

      return this.containsAll(itemIdArray);
  }

    public com.runwaysdk.query.Condition notContainsAll(com.runwayskd.geodashboard.etl.TargetFieldDerivedBinding ... targetFieldDerivedBinding)  {

      String[] itemIdArray = new String[targetFieldDerivedBinding.length]; 

      for (int i=0; i<targetFieldDerivedBinding.length; i++)
      {
        itemIdArray[i] = targetFieldDerivedBinding[i].getId();
      }

      return this.notContainsAll(itemIdArray);
  }

    public com.runwaysdk.query.Condition containsExactly(com.runwayskd.geodashboard.etl.TargetFieldDerivedBinding ... targetFieldDerivedBinding)  {

      String[] itemIdArray = new String[targetFieldDerivedBinding.length]; 

      for (int i=0; i<targetFieldDerivedBinding.length; i++)
      {
        itemIdArray[i] = targetFieldDerivedBinding[i].getId();
      }

      return this.containsExactly(itemIdArray);
  }
  public com.runwaysdk.system.gis.geo.GeoEntityQuery.GeoEntityQueryReferenceIF getGeoEntity()
  {
    return getGeoEntity(null);

  }
 
  public com.runwaysdk.system.gis.geo.GeoEntityQuery.GeoEntityQueryReferenceIF getGeoEntity(String alias)
  {
    return (com.runwaysdk.system.gis.geo.GeoEntityQuery.GeoEntityQueryReferenceIF)this.get(com.runwayskd.geodashboard.etl.TargetFieldDerivedBinding.GEOENTITY, alias, null);

  }
 
  public com.runwaysdk.system.gis.geo.GeoEntityQuery.GeoEntityQueryReferenceIF getGeoEntity(String alias, String displayLabel)
  {
    return (com.runwaysdk.system.gis.geo.GeoEntityQuery.GeoEntityQueryReferenceIF)this.get(com.runwayskd.geodashboard.etl.TargetFieldDerivedBinding.GEOENTITY,  alias, displayLabel);

  }
  public com.runwaysdk.system.gis.geo.UniversalQuery.UniversalQueryReferenceIF getUniversal()
  {
    return getUniversal(null);

  }
 
  public com.runwaysdk.system.gis.geo.UniversalQuery.UniversalQueryReferenceIF getUniversal(String alias)
  {
    return (com.runwaysdk.system.gis.geo.UniversalQuery.UniversalQueryReferenceIF)this.get(com.runwayskd.geodashboard.etl.TargetFieldDerivedBinding.UNIVERSAL, alias, null);

  }
 
  public com.runwaysdk.system.gis.geo.UniversalQuery.UniversalQueryReferenceIF getUniversal(String alias, String displayLabel)
  {
    return (com.runwaysdk.system.gis.geo.UniversalQuery.UniversalQueryReferenceIF)this.get(com.runwayskd.geodashboard.etl.TargetFieldDerivedBinding.UNIVERSAL,  alias, displayLabel);

  }
  protected com.runwaysdk.query.AttributeReference referenceFactory( com.runwaysdk.dataaccess.MdAttributeRefDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias,  com.runwaysdk.dataaccess.MdBusinessDAOIF referenceMdBusinessIF, String referenceTableAlias, com.runwaysdk.query.ComponentQuery rootQuery, java.util.Set<com.runwaysdk.query.Join> tableJoinSet, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    String name = mdAttributeIF.definesAttribute();
    
    if (name.equals(com.runwayskd.geodashboard.etl.TargetFieldDerivedBinding.GEOENTITY)) 
    {
       return new com.runwaysdk.system.gis.geo.GeoEntityQuery.GeoEntityQueryReference((com.runwaysdk.dataaccess.MdAttributeRefDAOIF)mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, referenceMdBusinessIF, referenceTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (name.equals(com.runwayskd.geodashboard.etl.TargetFieldDerivedBinding.UNIVERSAL)) 
    {
       return new com.runwaysdk.system.gis.geo.UniversalQuery.UniversalQueryReference((com.runwaysdk.dataaccess.MdAttributeRefDAOIF)mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, referenceMdBusinessIF, referenceTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else 
    {
      return super.referenceFactory(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, referenceMdBusinessIF, referenceTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
  }

  }
}
