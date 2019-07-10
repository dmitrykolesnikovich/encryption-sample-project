package encryptionSample;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

import static encryptionSample.Utils.readFile;

public class ClientEncrypt {

  private static final String publicKeyString = readFile("publicKey.txt");
  private static final String text = readFile("message.txt");

  public static void main(String[] args) throws Throwable {
    // 1. generate secret key using AES
    KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
    keyGenerator.init(128); // AES is currently available in three key sizes: 128, 192 and 256 bits.The design and strength of all key lengths of the AES algorithm are sufficient to protect classified information up to the SECRET level
    SecretKey secretKey = keyGenerator.generateKey();

    // 2. encrypt string using secret key
    byte[] raw = secretKey.getEncoded();
    SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.ENCRYPT_MODE, skeySpec, new IvParameterSpec(new byte[16]));
    String cipherTextString = Base64.encodeToString(cipher.doFinal(text.getBytes(Charset.forName("UTF-8"))), Base64.DEFAULT);

    // 3. get public key
    X509EncodedKeySpec publicSpec = new X509EncodedKeySpec(Base64.decode(publicKeyString, Base64.DEFAULT));
    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
    PublicKey publicKey = keyFactory.generatePublic(publicSpec);

    // 4. encrypt secret key using public key
    Cipher cipher2 = Cipher.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding");
    cipher2.init(Cipher.ENCRYPT_MODE, publicKey);
    String encryptedSecretKey = Base64.encodeToString(cipher2.doFinal(secretKey.getEncoded()), Base64.DEFAULT);

    // print result
    System.out.println("cipherTextString: '" + cipherTextString + "'");
    System.out.println("encryptedSecretKey: '" + encryptedSecretKey + "'");
  }

}
