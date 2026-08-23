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
package net.geoprism.registry.view;

import org.commongeoregistry.adapter.dataaccess.LocalizedValue;

public class ConceptEdgeTypeDTO extends EdgeClassDTO
{
  private static final long serialVersionUID = -7643300715090494998L;

  private DiscreteType      discreteType;

  public ConceptEdgeTypeDTO()
  {
    super();
  }

  public ConceptEdgeTypeDTO(String code, LocalizedValue label, LocalizedValue description)
  {
    super(TypeClass.CONCEPT_EDGE.getCode(), code, label, description);
  }

  public DiscreteType getDiscreteType()
  {
    return discreteType;
  }

  public void setDiscreteType(DiscreteType discreteType)
  {
    this.discreteType = discreteType;
  }

  public String getType()
  {
    return TypeClass.CONCEPT_EDGE.getCode();
  }

  public static ConceptEdgeTypeDTO build(String organizationCode, String code, String parentTypeCode, String childTypeCode, DiscreteType discreteType)
  {
    ConceptEdgeTypeDTO dto = new ConceptEdgeTypeDTO();
    dto.setCode(code);
    dto.setParentType(parentTypeCode);
    dto.setChildType(childTypeCode);
    dto.setLabel(new LocalizedValue(code));
    dto.setDescription(new LocalizedValue(code));
    dto.setOrganizationCode(organizationCode);
    dto.setDiscreteType(discreteType);
    dto.setSeq(0L);

    return dto;
  }

  public static ConceptEdgeTypeDTO build(String organizationCode, String code, LocalizedValue label, LocalizedValue description, String parentTypeCode, String childTypeCode, DiscreteType discreteType)
  {
    ConceptEdgeTypeDTO dto = new ConceptEdgeTypeDTO();
    dto.setCode(code);
    dto.setParentType(parentTypeCode);
    dto.setChildType(childTypeCode);
    dto.setLabel(label);
    dto.setDescription(description);
    dto.setOrganizationCode(organizationCode);
    dto.setSeq(0L);
    dto.setDiscreteType(discreteType);

    return dto;
  }
}
