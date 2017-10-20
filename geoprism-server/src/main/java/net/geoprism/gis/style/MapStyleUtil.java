/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism.gis.style;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.HashMap;

import net.geoprism.gis.wrapper.AttributeType;
import net.geoprism.gis.wrapper.ThematicStyle;
import net.geoprism.localization.LocalizationFacade;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.dataaccess.ProgrammingErrorException;

public class MapStyleUtil 
{

  public MapStyleUtil()
  {
    super();
  }
  
  public static NumberFormat getRuleNumberFormatter()
  {
    return new DecimalFormat("#.##", DecimalFormatSymbols.getInstance(LocalizationFacade.getLocale()));
  }
  
  public static HashMap<String,Object> getCategoryProps(JSONObject categoryJSON, AttributeType attributeType)
  {
    NumberFormat formatter = getRuleNumberFormatter();
    HashMap<String,Object> categoryMap = new HashMap<String,Object>();
    
    String catVal;
    String catTitle;
    String catColor;
    boolean isOtherCat = false;
    boolean otherCatEnabled = true;
    boolean isOntologyCat;
    
    try
    {
      catVal = categoryJSON.getString(ThematicStyle.VAL);
      catColor = categoryJSON.getString(ThematicStyle.COLOR);
      catTitle = catVal;
      isOntologyCat = categoryJSON.getBoolean("isOntologyCat");
  
      if (isOntologyCat == false)
      {
        // 'other' attributes only relevant for non-ontology categories
        isOtherCat = categoryJSON.getBoolean(ThematicStyle.ISOTHERCAT);
        otherCatEnabled = categoryJSON.getBoolean("otherEnabled");
      }
    }
    catch (JSONException e)
    {
      String msg = "Can not parse JSON during SLD generation.";
      throw new ProgrammingErrorException(msg, e);
    }

    // If this category is a defined category (i.e. not the other category)
    if (isOtherCat == false)
    {
      if (attributeType.isNumber() && catVal != null && catVal.length() > 0)
      {
        try
        {
          catTitle = formatter.format(new Double(catVal));
        }
        catch (Exception e)
        {
          // The category isn't actually a number so it can't be localized
        }
      }
    }
    
    categoryMap.put(ThematicStyle.CATEGORYVALUE, catVal);
    categoryMap.put(ThematicStyle.CATEGORYTITLE, catTitle);
    categoryMap.put(ThematicStyle.CATEGORYCOLOR, catColor);
    categoryMap.put("catOtherCat", isOtherCat);
    categoryMap.put("catOtherEnabled", otherCatEnabled);
    categoryMap.put("isOntologyCat", isOntologyCat);
    
    return categoryMap;
  }

  public static JSONArray getCategories(String cats)
  {
    try
    {
      JSONObject catsJSON = new JSONObject(cats);
      
      if(catsJSON.has("catLiElems"))
      {
        return catsJSON.getJSONArray("catLiElems");
      }
    }
    catch (JSONException e)
    {
      throw new ProgrammingErrorException("Can not parse categories json. There may be missing data.", e);
    }
    
    return new JSONArray();
  }
  
  public static HashMap<String,Object> getCategoryProps(JSONObject categoryJSON, AttributeType attributeType, HashMap<String, Double> attributeMinMax)
  {
    NumberFormat formatter = getRuleNumberFormatter();
    
    HashMap<String,Object> categoryMap = getCategoryProps(categoryJSON, attributeType);
    
    String catVal = (String) categoryMap.get(ThematicStyle.CATEGORYVALUE); // should be/become the min val
    String catTitle = (String) categoryMap.get(ThematicStyle.CATEGORYTITLE);
    boolean isOtherCat = (boolean) categoryMap.get("catOtherCat");
    boolean isOntologyCat = (boolean) categoryMap.get("isOntologyCat");
    
    String catMaxVal = null;
    boolean rangeAllMin = false;
    boolean rangeAllMax = false;
    
    try
    {
      if (isOntologyCat == false)
      {
        if(isOtherCat == false)
        {
          catMaxVal = categoryJSON.getString(ThematicStyle.VALMAX);
          if(categoryJSON.has(ThematicStyle.RANGEALLMIN))
          {
            rangeAllMin = categoryJSON.getBoolean(ThematicStyle.RANGEALLMIN);
            if(rangeAllMin == true)
            {
              catVal = Double.toString(attributeMinMax.get("min"));
            }
          }
          if(categoryJSON.has(ThematicStyle.RANGEALLMAX))
          {
            rangeAllMax = categoryJSON.getBoolean(ThematicStyle.RANGEALLMAX);
            if(rangeAllMax == true)
            {
              catMaxVal = Double.toString(attributeMinMax.get("max"));
            }
          }
        }
      }
    }
    catch (JSONException e)
    {
      String msg = "Can not parse JSON during SLD generation.";
      throw new ProgrammingErrorException(msg, e);
    }

    // If this category is a defined category (i.e. not the other category)
    if (isOtherCat == false)
    {
      if (attributeType.isNumber() && catVal != null && catVal.length() > 0)
      {
        if(catMaxVal != null && catMaxVal.length() > 0)
        {
          try
          {
            String catMin = formatter.format(new Double(catVal));
            String catMax = formatter.format(new Double(catMaxVal));
            catTitle = catMin.concat(" - ").concat(catMax);
          }
          catch (Exception e)
          {
            // The category isn't actually a number so it can't be localized
          }
        }
      }
    }
    
    categoryMap.put(ThematicStyle.CATEGORYVALUE, catVal);
    categoryMap.put(ThematicStyle.CATEGORYMAXVALUE, catMaxVal);
    categoryMap.put(ThematicStyle.CATEGORYTITLE, catTitle);
    categoryMap.put(ThematicStyle.RANGEALLMIN, rangeAllMin);
    categoryMap.put(ThematicStyle.RANGEALLMAX, rangeAllMax);
    
    return categoryMap;
  }


}
