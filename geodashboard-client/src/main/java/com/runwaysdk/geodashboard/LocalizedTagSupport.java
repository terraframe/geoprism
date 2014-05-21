package com.runwaysdk.geodashboard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Locale;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.runwaysdk.ClientSession;
import com.runwaysdk.constants.ClientConstants;
import com.runwaysdk.controller.tag.develop.AttributeAnnotation;
import com.runwaysdk.controller.tag.develop.TagAnnotation;
import com.runwaysdk.geodashboard.localization.LocalizationFacadeDTO;
import com.runwaysdk.session.InvalidSessionExceptionDTO;

@TagAnnotation(bodyContent = "empty", name = "localize", description = "Localizes the given key, respecting bundle precedence")
public class LocalizedTagSupport extends SimpleTagSupport
{
  private String key;

  private String var;

  @AttributeAnnotation(description = "The key for the localized value", rtexprvalue = true)
  public String getKey()
  {
    return key;
  }

  public void setKey(String key)
  {
    this.key = key;
  }

  @AttributeAnnotation(description = "The variable to store the localized key in", required = false, rtexprvalue = false)
  public String getVar()
  {
    return var;
  }

  public void setVar(String var)
  {
    this.var = var;
  }

  @Override
//  @Request
  public void doTag() throws JspException, IOException
  {
    PageContext pageContext = (PageContext) this.getJspContext();

    ClientSession clientSession = (ClientSession) pageContext.findAttribute(ClientConstants.CLIENTSESSION);

    if (clientSession == null)
    {
      clientSession = this.createAnonymousSession(pageContext);
    }

    try
    {
      this.write(pageContext, clientSession);
    }
    catch (InvalidSessionExceptionDTO e1)
    {
      try
      {
        clientSession = this.createAnonymousSession(pageContext);

        this.write(pageContext, clientSession);
      }
      catch (Exception e2)
      {
        this.write(pageContext, this.getKey());
      }
    }

  }

  private void write(PageContext pageContext, ClientSession clientSession) throws IOException
  {
    String localizedValue = LocalizationFacadeDTO.getFromBundles(clientSession.getRequest(), getKey());

    this.write(pageContext, localizedValue);
  }

  private void write(PageContext pageContext, String localizedValue) throws IOException
  {
    if (var == null)
    {
      JspWriter out = pageContext.getOut();

      out.write(localizedValue);
    }
    else
    {
      pageContext.getRequest().setAttribute(var, localizedValue);
    }
  }

  @SuppressWarnings("unchecked")
  private ClientSession createAnonymousSession(PageContext pageContext)
  {
    ArrayList<Locale> arrayList = new ArrayList<Locale>();
    Enumeration<Locale> locales = pageContext.getRequest().getLocales();

    while (locales.hasMoreElements())
    {
      arrayList.add(locales.nextElement());
    }

    Locale[] array = arrayList.toArray(new Locale[arrayList.size()]);
    
    return ClientSession.createAnonymousSession(array);
  }
}
