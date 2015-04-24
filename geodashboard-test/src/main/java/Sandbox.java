import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.eclipse.emf.ecore.xml.type.internal.DataValue.Base64;

public class Sandbox
{
  public static void main(String[] args) throws NoSuchAlgorithmException
  {

    KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
    keyGenerator.init(128);

    SecretKey key = keyGenerator.generateKey();

    byte[] bytes = key.getEncoded();

    System.out.println(Base64.encode(bytes));
  }
}
