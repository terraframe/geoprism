package net.geoprism.registry.service.business;

import org.commongeoregistry.adapter.Term;
import org.commongeoregistry.adapter.dataaccess.LocalizedValue;
import org.springframework.stereotype.Component;

@Component
public interface TermBusinessServiceIF
{

  public Term createTerm(String parentTermCode, Term term);

  public Term updateTerm(String parentTermCode, String termCode, LocalizedValue label);

  public void deleteTerm(Term parent, String termCode);

}
