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
package net.geoprism.gis.sld;

/**
 * Represents a Well Known Name for SLD and provides a set of pre-defined
 * symbols or allows for construction of a custom value.
 */
public class WellKnownName implements Symbol
{
  /**
   * Standard symbols.
   * 
   */
  public enum STANDARD implements Symbol {
    CIRCLE, SQUARE, TRIANGLE, START, CROSS, X;

    @Override
    public String getSymbol()
    {
      return this.name().toLowerCase();
    }
    
    /**
     * Returns the enum as a WellKnownName object.
     * 
     * @return
     */
    public WellKnownName getWellKnownName()
    {
      return new WellKnownName(this.getSymbol());
    }
  }

  public static final String SHAPE_PREFIX = "shape://";

  /**
   * Shape symbols.
   */
  public enum SHAPE implements Symbol {
    VERTLINE, HORLINE, SLASH, BACKSLASH, DOT, PLUS, TIMES, OARROW, CARROW;

    @Override
    public String getSymbol()
    {
      return SHAPE_PREFIX + this.name().toLowerCase();
    }
    
    /**
     * Returns the enum as a WellKnownName object.
     * 
     * @return
     */
    public WellKnownName getWellKnownName()
    {
      return new WellKnownName(this.getSymbol());
    }
  }

  /**
   * The symbol recognized by SLD.
   */
  private String symbol;

  public WellKnownName(String symbol)
  {
    this.symbol = symbol;
  }
  
  @Override
  public String getSymbol()
  {
    return this.symbol;
  }

  @Override
  public String toString()
  {
    return this.getSymbol();
  }
}
