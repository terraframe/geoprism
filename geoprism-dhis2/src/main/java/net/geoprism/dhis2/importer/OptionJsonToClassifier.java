package net.geoprism.dhis2.importer;

import org.json.JSONObject;

import net.geoprism.ontology.Classifier;

public class OptionJsonToClassifier
{
  private JSONObject json;
  
  public OptionJsonToClassifier(JSONObject json)
  {
    this.json = json;
  }
  
  public void apply()
  {
    Classifier classy = new Classifier();
    classy.getDisplayLabel().setValue(json.getString("name"));
    classy.setClassifierId(json.getString("id"));
    classy.setClassifierPackage(json.getString("id"));
    classy.apply();
  }
}
