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
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.commongeoregistry.adapter.constants.DefaultAttribute;
import org.commongeoregistry.adapter.constants.GeometryType;
import org.commongeoregistry.adapter.dataaccess.LocalizedValue;
import org.commongeoregistry.adapter.metadata.CustomSerializer;

import com.google.gson.JsonObject;
import com.runwaysdk.business.graph.GraphQuery;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;
import com.runwaysdk.dataaccess.transaction.TransactionState;
import com.runwaysdk.system.gis.geo.Universal;

import net.geoprism.registry.conversion.LocalizedValueConverter;
import net.geoprism.registry.graph.AttributeType;
import net.geoprism.registry.graph.BaseGeoObjectType;
import net.geoprism.registry.graph.GeoObjectType;
import net.geoprism.registry.service.request.ServiceFactory;

public class ServerGeoObjectType extends CachableObjectWrapper<BaseGeoObjectType> implements ServerElement
{
  // private Logger logger = LoggerFactory.getLogger(ServerLeafGeoObject.class);

  private org.commongeoregistry.adapter.metadata.GeoObjectType dto;

  private Map<String, AttributeType>                           attributes;

  public ServerGeoObjectType()
  {
    super();
  }

  public ServerGeoObjectType(GeoObjectType type)
  {
    super(type);
    this.attributes = type.getAttributeMap();
  }

  public ServerGeoObjectType(BaseGeoObjectType type, Map<String, AttributeType> attributes)
  {
    super(type);
    this.attributes = attributes;
  }

  protected Map<String, AttributeType> getAttributes()
  {
    // Ensure the object isn't dirty and up to date
    this.getType();

    return this.attributes;
  }
  
  @Override
  public void markAsDirty()
  {
    super.markAsDirty();
    
    this.dto = null;
  }

  @Override
  protected void refresh(BaseGeoObjectType object)
  {
    GeoObjectType type = GeoObjectType.getByCode(object.getCode());

    this.setObject(type);
    this.attributes = type.getAttributeMap();
    this.dto = null;
  }

  public String getOid()
  {
    return this.getObject().getOid();
  }

  public GeoObjectType getType()
  {
    return (GeoObjectType) this.getObject();
  }

  public String getCode()
  {
    return this.getObject().getCode();
  }

  public String getDBClassName()
  {
    return getMdVertex().getDBClassName();
  }

  public MdVertexDAOIF getMdVertex()
  {
    return MdVertexDAO.get(this.getObject().getObjectValue(GeoObjectType.MDVERTEX));
  }

  public GeometryType getGeometryType()
  {
    return GeometryType.valueOf(this.getType().getGeometryType());
  }

  public boolean isGeometryEditable()
  {
    return this.getType().getIsGeometryEditable();
  }

  public LocalizedValue getLabel()
  {
    return LocalizedValueConverter.convert(this.getObject().getEmbeddedComponent(GeoObjectType.LABEL));
  }

  public LocalizedValue getDescription()
  {
    return LocalizedValueConverter.convert(this.getObject().getEmbeddedComponent(GeoObjectType.DESCRIPTION));
  }

  public boolean getIsAbstract()
  {
    return this.getType().getIsAbstract();
  }

  public org.commongeoregistry.adapter.metadata.GeoObjectType toDTO()
  {
    synchronized (this)
    {
      if (this.dto == null)
      {
        this.dto = this.getType().toDTO();
      }
    }

    return this.dto;
  }

  public JsonObject toJSON(CustomSerializer serializer)
  {
    return this.toDTO().toJSON(serializer);
  }

  public Map<String, AttributeType> getAttributeMap()
  {
    return getAttributes();
  }

  public Optional<AttributeType> getAttribute(String name)
  {
    return Optional.ofNullable(getAttributes().get(name));
  }

  public List<AttributeType> definesAttributes()
  {
    return new LinkedList<AttributeType>(getAttributes().values());
  }

  /**
   * @return The organization associated with this GeoObjectType.
   */
  public ServerOrganization getOrganization()
  {
    return ServiceFactory.getMetadataCache().getOrganization(this.getOrganizationCode()).orElseThrow();
  }

  public String getOrganizationCode()
  {
    // Use the DTO to avoid a call to the database to get the organization
    // information
    return this.toDTO().getOrganizationCode();
  }

  public ServerGeoObjectType getSuperType()
  {
    String oid = this.getObject().getObjectValue(GeoObjectType.SUPERTYPE);

    if (!StringUtils.isBlank(oid))
    {
      return ServerGeoObjectType.getByOid(oid);
    }

    return null;
  }

  public boolean getIsPrivate()
  {
    return this.getType().getIsPrivate();
  }

  public void setIsPrivate(Boolean isPrivate)
  {
    this.getType().setIsPrivate(isPrivate);
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

  public static List<ServerGeoObjectType> getAll()
  {
    List<GeoObjectType> results = GeoObjectType.getAll();

    return results.stream().map(t -> new ServerGeoObjectType(t)).collect(Collectors.toList());
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
    AttributeType attributeType = getAttributes().get(attributeName);

    if (attributeType != null)
    {
      attributeType.delete();

      getAttributes().remove(attributeName);
    }
  }

  public void delete()
  {
    this.getObject().delete();
  }

  public List<ServerGeoObjectType> getSubTypes()
  {
    List<String> codes = this.getType().getSubTypeCodes();
    return codes.stream().map(code -> ServerGeoObjectType.get(code)).collect(Collectors.toList());
  }

}
