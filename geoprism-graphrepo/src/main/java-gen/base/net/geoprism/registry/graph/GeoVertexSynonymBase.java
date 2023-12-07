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

@com.runwaysdk.business.ClassSignature(hash = -1205350658)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to GeoVertexSynonym.java
 *
 * @author Autogenerated by RunwaySDK
 */
public abstract class GeoVertexSynonymBase extends com.runwaysdk.business.graph.VertexObject
{
  public final static String CLASS = "net.geoprism.registry.graph.GeoVertexSynonym";
  public static final java.lang.String LABEL = "label";
  public static final java.lang.String OID = "oid";
  public static final java.lang.String SEQ = "seq";
  private static final long serialVersionUID = -1205350658;
  
  public GeoVertexSynonymBase()
  {
    super();
  }
  
  public String getLabel()
  {
    return (String) this.getObjectValue(LABEL);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF getLabelMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.graph.GeoVertexSynonym.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF)mdClassIF.definesAttribute(LABEL);
  }
  
  public void setLabel(String value)
  {
    this.setValue(LABEL, value);
  }
  
  public String getOid()
  {
    return (String) this.getObjectValue(OID);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeUUIDDAOIF getOidMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.graph.GeoVertexSynonym.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeUUIDDAOIF)mdClassIF.definesAttribute(OID);
  }
  
  public Long getSeq()
  {
    return (Long) this.getObjectValue(SEQ);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeLongDAOIF getSeqMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.graph.GeoVertexSynonym.CLASS);
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
  
  public com.runwaysdk.business.graph.EdgeObject addGeoVertexHasSynonymParent(net.geoprism.registry.graph.GeoVertex geoVertex)
  {
    return super.addParent(geoVertex, "net.geoprism.registry.graph.GeoVertexHasSynonym");
  }
  
  public void removeGeoVertexHasSynonymParent(net.geoprism.registry.graph.GeoVertex geoVertex)
  {
    super.removeParent(geoVertex, "net.geoprism.registry.graph.GeoVertexHasSynonym");
  }
  
  
  public java.util.List<net.geoprism.registry.graph.GeoVertex> getGeoVertexHasSynonymParentGeoVertexs()
  {
    return super.getParents("net.geoprism.registry.graph.GeoVertexHasSynonym", net.geoprism.registry.graph.GeoVertex.class);
  }
  
  public static GeoVertexSynonym get(String oid)
  {
    return (GeoVertexSynonym) com.runwaysdk.business.graph.VertexObject.get(CLASS, oid);
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
