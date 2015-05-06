package com.runwaysdk.geodashboard.oda.driver;

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
