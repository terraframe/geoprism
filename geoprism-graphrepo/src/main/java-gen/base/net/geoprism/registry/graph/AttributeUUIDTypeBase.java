package net.geoprism.registry.graph;

@com.runwaysdk.business.ClassSignature(hash = -235126065)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to AttributeUUIDType.java
 *
 * @author Autogenerated by RunwaySDK
 */
public abstract class AttributeUUIDTypeBase extends net.geoprism.registry.graph.AttributeType
{
  public final static String CLASS = "net.geoprism.registry.graph.AttributeUUIDType";
  @SuppressWarnings("unused")
  private static final long serialVersionUID = -235126065;
  
  public AttributeUUIDTypeBase()
  {
    super();
  }
  
  protected String getDeclaredType()
  {
    return CLASS;
  }
  
  public static AttributeUUIDType get(String oid)
  {
    return (AttributeUUIDType) com.runwaysdk.business.graph.VertexObject.get(CLASS, oid);
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