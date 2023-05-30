/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Geoprism Registry(tm).
 *
 * Geoprism Registry(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Geoprism Registry(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Geoprism Registry(tm). If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism.graph.conversion;

import java.text.DateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.commongeoregistry.adapter.dataaccess.LocalizedValue;
import org.commongeoregistry.adapter.metadata.GeoObjectType;
import org.commongeoregistry.adapter.metadata.RegistryRole;

import com.runwaysdk.business.BusinessFacade;
import com.runwaysdk.business.LocalStruct;
import com.runwaysdk.business.graph.GraphObject;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.dataaccess.attributes.entity.AttributeLocal;
import com.runwaysdk.dataaccess.graph.GraphObjectDAO;
import com.runwaysdk.dataaccess.metadata.MetadataDAO;
import com.runwaysdk.localization.LocalizationFacade;
import com.runwaysdk.localization.LocalizedValueIF;
import com.runwaysdk.session.Session;
import com.runwaysdk.system.gis.geo.Universal;

import net.geoprism.graph.DateUtil;

public class LocalizedValueConverter
{

  /**
   * This method will coalesce the value for all of the locales. So if the value
   * is empty for a particular locale, that locale will actually have the
   * default locale placed in it. For this reason, usage of this method should
   * be deprecated.
   * 
   * Use convertNoAutoCoalesce instead.
   */
  @Deprecated
  public static LocalizedValue convert(LocalStruct localStruct)
  {
    LocalizedValue label = new LocalizedValue(localStruct.getValue());
    label.setValue(MdAttributeLocalInfo.DEFAULT_LOCALE, localStruct.getValue(MdAttributeLocalInfo.DEFAULT_LOCALE));

    Set<Locale> locales = LocalizationFacade.getInstalledLocales();

    for (Locale locale : locales)
    {
      label.setValue(locale, localStruct.getValue(locale));
    }

    return label;
  }

  public static LocalizedValue convert(Date date)
  {
    LocalizedValue dateValue = LocalizedValue.createEmptyLocalizedValue();

    // Add the default locale
    {
      DateFormat formatter = DateFormat.getDateInstance(DateFormat.SHORT, Locale.US);
      formatter.setTimeZone(DateUtil.SYSTEM_TIMEZONE);
      dateValue.setValue(LocalizedValue.DEFAULT_LOCALE, formatter.format(date));
    }

    // Add the individual locales
    LocalizationFacade.getInstalledLocales().forEach(locale -> {
      DateFormat formatter = DateFormat.getDateInstance(DateFormat.SHORT, locale);
      formatter.setTimeZone(DateUtil.SYSTEM_TIMEZONE);
      String format = formatter.format(date);

      dateValue.setValue(locale, format);
    });
    return dateValue;
  }

  public static LocalizedValue convertNoAutoCoalesce(LocalStruct localStruct)
  {
    LocalizedValue label = new LocalizedValue(localStruct.getValue());

    label.setLocaleMap(localStruct.getLocaleMap());

    return label;
  }

  public static LocalizedValue convert(String value, Map<String, ?> map)
  {
    LocalizedValue localizedValue = new LocalizedValue(value);
    localizedValue.setValue(MdAttributeLocalInfo.DEFAULT_LOCALE, map.get(MdAttributeLocalInfo.DEFAULT_LOCALE).toString());

    Set<Locale> locales = LocalizationFacade.getInstalledLocales();

    for (Locale locale : locales)
    {
      localizedValue.setValue(locale, map.get(locale.toString()).toString());
    }

    return localizedValue;
  }

  @SuppressWarnings("unchecked")
  public static LocalizedValue convert(List<Map<String, Object>> labels, Date date)
  {
    if (date != null)
    {
      LocalDate localDate = date.toInstant().atZone(ZoneId.of("Z")).toLocalDate();

      Optional<Map<String, Object>> result = labels.stream().filter(o -> {
        LocalDate startDate = ( (Date) o.get("startDate") ).toInstant().atZone(ZoneId.of("Z")).toLocalDate();
        LocalDate endDate = ( (Date) o.get("endDate") ).toInstant().atZone(ZoneId.of("Z")).toLocalDate();

        return ( startDate.equals(localDate) || startDate.isBefore(localDate) ) && ( endDate.equals(localDate) || endDate.isAfter(localDate) );
      }).findAny();

      return LocalizedValueConverter.convert((Map<String, Object>) result.orElseGet(() -> labels.get(labels.size() - 1)).get("value"));
    }

    return LocalizedValueConverter.convert((Map<String, Object>) labels.get(labels.size() - 1).get("value"));
  }

  public static LocalizedValue convert(Map<String, ?> map)
  {
    LocalizedValue localizedValue = new LocalizedValue(coalesce(map));
    localizedValue.setValue(MdAttributeLocalInfo.DEFAULT_LOCALE, map.get(MdAttributeLocalInfo.DEFAULT_LOCALE).toString());

    Set<Locale> locales = LocalizationFacade.getInstalledLocales();

    for (Locale locale : locales)
    {
      String key = locale.toString().toLowerCase();

      if (map.containsKey(key) && map.get(key) != null)
      {
        localizedValue.setValue(locale, map.get(key).toString());
      }
    }

    return localizedValue;
  }

  public static String coalesce(Map<String, ?> map)
  {
    Locale locale = Session.getCurrentLocale();
    String[] localeStringArray = new String[] { locale.toString() };

    for (String localeString : localeStringArray)
    {
      for (int i = localeString.length(); i > 0; i = localeString.lastIndexOf('_', i - 1))
      {
        String subLocale = localeString.substring(0, i);

        if (map.containsKey(subLocale))
        {
          String subLocaleValue = map.get(subLocale).toString();

          if (!subLocaleValue.trim().equals(""))
          {
            return subLocaleValue;
          }
        }
      }
    }

    // No matching locale found. Resort to the default
    return map.get(MdAttributeLocalInfo.DEFAULT_LOCALE).toString();
  }

  public static void populate(MetadataDAO mdClass, String attributeName, LocalizedValue label)
  {
    AttributeLocal attributeLocal = (AttributeLocal) mdClass.getAttribute(attributeName);
    LocalStruct struct = (LocalStruct) BusinessFacade.get(attributeLocal.getStructDAO());

    populate(struct, label);
  }

  public static void populate(LocalStruct struct, LocalizedValue label)
  {
    struct.setValue(label.getValue());
    struct.setValue(MdAttributeLocalInfo.DEFAULT_LOCALE, label.getValue(MdAttributeLocalInfo.DEFAULT_LOCALE));

    Set<Locale> locales = LocalizationFacade.getInstalledLocales();

    for (Locale locale : locales)
    {
      if (label.contains(locale))
      {
        struct.setValue(locale, label.getValue(locale));
      }
    }
  }

  public static void populate(LocalStruct struct, LocalizedValueIF label)
  {
    struct.setValue(label.getValue());
    struct.setValue(MdAttributeLocalInfo.DEFAULT_LOCALE, label.getDefaultValue());

    Set<Locale> locales = LocalizationFacade.getInstalledLocales();
    Map<String, String> map = label.getLocaleMap();

    for (Locale locale : locales)
    {
      if (map.containsKey(locale.toString()))
      {
        struct.setValue(locale, label.getValue(locale));
      }
    }
  }

  public static void populate(LocalStruct struct, LocalizedValue label, String suffix)
  {
    struct.setValue(label.getValue());
    struct.setValue(MdAttributeLocalInfo.DEFAULT_LOCALE, label.getValue(MdAttributeLocalInfo.DEFAULT_LOCALE) + suffix);

    Set<Locale> locales = LocalizationFacade.getInstalledLocales();

    for (Locale locale : locales)
    {
      if (label.contains(locale))
      {
        struct.setValue(locale, label.getValue(locale) + suffix);
      }
    }
  }

  public static LocalizedValue convert(GraphObjectDAO graphObject)
  {
    String attributeName = Session.getCurrentLocale().toString();
    String defaultLocale = (String) graphObject.getObjectValue(MdAttributeLocalInfo.DEFAULT_LOCALE);
    String value = (String) ( graphObject.hasAttribute(attributeName) && graphObject.getObjectValue(attributeName) != null ? graphObject.getObjectValue(attributeName) : defaultLocale );

    LocalizedValue localizedValue = new LocalizedValue(value);
    localizedValue.setValue(MdAttributeLocalInfo.DEFAULT_LOCALE, defaultLocale);

    Set<Locale> locales = LocalizationFacade.getInstalledLocales();

    for (Locale locale : locales)
    {
      localizedValue.setValue(locale, (String) graphObject.getObjectValue(locale.toString()));
    }

    return localizedValue;
  }

  public static LocalizedValue convert(GraphObject graphObject)
  {
    String attributeName = Session.getCurrentLocale().toString();
    String defaultLocale = graphObject.getObjectValue(MdAttributeLocalInfo.DEFAULT_LOCALE);
    String value = (String) ( graphObject.hasAttribute(attributeName) && graphObject.getObjectValue(attributeName) != null ? graphObject.getObjectValue(attributeName) : defaultLocale );

    LocalizedValue localizedValue = new LocalizedValue(value);
    localizedValue.setValue(MdAttributeLocalInfo.DEFAULT_LOCALE, defaultLocale);

    Set<Locale> locales = LocalizationFacade.getInstalledLocales();

    for (Locale locale : locales)
    {
      localizedValue.setValue(locale, (String) graphObject.getObjectValue(locale.toString()));
    }

    return localizedValue;
  }

  public static void populate(GraphObject graphObject, String attributeName, LocalizedValue value)
  {
    graphObject.setEmbeddedValue(attributeName, MdAttributeLocalInfo.DEFAULT_LOCALE, value.getValue(MdAttributeLocalInfo.DEFAULT_LOCALE));

    Set<Locale> locales = LocalizationFacade.getInstalledLocales();

    for (Locale locale : locales)
    {
      if (value.contains(locale))
      {
        graphObject.setEmbeddedValue(attributeName, locale.toString(), value.getValue(locale));
      }
    }
  }

  public static void populate(GraphObject graphObject, String attributeName, LocalizedValue value, Date startDate, Date endDate)
  {
    graphObject.setEmbeddedValue(attributeName, MdAttributeLocalInfo.DEFAULT_LOCALE, value.getValue(MdAttributeLocalInfo.DEFAULT_LOCALE), startDate, endDate);

    Set<Locale> locales = LocalizationFacade.getInstalledLocales();

    for (Locale locale : locales)
    {
      if (value.contains(locale))
      {
        graphObject.setEmbeddedValue(attributeName, locale.toString(), value.getValue(locale), startDate, endDate);
      }
    }
  }

  /**
   * Populates the {@link GeoObjectType} display label on the given
   * {@link RegistryRole} object.
   * 
   * @param registryRole
   */
  public static void populateGeoObjectTypeLabel(RegistryRole registryRole)
  {
    String geoObjectTypeCode = registryRole.getGeoObjectTypeCode();
    if (geoObjectTypeCode != null && !geoObjectTypeCode.trim().equals(""))
    {
      Universal universal = Universal.getByKey(geoObjectTypeCode);
      registryRole.setGeoObjectTypeLabel(LocalizedValueConverter.convert(universal.getDisplayLabel()));
    }
  }

}
