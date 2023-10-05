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
package net.geoprism.graph.lpg.adapter.response;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

public class RegistryResponse
{
  protected String response;
  
  protected int statusCode;
  
  public RegistryResponse(String response, int statusCode)
  {
    this.response = response;
    this.statusCode = statusCode;
  }
  
  public boolean isSuccess()
  {
//    final int[] successCodes = new int[] {200, 201, 202, 203, 204, 205, 206, 207, 208, 226};
//    
//    boolean isSuccessCode = Arrays.stream(successCodes).anyMatch(new Integer(this.getStatusCode())::equals);
//    
//    return isSuccessCode;
    
    return this.getStatusCode() < 300;
  }

  public JsonObject getJsonObject()
  {
    if (this.response == null)
    {
      return null;
    }
    
    try
    {
      return JsonParser.parseString(response).getAsJsonObject();
    }
    catch (JsonParseException | IllegalStateException e)
    {
      return null;
    }
  }
  
  public JsonArray getJsonArray()
  {
    if (this.response == null)
    {
      return null;
    }
    
    try
    {
      return JsonParser.parseString(response).getAsJsonArray();
    }
    catch (JsonParseException e)
    {
      return null;
    }
  }

  public String getResponse()
  {
    return this.response;
  }
  
  public void setResponse(String response)
  {
    this.response = response;
  }

  public int getStatusCode() {
    return statusCode;
  }

  public void setStatusCode(int statusCode) {
    this.statusCode = statusCode;
  }
  
  public String getMessage()
  {
//    {"httpStatus":"Conflict","httpStatusCode":409,"status":"ERROR","message":"Unexpected end-of-input: expected close marker for Array (start marker at [Source: (BufferedInputStream); line: 1, column: 22])\n at [Source: (BufferedInputStream); line: 1, column: 45]"}

    if (this.hasMessage())
    {
      return this.getJsonObject().get("message").getAsString();
    }
    else
    {
      return "";
    }
  }
  
  public boolean hasMessage()
  {
    JsonObject jo = this.getJsonObject();
    
    return jo != null && jo.has("message") && jo.get("message").getAsString().length() > 0;
  }
}
