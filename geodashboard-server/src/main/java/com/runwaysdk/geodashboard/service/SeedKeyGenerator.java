package com.runwaysdk.geodashboard.service;

import java.util.Random;

import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.geodashboard.KeyGeneratorIF;
import com.runwaysdk.system.gis.geo.GeoEntity;

public class SeedKeyGenerator implements KeyGeneratorIF, Reloadable
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
