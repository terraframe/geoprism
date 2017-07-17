package net.geoprism.dhis2.util;

import java.util.Stack;

import org.apache.commons.httpclient.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import net.geoprism.dhis2.connector.AbstractDHIS2Connector;
import net.geoprism.dhis2.response.DHIS2UnexpectedResponseException;

/**
 * This class is responsible for maintaining a cache of new DHIS2 ids that can be fetched at will.
 * 
 * @author rrowlands
 */
public class DHIS2IdCache
{
  // Number of ids to fetch and cache
  private static final String FETCH_NUM = "10";
  
  Stack<String> cache = new Stack<String>();
  
  AbstractDHIS2Connector dhis2;
  
  public DHIS2IdCache(AbstractDHIS2Connector dhis2)
  {
    this.dhis2 = dhis2;
  }
  
  /**
   * Fetches more ids from DHIS2 and adds them to our internal cache.
   */
  public void fetchIds()
  {
    JSONObject response = dhis2.httpGet("api/27/system/id.json", new NameValuePair[]{
        new NameValuePair("limit", FETCH_NUM)
    });
    
    if (response.has("codes"))
    {
      JSONArray codes = response.getJSONArray("codes");
      
      for (int i = 0; i < codes.length(); ++i)
      {
        String id = codes.getString(i);
        cache.push(id);
      }
    }
    else
    {
      DHIS2UnexpectedResponseException ex = new DHIS2UnexpectedResponseException();
      ex.setDhis2Response(response.toString().substring(0, 500)); // TODO : We need a better solution for this
      throw ex;
    }
  }
  
  /**
   * Fetches the next id from the cache. If the cache is empty, this method will fetch more ids and return one when it becomes available, haulting execution in the meantime.
   */
  public String next()
  {
    if (cache.isEmpty())
    {
      this.fetchIds();
    }
    
    return cache.pop();
  }
}
