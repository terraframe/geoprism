/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
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
package net.geoprism.dashboard;

@com.runwaysdk.business.ClassSignature(hash = -50141811)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to DashboardStyle.java
 *
 * @author Autogenerated by RunwaySDK
 */
public abstract class DashboardStyleBase extends com.runwaysdk.business.Business 
{
  public final static String CLASS = "net.geoprism.dashboard.DashboardStyle";
  public static java.lang.String BASICPOINTSIZE = "basicPointSize";
  public static java.lang.String CREATEDATE = "createDate";
  public static java.lang.String CREATEDBY = "createdBy";
  public static java.lang.String ENABLELABEL = "enableLabel";
  public static java.lang.String ENABLEVALUE = "enableValue";
  public static java.lang.String ENTITYDOMAIN = "entityDomain";
  public static java.lang.String OID = "oid";
  public static java.lang.String KEYNAME = "keyName";
  public static java.lang.String LABELCOLOR = "labelColor";
  public static java.lang.String LABELFONT = "labelFont";
  public static java.lang.String LABELHALO = "labelHalo";
  public static java.lang.String LABELHALOWIDTH = "labelHaloWidth";
  public static java.lang.String LABELSIZE = "labelSize";
  public static java.lang.String LASTUPDATEDATE = "lastUpdateDate";
  public static java.lang.String LASTUPDATEDBY = "lastUpdatedBy";
  public static java.lang.String LINEOPACITY = "lineOpacity";
  public static java.lang.String LINESTROKE = "lineStroke";
  public static java.lang.String LINESTROKECAP = "lineStrokeCap";
  public static java.lang.String LINESTROKEWIDTH = "lineStrokeWidth";
  public static java.lang.String LOCKEDBY = "lockedBy";
  public static java.lang.String NAME = "name";
  public static java.lang.String OWNER = "owner";
  public static java.lang.String POINTFILL = "pointFill";
  public static java.lang.String POINTOPACITY = "pointOpacity";
  public static java.lang.String POINTROTATION = "pointRotation";
  public static java.lang.String POINTSTROKE = "pointStroke";
  public static java.lang.String POINTSTROKEOPACITY = "pointStrokeOpacity";
  public static java.lang.String POINTSTROKEWIDTH = "pointStrokeWidth";
  public static java.lang.String POINTWELLKNOWNNAME = "pointWellKnownName";
  public static java.lang.String POLYGONFILL = "polygonFill";
  public static java.lang.String POLYGONFILLOPACITY = "polygonFillOpacity";
  public static java.lang.String POLYGONSTROKE = "polygonStroke";
  public static java.lang.String POLYGONSTROKEOPACITY = "polygonStrokeOpacity";
  public static java.lang.String POLYGONSTROKEWIDTH = "polygonStrokeWidth";
  public static java.lang.String SEQ = "seq";
  public static java.lang.String SITEMASTER = "siteMaster";
  public static java.lang.String TYPE = "type";
  public static java.lang.String VALUECOLOR = "valueColor";
  public static java.lang.String VALUEFONT = "valueFont";
  public static java.lang.String VALUEHALO = "valueHalo";
  public static java.lang.String VALUEHALOWIDTH = "valueHaloWidth";
  public static java.lang.String VALUESIZE = "valueSize";
  private static final long serialVersionUID = -50141811;
  
  public DashboardStyleBase()
  {
    super();
  }
  
  public Integer getBasicPointSize()
  {
    return com.runwaysdk.constants.MdAttributeIntegerUtil.getTypeSafeValue(getValue(BASICPOINTSIZE));
  }
  
  public void validateBasicPointSize()
  {
    this.validateAttribute(BASICPOINTSIZE);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeIntegerDAOIF getBasicPointSizeMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.dashboard.DashboardStyle.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeIntegerDAOIF)mdClassIF.definesAttribute(BASICPOINTSIZE);
  }
  
  public void setBasicPointSize(Integer value)
  {
    if(value == null)
    {
      setValue(BASICPOINTSIZE, "");
    }
    else
    {
      setValue(BASICPOINTSIZE, java.lang.Integer.toString(value));
    }
  }
  
  public java.util.Date getCreateDate()
  {
    return com.runwaysdk.constants.MdAttributeDateTimeUtil.getTypeSafeValue(getValue(CREATEDATE));
  }
  
  public void validateCreateDate()
  {
    this.validateAttribute(CREATEDATE);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeDateTimeDAOIF getCreateDateMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.dashboard.DashboardStyle.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeDateTimeDAOIF)mdClassIF.definesAttribute(CREATEDATE);
  }
  
  public com.runwaysdk.system.SingleActor getCreatedBy()
  {
    if (getValue(CREATEDBY).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.SingleActor.get(getValue(CREATEDBY));
    }
  }
  
  public String getCreatedById()
  {
    return getValue(CREATEDBY);
  }
  
  public void validateCreatedBy()
  {
    this.validateAttribute(CREATEDBY);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF getCreatedByMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.dashboard.DashboardStyle.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF)mdClassIF.definesAttribute(CREATEDBY);
  }
  
  public Boolean getEnableLabel()
  {
    return com.runwaysdk.constants.MdAttributeBooleanUtil.getTypeSafeValue(getValue(ENABLELABEL));
  }
  
  public void validateEnableLabel()
  {
    this.validateAttribute(ENABLELABEL);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeBooleanDAOIF getEnableLabelMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.dashboard.DashboardStyle.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeBooleanDAOIF)mdClassIF.definesAttribute(ENABLELABEL);
  }
  
  public void setEnableLabel(Boolean value)
  {
    if(value == null)
    {
      setValue(ENABLELABEL, "");
    }
    else
    {
      setValue(ENABLELABEL, java.lang.Boolean.toString(value));
    }
  }
  
  public Boolean getEnableValue()
  {
    return com.runwaysdk.constants.MdAttributeBooleanUtil.getTypeSafeValue(getValue(ENABLEVALUE));
  }
  
  public void validateEnableValue()
  {
    this.validateAttribute(ENABLEVALUE);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeBooleanDAOIF getEnableValueMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.dashboard.DashboardStyle.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeBooleanDAOIF)mdClassIF.definesAttribute(ENABLEVALUE);
  }
  
  public void setEnableValue(Boolean value)
  {
    if(value == null)
    {
      setValue(ENABLEVALUE, "");
    }
    else
    {
      setValue(ENABLEVALUE, java.lang.Boolean.toString(value));
    }
  }
  
  public com.runwaysdk.system.metadata.MdDomain getEntityDomain()
  {
    if (getValue(ENTITYDOMAIN).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.metadata.MdDomain.get(getValue(ENTITYDOMAIN));
    }
  }
  
  public String getEntityDomainId()
  {
    return getValue(ENTITYDOMAIN);
  }
  
  public void validateEntityDomain()
  {
    this.validateAttribute(ENTITYDOMAIN);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF getEntityDomainMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.dashboard.DashboardStyle.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF)mdClassIF.definesAttribute(ENTITYDOMAIN);
  }
  
  public void setEntityDomain(com.runwaysdk.system.metadata.MdDomain value)
  {
    if(value == null)
    {
      setValue(ENTITYDOMAIN, "");
    }
    else
    {
      setValue(ENTITYDOMAIN, value.getOid());
    }
  }
  
  public String getOid()
  {
    return getValue(OID);
  }
  
  public void validateId()
  {
    this.validateAttribute(OID);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF getOidMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.dashboard.DashboardStyle.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF)mdClassIF.definesAttribute(OID);
  }
  
  public String getKeyName()
  {
    return getValue(KEYNAME);
  }
  
  public void validateKeyName()
  {
    this.validateAttribute(KEYNAME);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF getKeyNameMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.dashboard.DashboardStyle.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF)mdClassIF.definesAttribute(KEYNAME);
  }
  
  public void setKeyName(String value)
  {
    if(value == null)
    {
      setValue(KEYNAME, "");
    }
    else
    {
      setValue(KEYNAME, value);
    }
  }
  
  public String getLabelColor()
  {
    return getValue(LABELCOLOR);
  }
  
  public void validateLabelColor()
  {
    this.validateAttribute(LABELCOLOR);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF getLabelColorMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.dashboard.DashboardStyle.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF)mdClassIF.definesAttribute(LABELCOLOR);
  }
  
  public void setLabelColor(String value)
  {
    if(value == null)
    {
      setValue(LABELCOLOR, "");
    }
    else
    {
      setValue(LABELCOLOR, value);
    }
  }
  
  public String getLabelFont()
  {
    return getValue(LABELFONT);
  }
  
  public void validateLabelFont()
  {
    this.validateAttribute(LABELFONT);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF getLabelFontMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.dashboard.DashboardStyle.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF)mdClassIF.definesAttribute(LABELFONT);
  }
  
  public void setLabelFont(String value)
  {
    if(value == null)
    {
      setValue(LABELFONT, "");
    }
    else
    {
      setValue(LABELFONT, value);
    }
  }
  
  public String getLabelHalo()
  {
    return getValue(LABELHALO);
  }
  
  public void validateLabelHalo()
  {
    this.validateAttribute(LABELHALO);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF getLabelHaloMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.dashboard.DashboardStyle.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF)mdClassIF.definesAttribute(LABELHALO);
  }
  
  public void setLabelHalo(String value)
  {
    if(value == null)
    {
      setValue(LABELHALO, "");
    }
    else
    {
      setValue(LABELHALO, value);
    }
  }
  
  public Integer getLabelHaloWidth()
  {
    return com.runwaysdk.constants.MdAttributeIntegerUtil.getTypeSafeValue(getValue(LABELHALOWIDTH));
  }
  
  public void validateLabelHaloWidth()
  {
    this.validateAttribute(LABELHALOWIDTH);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeIntegerDAOIF getLabelHaloWidthMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.dashboard.DashboardStyle.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeIntegerDAOIF)mdClassIF.definesAttribute(LABELHALOWIDTH);
  }
  
  public void setLabelHaloWidth(Integer value)
  {
    if(value == null)
    {
      setValue(LABELHALOWIDTH, "");
    }
    else
    {
      setValue(LABELHALOWIDTH, java.lang.Integer.toString(value));
    }
  }
  
  public Integer getLabelSize()
  {
    return com.runwaysdk.constants.MdAttributeIntegerUtil.getTypeSafeValue(getValue(LABELSIZE));
  }
  
  public void validateLabelSize()
  {
    this.validateAttribute(LABELSIZE);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeIntegerDAOIF getLabelSizeMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.dashboard.DashboardStyle.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeIntegerDAOIF)mdClassIF.definesAttribute(LABELSIZE);
  }
  
  public void setLabelSize(Integer value)
  {
    if(value == null)
    {
      setValue(LABELSIZE, "");
    }
    else
    {
      setValue(LABELSIZE, java.lang.Integer.toString(value));
    }
  }
  
  public java.util.Date getLastUpdateDate()
  {
    return com.runwaysdk.constants.MdAttributeDateTimeUtil.getTypeSafeValue(getValue(LASTUPDATEDATE));
  }
  
  public void validateLastUpdateDate()
  {
    this.validateAttribute(LASTUPDATEDATE);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeDateTimeDAOIF getLastUpdateDateMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.dashboard.DashboardStyle.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeDateTimeDAOIF)mdClassIF.definesAttribute(LASTUPDATEDATE);
  }
  
  public com.runwaysdk.system.SingleActor getLastUpdatedBy()
  {
    if (getValue(LASTUPDATEDBY).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.SingleActor.get(getValue(LASTUPDATEDBY));
    }
  }
  
  public String getLastUpdatedById()
  {
    return getValue(LASTUPDATEDBY);
  }
  
  public void validateLastUpdatedBy()
  {
    this.validateAttribute(LASTUPDATEDBY);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF getLastUpdatedByMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.dashboard.DashboardStyle.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF)mdClassIF.definesAttribute(LASTUPDATEDBY);
  }
  
  public Double getLineOpacity()
  {
    return com.runwaysdk.constants.MdAttributeDoubleUtil.getTypeSafeValue(getValue(LINEOPACITY));
  }
  
  public void validateLineOpacity()
  {
    this.validateAttribute(LINEOPACITY);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeDoubleDAOIF getLineOpacityMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.dashboard.DashboardStyle.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeDoubleDAOIF)mdClassIF.definesAttribute(LINEOPACITY);
  }
  
  public void setLineOpacity(Double value)
  {
    if(value == null)
    {
      setValue(LINEOPACITY, "");
    }
    else
    {
      setValue(LINEOPACITY, java.lang.Double.toString(value));
    }
  }
  
  public String getLineStroke()
  {
    return getValue(LINESTROKE);
  }
  
  public void validateLineStroke()
  {
    this.validateAttribute(LINESTROKE);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF getLineStrokeMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.dashboard.DashboardStyle.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF)mdClassIF.definesAttribute(LINESTROKE);
  }
  
  public void setLineStroke(String value)
  {
    if(value == null)
    {
      setValue(LINESTROKE, "");
    }
    else
    {
      setValue(LINESTROKE, value);
    }
  }
  
  public String getLineStrokeCap()
  {
    return getValue(LINESTROKECAP);
  }
  
  public void validateLineStrokeCap()
  {
    this.validateAttribute(LINESTROKECAP);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF getLineStrokeCapMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.dashboard.DashboardStyle.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF)mdClassIF.definesAttribute(LINESTROKECAP);
  }
  
  public void setLineStrokeCap(String value)
  {
    if(value == null)
    {
      setValue(LINESTROKECAP, "");
    }
    else
    {
      setValue(LINESTROKECAP, value);
    }
  }
  
  public Integer getLineStrokeWidth()
  {
    return com.runwaysdk.constants.MdAttributeIntegerUtil.getTypeSafeValue(getValue(LINESTROKEWIDTH));
  }
  
  public void validateLineStrokeWidth()
  {
    this.validateAttribute(LINESTROKEWIDTH);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeIntegerDAOIF getLineStrokeWidthMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.dashboard.DashboardStyle.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeIntegerDAOIF)mdClassIF.definesAttribute(LINESTROKEWIDTH);
  }
  
  public void setLineStrokeWidth(Integer value)
  {
    if(value == null)
    {
      setValue(LINESTROKEWIDTH, "");
    }
    else
    {
      setValue(LINESTROKEWIDTH, java.lang.Integer.toString(value));
    }
  }
  
  public com.runwaysdk.system.SingleActor getLockedBy()
  {
    if (getValue(LOCKEDBY).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.SingleActor.get(getValue(LOCKEDBY));
    }
  }
  
  public String getLockedById()
  {
    return getValue(LOCKEDBY);
  }
  
  public void validateLockedBy()
  {
    this.validateAttribute(LOCKEDBY);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF getLockedByMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.dashboard.DashboardStyle.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF)mdClassIF.definesAttribute(LOCKEDBY);
  }
  
  public String getName()
  {
    return getValue(NAME);
  }
  
  public void validateName()
  {
    this.validateAttribute(NAME);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF getNameMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.dashboard.DashboardStyle.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF)mdClassIF.definesAttribute(NAME);
  }
  
  public void setName(String value)
  {
    if(value == null)
    {
      setValue(NAME, "");
    }
    else
    {
      setValue(NAME, value);
    }
  }
  
  public com.runwaysdk.system.Actor getOwner()
  {
    if (getValue(OWNER).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.Actor.get(getValue(OWNER));
    }
  }
  
  public String getOwnerId()
  {
    return getValue(OWNER);
  }
  
  public void validateOwner()
  {
    this.validateAttribute(OWNER);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF getOwnerMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.dashboard.DashboardStyle.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF)mdClassIF.definesAttribute(OWNER);
  }
  
  public void setOwner(com.runwaysdk.system.Actor value)
  {
    if(value == null)
    {
      setValue(OWNER, "");
    }
    else
    {
      setValue(OWNER, value.getOid());
    }
  }
  
  public String getPointFill()
  {
    return getValue(POINTFILL);
  }
  
  public void validatePointFill()
  {
    this.validateAttribute(POINTFILL);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF getPointFillMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.dashboard.DashboardStyle.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF)mdClassIF.definesAttribute(POINTFILL);
  }
  
  public void setPointFill(String value)
  {
    if(value == null)
    {
      setValue(POINTFILL, "");
    }
    else
    {
      setValue(POINTFILL, value);
    }
  }
  
  public Double getPointOpacity()
  {
    return com.runwaysdk.constants.MdAttributeDoubleUtil.getTypeSafeValue(getValue(POINTOPACITY));
  }
  
  public void validatePointOpacity()
  {
    this.validateAttribute(POINTOPACITY);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeDoubleDAOIF getPointOpacityMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.dashboard.DashboardStyle.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeDoubleDAOIF)mdClassIF.definesAttribute(POINTOPACITY);
  }
  
  public void setPointOpacity(Double value)
  {
    if(value == null)
    {
      setValue(POINTOPACITY, "");
    }
    else
    {
      setValue(POINTOPACITY, java.lang.Double.toString(value));
    }
  }
  
  public Integer getPointRotation()
  {
    return com.runwaysdk.constants.MdAttributeIntegerUtil.getTypeSafeValue(getValue(POINTROTATION));
  }
  
  public void validatePointRotation()
  {
    this.validateAttribute(POINTROTATION);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeIntegerDAOIF getPointRotationMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.dashboard.DashboardStyle.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeIntegerDAOIF)mdClassIF.definesAttribute(POINTROTATION);
  }
  
  public void setPointRotation(Integer value)
  {
    if(value == null)
    {
      setValue(POINTROTATION, "");
    }
    else
    {
      setValue(POINTROTATION, java.lang.Integer.toString(value));
    }
  }
  
  public String getPointStroke()
  {
    return getValue(POINTSTROKE);
  }
  
  public void validatePointStroke()
  {
    this.validateAttribute(POINTSTROKE);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF getPointStrokeMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.dashboard.DashboardStyle.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF)mdClassIF.definesAttribute(POINTSTROKE);
  }
  
  public void setPointStroke(String value)
  {
    if(value == null)
    {
      setValue(POINTSTROKE, "");
    }
    else
    {
      setValue(POINTSTROKE, value);
    }
  }
  
  public Double getPointStrokeOpacity()
  {
    return com.runwaysdk.constants.MdAttributeDoubleUtil.getTypeSafeValue(getValue(POINTSTROKEOPACITY));
  }
  
  public void validatePointStrokeOpacity()
  {
    this.validateAttribute(POINTSTROKEOPACITY);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeDoubleDAOIF getPointStrokeOpacityMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.dashboard.DashboardStyle.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeDoubleDAOIF)mdClassIF.definesAttribute(POINTSTROKEOPACITY);
  }
  
  public void setPointStrokeOpacity(Double value)
  {
    if(value == null)
    {
      setValue(POINTSTROKEOPACITY, "");
    }
    else
    {
      setValue(POINTSTROKEOPACITY, java.lang.Double.toString(value));
    }
  }
  
  public Integer getPointStrokeWidth()
  {
    return com.runwaysdk.constants.MdAttributeIntegerUtil.getTypeSafeValue(getValue(POINTSTROKEWIDTH));
  }
  
  public void validatePointStrokeWidth()
  {
    this.validateAttribute(POINTSTROKEWIDTH);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeIntegerDAOIF getPointStrokeWidthMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.dashboard.DashboardStyle.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeIntegerDAOIF)mdClassIF.definesAttribute(POINTSTROKEWIDTH);
  }
  
  public void setPointStrokeWidth(Integer value)
  {
    if(value == null)
    {
      setValue(POINTSTROKEWIDTH, "");
    }
    else
    {
      setValue(POINTSTROKEWIDTH, java.lang.Integer.toString(value));
    }
  }
  
  public String getPointWellKnownName()
  {
    return getValue(POINTWELLKNOWNNAME);
  }
  
  public void validatePointWellKnownName()
  {
    this.validateAttribute(POINTWELLKNOWNNAME);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF getPointWellKnownNameMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.dashboard.DashboardStyle.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF)mdClassIF.definesAttribute(POINTWELLKNOWNNAME);
  }
  
  public void setPointWellKnownName(String value)
  {
    if(value == null)
    {
      setValue(POINTWELLKNOWNNAME, "");
    }
    else
    {
      setValue(POINTWELLKNOWNNAME, value);
    }
  }
  
  public String getPolygonFill()
  {
    return getValue(POLYGONFILL);
  }
  
  public void validatePolygonFill()
  {
    this.validateAttribute(POLYGONFILL);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF getPolygonFillMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.dashboard.DashboardStyle.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF)mdClassIF.definesAttribute(POLYGONFILL);
  }
  
  public void setPolygonFill(String value)
  {
    if(value == null)
    {
      setValue(POLYGONFILL, "");
    }
    else
    {
      setValue(POLYGONFILL, value);
    }
  }
  
  public Double getPolygonFillOpacity()
  {
    return com.runwaysdk.constants.MdAttributeDoubleUtil.getTypeSafeValue(getValue(POLYGONFILLOPACITY));
  }
  
  public void validatePolygonFillOpacity()
  {
    this.validateAttribute(POLYGONFILLOPACITY);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeDoubleDAOIF getPolygonFillOpacityMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.dashboard.DashboardStyle.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeDoubleDAOIF)mdClassIF.definesAttribute(POLYGONFILLOPACITY);
  }
  
  public void setPolygonFillOpacity(Double value)
  {
    if(value == null)
    {
      setValue(POLYGONFILLOPACITY, "");
    }
    else
    {
      setValue(POLYGONFILLOPACITY, java.lang.Double.toString(value));
    }
  }
  
  public String getPolygonStroke()
  {
    return getValue(POLYGONSTROKE);
  }
  
  public void validatePolygonStroke()
  {
    this.validateAttribute(POLYGONSTROKE);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF getPolygonStrokeMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.dashboard.DashboardStyle.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF)mdClassIF.definesAttribute(POLYGONSTROKE);
  }
  
  public void setPolygonStroke(String value)
  {
    if(value == null)
    {
      setValue(POLYGONSTROKE, "");
    }
    else
    {
      setValue(POLYGONSTROKE, value);
    }
  }
  
  public Double getPolygonStrokeOpacity()
  {
    return com.runwaysdk.constants.MdAttributeDoubleUtil.getTypeSafeValue(getValue(POLYGONSTROKEOPACITY));
  }
  
  public void validatePolygonStrokeOpacity()
  {
    this.validateAttribute(POLYGONSTROKEOPACITY);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeDoubleDAOIF getPolygonStrokeOpacityMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.dashboard.DashboardStyle.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeDoubleDAOIF)mdClassIF.definesAttribute(POLYGONSTROKEOPACITY);
  }
  
  public void setPolygonStrokeOpacity(Double value)
  {
    if(value == null)
    {
      setValue(POLYGONSTROKEOPACITY, "");
    }
    else
    {
      setValue(POLYGONSTROKEOPACITY, java.lang.Double.toString(value));
    }
  }
  
  public Integer getPolygonStrokeWidth()
  {
    return com.runwaysdk.constants.MdAttributeIntegerUtil.getTypeSafeValue(getValue(POLYGONSTROKEWIDTH));
  }
  
  public void validatePolygonStrokeWidth()
  {
    this.validateAttribute(POLYGONSTROKEWIDTH);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeIntegerDAOIF getPolygonStrokeWidthMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.dashboard.DashboardStyle.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeIntegerDAOIF)mdClassIF.definesAttribute(POLYGONSTROKEWIDTH);
  }
  
  public void setPolygonStrokeWidth(Integer value)
  {
    if(value == null)
    {
      setValue(POLYGONSTROKEWIDTH, "");
    }
    else
    {
      setValue(POLYGONSTROKEWIDTH, java.lang.Integer.toString(value));
    }
  }
  
  public Long getSeq()
  {
    return com.runwaysdk.constants.MdAttributeLongUtil.getTypeSafeValue(getValue(SEQ));
  }
  
  public void validateSeq()
  {
    this.validateAttribute(SEQ);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeLongDAOIF getSeqMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.dashboard.DashboardStyle.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeLongDAOIF)mdClassIF.definesAttribute(SEQ);
  }
  
  public String getSiteMaster()
  {
    return getValue(SITEMASTER);
  }
  
  public void validateSiteMaster()
  {
    this.validateAttribute(SITEMASTER);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF getSiteMasterMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.dashboard.DashboardStyle.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF)mdClassIF.definesAttribute(SITEMASTER);
  }
  
  public String getType()
  {
    return getValue(TYPE);
  }
  
  public void validateType()
  {
    this.validateAttribute(TYPE);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF getTypeMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.dashboard.DashboardStyle.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF)mdClassIF.definesAttribute(TYPE);
  }
  
  public String getValueColor()
  {
    return getValue(VALUECOLOR);
  }
  
  public void validateValueColor()
  {
    this.validateAttribute(VALUECOLOR);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF getValueColorMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.dashboard.DashboardStyle.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF)mdClassIF.definesAttribute(VALUECOLOR);
  }
  
  public void setValueColor(String value)
  {
    if(value == null)
    {
      setValue(VALUECOLOR, "");
    }
    else
    {
      setValue(VALUECOLOR, value);
    }
  }
  
  public String getValueFont()
  {
    return getValue(VALUEFONT);
  }
  
  public void validateValueFont()
  {
    this.validateAttribute(VALUEFONT);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF getValueFontMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.dashboard.DashboardStyle.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF)mdClassIF.definesAttribute(VALUEFONT);
  }
  
  public void setValueFont(String value)
  {
    if(value == null)
    {
      setValue(VALUEFONT, "");
    }
    else
    {
      setValue(VALUEFONT, value);
    }
  }
  
  public String getValueHalo()
  {
    return getValue(VALUEHALO);
  }
  
  public void validateValueHalo()
  {
    this.validateAttribute(VALUEHALO);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF getValueHaloMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.dashboard.DashboardStyle.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF)mdClassIF.definesAttribute(VALUEHALO);
  }
  
  public void setValueHalo(String value)
  {
    if(value == null)
    {
      setValue(VALUEHALO, "");
    }
    else
    {
      setValue(VALUEHALO, value);
    }
  }
  
  public Integer getValueHaloWidth()
  {
    return com.runwaysdk.constants.MdAttributeIntegerUtil.getTypeSafeValue(getValue(VALUEHALOWIDTH));
  }
  
  public void validateValueHaloWidth()
  {
    this.validateAttribute(VALUEHALOWIDTH);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeIntegerDAOIF getValueHaloWidthMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.dashboard.DashboardStyle.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeIntegerDAOIF)mdClassIF.definesAttribute(VALUEHALOWIDTH);
  }
  
  public void setValueHaloWidth(Integer value)
  {
    if(value == null)
    {
      setValue(VALUEHALOWIDTH, "");
    }
    else
    {
      setValue(VALUEHALOWIDTH, java.lang.Integer.toString(value));
    }
  }
  
  public Integer getValueSize()
  {
    return com.runwaysdk.constants.MdAttributeIntegerUtil.getTypeSafeValue(getValue(VALUESIZE));
  }
  
  public void validateValueSize()
  {
    this.validateAttribute(VALUESIZE);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeIntegerDAOIF getValueSizeMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.dashboard.DashboardStyle.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeIntegerDAOIF)mdClassIF.definesAttribute(VALUESIZE);
  }
  
  public void setValueSize(Integer value)
  {
    if(value == null)
    {
      setValue(VALUESIZE, "");
    }
    else
    {
      setValue(VALUESIZE, java.lang.Integer.toString(value));
    }
  }
  
  protected String getDeclaredType()
  {
    return CLASS;
  }
  
  public static DashboardStyleQuery getAllInstances(String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    DashboardStyleQuery query = new DashboardStyleQuery(new com.runwaysdk.query.QueryFactory());
    com.runwaysdk.business.Entity.getAllInstances(query, sortAttribute, ascending, pageSize, pageNumber);
    return query;
  }
  
  public net.geoprism.dashboard.HasStyle addContainingLayer(net.geoprism.dashboard.layer.DashboardLayer dashboardLayer)
  {
    return (net.geoprism.dashboard.HasStyle) addParent(dashboardLayer, net.geoprism.dashboard.HasStyle.CLASS);
  }
  
  public void removeContainingLayer(net.geoprism.dashboard.layer.DashboardLayer dashboardLayer)
  {
    removeAllParents(dashboardLayer, net.geoprism.dashboard.HasStyle.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public com.runwaysdk.query.OIterator<? extends net.geoprism.dashboard.layer.DashboardLayer> getAllContainingLayer()
  {
    return (com.runwaysdk.query.OIterator<? extends net.geoprism.dashboard.layer.DashboardLayer>) getParents(net.geoprism.dashboard.HasStyle.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public com.runwaysdk.query.OIterator<? extends net.geoprism.dashboard.HasStyle> getAllContainingLayerRel()
  {
    return (com.runwaysdk.query.OIterator<? extends net.geoprism.dashboard.HasStyle>) getParentRelationships(net.geoprism.dashboard.HasStyle.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public com.runwaysdk.query.OIterator<? extends net.geoprism.dashboard.HasStyle> getContainingLayerRel(net.geoprism.dashboard.layer.DashboardLayer dashboardLayer)
  {
    return (com.runwaysdk.query.OIterator<? extends net.geoprism.dashboard.HasStyle>) getRelationshipsWithParent(dashboardLayer, net.geoprism.dashboard.HasStyle.CLASS);
  }
  
  public static DashboardStyle get(String oid)
  {
    return (DashboardStyle) com.runwaysdk.business.Business.get(oid);
  }
  
  public static DashboardStyle getByKey(String key)
  {
    return (DashboardStyle) com.runwaysdk.business.Business.get(CLASS, key);
  }
  
  public static java.lang.String getAggregationJSON()
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.dashboard.DashboardStyle.java";
    throw new com.runwaysdk.dataaccess.metadata.ForbiddenMethodException(msg);
  }
  
  public java.lang.String getJSON()
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.dashboard.DashboardStyle.java";
    throw new com.runwaysdk.dataaccess.metadata.ForbiddenMethodException(msg);
  }
  
  public static final java.lang.String getJSON(java.lang.String oid)
  {
    DashboardStyle _instance = DashboardStyle.get(oid);
    return _instance.getJSON();
  }
  
  public static net.geoprism.dashboard.AggregationTypeQuery getSortedAggregations(java.lang.String thematicAttributeId)
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.dashboard.DashboardStyle.java";
    throw new com.runwaysdk.dataaccess.metadata.ForbiddenMethodException(msg);
  }
  
  public static java.lang.String[] getSortedFonts()
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.dashboard.DashboardStyle.java";
    throw new com.runwaysdk.dataaccess.metadata.ForbiddenMethodException(msg);
  }
  
  public static DashboardStyle lock(java.lang.String oid)
  {
    DashboardStyle _instance = DashboardStyle.get(oid);
    _instance.lock();
    
    return _instance;
  }
  
  public static DashboardStyle unlock(java.lang.String oid)
  {
    DashboardStyle _instance = DashboardStyle.get(oid);
    _instance.unlock();
    
    return _instance;
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
