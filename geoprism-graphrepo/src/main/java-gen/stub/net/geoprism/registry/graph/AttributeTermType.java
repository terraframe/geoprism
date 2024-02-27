package net.geoprism.registry.graph;

import org.commongeoregistry.adapter.metadata.AttributeType;

import com.runwaysdk.constants.MdAttributeTermInfo;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.metadata.MdAttributeConcreteDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeTermDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;

import net.geoprism.ontology.Classifier;
import net.geoprism.registry.conversion.TermConverter;
import net.geoprism.registry.model.ValueNodeStrategy;
import net.geoprism.registry.model.ValueStrategy;
import net.geoprism.registry.model.VertexValueStrategy;

public class AttributeTermType extends AttributeTermTypeBase
{
  @SuppressWarnings("unused")
  private static final long serialVersionUID = -1954824376;

  public AttributeTermType()
  {
    super();
  }

  @Override
  @Transaction
  public void apply()
  {
    GeoObjectType type = this.getGeoObjectType();

    // Create the root term node for this attribute
    this.setRootTerm(TermConverter.buildIfNotExistAttribute(type, this.getCode(), type.getRootTerm()));

    super.apply();

    if (!this.getIsChangeOverTime())
    {
      // Create the MdAttribute on the MdVertex
      MdAttributeTermDAO mdAttribute = MdAttributeTermDAO.newInstance();

      populate(mdAttribute);

      mdAttribute.apply();
    }
  }

  @Override
  protected void populate(MdAttributeConcreteDAO mdAttribute)
  {
    super.populate(mdAttribute);

    mdAttribute.setValue(MdAttributeTermInfo.REF_MD_ENTITY, MdBusinessDAO.getMdBusinessDAO(Classifier.CLASS).getOid());
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

    this.getRootTerm().delete();

    super.delete();
  }

  @Override
  protected void populate(AttributeType dto)
  {
    super.populate(dto);

    ( (org.commongeoregistry.adapter.metadata.AttributeTermType) dto ).setRootTerm(TermConverter.buildTermFromClassifier(this.getRootTerm()));
  }

  @Override
  public AttributeType toDTO()
  {
    org.commongeoregistry.adapter.metadata.AttributeTermType dto = new org.commongeoregistry.adapter.metadata.AttributeTermType(this.getCode(), getLocalizedLabel(), getLocalizedDescription(), isAppliedToDb(), isNew(), isAppliedToDb());

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
      return new ValueNodeStrategy(this, MdVertexDAO.getMdVertexDAO(AttributeTermValue.CLASS), AttributeTermValue.VALUE);
    }
  }

}
