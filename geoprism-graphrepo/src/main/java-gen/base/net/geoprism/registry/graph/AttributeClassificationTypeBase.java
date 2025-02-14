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

@com.runwaysdk.business.ClassSignature(hash = -133082066)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to AttributeClassificationType.java
 *
 * @author Autogenerated by RunwaySDK
 */
public abstract class AttributeClassificationTypeBase extends net.geoprism.registry.graph.AttributeType
{
  public final static String CLASS = "net.geoprism.registry.graph.AttributeClassificationType";
  public final static java.lang.String MDCLASSIFICATION = "mdClassification";
  public final static java.lang.String ROOTTERM = "rootTerm";
  public final static java.lang.String VALUEVERTEX = "valueVertex";
  @SuppressWarnings("unused")
  private static final long serialVersionUID = -133082066;
  
  public AttributeClassificationTypeBase()
  {
    super();
  }
  
  public com.runwaysdk.system.metadata.MdClassification getMdClassification()
  {
    if (this.getObjectValue(MDCLASSIFICATION) == null)
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.metadata.MdClassification.get( (String) this.getObjectValue(MDCLASSIFICATION));
    }
  }
  
  public String getMdClassificationOid()
  {
    return (String) this.getObjectValue(MDCLASSIFICATION);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF getMdClassificationMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.graph.AttributeClassificationType.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF)mdClassIF.definesAttribute(MDCLASSIFICATION);
  }
  
  public void setMdClassification(com.runwaysdk.system.metadata.MdClassification value)
  {
    this.setValue(MDCLASSIFICATION, value.getOid());
  }
  
  public void setMdClassificationId(java.lang.String oid)
  {
    this.setValue(MDCLASSIFICATION, oid);
  }
  
  public String getRootTerm()
  {
    return (String) this.getObjectValue(ROOTTERM);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeTextDAOIF getRootTermMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.graph.AttributeClassificationType.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeTextDAOIF)mdClassIF.definesAttribute(ROOTTERM);
  }
  
  public void setRootTerm(String value)
  {
    this.setValue(ROOTTERM, value);
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
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.graph.AttributeClassificationType.CLASS);
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
  
  public static AttributeClassificationType get(String oid)
  {
    return (AttributeClassificationType) com.runwaysdk.business.graph.VertexObject.get(CLASS, oid);
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
