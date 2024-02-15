package net.geoprism.registry.graph;

import org.commongeoregistry.adapter.metadata.AttributeType;

import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeMultiTermDAOIF;
import com.runwaysdk.dataaccess.MdAttributeTermDAOIF;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.dataaccess.metadata.MdAttributeConcreteDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;

import net.geoprism.ontology.Classifier;
import net.geoprism.registry.conversion.TermConverter;
import net.geoprism.registry.model.ValueStrategy;

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
  public void delete()
  {
    super.delete();
    
    // TODO: HEADS UP
    
//    MdAttributeConcreteDAOIF mdAttributeConcreteDAOIF = this.getMdAttribute(serverType.getMdBusiness(), attributeName);
//
//    if (mdAttributeConcreteDAOIF != null)
//    {
//      if (mdAttributeConcreteDAOIF instanceof MdAttributeTermDAOIF || mdAttributeConcreteDAOIF instanceof MdAttributeMultiTermDAOIF)
//      {
//        String attributeTermKey = TermConverter.buildtAtttributeKey(serverType.getMdBusiness().getTypeName(), mdAttributeConcreteDAOIF.definesAttribute());
//
//        try
//        {
//          Classifier attributeTerm = Classifier.getByKey(attributeTermKey);
//          attributeTerm.delete();
//        }
//        catch (DataNotFoundException e)
//        {
//        }
//      }
//
//      mdAttributeConcreteDAOIF.getBusinessDAO().delete();
//    }

  }
  
  @Override
  protected void populate(MdAttributeConcreteDAO mdAttribute)
  {
    // TODO Auto-generated method stub
    super.populate(mdAttribute);
  }
  
  @Override
  public void fromDTO(AttributeType dto)
  {
    super.fromDTO(dto);
    
    // TODO: Heads up
//  mdAttribute = new MdAttributeTerm();
//  MdAttributeTerm mdAttributeTerm = (MdAttributeTerm) mdAttribute;
//
//  MdBusiness classifierMdBusiness = MdBusiness.getMdBusiness(Classifier.CLASS);
//  mdAttributeTerm.setMdBusiness(classifierMdBusiness);
//  // TODO implement support for multi-term
//  // mdAttribute = new MdAttributeMultiTerm();
//  // MdAttributeMultiTerm mdAttributeMultiTerm =
//  // (MdAttributeMultiTerm)mdAttribute;
//  //
//  // MdBusiness classifierMdBusiness =
//  // MdBusiness.getMdBusiness(Classifier.CLASS);
//  // mdAttributeMultiTerm.setMdBusiness(classifierMdBusiness);
  }
  
  @Override
  public AttributeType toDTO()
  {
    // TODO: HEADS UP
    // Populate root
    return new org.commongeoregistry.adapter.metadata.AttributeTermType(this.getCode(), getLocalizedLabel(), getLocalizedDescription(), isAppliedToDb(), isNew(), isAppliedToDb());
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
