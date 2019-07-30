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

@com.runwaysdk.business.ClassSignature(hash = -1855322552)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to DashboardReferenceLayer.java
 *
 * @author Autogenerated by RunwaySDK
 */
public  class DashboardReferenceLayerQuery extends net.geoprism.dashboard.layer.DashboardLayerQuery
 
{

  public DashboardReferenceLayerQuery(com.runwaysdk.query.QueryFactory componentQueryFactory)
  {
    super(componentQueryFactory);
    if (this.getComponentQuery() == null)
    {
      com.runwaysdk.business.BusinessQuery businessQuery = componentQueryFactory.businessQuery(this.getClassType());

       this.setBusinessQuery(businessQuery);
    }
  }

  public DashboardReferenceLayerQuery(com.runwaysdk.query.ValueQuery valueQuery)
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
    return net.geoprism.dashboard.layer.DashboardReferenceLayer.CLASS;
  }
  public com.runwaysdk.system.gis.geo.UniversalQuery.UniversalQueryReferenceIF getUniversal()
  {
    return getUniversal(null);

  }
 
  public com.runwaysdk.system.gis.geo.UniversalQuery.UniversalQueryReferenceIF getUniversal(String alias)
  {

    com.runwaysdk.dataaccess.MdAttributeDAOIF mdAttributeIF = this.getComponentQuery().getMdAttributeROfromMap(net.geoprism.dashboard.layer.DashboardReferenceLayer.UNIVERSAL);

    return (com.runwaysdk.system.gis.geo.UniversalQuery.UniversalQueryReferenceIF)this.getComponentQuery().internalAttributeFactory(net.geoprism.dashboard.layer.DashboardReferenceLayer.UNIVERSAL, mdAttributeIF, this, alias, null);

  }
 
  public com.runwaysdk.system.gis.geo.UniversalQuery.UniversalQueryReferenceIF getUniversal(String alias, String displayLabel)
  {

    com.runwaysdk.dataaccess.MdAttributeDAOIF mdAttributeIF = this.getComponentQuery().getMdAttributeROfromMap(net.geoprism.dashboard.layer.DashboardReferenceLayer.UNIVERSAL);

    return (com.runwaysdk.system.gis.geo.UniversalQuery.UniversalQueryReferenceIF)this.getComponentQuery().internalAttributeFactory(net.geoprism.dashboard.layer.DashboardReferenceLayer.UNIVERSAL, mdAttributeIF, this, alias, displayLabel);

  }
  protected com.runwaysdk.query.AttributeReference referenceFactory( com.runwaysdk.dataaccess.MdAttributeRefDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias,  com.runwaysdk.dataaccess.MdBusinessDAOIF referenceMdBusinessIF, String referenceTableAlias, com.runwaysdk.query.ComponentQuery rootQuery, java.util.Set<com.runwaysdk.query.Join> tableJoinSet, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    String name = mdAttributeIF.definesAttribute();
    
    if (name.equals(net.geoprism.dashboard.layer.DashboardReferenceLayer.UNIVERSAL)) 
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
  public com.runwaysdk.query.OIterator<? extends DashboardReferenceLayer> getIterator()
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
    return new com.runwaysdk.business.BusinessIterator<DashboardReferenceLayer>(this.getComponentQuery().getMdEntityIF(), columnInfoMap, results);
  }


/**
 * Interface that masks all type unsafe query methods and defines all type safe methods.
 * This type is used when a join is performed on this class as a reference.
 **/
  public interface DashboardReferenceLayerQueryReferenceIF extends  net.geoprism.dashboard.layer.DashboardLayerQuery.DashboardLayerQueryReferenceIF
  {

    public com.runwaysdk.system.gis.geo.UniversalQuery.UniversalQueryReferenceIF getUniversal();
    public com.runwaysdk.system.gis.geo.UniversalQuery.UniversalQueryReferenceIF getUniversal(String alias);
    public com.runwaysdk.system.gis.geo.UniversalQuery.UniversalQueryReferenceIF getUniversal(String alias, String displayLabel);

    public com.runwaysdk.query.BasicCondition EQ(net.geoprism.dashboard.layer.DashboardReferenceLayer dashboardReferenceLayer);

    public com.runwaysdk.query.BasicCondition NE(net.geoprism.dashboard.layer.DashboardReferenceLayer dashboardReferenceLayer);

  }

/**
 * Implements type safe query methods.
 * This type is used when a join is performed on this class as a reference.
 **/
  public static class DashboardReferenceLayerQueryReference extends net.geoprism.dashboard.layer.DashboardLayerQuery.DashboardLayerQueryReference
 implements DashboardReferenceLayerQueryReferenceIF

  {

  public DashboardReferenceLayerQueryReference(com.runwaysdk.dataaccess.MdAttributeRefDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias, com.runwaysdk.dataaccess.MdBusinessDAOIF referenceMdBusinessIF, String referenceTableAlias, com.runwaysdk.query.ComponentQuery rootQuery, java.util.Set<com.runwaysdk.query.Join> tableJoinSet, String alias, String displayLabel)
  {
    super(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, referenceMdBusinessIF, referenceTableAlias, rootQuery, tableJoinSet, alias, displayLabel);

  }


    public com.runwaysdk.query.BasicCondition EQ(net.geoprism.dashboard.layer.DashboardReferenceLayer dashboardReferenceLayer)
    {
      if(dashboardReferenceLayer == null) return this.EQ((java.lang.String)null);
      return this.EQ(dashboardReferenceLayer.getOid());
    }

    public com.runwaysdk.query.BasicCondition NE(net.geoprism.dashboard.layer.DashboardReferenceLayer dashboardReferenceLayer)
    {
      if(dashboardReferenceLayer == null) return this.NE((java.lang.String)null);
      return this.NE(dashboardReferenceLayer.getOid());
    }

  public com.runwaysdk.system.gis.geo.UniversalQuery.UniversalQueryReferenceIF getUniversal()
  {
    return getUniversal(null);

  }
 
  public com.runwaysdk.system.gis.geo.UniversalQuery.UniversalQueryReferenceIF getUniversal(String alias)
  {
    return (com.runwaysdk.system.gis.geo.UniversalQuery.UniversalQueryReferenceIF)this.get(net.geoprism.dashboard.layer.DashboardReferenceLayer.UNIVERSAL, alias, null);

  }
 
  public com.runwaysdk.system.gis.geo.UniversalQuery.UniversalQueryReferenceIF getUniversal(String alias, String displayLabel)
  {
    return (com.runwaysdk.system.gis.geo.UniversalQuery.UniversalQueryReferenceIF)this.get(net.geoprism.dashboard.layer.DashboardReferenceLayer.UNIVERSAL,  alias, displayLabel);

  }
  protected com.runwaysdk.query.AttributeReference referenceFactory( com.runwaysdk.dataaccess.MdAttributeRefDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias,  com.runwaysdk.dataaccess.MdBusinessDAOIF referenceMdBusinessIF, String referenceTableAlias, com.runwaysdk.query.ComponentQuery rootQuery, java.util.Set<com.runwaysdk.query.Join> tableJoinSet, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    String name = mdAttributeIF.definesAttribute();
    
    if (name.equals(net.geoprism.dashboard.layer.DashboardReferenceLayer.UNIVERSAL)) 
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
  public interface DashboardReferenceLayerQueryMultiReferenceIF extends  net.geoprism.dashboard.layer.DashboardLayerQuery.DashboardLayerQueryMultiReferenceIF
  {

    public com.runwaysdk.system.gis.geo.UniversalQuery.UniversalQueryReferenceIF getUniversal();
    public com.runwaysdk.system.gis.geo.UniversalQuery.UniversalQueryReferenceIF getUniversal(String alias);
    public com.runwaysdk.system.gis.geo.UniversalQuery.UniversalQueryReferenceIF getUniversal(String alias, String displayLabel);

    public com.runwaysdk.query.Condition containsAny(net.geoprism.dashboard.layer.DashboardReferenceLayer ... dashboardReferenceLayer);
    public com.runwaysdk.query.Condition notContainsAny(net.geoprism.dashboard.layer.DashboardReferenceLayer ... dashboardReferenceLayer);
    public com.runwaysdk.query.Condition containsAll(net.geoprism.dashboard.layer.DashboardReferenceLayer ... dashboardReferenceLayer);
    public com.runwaysdk.query.Condition notContainsAll(net.geoprism.dashboard.layer.DashboardReferenceLayer ... dashboardReferenceLayer);
    public com.runwaysdk.query.Condition containsExactly(net.geoprism.dashboard.layer.DashboardReferenceLayer ... dashboardReferenceLayer);
  }

/**
 * Implements type safe query methods.
 * This type is used when a join is performed on this class as a reference.
 **/
  public static class DashboardReferenceLayerQueryMultiReference extends net.geoprism.dashboard.layer.DashboardLayerQuery.DashboardLayerQueryMultiReference
 implements DashboardReferenceLayerQueryMultiReferenceIF

  {

  public DashboardReferenceLayerQueryMultiReference(com.runwaysdk.dataaccess.MdAttributeMultiReferenceDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias, String mdMultiReferenceTableName, com.runwaysdk.dataaccess.MdBusinessDAOIF referenceMdBusinessIF, String referenceTableAlias, com.runwaysdk.query.ComponentQuery rootQuery, java.util.Set<com.runwaysdk.query.Join> tableJoinSet, String alias, String displayLabel)
  {
    super(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, mdMultiReferenceTableName, referenceMdBusinessIF, referenceTableAlias, rootQuery, tableJoinSet, alias, displayLabel);

  }



    public com.runwaysdk.query.Condition containsAny(net.geoprism.dashboard.layer.DashboardReferenceLayer ... dashboardReferenceLayer)  {

      String[] itemIdArray = new String[dashboardReferenceLayer.length]; 

      for (int i=0; i<dashboardReferenceLayer.length; i++)
      {
        itemIdArray[i] = dashboardReferenceLayer[i].getOid();
      }

      return this.containsAny(itemIdArray);
  }

    public com.runwaysdk.query.Condition notContainsAny(net.geoprism.dashboard.layer.DashboardReferenceLayer ... dashboardReferenceLayer)  {

      String[] itemIdArray = new String[dashboardReferenceLayer.length]; 

      for (int i=0; i<dashboardReferenceLayer.length; i++)
      {
        itemIdArray[i] = dashboardReferenceLayer[i].getOid();
      }

      return this.notContainsAny(itemIdArray);
  }

    public com.runwaysdk.query.Condition containsAll(net.geoprism.dashboard.layer.DashboardReferenceLayer ... dashboardReferenceLayer)  {

      String[] itemIdArray = new String[dashboardReferenceLayer.length]; 

      for (int i=0; i<dashboardReferenceLayer.length; i++)
      {
        itemIdArray[i] = dashboardReferenceLayer[i].getOid();
      }

      return this.containsAll(itemIdArray);
  }

    public com.runwaysdk.query.Condition notContainsAll(net.geoprism.dashboard.layer.DashboardReferenceLayer ... dashboardReferenceLayer)  {

      String[] itemIdArray = new String[dashboardReferenceLayer.length]; 

      for (int i=0; i<dashboardReferenceLayer.length; i++)
      {
        itemIdArray[i] = dashboardReferenceLayer[i].getOid();
      }

      return this.notContainsAll(itemIdArray);
  }

    public com.runwaysdk.query.Condition containsExactly(net.geoprism.dashboard.layer.DashboardReferenceLayer ... dashboardReferenceLayer)  {

      String[] itemIdArray = new String[dashboardReferenceLayer.length]; 

      for (int i=0; i<dashboardReferenceLayer.length; i++)
      {
        itemIdArray[i] = dashboardReferenceLayer[i].getOid();
      }

      return this.containsExactly(itemIdArray);
  }
  public com.runwaysdk.system.gis.geo.UniversalQuery.UniversalQueryReferenceIF getUniversal()
  {
    return getUniversal(null);

  }
 
  public com.runwaysdk.system.gis.geo.UniversalQuery.UniversalQueryReferenceIF getUniversal(String alias)
  {
    return (com.runwaysdk.system.gis.geo.UniversalQuery.UniversalQueryReferenceIF)this.get(net.geoprism.dashboard.layer.DashboardReferenceLayer.UNIVERSAL, alias, null);

  }
 
  public com.runwaysdk.system.gis.geo.UniversalQuery.UniversalQueryReferenceIF getUniversal(String alias, String displayLabel)
  {
    return (com.runwaysdk.system.gis.geo.UniversalQuery.UniversalQueryReferenceIF)this.get(net.geoprism.dashboard.layer.DashboardReferenceLayer.UNIVERSAL,  alias, displayLabel);

  }
  protected com.runwaysdk.query.AttributeReference referenceFactory( com.runwaysdk.dataaccess.MdAttributeRefDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias,  com.runwaysdk.dataaccess.MdBusinessDAOIF referenceMdBusinessIF, String referenceTableAlias, com.runwaysdk.query.ComponentQuery rootQuery, java.util.Set<com.runwaysdk.query.Join> tableJoinSet, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    String name = mdAttributeIF.definesAttribute();
    
    if (name.equals(net.geoprism.dashboard.layer.DashboardReferenceLayer.UNIVERSAL)) 
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
