/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism.dhis2.response;

import java.util.List;

import org.json.JSONObject;

public class DHIS2DuplicateDataException extends RuntimeException
{
  private static final long serialVersionUID = -5902095640582124900L;
  
  JSONObject errorReport;
  
  List<String> msgs;
  
  public DHIS2DuplicateDataException(List<String> msgs)
  {
    this.msgs = msgs;
  }
  
  public DHIS2DuplicateDataException(JSONObject errorReport)
  {
    this.errorReport = errorReport;
  }
  
  public DHIS2DuplicateDataException(String message)
  {
    super(message);
  }
  
  public List<String> getErrorMessages()
  {
    return this.msgs;
  }
}
