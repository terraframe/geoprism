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
