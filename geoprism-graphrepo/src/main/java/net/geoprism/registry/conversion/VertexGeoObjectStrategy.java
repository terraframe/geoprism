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
package net.geoprism.registry.conversion;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.commongeoregistry.adapter.constants.DefaultAttribute;
import org.commongeoregistry.adapter.dataaccess.GeoObject;
import org.commongeoregistry.adapter.dataaccess.GeoObjectOverTime;

import com.runwaysdk.business.graph.VertexObject;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;

import net.geoprism.registry.DataNotFoundException;
import net.geoprism.registry.graph.AttributeValue;
import net.geoprism.registry.graph.GeoVertex;
import net.geoprism.registry.model.GeoObjectMetadata;
import net.geoprism.registry.model.ServerGeoObjectType;
import net.geoprism.registry.model.graph.VertexServerGeoObject;
import net.geoprism.registry.query.ServerGeoObjectQuery;
import net.geoprism.registry.query.graph.VertexGeoObjectQuery;

public class VertexGeoObjectStrategy extends RegistryLocalizedValueConverter implements ServerGeoObjectStrategyIF
{
  private ServerGeoObjectType type;

  public VertexGeoObjectStrategy(ServerGeoObjectType type)
  {
    super();
    this.type = type;
  }

  @Override
  public ServerGeoObjectType getType()
  {
    return this.type;
  }

  @Override
  public VertexServerGeoObject constructFromGeoObject(GeoObject geoObject, boolean isNew)
  {
    if (!isNew)
    {
      VertexServerGeoObject vertex = getGeoObjectByUid(geoObject.getUid());

      if (vertex == null)
      {
        DataNotFoundException ex = new DataNotFoundException();
        ex.setTypeLabel(geoObject.getType().getLabel().getValue());
        ex.setAttributeLabel(GeoObjectMetadata.get().getAttributeDisplayLabel(DefaultAttribute.UID.getName()));
        ex.setDataIdentifier(geoObject.getUid());
        throw ex;
      }

      return vertex;
    }
    else
    {
      VertexObject vertex = VertexServerGeoObject.newInstance(type);

      return new VertexServerGeoObject(type, vertex, new TreeMap<>());
    }
  }

  @Override
  public VertexServerGeoObject constructFromGeoObjectOverTime(GeoObjectOverTime goTime, boolean isNew)
  {
    if (!isNew)
    {
      VertexServerGeoObject vertex = getGeoObjectByUid(goTime.getUid());

      if (vertex == null)
      {
        DataNotFoundException ex = new DataNotFoundException();
        ex.setTypeLabel(goTime.getType().getLabel().getValue());
        ex.setAttributeLabel(GeoObjectMetadata.get().getAttributeDisplayLabel(DefaultAttribute.UID.getName()));
        ex.setDataIdentifier(goTime.getUid());
        throw ex;
      }

      return vertex;
    }
    else
    {
      VertexObject vertex = VertexServerGeoObject.newInstance(type);

      return new VertexServerGeoObject(type, vertex, new TreeMap<>());
    }
  }

  @Override
  public VertexServerGeoObject constructFromDB(VertexObject dbObject)
  {
    List<VertexObject> rows = VertexServerGeoObject.getByRID(type, dbObject.getRID());

    return buildVertexServerGeoObject(rows);
  }

  @Override
  public VertexServerGeoObject getGeoObjectByCode(String code)
  {
    List<VertexObject> rows = VertexServerGeoObject.getVertexByCode(type, code);

    return buildVertexServerGeoObject(rows);
  }

  @Override
  public VertexServerGeoObject getGeoObjectByUid(String uid)
  {
    List<VertexObject> rows = VertexServerGeoObject.getVertex(type, uid);

    return buildVertexServerGeoObject(rows);
  }

  protected VertexServerGeoObject buildVertexServerGeoObject(List<VertexObject> rows)
  {
    if (rows.size() > 0)
    {
      Optional<VertexObject> vertex = rows.stream().filter(v -> {
        MdVertexDAOIF mdClass = (MdVertexDAOIF) v.getMdClass();

        return mdClass.getSuperClasses().contains(MdVertexDAO.getMdVertexDAO(GeoVertex.CLASS));
      }).findFirst();

      if (vertex.isPresent())
      {
        Map<String, List<VertexObject>> attributeNodeMap = rows.stream().filter(v -> {
          MdVertexDAOIF mdClass = (MdVertexDAOIF) v.getMdClass();

          return mdClass.getSuperClasses().contains(MdVertexDAO.getMdVertexDAO(AttributeValue.CLASS));
        }).collect(Collectors.groupingBy(v -> {
          return (String) v.getObjectValue(AttributeValue.ATTRIBUTENAME);
        }));

        return new VertexServerGeoObject(type, vertex.get(), attributeNodeMap);
      }
    }

    return null;
  }

  @Override
  public VertexServerGeoObject newInstance()
  {
    VertexObject vertex = VertexServerGeoObject.newInstance(type);

    return new VertexServerGeoObject(type, vertex, new TreeMap<>());
  }

  @Override
  public ServerGeoObjectQuery createQuery(Date date)
  {
    return new VertexGeoObjectQuery(type, date);
  }
}
