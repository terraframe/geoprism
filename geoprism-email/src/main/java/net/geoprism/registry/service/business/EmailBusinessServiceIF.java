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

import org.springframework.stereotype.Component;

import net.geoprism.EmailSetting;

@Component
public interface EmailBusinessServiceIF
{
  /**
   * A reusable, easy way to send an email using the saved default EmailSetting. If toAddresses is null, they will be read from the default settings.
   * 
   * @param subject
   * @param body
   * @param toAddresses
   */
  public void sendEmail(String subject, String body, String[] toAddresses);
  
  /**
   * Sends an test email using all the pre-configured defaults.
   */
  public void sendTestEmail();
  
  /**
   * Applies the provided EmailSetting. We can optionally send a test email to verify that the settings work. If sending a test email
   * fails, we will attempt to rollback the apply.
   */
  public EmailSetting apply(EmailSetting model, boolean sendTestEmail);
  
  /**
   * Fetches the default email settings and returns them.
   * 
   * @return The default email settings.
   */
  public EmailSetting getDefault();
  
  /**
   * Used when you want to edit the default email settings.
   * 
   * @return The default email settings.
   */
  public EmailSetting editDefault();
}
