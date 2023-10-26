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
package net.geoprism.account;

import org.springframework.context.ApplicationContext;

import com.runwaysdk.business.rbac.Authenticate;

import net.geoprism.registry.service.business.ExternalProfileBusinessServiceIF;

public class ExternalProfileAuthenticator extends ExternalProfileAuthenticatorBase
{
  private static final long serialVersionUID = 1328523372;
  
  protected ApplicationContext applicationContext;
  
  public ExternalProfileAuthenticator(ApplicationContext applicationContext)
  {
    super();
    this.applicationContext = applicationContext;
  }
  
  public ExternalProfileAuthenticator()
  {
    super();
  }
  
  @Authenticate
  public String ologin(java.lang.String json)
  {
    return this.applicationContext.getBean(ExternalProfileBusinessServiceIF.class).ologin(json);
  }
}
