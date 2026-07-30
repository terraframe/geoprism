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
package net.geoprism.registry.model.graph;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.commongeoregistry.adapter.dataaccess.LocalizedValue;

import com.runwaysdk.dataaccess.MdVertexDAOIF;

import net.geoprism.registry.graph.AttributeType;
import net.geoprism.registry.model.ServerOrganization;
import net.geoprism.registry.view.TypeInfo;

public interface ObjectClassIF
{

  public Optional<AttributeType> getAttribute(String attributeName);

  public List<AttributeType> getAttributes();

  public Map<String, AttributeType> getAttributeMap();

  public ServerOrganization getServerOrganization();

  public MdVertexDAOIF getMdVertexDAO();

  public String getOrigin();

  public TypeInfo getTypeInfo();

  public LocalizedValue getLabel();

  public String getCode();
}
