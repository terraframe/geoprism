/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Geoprism(tm).
 *
 * Geoprism(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Geoprism(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Geoprism(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism.ontology;

import java.util.LinkedList;
import java.util.List;

import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;

public class ClassifierProblem extends ClassifierProblemBase 
{
  private static final long serialVersionUID = 274326646;

  public ClassifierProblem()
  {
    super();
  }

  public List<ClassifierProblemView> getViews()
  {
    Classifier classifier = this.getClassifier();

    String label = classifier.getDisplayLabel().getValue();

    List<ClassifierProblemType> problemTypes = this.getProblemType();

    List<ClassifierProblemView> list = new LinkedList<ClassifierProblemView>();

    for (ClassifierProblemType type : problemTypes)
    {
      String problemText = type.getDescription().getValue().replace("{0}", label);

      ClassifierProblemView view = new ClassifierProblemView();
      view.setConcreteId(this.getOid());
      view.setClassifierId(this.getClassifierId());
      view.setProblem(problemText);
      view.setProblemName(type.getEnumName());

      list.add(view);
    }

    return list;
  }

  public static void createProblems(Classifier classifier, ClassifierProblemType... types)
  {
    for (ClassifierProblemType type : types)
    {
      ClassifierProblem problem = new ClassifierProblem();
      problem.setClassifier(classifier);
      problem.addProblemType(type);
      problem.apply();
    }
  }

  public static void deleteProblems(Classifier classifier, ClassifierProblemType... types)
  {
    ClassifierProblemQuery query = new ClassifierProblemQuery(new QueryFactory());
    query.WHERE(query.getClassifier().EQ(classifier));

    if (types != null && types.length > 0)
    {
      query.AND(query.getProblemType().containsAny(types));
    }

    OIterator<? extends ClassifierProblem> iterator = query.getIterator();

    try
    {
      while (iterator.hasNext())
      {
        ClassifierProblem problem = iterator.next();
        problem.delete();
      }
    }
    finally
    {
      iterator.close();
    }
  }

  public static boolean hasEntry(Classifier classifier, ClassifierProblemType type)
  {
    ClassifierProblemQuery query = new ClassifierProblemQuery(new QueryFactory());
    query.WHERE(query.getClassifier().EQ(classifier));
    query.AND(query.getProblemType().containsExactly(type));

    return ( query.getCount() > 0 );
  }

}
