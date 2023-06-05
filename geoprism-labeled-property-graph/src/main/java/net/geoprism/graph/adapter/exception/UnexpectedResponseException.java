/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Geoprism Registry(tm).
 *
 * Geoprism Registry(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Geoprism Registry(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Geoprism Registry(tm). If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism.graph.adapter.exception;

import net.geoprism.graph.adapter.response.RegistryResponse;

public class UnexpectedResponseException extends Exception
{

  private static final long serialVersionUID = -6010794787560014358L;

  private RegistryResponse  response;

  public UnexpectedResponseException(RegistryResponse response)
  {
    super(response.getJsonObject() != null && response.getJsonObject().has("message") ? response.getJsonObject().get("message").getAsString() : null);

    this.setResponse(response);
  }

  public UnexpectedResponseException(Throwable t, RegistryResponse response)
  {
    super(response.getJsonObject() != null && response.getJsonObject().has("message") ? response.getJsonObject().get("message").getAsString() : null);

    this.setResponse(response);
  }

  public UnexpectedResponseException()
  {
    super();
  }

  public UnexpectedResponseException(String message)
  {
    super(message);
  }

  public UnexpectedResponseException(String message, Throwable cause)
  {
    super(message, cause);
  }

  public UnexpectedResponseException(Throwable cause)
  {
    super(cause);
  }

  protected UnexpectedResponseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
  {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  public RegistryResponse getResponse()
  {
    return response;
  }

  public void setResponse(RegistryResponse response)
  {
    this.response = response;
  }

}
