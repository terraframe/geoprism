package net.geoprism.userinvite.service;

public interface UserInviteServiceIF
{

  public void complete(String sessionId, String token, String jsonUser);

  public void initiate(String sessionId, String invite, String roleIds);

}
