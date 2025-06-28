/**
 * Copyright (c) 2023 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Geoprism(tm).
 *
 * Geoprism(tm) is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * Geoprism(tm) is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Geoprism(tm). If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism.registry.model;

import java.util.Date;

import org.commongeoregistry.adapter.dataaccess.TreeNode;

import com.google.gson.JsonObject;

import net.geoprism.registry.DateFormatter;
import net.geoprism.registry.service.business.GeoObjectBusinessServiceIF;
import net.geoprism.registry.service.business.ServiceFactory;

public abstract class ServerGraphNode
{
  private ServerGeoObjectIF geoObject;

  private GraphType         graphType;

  private Date              startDate;

  private Date              endDate;

  private String            oid;

  private String            uid;

  public ServerGraphNode(ServerGeoObjectIF geoObject, GraphType graphType, Date startDate, Date endDate, String oid, String uid)
  {
    this.geoObject = geoObject;
    this.graphType = graphType;
    this.startDate = startDate;
    this.endDate = endDate;
    this.oid = oid;
    this.uid = uid;
  }

  public ServerGeoObjectIF getGeoObject()
  {
    return geoObject;
  }

  public GraphType getHierarchyType()
  {
    return graphType;
  }

  public Date getStartDate()
  {
    return startDate;
  }

  public void setStartDate(Date startDate)
  {
    this.startDate = startDate;
  }

  public void setEndDate(Date endDate)
  {
    this.endDate = endDate;
  }

  public Date getEndDate()
  {
    return endDate;
  }

  public String getOid()
  {
    return oid;
  }

  public void setOid(String oid)
  {
    this.oid = oid;
  }
  
  public String getUid()
  {
    return uid;
  }
  
  public void setUid(String uid)
  {
    this.uid = uid;
  }

  public JsonObject toJSON()
  {
    JsonObject json = new JsonObject();

    if (this.geoObject != null)
    {
      json.add(TreeNode.JSON_GEO_OBJECT, ServiceFactory.getBean(GeoObjectBusinessServiceIF.class).toGeoObject(this.geoObject, this.startDate).toJSON());
    }
    else
    {
      json.add(TreeNode.JSON_GEO_OBJECT, null);
    }

    if (this.graphType != null) // The hierarchyType is null on the root node
    {
      json.addProperty("graphType", this.graphType.getCode());
      json.addProperty("graphTypeClass", this.graphType.getClass().getName());
    }

    if (this.oid != null)
    {
      json.addProperty("oid", this.oid);
    }

    if (this.uid != null)
    {
      json.addProperty("uid", this.uid);
    }
    
    if (this.startDate != null)
    {
      json.addProperty("startDate", DateFormatter.formatDate(this.startDate, false));
    }

    if (this.endDate != null)
    {
      json.addProperty("endDate", DateFormatter.formatDate(this.endDate, false));
    }

    return json;
  }

}
