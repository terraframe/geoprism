/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
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
package net.geoprism;

import java.io.File;

import com.runwaysdk.configuration.ConfigurationManager;
import com.runwaysdk.configuration.ConfigurationReaderIF;

import net.geoprism.configuration.GeoprismConfigGroup;


public class GeoprismProperties
{
  private ConfigurationReaderIF props;

  private static class Singleton 
  {
    private static GeoprismProperties INSTANCE = new GeoprismProperties();
  }

  public GeoprismProperties()
  {
    this.props = ConfigurationManager.getReader(GeoprismConfigGroup.COMMON, "geoprism.properties");
  }

  public static int getInviteUserTokenExpireTime()
  {
    return Singleton.INSTANCE.props.getInteger("invite.user.token.expire.time", 72);
  }
  
  public static String getEmailFrom()
  {
    return Singleton.INSTANCE.props.getString("email.from");
  }

  public static String getEmailTo()
  {
    return Singleton.INSTANCE.props.getString("email.to");
  }

  public static String getEmailUsername()
  {
    return Singleton.INSTANCE.props.getString("email.username");
  }

  public static String getEmailPassword()
  {
    return Singleton.INSTANCE.props.getString("email.password");
  }

  public static String getEmailServer()
  {
    return Singleton.INSTANCE.props.getString("email.server");
  }

  public static Integer getEmailPort()
  {
    return Singleton.INSTANCE.props.getInteger("email.port");
  }

  public static Integer getForgotPasswordExpireTime()
  {
    return Singleton.INSTANCE.props.getInteger("forgotPassword.expireTime", 2);
  }

  public static Boolean getEncrypted()
  {
    return Singleton.INSTANCE.props.getBoolean("email.encrypted");
  }

  public static int getClassifierCacheSize()
  {
    return Singleton.INSTANCE.props.getInteger("classifier.cache.size", 10);
  }

  public static boolean getSolrLookup()
  {
    return Singleton.INSTANCE.props.getBoolean("solr.lookup", false);
  }
  
  /**
   * Returns the root directory of a filesystem location which is suitable for long-term
   * storage of files that will be preserved through patches, upgrades and reboots.
   */
  public static File getGeoprismFileStorage()
  {
    String geoprismVolume = Singleton.INSTANCE.props.getString("geoprism.volume", "/opt/geoprism/misc");
    
    File fGeoprismVolume = new File(geoprismVolume);
    
    if (!fGeoprismVolume.exists())
    {
      fGeoprismVolume.mkdirs();
    }
    
    return fGeoprismVolume;
  }
}
