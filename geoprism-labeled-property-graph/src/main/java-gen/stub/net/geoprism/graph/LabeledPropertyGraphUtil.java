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
package net.geoprism.graph;

import java.util.Date;

import com.runwaysdk.business.rbac.Authenticate;

import net.geoprism.graph.lpg.business.LabeledPropertyGraphSynchronizationBusinessServiceIF;
import net.geoprism.graph.lpg.business.LabeledPropertyGraphTypeBusinessServiceIF;
import net.geoprism.graph.lpg.business.LabeledPropertyGraphTypeEntryBusinessServiceIF;
import net.geoprism.graph.lpg.business.LabeledPropertyGraphTypeVersionBusinessServiceIF;

public class LabeledPropertyGraphUtil extends LabeledPropertyGraphUtilBase
{
  @SuppressWarnings("unused")
  private static final long                                    serialVersionUID = -2063169769;

  private LabeledPropertyGraphTypeBusinessServiceIF            typeService;

  private LabeledPropertyGraphTypeEntryBusinessServiceIF       entryService;

  private LabeledPropertyGraphTypeVersionBusinessServiceIF     versionService;

  private LabeledPropertyGraphSynchronizationBusinessServiceIF synchronzationService;

  public LabeledPropertyGraphUtil()
  {
    super();
  }

  public LabeledPropertyGraphUtil(LabeledPropertyGraphTypeEntryBusinessServiceIF entryService)
  {
    this.entryService = entryService;
  }

  public LabeledPropertyGraphUtil(LabeledPropertyGraphTypeVersionBusinessServiceIF versionService)
  {
    this.versionService = versionService;
  }

  public LabeledPropertyGraphUtil(LabeledPropertyGraphTypeBusinessServiceIF typeService, LabeledPropertyGraphTypeEntryBusinessServiceIF entryService)
  {
    this.typeService = typeService;
    this.entryService = entryService;
  }

  public LabeledPropertyGraphUtil(LabeledPropertyGraphSynchronizationBusinessServiceIF synchronzationService)
  {
    this.synchronzationService = synchronzationService;
  }

  @Override
  @Authenticate
  public LabeledPropertyGraphTypeVersion publishEntry(LabeledPropertyGraphTypeEntry entry)
  {
    return this.entryService.publishNoAuth(entry);
  }
  
  @Override
  @Authenticate
  public void execute(LabeledPropertyGraphSynchronization synchronization)
  {
    this.synchronzationService.executeNoAuth(synchronization);
  }

  @Override
  @Authenticate
  public LabeledPropertyGraphTypeEntry createEntry(String typeId, Date forDate)
  {
    LabeledPropertyGraphType type = this.typeService.get(typeId);

    return this.entryService.create(type, forDate);
  }

  @Override
  @Authenticate
  public void removeVersion(LabeledPropertyGraphTypeVersion version)
  {
    this.versionService.delete(version);
  }

  @Override
  public void publishVersion(LabeledPropertyGraphTypeVersion version)
  {
    this.versionService.publishNoAuth(version);
  }

}
