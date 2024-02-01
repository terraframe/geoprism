package net.geoprism.registry.graph;

import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeMultiTermDAOIF;
import com.runwaysdk.dataaccess.MdAttributeTermDAOIF;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.dataaccess.transaction.Transaction;

import net.geoprism.ontology.Classifier;
import net.geoprism.registry.conversion.TermConverter;

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
  
}
