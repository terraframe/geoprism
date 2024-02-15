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

public class AttributeClassificationType extends AttributeClassificationTypeBase
{
  @SuppressWarnings("unused")
  private static final long serialVersionUID = -1394327486;
  
  public AttributeClassificationType()
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
      MdAttributeDateTimeDAO mdAttribute = MdAttributeDateTimeDAO.newInstance();

      populate(mdAttribute);

      mdAttribute.apply();
    }
    else {
      // TODO create a custom AttributeClassificationValue vertex class which has a reference to this classification type
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
    else {
        // TODO delete the custom AttributeClassificationValue vertex class which has a reference to this classification type
    }

    super.delete();
  }


  @Override
  public AttributeType toDTO()
  {
    return new org.commongeoregistry.adapter.metadata.AttributeClassificationType(this.getCode(), getLocalizedLabel(), getLocalizedDescription(), isAppliedToDb(), isNew(), isAppliedToDb());
  }

  @Override
  public ValueStrategy getStrategy()
  {
    throw new UnsupportedOperationException();
    // TODO: HEADS UP 
//    if (!this.getIsChangeOverTime())
//    {
//      return new VertexValueStrategy(this);
//    }
//    else
//    {
//      return new ValueNodeStrategy(this, MdVertexDAO.getMdVertexDAO(AttributeCharacterValue.CLASS), AttributeCharacterValue.VALUE);
//    }
  }


}
