package net.geoprism.registry.graph;

@com.runwaysdk.business.ClassSignature(hash = 2032893524)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to AttributeTermType.java
 *
 * @author Autogenerated by RunwaySDK
 */
public abstract class AttributeTermTypeBase extends net.geoprism.registry.graph.AttributeType
{
  public final static String CLASS = "net.geoprism.registry.graph.AttributeTermType";
  public final static java.lang.String ROOTTERM = "rootTerm";
  public final static java.lang.String VALUEVERTEX = "valueVertex";
  @SuppressWarnings("unused")
  private static final long serialVersionUID = 2032893524;
  
  public AttributeTermTypeBase()
  {
    super();
  }
  
  public net.geoprism.ontology.Classifier getRootTerm()
  {
    if (this.getObjectValue(ROOTTERM) == null)
    {
      return null;
    }
    else
    {
      return net.geoprism.ontology.Classifier.get( (String) this.getObjectValue(ROOTTERM));
    }
  }
  
  public String getRootTermOid()
  {
    return (String) this.getObjectValue(ROOTTERM);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF getRootTermMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.graph.AttributeTermType.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF)mdClassIF.definesAttribute(ROOTTERM);
  }
  
  public void setRootTerm(net.geoprism.ontology.Classifier value)
  {
    this.setValue(ROOTTERM, value.getOid());
  }
  
  public void setRootTermId(java.lang.String oid)
  {
    this.setValue(ROOTTERM, oid);
  }
  
  public com.runwaysdk.system.metadata.MdVertex getValueVertex()
  {
    if (this.getObjectValue(VALUEVERTEX) == null)
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.metadata.MdVertex.get( (String) this.getObjectValue(VALUEVERTEX));
    }
  }
  
  public String getValueVertexOid()
  {
    return (String) this.getObjectValue(VALUEVERTEX);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF getValueVertexMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.graph.AttributeTermType.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF)mdClassIF.definesAttribute(VALUEVERTEX);
  }
  
  public void setValueVertex(com.runwaysdk.system.metadata.MdVertex value)
  {
    this.setValue(VALUEVERTEX, value.getOid());
  }
  
  public void setValueVertexId(java.lang.String oid)
  {
    this.setValue(VALUEVERTEX, oid);
  }
  
  protected String getDeclaredType()
  {
    return CLASS;
  }
  
  public static AttributeTermType get(String oid)
  {
    return (AttributeTermType) com.runwaysdk.business.graph.VertexObject.get(CLASS, oid);
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
