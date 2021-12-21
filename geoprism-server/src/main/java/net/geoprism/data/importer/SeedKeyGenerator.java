/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
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
package net.geoprism.data.importer;

import java.util.Random;

import com.runwaysdk.system.gis.geo.GeoEntity;

import net.geoprism.KeyGeneratorIF;

public class SeedKeyGenerator implements KeyGeneratorIF
{
  private Random random;

  public SeedKeyGenerator()
  {
    this.random = new Random();
  }

  @Override
  public Long next()
  {
    return this.random.nextLong();
  }

  @Override
  public String generateKey(String prefix)
  {
    String geoId = Long.toString(this.random.nextLong(), 36).toUpperCase();

    return GeoEntity.buildGeoIdFromCountryId(prefix, geoId);

  }

}
