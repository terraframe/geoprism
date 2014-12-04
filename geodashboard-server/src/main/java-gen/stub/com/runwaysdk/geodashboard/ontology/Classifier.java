package com.runwaysdk.geodashboard.ontology;

import org.apache.commons.lang.StringUtils;

import com.runwaysdk.business.Relationship;
import com.runwaysdk.business.ontology.OntologyStrategyIF;
import com.runwaysdk.business.ontology.Term;
import com.runwaysdk.business.ontology.TermAndRel;
import com.runwaysdk.dataaccess.metadata.MdAttributeTermDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.OR;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.system.gis.geo.LocatedIn;
import com.runwaysdk.system.metadata.ontology.DatabaseAllPathsStrategy;

public class Classifier extends ClassifierBase implements com.runwaysdk.generation.loader.Reloadable
{
  private static final long serialVersionUID = 1158111601;
  
  private static final String KEY_CONCATENATOR    = ".";
  
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
    if (this.getClassifierId() == null || this.getClassifierId().length() == 0) 
    {
      String uniqueId = this.getDisplayLabel().getValue().trim().replaceAll("\\s+","");
      this.setClassifierId(uniqueId);
    }
        
    super.apply();
    
    if (this.isNew())
    {
      this.addTerm(ClassifierIsARelationship.CLASS);
    }
  }
  
  public static String buildKey(String pkg, String id)
  {
    return pkg + KEY_CONCATENATOR + id;
  }
  
  @Override
  public String buildKey()
  {
    return Classifier.buildKey(this.getClassifierPackage(), this.getClassifierId());
  }
 
  /**
   * Returns the <code>Classifier</code> object with a label or synonym that matches the given term. Searches all nodes
   * that are children of the given attribute root nodes including the root nodes.
   *  
   * @param sfTermToMatch
   * @param mdAttributeTermDAO
   * @return the <code>Classifier</code> object with a label or synonym that matches the given term.
   */
  public static Classifier findMatchingTerm(String sfTermToMatch, MdAttributeTermDAO mdAttributeTermDAO)
  {
      QueryFactory qf = new QueryFactory();
   
      ClassifierQuery classifierRootQ = new ClassifierQuery(qf);
      ClassifierAttributeRootQuery carQ = new ClassifierAttributeRootQuery(qf);
      ClassifierQuery classifierQ = new ClassifierQuery(qf);
      ClassifierAllPathsTableQuery allPathsQ = new ClassifierAllPathsTableQuery(qf);
      ClassifierSynonymQuery synonymQ = new ClassifierSynonymQuery(qf);
  
      carQ.
      WHERE(carQ.getParent().EQ(mdAttributeTermDAO));
      
      classifierRootQ.
      WHERE(classifierRootQ.classifierAttributeRoots(carQ));
      
      allPathsQ.
      WHERE(allPathsQ.getParentTerm().EQ(classifierRootQ));
      
      synonymQ.
      WHERE(synonymQ.getDisplayLabel().localize().EQ(sfTermToMatch));
      
      classifierQ.
      WHERE(OR.get(classifierQ.getDisplayLabel().localize().EQ(sfTermToMatch), classifierQ.hasSynonym(synonymQ)).
      AND(classifierQ.EQ(allPathsQ.getChildTerm())));
      
      OIterator<? extends Classifier> i = classifierQ.getIterator();
      try
      {
        for (Classifier classifier : i)
        {     
  System.out.println("Heads up: FOUND: "+classifier.getClassifierId());
          return classifier;
        }
      }
      finally
      {
        i.close();
      }
  System.out.println("Heads up: NOT FOUND: "+null);
      return null;
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
      
      if (Classifier.getRoot().equals(parent)) {
        dto.setClassifierPackage(camelUniqueId);
      }
      else {
        dto.setClassifierPackage(parent.getClassifierPackage() + KEY_CONCATENATOR + camelUniqueId);
      }
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
