package encryptionSample;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

public class ClientEncrypt {

  private static String publicKeyString =
          "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCVlybjlWfwlNbiHmk7pOSIuKoA24LpE2C+jBY7\n" +
          "Opsev1xKw2XFnw2DPYcbxLQKthvPW/JcLAzRmiphWZgROW9eKHu5uiIiUHQLmfMZDLb8XuYXN6v6\n" +
          "fDhf1NXUdEevP6XS1UJKxB3S7kd3il6qEyJ9PFWJ/aiciKy8HuXx7RjfJQIDAQAB\n";

  public static void main(String args[]) {
    try {

      // 1. generate secret key using AES
      KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
      keyGenerator.init(128); // AES is currently available in three key sizes: 128, 192 and 256 bits.The design and strength of all key lengths of the AES algorithm are sufficient to protect classified information up to the SECRET level
      SecretKey secretKey = keyGenerator.generateKey();

      // 2. get string which needs to be encrypted
      String text = "Featurea is the game development platform in Kotlin";

      // 3. encrypt string using secret key
      byte[] raw = secretKey.getEncoded();
      SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
      cipher.init(Cipher.ENCRYPT_MODE, skeySpec, new IvParameterSpec(new byte[16]));
      String cipherTextString = Base64.encodeToString(cipher.doFinal(text.getBytes(Charset.forName("UTF-8"))), Base64.DEFAULT);

      // 4. get public key
      X509EncodedKeySpec publicSpec = new X509EncodedKeySpec(Base64.decode(publicKeyString, Base64.DEFAULT));
      KeyFactory keyFactory = KeyFactory.getInstance("RSA");
      PublicKey publicKey = keyFactory.generatePublic(publicSpec);

      // 6. encrypt secret key using public key
      Cipher cipher2 = Cipher.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding");
      cipher2.init(Cipher.ENCRYPT_MODE, publicKey);
      String encryptedSecretKey = Base64.encodeToString(cipher2.doFinal(secretKey.getEncoded()), Base64.DEFAULT);

      // 7. pass cipherTextString (encypted sensitive data) and encryptedSecretKey to your server via your preferred way.
      // Tips:
      // You may use JSON to combine both the strings under 1 object.
      // You may use a volley call to send this data to your server.
      System.out.println("cipherTextString: '" + cipherTextString + "'");
      System.out.println("encryptedSecretKey: '" + encryptedSecretKey + "'");
    } catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException | InvalidKeySpecException | InvalidAlgorithmParameterException e) {
      e.printStackTrace();
    }
  }
}