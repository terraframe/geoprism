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
package net.geoprism.registry.service.business;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import net.geoprism.registry.graph.SourceAuthority;
import net.geoprism.registry.model.SourceAuthorityDTO;

@Component
public interface SourceAuthorityBusinessServiceIF
{

  void delete(SourceAuthority source);

  SourceAuthorityDTO toDTO(SourceAuthority source);

  SourceAuthority apply(SourceAuthorityDTO object);

  SourceAuthority apply(SourceAuthority source);

  Optional<SourceAuthority> getByCode(String code);

  Optional<SourceAuthority> getByRid(String rid);

  Optional<SourceAuthority> get(String sourceOid);

  List<SourceAuthority> getAll();

  List<SourceAuthority> search(String text);

  Long getUseCount(SourceAuthority authority);

}
