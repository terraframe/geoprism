package net.geoprism.registry.graph;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.commongeoregistry.adapter.dataaccess.LocalizedValue;
import org.commongeoregistry.adapter.metadata.HierarchyType;

import com.runwaysdk.business.graph.GraphQuery;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdEdgeDAOIF;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.RelationshipCardinalityException;
import com.runwaysdk.dataaccess.metadata.graph.MdEdgeDAO;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.system.metadata.MdEdge;

import net.geoprism.registry.conversion.LocalizedValueConverter;
import net.geoprism.registry.conversion.RegistryLocalizedValueConverter;
import net.geoprism.registry.model.ServerGeoObjectType;

public class HierarchicalRelationshipType extends HierarchicalRelationshipTypeBase
{
  @SuppressWarnings("unused")
  private static final long serialVersionUID = -1536469831;

  public HierarchicalRelationshipType()
  {
    super();
  }

  protected MdEdgeDAOIF getDefinitionEdgeDAO()
  {
    return MdEdgeDAO.get(this.getDefinitionEdgeOid());
  }

  @Override
  protected String buildKey()
  {
    return this.getCode();
  }

  @Transaction
  public void update(HierarchyType dto)
  {
    RegistryLocalizedValueConverter.populate(this, HierarchicalRelationshipType.LABEL, dto.getLabel());
    RegistryLocalizedValueConverter.populate(this, HierarchicalRelationshipType.DESCRIPTION, dto.getDescription());

    this.setAbstractDescription(dto.getAbstractDescription());
    this.setProgress(dto.getProgress());
    this.setAcknowledgement(dto.getAcknowledgement());
    this.setDisclaimer(dto.getDisclaimer());
    this.setContact(dto.getContact());
    this.setPhoneNumber(dto.getPhoneNumber());
    this.setEmail(dto.getEmail());
    this.setAccessConstraints(dto.getAccessConstraints());
    this.setUseConstraints(dto.getUseConstraints());
    this.apply();
  }

  @Transaction
  public void addToHierarchy(ServerGeoObjectType parentType, ServerGeoObjectType childType)
  {
    try
    {
      MdEdgeDAOIF mdEdge = getDefinitionEdgeDAO();

      parentType.getObject().addChild(childType.getObject(), mdEdge).apply();
    }
    catch (RelationshipCardinalityException e)
    {
      GeoObjectTypeAlreadyInHierarchyException ex = new GeoObjectTypeAlreadyInHierarchyException();
      ex.setGotCode(childType.getCode());
      throw ex;
    }
  }

  @Override
  @Transaction
  public void delete()
  {
    MdEdge objectEdge = this.getObjectEdge();
    MdEdge definitionEdge = this.getDefinitionEdge();

    // TODO: HEADS UP
    // /*
    // * Delete all inherited hierarchies
    // */
    // List<? extends InheritedHierarchyAnnotation> annotations =
    // InheritedHierarchyAnnotation.getByRelationship(this);
    //
    // for (InheritedHierarchyAnnotation annotation : annotations)
    // {
    // annotation.delete();
    // }

    super.delete();

    objectEdge.delete();
    definitionEdge.delete();
  }

  public List<ServerGeoObjectType> getChildren(ServerGeoObjectType parent)
  {
    MdEdgeDAOIF mdEdge = this.getDefinitionEdgeDAO();

    StringBuilder statement = new StringBuilder();
    statement.append("SELECT EXPAND(out('" + mdEdge.getDBClassName() + "')).code FROM :rid");

    GraphQuery<String> query = new GraphQuery<String>(statement.toString());
    query.setParameter("rid", this.getRID());

    return query.getResults().stream().map(code -> {
      return ServerGeoObjectType.get(code);
    }).collect(Collectors.toList());
  }
  
  public String getOrganizationCode() {
    MdVertexDAOIF mdVertexDAO = MdVertexDAO.getMdVertexDAO(CLASS);
    MdAttributeDAOIF mdAttribute = mdVertexDAO.definesAttribute(HierarchicalRelationshipType.ORGANIZATION);

    StringBuilder statement = new StringBuilder();
    statement.append("SELECT " + mdAttribute.getColumnName()+".code FROM :rid");

    GraphQuery<String> query = new GraphQuery<String>(statement.toString());
    query.setParameter("rid", this.getRID());

    return query.getSingleResult();
  }
  
  public void fromDTO(HierarchyType dto)
  {
    this.setCode(dto.getCode());
    LocalizedValueConverter.populate(this, HierarchicalRelationshipType.LABEL, dto.getLabel());
    LocalizedValueConverter.populate(this, HierarchicalRelationshipType.DESCRIPTION, dto.getDescription());
    this.setAbstractDescription(dto.getAbstractDescription());
    this.setAcknowledgement(dto.getAcknowledgement());
    this.setDisclaimer(dto.getDisclaimer());
    this.setContact(dto.getContact());
    this.setPhoneNumber(dto.getPhoneNumber());
    this.setEmail(dto.getEmail());
    this.setProgress(dto.getProgress());
    this.setAccessConstraints(dto.getAccessConstraints());
    this.setUseConstraints(dto.getUseConstraints());
  }
  
  public HierarchyType toDTO()
  {
    LocalizedValue label = LocalizedValueConverter.convert(this.getEmbeddedComponent(LABEL));
    LocalizedValue description = LocalizedValueConverter.convert(this.getEmbeddedComponent(DESCRIPTION));

    final HierarchyType dto = new HierarchyType(this.getCode(), label, description, this.getOrganizationCode());
    dto.setAbstractDescription(this.getAbstractDescription());
    dto.setAcknowledgement(this.getAcknowledgement());
    dto.setDisclaimer(this.getDisclaimer());
    dto.setContact(this.getContact());
    dto.setPhoneNumber(this.getPhoneNumber());
    dto.setEmail(this.getEmail());
    dto.setProgress(this.getProgress());
    dto.setAccessConstraints(this.getAccessConstraints());
    dto.setUseConstraints(this.getUseConstraints());
    
    return dto;
  }


  public static List<HierarchicalRelationshipType> getInheritedTypes()
  {
    // TODO: HEADS UP
    // QueryFactory factory = new QueryFactory();
    //
    // InheritedHierarchyAnnotationQuery ihaQuery = new
    // InheritedHierarchyAnnotationQuery(factory);
    //
    // HierarchicalRelationshipTypeQuery query = new
    // HierarchicalRelationshipTypeQuery(factory);
    // query.WHERE(query.getOid().EQ(ihaQuery.getInheritedHierarchicalRelationshipType().getOid()));
    //
    // try (OIterator<? extends HierarchicalRelationshipType> it =
    // query.getIterator())
    // {
    // return new LinkedList<HierarchicalRelationshipType>(it.getAll());
    // }

    return new LinkedList<HierarchicalRelationshipType>();
  }

  public static List<HierarchicalRelationshipType> getAll()
  {
    MdVertexDAOIF mdVertexDAO = MdVertexDAO.getMdVertexDAO(CLASS);

    StringBuilder statement = new StringBuilder();
    statement.append("SELECT FROM " + mdVertexDAO.getDBClassName());

    GraphQuery<HierarchicalRelationshipType> query = new GraphQuery<HierarchicalRelationshipType>(statement.toString());

    return query.getResults();
  }

  protected static HierarchicalRelationshipType getByValue(Object value, String attributeName)
  {
    MdVertexDAOIF mdVertexDAO = MdVertexDAO.getMdVertexDAO(CLASS);
    MdAttributeDAOIF mdAttribute = mdVertexDAO.definesAttribute(attributeName);

    StringBuilder statement = new StringBuilder();
    statement.append("SELECT FROM " + mdVertexDAO.getDBClassName());
    statement.append(" WHERE " + mdAttribute.getColumnName() + " = :value");

    GraphQuery<HierarchicalRelationshipType> query = new GraphQuery<HierarchicalRelationshipType>(statement.toString());
    query.setParameter("value", value);

    return query.getSingleResult();
  }

  public static HierarchicalRelationshipType getByCode(String code)
  {
    return getByValue(code, HierarchicalRelationshipType.CODE);
  }

  public static HierarchicalRelationshipType getByMdEdge(MdEdgeDAOIF mdEdge)
  {
    return getByValue(mdEdge.getOid(), HierarchicalRelationshipType.OBJECTEDGE);
  }

  public static boolean isEdgeAHierarchyType(MdEdgeDAOIF mdEdge)
  {
    return ( HierarchicalRelationshipType.getByMdEdge(mdEdge) != null );
  }

}
