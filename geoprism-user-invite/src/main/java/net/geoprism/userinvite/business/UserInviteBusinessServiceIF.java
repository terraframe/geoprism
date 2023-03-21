package net.geoprism.userinvite.business;

public interface UserInviteBusinessServiceIF
{

  public void complete(String token, String jsonUser);

  public void initiate(String invite, String roleIds);

}
