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
package net.geoprism.registry.service.request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.runwaysdk.session.Request;
import com.runwaysdk.session.RequestType;

import net.geoprism.ForgotPasswordAuthenticator;
import net.geoprism.configuration.GeoprismProperties;

@Service
public class ForgotPasswordService implements ForgotPasswordServiceIF
{
  private static Logger logger = LoggerFactory.getLogger(ForgotPasswordService.class);
  
  protected static final int expireTime = GeoprismProperties.getForgotPasswordExpireTime(); // in hours
  
  @Autowired
  protected ApplicationContext applicationContext;
  
  @Request(RequestType.SESSION)
  public void complete(String sessionId, String token, String username)
  {
    new ForgotPasswordAuthenticator(applicationContext).complete(token, username);
  }
  
  @Request(RequestType.SESSION)
  public void initiate(String sessionId, String username)
  {
    new ForgotPasswordAuthenticator(applicationContext).initiate(username);
  }
}
