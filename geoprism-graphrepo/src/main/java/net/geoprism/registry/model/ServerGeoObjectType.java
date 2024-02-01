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
package net.geoprism.registry.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang.StringUtils;
import org.commongeoregistry.adapter.constants.DefaultAttribute;
import org.commongeoregistry.adapter.constants.GeometryType;
import org.commongeoregistry.adapter.dataaccess.LocalizedValue;
import org.commongeoregistry.adapter.metadata.CustomSerializer;

import com.google.gson.JsonObject;
import com.runwaysdk.business.graph.GraphQuery;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeMultiTermDAOIF;
import com.runwaysdk.dataaccess.MdAttributeTermDAOIF;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.dataaccess.transaction.TransactionState;
import com.runwaysdk.system.gis.geo.Universal;

import net.geoprism.ontology.Classifier;
import net.geoprism.registry.conversion.LocalizedValueConverter;
import net.geoprism.registry.conversion.TermConverter;
import net.geoprism.registry.graph.AttributeType;
import net.geoprism.registry.graph.GeoObjectType;
import net.geoprism.registry.service.request.ServiceFactory;

public class ServerGeoObjectType implements ServerElement
{
  // private Logger logger = LoggerFactory.getLogger(ServerLeafGeoObject.class);

  private GeoObjectType              type;

  private Map<String, AttributeType> attributes;

  public ServerGeoObjectType()
  {
  }

  public ServerGeoObjectType(GeoObjectType type)
  {
    this.type = type;
    this.attributes = type.getAttributes();
  }

  public ServerGeoObjectType(GeoObjectType type, Map<String, AttributeType> attributes)
  {
    this.type = type;
    this.attributes = attributes;
  }

  public String getOid()
  {
    return this.type.getOid();
  }

  public GeoObjectType getType()
  {
    return type;
  }

  public void setType(GeoObjectType type)
  {
    this.type = type;
  }

  public String getCode()
  {
    return this.type.getCode();
  }

  public String getDBClassName()
  {
    return getMdVertex().getDBClassName();
  }

  public MdVertexDAOIF getMdVertex()
  {
    return MdVertexDAO.get(this.type.getObjectValue(GeoObjectType.MDVERTEX));
  }

  public GeometryType getGeometryType()
  {
    return GeometryType.valueOf(this.type.getGeometryType());
  }

  public boolean isGeometryEditable()
  {
    return this.type.getIsGeometryEditable();
  }

  public LocalizedValue getLabel()
  {
    return LocalizedValueConverter.convert(this.type.getEmbeddedComponent(GeoObjectType.LABEL));
  }

  public LocalizedValue getDescription()
  {
    return LocalizedValueConverter.convert(this.type.getEmbeddedComponent(GeoObjectType.DESCRIPTION));
  }

  public boolean getIsAbstract()
  {
    return this.type.getIsAbstract();
  }

  public org.commongeoregistry.adapter.metadata.GeoObjectType toDTO()
  {
    GeometryType cgrGeometryType = this.getGeometryType();

    ServerOrganization organization = this.getOrganization();

    ServerGeoObjectType superType = this.getSuperType();

    org.commongeoregistry.adapter.metadata.GeoObjectType type = new org.commongeoregistry.adapter.metadata.GeoObjectType(this.getCode(), cgrGeometryType, this.getLabel(), this.getDescription(), this.type.getIsGeometryEditable(), organization.getCode(), ServiceFactory.getAdapter());
    type.setIsAbstract(this.type.getIsAbstract());
    type.setIsPrivate(this.type.getIsPrivate());

    if (superType != null)
    {
      type.setSuperTypeCode(superType.getCode());
    }

    this.attributes.values().stream().forEach(attributeType -> {
      type.addAttribute(attributeType.toDTO());
    });

    // TODO: HEADS UP
    // try
    // {
    // GeoObjectTypeMetadata metadata =
    // GeoObjectTypeMetadata.getByKey(serverType.getUniversal().getKey());
    // metadata.injectDisplayLabels(type);
    // }
    // catch (DataNotFoundException | AttributeDoesNotExistException e)
    // {
    // }

    return type;
  }

  public JsonObject toJSON(CustomSerializer serializer)
  {
    return this.toDTO().toJSON(serializer);
  }

  public Map<String, AttributeType> getAttributeMap()
  {
    return this.attributes;
  }

  public Optional<AttributeType> getAttribute(String name)
  {
    return Optional.ofNullable(this.attributes.get(name));
  }

  public List<AttributeType> definesAttributes()
  {
    return new LinkedList<AttributeType>(this.attributes.values());
  }

  /**
   * @return The organization associated with this GeoObjectType.
   */
  public ServerOrganization getOrganization()
  {
    return ServerOrganization.getByGraphId(this.type.getObjectValue(GeoObjectType.ORGANIZATION));
  }

  public String getOrganizationCode()
  {
    return this.getOrganization().getCode();
  }

  public ServerGeoObjectType getSuperType()
  {
    String oid = this.type.getObjectValue(GeoObjectType.SUPERTYPE);

    if (!StringUtils.isBlank(oid))
    {
      return ServerGeoObjectType.getByOid(oid);
    }

    return null;
  }

  public boolean getIsPrivate()
  {
    return this.type.getIsPrivate();
  }

  public void setIsPrivate(Boolean isPrivate)
  {
    this.type.setIsPrivate(isPrivate);
  }

  @Override
  public boolean equals(Object obj)
  {
    if (obj instanceof ServerGeoObjectType)
    {
      return ( (ServerGeoObjectType) obj ).getCode().equals(this.getCode());
    }
    else if (obj instanceof GeoObjectType)
    {
      return ( (GeoObjectType) obj ).getCode().equals(this.getCode());
    }

    return false;
  }

  @Override
  public int hashCode()
  {
    return this.getCode().hashCode();
  }

  @Override
  public String toString()
  {
    return GeoObjectTypeMetadata.sGetClassDisplayLabel() + " : " + this.getCode();
  }

  public static ServerGeoObjectType get(MdVertexDAOIF mdVertex)
  {
    MdVertexDAOIF metadata = MdVertexDAO.getMdVertexDAO(GeoObjectType.CLASS);

    StringBuilder statement = new StringBuilder();
    statement.append("SELECT FROM " + metadata.getDBClassName());
    statement.append(" WHERE " + metadata.definesAttribute(GeoObjectType.MDVERTEX).getColumnName() + " = :oid");

    GraphQuery<GeoObjectType> query = new GraphQuery<GeoObjectType>(statement.toString());
    query.setParameter("oid", mdVertex.getOid());

    GeoObjectType result = query.getSingleResult();

    if (result != null)
    {
      return ServerGeoObjectType.getByOid(result.getOid());
    }

    return null;
  }

  public static ServerGeoObjectType get(String code)
  {
    return ServerGeoObjectType.get(code, false);
  }

  @SuppressWarnings("unchecked")
  public static ServerGeoObjectType get(String code, boolean nullIfNotFound)
  {
    if (code == null || code.equals(Universal.ROOT))
    {
      return RootGeoObjectType.INSTANCE;
    }

    TransactionState state = TransactionState.getCurrentTransactionState();

    if (state != null)
    {
      Object transactionCache = state.getTransactionObject("transaction-state");

      if (transactionCache != null)
      {
        Map<String, ServerElement> cache = (Map<String, ServerElement>) transactionCache;
        ServerElement element = cache.get(code);

        if (element != null && element instanceof ServerGeoObjectType)
        {
          return (ServerGeoObjectType) element;
        }
      }
    }

    Optional<ServerGeoObjectType> geoObjectType = ServiceFactory.getMetadataCache().getGeoObjectType(code);

    if (geoObjectType.isPresent())
    {
      return geoObjectType.get();
    }
    else if (!nullIfNotFound)
    {
      net.geoprism.registry.DataNotFoundException ex = new net.geoprism.registry.DataNotFoundException();
      ex.setTypeLabel(GeoObjectTypeMetadata.sGetClassDisplayLabel());
      ex.setDataIdentifier(code);
      ex.setAttributeLabel(GeoObjectTypeMetadata.getAttributeDisplayLabel(DefaultAttribute.CODE.getName()));
      throw ex;
    }

    return null;
  }

  public static ServerGeoObjectType get(GeoObjectType geoObjectType)
  {
    String code = geoObjectType.getCode();

    return getFromCache(code);
  }

  @SuppressWarnings("unchecked")
  public static ServerGeoObjectType getByOid(String oid)
  {
    TransactionState state = TransactionState.getCurrentTransactionState();

    if (state != null)
    {
      Object transactionCache = state.getTransactionObject("transaction-state");

      if (transactionCache != null)
      {
        Map<String, ServerElement> cache = (Map<String, ServerElement>) transactionCache;

        Optional<ServerGeoObjectType> optional = cache.values().stream().filter(e -> e instanceof ServerGeoObjectType).map(e -> (ServerGeoObjectType) e).filter(e -> e.getOid().equals(oid)).findFirst();

        if (optional.isPresent())
        {
          return optional.get();
        }
      }
    }

    return ServiceFactory.getMetadataCache().getGeoObjectTypeByOid(oid).orElseThrow(() -> new ProgrammingErrorException("Unknown Geo Object Type with oid [" + oid + "]"));
  }

  @SuppressWarnings("unchecked")
  private static ServerGeoObjectType getFromCache(String code)
  {
    TransactionState state = TransactionState.getCurrentTransactionState();

    if (state != null)
    {
      Object transactionCache = state.getTransactionObject("transaction-state");

      if (transactionCache != null)
      {
        Map<String, ServerElement> cache = (Map<String, ServerElement>) transactionCache;
        ServerElement element = cache.get(code);

        if (element != null && element instanceof ServerGeoObjectType)
        {
          return (ServerGeoObjectType) element;
        }
      }
    }

    return ServiceFactory.getMetadataCache().getGeoObjectType(code).get();
  }

  public synchronized void removeAttribute(String attributeName)
  {
    AttributeType attributeType = this.attributes.get(attributeName);

    if (attributeType != null)
    {
      attributeType.delete();

      this.attributes.remove(attributeName);
    }

  }

}
