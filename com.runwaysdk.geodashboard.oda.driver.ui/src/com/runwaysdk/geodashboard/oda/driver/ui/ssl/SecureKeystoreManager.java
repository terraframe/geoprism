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
package com.runwaysdk.geodashboard.oda.driver.ui.ssl;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import org.eclipse.core.runtime.Platform;
import org.eclipse.equinox.security.storage.ISecurePreferences;
import org.eclipse.equinox.security.storage.SecurePreferencesFactory;
import org.eclipse.equinox.security.storage.StorageException;
import org.eclipse.osgi.service.datalocation.Location;

import com.runwaysdk.geodashboard.oda.driver.SSLContextConfiguration;

public class SecureKeystoreManager
{
  private static final String          KEYSTORE        = "keystore";    //$NON-NLS-1$

  private static final String          PASSWORD        = "password";    //$NON-NLS-1$

  private static final String          TRUSTSTORE_NAME = "trust.ks";    //$NON-NLS-1$

  /**
   * ISecurePreference node name specific to this plugin
   */
  private static final String          NODE_NAME       = "geodashboard"; //$NON-NLS-1$

  private static SecureKeystoreManager instance;

  /**
   * Flag denoting if the SSL context needs to be configured. This may change if new certificates are added to the
   * keystore.
   */
  private boolean                      configured;

  private SecureKeystoreManager()
  {
    this.configured = false;
  }

  public synchronized void configureSSLContext()
  {
    if (!this.configured)
    {
      try
      {
        KeyStore ts = this.getKeystore();

        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(ts);

        SSLContext context = SSLContext.getInstance("SSL");
        context.init(new KeyManager[] {}, tmf.getTrustManagers(), new SecureRandom());

        SSLContextConfiguration.configure(context);

        this.configured = true;
      }
      catch (NoSuchAlgorithmException e)
      {
        throw new SSLContextException(e);
      }
      catch (KeyStoreException e)
      {
        throw new SSLContextException(e);
      }
      catch (KeyManagementException e)
      {
        throw new SSLContextException(e);
      }
    }
  }

  private synchronized KeyStore getKeystore()
  {
    try
    {
      Map<String, String> props = this.getKeystoreProperties();

      String path = props.get(KEYSTORE);
      String password = props.get(PASSWORD);

      if (!new File(path).exists())
      {
        this.createKeystore(props);
      }

      FileInputStream fis = new FileInputStream(path);

      try
      {
        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        ks.load(fis, password.toCharArray());

        return ks;
      }
      finally
      {
        fis.close();
      }
    }
    catch (KeyStoreException e)
    {
      throw new SecureKeystoreException(e);
    }
    catch (NoSuchAlgorithmException e)
    {
      throw new SecureKeystoreException(e);
    }
    catch (CertificateException e)
    {
      throw new SecureKeystoreException(e);
    }
    catch (IOException e)
    {
      throw new SecureKeystoreException(e);
    }
    catch (StorageException e)
    {
      throw new SecureKeystoreException(e);
    }
  }

  private synchronized void write(KeyStore keystore)
  {
    try
    {
      Map<String, String> props = this.getKeystoreProperties();

      String path = props.get(KEYSTORE);
      String password = props.get(PASSWORD);

      if (!new File(path).exists())
      {
        this.createKeystore(props);
      }

      FileOutputStream fos = new FileOutputStream(path);

      try
      {
        keystore.store(fos, password.toCharArray());
      }
      finally
      {
        fos.close();
      }
    }
    catch (KeyStoreException e)
    {
      throw new SecureKeystoreException(e);
    }
    catch (NoSuchAlgorithmException e)
    {
      throw new SecureKeystoreException(e);
    }
    catch (CertificateException e)
    {
      throw new SecureKeystoreException(e);
    }
    catch (IOException e)
    {
      throw new SecureKeystoreException(e);
    }
    catch (StorageException e)
    {
      throw new SecureKeystoreException(e);
    }
  }

  private Map<String, String> getKeystoreProperties() throws StorageException, IOException
  {
    ISecurePreferences root = SecurePreferencesFactory.getDefault();
    ISecurePreferences node = root.node(NODE_NAME);

    String path = node.get(KEYSTORE, ""); //$NON-NLS-1$
    String password = node.get(PASSWORD, ""); //$NON-NLS-1$

    if (path.equals("") || password.equals("")) //$NON-NLS-1$
    {
      Location config = Platform.getConfigurationLocation();
      String location = config.getURL().getPath();

      path = new File(location, TRUSTSTORE_NAME).getAbsolutePath();

      // Generate a new keystore and password
      password = new Double(new SecureRandom().nextDouble()).toString();

      node.put(KEYSTORE, path, true);
      node.put(PASSWORD, password, true);

      node.flush();
    }

    Map<String, String> map = new HashMap<String, String>();
    map.put(KEYSTORE, path);
    map.put(PASSWORD, password);

    return map;
  }

  private void createKeystore(Map<String, String> properties) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException
  {
    String path = properties.get(KEYSTORE);
    String password = properties.get(PASSWORD);

    FileOutputStream fos = new FileOutputStream(path);

    try
    {
      // Create the trust store
      KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
      ks.load(null, password.toCharArray());

      ks.store(fos, password.toCharArray());
    }
    finally
    {
      fos.close();
    }
  }

  public synchronized void delete() throws StorageException, IOException
  {
    Map<String, String> props = this.getKeystoreProperties();

    String path = props.get(KEYSTORE);

    File file = new File(path);

    if (file.exists())
    {
      file.delete();
    }

    this.configured = false;
  }

  public synchronized void addCertificate(FileInputStream stream, String alias) throws KeyStoreException, CertificateException, IOException
  {
    KeyStore keystore = this.getKeystore();

    if (!keystore.containsAlias(alias))
    {
      BufferedInputStream bis = new BufferedInputStream(stream);
      CertificateFactory cf = CertificateFactory.getInstance("X.509"); //$NON-NLS-1$

      while (bis.available() > 0)
      {
        Certificate cert = cf.generateCertificate(bis);
        keystore.setCertificateEntry(alias, cert);
      }

      this.write(keystore);

      this.configured = false;
    }
    else
    {
      throw new DuplicateCertificateException();
    }
  }

  public static synchronized SecureKeystoreManager getInstance()
  {
    if (instance == null)
    {
      instance = new SecureKeystoreManager();
    }

    return instance;
  }
}
