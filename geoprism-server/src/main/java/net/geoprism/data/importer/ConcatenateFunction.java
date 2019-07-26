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
package net.geoprism.data.importer;

public class ConcatenateFunction implements ShapefileFunction
{
  private ShapefileFunction f1;

  private ShapefileFunction f2;

  public ConcatenateFunction(ShapefileFunction f1, ShapefileFunction f2)
  {
    super();
    this.f1 = f1;
    this.f2 = f2;
  }


  @Override
  public Object getValue(FeatureRow feature)
  {
    String v1 = (String) this.f1.getValue(feature);
    String v2 = (String) this.f2.getValue(feature);

    if (v1 != null && v2 != null)
    {
      return v1 + v2;
    }
    else if (v1 != null)
    {
      return v1;
    }

    return v2;
  }

  @Override
  public String toJson()
  {
    throw new UnsupportedOperationException();
  }
}
