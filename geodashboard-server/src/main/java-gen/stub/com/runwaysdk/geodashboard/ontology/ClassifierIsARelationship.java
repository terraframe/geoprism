package com.runwaysdk.geodashboard.ontology;

public class ClassifierIsARelationship extends ClassifierIsARelationshipBase implements com.runwaysdk.generation.loader.Reloadable
{
  private static final long serialVersionUID = -985779118;
  
  public ClassifierIsARelationship(String parentId, String childId)
  {
    super(parentId, childId);
  }
  
  public ClassifierIsARelationship(com.runwaysdk.geodashboard.ontology.Classifier parent, com.runwaysdk.geodashboard.ontology.Classifier child)
  {
    this(parent.getId(), child.getId());
  }
  
  @Override
  public String buildKey()
  {
    return this.getParentId() + this.getChildId();
  }
  
}
