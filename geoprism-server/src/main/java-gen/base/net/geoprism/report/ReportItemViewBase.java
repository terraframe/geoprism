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
package net.geoprism.report;

@com.runwaysdk.business.ClassSignature(hash = -1316633316)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to ReportItemView.java
 *
 * @author Autogenerated by RunwaySDK
 */
public abstract class ReportItemViewBase extends com.runwaysdk.business.View 
{
  public final static String CLASS = "net.geoprism.report.ReportItemView";
  public static java.lang.String DASHBOARDLABEL = "dashboardLabel";
  public static java.lang.String OID = "oid";
  public static java.lang.String REPORTID = "reportId";
  public static java.lang.String REPORTLABEL = "reportLabel";
  public static java.lang.String REPORTNAME = "reportName";
  private static final long serialVersionUID = -1316633316;
  
  public ReportItemViewBase()
  {
    super();
  }
  
  public String getDashboardLabel()
  {
    return getValue(DASHBOARDLABEL);
  }
  
  public void validateDashboardLabel()
  {
    this.validateAttribute(DASHBOARDLABEL);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeTextDAOIF getDashboardLabelMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.report.ReportItemView.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeTextDAOIF)mdClassIF.definesAttribute(DASHBOARDLABEL);
  }
  
  public void setDashboardLabel(String value)
  {
    if(value == null)
    {
      setValue(DASHBOARDLABEL, "");
    }
    else
    {
      setValue(DASHBOARDLABEL, value);
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
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.report.ReportItemView.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF)mdClassIF.definesAttribute(OID);
  }
  
  public String getReportId()
  {
    return getValue(REPORTID);
  }
  
  public void validateReportId()
  {
    this.validateAttribute(REPORTID);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeTextDAOIF getReportIdMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.report.ReportItemView.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeTextDAOIF)mdClassIF.definesAttribute(REPORTID);
  }
  
  public void setReportId(String value)
  {
    if(value == null)
    {
      setValue(REPORTID, "");
    }
    else
    {
      setValue(REPORTID, value);
    }
  }
  
  public String getReportLabel()
  {
    return getValue(REPORTLABEL);
  }
  
  public void validateReportLabel()
  {
    this.validateAttribute(REPORTLABEL);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeTextDAOIF getReportLabelMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.report.ReportItemView.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeTextDAOIF)mdClassIF.definesAttribute(REPORTLABEL);
  }
  
  public void setReportLabel(String value)
  {
    if(value == null)
    {
      setValue(REPORTLABEL, "");
    }
    else
    {
      setValue(REPORTLABEL, value);
    }
  }
  
  public String getReportName()
  {
    return getValue(REPORTNAME);
  }
  
  public void validateReportName()
  {
    this.validateAttribute(REPORTNAME);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeTextDAOIF getReportNameMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.report.ReportItemView.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeTextDAOIF)mdClassIF.definesAttribute(REPORTNAME);
  }
  
  public void setReportName(String value)
  {
    if(value == null)
    {
      setValue(REPORTNAME, "");
    }
    else
    {
      setValue(REPORTNAME, value);
    }
  }
  
  protected String getDeclaredType()
  {
    return CLASS;
  }
  
  public static ReportItemView get(String oid)
  {
    return (ReportItemView) com.runwaysdk.business.View.get(oid);
  }
  
  public void remove()
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.report.ReportItemView.java";
    throw new com.runwaysdk.dataaccess.metadata.ForbiddenMethodException(msg);
  }
  
  public static final void remove(java.lang.String oid)
  {
    ReportItemView _instance = ReportItemView.get(oid);
    _instance.remove();
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
