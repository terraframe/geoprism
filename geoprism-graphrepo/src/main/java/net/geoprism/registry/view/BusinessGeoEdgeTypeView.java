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

import net.geoprism.registry.model.EdgeDirection;

public class BusinessGeoEdgeTypeView
{
  public static BusinessEdgeTypeView build(String organizationCode, String code, LocalizedValue label, LocalizedValue description, String typeCode, EdgeDirection direction)
  {
    BusinessEdgeTypeView view = new BusinessEdgeTypeView();
    view.setCode(code);
    view.setLabel(label);
    view.setDescription(description);
    view.setOrganizationCode(organizationCode);

    if (direction.equals(EdgeDirection.PARENT))
    {
      view.setParentTypeCode(BusinessEdgeTypeView.GEO_OBJECT_TYPE);
      view.setChildTypeCode(typeCode);
    }
    else
    {
      view.setChildTypeCode(BusinessEdgeTypeView.GEO_OBJECT_TYPE);
      view.setParentTypeCode(typeCode);
    }
    view.setSeq(0L);

    return view;
  }

}
