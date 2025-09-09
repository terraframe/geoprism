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
package net.geoprism.registry;

import org.commongeoregistry.adapter.dataaccess.LocalizedValue;
import org.commongeoregistry.adapter.metadata.GraphTypeDTO;

import com.google.gson.JsonObject;
import com.runwaysdk.dataaccess.MdEdgeDAOIF;
import com.runwaysdk.dataaccess.metadata.graph.MdEdgeDAO;

import net.geoprism.graph.GraphTypeSnapshot;
import net.geoprism.registry.conversion.LocalizedValueConverter;
import net.geoprism.registry.conversion.RegistryLocalizedValueConverter;
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
  public void apply()
  {
    super.apply();
  }

  @Override
  public void delete()
  {
    super.delete();
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
    object.addProperty(DirectedAcyclicGraphType.SEQ, this.getSeq());
    object.add(DirectedAcyclicGraphType.JSON_LABEL, RegistryLocalizedValueConverter.convertNoAutoCoalesce(this.getDisplayLabel()).toJSON());
    object.add(DirectedAcyclicGraphType.DESCRIPTION, RegistryLocalizedValueConverter.convertNoAutoCoalesce(this.getDescription()).toJSON());

    return object;
  }

  @Override
  public GraphTypeDTO toDTO()
  {
    LocalizedValue label = LocalizedValueConverter.convertNoAutoCoalesce(this.getDisplayLabel());
    LocalizedValue description = LocalizedValueConverter.convertNoAutoCoalesce(this.getDescription());

    final GraphTypeDTO dto = new GraphTypeDTO(GraphTypeSnapshot.DIRECTED_ACYCLIC_GRAPH_TYPE, this.getCode(), label, description);

    return dto;
  }

  public GraphStrategy getStrategy()
  {
    return new DirectedAcyclicGraphStrategy(this);
  }
}
