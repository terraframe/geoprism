/**
 * Copyright (c) 2023 TerraFrame, Inc. All rights reserved.
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
package net.geoprism.registry.service.business;

import org.commongeoregistry.adapter.Term;
import org.commongeoregistry.adapter.dataaccess.LocalizedValue;
import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.runwaysdk.dataaccess.transaction.Transaction;

import net.geoprism.ontology.Classifier;
import net.geoprism.registry.command.CacheEventType;
import net.geoprism.registry.command.GeoObjectTypeCacheEventCommand;
import net.geoprism.registry.conversion.TermConverter;

@Service
public class TermBusinessService implements TermBusinessServiceIF
{
  
  @Override
  public Term createTerm(String parentTermCode, String termJSON)
  {
    JsonObject termJSONobj = JsonParser.parseString(termJSON).getAsJsonObject();

    LocalizedValue label = LocalizedValue.fromJSON(termJSONobj.get(Term.JSON_LOCALIZED_LABEL).getAsJsonObject());

    Term term = new Term(termJSONobj.get(Term.JSON_CODE).getAsString(), label, new LocalizedValue(""));

    return this.createTerm(parentTermCode, term);
  }


  @Override
  public Term createTerm(String parentTermCode, Term term)
  {
    Classifier classifier = TermConverter.createClassifierFromTerm(parentTermCode, term);

    TermConverter termBuilder = new TermConverter(classifier.getKeyName());

    // TODO: Optimize so the entire cache doesn't need to be rebuilt
    // Just the geo object type whichs terms were updated
    // We don't
    new GeoObjectTypeCacheEventCommand(null, CacheEventType.VIEW).doIt();

    return termBuilder.build();
  }

  @Override
  @Transaction
  public Term updateTerm(String parentTermCode, String termCode, LocalizedValue value)
  {
    Classifier classifier = TermConverter.updateClassifier(parentTermCode, termCode, value);

    TermConverter termBuilder = new TermConverter(classifier.getKeyName());

    // TODO: Optimize so the entire cache doesn't need to be rebuilt
    // Just the geo object type whichs terms were updated
    // We don't
    new GeoObjectTypeCacheEventCommand(null, CacheEventType.VIEW).doIt();

    return termBuilder.build();
  }
  
  @Override
  public Term updateTerm(String parentTermCode, String termJSON)
  {
    JsonObject termJSONobj = JsonParser.parseString(termJSON).getAsJsonObject();

    String termCode = termJSONobj.get(Term.JSON_CODE).getAsString();

    LocalizedValue value = LocalizedValue.fromJSON(termJSONobj.get(Term.JSON_LOCALIZED_LABEL).getAsJsonObject());

    return this.updateTerm(parentTermCode, termCode, value);
  }


  @Override
  @Transaction
  public void deleteTerm(Term parent, String termCode)
  {
    String parentClassifierKey = TermConverter.buildClassifierKeyFromTermCode(parent.getCode());

    Classifier parentClassifier = Classifier.getByKey(parentClassifierKey);

    String classifierKey = Classifier.buildKey(parentClassifier.getKey(), termCode);

    Classifier classifier = Classifier.getByKey(classifierKey);
    classifier.delete();

    // TODO: Optimize so the entire cache doesn't need to be rebuilt
    // Just the geo object type whichs terms were updated
    // We don't
    new GeoObjectTypeCacheEventCommand(null, CacheEventType.VIEW).doIt();
  }

}
