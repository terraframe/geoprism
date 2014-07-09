package com.runwaysdk.geodashboard.ontology;

import org.apache.commons.lang.StringUtils;

import com.runwaysdk.business.Relationship;
import com.runwaysdk.business.ontology.OntologyStrategyIF;
import com.runwaysdk.business.ontology.Term;
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
  
  /**
   * Persists this Classifier, performs validation on its values, and generates a UniqueId from the display label, if necessary.
   */
  @Override
  @Transaction
  public void apply()
  {
    // If they didn't specify a UniqueId we can figure one out from the DisplayLabel
    if (this.getClassifierId() == null || this.getClassifierId().length() == 0) {
      String uniqueId = this.getDisplayLabel().getValue().trim().replaceAll("\\s+","");
      this.setClassifierId(uniqueId);
    }
    
    super.apply();
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
    Classifier parent = Classifier.get(parentId);
    
    // If they didn't specify a package we can attempt to figure one out for them.
    if (dto.getClassifierPackage() == null || dto.getClassifierPackage().length() == 0) {
      String camelUniqueId = StringUtils.uncapitalize(parent.getClassifierId().trim().replaceAll("\\s+",""));
      dto.setClassifierPackage(parent.getClassifierPackage() + KEY_CONCATENATOR + camelUniqueId);
    }
    
    dto.apply();
    
    Relationship rel = dto.addLink(parent, ClassifierIsARelationship.CLASS);
    
    return new TermAndRel(dto, ClassifierIsARelationship.CLASS, rel.getId());
  }
  
  public static OntologyStrategyIF createStrategy()
  {
    return new DatabaseAllPathsStrategy();
  }
  
  public static Classifier getRoot() {
    return (Classifier) Term.getRoot(Classifier.CLASS);
  }
}
