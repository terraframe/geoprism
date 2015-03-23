package com.runwaysdk.geodashboard.ontology;

import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.OR;
import com.runwaysdk.query.QueryFactory;

public class ClassifierSynonym extends ClassifierSynonymBase implements com.runwaysdk.generation.loader.Reloadable
{
  private static final long serialVersionUID = -1215181333;
  
  private static final String KEY_CONCATENATOR    = ".";
  
  public ClassifierSynonym()
  {
    super();
  }
  
  public void apply()
  {
    Classifier classifier = this.getClassifier();
    
    checkForAmbiguousSynonyms(classifier, this);
    
    super.apply();
    
    this.addIsSynonymFor(classifier).apply();
  }
  
  @Override
  public String buildKey()
  {
    return buildKey(this.getClassifier(), this.getDisplayLabel().getDefaultValue());
  }
  
  public static String buildKey(Classifier _classifier, String synonymId)
  {
    return _classifier.getKey() + KEY_CONCATENATOR + synonymId;
  }
  
  /**
   * Check for a potential synonym match with a parent or a child term, either of which could result in an ambiguous synonym match.
   * 
   * @param _classifier
   * @param _synonym
   */
  private static void checkForAmbiguousSynonyms(Classifier _classifier, ClassifierSynonym _synonym)
  {
    QueryFactory qf = new QueryFactory();
        
    ClassifierQuery classifier1Q = new ClassifierQuery(qf);
    ClassifierQuery classifier2Q = new ClassifierQuery(qf);
    ClassifierAllPathsTableQuery allPathsQ = new ClassifierAllPathsTableQuery(qf);
    ClassifierSynonymQuery synonymQ = new ClassifierSynonymQuery(qf);
    
    synonymQ.
      WHERE(synonymQ.getDisplayLabel().localize().EQ(_synonym.getDisplayLabel().getValue()));
    
    classifier1Q.
      WHERE(classifier1Q.getId().EQ(_classifier.getId()));

    allPathsQ.WHERE(OR.get(allPathsQ.getParentTerm().EQ(classifier1Q), allPathsQ.getChildTerm().EQ(classifier1Q)));
    
    classifier2Q.
      WHERE(classifier2Q.hasSynonym(synonymQ).
      AND(OR.get(classifier2Q.EQ(allPathsQ.getChildTerm()), classifier2Q.EQ(allPathsQ.getParentTerm()))));   
    
    OIterator<? extends Classifier> classifier2I = classifier2Q.getIterator();
    for (Classifier classifier : classifier2I)
    {
      PossibleAmbiguousSynonym warning = new PossibleAmbiguousSynonym();
      warning.setClassifierLabel(classifier.getDisplayLabel().getValue());
      warning.setSynonymLabel(_synonym.getDisplayLabel().getValue());
      warning.apply();
      warning.throwIt();
    }
    
  }
  
}
