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
package net.geoprism.email.service;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.lang.Nullable;

import net.geoprism.EmailSetting;

public class EmailSettingView
{
  @NotEmpty
  private String username;
  
  @NotEmpty
  private String password;
  
  @NotEmpty
  private String server;
  
  @NotEmpty
  private String from;
  
  @Nullable
  private String to;
  
  @Nullable
  private Integer port;
  
  public EmailSettingView()
  {
    
  }

  public EmailSettingView(String username, String password, String server, String from, String to, Integer port)
  {
    super();
    this.username = username;
    this.password = password;
    this.server = server;
    this.from = from;
    this.to = to;
    this.port = port;
  }
  
  public static EmailSettingView fromEmailSetting(EmailSetting email)
  {
    EmailSettingView view = new EmailSettingView(email.getUsername(), email.getPassword(), email.getServer(), email.getFrom(), email.getTo(), email.getPort());
    return view;
  }
  
  public void populate(EmailSetting email)
  {
    email.setUsername(this.getUsername());
    email.setPassword(this.getPassword());
    email.setServer(this.getServer());
    email.setFrom(this.getFrom());
    email.setTo(this.getTo());
    email.setPort(this.getPort());
  }

  public String getUsername()
  {
    return username;
  }

  public void setUsername(String username)
  {
    this.username = username;
  }

  public String getPassword()
  {
    return password;
  }

  public void setPassword(String password)
  {
    this.password = password;
  }

  public String getServer()
  {
    return server;
  }

  public void setServer(String server)
  {
    this.server = server;
  }

  public String getFrom()
  {
    return from;
  }

  public void setFrom(String from)
  {
    this.from = from;
  }

  public String getTo()
  {
    return to;
  }

  public void setTo(String to)
  {
    this.to = to;
  }

  public Integer getPort()
  {
    return port;
  }

  public void setPort(Integer port)
  {
    this.port = port;
  }
  
  
}
