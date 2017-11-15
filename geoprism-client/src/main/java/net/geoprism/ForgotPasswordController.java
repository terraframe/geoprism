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

@Controller(url = "forgotpassword")
public class ForgotPasswordController
{
  @Endpoint(method = ServletMethod.GET, error = ErrorSerialization.JSON)
  public ResponseIF initiate(ClientRequestIF request, @RequestParamter(name = "username") String username) throws JSONException
  {
    ForgotPasswordRequestDTO.initiate(request, username, "<external url>"); // TODO : We need to get this external url from somewhere
    
    return new RestResponse();
  }
  
  @Endpoint(method = ServletMethod.GET, error = ErrorSerialization.JSON)
  public ResponseIF verify(ClientRequestIF request, @RequestParamter(name = "token") String token) throws JSONException
  {
//    ForgotPasswordRequestDTO.verify(request, token);
    
    return new RestResponse();
  }
}
