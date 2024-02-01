package net.geoprism.registry.graph;

@com.runwaysdk.business.ClassSignature(hash = -1845733668)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to AttributeDateType.java
 *
 * @author Autogenerated by RunwaySDK
 */
public abstract class AttributeDateTypeBase extends net.geoprism.registry.graph.AttributeType
{
  public final static String CLASS = "net.geoprism.registry.graph.AttributeDateType";
  @SuppressWarnings("unused")
  private static final long serialVersionUID = -1845733668;
  
  public AttributeDateTypeBase()
  {
    super();
  }
  
  protected String getDeclaredType()
  {
    return CLASS;
  }
  
  public static AttributeDateType get(String oid)
  {
    return (AttributeDateType) com.runwaysdk.business.graph.VertexObject.get(CLASS, oid);
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
