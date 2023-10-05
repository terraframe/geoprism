/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Geoprism Registry(tm).
 *
 * Geoprism Registry(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Geoprism Registry(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Geoprism Registry(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism.registry;

import java.util.LinkedList;
import java.util.List;

import org.commongeoregistry.adapter.dataaccess.LocalizedValue;

import com.google.gson.JsonObject;
import com.runwaysdk.business.rbac.RoleDAO;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeDateTimeInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.graph.MdEdgeInfo;
import com.runwaysdk.dataaccess.DuplicateDataException;
import com.runwaysdk.dataaccess.MdEdgeDAOIF;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.metadata.MdAttributeDateTimeDAO;
import com.runwaysdk.dataaccess.metadata.graph.MdEdgeDAO;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.system.metadata.MdEdge;

import net.geoprism.registry.conversion.RegistryLocalizedValueConverter;
import net.geoprism.registry.graph.GeoVertex;
import net.geoprism.registry.model.GraphType;
import net.geoprism.registry.model.ServerElement;
import net.geoprism.registry.model.graph.DirectedAcyclicGraphStrategy;
import net.geoprism.registry.model.graph.GraphStrategy;
import net.geoprism.registry.view.JsonSerializable;

public class DirectedAcyclicGraphType extends DirectedAcyclicGraphTypeBase implements JsonSerializable, GraphType, ServerElement
{
  private static final long  serialVersionUID = 1222275153;

  public static final String JSON_LABEL       = "label";

  public DirectedAcyclicGraphType()
  {
    super();
  }

  @Override
  protected String buildKey()
  {
    return this.getCode();
  }

  @Override
  public LocalizedValue getLabel()
  {
    return RegistryLocalizedValueConverter.convertNoAutoCoalesce(this.getDisplayLabel());
  }

  public MdEdgeDAOIF getMdEdgeDAO()
  {
    return MdEdgeDAO.get(this.getMdEdgeOid());
  }

  @Override
  public JsonObject toJSON()
  {
    JsonObject object = new JsonObject();
    object.addProperty(DirectedAcyclicGraphType.OID, this.getOid());
    object.addProperty(DirectedAcyclicGraphType.TYPE, "DirectedAcyclicGraphType");
    object.addProperty(DirectedAcyclicGraphType.CODE, this.getCode());
    object.add(DirectedAcyclicGraphType.JSON_LABEL, RegistryLocalizedValueConverter.convertNoAutoCoalesce(this.getDisplayLabel()).toJSON());
    object.add(DirectedAcyclicGraphType.DESCRIPTION, RegistryLocalizedValueConverter.convertNoAutoCoalesce(this.getDescription()).toJSON());

    return object;
  }

  public GraphStrategy getStrategy()
  {
    return new DirectedAcyclicGraphStrategy(this);
  }

  public static List<DirectedAcyclicGraphType> getAll()
  {
    DirectedAcyclicGraphTypeQuery query = new DirectedAcyclicGraphTypeQuery(new QueryFactory());

    try (OIterator<? extends DirectedAcyclicGraphType> it = query.getIterator())
    {
      return new LinkedList<DirectedAcyclicGraphType>(it.getAll());
    }
  }

  public static DirectedAcyclicGraphType getByCode(String code)
  {
    return DirectedAcyclicGraphType.getByKey(code);
  }

  public static DirectedAcyclicGraphType getByMdEdge(MdEdge mdEdge)
  {
    DirectedAcyclicGraphTypeQuery query = new DirectedAcyclicGraphTypeQuery(new QueryFactory());
    query.WHERE(query.getMdEdge().EQ(mdEdge));

    try (OIterator<? extends DirectedAcyclicGraphType> it = query.getIterator())
    {
      if (it.hasNext())
      {
        return it.next();
      }
    }

    return null;
  }
}
