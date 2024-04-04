package net.geoprism.graph;

@com.runwaysdk.business.ClassSignature(hash = 1941949831)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to DirectedAcyclicGraphTypeSnapshotDescription.java
 *
 * @author Autogenerated by RunwaySDK
 */
public abstract class DirectedAcyclicGraphTypeSnapshotDescriptionBase extends com.runwaysdk.business.LocalStruct
{
  public final static String CLASS = "net.geoprism.graph.DirectedAcyclicGraphTypeSnapshotDescription";
  public final static java.lang.String DEFAULTLOCALE = "defaultLocale";
  public final static java.lang.String KEYNAME = "keyName";
  public final static java.lang.String OID = "oid";
  public final static java.lang.String SITEMASTER = "siteMaster";
  @SuppressWarnings("unused")
  private static final long serialVersionUID = 1941949831;
  
  public DirectedAcyclicGraphTypeSnapshotDescriptionBase()
  {
    super();
  }
  
  public DirectedAcyclicGraphTypeSnapshotDescriptionBase(com.runwaysdk.business.MutableWithStructs component, String structName)
  {
    super(component, structName);
  }
  
  public static DirectedAcyclicGraphTypeSnapshotDescription get(String oid)
  {
    return (DirectedAcyclicGraphTypeSnapshotDescription) com.runwaysdk.business.Struct.get(oid);
  }
  
  public static DirectedAcyclicGraphTypeSnapshotDescription getByKey(String key)
  {
    return (DirectedAcyclicGraphTypeSnapshotDescription) com.runwaysdk.business.Struct.get(CLASS, key);
  }
  
  public String getKeyName()
  {
    return getValue(KEYNAME);
  }
  
  public void validateKeyName()
  {
    this.validateAttribute(KEYNAME);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF getKeyNameMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.graph.DirectedAcyclicGraphTypeSnapshotDescription.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF)mdClassIF.definesAttribute(KEYNAME);
  }
  
  public void setKeyName(String value)
  {
    if(value == null)
    {
      setValue(KEYNAME, "");
    }
    else
    {
      setValue(KEYNAME, value);
    }
  }
  
  public String getOid()
  {
    return getValue(OID);
  }
  
  public void validateOid()
  {
    this.validateAttribute(OID);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeUUIDDAOIF getOidMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.graph.DirectedAcyclicGraphTypeSnapshotDescription.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeUUIDDAOIF)mdClassIF.definesAttribute(OID);
  }
  
  public String getSiteMaster()
  {
    return getValue(SITEMASTER);
  }
  
  public void validateSiteMaster()
  {
    this.validateAttribute(SITEMASTER);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF getSiteMasterMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.graph.DirectedAcyclicGraphTypeSnapshotDescription.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF)mdClassIF.definesAttribute(SITEMASTER);
  }
  
  protected String getDeclaredType()
  {
    return CLASS;
  }
  
  public static DirectedAcyclicGraphTypeSnapshotDescriptionQuery getAllInstances(String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    DirectedAcyclicGraphTypeSnapshotDescriptionQuery query = new DirectedAcyclicGraphTypeSnapshotDescriptionQuery(new com.runwaysdk.query.QueryFactory());
    com.runwaysdk.business.Entity.getAllInstances(query, sortAttribute, ascending, pageSize, pageNumber);
    return query;
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
