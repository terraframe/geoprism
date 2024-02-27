package net.geoprism.registry.model;

import java.util.Locale;
import java.util.Set;

import org.commongeoregistry.adapter.dataaccess.LocalizedValue;

import com.runwaysdk.business.graph.VertexObject;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.localization.LocalizationFacade;

import net.geoprism.registry.graph.AttributeType;

public class LocalValueNodeStrategy extends ValueNodeStrategy implements ValueStrategy
{

  public LocalValueNodeStrategy(AttributeType type, MdVertexDAOIF nodeVertex, String nodeAttribute)
  {
    super(type, nodeVertex, nodeAttribute);
  }

  @Override
  protected void setNodeValue(VertexObject node, Object value)
  {
    if (value instanceof LocalizedValue)
    {
      LocalizedValue lValue = (LocalizedValue) value;

      node.setValue(LocalizedValue.DEFAULT_LOCALE, lValue.getValue(LocalizedValue.DEFAULT_LOCALE));

      Set<Locale> locales = LocalizationFacade.getInstalledLocales();

      for (Locale locale : locales)
      {
        String localeName = locale.toString();

        if (lValue.contains(locale) && node.hasAttribute(localeName))
        {
          node.setValue(localeName, lValue.getValue(localeName));
        }
      }
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  protected <T> T getNodeValue(VertexObject node)
  {
    LocalizedValue value = new LocalizedValue(node.getObjectValue(LocalizedValue.DEFAULT_LOCALE));
    value.setValue(LocalizedValue.DEFAULT_LOCALE, node.getObjectValue(LocalizedValue.DEFAULT_LOCALE));

    Set<Locale> locales = LocalizationFacade.getInstalledLocales();

    for (Locale locale : locales)
    {
      String localeName = locale.toString();

      if (node.hasAttribute(localeName))
      {
        value.setValue(locale, node.getObjectValue(localeName));
      }
    }

    return (T) value;
  }

}
