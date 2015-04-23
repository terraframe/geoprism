package com.runwaysdk.geodashboard.dashboard;

import java.util.Comparator;

import com.runwaysdk.business.ontology.Term;
import com.runwaysdk.generation.loader.Reloadable;

public class TermComparator implements Comparator<Term>, Reloadable
{

  @Override
  public int compare(Term o1, Term o2)
  {
    return o1.getDisplayLabel().getValue().compareTo(o2.getDisplayLabel().getValue());
  }

}
