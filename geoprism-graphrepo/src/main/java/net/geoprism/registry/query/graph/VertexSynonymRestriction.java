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
package net.geoprism.registry.query.graph;

import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import net.geoprism.registry.model.ServerGeoObjectIF;
import net.geoprism.registry.model.ServerGeoObjectType;
import net.geoprism.registry.model.ServerHierarchyType;
import net.geoprism.registry.service.business.GeoObjectTypeBusinessServiceIF;
import net.geoprism.registry.service.request.ServiceFactory;

public class VertexSynonymRestriction extends AbstractVertexRestriction implements VertexGeoObjectRestriction
{
  private ServerGeoObjectType type;

  private String              label;

  private ServerGeoObjectIF   parent;

  private ServerHierarchyType hierarchyType;

  private Date                date;

  public VertexSynonymRestriction(ServerGeoObjectType type, String label, Date date)
  {
    this.type = type;
    this.label = label;
    this.date = date;
    this.parent = null;
    this.hierarchyType = null;
  }

  public VertexSynonymRestriction(ServerGeoObjectType type, String label, Date date, ServerGeoObjectIF parent, ServerHierarchyType hierarchyType)
  {
    this.type = type;
    this.label = label;
    this.date = date;
    this.parent = parent;
    this.hierarchyType = hierarchyType;
  }

  @Override
  public void restrict(StringBuilder statement, Map<String, Object> parameters)
  {
    statement.append(",where: (code = :label");
    statement.append(" OR displayLabel_cot CONTAINS (:date BETWEEN startDate AND endDate AND " + localize("value") + " = :label)");
    statement.append(" OR out('geo_vertex_has_synonym').label CONTAINS :label)");

    parameters.put("label", this.label);
    parameters.put("date", this.date);

    if (this.parent != null && this.hierarchyType != null)
    {
      Set<String> edges = new TreeSet<String>();
      edges.add(this.hierarchyType.getObjectEdge().getDBClassName());

      ServerHierarchyType inheritedHierarchy = ServiceFactory.getBean(GeoObjectTypeBusinessServiceIF.class).findHierarchy(type, this.hierarchyType, this.parent.getType());

      if (inheritedHierarchy != null)
      {
        edges.add(inheritedHierarchy.getObjectEdge().getDBClassName());
      }

      statement.append("}.in(");

      int i = 0;

      for (String edge : edges)
      {
        if (i > 0)
        {
          statement.append(",");
        }

        statement.append("'" + edge + "'");

        i++;
      }

      statement.append("){where: (uid=:uid), while: (true)");

      parameters.put("uid", this.parent.getUid());

    }
  }
}
