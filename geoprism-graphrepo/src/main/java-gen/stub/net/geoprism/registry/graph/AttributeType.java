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
package net.geoprism.registry.graph;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.commongeoregistry.adapter.dataaccess.LocalizedValue;

import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.runwaysdk.constants.IndexTypes;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeConcreteInfo;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.graph.GraphDBService;
import com.runwaysdk.dataaccess.graph.orientdb.OrientDBRequest;
import com.runwaysdk.dataaccess.metadata.MdAttributeConcreteDAO;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.system.metadata.MdVertex;

import net.geoprism.registry.conversion.LocalizedValueConverter;
import net.geoprism.registry.model.EdgeConstant;
import net.geoprism.registry.model.ServerGeoObjectType;
import net.geoprism.registry.model.ValueStrategy;

public abstract class AttributeType extends AttributeTypeBase
{
  @SuppressWarnings("unused")
  private static final long serialVersionUID = -1935054848;

  public AttributeType()
  {
    super();

    this.setIsVirtual(false);
  }

  public abstract org.commongeoregistry.adapter.metadata.AttributeType toDTO();

  public abstract ValueStrategy getStrategy();

  @Override
  public ObjectClass getObjectType()
  {
    String oid = this.getObjectValue(OBJECTTYPE);

    return ObjectClass.get(oid);
  }

  public LocalizedValue getLocalizedLabel()
  {
    return LocalizedValueConverter.convert(this.getEmbeddedComponent(LABEL));
  }

  public LocalizedValue getLocalizedDescription()
  {
    return LocalizedValueConverter.convert(this.getEmbeddedComponent(DESCRIPTION));
  }

  protected void populate(org.commongeoregistry.adapter.metadata.AttributeType dto)
  {
    dto.setUnique(this.getUnique());
    dto.setRequired(this.getRequired());
    dto.setChangeOverTime(this.getIsChangeOverTime());
    dto.setTypeInfo(this.getObjectType().getTypeInfo());
  }

  protected void populate(MdAttributeConcreteDAO mdAttribute)
  {
    ObjectClass type = this.getObjectType();
    MdVertex mdVertex = type.getMdVertex();

    mdAttribute.setValue(MdAttributeConcreteInfo.NAME, this.getCode());
    LocalizedValueConverter.populate(mdAttribute, MdAttributeBooleanInfo.DISPLAY_LABEL, this.getLocalizedLabel());
    LocalizedValueConverter.populate(mdAttribute, MdAttributeBooleanInfo.DESCRIPTION, this.getLocalizedDescription());
    mdAttribute.setValue(MdAttributeConcreteInfo.DEFINING_MD_CLASS, mdVertex.getOid());
    mdAttribute.setValue(MdAttributeConcreteInfo.REQUIRED, this.getRequired());

    if (this.getUnique())
    {
      mdAttribute.addItem(MdAttributeConcreteInfo.INDEX_TYPE, IndexTypes.UNIQUE_INDEX.getOid());
    }
  }

  public void fromDTO(org.commongeoregistry.adapter.metadata.AttributeType dto)
  {
    this.setCode(dto.getCode());
    this.setRequired(dto.isRequired());
    this.setUnique(dto.isUnique());
    this.setIsChangeOverTime(dto.isChangeOverTime());

    LocalizedValueConverter.populate(this, AttributeType.LABEL, dto.getLabel());
    LocalizedValueConverter.populate(this, AttributeType.DESCRIPTION, dto.getDescription());
  }

  public List<MdAttributeDAOIF> getValueAttributes(ServerGeoObjectType type)
  {
    return this.getStrategy().getValueAttributes();
  }

  public List<String> getColumnNames(ServerGeoObjectType type)
  {
    return this.getValueAttributes(type).stream().map(mdAttribute -> mdAttribute.getColumnName()).collect(Collectors.toList());
  }

  /**
   * @return The edge which links an object vertex to the value nodes of this
   *         attribute when the attribute changes over time
   */
  protected EdgeConstant getValueEdge()
  {
    return EdgeConstant.HAS_VALUE;
  }

  @Override
  @Transaction
  public void delete()
  {
    if (this.getIsChangeOverTime() && !this.getIsVirtual())
    {
      this.deleteValueNodes();
    }

    super.delete();
  }

  /**
   * Deletes all of the value nodes of this attribute, along with their incoming
   * edges, using a single server side command. The delete is scoped by
   * traversing from the vertex class of the owning type (polymorphic, so
   * objects of sub types are included) because attribute codes are not unique
   * across types.
   */
  protected void deleteValueNodes()
  {
    MdVertexDAOIF ownerVertex = MdVertexDAO.get(this.getObjectType().getMdVertexOid());

    StringBuilder statement = new StringBuilder();
    statement.append("DELETE VERTEX FROM (");
    statement.append(" SELECT expand(out('" + this.getValueEdge().getDBClassName() + "')[attributeName = :code])");
    statement.append(" FROM " + ownerVertex.getDBClassName());
    statement.append(")");

    GraphDBService service = GraphDBService.getInstance();
    OrientDBRequest request = (OrientDBRequest) service.getGraphDBRequest();

    service.command(request, statement.toString(), Map.of("code", this.getCode()));

    // Deleting through a direct SQL command bypasses the java abstraction and
    // can leave stale entities in the L1 cache. So we need to clear the cache
    ODatabaseSession session = request.getODatabaseSession();
    session.getLocalCache().clear();
  }

}
