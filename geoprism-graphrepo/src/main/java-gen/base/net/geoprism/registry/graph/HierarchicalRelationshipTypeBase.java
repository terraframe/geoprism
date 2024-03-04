package net.geoprism.registry.graph;

@com.runwaysdk.business.ClassSignature(hash = -390352493)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to HierarchicalRelationshipType.java
 *
 * @author Autogenerated by RunwaySDK
 */
public abstract class HierarchicalRelationshipTypeBase extends com.runwaysdk.business.graph.VertexObject
{
  public final static String CLASS = "net.geoprism.registry.graph.HierarchicalRelationshipType";
  public final static java.lang.String ABSTRACTDESCRIPTION = "abstractDescription";
  public final static java.lang.String ACCESSCONSTRAINTS = "accessConstraints";
  public final static java.lang.String ACKNOWLEDGEMENT = "acknowledgement";
  public final static java.lang.String CODE = "code";
  public final static java.lang.String CONTACT = "contact";
  public final static java.lang.String DBCLASSNAME = "dbClassName";
  public final static java.lang.String DEFINITIONEDGE = "definitionEdge";
  public final static java.lang.String DESCRIPTION = "description";
  public final static java.lang.String DISCLAIMER = "disclaimer";
  public final static java.lang.String DISPLAYLABEL = "displayLabel";
  public final static java.lang.String EMAIL = "email";
  public final static java.lang.String OBJECTEDGE = "objectEdge";
  public final static java.lang.String OID = "oid";
  public final static java.lang.String ORGANIZATION = "organization";
  public final static java.lang.String PHONENUMBER = "phoneNumber";
  public final static java.lang.String PROGRESS = "progress";
  public final static java.lang.String SEQ = "seq";
  public final static java.lang.String USECONSTRAINTS = "useConstraints";
  @SuppressWarnings("unused")
  private static final long serialVersionUID = -390352493;
  
  public HierarchicalRelationshipTypeBase()
  {
    super();
  }
  
  public String getAbstractDescription()
  {
    return (String) this.getObjectValue(ABSTRACTDESCRIPTION);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeTextDAOIF getAbstractDescriptionMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.graph.HierarchicalRelationshipType.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeTextDAOIF)mdClassIF.definesAttribute(ABSTRACTDESCRIPTION);
  }
  
  public void setAbstractDescription(String value)
  {
    this.setValue(ABSTRACTDESCRIPTION, value);
  }
  
  public String getAccessConstraints()
  {
    return (String) this.getObjectValue(ACCESSCONSTRAINTS);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeTextDAOIF getAccessConstraintsMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.graph.HierarchicalRelationshipType.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeTextDAOIF)mdClassIF.definesAttribute(ACCESSCONSTRAINTS);
  }
  
  public void setAccessConstraints(String value)
  {
    this.setValue(ACCESSCONSTRAINTS, value);
  }
  
  public String getAcknowledgement()
  {
    return (String) this.getObjectValue(ACKNOWLEDGEMENT);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeTextDAOIF getAcknowledgementMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.graph.HierarchicalRelationshipType.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeTextDAOIF)mdClassIF.definesAttribute(ACKNOWLEDGEMENT);
  }
  
  public void setAcknowledgement(String value)
  {
    this.setValue(ACKNOWLEDGEMENT, value);
  }
  
  public String getCode()
  {
    return (String) this.getObjectValue(CODE);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF getCodeMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.graph.HierarchicalRelationshipType.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF)mdClassIF.definesAttribute(CODE);
  }
  
  public void setCode(String value)
  {
    this.setValue(CODE, value);
  }
  
  public String getContact()
  {
    return (String) this.getObjectValue(CONTACT);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeTextDAOIF getContactMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.graph.HierarchicalRelationshipType.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeTextDAOIF)mdClassIF.definesAttribute(CONTACT);
  }
  
  public void setContact(String value)
  {
    this.setValue(CONTACT, value);
  }
  
  public String getDbClassName()
  {
    return (String) this.getObjectValue(DBCLASSNAME);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF getDbClassNameMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.graph.HierarchicalRelationshipType.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF)mdClassIF.definesAttribute(DBCLASSNAME);
  }
  
  public void setDbClassName(String value)
  {
    this.setValue(DBCLASSNAME, value);
  }
  
  public com.runwaysdk.system.metadata.MdEdge getDefinitionEdge()
  {
    if (this.getObjectValue(DEFINITIONEDGE) == null)
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.metadata.MdEdge.get( (String) this.getObjectValue(DEFINITIONEDGE));
    }
  }
  
  public String getDefinitionEdgeOid()
  {
    return (String) this.getObjectValue(DEFINITIONEDGE);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF getDefinitionEdgeMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.graph.HierarchicalRelationshipType.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF)mdClassIF.definesAttribute(DEFINITIONEDGE);
  }
  
  public void setDefinitionEdge(com.runwaysdk.system.metadata.MdEdge value)
  {
    this.setValue(DEFINITIONEDGE, value.getOid());
  }
  
  public void setDefinitionEdgeId(java.lang.String oid)
  {
    this.setValue(DEFINITIONEDGE, oid);
  }
  
  public com.runwaysdk.ComponentIF getDescription()
  {
    return (com.runwaysdk.ComponentIF) this.getObjectValue(DESCRIPTION);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeDAOIF getDescriptionMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.graph.HierarchicalRelationshipType.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeDAOIF)mdClassIF.definesAttribute(DESCRIPTION);
  }
  
  public void setDescription(com.runwaysdk.ComponentIF value)
  {
    this.setValue(DESCRIPTION, value);
  }
  
  public String getDisclaimer()
  {
    return (String) this.getObjectValue(DISCLAIMER);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeTextDAOIF getDisclaimerMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.graph.HierarchicalRelationshipType.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeTextDAOIF)mdClassIF.definesAttribute(DISCLAIMER);
  }
  
  public void setDisclaimer(String value)
  {
    this.setValue(DISCLAIMER, value);
  }
  
  public com.runwaysdk.ComponentIF getDisplayLabel()
  {
    return (com.runwaysdk.ComponentIF) this.getObjectValue(DISPLAYLABEL);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeDAOIF getDisplayLabelMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.graph.HierarchicalRelationshipType.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeDAOIF)mdClassIF.definesAttribute(DISPLAYLABEL);
  }
  
  public void setDisplayLabel(com.runwaysdk.ComponentIF value)
  {
    this.setValue(DISPLAYLABEL, value);
  }
  
  public String getEmail()
  {
    return (String) this.getObjectValue(EMAIL);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeTextDAOIF getEmailMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.graph.HierarchicalRelationshipType.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeTextDAOIF)mdClassIF.definesAttribute(EMAIL);
  }
  
  public void setEmail(String value)
  {
    this.setValue(EMAIL, value);
  }
  
  public com.runwaysdk.system.metadata.MdEdge getObjectEdge()
  {
    if (this.getObjectValue(OBJECTEDGE) == null)
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.metadata.MdEdge.get( (String) this.getObjectValue(OBJECTEDGE));
    }
  }
  
  public String getObjectEdgeOid()
  {
    return (String) this.getObjectValue(OBJECTEDGE);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF getObjectEdgeMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.graph.HierarchicalRelationshipType.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF)mdClassIF.definesAttribute(OBJECTEDGE);
  }
  
  public void setObjectEdge(com.runwaysdk.system.metadata.MdEdge value)
  {
    this.setValue(OBJECTEDGE, value.getOid());
  }
  
  public void setObjectEdgeId(java.lang.String oid)
  {
    this.setValue(OBJECTEDGE, oid);
  }
  
  public String getOid()
  {
    return (String) this.getObjectValue(OID);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeUUIDDAOIF getOidMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.graph.HierarchicalRelationshipType.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeUUIDDAOIF)mdClassIF.definesAttribute(OID);
  }
  
  public net.geoprism.registry.graph.GraphOrganization getOrganization()
  {
    return (net.geoprism.registry.graph.GraphOrganization) this.getObjectValue(ORGANIZATION);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeGraphReferenceDAOIF getOrganizationMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.graph.HierarchicalRelationshipType.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeGraphReferenceDAOIF)mdClassIF.definesAttribute(ORGANIZATION);
  }
  
  public void setOrganization(net.geoprism.registry.graph.GraphOrganization value)
  {
    this.setValue(ORGANIZATION, value);
  }
  
  public String getPhoneNumber()
  {
    return (String) this.getObjectValue(PHONENUMBER);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeTextDAOIF getPhoneNumberMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.graph.HierarchicalRelationshipType.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeTextDAOIF)mdClassIF.definesAttribute(PHONENUMBER);
  }
  
  public void setPhoneNumber(String value)
  {
    this.setValue(PHONENUMBER, value);
  }
  
  public String getProgress()
  {
    return (String) this.getObjectValue(PROGRESS);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeTextDAOIF getProgressMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.graph.HierarchicalRelationshipType.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeTextDAOIF)mdClassIF.definesAttribute(PROGRESS);
  }
  
  public void setProgress(String value)
  {
    this.setValue(PROGRESS, value);
  }
  
  public Long getSeq()
  {
    return (Long) this.getObjectValue(SEQ);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeLongDAOIF getSeqMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.graph.HierarchicalRelationshipType.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeLongDAOIF)mdClassIF.definesAttribute(SEQ);
  }
  
  public void setSeq(Long value)
  {
    this.setValue(SEQ, value);
  }
  
  public String getUseConstraints()
  {
    return (String) this.getObjectValue(USECONSTRAINTS);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeTextDAOIF getUseConstraintsMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.graph.HierarchicalRelationshipType.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeTextDAOIF)mdClassIF.definesAttribute(USECONSTRAINTS);
  }
  
  public void setUseConstraints(String value)
  {
    this.setValue(USECONSTRAINTS, value);
  }
  
  protected String getDeclaredType()
  {
    return CLASS;
  }
  
  public static HierarchicalRelationshipType get(String oid)
  {
    return (HierarchicalRelationshipType) com.runwaysdk.business.graph.VertexObject.get(CLASS, oid);
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
