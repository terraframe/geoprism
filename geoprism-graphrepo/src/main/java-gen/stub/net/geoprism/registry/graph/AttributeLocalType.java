package net.geoprism.registry.graph;

import org.commongeoregistry.adapter.metadata.AttributeType;

import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.metadata.MdAttributeLocalCharacterEmbeddedDAO;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;

import net.geoprism.registry.model.LocalValueNodeStrategy;
import net.geoprism.registry.model.ValueStrategy;
import net.geoprism.registry.model.VertexValueStrategy;

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
    if (!this.getIsChangeOverTime())
    {
      MdAttributeLocalCharacterEmbeddedDAO mdAttribute = null;

      // Create the value vertex class
      if (this.isNew() && !this.isAppliedToDb())
      {
        // Create the MdAttribute on the MdVertex
        mdAttribute = MdAttributeLocalCharacterEmbeddedDAO.newInstance();
      }
      else
      {
        // Update the precision and scale of the value attribute
        MdVertexDAOIF mdVertex = MdVertexDAO.get(this.getGeoObjectType().getMdVertexOid());

        mdAttribute = (MdAttributeLocalCharacterEmbeddedDAO) mdVertex.definesAttribute(this.getCode()).getBusinessDAO();
      }

      populate(mdAttribute);

      mdAttribute.apply();
    }

    super.apply();
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
    org.commongeoregistry.adapter.metadata.AttributeLocalType dto = new org.commongeoregistry.adapter.metadata.AttributeLocalType(this.getCode(), getLocalizedLabel(), getLocalizedDescription(), isAppliedToDb(), isNew(), isAppliedToDb());

    this.populate(dto);

    return dto;
  }

  @Override
  public ValueStrategy getStrategy()
  {
    if (!this.getIsChangeOverTime())
    {
      return new VertexValueStrategy(this);
    }
    else
    {
      return new LocalValueNodeStrategy(this, MdVertexDAO.getMdVertexDAO(AttributeLocalValue.CLASS), AttributeLocalValue.DEFAULTLOCALE);
    }
  }

}
