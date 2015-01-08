package com.runwaysdk.geodashboard.ontology;

public class ClassifierHasSynonym extends ClassifierHasSynonymBase implements com.runwaysdk.generation.loader.Reloadable
{
  private static final long serialVersionUID = 969760007;
  
  public ClassifierHasSynonym(String parentId, String childId)
  {
    super(parentId, childId);
  }
  
  public ClassifierHasSynonym(com.runwaysdk.geodashboard.ontology.Classifier parent, com.runwaysdk.geodashboard.ontology.ClassifierSynonym child)
  {
    this(parent.getId(), child.getId());
  }
  
}
