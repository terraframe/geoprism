/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
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
package net.geoprism.data.importer;

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
