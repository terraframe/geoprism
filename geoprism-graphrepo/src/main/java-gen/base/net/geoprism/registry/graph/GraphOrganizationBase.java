package net.geoprism.registry.graph;

@com.runwaysdk.business.ClassSignature(hash = 122089023)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to GraphOrganization.java
 *
 * @author Autogenerated by RunwaySDK
 */
public abstract class GraphOrganizationBase extends com.runwaysdk.business.graph.VertexObject
{
  public final static String CLASS = "net.geoprism.registry.graph.GraphOrganization";
  public final static java.lang.String CODE = "code";
  public final static java.lang.String CONTACTINFO = "contactInfo";
  public final static java.lang.String DISPLAYLABEL = "displayLabel";
  public final static java.lang.String OID = "oid";
  public final static java.lang.String ORGANIZATION = "organization";
  public final static java.lang.String SEQ = "seq";
  @SuppressWarnings("unused")
  private static final long serialVersionUID = 122089023;
  
  public GraphOrganizationBase()
  {
    super();
  }
  
  public String getCode()
  {
    return (String) this.getObjectValue(CODE);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF getCodeMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.graph.GraphOrganization.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF)mdClassIF.definesAttribute(CODE);
  }
  
  public void setCode(String value)
  {
    this.setValue(CODE, value);
  }
  
  public com.runwaysdk.ComponentIF getContactInfo()
  {
    return (com.runwaysdk.ComponentIF) this.getObjectValue(CONTACTINFO);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeDAOIF getContactInfoMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.graph.GraphOrganization.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeDAOIF)mdClassIF.definesAttribute(CONTACTINFO);
  }
  
  public void setContactInfo(com.runwaysdk.ComponentIF value)
  {
    this.setValue(CONTACTINFO, value);
  }
  
  public com.runwaysdk.ComponentIF getDisplayLabel()
  {
    return (com.runwaysdk.ComponentIF) this.getObjectValue(DISPLAYLABEL);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeDAOIF getDisplayLabelMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.graph.GraphOrganization.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeDAOIF)mdClassIF.definesAttribute(DISPLAYLABEL);
  }
  
  public void setDisplayLabel(com.runwaysdk.ComponentIF value)
  {
    this.setValue(DISPLAYLABEL, value);
  }
  
  public String getOid()
  {
    return (String) this.getObjectValue(OID);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeUUIDDAOIF getOidMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.graph.GraphOrganization.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeUUIDDAOIF)mdClassIF.definesAttribute(OID);
  }
  
  public net.geoprism.registry.Organization getOrganization()
  {
    if (this.getObjectValue(ORGANIZATION) == null)
    {
      return null;
    }
    else
    {
      return net.geoprism.registry.Organization.get( (String) this.getObjectValue(ORGANIZATION));
    }
  }
  
  public String getOrganizationOid()
  {
    return (String) this.getObjectValue(ORGANIZATION);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF getOrganizationMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.graph.GraphOrganization.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF)mdClassIF.definesAttribute(ORGANIZATION);
  }
  
  public void setOrganization(net.geoprism.registry.Organization value)
  {
    this.setValue(ORGANIZATION, value.getOid());
  }
  
  public void setOrganizationId(java.lang.String oid)
  {
    this.setValue(ORGANIZATION, oid);
  }
  
  public Long getSeq()
  {
    return (Long) this.getObjectValue(SEQ);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeLongDAOIF getSeqMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.graph.GraphOrganization.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeLongDAOIF)mdClassIF.definesAttribute(SEQ);
  }
  
  public void setSeq(Long value)
  {
    this.setValue(SEQ, value);
  }
  
  protected String getDeclaredType()
  {
    return CLASS;
  }
  
  public com.runwaysdk.business.graph.EdgeObject addOrganizationHierarchyChild(net.geoprism.registry.graph.GraphOrganization graphOrganization)
  {
    return super.addChild(graphOrganization, "net.geoprism.registry.graph.OrganizationHierarchy");
  }
  
  public void removeOrganizationHierarchyChild(net.geoprism.registry.graph.GraphOrganization graphOrganization)
  {
    super.removeChild(graphOrganization, "net.geoprism.registry.graph.OrganizationHierarchy");
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<net.geoprism.registry.graph.GraphOrganization> getOrganizationHierarchyChildGraphOrganizations()
  {
    return super.getChildren("net.geoprism.registry.graph.OrganizationHierarchy",net.geoprism.registry.graph.GraphOrganization.class);
  }
  
  public com.runwaysdk.business.graph.EdgeObject addOrganizationHierarchyParent(net.geoprism.registry.graph.GraphOrganization graphOrganization)
  {
    return super.addParent(graphOrganization, "net.geoprism.registry.graph.OrganizationHierarchy");
  }
  
  public void removeOrganizationHierarchyParent(net.geoprism.registry.graph.GraphOrganization graphOrganization)
  {
    super.removeParent(graphOrganization, "net.geoprism.registry.graph.OrganizationHierarchy");
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<net.geoprism.registry.graph.GraphOrganization> getOrganizationHierarchyParentGraphOrganizations()
  {
    return super.getParents("net.geoprism.registry.graph.OrganizationHierarchy", net.geoprism.registry.graph.GraphOrganization.class);
  }
  
  public static GraphOrganization get(String oid)
  {
    return (GraphOrganization) com.runwaysdk.business.graph.VertexObject.get(CLASS, oid);
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
