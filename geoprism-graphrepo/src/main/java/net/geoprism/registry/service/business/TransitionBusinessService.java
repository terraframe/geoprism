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

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.runwaysdk.business.graph.GraphQuery;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;

import net.geoprism.registry.graph.transition.Transition;
import net.geoprism.registry.graph.transition.TransitionEvent;
import net.geoprism.registry.model.ServerGeoObjectType;
import net.geoprism.registry.model.graph.VertexServerGeoObject;

@Service
public class TransitionBusinessService implements TransitionBusinessServiceIF
{
  @Autowired
  protected GeoObjectTypeBusinessServiceIF gotServ;
  
  @Transaction
  public void delete(Transition tran)
  {
    tran.delete();
  }
  
  @Transaction
  public void apply(Transition tran, TransitionEvent event, int order, VertexServerGeoObject source, VertexServerGeoObject target)
  {
    this.validate(tran, event, source, target);

    tran.setOrder(order);
    tran.setValue(Transition.SOURCE, source.getVertex());
    tran.setValue(Transition.TARGET, target.getVertex());

    tran.apply();
  }
  
  public void validate(Transition tran, TransitionEvent event, VertexServerGeoObject source, VertexServerGeoObject target)
  {
    ServerGeoObjectType beforeType = ServerGeoObjectType.get(event.getBeforeTypeCode());
    ServerGeoObjectType afterType = ServerGeoObjectType.get(event.getAfterTypeCode());

    List<ServerGeoObjectType> beforeSubtypes = gotServ.getSubtypes(beforeType);
    List<ServerGeoObjectType> afterSubtypes = gotServ.getSubtypes(afterType);

    if (! ( beforeSubtypes.contains(source.getType()) || beforeType.getCode().equals(source.getType().getCode()) ))
    {
      // This should be prevented by the front-end
      throw new ProgrammingErrorException("Source type must be a subtype of (" + beforeType.getCode() + ")");
    }

    if (! ( afterSubtypes.contains(target.getType()) || afterType.getCode().equals(target.getType().getCode()) ))
    {
      // This should be prevented by the front-end
      throw new ProgrammingErrorException("Target type must be a subtype of (" + afterType.getCode() + ")");
    }
  }
  
  @Transaction
  public void removeAll(ServerGeoObjectType type)
  {
    MdVertexDAOIF mdVertex = MdVertexDAO.getMdVertexDAO(Transition.CLASS);
    MdAttributeDAOIF sourceAttribute = mdVertex.definesAttribute(Transition.SOURCE);
    MdAttributeDAOIF targetAttribute = mdVertex.definesAttribute(Transition.TARGET);

    StringBuilder statement = new StringBuilder();
    statement.append("SELECT FROM " + mdVertex.getDBClassName());
    statement.append(" WHERE " + sourceAttribute.getColumnName() + ".@class = :vertexClass");
    statement.append(" OR " + targetAttribute.getColumnName() + ".@class = :vertexClass");

    GraphQuery<Transition> query = new GraphQuery<Transition>(statement.toString());
    query.setParameter("vertexClass", type.getMdVertex().getDBClassName());

    List<Transition> results = query.getResults();
    results.forEach(event -> event.delete());
  }
}
