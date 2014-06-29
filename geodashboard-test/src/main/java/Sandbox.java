import com.runwaysdk.geodashboard.ontology.Classifier;
import com.runwaysdk.geodashboard.ontology.ClassifierIsARelationship;
import com.runwaysdk.session.Request;


public class Sandbox
{
  @Request
  public static void main(String[] args)
  {
    Classifier.getStrategy().initialize(ClassifierIsARelationship.CLASS);
    Classifier.getStrategy().initialize(ClassifierIsARelationship.CLASS);
    Classifier.getStrategy().initialize(ClassifierIsARelationship.CLASS);
    Classifier.getStrategy().initialize(ClassifierIsARelationship.CLASS);
    Classifier.getStrategy().initialize(ClassifierIsARelationship.CLASS);
  }
}
