package net.geoprism;

import org.json.JSONException;

import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.controller.ServletMethod;
import com.runwaysdk.mvc.Controller;
import com.runwaysdk.mvc.Endpoint;
import com.runwaysdk.mvc.ErrorSerialization;
import com.runwaysdk.mvc.RequestParamter;
import com.runwaysdk.mvc.ResponseIF;
import com.runwaysdk.mvc.RestResponse;
import com.runwaysdk.request.ServletRequestIF;

@Controller(url = "forgotpassword")
public class ForgotPasswordController
{
  @Endpoint(method = ServletMethod.GET, error = ErrorSerialization.JSON)
  public ResponseIF initiate(ClientRequestIF request, ServletRequestIF sr, @RequestParamter(name = "username") String username) throws JSONException
  {
    ForgotPasswordRequestDTO.initiate(request, username, getBaseUrl(sr));
    
    return new RestResponse();
  }
  
  @Endpoint(method = ServletMethod.GET, error = ErrorSerialization.JSON)
  public ResponseIF complete(ClientRequestIF request, @RequestParamter(name = "token") String token, @RequestParamter(name = "newPassword") String newPassword) throws JSONException
  {
    ForgotPasswordRequestDTO.complete(request, token, newPassword);
    
    return new RestResponse();
  }
  
  public static String getBaseUrl(ServletRequestIF request) {
    String scheme = request.getScheme() + "://";
    String serverName = request.getServerName();
    String serverPort = (request.getServerPort() == 80) ? "" : ":" + request.getServerPort();
    String contextPath = request.getContextPath();
    return scheme + serverName + serverPort + contextPath;
  }
}
