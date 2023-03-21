package net.geoprism.userinvite.controller;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.geoprism.registry.controller.RunwaySpringController;
import net.geoprism.spring.JsonArrayDeserializer;
import net.geoprism.spring.JsonObjectDeserializer;
import net.geoprism.userinvite.service.UserInviteService;

@RestController
@Validated
public class UserInviteController extends RunwaySpringController
{
  public static final String API_PATH = "invite-user";
  
  public static class InviteCompleteBody
  {
    @NotNull
    @JsonDeserialize(using = JsonObjectDeserializer.class)
    JsonObject user;

    @NotEmpty
    String     token;

    public JsonObject getUser()
    {
      return user;
    }

    public void setUser(JsonObject user)
    {
      this.user = user;
    }

    public String getToken()
    {
      return token;
    }

    public void setToken(String token)
    {
      this.token = token;
    }

  }
  
  public static class InviteUserBody
  {
    @NotNull
    @JsonDeserialize(using = JsonObjectDeserializer.class)
    JsonObject invite;

    @JsonDeserialize(using = JsonArrayDeserializer.class)
    JsonArray  roleIds;

    public JsonObject getInvite()
    {
      return invite;
    }

    public void setInvite(JsonObject invite)
    {
      this.invite = invite;
    }

    public JsonArray getRoleIds()
    {
      return roleIds;
    }

    public void setRoleIds(JsonArray roleIds)
    {
      this.roleIds = roleIds;
    }
  }
  
  @Autowired
  protected UserInviteService service;
  
  @PostMapping(API_PATH + "/initiate")
  public ResponseEntity<Void> initiate(@Valid @RequestBody InviteUserBody body)
  {
    this.service.initiate(this.getSessionId(), body.invite.toString(), body.roleIds.toString());

    return new ResponseEntity<Void>(HttpStatus.OK);
  }
  
  @PostMapping(API_PATH + "/complete")
  public ResponseEntity<Void> complete(@Valid @RequestBody InviteCompleteBody body)
  {
    this.service.complete(this.getSessionId(), body.token, body.user.toString());

    return new ResponseEntity<Void>(HttpStatus.OK);
  }
}
