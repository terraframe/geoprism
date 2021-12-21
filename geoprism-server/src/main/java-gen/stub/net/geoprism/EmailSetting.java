/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
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

import com.runwaysdk.business.rbac.Operation;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.attributes.AttributeValueException;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.session.Request;
import com.runwaysdk.session.Session;

import net.geoprism.email.EmailSendAuthenticationException;
import net.geoprism.email.EmailSendException;
import net.geoprism.email.InvalidEmailSettings;
import net.geoprism.email.InvalidToRecipient;
import net.geoprism.localization.LocalizationFacade;

public class EmailSetting extends EmailSettingBase 
{
  private static final long serialVersionUID = -1631634656;
  
  private static Logger logger = LoggerFactory.getLogger(EmailSetting.class);
  
  public EmailSetting()
  {
    super();
  }
  
  public static void main(String[] args)
  {
    mainInReq(args);
  }
  @Request
  private static void mainInReq(String[] args)
  {
    String[] toAddr = null;
    if (args.length > 0)
    {
      toAddr = args;
    }
    
    String subject = LocalizationFacade.getFromBundles("emailSettings.testEmailSubject");
    String body = LocalizationFacade.getFromBundles("emailSettings.testEmailBody");
    
    System.out.println("email.username=" +  GeoprismProperties.getEmailUsername() + "\nemail.password=" + GeoprismProperties.getEmailPassword());
    
    sendEmail(subject, body, toAddr);
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
          throw new InvalidToRecipient("The email address [" + to + "] is invalid.");
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
      email.setFrom(settings.getFrom());
      email.setSubject(subject);
      email.setMsg(body);
      email.setTo(iaTos);
      email.setSSLOnConnect(GeoprismProperties.getEncrypted());
      email.setStartTLSRequired(GeoprismProperties.getEncrypted());
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
   * Used when you want to edit the default email settings. Same as 'getDefault' except we lock them before returning them.
   * 
   * @return The default email settings.
   */
  public static synchronized net.geoprism.EmailSetting editDefault()
  {
    EmailSetting setting = getDefault();
    
    if (!setting.isAppliedToDB())
    {
      setting.applyWithoutTesting();
    }
    
    setting.lock();
    
    return setting;
  }
  
  /**
   * MdMethod
   * 
   * Fetches the default email settings and returns them.
   * 
   * @return The default email settings.
   */
  public static net.geoprism.EmailSetting getDefault()
  {
    EmailSettingQuery query = new EmailSettingQuery(new QueryFactory());
    OIterator<? extends EmailSetting> it = query.getIterator();
    
    EmailSetting setting;
    
    if (it.hasNext())
    {
      setting = it.next();
      
      if (it.hasNext())
      {
        throw new InvalidEmailSettings();
      }
    }
    else
    {
      setting = readSettingsFromProperties();
//      setting.applyWithoutTesting(); // They may not have permissions to write EmailSettings
    }
    
    return setting;
  }
  
  private static EmailSetting readSettingsFromProperties()
  {
    EmailSetting set = new EmailSetting();
    set.setUsername(GeoprismProperties.getEmailUsername());
    set.setPassword(GeoprismProperties.getEmailPassword());
    set.setServer(GeoprismProperties.getEmailServer());
    set.setPort(GeoprismProperties.getEmailPort());
    set.setFrom(GeoprismProperties.getEmailFrom());
    set.setTo(GeoprismProperties.getEmailTo());
    return set;
  }
  
  @Override
  @Transaction
  public void apply()
  {
    this.applyWithoutTesting();
    
    EmailSetting.sendTestEmail();
  }
  
  public void applyWithoutTesting()
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
        throw new AttributeValueException("Server is not url parseable", e, BusinessDAO.get(this.getOid()).getAttributeIF(EmailSetting.SERVER), server);
      }
    }
    
    super.apply();
  }  
}
