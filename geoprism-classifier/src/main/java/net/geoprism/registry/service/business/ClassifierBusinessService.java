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

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.runwaysdk.business.ontology.TermAndRel;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.transaction.Transaction;

import net.geoprism.ontology.Classifier;
import net.geoprism.ontology.ClassifierSynonym;

@Service
public class ClassifierBusinessService implements ClassifierBusinessServiceIF
{
  @Transaction
  @Override
  public String createSynonym(String classifierId, String label)
  {
    try
    {
      Classifier classifier = Classifier.get(classifierId);

      ClassifierSynonym synonym = new ClassifierSynonym();
      synonym.getDisplayLabel().setValue(label);

      TermAndRel tr = ClassifierSynonym.createSynonym(synonym, classifierId);

      JSONObject object = new JSONObject();
      object.put("synonymId", tr.getTerm().getOid());
      object.put("label", classifier.getDisplayLabel().getValue());

      return object.toString();
    }
    catch (JSONException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

  @Transaction
  @Override
  public void deleteSynonym(String synonymId)
  {
    ClassifierSynonym synonym = ClassifierSynonym.get(synonymId);
    synonym.delete();
  }
}
