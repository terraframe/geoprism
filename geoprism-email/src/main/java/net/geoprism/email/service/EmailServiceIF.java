package net.geoprism.email.service;

public interface EmailServiceIF
{
  /**
   * A reusable, easy way to send an email using the saved default EmailSetting. If toAddresses is null, they will be read from the default settings.
   * 
   * @param subject
   * @param body
   * @param toAddresses
   */
  public void sendEmail(String sessionId, String subject, String body, String[] toAddresses);
  
  /**
   * Sends an test email using all the pre-configured defaults.
   */
  public void sendTestEmail(String sessionId);
  
  /**
   * Applies the provided EmailSetting. We can optionally send a test email to verify that the settings work. If sending a test email
   * fails, we will attempt to rollback the apply.
   */
  public EmailSettingView apply(String sessionId, EmailSettingView emailSetting, boolean sendTestEmail);
  
  /**
   * Fetches the default email settings and returns them.
   * 
   * @return The default email settings.
   */
  public EmailSettingView getDefault(String sessionId);
  
  /**
   * Used when you want to edit the default email settings.
   * 
   * @return The default email settings.
   */
  public EmailSettingView editDefault(String sessionId);
}
