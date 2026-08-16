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
package net.geoprism.registry.graph;

import com.runwaysdk.dataaccess.MdEdgeDAOIF;
import com.runwaysdk.dataaccess.metadata.graph.MdEdgeDAO;

import net.geoprism.registry.model.EdgeType;
import net.geoprism.registry.model.ServerElement;
import net.geoprism.registry.view.BusinessEdgeTypeDTO;
import net.geoprism.registry.view.TypeClass;

public class BusinessEdgeType extends BusinessEdgeTypeBase implements ServerElement, EdgeType
{
  @SuppressWarnings("unused")
  private static final long  serialVersionUID = -1808640970;

  public static final String JSON_LABEL       = "label";

  public BusinessEdgeType()
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

  public MdEdgeDAOIF getMdEdgeDAO()
  {
    return MdEdgeDAO.get(this.getMdEdgeOid());
  }

  @Override
  public GraphOrganization getOrganization()
  {
    return GraphOrganization.get((String) this.getObjectValue(BusinessEdgeType.ORGANIZATION));
  }

  @Override
  public BusinessEdgeTypeDTO toDTO()
  {
    final BusinessEdgeTypeDTO dto = new BusinessEdgeTypeDTO(this.getCode(), this.getLabel(), getDescriptionLV());
    dto.setChildType(this.getIsChildGeoObject() ? BusinessEdgeTypeDTO.GEO_OBJECT_TYPE : this.getChildType().getTypeName());
    dto.setParentType(this.getIsParentGeoObject() ? BusinessEdgeTypeDTO.GEO_OBJECT_TYPE : this.getParentType().getTypeName());

    return dto;
  }

  @Override
  public TypeClass getSourceType()
  {
    return this.getIsParentGeoObject() ? TypeClass.GEO_OBJECT_TYPE : TypeClass.BUSINESS_TYPE;
  }

  @Override
  public TypeClass getTargetType()
  {
    return this.getIsChildGeoObject() ? TypeClass.GEO_OBJECT_TYPE : TypeClass.BUSINESS_TYPE;
  }

}
