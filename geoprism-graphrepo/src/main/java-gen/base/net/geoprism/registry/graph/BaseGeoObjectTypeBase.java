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

@com.runwaysdk.business.ClassSignature(hash = 1501997198)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to BaseGeoObjectType.java
 *
 * @author Autogenerated by RunwaySDK
 */
public abstract class BaseGeoObjectTypeBase extends com.runwaysdk.business.graph.VertexObject
{
  public final static String CLASS = "net.geoprism.registry.graph.BaseGeoObjectType";
  public final static java.lang.String CODE = "code";
  public final static java.lang.String OID = "oid";
  public final static java.lang.String SEQ = "seq";
  @SuppressWarnings("unused")
  private static final long serialVersionUID = 1501997198;
  
  public BaseGeoObjectTypeBase()
  {
    super();
  }
  
  public String getCode()
  {
    return (String) this.getObjectValue(CODE);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF getCodeMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.graph.BaseGeoObjectType.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF)mdClassIF.definesAttribute(CODE);
  }
  
  public void setCode(String value)
  {
    this.setValue(CODE, value);
  }
  
  public String getOid()
  {
    return (String) this.getObjectValue(OID);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeUUIDDAOIF getOidMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.graph.BaseGeoObjectType.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeUUIDDAOIF)mdClassIF.definesAttribute(OID);
  }
  
  public Long getSeq()
  {
    return (Long) this.getObjectValue(SEQ);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeLongDAOIF getSeqMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.graph.BaseGeoObjectType.CLASS);
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
  
  public static BaseGeoObjectType get(String oid)
  {
    return (BaseGeoObjectType) com.runwaysdk.business.graph.VertexObject.get(CLASS, oid);
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
