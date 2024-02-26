package net.geoprism.registry.graph;

@com.runwaysdk.business.ClassSignature(hash = 1443567802)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to AttributeLineValue.java
 *
 * @author Autogenerated by RunwaySDK
 */
public abstract class AttributeLineValueBase extends net.geoprism.registry.graph.AttributeGeometryValue
{
  public final static String CLASS = "net.geoprism.registry.graph.AttributeLineValue";
  public final static java.lang.String VALUE = "value";
  @SuppressWarnings("unused")
  private static final long serialVersionUID = 1443567802;
  
  public AttributeLineValueBase()
  {
    super();
  }
  
  public org.locationtech.jts.geom.MultiLineString getValue()
  {
    return (org.locationtech.jts.geom.MultiLineString) this.getObjectValue(VALUE);
  }
  
  public static com.runwaysdk.gis.dataaccess.MdAttributeMultiLineStringDAOIF getValueMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.graph.AttributeLineValue.CLASS);
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
  
  public static AttributeLineValue get(String oid)
  {
    return (AttributeLineValue) com.runwaysdk.business.graph.VertexObject.get(CLASS, oid);
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