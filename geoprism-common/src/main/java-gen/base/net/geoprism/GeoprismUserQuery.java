/**
 * Copyright (c) 2023 TerraFrame, Inc. All rights reserved.
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
package net.geoprism;

@com.runwaysdk.business.ClassSignature(hash = 228531742)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to GeoprismUser.java
 *
 * @author Autogenerated by RunwaySDK
 */
public  class GeoprismUserQuery extends com.runwaysdk.system.UsersQuery
 
{

  public GeoprismUserQuery(com.runwaysdk.query.QueryFactory componentQueryFactory)
  {
    super(componentQueryFactory);
    if (this.getComponentQuery() == null)
    {
      com.runwaysdk.business.BusinessQuery businessQuery = componentQueryFactory.businessQuery(this.getClassType());

       this.setBusinessQuery(businessQuery);
    }
  }

  public GeoprismUserQuery(com.runwaysdk.query.ValueQuery valueQuery)
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
    return net.geoprism.GeoprismUser.CLASS;
  }
  public com.runwaysdk.query.SelectableChar getEmail()
  {
    return getEmail(null);

  }
 
  public com.runwaysdk.query.SelectableChar getEmail(String alias)
  {
    return (com.runwaysdk.query.SelectableChar)this.getComponentQuery().get(net.geoprism.GeoprismUser.EMAIL, alias, null);

  }
 
  public com.runwaysdk.query.SelectableChar getEmail(String alias, String displayLabel)
  {
    return (com.runwaysdk.query.SelectableChar)this.getComponentQuery().get(net.geoprism.GeoprismUser.EMAIL, alias, displayLabel);

  }
  public com.runwaysdk.query.SelectableChar getFirstName()
  {
    return getFirstName(null);

  }
 
  public com.runwaysdk.query.SelectableChar getFirstName(String alias)
  {
    return (com.runwaysdk.query.SelectableChar)this.getComponentQuery().get(net.geoprism.GeoprismUser.FIRSTNAME, alias, null);

  }
 
  public com.runwaysdk.query.SelectableChar getFirstName(String alias, String displayLabel)
  {
    return (com.runwaysdk.query.SelectableChar)this.getComponentQuery().get(net.geoprism.GeoprismUser.FIRSTNAME, alias, displayLabel);

  }
  public com.runwaysdk.query.SelectableChar getLastName()
  {
    return getLastName(null);

  }
 
  public com.runwaysdk.query.SelectableChar getLastName(String alias)
  {
    return (com.runwaysdk.query.SelectableChar)this.getComponentQuery().get(net.geoprism.GeoprismUser.LASTNAME, alias, null);

  }
 
  public com.runwaysdk.query.SelectableChar getLastName(String alias, String displayLabel)
  {
    return (com.runwaysdk.query.SelectableChar)this.getComponentQuery().get(net.geoprism.GeoprismUser.LASTNAME, alias, displayLabel);

  }
  public com.runwaysdk.query.SelectableChar getPhoneNumber()
  {
    return getPhoneNumber(null);

  }
 
  public com.runwaysdk.query.SelectableChar getPhoneNumber(String alias)
  {
    return (com.runwaysdk.query.SelectableChar)this.getComponentQuery().get(net.geoprism.GeoprismUser.PHONENUMBER, alias, null);

  }
 
  public com.runwaysdk.query.SelectableChar getPhoneNumber(String alias, String displayLabel)
  {
    return (com.runwaysdk.query.SelectableChar)this.getComponentQuery().get(net.geoprism.GeoprismUser.PHONENUMBER, alias, displayLabel);

  }
  /**  
   * Returns an iterator of Business objects that match the query criteria specified
   * on this query object. 
   * @return iterator of Business objects that match the query criteria specified
   * on this query object.
   */
  public com.runwaysdk.query.OIterator<? extends GeoprismUser> getIterator()
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
    return new com.runwaysdk.business.BusinessIterator<GeoprismUser>(this.getComponentQuery().getMdEntityIF(), columnInfoMap, results);
  }


/**
 * Interface that masks all type unsafe query methods and defines all type safe methods.
 * This type is used when a join is performed on this class as a reference.
 **/
  public interface GeoprismUserQueryReferenceIF extends  com.runwaysdk.system.UsersQuery.UsersQueryReferenceIF
  {

    public com.runwaysdk.query.SelectableChar getEmail();
    public com.runwaysdk.query.SelectableChar getEmail(String alias);
    public com.runwaysdk.query.SelectableChar getEmail(String alias, String displayLabel);
    public com.runwaysdk.query.SelectableChar getFirstName();
    public com.runwaysdk.query.SelectableChar getFirstName(String alias);
    public com.runwaysdk.query.SelectableChar getFirstName(String alias, String displayLabel);
    public com.runwaysdk.query.SelectableChar getLastName();
    public com.runwaysdk.query.SelectableChar getLastName(String alias);
    public com.runwaysdk.query.SelectableChar getLastName(String alias, String displayLabel);
    public com.runwaysdk.query.SelectableChar getPhoneNumber();
    public com.runwaysdk.query.SelectableChar getPhoneNumber(String alias);
    public com.runwaysdk.query.SelectableChar getPhoneNumber(String alias, String displayLabel);

    public com.runwaysdk.query.BasicCondition EQ(net.geoprism.GeoprismUser geoprismUser);

    public com.runwaysdk.query.BasicCondition NE(net.geoprism.GeoprismUser geoprismUser);

  }

/**
 * Implements type safe query methods.
 * This type is used when a join is performed on this class as a reference.
 **/
  public static class GeoprismUserQueryReference extends com.runwaysdk.system.UsersQuery.UsersQueryReference
 implements GeoprismUserQueryReferenceIF

  {

  public GeoprismUserQueryReference(com.runwaysdk.dataaccess.MdAttributeRefDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias, com.runwaysdk.dataaccess.MdBusinessDAOIF referenceMdBusinessIF, String referenceTableAlias, com.runwaysdk.query.ComponentQuery rootQuery, java.util.Set<com.runwaysdk.query.Join> tableJoinSet, String alias, String displayLabel)
  {
    super(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, referenceMdBusinessIF, referenceTableAlias, rootQuery, tableJoinSet, alias, displayLabel);

  }


    public com.runwaysdk.query.BasicCondition EQ(net.geoprism.GeoprismUser geoprismUser)
    {
      if(geoprismUser == null) return this.EQ((java.lang.String)null);
      return this.EQ(geoprismUser.getOid());
    }

    public com.runwaysdk.query.BasicCondition NE(net.geoprism.GeoprismUser geoprismUser)
    {
      if(geoprismUser == null) return this.NE((java.lang.String)null);
      return this.NE(geoprismUser.getOid());
    }

  public com.runwaysdk.query.SelectableChar getEmail()
  {
    return getEmail(null);

  }
 
  public com.runwaysdk.query.SelectableChar getEmail(String alias)
  {
    return (com.runwaysdk.query.SelectableChar)this.get(net.geoprism.GeoprismUser.EMAIL, alias, null);

  }
 
  public com.runwaysdk.query.SelectableChar getEmail(String alias, String displayLabel)
  {
    return (com.runwaysdk.query.SelectableChar)this.get(net.geoprism.GeoprismUser.EMAIL, alias, displayLabel);

  }
  public com.runwaysdk.query.SelectableChar getFirstName()
  {
    return getFirstName(null);

  }
 
  public com.runwaysdk.query.SelectableChar getFirstName(String alias)
  {
    return (com.runwaysdk.query.SelectableChar)this.get(net.geoprism.GeoprismUser.FIRSTNAME, alias, null);

  }
 
  public com.runwaysdk.query.SelectableChar getFirstName(String alias, String displayLabel)
  {
    return (com.runwaysdk.query.SelectableChar)this.get(net.geoprism.GeoprismUser.FIRSTNAME, alias, displayLabel);

  }
  public com.runwaysdk.query.SelectableChar getLastName()
  {
    return getLastName(null);

  }
 
  public com.runwaysdk.query.SelectableChar getLastName(String alias)
  {
    return (com.runwaysdk.query.SelectableChar)this.get(net.geoprism.GeoprismUser.LASTNAME, alias, null);

  }
 
  public com.runwaysdk.query.SelectableChar getLastName(String alias, String displayLabel)
  {
    return (com.runwaysdk.query.SelectableChar)this.get(net.geoprism.GeoprismUser.LASTNAME, alias, displayLabel);

  }
  public com.runwaysdk.query.SelectableChar getPhoneNumber()
  {
    return getPhoneNumber(null);

  }
 
  public com.runwaysdk.query.SelectableChar getPhoneNumber(String alias)
  {
    return (com.runwaysdk.query.SelectableChar)this.get(net.geoprism.GeoprismUser.PHONENUMBER, alias, null);

  }
 
  public com.runwaysdk.query.SelectableChar getPhoneNumber(String alias, String displayLabel)
  {
    return (com.runwaysdk.query.SelectableChar)this.get(net.geoprism.GeoprismUser.PHONENUMBER, alias, displayLabel);

  }
  }

/**
 * Interface that masks all type unsafe query methods and defines all type safe methods.
 * This type is used when a join is performed on this class as a reference.
 **/
  public interface GeoprismUserQueryMultiReferenceIF extends  com.runwaysdk.system.UsersQuery.UsersQueryMultiReferenceIF
  {

    public com.runwaysdk.query.SelectableChar getEmail();
    public com.runwaysdk.query.SelectableChar getEmail(String alias);
    public com.runwaysdk.query.SelectableChar getEmail(String alias, String displayLabel);
    public com.runwaysdk.query.SelectableChar getFirstName();
    public com.runwaysdk.query.SelectableChar getFirstName(String alias);
    public com.runwaysdk.query.SelectableChar getFirstName(String alias, String displayLabel);
    public com.runwaysdk.query.SelectableChar getLastName();
    public com.runwaysdk.query.SelectableChar getLastName(String alias);
    public com.runwaysdk.query.SelectableChar getLastName(String alias, String displayLabel);
    public com.runwaysdk.query.SelectableChar getPhoneNumber();
    public com.runwaysdk.query.SelectableChar getPhoneNumber(String alias);
    public com.runwaysdk.query.SelectableChar getPhoneNumber(String alias, String displayLabel);

    public com.runwaysdk.query.Condition containsAny(net.geoprism.GeoprismUser ... geoprismUser);
    public com.runwaysdk.query.Condition notContainsAny(net.geoprism.GeoprismUser ... geoprismUser);
    public com.runwaysdk.query.Condition containsAll(net.geoprism.GeoprismUser ... geoprismUser);
    public com.runwaysdk.query.Condition notContainsAll(net.geoprism.GeoprismUser ... geoprismUser);
    public com.runwaysdk.query.Condition containsExactly(net.geoprism.GeoprismUser ... geoprismUser);
  }

/**
 * Implements type safe query methods.
 * This type is used when a join is performed on this class as a reference.
 **/
  public static class GeoprismUserQueryMultiReference extends com.runwaysdk.system.UsersQuery.UsersQueryMultiReference
 implements GeoprismUserQueryMultiReferenceIF

  {

  public GeoprismUserQueryMultiReference(com.runwaysdk.dataaccess.MdAttributeMultiReferenceDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias, String mdMultiReferenceTableName, com.runwaysdk.dataaccess.MdBusinessDAOIF referenceMdBusinessIF, String referenceTableAlias, com.runwaysdk.query.ComponentQuery rootQuery, java.util.Set<com.runwaysdk.query.Join> tableJoinSet, String alias, String displayLabel)
  {
    super(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, mdMultiReferenceTableName, referenceMdBusinessIF, referenceTableAlias, rootQuery, tableJoinSet, alias, displayLabel);

  }



    public com.runwaysdk.query.Condition containsAny(net.geoprism.GeoprismUser ... geoprismUser)  {

      String[] itemIdArray = new String[geoprismUser.length]; 

      for (int i=0; i<geoprismUser.length; i++)
      {
        itemIdArray[i] = geoprismUser[i].getOid();
      }

      return this.containsAny(itemIdArray);
  }

    public com.runwaysdk.query.Condition notContainsAny(net.geoprism.GeoprismUser ... geoprismUser)  {

      String[] itemIdArray = new String[geoprismUser.length]; 

      for (int i=0; i<geoprismUser.length; i++)
      {
        itemIdArray[i] = geoprismUser[i].getOid();
      }

      return this.notContainsAny(itemIdArray);
  }

    public com.runwaysdk.query.Condition containsAll(net.geoprism.GeoprismUser ... geoprismUser)  {

      String[] itemIdArray = new String[geoprismUser.length]; 

      for (int i=0; i<geoprismUser.length; i++)
      {
        itemIdArray[i] = geoprismUser[i].getOid();
      }

      return this.containsAll(itemIdArray);
  }

    public com.runwaysdk.query.Condition notContainsAll(net.geoprism.GeoprismUser ... geoprismUser)  {

      String[] itemIdArray = new String[geoprismUser.length]; 

      for (int i=0; i<geoprismUser.length; i++)
      {
        itemIdArray[i] = geoprismUser[i].getOid();
      }

      return this.notContainsAll(itemIdArray);
  }

    public com.runwaysdk.query.Condition containsExactly(net.geoprism.GeoprismUser ... geoprismUser)  {

      String[] itemIdArray = new String[geoprismUser.length]; 

      for (int i=0; i<geoprismUser.length; i++)
      {
        itemIdArray[i] = geoprismUser[i].getOid();
      }

      return this.containsExactly(itemIdArray);
  }
  public com.runwaysdk.query.SelectableChar getEmail()
  {
    return getEmail(null);

  }
 
  public com.runwaysdk.query.SelectableChar getEmail(String alias)
  {
    return (com.runwaysdk.query.SelectableChar)this.get(net.geoprism.GeoprismUser.EMAIL, alias, null);

  }
 
  public com.runwaysdk.query.SelectableChar getEmail(String alias, String displayLabel)
  {
    return (com.runwaysdk.query.SelectableChar)this.get(net.geoprism.GeoprismUser.EMAIL, alias, displayLabel);

  }
  public com.runwaysdk.query.SelectableChar getFirstName()
  {
    return getFirstName(null);

  }
 
  public com.runwaysdk.query.SelectableChar getFirstName(String alias)
  {
    return (com.runwaysdk.query.SelectableChar)this.get(net.geoprism.GeoprismUser.FIRSTNAME, alias, null);

  }
 
  public com.runwaysdk.query.SelectableChar getFirstName(String alias, String displayLabel)
  {
    return (com.runwaysdk.query.SelectableChar)this.get(net.geoprism.GeoprismUser.FIRSTNAME, alias, displayLabel);

  }
  public com.runwaysdk.query.SelectableChar getLastName()
  {
    return getLastName(null);

  }
 
  public com.runwaysdk.query.SelectableChar getLastName(String alias)
  {
    return (com.runwaysdk.query.SelectableChar)this.get(net.geoprism.GeoprismUser.LASTNAME, alias, null);

  }
 
  public com.runwaysdk.query.SelectableChar getLastName(String alias, String displayLabel)
  {
    return (com.runwaysdk.query.SelectableChar)this.get(net.geoprism.GeoprismUser.LASTNAME, alias, displayLabel);

  }
  public com.runwaysdk.query.SelectableChar getPhoneNumber()
  {
    return getPhoneNumber(null);

  }
 
  public com.runwaysdk.query.SelectableChar getPhoneNumber(String alias)
  {
    return (com.runwaysdk.query.SelectableChar)this.get(net.geoprism.GeoprismUser.PHONENUMBER, alias, null);

  }
 
  public com.runwaysdk.query.SelectableChar getPhoneNumber(String alias, String displayLabel)
  {
    return (com.runwaysdk.query.SelectableChar)this.get(net.geoprism.GeoprismUser.PHONENUMBER, alias, displayLabel);

  }
  }
}