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
import java.util.List;
import java.util.SortedSet;

import org.commongeoregistry.adapter.dataaccess.LocalizedValue;
import org.locationtech.jts.geom.Geometry;

import com.runwaysdk.business.graph.EdgeObject;
import com.runwaysdk.business.graph.VertexObject;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdGraphClassDAOIF;
import com.runwaysdk.dataaccess.graph.attributes.ValueOverTimeCollection;

public interface ServerGeoObjectIF
{
  public boolean exists(ServerGeoObjectIF parent, ServerHierarchyType hierarchyType, Date startDate, Date endDate);

  public ServerGeoObjectType getType();

  public Date getDate();

  public Date getCreateDate();

  public Date getLastUpdateDate();

  public String getCode();

  public Boolean getInvalid();

  public void setInvalid(Boolean invalid);

  public void setCode(String code);

  public Boolean getExists();

  public Boolean getExists(Date date);

  public void setExists(Boolean exists);

  public void setExists(Boolean exists, Date startDate, Date endDate);

  public void setGeometry(Geometry geometry);

  public void setGeometry(Geometry geometry, Date startDate, Date endDate);

  public Geometry getGeometry();

  public Geometry getGeometry(Date date);

  public String getUid();

  public void setUid(String uid);

  public String getRunwayId();

  public <T> T getValue(String attributeName);

  public <T> T getValue(String attributeName, Date date);

  public void setValue(String attributeName, Object value);

  public void setValue(String attributeName, Object value, Date startDate, Date endDate);

  public void setDisplayLabel(LocalizedValue label);

  public void setDisplayLabel(LocalizedValue value, Date startDate, Date endDate);

  public LocalizedValue getDisplayLabel();

  public String bbox(Date date);

  public void setDate(Date date);

  public MdGraphClassDAOIF getMdClass();

  public VertexObject getVertex();

  public EdgeObject getEdge(ServerGeoObjectIF parent, ServerHierarchyType hierarchyType, Date startDate, Date endDate);

  public ValueOverTimeCollection getValuesOverTime(String attributeName);

  public void setValuesOverTime(String attributeName, ValueOverTimeCollection collection);

  public LocalizedValue getDisplayLabel(Date date);

  public LocalizedValue getValueLocalized(String attributeName, Date startDate);

  public SortedSet<EdgeObject> getEdges(ServerHierarchyType hierarchyType);

  public <T extends ServerGraphNode> T getGraphChildren(GraphType type, Boolean recursive, Date date);

  public <T extends ServerGraphNode> T getGraphParents(GraphType type, Boolean recursive, Date date);

  <T extends ServerGraphNode> T getGraphParents(GraphType type, Boolean recursive, Date date, String boundsWKT, Long skip, Long limit);

  <T extends ServerGraphNode> T getGraphChildren(GraphType type, Boolean recursive, Date date, String boundsWKT, Long skip, Long limit);

  <T extends ServerGraphNode> T addGraphParent(ServerGeoObjectIF parent, GraphType type, Date startDate, Date endDate, boolean validate);

  <T extends ServerGraphNode> T addGraphChild(ServerGeoObjectIF child, GraphType type, Date startDate, Date endDate, boolean validate);

  void removeGraphChild(ServerGeoObjectIF child, GraphType type, Date startDate, Date endDate);

  public boolean isNew();

  public boolean isModified(String attributeName);

  public boolean hasAttribute(String attributeName);

  public void apply();

  void delete();

}
