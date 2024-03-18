package net.geoprism.registry.graph;

import org.commongeoregistry.adapter.metadata.AttributeFloatType;
import org.commongeoregistry.adapter.metadata.AttributeType;

import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeDoubleInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.graph.MdVertexInfo;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.metadata.MdAttributeConcreteDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDoubleDAO;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.system.metadata.MdVertex;

import net.geoprism.registry.RegistryConstants;
import net.geoprism.registry.model.ValueNodeStrategy;
import net.geoprism.registry.model.ValueStrategy;
import net.geoprism.registry.model.VertexValueStrategy;
import net.geoprism.registry.model.graph.GraphTableUtil;

public class AttributeDoubleType extends AttributeDoubleTypeBase
{
  public static final String PREFIX           = "avd_";

  public static final String VALUE            = "value";

  @SuppressWarnings("unused")
  private static final long  serialVersionUID = -352072420;

  public AttributeDoubleType()
  {
    super();
  }

  @Override
  @Transaction
  public void apply()
  {
    if (!this.getIsChangeOverTime())
    {
      MdAttributeDoubleDAO mdAttribute = null;

      // Create the value vertex class
      if (this.isNew() && !this.isAppliedToDb())
      {
        // Create the MdAttribute on the MdVertex
        mdAttribute = MdAttributeDoubleDAO.newInstance();
      }
      else
      {
        // Update the precision and scale of the value attribute
        MdVertexDAOIF mdVertex = MdVertexDAO.get(this.getGeoObjectType().getMdVertexOid());

        mdAttribute = (MdAttributeDoubleDAO) mdVertex.definesAttribute(this.getCode()).getBusinessDAO();
      }

      populate(mdAttribute);

      mdAttribute.apply();
    }
    else
    {

      // Create the value vertex class
      if (this.isNew() && !this.isAppliedToDb())
      {
        String tableName = GraphTableUtil.generateTableName(PREFIX, "_double");

        MdVertexDAOIF superVertex = MdVertexDAO.getMdVertexDAO(AttributeBasicValue.CLASS);

        MdVertexDAO mdVertex = MdVertexDAO.newInstance();
        mdVertex.setValue(MdVertexInfo.PACKAGE, RegistryConstants.UNIVERSAL_GRAPH_PACKAGE + ".value");
        mdVertex.setValue(MdVertexInfo.NAME, tableName);
        mdVertex.setValue(MdVertexInfo.ENABLE_CHANGE_OVER_TIME, MdAttributeBooleanInfo.FALSE);
        mdVertex.setValue(MdVertexInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
        mdVertex.setValue(MdVertexInfo.DB_CLASS_NAME, tableName);
        mdVertex.setValue(MdVertexInfo.SUPER_MD_VERTEX, superVertex.getOid());
        mdVertex.apply();

        // Create the value attribute
        MdAttributeDoubleDAO mdAttribute = MdAttributeDoubleDAO.newInstance();
        mdAttribute.setValue(MdAttributeDoubleInfo.DEFINING_MD_CLASS, mdVertex.getOid());
        mdAttribute.setValue(MdAttributeDoubleInfo.NAME, VALUE);
        mdAttribute.setStructValue(MdAttributeDoubleInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Value");
        mdAttribute.setValue(MdAttributeDoubleInfo.LENGTH, getPrecision());
        mdAttribute.setValue(MdAttributeDoubleInfo.DECIMAL, getScale());
        mdAttribute.setValue(MdAttributeDoubleInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
        mdAttribute.apply();

        this.setValueVertexId(mdVertex.getOid());
      }
      else
      {
        // Update the precision and scale of the value attribute
        MdVertexDAOIF mdVertex = MdVertexDAO.get(this.getValueVertexOid());

        MdAttributeDoubleDAO mdAttribute = (MdAttributeDoubleDAO) mdVertex.definesAttribute(VALUE).getBusinessDAO();
        mdAttribute.setValue(MdAttributeDoubleInfo.LENGTH, getPrecision());
        mdAttribute.setValue(MdAttributeDoubleInfo.DECIMAL, getScale());
        mdAttribute.apply();
      }
    }

    super.apply();
  }

  @Override
  @Transaction
  public void delete()
  {
    MdVertex mdVertex = this.getValueVertex();

    super.delete();

    if (mdVertex != null)
    {
      mdVertex.delete();
    }
  }

  public Integer getPrecision()
  {
    return (Integer) this.getObjectValue(AttributeDoubleType.PRECISION);
  }

  public Integer getScale()
  {
    return (Integer) this.getObjectValue(AttributeDoubleType.SCALE);
  }

  @Override
  protected void populate(MdAttributeConcreteDAO mdAttribute)
  {
    super.populate(mdAttribute);

    mdAttribute.setValue(MdAttributeDoubleInfo.LENGTH, getPrecision());
    mdAttribute.setValue(MdAttributeDoubleInfo.DECIMAL, getScale());
  }

  @Override
  public void fromDTO(AttributeType dto)
  {
    super.fromDTO(dto);

    AttributeFloatType attributeFloatType = (AttributeFloatType) dto;

    this.setValue(AttributeDoubleType.PRECISION, Integer.valueOf(attributeFloatType.getPrecision()));
    this.setValue(AttributeDoubleType.SCALE, Integer.valueOf(attributeFloatType.getScale()));
  }

  @Override
  protected void populate(AttributeType dto)
  {
    super.populate(dto);

    ( (AttributeFloatType) dto ).setPrecision(this.getPrecision());
    ( (AttributeFloatType) dto ).setScale(this.getScale());
  }

  @Override
  public AttributeType toDTO()
  {
    AttributeFloatType dto = new AttributeFloatType(this.getCode(), getLocalizedLabel(), getLocalizedDescription(), getIsDefault(), isNew(), getUnique());

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
      return new ValueNodeStrategy(this, MdVertexDAO.get(this.getValueVertexOid()), VALUE);
    }
  }

}
