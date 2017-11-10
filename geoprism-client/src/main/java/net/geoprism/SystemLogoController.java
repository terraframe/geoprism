package net.geoprism;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.io.FilenameUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.constants.LocalProperties;
import com.runwaysdk.controller.MultipartFileParameter;
import com.runwaysdk.controller.ServletMethod;
import com.runwaysdk.mvc.Controller;
import com.runwaysdk.mvc.Endpoint;
import com.runwaysdk.mvc.ErrorSerialization;
import com.runwaysdk.mvc.InputStreamResponse;
import com.runwaysdk.mvc.RequestParamter;
import com.runwaysdk.mvc.ResponseIF;
import com.runwaysdk.mvc.RestBodyResponse;

@Controller(url = "logo")
public class SystemLogoController
{
  @Endpoint(method = ServletMethod.POST, error = ErrorSerialization.JSON)
  public ResponseIF apply(ClientRequestIF request, @RequestParamter(name = "id") String id, @RequestParamter(name = "file") MultipartFileParameter file) throws IOException
  {
    if (id != null && id.equals("banner"))
    {
      SystemLogoSingletonDTO.uploadBannerAndCache(request, file.getInputStream(), file.getFilename());
    }
    else
    {
      SystemLogoSingletonDTO.uploadMiniLogoAndCache(request, file.getInputStream(), file.getFilename());
    }

    return new RestBodyResponse("");
  }

  @Endpoint(method = ServletMethod.GET, error = ErrorSerialization.JSON)
  public ResponseIF getAll(ClientRequestIF request) throws JSONException
  {
    JSONObject banner = new JSONObject();
    banner.put("id", "banner");
    banner.put("label", "Banner");

    JSONObject logo = new JSONObject();
    logo.put("id", "logo");
    logo.put("label", "Logo");

    JSONArray icons = new JSONArray();
    icons.put(banner);
    icons.put(logo);

    JSONObject object = new JSONObject();
    object.put("icons", icons);

    return new RestBodyResponse(object);
  }

  @Endpoint(method = ServletMethod.GET, error = ErrorSerialization.JSON)
  public ResponseIF view(ClientRequestIF request, @RequestParamter(name = "id") String id) throws IOException
  {
    String path = null;

    if (id != null && id.equals("banner"))
    {
      path = SystemLogoSingletonDTO.getBannerFileFromCache(request, null);

      if (path == null)
      {
        path = LocalProperties.getJspDir() + "/../net/geoprism/images/splash_logo.png";
      }
    }
    else
    {
      path = SystemLogoSingletonDTO.getMiniLogoFileFromCache(request, null);

      if (path == null)
      {
        path = LocalProperties.getJspDir() + "/../net/geoprism/images/splash_logo_icon.png";
      }
    }

    File file = new File(path);
    FileInputStream fis = new FileInputStream(file);

    String ext = FilenameUtils.getExtension(file.getName());

    return new InputStreamResponse(fis, "image/" + ext);
  }

}
