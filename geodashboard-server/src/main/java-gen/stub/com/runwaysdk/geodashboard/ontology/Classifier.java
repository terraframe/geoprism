package com.runwaysdk.geodashboard.ontology;

import com.runwaysdk.business.ontology.OntologyStrategyIF;
import com.runwaysdk.business.ontology.TermAndRel;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.system.metadata.ontology.DatabaseAllPathsStrategy;

public class Classifier extends ClassifierBase implements com.runwaysdk.generation.loader.Reloadable
{
  private static final long serialVersionUID = 1158111601;
  
  private static final String KEY_CONCATENATOR    = ".";
  
  public static final String ROOT_KEY             = "ROOT" + KEY_CONCATENATOR + "ROOT";
  
  public Classifier()
  {
    super();
  }
  
  @Override
  public String toString()
  {
    String template = "[%s : %s] - %s.";
    Object[] args = new Object[] { this.getClassDisplayLabel(), this.buildKey(), this.getDisplayLabel().getValue() };
    return String.format(template, args);
  }
  
  private static String buildKey(String pkg, String id)
  {
    return pkg + KEY_CONCATENATOR + id;
  }
  
  @Override
  public String buildKey()
  {
    return Classifier.buildKey(this.getClassifierPackage(), this.getClassifierId());
  }
  
  /**
   * MdMethod used for creating Classifiers.
   * 
   * @param termId
   * @param name
   * @return
   */
  @Transaction
  public static TermAndRel create(Classifier dto, String parentId)
  {
    dto.apply();
    
    Classifier parent = Classifier.get(parentId);
    
    ClassifierIsARelationship rel = parent.addIsAChild(dto);
    rel.apply();
    
    return new TermAndRel(dto, ClassifierIsARelationship.CLASS, rel.getId());
  }
  
  /**
    * Specify the root ontology node.
    */
  public static Classifier getRoot()
  {
    return Classifier.getByKey(ROOT_KEY);
  }
  
  public static OntologyStrategyIF createStrategy()
  {
    return new DatabaseAllPathsStrategy();
  }

  public static void configureStrategy(OntologyStrategyIF strategy)
  {
    if (strategy instanceof DatabaseAllPathsStrategy) {
      ( (DatabaseAllPathsStrategy) strategy ).configure(CLASS);
    }
  }
}
