package net.geoprism.registry.graph;

import java.util.List;

import com.runwaysdk.business.graph.GraphQuery;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;

import net.geoprism.registry.model.ServerGeoObjectType;
import net.geoprism.registry.model.ServerHierarchyType;

public class InheritedHierarchyAnnotation extends InheritedHierarchyAnnotationBase
{
  @SuppressWarnings("unused")
  private static final long serialVersionUID = -466848506;

  public InheritedHierarchyAnnotation()
  {
    super();
  }

  public String getForHierarchyCode()
  {
    MdVertexDAOIF mdVertex = MdVertexDAO.getMdVertexDAO(CLASS);
    MdAttributeDAOIF mdAttribute = mdVertex.definesAttribute(FORHIERARCHY);

    StringBuilder statement = new StringBuilder();
    statement.append("SELECT " + mdAttribute.getColumnName() + ".code");
    statement.append(" FROM :rid");

    GraphQuery<String> query = new GraphQuery<String>(statement.toString());
    query.setParameter("rid", this.getRID());

    return query.getSingleResult();
  }

  public String getInheritedHierarchyCode()
  {
    MdVertexDAOIF mdVertex = MdVertexDAO.getMdVertexDAO(CLASS);
    MdAttributeDAOIF mdAttribute = mdVertex.definesAttribute(INHERITEDHIERARCHY);

    StringBuilder statement = new StringBuilder();
    statement.append("SELECT " + mdAttribute.getColumnName() + ".code");
    statement.append(" FROM :rid");

    GraphQuery<String> query = new GraphQuery<String>(statement.toString());
    query.setParameter("rid", this.getRID());

    return query.getSingleResult();
  }

  public static List<InheritedHierarchyAnnotation> getByGeoObjectType(ServerGeoObjectType type)
  {
    return getByGeoObjectType(type.getType());
  }

  public static List<InheritedHierarchyAnnotation> getByGeoObjectType(GeoObjectType type)
  {
    MdVertexDAOIF mdVertex = MdVertexDAO.getMdVertexDAO(CLASS);
    MdAttributeDAOIF mdAttribute = mdVertex.definesAttribute(GEOOBJECTTYPE);

    StringBuilder statement = new StringBuilder();
    statement.append("SELECT FROM " + mdVertex.getDBClassName());
    statement.append(" WHERE " + mdAttribute.getColumnName() + " = :geoObjectType");

    GraphQuery<InheritedHierarchyAnnotation> query = new GraphQuery<InheritedHierarchyAnnotation>(statement.toString());
    query.setParameter("geoObjectType", type.getRID());

    return query.getResults();
  }

  public static List<InheritedHierarchyAnnotation> getByRelationship(HierarchicalRelationshipType hierarchicalRelationship)
  {
    MdVertexDAOIF mdVertex = MdVertexDAO.getMdVertexDAO(CLASS);
    MdAttributeDAOIF mdForHierarchy = mdVertex.definesAttribute(FORHIERARCHY);
    MdAttributeDAOIF mdInheritedHierarchy = mdVertex.definesAttribute(INHERITEDHIERARCHY);

    StringBuilder statement = new StringBuilder();
    statement.append("SELECT FROM " + mdVertex.getDBClassName());
    statement.append(" WHERE " + mdForHierarchy.getColumnName() + " = :hierarchicalRelationship");
    statement.append(" OR " + mdInheritedHierarchy.getColumnName() + " = :hierarchicalRelationship");

    GraphQuery<InheritedHierarchyAnnotation> query = new GraphQuery<InheritedHierarchyAnnotation>(statement.toString());
    query.setParameter("hierarchicalRelationship", hierarchicalRelationship.getRID());

    return query.getResults();
  }

  public static InheritedHierarchyAnnotation get(ServerGeoObjectType geoObjectType, ServerHierarchyType forRelationship)
  {
    return get(geoObjectType.getType(), forRelationship.getObject());
  }

  public static InheritedHierarchyAnnotation get(GeoObjectType geoObjectType, HierarchicalRelationshipType forRelationship)
  {
    MdVertexDAOIF mdVertex = MdVertexDAO.getMdVertexDAO(CLASS);
    MdAttributeDAOIF mdAttribute = mdVertex.definesAttribute(GEOOBJECTTYPE);
    MdAttributeDAOIF mdForHierarchy = mdVertex.definesAttribute(FORHIERARCHY);

    StringBuilder statement = new StringBuilder();
    statement.append("SELECT FROM " + mdVertex.getDBClassName());
    statement.append(" WHERE " + mdAttribute.getColumnName() + " = :geoObjectType");
    statement.append(" AND " + mdForHierarchy.getColumnName() + " = :forRelationship");

    GraphQuery<InheritedHierarchyAnnotation> query = new GraphQuery<InheritedHierarchyAnnotation>(statement.toString());
    query.setParameter("geoObjectType", geoObjectType.getRID());
    query.setParameter("forRelationship", forRelationship.getRID());

    return query.getSingleResult();
  }

  public static List<InheritedHierarchyAnnotation> getByInheritedHierarchy(ServerGeoObjectType geoObjectType, ServerHierarchyType inheritedHierarchy)
  {
    return getByInheritedHierarchy(geoObjectType.getType(), inheritedHierarchy.getObject());
  }

  public static List<InheritedHierarchyAnnotation> getByInheritedHierarchy(GeoObjectType geoObjectType, HierarchicalRelationshipType inheritedHierarchy)
  {
    MdVertexDAOIF mdVertex = MdVertexDAO.getMdVertexDAO(CLASS);
    MdAttributeDAOIF mdAttribute = mdVertex.definesAttribute(GEOOBJECTTYPE);
    MdAttributeDAOIF mdInheritedHierarchy = mdVertex.definesAttribute(INHERITEDHIERARCHY);

    StringBuilder statement = new StringBuilder();
    statement.append("SELECT FROM " + mdVertex.getDBClassName());
    statement.append(" WHERE " + mdAttribute.getColumnName() + " = :geoObjectType");
    statement.append(" AND " + mdInheritedHierarchy.getColumnName() + " = :inheritedHierarchy");

    GraphQuery<InheritedHierarchyAnnotation> query = new GraphQuery<InheritedHierarchyAnnotation>(statement.toString());
    query.setParameter("geoObjectType", geoObjectType.getRID());
    query.setParameter("inheritedHierarchy", inheritedHierarchy.getRID());

    return query.getResults();
  }

  public static InheritedHierarchyAnnotation getByForHierarchical(ServerHierarchyType forHierarchicalRelationship)
  {
    return getByForHierarchical(forHierarchicalRelationship.getObject());
  }

  public static InheritedHierarchyAnnotation getByForHierarchical(HierarchicalRelationshipType forHierarchicalRelationship)
  {
    MdVertexDAOIF mdVertex = MdVertexDAO.getMdVertexDAO(CLASS);
    MdAttributeDAOIF mdForHierarchy = mdVertex.definesAttribute(FORHIERARCHY);

    StringBuilder statement = new StringBuilder();
    statement.append("SELECT FROM " + mdVertex.getDBClassName());
    statement.append(" WHERE " + mdForHierarchy.getColumnName() + " = :hierarchicalRelationship");

    GraphQuery<InheritedHierarchyAnnotation> query = new GraphQuery<InheritedHierarchyAnnotation>(statement.toString());
    query.setParameter("hierarchicalRelationship", forHierarchicalRelationship.getRID());

    return query.getSingleResult();
  }

  public static List<InheritedHierarchyAnnotation> getAnnotationByHierarchies(ServerHierarchyType forHierarchicalRelationship, ServerHierarchyType inheritedHierarchicalRelationship)
  {
    return getAnnotationByHierarchies(forHierarchicalRelationship.getObject(), inheritedHierarchicalRelationship.getObject());
  }

  public static List<InheritedHierarchyAnnotation> getAnnotationByHierarchies(HierarchicalRelationshipType forHierarchicalRelationship, HierarchicalRelationshipType inheritedHierarchicalRelationship)
  {
    MdVertexDAOIF mdVertex = MdVertexDAO.getMdVertexDAO(CLASS);
    MdAttributeDAOIF mdForHierarchy = mdVertex.definesAttribute(FORHIERARCHY);
    MdAttributeDAOIF mdInheritedHierarchy = mdVertex.definesAttribute(INHERITEDHIERARCHY);

    StringBuilder statement = new StringBuilder();
    statement.append("SELECT FROM " + mdVertex.getDBClassName());
    statement.append(" WHERE " + mdForHierarchy.getColumnName() + " = :forHierarchicalRelationship");
    statement.append(" AND " + mdInheritedHierarchy.getColumnName() + " = :inheritedHierarchicalRelationship");

    GraphQuery<InheritedHierarchyAnnotation> query = new GraphQuery<InheritedHierarchyAnnotation>(statement.toString());
    query.setParameter("forHierarchicalRelationship", forHierarchicalRelationship.getRID());
    query.setParameter("inheritedHierarchicalRelationship", inheritedHierarchicalRelationship.getRID());

    return query.getResults();
  }
  
  public static List<HierarchicalRelationshipType> getInheritedTypes()
  {
    MdVertexDAOIF mdVertex = MdVertexDAO.getMdVertexDAO(CLASS);
    MdAttributeDAOIF mdInheritedHierarchy = mdVertex.definesAttribute(INHERITEDHIERARCHY);

    StringBuilder statement = new StringBuilder();
    statement.append("SELECT EXPAND( " + mdInheritedHierarchy.getColumnName() + ") FROM " + mdVertex.getDBClassName());

    GraphQuery<HierarchicalRelationshipType> query = new GraphQuery<HierarchicalRelationshipType>(statement.toString());

    return query.getResults();
  }


}
