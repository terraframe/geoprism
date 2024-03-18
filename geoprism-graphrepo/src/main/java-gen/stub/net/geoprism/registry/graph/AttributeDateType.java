package net.geoprism.registry.graph;

import org.commongeoregistry.adapter.metadata.AttributeType;

import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.metadata.MdAttributeDateTimeDAO;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;

import net.geoprism.registry.model.ValueNodeStrategy;
import net.geoprism.registry.model.ValueStrategy;
import net.geoprism.registry.model.VertexValueStrategy;

public class AttributeDateType extends AttributeDateTypeBase
{
  @SuppressWarnings("unused")
  private static final long serialVersionUID = -1619753569;

  public AttributeDateType()
  {
    super();
  }

  @Override
  @Transaction
  public void apply()
  {
    if (!this.getIsChangeOverTime())
    {
      MdAttributeDateTimeDAO mdAttribute = null;

      // Create the value vertex class
      if (this.isNew() && !this.isAppliedToDb())
      {
        // Create the MdAttribute on the MdVertex
        mdAttribute = MdAttributeDateTimeDAO.newInstance();
      }
      else
      {
        // Update the precision and scale of the value attribute
        MdVertexDAOIF mdVertex = MdVertexDAO.get(this.getGeoObjectType().getMdVertexOid());

        mdAttribute = (MdAttributeDateTimeDAO) mdVertex.definesAttribute(this.getCode()).getBusinessDAO();
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
    org.commongeoregistry.adapter.metadata.AttributeDateType dto = new org.commongeoregistry.adapter.metadata.AttributeDateType(this.getCode(), getLocalizedLabel(), getLocalizedDescription(), getIsDefault(), isNew(), getUnique());

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
      return new ValueNodeStrategy(this, MdVertexDAO.getMdVertexDAO(AttributeDateValue.CLASS), AttributeDateValue.VALUE);
    }
  }

}
