package net.geoprism.registry.graph;

import org.commongeoregistry.adapter.metadata.AttributeFloatType;
import org.commongeoregistry.adapter.metadata.AttributeType;

import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeDoubleInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.graph.MdVertexInfo;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.metadata.MdAttributeConcreteDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDoubleDAO;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.system.metadata.MdVertex;

import net.geoprism.registry.RegistryConstants;
import net.geoprism.registry.model.ValueNodeStrategy;
import net.geoprism.registry.model.ValueStrategy;
import net.geoprism.registry.model.VertexValueStrategy;

public class AttributeDoubleType extends AttributeDoubleTypeBase
{
  public static final String PREFIX           = "dv_";

  public static final String VALUE            = "value";

  @SuppressWarnings("unused")
  private static final long  serialVersionUID = -352072420;

  public AttributeDoubleType()
  {
    super();
  }

  private String getTableName()
  {
    int count = 0;

    String name = PREFIX + count + "DoubleValue";

    if (name.length() > 25)
    {
      name = name.substring(0, 25);
    }

    while (Database.tableExists(name))
    {
      count++;

      name = PREFIX + count + "DoubleValue";

      if (name.length() > 25)
      {
        name = name.substring(0, 25);
      }
    }

    return name;
  }

  @Override
  @Transaction
  public void apply()
  {
    // Create the value vertex class
    if (this.isNew() && !this.isAppliedToDb())
    {
      String tableName = this.getTableName();
      
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
      mdAttribute.setValue(MdAttributeDoubleInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
      mdAttribute.apply();

      this.setValueVertexId(mdVertex.getOid());
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
  public AttributeType toDTO()
  {
    AttributeFloatType dto = new AttributeFloatType(this.getCode(), getLocalizedLabel(), getLocalizedDescription(), isAppliedToDb(), isNew(), isAppliedToDb());
    dto.setPrecision(this.getPrecision());
    dto.setScale(this.getScale());

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
