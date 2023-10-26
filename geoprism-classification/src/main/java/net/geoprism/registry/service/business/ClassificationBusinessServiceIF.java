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
package net.geoprism.registry.service.business;

import java.io.IOException;
import java.util.List;

import org.commongeoregistry.adapter.Term;
import org.commongeoregistry.adapter.metadata.AttributeClassificationType;
import org.springframework.stereotype.Component;

import com.google.gson.JsonObject;
import com.runwaysdk.business.graph.EdgeObject;
import com.runwaysdk.resource.ApplicationResource;

import net.geoprism.registry.model.Classification;
import net.geoprism.registry.model.ClassificationNode;
import net.geoprism.registry.model.ClassificationType;
import net.geoprism.registry.view.Page;

@Component
public interface ClassificationBusinessServiceIF
{

  void apply(Classification classification, Classification parent);

  void populate(Classification classification, JsonObject object);

  void populate(Classification classification, Term term);

  void delete(Classification classification);

  void addParent(Classification classification, Classification parent);

  void addChild(Classification classification, Classification child);

  void removeParent(Classification classification, Classification parent);

  void removeChild(Classification classification, Classification child);

  void move(Classification classification, Classification newParent);

  Page<Classification> getChildren(Classification classification);

  Page<Classification> getChildren(Classification classification, Integer pageSize, Integer pageNumber);

  List<Classification> getParents(Classification classification);

  List<Classification> getAncestors(Classification classification, String rootCode);

  ClassificationNode getAncestorTree(Classification classification, String rootCode, Integer pageSize);

  EdgeObject getEdge(Classification classification, Classification parent);

  Classification get(ClassificationType type, String code);

  Classification getByOid(ClassificationType type, String oid);

  Classification newInstance(ClassificationType type);

  Classification construct(ClassificationType type, JsonObject object, boolean isNew);

  List<Classification> search(ClassificationType type, String rootCode, String text);

  Classification get(AttributeClassificationType attribute, String code);

  JsonObject exportToJson(String classificationTypeCode, String code);

  void importJsonTree(String classificationTypeCode, ApplicationResource resource) throws IOException;

  void importJsonTree(ClassificationType type, Classification parent, JsonObject object);
}
