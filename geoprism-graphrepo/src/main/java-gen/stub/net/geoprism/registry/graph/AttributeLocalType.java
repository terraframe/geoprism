package net.geoprism.registry.graph;

import org.commongeoregistry.adapter.metadata.AttributeType;

import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.metadata.MdAttributeLocalCharacterEmbeddedDAO;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;

public class AttributeLocalType extends AttributeLocalTypeBase
{
  @SuppressWarnings("unused")
  private static final long serialVersionUID = 534282830;
  
  public AttributeLocalType()
  {
    super();
  }
  
  @Override
  @Transaction
  public void apply()
  {
    super.apply();

    if (!this.getIsChangeOverTime())
    {
      // Create the MdAttribute on the MdVertex
      MdAttributeLocalCharacterEmbeddedDAO mdAttribute = MdAttributeLocalCharacterEmbeddedDAO.newInstance();

      populate(mdAttribute);

      mdAttribute.apply();

    }
  }

  @Override
  @Transaction
  public void delete()
  {
    if (!this.getIsChangeOverTime())
    {
      GeoObjectType type = this.getGeoObjectType();
      MdVertexDAOIF mdVertex = MdVertexDAO.get(type.getMdVertexOid());
      MdAttributeDAOIF mdAttribute = mdVertex.definesAttribute(this.getCode());

      if (mdAttribute != null)
      {
        mdAttribute.getBusinessDAO().delete();
      }
    }

    super.delete();
  }
  
  @Override
  public AttributeType toDTO()
  {
    return new org.commongeoregistry.adapter.metadata.AttributeLocalType(this.getCode(), getLocalizedLabel(), getLocalizedDescription(), isAppliedToDb(), isNew(), isAppliedToDb());
  }
}
