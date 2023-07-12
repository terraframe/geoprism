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
package net.geoprism.graph.service;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.runwaysdk.ComponentIF;
import com.runwaysdk.business.graph.VertexObject;

import net.geoprism.graph.LabeledPropertyGraphSynchronization;
import net.geoprism.graph.LabeledPropertyGraphTypeVersion;
import net.geoprism.spring.ApplicationContextHolder;

@Repository
public interface LabeledPropertyGraphServiceIF
{
  public static LabeledPropertyGraphServiceIF getInstance()
  {
    return ApplicationContextHolder.getContext().getBean(LabeledPropertyGraphServiceIF.class);
  }

  public String publish(LabeledPropertyGraphTypeVersion version);

  public void preDelete(LabeledPropertyGraphTypeVersion version);

  public void postDelete(LabeledPropertyGraphTypeVersion version);

  public void postCreate(LabeledPropertyGraphTypeVersion version);

  public void assignPermissions(ComponentIF component);

  public void createPublishJob(LabeledPropertyGraphTypeVersion version);

  public void postSynchronization(LabeledPropertyGraphSynchronization synchronization, Map<String, Object> cache);

  public void postSynchronization(LabeledPropertyGraphSynchronization synchronization, VertexObject node, Map<String, Object> cache);

  public void postTruncate(LabeledPropertyGraphSynchronization synchronization);
}
