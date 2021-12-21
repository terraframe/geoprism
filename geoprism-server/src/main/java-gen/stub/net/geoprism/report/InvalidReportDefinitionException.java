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
package net.geoprism.report;


public class InvalidReportDefinitionException extends InvalidReportDefinitionExceptionBase 
{
  private static final long serialVersionUID = 737376829;
  
  public InvalidReportDefinitionException()
  {
    super();
  }
  
  public InvalidReportDefinitionException(java.lang.String developerMessage)
  {
    super(developerMessage);
  }
  
  public InvalidReportDefinitionException(java.lang.String developerMessage, java.lang.Throwable cause)
  {
    super(developerMessage, cause);
  }
  
  public InvalidReportDefinitionException(java.lang.Throwable cause)
  {
    super(cause);
  }
  
}
