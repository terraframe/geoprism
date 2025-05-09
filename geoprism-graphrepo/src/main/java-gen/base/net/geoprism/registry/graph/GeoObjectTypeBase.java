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

@com.runwaysdk.business.ClassSignature(hash = -1856327530)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to GeoObjectType.java
 *
 * @author Autogenerated by RunwaySDK
 */
public abstract class GeoObjectTypeBase extends net.geoprism.registry.graph.BaseGeoObjectType
{
  public final static String CLASS = "net.geoprism.registry.graph.GeoObjectType";
  public final static java.lang.String DBCLASSNAME = "dbClassName";
  public final static java.lang.String DESCRIPTION = "description";
  public final static java.lang.String DISPLAYLABEL = "displayLabel";
  public final static java.lang.String GEOMETRYTYPE = "geometryType";
  public final static java.lang.String ISABSTRACT = "isAbstract";
  public final static java.lang.String ISGEOMETRYEDITABLE = "isGeometryEditable";
  public final static java.lang.String ISPRIVATE = "isPrivate";
  public final static java.lang.String MDVERTEX = "mdVertex";
  public final static java.lang.String ORGANIZATION = "organization";
  public final static java.lang.String ROOTTERM = "rootTerm";
  public final static java.lang.String SUPERTYPE = "superType";
  @SuppressWarnings("unused")
  private static final long serialVersionUID = -1856327530;
  
  public GeoObjectTypeBase()
  {
    super();
  }
  
  public String getDbClassName()
  {
    return (String) this.getObjectValue(DBCLASSNAME);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF getDbClassNameMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.graph.GeoObjectType.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF)mdClassIF.definesAttribute(DBCLASSNAME);
  }
  
  public void setDbClassName(String value)
  {
    this.setValue(DBCLASSNAME, value);
  }
  
  public com.runwaysdk.ComponentIF getDescription()
  {
    return (com.runwaysdk.ComponentIF) this.getObjectValue(DESCRIPTION);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeDAOIF getDescriptionMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.graph.GeoObjectType.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeDAOIF)mdClassIF.definesAttribute(DESCRIPTION);
  }
  
  public void setDescription(com.runwaysdk.ComponentIF value)
  {
    this.setValue(DESCRIPTION, value);
  }
  
  public com.runwaysdk.ComponentIF getDisplayLabel()
  {
    return (com.runwaysdk.ComponentIF) this.getObjectValue(DISPLAYLABEL);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeDAOIF getDisplayLabelMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.graph.GeoObjectType.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeDAOIF)mdClassIF.definesAttribute(DISPLAYLABEL);
  }
  
  public void setDisplayLabel(com.runwaysdk.ComponentIF value)
  {
    this.setValue(DISPLAYLABEL, value);
  }
  
  public String getGeometryType()
  {
    return (String) this.getObjectValue(GEOMETRYTYPE);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF getGeometryTypeMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.graph.GeoObjectType.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF)mdClassIF.definesAttribute(GEOMETRYTYPE);
  }
  
  public void setGeometryType(String value)
  {
    this.setValue(GEOMETRYTYPE, value);
  }
  
  public Boolean getIsAbstract()
  {
    return (Boolean) this.getObjectValue(ISABSTRACT);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeBooleanDAOIF getIsAbstractMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.graph.GeoObjectType.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeBooleanDAOIF)mdClassIF.definesAttribute(ISABSTRACT);
  }
  
  public void setIsAbstract(Boolean value)
  {
    this.setValue(ISABSTRACT, value);
  }
  
  public Boolean getIsGeometryEditable()
  {
    return (Boolean) this.getObjectValue(ISGEOMETRYEDITABLE);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeBooleanDAOIF getIsGeometryEditableMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.graph.GeoObjectType.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeBooleanDAOIF)mdClassIF.definesAttribute(ISGEOMETRYEDITABLE);
  }
  
  public void setIsGeometryEditable(Boolean value)
  {
    this.setValue(ISGEOMETRYEDITABLE, value);
  }
  
  public Boolean getIsPrivate()
  {
    return (Boolean) this.getObjectValue(ISPRIVATE);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeBooleanDAOIF getIsPrivateMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.graph.GeoObjectType.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeBooleanDAOIF)mdClassIF.definesAttribute(ISPRIVATE);
  }
  
  public void setIsPrivate(Boolean value)
  {
    this.setValue(ISPRIVATE, value);
  }
  
  public com.runwaysdk.system.metadata.MdVertex getMdVertex()
  {
    if (this.getObjectValue(MDVERTEX) == null)
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.metadata.MdVertex.get( (String) this.getObjectValue(MDVERTEX));
    }
  }
  
  public String getMdVertexOid()
  {
    return (String) this.getObjectValue(MDVERTEX);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF getMdVertexMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.graph.GeoObjectType.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF)mdClassIF.definesAttribute(MDVERTEX);
  }
  
  public void setMdVertex(com.runwaysdk.system.metadata.MdVertex value)
  {
    this.setValue(MDVERTEX, value.getOid());
  }
  
  public void setMdVertexId(java.lang.String oid)
  {
    this.setValue(MDVERTEX, oid);
  }
  
  public net.geoprism.registry.graph.GraphOrganization getOrganization()
  {
    return (net.geoprism.registry.graph.GraphOrganization) this.getObjectValue(ORGANIZATION);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeGraphReferenceDAOIF getOrganizationMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.graph.GeoObjectType.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeGraphReferenceDAOIF)mdClassIF.definesAttribute(ORGANIZATION);
  }
  
  public void setOrganization(net.geoprism.registry.graph.GraphOrganization value)
  {
    this.setValue(ORGANIZATION, value);
  }
  
  public net.geoprism.ontology.Classifier getRootTerm()
  {
    if (this.getObjectValue(ROOTTERM) == null)
    {
      return null;
    }
    else
    {
      return net.geoprism.ontology.Classifier.get( (String) this.getObjectValue(ROOTTERM));
    }
  }
  
  public String getRootTermOid()
  {
    return (String) this.getObjectValue(ROOTTERM);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF getRootTermMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.graph.GeoObjectType.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF)mdClassIF.definesAttribute(ROOTTERM);
  }
  
  public void setRootTerm(net.geoprism.ontology.Classifier value)
  {
    this.setValue(ROOTTERM, value.getOid());
  }
  
  public void setRootTermId(java.lang.String oid)
  {
    this.setValue(ROOTTERM, oid);
  }
  
  public net.geoprism.registry.graph.GeoObjectType getSuperType()
  {
    return (net.geoprism.registry.graph.GeoObjectType) this.getObjectValue(SUPERTYPE);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeGraphReferenceDAOIF getSuperTypeMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.graph.GeoObjectType.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeGraphReferenceDAOIF)mdClassIF.definesAttribute(SUPERTYPE);
  }
  
  public void setSuperType(net.geoprism.registry.graph.GeoObjectType value)
  {
    this.setValue(SUPERTYPE, value);
  }
  
  protected String getDeclaredType()
  {
    return CLASS;
  }
  
  public static GeoObjectType get(String oid)
  {
    return (GeoObjectType) com.runwaysdk.business.graph.VertexObject.get(CLASS, oid);
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
