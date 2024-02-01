package net.geoprism.registry.graph;

import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeConcreteInfo;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.metadata.MdAttributeBooleanDAO;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;

public class AttributeBooleanType extends AttributeBooleanTypeBase
{
  @SuppressWarnings("unused")
  private static final long serialVersionUID = -301152783;

  public AttributeBooleanType()
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
      MdAttributeBooleanDAO mdAttribute = MdAttributeBooleanDAO.newInstance();

      populate(mdAttribute);

      if (this.getIsDefault())
      {
        mdAttribute.setValue(MdAttributeConcreteInfo.DEFAULT_VALUE, MdAttributeBooleanInfo.FALSE);
      }

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

}
