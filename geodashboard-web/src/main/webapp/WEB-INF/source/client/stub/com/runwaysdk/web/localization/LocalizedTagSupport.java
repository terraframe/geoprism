package com.runwaysdk.web.localization;

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
import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.controller.tag.develop.AttributeAnnotation;
import com.runwaysdk.controller.tag.develop.TagAnnotation;
import com.runwaysdk.session.Request;

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
  @Request
  @SuppressWarnings("unchecked")
  public void doTag() throws JspException, IOException
  {
    PageContext pageContext = (PageContext)this.getJspContext();
    JspWriter out = pageContext.getOut();
    ClientSession clientSession = (ClientSession) pageContext.findAttribute(ClientConstants.CLIENTSESSION);
    
    if (clientSession==null)
    {
      ArrayList<Locale> arrayList = new ArrayList<Locale>();
      Enumeration<Locale> locales = pageContext.getRequest().getLocales();
      while (locales.hasMoreElements())
      {
        arrayList.add(locales.nextElement());
      }
      Locale[] array = arrayList.toArray(new Locale[arrayList.size()]);
      
      clientSession = ClientSession.createAnonymousSession(array);
    }
    
    ClientRequestIF request = clientSession.getRequest();
    
    // TODO : localize
    out.write(getKey());
    
//    String localizedValue = LocalizationFacadeDTO.getFromBundles(request, getKey());
//    if (var == null)
//    {
//      out.write(localizedValue);
//    }
//    else
//    {
//      pageContext.setAttribute(var, localizedValue);
//    }
  }
}
