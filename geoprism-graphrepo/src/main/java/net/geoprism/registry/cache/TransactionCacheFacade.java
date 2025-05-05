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
package net.geoprism.registry.cache;

import java.util.Map;
import java.util.TreeMap;

import com.runwaysdk.dataaccess.transaction.TransactionState;

import net.geoprism.registry.model.ServerElement;
import net.geoprism.registry.model.ServerGeoObjectType;

public class TransactionCacheFacade
{
  public static final String TRANSACTION_CACHE_KEY = "transaction-state";

  public static void put(ServerElement type)
  {
    Map<String, ServerElement> cache = getTransactionCache();

    if (cache != null)
    {
      cache.put(type.getCode(), type);
      cache.put(type.getOid(), type);
      
      if (type instanceof ServerGeoObjectType)
      {
        cache.put(((ServerGeoObjectType)type).getMdVertex().getOid(), type);
      }
    }
  }

  public static ServerElement get(String codeOrOid)
  {
    Map<String, ServerElement> cache = getTransactionCache();

    if (cache != null)
    {
      return cache.get(codeOrOid);
    }

    return null;
  }

  @SuppressWarnings("unchecked")
  private static Map<String, ServerElement> getTransactionCache()
  {
    TransactionState state = TransactionState.getCurrentTransactionState();

    if (state != null)
    {
      Map<String, ServerElement> cache = (Map<String, ServerElement>) state.getTransactionObject(TRANSACTION_CACHE_KEY);

      if (cache == null)
      {
        cache = new TreeMap<String, ServerElement>();

        state.putTransactionObject(TRANSACTION_CACHE_KEY, cache);
      }

      return cache;
    }

    return null;
  }

}
