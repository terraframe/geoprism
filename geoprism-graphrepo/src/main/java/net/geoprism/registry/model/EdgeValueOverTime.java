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
package net.geoprism.registry.model;

import java.util.Date;

import com.runwaysdk.dataaccess.graph.attributes.ValueOverTime;

public class EdgeValueOverTime extends ValueOverTime
{
  private String uid;

  public EdgeValueOverTime(Date startDate, Date endDate, Object value, String uid)
  {
    super(startDate, endDate, value);

    this.uid = uid;
  }

  public EdgeValueOverTime(String oid, Date startDate, Date endDate, Object value, String uid)
  {
    super(oid, startDate, endDate, value);

    this.uid = uid;
  }

  public String getUid()
  {
    return uid;
  }

  public void setUid(String uid)
  {
    this.uid = uid;
  }
}
