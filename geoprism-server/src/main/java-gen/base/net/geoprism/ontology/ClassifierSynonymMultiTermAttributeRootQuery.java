package net.geoprism.ontology;

@com.runwaysdk.business.ClassSignature(hash = -777833095)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to ClassifierSynonymMultiTermAttributeRoot.java
 *
 * @author Autogenerated by RunwaySDK
 */
public  class ClassifierSynonymMultiTermAttributeRootQuery extends com.runwaysdk.query.GeneratedRelationshipQuery implements com.runwaysdk.generation.loader.Reloadable
{

  public ClassifierSynonymMultiTermAttributeRootQuery(com.runwaysdk.query.QueryFactory componentQueryFactory)
  {
     super();
    if (this.getComponentQuery() == null)
    {
      com.runwaysdk.business.RelationshipQuery relationshipQuery = componentQueryFactory.relationshipQuery(this.getClassType());

       this.setRelationshipQuery(relationshipQuery);
    }
  }

  public ClassifierSynonymMultiTermAttributeRootQuery(com.runwaysdk.query.ValueQuery valueQuery)
  {
     super();
    if (this.getComponentQuery() == null)
    {
      com.runwaysdk.business.RelationshipQuery relationshipQuery = new com.runwaysdk.business.RelationshipQuery(valueQuery, this.getClassType());

       this.setRelationshipQuery(relationshipQuery);
    }
  }

  public String getClassType()
  {
    return net.geoprism.ontology.ClassifierSynonymMultiTermAttributeRoot.CLASS;
  }
  /**
   * Restricts the query to include objects that are children in this relationship.
   * @param classifierSynonymQuery
   * @return Condition restricting objects that are children in this relationship.
   */
   public com.runwaysdk.query.Condition hasChild(net.geoprism.ontology.ClassifierSynonymQuery classifierSynonymQuery)
   {
     return this.getRelationshipQuery().hasChild(classifierSynonymQuery);
   }
  /**
   * Restricts the query to include objects that are children in this relationship.
   * @param classifierSynonymQuery
   * @return Condition restricting objects that are children in this relationship.
   */
   public com.runwaysdk.query.Condition doesNotHaveChild(net.geoprism.ontology.ClassifierSynonymQuery classifierSynonymQuery)
   {
     return this.getRelationshipQuery().doesNotHaveChild(classifierSynonymQuery);
   }
  /**
   * Restricts the query to include objects that are parents in this relationship.
   * @param mdAttributeMultiTermQuery
   * @return Condition restricting objects that are parents in this relationship.
   */
   public com.runwaysdk.query.Condition hasParent(com.runwaysdk.system.metadata.MdAttributeMultiTermQuery mdAttributeMultiTermQuery)
   {
     return this.getRelationshipQuery().hasParent(mdAttributeMultiTermQuery);
   }
  /**
   * Restricts the query to include objects that are parents in this relationship.
   * @param mdAttributeMultiTermQuery
   * @return Condition restricting objects that are parents in this relationship.
   */
   public com.runwaysdk.query.Condition doesNotHaveParent(com.runwaysdk.system.metadata.MdAttributeMultiTermQuery mdAttributeMultiTermQuery)
   {
     return this.getRelationshipQuery().doesNotHaveParent(mdAttributeMultiTermQuery);
   }
  protected com.runwaysdk.query.AttributeReference referenceFactory( com.runwaysdk.dataaccess.MdAttributeRefDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias,  com.runwaysdk.dataaccess.MdBusinessDAOIF referenceMdBusinessIF, String referenceTableAlias, com.runwaysdk.query.ComponentQuery rootQuery, java.util.Set<com.runwaysdk.query.Join> tableJoinSet, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    String name = mdAttributeIF.definesAttribute();
    
    if (name.equals(net.geoprism.ontology.ClassifierSynonymMultiTermAttributeRoot.CREATEDBY)) 
    {
       return new com.runwaysdk.system.SingleActorQuery.SingleActorQueryReference((com.runwaysdk.dataaccess.MdAttributeRefDAOIF)mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, referenceMdBusinessIF, referenceTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (name.equals(net.geoprism.ontology.ClassifierSynonymMultiTermAttributeRoot.ENTITYDOMAIN)) 
    {
       return new com.runwaysdk.system.metadata.MdDomainQuery.MdDomainQueryReference((com.runwaysdk.dataaccess.MdAttributeRefDAOIF)mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, referenceMdBusinessIF, referenceTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (name.equals(net.geoprism.ontology.ClassifierSynonymMultiTermAttributeRoot.LASTUPDATEDBY)) 
    {
       return new com.runwaysdk.system.SingleActorQuery.SingleActorQueryReference((com.runwaysdk.dataaccess.MdAttributeRefDAOIF)mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, referenceMdBusinessIF, referenceTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (name.equals(net.geoprism.ontology.ClassifierSynonymMultiTermAttributeRoot.LOCKEDBY)) 
    {
       return new com.runwaysdk.system.SingleActorQuery.SingleActorQueryReference((com.runwaysdk.dataaccess.MdAttributeRefDAOIF)mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, referenceMdBusinessIF, referenceTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else if (name.equals(net.geoprism.ontology.ClassifierSynonymMultiTermAttributeRoot.OWNER)) 
    {
       return new com.runwaysdk.system.ActorQuery.ActorQueryReference((com.runwaysdk.dataaccess.MdAttributeRefDAOIF)mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, referenceMdBusinessIF, referenceTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
    }
    else 
    {
      String error = "Attribute type ["+mdAttributeIF.getType()+"] is invalid.";
      throw new com.runwaysdk.query.QueryException(error);
    }
  }

  public com.runwaysdk.query.SelectableMoment getCreateDate()
  {
    return getCreateDate(null);

  }
 
  public com.runwaysdk.query.SelectableMoment getCreateDate(String alias)
  {
    return (com.runwaysdk.query.SelectableMoment)this.getComponentQuery().get(net.geoprism.ontology.ClassifierSynonymMultiTermAttributeRoot.CREATEDATE, alias, null);

  }
 
  public com.runwaysdk.query.SelectableMoment getCreateDate(String alias, String displayLabel)
  {
    return (com.runwaysdk.query.SelectableMoment)this.getComponentQuery().get(net.geoprism.ontology.ClassifierSynonymMultiTermAttributeRoot.CREATEDATE, alias, displayLabel);

  }
  public com.runwaysdk.system.SingleActorQuery.SingleActorQueryReferenceIF getCreatedBy()
  {
    return getCreatedBy(null);

  }
 
  public com.runwaysdk.system.SingleActorQuery.SingleActorQueryReferenceIF getCreatedBy(String alias)
  {

    com.runwaysdk.dataaccess.MdAttributeDAOIF mdAttributeIF = this.getComponentQuery().getMdAttributeROfromMap(net.geoprism.ontology.ClassifierSynonymMultiTermAttributeRoot.CREATEDBY);

    return (com.runwaysdk.system.SingleActorQuery.SingleActorQueryReferenceIF)this.getComponentQuery().internalAttributeFactory(net.geoprism.ontology.ClassifierSynonymMultiTermAttributeRoot.CREATEDBY, mdAttributeIF, this, alias, null);

  }
 
  public com.runwaysdk.system.SingleActorQuery.SingleActorQueryReferenceIF getCreatedBy(String alias, String displayLabel)
  {

    com.runwaysdk.dataaccess.MdAttributeDAOIF mdAttributeIF = this.getComponentQuery().getMdAttributeROfromMap(net.geoprism.ontology.ClassifierSynonymMultiTermAttributeRoot.CREATEDBY);

    return (com.runwaysdk.system.SingleActorQuery.SingleActorQueryReferenceIF)this.getComponentQuery().internalAttributeFactory(net.geoprism.ontology.ClassifierSynonymMultiTermAttributeRoot.CREATEDBY, mdAttributeIF, this, alias, displayLabel);

  }
  public com.runwaysdk.system.metadata.MdDomainQuery.MdDomainQueryReferenceIF getEntityDomain()
  {
    return getEntityDomain(null);

  }
 
  public com.runwaysdk.system.metadata.MdDomainQuery.MdDomainQueryReferenceIF getEntityDomain(String alias)
  {

    com.runwaysdk.dataaccess.MdAttributeDAOIF mdAttributeIF = this.getComponentQuery().getMdAttributeROfromMap(net.geoprism.ontology.ClassifierSynonymMultiTermAttributeRoot.ENTITYDOMAIN);

    return (com.runwaysdk.system.metadata.MdDomainQuery.MdDomainQueryReferenceIF)this.getComponentQuery().internalAttributeFactory(net.geoprism.ontology.ClassifierSynonymMultiTermAttributeRoot.ENTITYDOMAIN, mdAttributeIF, this, alias, null);

  }
 
  public com.runwaysdk.system.metadata.MdDomainQuery.MdDomainQueryReferenceIF getEntityDomain(String alias, String displayLabel)
  {

    com.runwaysdk.dataaccess.MdAttributeDAOIF mdAttributeIF = this.getComponentQuery().getMdAttributeROfromMap(net.geoprism.ontology.ClassifierSynonymMultiTermAttributeRoot.ENTITYDOMAIN);

    return (com.runwaysdk.system.metadata.MdDomainQuery.MdDomainQueryReferenceIF)this.getComponentQuery().internalAttributeFactory(net.geoprism.ontology.ClassifierSynonymMultiTermAttributeRoot.ENTITYDOMAIN, mdAttributeIF, this, alias, displayLabel);

  }
  public com.runwaysdk.query.SelectableChar getId()
  {
    return getId(null);

  }
 
  public com.runwaysdk.query.SelectableChar getId(String alias)
  {
    return (com.runwaysdk.query.SelectableChar)this.getComponentQuery().get(net.geoprism.ontology.ClassifierSynonymMultiTermAttributeRoot.ID, alias, null);

  }
 
  public com.runwaysdk.query.SelectableChar getId(String alias, String displayLabel)
  {
    return (com.runwaysdk.query.SelectableChar)this.getComponentQuery().get(net.geoprism.ontology.ClassifierSynonymMultiTermAttributeRoot.ID, alias, displayLabel);

  }
  public com.runwaysdk.query.SelectableChar getKeyName()
  {
    return getKeyName(null);

  }
 
  public com.runwaysdk.query.SelectableChar getKeyName(String alias)
  {
    return (com.runwaysdk.query.SelectableChar)this.getComponentQuery().get(net.geoprism.ontology.ClassifierSynonymMultiTermAttributeRoot.KEYNAME, alias, null);

  }
 
  public com.runwaysdk.query.SelectableChar getKeyName(String alias, String displayLabel)
  {
    return (com.runwaysdk.query.SelectableChar)this.getComponentQuery().get(net.geoprism.ontology.ClassifierSynonymMultiTermAttributeRoot.KEYNAME, alias, displayLabel);

  }
  public com.runwaysdk.query.SelectableMoment getLastUpdateDate()
  {
    return getLastUpdateDate(null);

  }
 
  public com.runwaysdk.query.SelectableMoment getLastUpdateDate(String alias)
  {
    return (com.runwaysdk.query.SelectableMoment)this.getComponentQuery().get(net.geoprism.ontology.ClassifierSynonymMultiTermAttributeRoot.LASTUPDATEDATE, alias, null);

  }
 
  public com.runwaysdk.query.SelectableMoment getLastUpdateDate(String alias, String displayLabel)
  {
    return (com.runwaysdk.query.SelectableMoment)this.getComponentQuery().get(net.geoprism.ontology.ClassifierSynonymMultiTermAttributeRoot.LASTUPDATEDATE, alias, displayLabel);

  }
  public com.runwaysdk.system.SingleActorQuery.SingleActorQueryReferenceIF getLastUpdatedBy()
  {
    return getLastUpdatedBy(null);

  }
 
  public com.runwaysdk.system.SingleActorQuery.SingleActorQueryReferenceIF getLastUpdatedBy(String alias)
  {

    com.runwaysdk.dataaccess.MdAttributeDAOIF mdAttributeIF = this.getComponentQuery().getMdAttributeROfromMap(net.geoprism.ontology.ClassifierSynonymMultiTermAttributeRoot.LASTUPDATEDBY);

    return (com.runwaysdk.system.SingleActorQuery.SingleActorQueryReferenceIF)this.getComponentQuery().internalAttributeFactory(net.geoprism.ontology.ClassifierSynonymMultiTermAttributeRoot.LASTUPDATEDBY, mdAttributeIF, this, alias, null);

  }
 
  public com.runwaysdk.system.SingleActorQuery.SingleActorQueryReferenceIF getLastUpdatedBy(String alias, String displayLabel)
  {

    com.runwaysdk.dataaccess.MdAttributeDAOIF mdAttributeIF = this.getComponentQuery().getMdAttributeROfromMap(net.geoprism.ontology.ClassifierSynonymMultiTermAttributeRoot.LASTUPDATEDBY);

    return (com.runwaysdk.system.SingleActorQuery.SingleActorQueryReferenceIF)this.getComponentQuery().internalAttributeFactory(net.geoprism.ontology.ClassifierSynonymMultiTermAttributeRoot.LASTUPDATEDBY, mdAttributeIF, this, alias, displayLabel);

  }
  public com.runwaysdk.system.SingleActorQuery.SingleActorQueryReferenceIF getLockedBy()
  {
    return getLockedBy(null);

  }
 
  public com.runwaysdk.system.SingleActorQuery.SingleActorQueryReferenceIF getLockedBy(String alias)
  {

    com.runwaysdk.dataaccess.MdAttributeDAOIF mdAttributeIF = this.getComponentQuery().getMdAttributeROfromMap(net.geoprism.ontology.ClassifierSynonymMultiTermAttributeRoot.LOCKEDBY);

    return (com.runwaysdk.system.SingleActorQuery.SingleActorQueryReferenceIF)this.getComponentQuery().internalAttributeFactory(net.geoprism.ontology.ClassifierSynonymMultiTermAttributeRoot.LOCKEDBY, mdAttributeIF, this, alias, null);

  }
 
  public com.runwaysdk.system.SingleActorQuery.SingleActorQueryReferenceIF getLockedBy(String alias, String displayLabel)
  {

    com.runwaysdk.dataaccess.MdAttributeDAOIF mdAttributeIF = this.getComponentQuery().getMdAttributeROfromMap(net.geoprism.ontology.ClassifierSynonymMultiTermAttributeRoot.LOCKEDBY);

    return (com.runwaysdk.system.SingleActorQuery.SingleActorQueryReferenceIF)this.getComponentQuery().internalAttributeFactory(net.geoprism.ontology.ClassifierSynonymMultiTermAttributeRoot.LOCKEDBY, mdAttributeIF, this, alias, displayLabel);

  }
  public com.runwaysdk.system.ActorQuery.ActorQueryReferenceIF getOwner()
  {
    return getOwner(null);

  }
 
  public com.runwaysdk.system.ActorQuery.ActorQueryReferenceIF getOwner(String alias)
  {

    com.runwaysdk.dataaccess.MdAttributeDAOIF mdAttributeIF = this.getComponentQuery().getMdAttributeROfromMap(net.geoprism.ontology.ClassifierSynonymMultiTermAttributeRoot.OWNER);

    return (com.runwaysdk.system.ActorQuery.ActorQueryReferenceIF)this.getComponentQuery().internalAttributeFactory(net.geoprism.ontology.ClassifierSynonymMultiTermAttributeRoot.OWNER, mdAttributeIF, this, alias, null);

  }
 
  public com.runwaysdk.system.ActorQuery.ActorQueryReferenceIF getOwner(String alias, String displayLabel)
  {

    com.runwaysdk.dataaccess.MdAttributeDAOIF mdAttributeIF = this.getComponentQuery().getMdAttributeROfromMap(net.geoprism.ontology.ClassifierSynonymMultiTermAttributeRoot.OWNER);

    return (com.runwaysdk.system.ActorQuery.ActorQueryReferenceIF)this.getComponentQuery().internalAttributeFactory(net.geoprism.ontology.ClassifierSynonymMultiTermAttributeRoot.OWNER, mdAttributeIF, this, alias, displayLabel);

  }
  public com.runwaysdk.query.SelectableBoolean getSelectable()
  {
    return getSelectable(null);

  }
 
  public com.runwaysdk.query.SelectableBoolean getSelectable(String alias)
  {
    return (com.runwaysdk.query.SelectableBoolean)this.getComponentQuery().get(net.geoprism.ontology.ClassifierSynonymMultiTermAttributeRoot.SELECTABLE, alias, null);

  }
 
  public com.runwaysdk.query.SelectableBoolean getSelectable(String alias, String displayLabel)
  {
    return (com.runwaysdk.query.SelectableBoolean)this.getComponentQuery().get(net.geoprism.ontology.ClassifierSynonymMultiTermAttributeRoot.SELECTABLE, alias, displayLabel);

  }
  public com.runwaysdk.query.SelectableLong getSeq()
  {
    return getSeq(null);

  }
 
  public com.runwaysdk.query.SelectableLong getSeq(String alias)
  {
    return (com.runwaysdk.query.SelectableLong)this.getComponentQuery().get(net.geoprism.ontology.ClassifierSynonymMultiTermAttributeRoot.SEQ, alias, null);

  }
 
  public com.runwaysdk.query.SelectableLong getSeq(String alias, String displayLabel)
  {
    return (com.runwaysdk.query.SelectableLong)this.getComponentQuery().get(net.geoprism.ontology.ClassifierSynonymMultiTermAttributeRoot.SEQ, alias, displayLabel);

  }
  public com.runwaysdk.query.SelectableChar getSiteMaster()
  {
    return getSiteMaster(null);

  }
 
  public com.runwaysdk.query.SelectableChar getSiteMaster(String alias)
  {
    return (com.runwaysdk.query.SelectableChar)this.getComponentQuery().get(net.geoprism.ontology.ClassifierSynonymMultiTermAttributeRoot.SITEMASTER, alias, null);

  }
 
  public com.runwaysdk.query.SelectableChar getSiteMaster(String alias, String displayLabel)
  {
    return (com.runwaysdk.query.SelectableChar)this.getComponentQuery().get(net.geoprism.ontology.ClassifierSynonymMultiTermAttributeRoot.SITEMASTER, alias, displayLabel);

  }
  public com.runwaysdk.query.SelectableChar getType()
  {
    return getType(null);

  }
 
  public com.runwaysdk.query.SelectableChar getType(String alias)
  {
    return (com.runwaysdk.query.SelectableChar)this.getComponentQuery().get(net.geoprism.ontology.ClassifierSynonymMultiTermAttributeRoot.TYPE, alias, null);

  }
 
  public com.runwaysdk.query.SelectableChar getType(String alias, String displayLabel)
  {
    return (com.runwaysdk.query.SelectableChar)this.getComponentQuery().get(net.geoprism.ontology.ClassifierSynonymMultiTermAttributeRoot.TYPE, alias, displayLabel);

  }
  /**  
   * Returns an iterator of Business objects that match the query criteria specified
   * on this query object. 
   * @return iterator of Business objects that match the query criteria specified
   * on this query object.
   */
  public com.runwaysdk.query.OIterator<? extends ClassifierSynonymMultiTermAttributeRoot> getIterator()
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
    return new com.runwaysdk.business.RelationshipIterator<ClassifierSynonymMultiTermAttributeRoot>(this.getComponentQuery().getMdEntityIF(), this.getRelationshipQuery(), columnInfoMap, results);
  }

}
