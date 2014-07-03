package com.runwaysdk.geodashboard.ontology;

import java.util.Iterator;

import org.apache.commons.lang.StringUtils;

import com.runwaysdk.business.Relationship;
import com.runwaysdk.business.ontology.OntologyStrategyIF;
import com.runwaysdk.business.ontology.Term;
import com.runwaysdk.business.ontology.TermAndRel;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.logging.LogLevel;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.system.gis.geo.DeleteRootException;
import com.runwaysdk.system.gis.geo.GeoEntity;
import com.runwaysdk.system.gis.geo.ImmutableRootException;
import com.runwaysdk.system.gis.geo.LocatedIn;
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
  public void apply()
  {
    applyInternal();
  }

  /**
   * Persists this Classifier, performs validation on its values, and generates a UniqueId from the display label, if necessary.
   */
  @Transaction
  public void applyInternal()
  {
    if (this.getKey().equals(ROOT_KEY))
    {
      ImmutableRootException exception = new ImmutableRootException("Cannot modify the root Classifier.");
      exception.setRootName(ROOT_KEY);
      exception.apply();
      
      throw exception;
    }
    
    // If they didn't specify a UniqueId we can figure one out from the DisplayLabel
    if (this.getClassifierId() == null || this.getClassifierId().length() == 0) {
      String uniqueId = this.getDisplayLabel().getValue().trim().replaceAll("\\s+","");
      this.setClassifierId(uniqueId);
    }
    
    super.apply();
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
  
  @Override
  @Transaction
  @com.runwaysdk.logging.Log(level = LogLevel.DEBUG)
  public void delete()
  {
    if (this.equals(Classifier.getRoot()))
    {
      DeleteRootException exception = new DeleteRootException("Cannot delete the root Classifier");
      exception.setRootName(Classifier.getRoot().getDisplayLabel().getValue());
      exception.apply();
      
      throw exception;
    }
    
    // 1. Delete this entity from the all paths strategy
    this.removeTerm(ClassifierIsARelationship.CLASS);
    
    // 2. Recursively delete all children.
    if (!this.isLeaf(ClassifierIsARelationship.CLASS))
    {
      @SuppressWarnings("unchecked")
      Iterator<Term> children = this.getDirectDescendants(ClassifierIsARelationship.CLASS).iterator();

      while (children.hasNext())
      {
        Term child = children.next();

        boolean hasSingleParent = child.getDirectAncestors(ClassifierIsARelationship.CLASS).getAll().size() == 1;

        if (hasSingleParent)
        {
          children.remove();
          child.delete();
        }
      }
    }
    
    // 3. Delete this.
    super.delete();
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
