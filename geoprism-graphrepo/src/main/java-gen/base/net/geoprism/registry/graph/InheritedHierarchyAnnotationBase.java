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

@com.runwaysdk.business.ClassSignature(hash = -789066173)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to InheritedHierarchyAnnotation.java
 *
 * @author Autogenerated by RunwaySDK
 */
public abstract class InheritedHierarchyAnnotationBase extends com.runwaysdk.business.graph.VertexObject
{
  public final static String CLASS = "net.geoprism.registry.graph.InheritedHierarchyAnnotation";
  public final static java.lang.String FORHIERARCHY = "forHierarchy";
  public final static java.lang.String GEOOBJECTTYPE = "geoObjectType";
  public final static java.lang.String INHERITEDHIERARCHY = "inheritedHierarchy";
  public final static java.lang.String OID = "oid";
  public final static java.lang.String SEQ = "seq";
  @SuppressWarnings("unused")
  private static final long serialVersionUID = -789066173;
  
  public InheritedHierarchyAnnotationBase()
  {
    super();
  }
  
  public net.geoprism.registry.graph.HierarchicalRelationshipType getForHierarchy()
  {
    return (net.geoprism.registry.graph.HierarchicalRelationshipType) this.getObjectValue(FORHIERARCHY);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeGraphReferenceDAOIF getForHierarchyMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.graph.InheritedHierarchyAnnotation.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeGraphReferenceDAOIF)mdClassIF.definesAttribute(FORHIERARCHY);
  }
  
  public void setForHierarchy(net.geoprism.registry.graph.HierarchicalRelationshipType value)
  {
    this.setValue(FORHIERARCHY, value);
  }
  
  public net.geoprism.registry.graph.GeoObjectType getGeoObjectType()
  {
    return (net.geoprism.registry.graph.GeoObjectType) this.getObjectValue(GEOOBJECTTYPE);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeGraphReferenceDAOIF getGeoObjectTypeMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.graph.InheritedHierarchyAnnotation.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeGraphReferenceDAOIF)mdClassIF.definesAttribute(GEOOBJECTTYPE);
  }
  
  public void setGeoObjectType(net.geoprism.registry.graph.GeoObjectType value)
  {
    this.setValue(GEOOBJECTTYPE, value);
  }
  
  public net.geoprism.registry.graph.HierarchicalRelationshipType getInheritedHierarchy()
  {
    return (net.geoprism.registry.graph.HierarchicalRelationshipType) this.getObjectValue(INHERITEDHIERARCHY);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeGraphReferenceDAOIF getInheritedHierarchyMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.graph.InheritedHierarchyAnnotation.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeGraphReferenceDAOIF)mdClassIF.definesAttribute(INHERITEDHIERARCHY);
  }
  
  public void setInheritedHierarchy(net.geoprism.registry.graph.HierarchicalRelationshipType value)
  {
    this.setValue(INHERITEDHIERARCHY, value);
  }
  
  public String getOid()
  {
    return (String) this.getObjectValue(OID);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeUUIDDAOIF getOidMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.graph.InheritedHierarchyAnnotation.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeUUIDDAOIF)mdClassIF.definesAttribute(OID);
  }
  
  public Long getSeq()
  {
    return (Long) this.getObjectValue(SEQ);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeLongDAOIF getSeqMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.graph.InheritedHierarchyAnnotation.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeLongDAOIF)mdClassIF.definesAttribute(SEQ);
  }
  
  public void setSeq(Long value)
  {
    this.setValue(SEQ, value);
  }
  
  protected String getDeclaredType()
  {
    return CLASS;
  }
  
  public static InheritedHierarchyAnnotation get(String oid)
  {
    return (InheritedHierarchyAnnotation) com.runwaysdk.business.graph.VertexObject.get(CLASS, oid);
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