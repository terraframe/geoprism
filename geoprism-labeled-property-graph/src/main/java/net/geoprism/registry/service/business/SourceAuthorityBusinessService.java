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
package net.geoprism.registry.service.business;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.runwaysdk.business.graph.GraphQuery;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.session.Session;

import net.geoprism.registry.cache.TransactionLRUCache;
import net.geoprism.registry.conversion.LocalizedValueConverter;
import net.geoprism.registry.exception.RequiredSourceAuthorityException;
import net.geoprism.registry.graph.DataSource;
import net.geoprism.registry.graph.SourceAuthority;
import net.geoprism.registry.model.AuthorityType;
import net.geoprism.registry.model.SourceAuthorityDTO;

@Service
public class SourceAuthorityBusinessService implements SourceAuthorityBusinessServiceIF
{
  private final TransactionLRUCache<String, SourceAuthority> cache;

  public SourceAuthorityBusinessService()
  {
    this.cache = new TransactionLRUCache<String, SourceAuthority>("t-so-au-cache", (v) -> {
      return new String[] { v.getOid(), v.getCode(), v.getRID().toString() };
    });
  }

  @Override
  @Transaction
  public void delete(SourceAuthority source)
  {
    if (this.getUseCount(source) > 0)
    {
      RequiredSourceAuthorityException ex = new RequiredSourceAuthorityException();
      ex.setCode(source.getCode());

      throw ex;
    }

    this.cache.remove(source);

    source.delete();
  }

  @Override
  public SourceAuthorityDTO toDTO(SourceAuthority source)
  {
    SourceAuthorityDTO object = new SourceAuthorityDTO();
    object.setOid(source.getOid());
    object.setCode(source.getCode());
    object.setLabel(source.getLabel());
    object.setDescription(source.getDescriptionLV());

    if (StringUtils.isNotBlank(source.getAuthorityType()))
    {
      object.setAuthorityType(AuthorityType.valueOf(source.getAuthorityType()));
    }

    return object;
  }

  @Override
  @Transaction
  public SourceAuthority apply(SourceAuthorityDTO dto)
  {
    SourceAuthority source = null;

    if (StringUtils.isNotBlank(dto.getOid()))
    {
      source = SourceAuthority.get(dto.getOid());
    }
    else
    {
      source = new SourceAuthority();
    }

    source.setCode(dto.getCode());
    LocalizedValueConverter.populate(source, SourceAuthority.DISPLAYLABEL, dto.getLabel());
    LocalizedValueConverter.populate(source, SourceAuthority.DESCRIPTION, dto.getDescription());

    if (dto.getAuthorityType() != null)
    {
      source.setAuthorityType(dto.getAuthorityType().name());
    }

    return apply(source);
  }

  @Override
  @Transaction
  public SourceAuthority apply(SourceAuthority source)
  {
    source.apply();

    this.cache.put(source);

    return source;
  }

  @Override
  public Optional<SourceAuthority> getByCode(String code)
  {
    if (!StringUtils.isBlank(code))
    {
      return this.cache.get(code, () -> SourceAuthority.getByCode(code));
    }

    return Optional.empty();
  }

  @Override
  public SourceAuthority getByCodeOrThrow(String code)
  {
    return this.getByCode(code).orElseThrow(() -> {
      MdVertexDAOIF mdVertex = MdVertexDAO.getMdVertexDAO(SourceAuthority.CLASS);
      Locale locale = Session.getCurrentLocale();

      net.geoprism.registry.DataNotFoundException ex = new net.geoprism.registry.DataNotFoundException();
      ex.setTypeLabel(mdVertex.getDisplayLabel(locale));
      ex.setDataIdentifier(code);
      ex.setAttributeLabel(mdVertex.definesAttribute(SourceAuthority.CODE).getDisplayLabel(locale));

      return ex;
    });
  }

  @Override
  public Optional<SourceAuthority> getByRid(String rid)
  {
    if (!StringUtils.isBlank(rid))
    {
      return this.cache.get(rid, () -> SourceAuthority.getByRid(rid));
    }

    return Optional.empty();
  }

  @Override
  public Optional<SourceAuthority> get(String sourceOid)
  {
    if (!StringUtils.isBlank(sourceOid))
    {
      return this.cache.get(sourceOid, () -> Optional.ofNullable(SourceAuthority.get(sourceOid)));
    }

    return Optional.empty();
  }

  @Override
  public List<SourceAuthority> getAll()
  {
    MdVertexDAOIF mdVertex = MdVertexDAO.getMdVertexDAO(SourceAuthority.CLASS);
    MdAttributeDAOIF mdAttribute = mdVertex.definesAttribute(SourceAuthority.CODE);

    StringBuilder statement = new StringBuilder();
    statement.append("SELECT FROM " + mdVertex.getDBClassName());
    statement.append(" ORDER BY " + mdAttribute.getColumnName());

    GraphQuery<SourceAuthority> query = new GraphQuery<SourceAuthority>(statement.toString());

    return query.getResults();
  }

  @Override
  public List<SourceAuthority> search(String text)
  {
    MdVertexDAOIF mdVertex = MdVertexDAO.getMdVertexDAO(SourceAuthority.CLASS);
    MdAttributeDAOIF mdAttribute = mdVertex.definesAttribute(SourceAuthority.CODE);

    StringBuilder statement = new StringBuilder();
    statement.append("SELECT FROM " + mdVertex.getDBClassName());
    statement.append(" WHERE " + mdAttribute.getColumnName() + " LIKE :text");
    statement.append(" ORDER BY " + mdAttribute.getColumnName());

    GraphQuery<SourceAuthority> query = new GraphQuery<SourceAuthority>(statement.toString());
    query.setParameter("text", "%" + text + "%");

    return query.getResults();
  }

  @Override
  public Long getUseCount(SourceAuthority authority)
  {
    MdVertexDAOIF mdVertex = MdVertexDAO.getMdVertexDAO(DataSource.CLASS);
    MdAttributeDAOIF mdAttribute = mdVertex.definesAttribute(DataSource.AUTHORITY);

    StringBuilder statement = new StringBuilder();
    statement.append("SELECT COUNT(*) FROM " + mdVertex.getDBClassName());
    statement.append(" WHERE " + mdAttribute.getColumnName() + " = :authority");
    statement.append(" ORDER BY " + mdAttribute.getColumnName());

    GraphQuery<Long> query = new GraphQuery<Long>(statement.toString());
    query.setParameter("authority", authority.getRID());

    return query.getSingleResult();
  }

}
