package net.geoprism.registry.view;

public class TypeInfo implements Comparable<TypeInfo>
{

  private TypeClass typeClass;

  private String    typeCode;

  public TypeInfo()
  {
    super();
  }

  public TypeInfo(TypeClass typeClass, String typeCode)
  {
    super();
    this.typeClass = typeClass;
    this.typeCode = typeCode;
  }

  public TypeClass getTypeClass()
  {
    return typeClass;
  }

  public void setTypeClass(TypeClass typeClass)
  {
    this.typeClass = typeClass;
  }

  public String getTypeCode()
  {
    return typeCode;
  }

  public void setTypeCode(String typeCode)
  {
    this.typeCode = typeCode;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (obj instanceof TypeInfo)
    {
      TypeInfo type = (TypeInfo) obj;

      return this.typeClass.equals(type.getTypeClass()) && this.typeCode.equals(type.getTypeCode());
    }

    return false;
  }

  @Override
  public int compareTo(TypeInfo arg0)
  {
    int compareTo = this.typeClass.compareTo(arg0.typeClass);

    if (compareTo == 0)
    {
      return this.typeCode.compareTo(arg0.typeCode);
    }

    return compareTo;
  }

  public static TypeInfo build(String code, TypeClass type)
  {
    return new TypeInfo(type, code);
  }

  public static TypeInfo build(String code, String typeClassCode)
  {
    TypeClass typeClass = TypeClass.getByCode(typeClassCode);

    return new TypeInfo(typeClass, code);
  }

}
