package com.runwaysdk.geodashboard.oda.driver;

import org.osgi.framework.Bundle;

import com.runwaysdk.generation.loader.ReloadableClassLoaderIF;

public class BundleClassLoader implements ReloadableClassLoaderIF
{
  private Bundle bundle;

  public BundleClassLoader(Bundle bundle)
  {
    this.bundle = bundle;
  }

  @Override
  public void addListener(Object object)
  {
  }

  @Override
  public Class<?> load(String type, boolean proccessException) throws ClassNotFoundException
  {
    return this.bundle.loadClass(type);
  }

  @Override
  public Class<?> loadClass(String type) throws ClassNotFoundException
  {
    return this.bundle.loadClass(type);
  }

  @Override
  public Class<?> loadClass(String type, boolean arg1) throws ClassNotFoundException
  {
    return this.bundle.loadClass(type);
  }

  @Override
  public void newLoader()
  {
    // do nothing
  }

  @Override
  public void notifyListeners()
  {
    // do nothing
  }

  @Override
  public void removeListener(Object object)
  {
    // do nothing
  }

  @Override
  public void setParent(ClassLoader parent)
  {
    // do nothing
  }

}
