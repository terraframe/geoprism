import com.runwaysdk.dataaccess.MdRelationshipDAOIF;
import com.runwaysdk.dataaccess.metadata.MdRelationshipDAO;
import com.runwaysdk.geodashboard.ontology.ClassifierAttributeRoot;
import com.runwaysdk.session.Request;


public class Sandbox
{
  @Request
  public static void main(String[] args)
  {
    MdRelationshipDAOIF mdRelationshipDAOIF = MdRelationshipDAO.getMdRelationshipDAO(ClassifierAttributeRoot.CLASS);
    
    mdRelationshipDAOIF.printAttributes();
    
//    NoLayersExceptionDTO ex = new NoLayersExceptionDTO(this.getClientRequest(), this.req.getLocale());
//    String msg = ex.getLocalizedMessage();
  }
}
