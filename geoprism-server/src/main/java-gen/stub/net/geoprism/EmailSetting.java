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
package net.geoprism;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.mail.AuthenticationFailedException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.attributes.AttributeValueException;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;

import net.geoprism.email.EmailSendAuthenticationException;
import net.geoprism.email.EmailSendException;
import net.geoprism.email.InvalidEmailSettings;
import net.geoprism.email.InvalidToRecipient;
import net.geoprism.localization.LocalizationFacade;

public class EmailSetting extends EmailSettingBase implements com.runwaysdk.generation.loader.Reloadable
{
  private static final long serialVersionUID = -1631634656;
  
  private static Logger logger = LoggerFactory.getLogger(EmailSetting.class);
  
  public EmailSetting()
  {
    super();
  }
  
  
  /**
   * A reusable, easy way to send an email using the saved default EmailSetting. If toAddresses is null, they will be read from the default settings.
   * 
   * @param subject
   * @param body
   * @param toAddresses
   */
  public static void sendEmail(String subject, String body, String[] toAddresses)
  {
    EmailSetting settings = EmailSetting.getDefault();
    if (settings == null || settings.getPort() == null || settings.getServer() == null || settings.getUsername() == null || settings.getPassword() == null)
    {
      throw new InvalidEmailSettings();
    }
    
    if (toAddresses == null)
    {
      toAddresses = StringUtils.split(settings.getTo(), ",");
    }
    
    ArrayList<InternetAddress> iaTos = new ArrayList<InternetAddress>();
    try
    {
      for (String to : toAddresses)
      {
        if (to.contains("@noreply"))
        {
          continue;
        }
        
        iaTos.add(new InternetAddress(to.trim()));
      }
    }
    catch (AddressException e)
    {
      throw new InvalidToRecipient();
    }
    if (iaTos.size() == 0) { return; }
    
    
    try
    {
      Email email = new SimpleEmail();
      email.setHostName(settings.getServer());
      email.setSmtpPort(settings.getPort());
      email.setAuthenticator(new DefaultAuthenticator(settings.getUsername(), settings.getPassword()));
      email.setSSLOnConnect(true);
      email.setFrom(settings.getFrom());
      email.setSubject(subject);
      email.setMsg(body);
      email.setTo(iaTos);
      email.send();
    }
    catch (EmailException e)
    {
      Throwable cause = e.getCause();
      
      if (cause instanceof AuthenticationFailedException)
      {
        throw new EmailSendAuthenticationException(e);
      }
      
      throw new EmailSendException(e);
    }
  }
  
  /**
   * MdMethod
   */
  public static void sendTestEmail()
  {
    String subject = LocalizationFacade.getFromBundles("emailSettings.testEmailSubject");
    String body = LocalizationFacade.getFromBundles("emailSettings.testEmailBody");
    
    sendEmail(subject, body, null);
  }
  
  /**
   * MdMethod
   * 
   * Used when you want to edit the default email settings. They will be created if not exist, locked, and returned.
   * 
   * @return The default email settings.
   */
  public static net.geoprism.EmailSetting editDefault()
  {
    EmailSetting setting = getDefault();
    
    if (setting == null)
    {
      setting = new EmailSetting();
      setting.apply();
    }
    
    setting.lock();
    
    return setting;
  }
  
  /**
   * MdMethod
   * 
   * Used in a situation where you want the email settings, but you want them in a read-only environment. We will not lock
   * them, we will not create them if they don't exist. Its just the most basic 'get' query. If they do not exist we will return null.
   * 
   * @return The default email settings.
   */
  public static net.geoprism.EmailSetting getDefault()
  {
    EmailSettingQuery query = new EmailSettingQuery(new QueryFactory());
    OIterator<? extends EmailSetting> it = query.getIterator();
    
    if (it.hasNext())
    {
      EmailSetting first = it.next();
      
      if (it.hasNext())
      {
        throw new InvalidEmailSettings();
      }
      
      return first;
    }
    else
    {
      return null;
    }
  }
  
  @Override
  @Transaction
  public void apply()
  {
    String server = this.getServer();
    
    if (server.contains("://"))
    {
      try
      {
        URL url = new URL(server);
        server = url.getHost();
        this.setServer(server);
      }
      catch (MalformedURLException e)
      {
        throw new AttributeValueException("Server is not url parseable", e, BusinessDAO.get(this.getId()).getAttributeIF(EmailSetting.SERVER), server);
      }
    }
    
    super.apply();
    
    EmailSetting.sendTestEmail();
  }
}
