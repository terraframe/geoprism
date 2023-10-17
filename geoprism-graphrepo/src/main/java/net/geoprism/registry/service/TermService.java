package net.geoprism.registry.service;

import org.commongeoregistry.adapter.Term;
import org.commongeoregistry.adapter.dataaccess.LocalizedValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.runwaysdk.session.Request;
import com.runwaysdk.session.RequestType;

import net.geoprism.graphrepo.permission.UserPermissionService.RepoPermissionAction;
import net.geoprism.ontology.Classifier;
import net.geoprism.registry.business.TermBusinessServiceIF;
import net.geoprism.registry.conversion.TermConverter;

@Service
public class TermService implements TermServiceIF
{
  @Autowired
  private TermBusinessServiceIF service;

  /**
   * Creates a new {@link Term} object and makes it a child of the term with the
   * given code.
   * 
   * @param sessionId
   * @param parentTemCode
   *          The code of the parent [@link Term}.
   * @param termJSON
   *          JSON of the term object.
   * 
   * @return Newly created {@link Term} object.
   */
  @Override
  @Request(RequestType.SESSION)
  public Term createTerm(String sessionId, String parentTermCode, String termJSON)
  {
    JsonObject termJSONobj = JsonParser.parseString(termJSON).getAsJsonObject();

    LocalizedValue label = LocalizedValue.fromJSON(termJSONobj.get(Term.JSON_LOCALIZED_LABEL).getAsJsonObject());

    Term term = new Term(termJSONobj.get(Term.JSON_CODE).getAsString(), label, new LocalizedValue(""));

    return this.service.createTerm(parentTermCode, term);
  }

  /**
   * Creates a new {@link Term} object and makes it a child of the term with the
   * given code.
   * 
   * @param sessionId
   * @param parentTermCode
   *          TODO
   * @param termJSON
   *          JSON of the term object.
   * @return Updated {@link Term} object.
   */
  @Override
  @Request(RequestType.SESSION)
  public Term updateTerm(String sessionId, String parentTermCode, String termJSON)
  {
    JsonObject termJSONobj = JsonParser.parseString(termJSON).getAsJsonObject();

    String termCode = termJSONobj.get(Term.JSON_CODE).getAsString();

    LocalizedValue value = LocalizedValue.fromJSON(termJSONobj.get(Term.JSON_LOCALIZED_LABEL).getAsJsonObject());

    return this.service.updateTerm(parentTermCode, termCode, value);
  }

  /**
   * Deletes the {@link Term} with the given code. All children codoe will be
   * deleted.
   * 
   * @param sessionId
   * @param parentTermCode
   *          TODO
   * @param geoObjectTypeCode
   * @param attributeTypeJSON
   */
  @Override
  @Request(RequestType.SESSION)
  public void deleteTerm(String sessionId, String parentTermCode, String termCode)
  {
    String parentClassifierKey = TermConverter.buildClassifierKeyFromTermCode(parentTermCode);

    Classifier parent = Classifier.getByKey(parentClassifierKey);

    TermConverter.enforceTermPermissions(parent, RepoPermissionAction.DELETE);

    this.service.deleteTerm(TermConverter.buildTermFromClassifier(parent), termCode);

  }
}
