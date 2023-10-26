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
package net.geoprism;

import org.springframework.context.ApplicationContext;

import com.runwaysdk.business.rbac.Authenticate;

import net.geoprism.registry.service.business.ForgotPasswordBusinessServiceIF;

public class ForgotPasswordAuthenticator extends ForgotPasswordAuthenticatorBase
{
  private static final long serialVersionUID = 1585492533;
  
  protected ApplicationContext applicationContext;
  
  public ForgotPasswordAuthenticator(ApplicationContext applicationContext)
  {
    super();
    this.applicationContext = applicationContext;
  }
  
  public ForgotPasswordAuthenticator()
  {
    super();
  }
  
  @Authenticate
  public void complete(java.lang.String token, java.lang.String newPassword)
  {
    this.applicationContext.getBean(ForgotPasswordBusinessServiceIF.class).complete(token, newPassword);
  }
  
  @Authenticate
  public void initiate(java.lang.String username)
  {
    this.applicationContext.getBean(ForgotPasswordBusinessServiceIF.class).initiate(username);
  }
}
