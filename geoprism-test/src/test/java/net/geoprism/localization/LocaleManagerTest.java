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
package net.geoprism.localization;

import java.util.Collection;
import java.util.Locale;

import net.geoprism.localization.LocaleManager;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

public class LocaleManagerTest
{
  @Test
  public void testGetConfiguration() throws Exception
  {
    JSONObject configuration = LocaleManager.getConfiguration(Locale.ENGLISH);

    JSONObject main = configuration.getJSONObject("main");

    Assert.assertNotNull(main);

    JSONObject en = main.getJSONObject("en");

    Assert.assertNotNull(en);

    JSONObject numbers = en.getJSONObject("numbers");

    Assert.assertNotNull(numbers);

    JSONObject supplemental = configuration.getJSONObject("supplemental");

    Assert.assertNotNull(supplemental);

    JSONObject subtags = supplemental.getJSONObject("likelySubtags");

    Assert.assertNotNull(subtags);
  }

  @Test
  public void testGetBestFitCLDR_EN()
  {
    Locale locale = LocaleManager.getBestFitCLDR(Locale.ENGLISH);

    Assert.assertEquals(locale.getLanguage(), Locale.ENGLISH.getLanguage());
    Assert.assertEquals(locale.getCountry(), Locale.ENGLISH.getCountry());
    Assert.assertEquals(locale.getVariant(), Locale.ENGLISH.getVariant());
  }

  @Test
  public void testGetBestFitCLDR_Default()
  {
    Locale locale = LocaleManager.getBestFitCLDR(new Locale("RK"));

    Assert.assertEquals(locale.getLanguage(), Locale.ENGLISH.getLanguage());
    Assert.assertEquals(locale.getCountry(), Locale.ENGLISH.getCountry());
    Assert.assertEquals(locale.getVariant(), Locale.ENGLISH.getVariant());
  }

  @Test
  public void testGetBestFitCLDR_EN_AU()
  {
    Locale locale = LocaleManager.getBestFitCLDR(new Locale("en", "AU", "ts"));

    Assert.assertEquals(locale.getLanguage(), "en");
    Assert.assertEquals(locale.getCountry(), "AU");
    Assert.assertEquals(locale.getVariant(), "");
  }

  @Test
  public void testGetLocaleName()
  {
    Locale locale = LocaleManager.getBestFitCLDR(new Locale("en", "AU", "ts"));

    String path = LocaleManager.getLocaleName(locale);

    Assert.assertEquals("en-AU", path);
  }

  @Test
  public void testGetAllCLDRLocales()
  {
    Collection<Locale> locales = LocaleManager.getAllCLDRs();

    Assert.assertTrue(locales.size() > 0);

    Assert.assertTrue(locales.contains(new Locale("en")));
    Assert.assertTrue(locales.contains(new Locale("zh")));
    Assert.assertTrue(locales.contains(new Locale("vi")));
  }

  @Test
  public void testGetAllDatepickers()
  {
    Collection<Locale> locales = LocaleManager.getAllDatepickers();

    Assert.assertTrue(locales.size() > 0);

    Assert.assertTrue(locales.contains(new Locale("en")));
    Assert.assertTrue(locales.contains(new Locale("it", "CH")));
    Assert.assertTrue(locales.contains(new Locale("vi")));
  }

  @Test
  public void testGetDatepickerLocaleName()
  {
    String name = LocaleManager.getDatepickerLocaleName(new Locale("it", "CH"));

    Assert.assertEquals("it-CH", name);
  }

}
