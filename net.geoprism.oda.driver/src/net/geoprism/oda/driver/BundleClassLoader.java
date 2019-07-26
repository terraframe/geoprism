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
package net.geoprism.oda.driver;

import org.osgi.framework.Bundle;

import com.runwaysdk.generation.loader.ReloadableClassLoaderIF;

public class BundleClassLoader
{
  private Bundle bundle;

  public BundleClassLoader(Bundle bundle)
  {
    this.bundle = bundle;
  }

  public void addListener(Object object)
  {
  }

  public Class<?> load(String type, boolean proccessException) throws ClassNotFoundException
  {
    return this.bundle.loadClass(type);
  }

  public Class<?> loadClass(String type) throws ClassNotFoundException
  {
    return this.bundle.loadClass(type);
  }

  public Class<?> loadClass(String type, boolean arg1) throws ClassNotFoundException
  {
    return this.bundle.loadClass(type);
  }

  public void newLoader()
  {
    // do nothing
  }

  public void notifyListeners()
  {
    // do nothing
  }

  public void removeListener(Object object)
  {
    // do nothing
  }

  public void setParent(ClassLoader parent)
  {
    // do nothing
  }

}
