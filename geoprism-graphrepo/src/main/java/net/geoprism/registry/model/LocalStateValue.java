package net.geoprism.registry.model;

import java.util.Locale;
import java.util.Set;

import org.commongeoregistry.adapter.dataaccess.LocalizedValue;

import com.runwaysdk.business.graph.VertexObject;
import com.runwaysdk.localization.LocalizationFacade;

public class LocalStateValue extends StateValue
{

  public LocalStateValue(VertexObject node)
  {
    super(node);
  }

  @Override
  public void setValue(Object value)
  {
    if (value instanceof LocalizedValue)
    {
      LocalizedValue lValue = (LocalizedValue) value;

      this.setValue(LocalizedValue.DEFAULT_LOCALE, lValue.getValue(LocalizedValue.DEFAULT_LOCALE));

      Set<Locale> locales = LocalizationFacade.getInstalledLocales();

      for (Locale locale : locales)
      {
        String localeName = locale.toString();

        if (lValue.contains(locale) && this.hasAttribute(localeName))
        {
          this.setValue(localeName, lValue.getValue(localeName));
        }
      }
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> T getValue()
  {
    LocalizedValue value = new LocalizedValue(this.getValue(LocalizedValue.DEFAULT_LOCALE));
    value.setValue(LocalizedValue.DEFAULT_LOCALE, this.getValue(LocalizedValue.DEFAULT_LOCALE));

    Set<Locale> locales = LocalizationFacade.getInstalledLocales();

    for (Locale locale : locales)
    {
      String localeName = locale.toString();

      if (this.hasAttribute(localeName))
      {
        value.setValue(locale, this.getValue(localeName));
      }
    }

    return (T) value;
  }

}
