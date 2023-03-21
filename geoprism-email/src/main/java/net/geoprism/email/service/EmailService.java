package net.geoprism.email.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.runwaysdk.session.Request;
import com.runwaysdk.session.RequestType;

import net.geoprism.EmailSetting;
import net.geoprism.email.business.EmailBusinessService;

@Component
public class EmailService implements EmailServiceIF
{
  private static Logger logger = LoggerFactory.getLogger(EmailService.class);
  
  @Autowired
  protected EmailBusinessService service;
  
  public EmailService() {}
  
  public EmailService(EmailBusinessService service) { this.service = service; }
  
  @Override
  @Request(RequestType.SESSION)
  public void sendEmail(String sessionId, String subject, String body, String[] toAddresses)
  {
    this.service.sendEmail(subject, body, toAddresses);
  }
  
  @Override
  @Request(RequestType.SESSION)
  public void sendTestEmail(String sessionId)
  {
    this.service.sendTestEmail();
  }
  
  @Override
  @Request(RequestType.SESSION)
  public synchronized EmailSettingView editDefault(String sessionId)
  {
    EmailSetting model = this.service.editDefault();
    
    return EmailSettingView.fromEmailSetting(model);
  }
  
  @Override
  @Request(RequestType.SESSION)
  public EmailSettingView getDefault(String sessionId)
  {
    return EmailSettingView.fromEmailSetting(this.service.getDefault());
  }
  
  @Override
  @Request(RequestType.SESSION)
  public EmailSettingView apply(String sessionId, EmailSettingView view, boolean sendTestEmail)
  {
    EmailSetting model = this.service.getDefault();
    model.appLock();
    view.populate(model);
    
    model = this.service.apply(model, sendTestEmail);
    
    return EmailSettingView.fromEmailSetting(model);
  }
}
