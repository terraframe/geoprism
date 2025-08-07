/**
 * Copyright (c) 2023 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Geoprism(tm).
 *
 * Geoprism(tm) is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * Geoprism(tm) is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Geoprism(tm). If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism.graph;

import java.util.List;

import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;

import net.geoprism.registry.lpg.LabeledVersion;
import net.geoprism.registry.model.SnapshotContainer;

public class LabeledPropertyGraphTypeVersion extends LabeledPropertyGraphTypeVersionBase implements LabeledVersion, SnapshotContainer<LabeledPropertyGraphTypeSnapshot>
{

  private static final long  serialVersionUID = -351397872;

  public static final String PREFIX           = "g_";

  public static final String SPLIT            = "__";

  public static final String TYPE_CODE        = "typeCode";

  public static final String ATTRIBUTES       = "attributes";

  // public static final String HIERARCHIES = "hierarchies";

  public static final String PERIOD           = "period";

  public LabeledPropertyGraphTypeVersion()
  {
    super();
  }

  @Override
  public boolean createTablesWithSnapshot()
  {
    return true;
  }

  public static List<? extends LabeledPropertyGraphTypeVersion> getAll()
  {
    LabeledPropertyGraphTypeVersionQuery query = new LabeledPropertyGraphTypeVersionQuery(new QueryFactory());

    try (OIterator<? extends LabeledPropertyGraphTypeVersion> it = query.getIterator())
    {
      return it.getAll();
    }
  }
}
