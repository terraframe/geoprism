/**
 * Copyright (c) 2023 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Geoprism(tm).
 *
 * Geoprism(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Geoprism(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Geoprism(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism.registry.service.request;

import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdAttributeLocalTextInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.metadata.MdAttributeTextDAO;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.localization.LocalizedValueStore;
import com.runwaysdk.localization.SupportedLocaleIF;
import com.runwaysdk.session.Request;
import com.runwaysdk.session.RequestType;
import com.runwaysdk.system.metadata.SupportedLocale;

import net.geoprism.localization.LocalizationService;
import net.geoprism.registry.graph.AttributeLocalValue;
import net.geoprism.registry.model.localization.DefaultLocaleView;
import net.geoprism.registry.model.localization.LocaleView;

public class RepoLocalizationService extends LocalizationService
{

  @Transaction
  public SupportedLocaleIF installLocale(LocaleView view)
  {
    SupportedLocaleIF supportedLocale = (SupportedLocale) com.runwaysdk.localization.LocalizationFacade.install(view.getLocale());

    supportedLocale.appLock();
    view.populate(supportedLocale);
    supportedLocale.apply();

    // Add the column to the AttributeLocalValue class
    MdVertexDAOIF mdVertex = MdVertexDAO.getMdVertexDAO(AttributeLocalValue.CLASS);

    MdAttributeTextDAO mdAttribute = MdAttributeTextDAO.newInstance();
    mdAttribute.setValue(MdAttributeLocalTextInfo.NAME, supportedLocale.getLocale().toString());
    mdAttribute.setStructValue(MdAttributeLocalTextInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, supportedLocale.getName());
    mdAttribute.setStructValue(MdAttributeLocalTextInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, supportedLocale.getName());
    mdAttribute.setValue(MdAttributeLocalTextInfo.DEFINING_MD_CLASS, mdVertex.getOid());
    mdAttribute.apply();

    // Be careful what you put here. We definitely don't want to refresh any
    // caches until after the transaction is over, especially given that we are
    // holding onto a lot of database locks and such right now.

    // Don't build the view inside the transaction either. Unless you like
    // deadlocks
    // return LocaleView.fromSupportedLocale(supportedLocale);

    return supportedLocale;
  }

  @Transaction
  public void uninstallLocale(LocaleView view)
  {
    com.runwaysdk.localization.LocalizationFacade.uninstall(view.getLocale());

    // Delete the column from AttributeLocalValue class
    MdVertexDAOIF mdVertex = MdVertexDAO.getMdVertexDAO(AttributeLocalValue.CLASS);

    BusinessDAO mdAttribute = mdVertex.definesAttribute(view.getLocale().toString()).getBusinessDAO();
    mdAttribute.delete();

    // Be careful what you put here. We definitely don't want to refresh any
    // caches until after the transaction is over, especially given that we are
    // holding onto a lot of database locks and such right now.
  }

  @Request(RequestType.SESSION)
  public LocaleView editLocaleInRequest(String sessionId, String json)
  {
    LocaleView view = LocaleView.fromJson(json);

    if (view.isDefaultLocale())
    {
      return editDefaultLocale(view);
    }
    else
    {
      SupportedLocaleIF supportedLocale = editLocale(view);

      return LocaleView.fromSupportedLocale(supportedLocale);
    }
  }

  @Transaction
  protected LocaleView editDefaultLocale(LocaleView view)
  {
    LocalizedValueStore lvs = LocalizedValueStore.getByKey(DefaultLocaleView.LABEL);

    lvs.lock();
    lvs.getStoreValue().setLocaleMap(view.getLabel().getLocaleMap());
    lvs.apply();

    view.getLabel().setValue(lvs.getStoreValue().getValue());

    return view;
  }

  @Transaction
  protected SupportedLocaleIF editLocale(LocaleView view)
  {
    SupportedLocaleIF supportedLocale = (SupportedLocale) com.runwaysdk.localization.LocalizationFacade.getSupportedLocale(view.getLocale());

    supportedLocale.appLock();
    view.populate(supportedLocale);
    supportedLocale.apply();

    // Be careful what you put here. We definitely don't want to refresh any
    // caches until after the transaction is over, especially given that we are
    // holding onto a lot of database locks and such right now.

    // Don't build the view inside the transaction either. Unless you like
    // deadlocks
    // return LocaleView.fromSupportedLocale(supportedLocale);

    return supportedLocale;
  }

}
