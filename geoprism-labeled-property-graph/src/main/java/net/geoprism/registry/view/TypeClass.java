package net.geoprism.registry.view;

import org.apache.commons.lang3.stream.Streams;

public enum TypeClass implements Comparable<TypeClass> {

  GEO_OBJECT_TYPE("GEO_OBJECT_TYPE", TypeCategory.OBJECT_CLASS), //
  BUSINESS_TYPE("BUSINESS_TYPE", TypeCategory.OBJECT_CLASS), //
  CONCEPT_CLASS("CONCEPT_CLASS", TypeCategory.OBJECT_CLASS), //
  BUSINESS_EDGE("BusinessEdgeType", TypeCategory.OBJECT_CLASS), //
  DAG("DirectedAcyclicGraphType", TypeCategory.OBJECT_CLASS), //
  UNDIRECTED_GRAPH("UndirectedGraphType", TypeCategory.OBJECT_CLASS), //
  HIERARCHY("HierarchyType", TypeCategory.OBJECT_CLASS);

  private String       code;

  private TypeCategory typeCategory;

  private TypeClass(String code, TypeCategory typeCategory)
  {
    this.code = code;
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
