package encryptionSample;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

import static encryptionSample.Utils.readFile;

public class ServerDecrypt {

  private static final String privateKey = readFile("privateKey.txt");
  private static final String encryptedTextString = readFile("message-encrypted.txt");
  private static final String encryptedSecretKeyString = readFile("secretKey-encrypted.txt");

  public static void main(String[] args) throws Throwable {
    // 1. Get private key
    PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(Base64.decode(privateKey, Base64.DEFAULT));
    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
    PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

    // 2. Decrypt encrypted secret key using private key
    Cipher cipher1 = Cipher.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding");
    cipher1.init(Cipher.DECRYPT_MODE, privateKey);
    byte[] secretKeyBytes = cipher1.doFinal(Base64.decode(encryptedSecretKeyString, Base64.DEFAULT));
    SecretKey secretKey = new SecretKeySpec(secretKeyBytes, 0, secretKeyBytes.length, "AES");

    // 3. Decrypt encrypted message using secret key
    SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getEncoded(), "AES");
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, new IvParameterSpec(new byte[16]));
    byte[] messageBytes = cipher.doFinal(Base64.decode(encryptedTextString, Base64.DEFAULT));
    String message = new String(messageBytes, Charset.forName("UTF-8"));

    // print result
    System.out.println("message: '" + message + "'");
  }

}
