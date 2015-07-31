package com.runwaysdk.geodashboard.service;

import java.util.LinkedList;
import java.util.List;

import com.runwaysdk.business.BusinessQuery;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.query.QueryFactory;

public class ShapefileExportConfiguration
{
  private MdBusinessDAOIF mdBusiness;

  private Integer         featureLimit;

  private List<String>    requiredAttributes;

  private List<String>    locationTypes;

  private String          geometryAttribue;

  public ShapefileExportConfiguration(MdBusinessDAOIF mdBusiness)
  {
    this(mdBusiness, null);
  }

  public ShapefileExportConfiguration(MdBusinessDAOIF mdBusiness, Integer featureLimit)
  {
    super();
    this.mdBusiness = mdBusiness;
    this.featureLimit = featureLimit;
    this.requiredAttributes = new LinkedList<String>();
    this.locationTypes = new LinkedList<String>();
  }

  public MdBusinessDAOIF getMdBusiness()
  {
    return mdBusiness;
  }

  public Integer getFeatureLimit()
  {
    return featureLimit;
  }

  public void addRequiredAttribute(String attributeName)
  {
    this.requiredAttributes.add(attributeName);
  }

  public void addLocationType(String key)
  {
    this.locationTypes.add(key);
  }

  public List<String> getLocationTypes()
  {
    return locationTypes;
  }

  public void setGeometryAttribute(String geometryAttribute)
  {
    this.geometryAttribue = geometryAttribute;
  }

  public String getGeometryAttribue()
  {
    return geometryAttribue;
  }

  public BusinessQuery createQuery()
  {
    QueryFactory qFactory = new QueryFactory();
    BusinessQuery query = qFactory.businessQuery(this.mdBusiness.definesType());

    for (String attributeName : this.requiredAttributes)
    {
      query.WHERE(query.get(attributeName).NE((String) null));
    }

    return query;
  }
}
