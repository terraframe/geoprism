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
package net.geoprism.registry.graph;

@com.runwaysdk.business.ClassSignature(hash = -1654122271)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to AttributeBasicValue.java
 *
 * @author Autogenerated by RunwaySDK
 */
public abstract class AttributeBasicValueBase extends net.geoprism.registry.graph.AttributeValue
{
  public final static String CLASS = "net.geoprism.registry.graph.AttributeBasicValue";
  @SuppressWarnings("unused")
  private static final long serialVersionUID = -1654122271;
  
  public AttributeBasicValueBase()
  {
    super();
  }
  
  protected String getDeclaredType()
  {
    return CLASS;
  }
  
  public com.runwaysdk.business.graph.EdgeObject addHasValueParent(net.geoprism.registry.graph.GeoVertex geoVertex)
  {
    return super.addParent(geoVertex, "net.geoprism.registry.graph.HasValue");
  }
  
  public void removeHasValueParent(net.geoprism.registry.graph.GeoVertex geoVertex)
  {
    super.removeParent(geoVertex, "net.geoprism.registry.graph.HasValue");
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<net.geoprism.registry.graph.GeoVertex> getHasValueParentGeoVertexs()
  {
    return super.getParents("net.geoprism.registry.graph.HasValue", net.geoprism.registry.graph.GeoVertex.class);
  }
  
  public static AttributeBasicValue get(String oid)
  {
    return (AttributeBasicValue) com.runwaysdk.business.graph.VertexObject.get(CLASS, oid);
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
