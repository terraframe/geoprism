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
package net.geoprism.registry;

import java.util.LinkedList;
import java.util.List;

import org.commongeoregistry.adapter.dataaccess.LocalizedValue;

import com.google.gson.JsonObject;
import com.runwaysdk.dataaccess.MdEdgeDAOIF;
import com.runwaysdk.dataaccess.metadata.graph.MdEdgeDAO;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.system.metadata.MdEdge;

import net.geoprism.registry.conversion.RegistryLocalizedValueConverter;
import net.geoprism.registry.model.GraphType;
import net.geoprism.registry.model.ServerElement;
import net.geoprism.registry.model.graph.GraphStrategy;
import net.geoprism.registry.model.graph.UndirectedGraphStrategy;
import net.geoprism.registry.view.JsonSerializable;

public class UndirectedGraphType extends UndirectedGraphTypeBase implements JsonSerializable, GraphType, ServerElement
{
  private static final long  serialVersionUID = -1097845938;

  public static final String JSON_LABEL       = "label";

  public UndirectedGraphType()
  {
    super();
  }

  public MdEdgeDAOIF getMdEdgeDAO()
  {
    return MdEdgeDAO.get(this.getMdEdgeOid());
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
  
  @Override
  public LocalizedValue getDescriptionLV()
  {
    return RegistryLocalizedValueConverter.convertNoAutoCoalesce(this.getDescription());
  }

  @Override
  public JsonObject toJSON()
  {
    JsonObject object = new JsonObject();
    object.addProperty(UndirectedGraphType.OID, this.getOid());
    object.addProperty(UndirectedGraphType.TYPE, "UndirectedGraphType");
    object.addProperty(UndirectedGraphType.CODE, this.getCode());
    object.add(UndirectedGraphType.JSON_LABEL, RegistryLocalizedValueConverter.convertNoAutoCoalesce(this.getDisplayLabel()).toJSON());
    object.add(UndirectedGraphType.DESCRIPTION, RegistryLocalizedValueConverter.convertNoAutoCoalesce(this.getDescription()).toJSON());

    return object;
  }

  public static List<UndirectedGraphType> getAll()
  {
    UndirectedGraphTypeQuery query = new UndirectedGraphTypeQuery(new QueryFactory());

    try (OIterator<? extends UndirectedGraphType> it = query.getIterator())
    {
      return new LinkedList<UndirectedGraphType>(it.getAll());
    }
  }

  @Override
  public GraphStrategy getStrategy()
  {
    return new UndirectedGraphStrategy(this);
  }

  public static UndirectedGraphType getByCode(String code)
  {
    return UndirectedGraphType.getByKey(code);
  }

  public static UndirectedGraphType getByMdEdge(MdEdge mdEdge)
  {
    UndirectedGraphTypeQuery query = new UndirectedGraphTypeQuery(new QueryFactory());
    query.WHERE(query.getMdEdge().EQ(mdEdge));

    try (OIterator<? extends UndirectedGraphType> it = query.getIterator())
    {
      if (it.hasNext())
      {
        return it.next();
      }
    }

    return null;
  }

}
