/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism.ontology;

import com.runwaysdk.business.ontology.Term;
import com.runwaysdk.business.ontology.TermAndRel;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.OR;
import com.runwaysdk.query.QueryFactory;

public class ClassifierSynonym extends ClassifierSynonymBase implements com.runwaysdk.generation.loader.Reloadable
{
  private static final long   serialVersionUID = -1215181333;

  private static final String KEY_CONCATENATOR = ".";

  public ClassifierSynonym()
  {
    super();
  }

  public void apply()
  {
    Classifier classifier = this.getClassifier();

    ClassifierSynonym.checkForAmbiguousSynonyms(classifier, this);

    super.apply();

    if (this.isNew())
    {
      this.addIsSynonymFor(classifier).apply();
    }
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
   * Check for a potential synonym match with a parent or a child term, either of which could result in an ambiguous
   * synonym match.
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

    synonymQ.WHERE(synonymQ.getDisplayLabel().localize().EQ(_synonym.getDisplayLabel().getValue()));

    classifier1Q.WHERE(classifier1Q.getId().EQ(_classifier.getId()));

    allPathsQ.WHERE(OR.get(allPathsQ.getParentTerm().EQ(classifier1Q), allPathsQ.getChildTerm().EQ(classifier1Q)));

    classifier2Q.WHERE(classifier2Q.hasSynonym(synonymQ).AND(OR.get(classifier2Q.EQ(allPathsQ.getChildTerm()), classifier2Q.EQ(allPathsQ.getParentTerm()))));

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

  /**
   * MdMethod used for creating Synonyms.
   * 
   * @param termId
   * @param name
   * @return
   */
  @Transaction
  public static TermAndRel createSynonym(ClassifierSynonym synonym, String classifierId)
  {
    Classifier classifier = Classifier.get(classifierId);

    String keyName = classifierId + "-" + synonym.getDisplayLabel().getValue().trim().replaceAll("\\s+", "");

    synonym.setClassifier(classifier);
    synonym.setKeyName(keyName);
    synonym.apply();

    ClassifierHasSynonym relationship = synonym.getAllIsSynonymForRel().getAll().get(0);

    return new TermAndRel(synonym, ClassifierHasSynonym.CLASS, relationship.getId());
  }

  public static Term getRoot()
  {
    return Classifier.getRoot();
  }
}
