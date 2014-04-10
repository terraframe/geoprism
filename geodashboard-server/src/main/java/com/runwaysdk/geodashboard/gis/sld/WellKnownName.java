package com.runwaysdk.geodashboard.gis.sld;

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
