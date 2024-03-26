package net.geoprism.registry.graph;

@com.runwaysdk.business.ClassSignature(hash = 61027873)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to AttributeMultiLineValue.java
 *
 * @author Autogenerated by RunwaySDK
 */
public abstract class AttributeMultiLineValueBase extends net.geoprism.registry.graph.AttributeGeometryValue
{
  public final static String CLASS = "net.geoprism.registry.graph.AttributeMultiLineValue";
  public final static java.lang.String VALUE = "value";
  @SuppressWarnings("unused")
  private static final long serialVersionUID = 61027873;
  
  public AttributeMultiLineValueBase()
  {
    super();
  }
  
  public org.locationtech.jts.geom.MultiLineString getValue()
  {
    return (org.locationtech.jts.geom.MultiLineString) this.getObjectValue(VALUE);
  }
  
  public static com.runwaysdk.gis.dataaccess.MdAttributeMultiLineStringDAOIF getValueMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.graph.AttributeMultiLineValue.CLASS);
    return (com.runwaysdk.gis.dataaccess.MdAttributeMultiLineStringDAOIF)mdClassIF.definesAttribute(VALUE);
  }
  
  public void setValue(org.locationtech.jts.geom.MultiLineString value)
  {
    this.setValue(VALUE, value);
  }
  
  protected String getDeclaredType()
  {
    return CLASS;
  }
  
  public static AttributeMultiLineValue get(String oid)
  {
    return (AttributeMultiLineValue) com.runwaysdk.business.graph.VertexObject.get(CLASS, oid);
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