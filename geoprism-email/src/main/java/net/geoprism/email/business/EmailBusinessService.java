package net.geoprism.email.business;

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
import org.springframework.stereotype.Component;

import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.attributes.AttributeValueException;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.localization.LocalizationFacade;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.session.Request;
import com.runwaysdk.session.RequestType;
import com.runwaysdk.session.Session;

import net.geoprism.EmailSetting;
import net.geoprism.EmailSettingQuery;
import net.geoprism.configuration.GeoprismProperties;
import net.geoprism.email.EmailSendAuthenticationException;
import net.geoprism.email.EmailSendException;
import net.geoprism.email.InvalidEmailSettings;
import net.geoprism.email.InvalidToRecipient;
import net.geoprism.email.service.EmailServiceIF;
import net.geoprism.email.service.EmailSettingView;

@Component
public class EmailBusinessService implements EmailBusinessServiceIF
{
  private static Logger logger = LoggerFactory.getLogger(EmailBusinessService.class);
  
  // TODO: CGR and IDM are constructing us outside of spring context. Do not add @Autowired components here without fixing first.
  
  @Override
  public void sendEmail(String subject, String body, String[] toAddresses)
  {
    EmailSetting settings = this.getDefault();
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
  
  @Override
  public void sendTestEmail()
  {
    String subject = LocalizationFacade.localize("emailSettings.testEmailSubject");
    String body = LocalizationFacade.localize("emailSettings.testEmailBody");
    
    sendEmail(subject, body, null);
  }
  
  @Override
  public synchronized EmailSetting editDefault()
  {
    EmailSetting model = getDefault();
    
    if (!model.isAppliedToDB())
    {
      model = this.apply(model, false);
    }
    
    return model;
  }
  
  @Override
  public EmailSetting getDefault()
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
  
  protected EmailSetting readSettingsFromProperties()
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
  public EmailSetting apply(EmailSetting model, boolean sendTestEmail)
  {
    String server = model.getServer();
    
    if (server.contains("://"))
    {
      try
      {
        URL url = new URL(server);
        server = url.getHost();
        model.setServer(server);
      }
      catch (MalformedURLException e)
      {
        throw new AttributeValueException("Server is not url parseable", e, BusinessDAO.get(model.getOid()).getAttributeIF(EmailSetting.SERVER), server);
      }
    }
    
    model.apply();
    
    if (sendTestEmail)
    {
      this.sendTestEmail();
    }
    
    return model;
  }
}
