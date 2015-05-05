package com.runwaysdk.geodashboard.oda.driver;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.runwaysdk.util.Base64;

public class Cryptography
{
  private SecretKeySpec   key;

  private Cipher          cipher;

  private IvParameterSpec iv;

  public Cryptography(byte[] keyBytes, byte[] ivBytes)
  {

    // create the cipher with the algorithm you choose
    // see javadoc for Cipher class for more info, e.g.
    try
    {
      this.key = new SecretKeySpec(keyBytes, "AES");
      this.iv = new IvParameterSpec(ivBytes);
      this.cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    }
    catch (NoSuchAlgorithmException e)
    {
      throw new RuntimeException(e);
    }
    catch (NoSuchPaddingException e)
    {
      throw new RuntimeException(e);
    }
  }

  public String encrypt(Object value) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException, InvalidAlgorithmParameterException
  {
    this.cipher.init(Cipher.ENCRYPT_MODE, this.key, this.iv);

    byte[] encrypted = cipher.doFinal(this.convertToByteArray(value));

    String encode = Base64.encodeToString(encrypted, false);
    return encode;
  }

  public Object decrypt(String encoded) throws IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IOException, ClassNotFoundException, InvalidAlgorithmParameterException
  {
    this.cipher.init(Cipher.DECRYPT_MODE, this.key, this.iv);

    // getting error here
    byte[] bytes = Base64.decode(encoded);
    byte[] decrypted = cipher.doFinal(bytes);

    return this.convertFromByteArray(decrypted);
  }

  private Object convertFromByteArray(byte[] byteObject) throws IOException, ClassNotFoundException
  {
    ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(byteObject));

    try
    {
      Object o = in.readObject();
      return o;
    }
    finally
    {
      in.close();
    }
  }

  private byte[] convertToByteArray(Object object) throws IOException
  {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ObjectOutputStream out = new ObjectOutputStream(baos);

    try
    {
      out.writeObject(object);

      return baos.toByteArray();
    }
    finally
    {
      out.close();
    }
  }

}
