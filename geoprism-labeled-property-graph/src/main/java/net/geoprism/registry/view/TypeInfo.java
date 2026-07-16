/**
 * Copyright (c) 2023 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Geoprism(tm).
 *
 * Geoprism(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Geoprism(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Geoprism(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
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
