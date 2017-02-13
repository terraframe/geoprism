package net.geoprism.dhis2.importer;

import org.json.JSONArray;
import org.json.JSONObject;

import net.geoprism.ontology.Classifier;
import net.geoprism.ontology.ClassifierIsARelationship;

public class CategoryJsonToClassifier
{
  private JSONObject json;
  
  private Classifier classy;
  
  public CategoryJsonToClassifier(JSONObject json)
  {
    this.json = json;
  }
  
  public void apply()
  {
    this.classy = new Classifier();
    this.classy.getDisplayLabel().setValue(json.getString("name"));
    this.classy.setClassifierId(json.getString("id"));
    this.classy.setClassifierPackage(json.getString("id"));
    this.classy.setCategory(true);
    this.classy.apply();
    
    Classifier parent = Classifier.getRoot();
    this.classy.addLink(parent, ClassifierIsARelationship.CLASS);
  }
  
  public void applyCategoryRelationships()
  {
    this.classy = Classifier.getByKey(json.getString("id") + Classifier.KEY_CONCATENATOR + json.getString("id"));
    
    JSONArray options = json.getJSONArray("categoryOptions");
    for (int i = 0; i < options.length(); ++i)
    {
      JSONObject jsonOption = options.getJSONObject(i);
      Classifier option = Classifier.getByKey(jsonOption.getString("id") + Classifier.KEY_CONCATENATOR + jsonOption.getString("id"));
      
      option.addLink(classy, ClassifierIsARelationship.CLASS);
    }
  }
}
