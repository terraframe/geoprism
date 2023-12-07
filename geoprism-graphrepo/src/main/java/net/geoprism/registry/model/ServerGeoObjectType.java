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
package net.geoprism.registry.model;

import java.util.List;
import java.util.Map;

import org.commongeoregistry.adapter.Optional;
import org.commongeoregistry.adapter.Term;
import org.commongeoregistry.adapter.constants.DefaultAttribute;
import org.commongeoregistry.adapter.constants.GeometryType;
import org.commongeoregistry.adapter.dataaccess.LocalizedValue;
import org.commongeoregistry.adapter.metadata.AttributeBooleanType;
import org.commongeoregistry.adapter.metadata.AttributeCharacterType;
import org.commongeoregistry.adapter.metadata.AttributeClassificationType;
import org.commongeoregistry.adapter.metadata.AttributeDateType;
import org.commongeoregistry.adapter.metadata.AttributeFloatType;
import org.commongeoregistry.adapter.metadata.AttributeIntegerType;
import org.commongeoregistry.adapter.metadata.AttributeLocalType;
import org.commongeoregistry.adapter.metadata.AttributeTermType;
import org.commongeoregistry.adapter.metadata.AttributeType;
import org.commongeoregistry.adapter.metadata.CustomSerializer;
import org.commongeoregistry.adapter.metadata.GeoObjectType;
import org.commongeoregistry.adapter.metadata.RegistryRole;

import com.google.gson.JsonObject;
import com.runwaysdk.business.BusinessFacade;
import com.runwaysdk.constants.MdAttributeCharacterInfo;
import com.runwaysdk.constants.MdAttributeConcreteInfo;
import com.runwaysdk.constants.MdAttributeDoubleInfo;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.dataaccess.transaction.TransactionState;
import com.runwaysdk.system.Actor;
import com.runwaysdk.system.Roles;
import com.runwaysdk.system.gis.geo.Universal;
import com.runwaysdk.system.metadata.MdAttributeBoolean;
import com.runwaysdk.system.metadata.MdAttributeCharacter;
import com.runwaysdk.system.metadata.MdAttributeClassification;
import com.runwaysdk.system.metadata.MdAttributeConcrete;
import com.runwaysdk.system.metadata.MdAttributeDateTime;
import com.runwaysdk.system.metadata.MdAttributeDouble;
import com.runwaysdk.system.metadata.MdAttributeIndices;
import com.runwaysdk.system.metadata.MdAttributeLocalCharacterEmbedded;
import com.runwaysdk.system.metadata.MdAttributeLocalText;
import com.runwaysdk.system.metadata.MdAttributeLong;
import com.runwaysdk.system.metadata.MdAttributeTerm;
import com.runwaysdk.system.metadata.MdBusiness;
import com.runwaysdk.system.metadata.MdClass;
import com.runwaysdk.system.metadata.MdGraphClass;

import net.geoprism.ontology.Classifier;
import net.geoprism.registry.Organization;
import net.geoprism.registry.conversion.RegistryLocalizedValueConverter;
import net.geoprism.registry.conversion.TermConverter;
import net.geoprism.registry.service.request.ServiceFactory;

public class ServerGeoObjectType implements ServerElement
{
  // private Logger logger = LoggerFactory.getLogger(ServerLeafGeoObject.class);

  GeoObjectType type;

  private Universal     universal;

  private MdBusiness    mdBusiness;

  private MdVertexDAOIF mdVertex;

  public ServerGeoObjectType(GeoObjectType go, Universal universal, MdBusiness mdBusiness, MdVertexDAOIF mdVertex)
  {
    this.type = go;
    this.universal = universal;
    this.mdBusiness = mdBusiness;
    this.mdVertex = mdVertex;
  }

  public GeoObjectType getType()
  {
    return type;
  }
  
  public void setType(GeoObjectType type)
  {
    this.type = type;
  }

  public Universal getUniversal()
  {
    return universal;
  }

  public void setUniversal(Universal universal)
  {
    this.universal = universal;
  }

  public MdBusiness getMdBusiness()
  {
    return mdBusiness;
  }

  public MdBusinessDAOIF getMdBusinessDAO()
  {
    return (MdBusinessDAOIF) BusinessFacade.getEntityDAO(this.mdBusiness);
  }

  public void setMdBusiness(MdBusiness mdBusiness)
  {
    this.mdBusiness = mdBusiness;
  }

  public MdVertexDAOIF getMdVertex()
  {
    return mdVertex;
  }

  public void setMdVertex(MdVertexDAOIF mdVertex)
  {
    this.mdVertex = mdVertex;
  }

  public String getCode()
  {
    return this.type.getCode();
  }

  public GeometryType getGeometryType()
  {
    return this.type.getGeometryType();
  }

  public boolean isGeometryEditable()
  {
    return this.type.isGeometryEditable();
  }

  public LocalizedValue getLabel()
  {
    return this.type.getLabel();
  }

  public LocalizedValue getDescription()
  {
    return this.type.getDescription();
  }
  
  public boolean getIsAbstract()
  {
    return this.type.getIsAbstract();
  }

  public JsonObject toJSON(CustomSerializer serializer)
  {
    return this.type.toJSON(serializer);
  }

  public Map<String, AttributeType> getAttributeMap()
  {
    return this.type.getAttributeMap();
  }

  public Optional<AttributeType> getAttribute(String name)
  {
    return this.type.getAttribute(name);
  }

  public String definesType()
  {
    return this.mdBusiness.definesType();
  }

  public List<? extends MdAttributeConcreteDAOIF> definesAttributes()
  {
    return this.getMdBusinessDAO().definesAttributes();
  }

  public void deleteAllRecords()
  {
    this.getMdBusinessDAO().getBusinessDAO().deleteAllRecords();
  }

  public GeoObjectTypeMetadata getMetadata()
  {
    return GeoObjectTypeMetadata.getByKey(this.universal.getKey());
  }

  /**
   * @return The organization associated with this GeoObjectType.
   */
  public Organization getOrganization()
  {
    Actor owner = this.universal.getOwner();

    if (! ( owner instanceof Roles ))
    {
      return null; // If we get here, then the GeoObjectType was not created
                   // correctly.
    }
    else
    {
      Roles uniRole = (Roles) owner;
      String myOrgCode = RegistryRole.Type.parseOrgCode(uniRole.getRoleName());

      return Organization.getByCode(myOrgCode);
    }
  }

  public String getOrganizationCode()
  {
    return this.getOrganization().getCode();
  }

  public ServerGeoObjectType getSuperType()
  {
    if (this.type.getSuperTypeCode() != null && this.type.getSuperTypeCode().length() > 0)
    {
      return ServerGeoObjectType.get(this.type.getSuperTypeCode());
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

  /**
   * Returns a {@link Universal} from the code value on the given
   * {@link GeoObjectType}.
   * 
   * @param got
   * @return a {@link Universal} from the code value on the given
   *         {@link GeoObjectType}.
   */
  public static Universal geoObjectTypeToUniversal(GeoObjectType got)
  {
    return Universal.getByKey(got.getCode());
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

  public static ServerGeoObjectType get(Universal universal)
  {
    String code = universal.getKey();

    return getFromCache(code);
  }

  public static ServerGeoObjectType get(GeoObjectType geoObjectType)
  {
    String code = geoObjectType.getCode();

    return getFromCache(code);
  }

  public static ServerGeoObjectType get(MdVertexDAOIF mdVertex)
  {
    String code = mdVertex.getTypeName();

    return getFromCache(code);
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

  // public String buildRMRoleName()
  // {
  // String ownerActorOid = this.universal.getOwnerOid();
  // Organization.getRootOrganization(ownerActorOid)
  // }

}
