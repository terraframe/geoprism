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
package net.geoprism.registry.view;

import org.apache.commons.lang3.stream.Streams;

public enum TypeClass implements Comparable<TypeClass> {

  GEO_OBJECT_TYPE("GEO_OBJECT_TYPE", "G", TypeCategory.OBJECT_CLASS), //
  BUSINESS_TYPE("BUSINESS_TYPE", "B", TypeCategory.OBJECT_CLASS), //
  CONCEPT_CLASS("CONCEPT_CLASS", "C", TypeCategory.OBJECT_CLASS), //
  BUSINESS_EDGE("BusinessEdgeType", "BE", TypeCategory.EDGE_CLASS), //
  CONCEPT_EDGE("ConceptEdgeType", "CE", TypeCategory.EDGE_CLASS), //
  DAG("DirectedAcyclicGraphType", "DA", TypeCategory.EDGE_CLASS), //
  UNDIRECTED_GRAPH("UndirectedGraphType", "UG", TypeCategory.EDGE_CLASS), //
  HIERARCHY("HierarchyType", "H", TypeCategory.EDGE_CLASS);

  private String       code;

  private String       shortCode;

  private TypeCategory typeCategory;

  private TypeClass(String code, String shortCode, TypeCategory typeCategory)
  {
    this.code = code;
    this.shortCode = shortCode;
    this.typeCategory = typeCategory;
  }

  public String getCode()
  {
    return code;
  }

  public TypeCategory getTypeCategory()
  {
    return typeCategory;
  }

  public String getShortCode()
  {
    return shortCode;
  }

  public static TypeClass getByCode(String code)
  {
    return Streams.of(TypeClass.values()) //
        .filter(t -> t.getCode().equals(code)) //
        .findFirst().orElseThrow();
  }

  public static TypeClass getByCategory(TypeCategory category)
  {
    return Streams.of(TypeClass.values()) //
        .filter(t -> t.getTypeCategory().equals(category)) //
        .findFirst().orElseThrow();
  }

  public static TypeClass getObjectTypes()
  {
    return getByCategory(TypeCategory.OBJECT_CLASS);
  }

  public static TypeClass getEdgeTypes()
  {
    return getByCategory(TypeCategory.EDGE_CLASS);
  }
}
