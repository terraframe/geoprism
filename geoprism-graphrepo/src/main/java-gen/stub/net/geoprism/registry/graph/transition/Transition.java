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
package net.geoprism.registry.graph.transition;

import java.util.TreeMap;

import com.google.gson.JsonObject;
import com.runwaysdk.business.graph.GraphQuery;
import com.runwaysdk.business.graph.VertexObject;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;

import net.geoprism.registry.conversion.VertexGeoObjectStrategy;
import net.geoprism.registry.model.ServerGeoObjectType;
import net.geoprism.registry.model.graph.VertexServerGeoObject;
import net.geoprism.registry.service.business.TransitionBusinessServiceIF;
import net.geoprism.registry.service.request.ServiceFactory;

public class Transition extends TransitionBase
{
  private static final long serialVersionUID = 1506268214;

  public static enum TransitionImpact {
    PARTIAL, FULL;
  }

  public static enum TransitionType {
    MERGE, SPLIT, REASSIGN, UPGRADE_MERGE, UPGRADE_SPLIT, UPGRADE, DOWNGRADE_MERGE, DOWNGRADE_SPLIT, DOWNGRADE;

    public boolean isReassign()
    {
      return this.equals(TransitionType.REASSIGN) || this.equals(TransitionType.UPGRADE) || this.equals(TransitionType.DOWNGRADE);
    }

    public boolean isMerge()
    {
      return this.equals(TransitionType.MERGE) || this.equals(TransitionType.UPGRADE_MERGE) || this.equals(TransitionType.DOWNGRADE_MERGE);
    }

    public boolean isSplit()
    {
      return this.equals(TransitionType.SPLIT) || this.equals(TransitionType.UPGRADE_SPLIT) || this.equals(TransitionType.DOWNGRADE_SPLIT);
    }
  }

  public Transition()
  {
    super();
  }

  public JsonObject toJSON()
  {
    VertexServerGeoObject source = this.getSourceVertex();
    VertexServerGeoObject target = this.getTargetVertex();

    JsonObject object = new JsonObject();
    object.addProperty(OID, this.getOid());
    object.addProperty(Transition.ORDER, this.getOrder());
    object.addProperty("isNew", this.isNew());
    object.addProperty("sourceCode", source.getCode());
    object.addProperty("sourceType", source.getType().getCode());
    object.addProperty("sourceText", source.getLabel() + " (" + source.getCode() + ")");
    object.addProperty("targetCode", target.getCode());
    object.addProperty("targetType", target.getType().getCode());
    object.addProperty("targetText", target.getLabel() + " (" + target.getCode() + ")");
    object.addProperty(Transition.TRANSITIONTYPE, this.getTransitionType());
    object.addProperty(Transition.IMPACT, this.getImpact());

    return object;
  }

  public void setTransitionType(TransitionType value)
  {
    this.setTransitionType(value.name());
  }

  public TransitionType toTransitionType()
  {
    return TransitionType.valueOf(this.getTransitionType());
  }

  public void setImpact(TransitionImpact value)
  {
    this.setImpact(value.name());
  }

  public VertexServerGeoObject getSourceVertex()
  {
    return getVertex(SOURCE);
  }

  public VertexServerGeoObject getTargetVertex()
  {
    return getVertex(TARGET);
  }

  private VertexServerGeoObject getVertex(String attributeName)
  {
    MdVertexDAOIF mdVertex = MdVertexDAO.getMdVertexDAO(Transition.CLASS);
    MdAttributeDAOIF mdAttribute = mdVertex.definesAttribute(attributeName);

    StringBuilder statement = new StringBuilder();
    statement.append("TRAVERSE out('has_value', 'has_geometry') FROM (");
    statement.append(" SELECT expand(" + mdAttribute.getColumnName() + ")");
    statement.append("   FROM :parent");
    statement.append(")");

    GraphQuery<VertexObject> query = new GraphQuery<VertexObject>(statement.toString());
    query.setParameter("parent", this.getRID());

    VertexObject vertex = query.getSingleResult();
    MdVertexDAOIF geoVertex = (MdVertexDAOIF) vertex.getMdClass();

    ServerGeoObjectType type = ServerGeoObjectType.get(geoVertex);

    return new VertexServerGeoObject(type, vertex, new TreeMap<>());
  }

  public static Transition apply(TransitionEvent event, int order, JsonObject object)
  {
    Transition transition = ( object.has("isNew") && object.get("isNew").getAsBoolean() ) ? new Transition() : Transition.get(object.get(OID).getAsString());
    transition.setTransitionType(object.get(Transition.TRANSITIONTYPE).getAsString());
    transition.setImpact(object.get(Transition.IMPACT).getAsString());

    if (transition.isNew())
    {
      transition.setValue(Transition.EVENT, event);
    }

    String sourceCode = object.get("sourceCode").getAsString();
    String sourceType = object.get("sourceType").getAsString();

    String targetCode = object.get("targetCode").getAsString();
    String targetType = object.get("targetType").getAsString();

    VertexServerGeoObject source = new VertexGeoObjectStrategy(ServerGeoObjectType.get(sourceType)).getGeoObjectByCode(sourceCode);
    VertexServerGeoObject target = new VertexGeoObjectStrategy(ServerGeoObjectType.get(targetType)).getGeoObjectByCode(targetCode);

    ServiceFactory.getBean(TransitionBusinessServiceIF.class).apply(transition, event, order, source, target);

    return transition;
  }
}
