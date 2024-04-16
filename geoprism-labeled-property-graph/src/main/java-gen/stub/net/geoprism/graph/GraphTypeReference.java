package net.geoprism.graph;

public class GraphTypeReference
{
  public String typeCode;
  public String code;
  
  public GraphTypeReference(String typeCode, String code)
  {
    this.typeCode = typeCode;
    this.code = code;
  }
  
  public static GraphTypeReference build(String[] codes)
  {
    return new GraphTypeReference(codes[0], codes[1]);
  }
}