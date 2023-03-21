package net.geoprism.classifier;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import com.runwaysdk.business.ontology.TermAndRel;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.transaction.Transaction;

import net.geoprism.ontology.Classifier;
import net.geoprism.ontology.ClassifierSynonym;

@Component
public class ClassifierBusinessService implements ClassifierBusinessServiceIF
{
  @Transaction
  @Override
  public String createSynonym(String classifierId, String label)
  {
    try
    {
      Classifier classifier = Classifier.get(classifierId);

      ClassifierSynonym synonym = new ClassifierSynonym();
      synonym.getDisplayLabel().setValue(label);

      TermAndRel tr = ClassifierSynonym.createSynonym(synonym, classifierId);

      JSONObject object = new JSONObject();
      object.put("synonymId", tr.getTerm().getOid());
      object.put("label", classifier.getDisplayLabel().getValue());

      return object.toString();
    }
    catch (JSONException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

  @Transaction
  @Override
  public void deleteSynonym(String synonymId)
  {
    ClassifierSynonym synonym = ClassifierSynonym.get(synonymId);
    synonym.delete();
  }
}
