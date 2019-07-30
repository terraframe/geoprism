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

@com.runwaysdk.business.ClassSignature(hash = -659693820)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to ReportParameter.java
 *
 * @author Autogenerated by RunwaySDK
 */
public  abstract  class ReportParameterQueryBase extends com.runwaysdk.query.GeneratedViewQuery
 
{

  public ReportParameterQueryBase(com.runwaysdk.query.QueryFactory componentQueryFactory)
  {
    super(componentQueryFactory);
  }

  public ReportParameterQueryBase(com.runwaysdk.query.QueryFactory componentQueryFactory, com.runwaysdk.query.ViewQueryBuilder viewQueryBuilder)
  {
    super(componentQueryFactory, viewQueryBuilder);
  }
  public String getClassType()
  {
    return net.geoprism.report.ReportParameter.CLASS;
  }
  public com.runwaysdk.query.SelectableUUID getOid()
  {
    return getOid(null);

  }
 
  public com.runwaysdk.query.SelectableUUID getOid(String alias)
  {
    return (com.runwaysdk.query.SelectableUUID)this.getSelectable(net.geoprism.report.ReportParameter.OID, alias, null);

  }
 
  public com.runwaysdk.query.SelectableUUID getOid(String alias, String displayLabel)
  {
    return (com.runwaysdk.query.SelectableUUID)this.getSelectable(net.geoprism.report.ReportParameter.OID, alias, displayLabel);

  }
 
  public com.runwaysdk.query.SelectableChar getParameterName()
  {
    return getParameterName(null);

  }
 
  public com.runwaysdk.query.SelectableChar getParameterName(String alias)
  {
    return (com.runwaysdk.query.SelectableChar)this.getSelectable(net.geoprism.report.ReportParameter.PARAMETERNAME, alias, null);

  }
 
  public com.runwaysdk.query.SelectableChar getParameterName(String alias, String displayLabel)
  {
    return (com.runwaysdk.query.SelectableChar)this.getSelectable(net.geoprism.report.ReportParameter.PARAMETERNAME, alias, displayLabel);

  }
 
  public com.runwaysdk.query.SelectableChar getParameterValue()
  {
    return getParameterValue(null);

  }
 
  public com.runwaysdk.query.SelectableChar getParameterValue(String alias)
  {
    return (com.runwaysdk.query.SelectableChar)this.getSelectable(net.geoprism.report.ReportParameter.PARAMETERVALUE, alias, null);

  }
 
  public com.runwaysdk.query.SelectableChar getParameterValue(String alias, String displayLabel)
  {
    return (com.runwaysdk.query.SelectableChar)this.getSelectable(net.geoprism.report.ReportParameter.PARAMETERVALUE, alias, displayLabel);

  }
 
  /**  
   * Returns an iterator of Business objects that match the query criteria specified
   * on this query object. 
   * @return iterator of Business objects that match the query criteria specified
   * on this query object.
   */
  @java.lang.SuppressWarnings("unchecked")
  public com.runwaysdk.query.OIterator<? extends ReportParameter> getIterator()
  {
    com.runwaysdk.query.ValueIterator valueIterator;
    if (_pageSize != null && _pageNumber != null)
    {
      valueIterator = (com.runwaysdk.query.ValueIterator<com.runwaysdk.dataaccess.ValueObject>)this.getComponentQuery().getIterator(_pageSize, _pageNumber);
    }
    else
    {
      valueIterator = (com.runwaysdk.query.ValueIterator<com.runwaysdk.dataaccess.ValueObject>)this.getComponentQuery().getIterator();
    }
    return new com.runwaysdk.query.ViewIterator<ReportParameter>(this.getMdClassIF(), valueIterator);
  }

}
