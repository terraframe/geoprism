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
package net.geoprism.registry.service.business;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.runwaysdk.business.graph.EdgeObject;

import net.geoprism.registry.graph.DataSource;
import net.geoprism.registry.graph.EdgeClass;
import net.geoprism.registry.graph.ObjectClass;
import net.geoprism.registry.model.graph.ServerObjectVertex;
import net.geoprism.registry.model.graph.VertexComponent;
import net.geoprism.registry.query.graph.VertexAndEdgeQuery.EdgeQueryObject;
import net.geoprism.registry.view.ObjectClassDTO;
import net.geoprism.registry.view.Page;

@Component
public interface ObjectEdgeBusinessServiceIF<V extends ServerObjectVertex, T extends ObjectClass, D extends ObjectClassDTO, E extends EdgeClass, N extends VertexComponent> extends ObjectBusinessServiceIF<V, T, D>
{
  public boolean exists(N object, E edgeType, N parent);

  public boolean exists(E type, String uid);

  public boolean exists(E type, N parent, N child, Date startDate, Date endDate);

  public Optional<EdgeObject> addParent(N object, E type, N parent, String uid, Date startDate, Date endDate, DataSource source);

  public Optional<EdgeObject> addParent(N object, E type, N parent, String uid, Date startDate, Date endDate, DataSource source, boolean validateOrigin);

  public void removeParent(N object, E type, N parent, Date startDate, Date endDate);

  public void removeParent(N object, E type, N parent, Date startDate, Date endDate, boolean validateOrigin);

  public Optional<EdgeObject> addChild(N object, E type, N child, String uid, Date startDate, Date endDate, DataSource source);

  public Optional<EdgeObject> addChild(N object, E type, N child, String uid, Date startDate, Date endDate, DataSource source, boolean validateOrigin);

  public void removeChild(N object, E type, N child, Date startDate, Date endDate);

  public void removeChild(N object, E type, N child, Date startDate, Date endDate, boolean validateOrigin);

  public List<N> getChildren(V object, E type, Date date);

  public Page<N> getChildren(V object, E type, Date date, Integer pageSize, Integer pageNumber);

  public List<N> getParents(V object, E type, Date date);

  public Page<N> getParents(V object, E type, Date date, Integer pageSize, Integer pageNumber);

  public List<EdgeQueryObject> getEdgeChildren(V object, E type, Date date);

  public List<EdgeQueryObject> getEdgeParents(V object, E type, Date date);

}
