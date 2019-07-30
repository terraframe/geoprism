/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
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
package net.geoprism.oda.driver;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class CryptographySingleton
{
  private static final String KEY = "74vVNRzY5eB/roZQTnu9RewKjYQIa5L/f/0jKBrogAk= ";

  private static final String IV  = "HqR6Lb0VtDjA8cr2rJTg4hePZGjE0Y8UsePJU6H4h3g=";

  private static Cryptography instance;

  private synchronized static Cryptography getInstance() throws UnsupportedEncodingException, NoSuchAlgorithmException
  {
    if (instance == null)
    {
      MessageDigest sha = MessageDigest.getInstance("SHA-1");

      byte[] keyBytes = KEY.getBytes("UTF-8");
      keyBytes = sha.digest(keyBytes);
      keyBytes = Arrays.copyOf(keyBytes, 16); // use only first 128 bit

      byte[] ivBytes = IV.getBytes("UTF-8");
      ivBytes = sha.digest(keyBytes);
      ivBytes = Arrays.copyOf(keyBytes, 16); // Only use the first 16 bytes

      instance = new Cryptography(keyBytes, ivBytes);
    }

    return instance;
  }

  public static String encrypt(String input)
  {
    try
    {
      return getInstance().encrypt(input);
    }
    catch (RuntimeException e)
    {
      throw e;
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
  }

  public static String decrypt(String input)
  {
    try
    {
      return (String) getInstance().decrypt(input);
    }
    catch (RuntimeException e)
    {
      throw e;
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
  }
}
