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
package net.geoprism.oda.driver.session;

import com.runwaysdk.ClientSession;
import com.runwaysdk.constants.ClientRequestIF;

public class ExistingClientSession implements IClientSession
{
  private String        url;

  private ClientSession session;

  public ExistingClientSession(String url, ClientSession session)
  {
    this.url = url;
    this.session = session;
  }

  @Override
  public ClientRequestIF getRequest()
  {
    return this.session.getRequest();
  }

  @Override
  public void logout()
  {
    // Do nothing this is an existing session
  }

  public String getKey()
  {
    return ExistingClientSession.buildKey(this.url, this.session.getSessionId());
  }

  public static String buildKey(String url, String sessionId)
  {
    return url + "-" + sessionId;
  }

}
