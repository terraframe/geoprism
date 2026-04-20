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

import org.commongeoregistry.adapter.constants.GeometryType;
import org.commongeoregistry.adapter.metadata.AttributeType;

import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;
import com.runwaysdk.system.metadata.MdAttributeConcrete;
import com.runwaysdk.system.metadata.MdClass;

import net.geoprism.graph.AttributeGeometryTypeSnapshot;
import net.geoprism.graph.AttributeTypeSnapshot;
import net.geoprism.graph.ObjectTypeSnapshot;

public interface ObjectTypeSnapshotBusinessServiceIF<T extends ObjectTypeSnapshot>
{
  public List<AttributeTypeSnapshot> getAttributeTypes(T snapshot);
  
  public AttributeTypeSnapshot createAttributeTypeSnapshot(T type, AttributeType attributeType);
  
  public AttributeGeometryTypeSnapshot createAttributeTypeSnapshot(T snapshot, GeometryType geometryType);

  public void createGeometryAttribute(GeometryType geometryType, MdVertexDAO mdTableDAO);
  
  public MdAttributeConcrete createMdAttributeFromAttributeType(MdClass mdClass, AttributeType attributeType);

}
