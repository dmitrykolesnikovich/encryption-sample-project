package encryptionSample;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

public class ServerDecrypt {

  static String privateKey =
      "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJWXJuOVZ/CU1uIeaTuk5Ii4qgDb\n" +
          "gukTYL6MFjs6mx6/XErDZcWfDYM9hxvEtAq2G89b8lwsDNGaKmFZmBE5b14oe7m6IiJQdAuZ8xkM\n" +
          "tvxe5hc3q/p8OF/U1dR0R68/pdLVQkrEHdLuR3eKXqoTIn08VYn9qJyIrLwe5fHtGN8lAgMBAAEC\n" +
          "gYA4RzWKSZthF54AVBCjL9YK2e6bg/osVe3/whRbFCFn3/aI6hpPzxb2WR+LguV5Yin0SVNU+f2Y\n" +
          "nbd0CJD9ae5LlwaNtpmRJoeqsK8F9epEL++y/v3vetJLDyUao/8fJhsZ1lJXbKcDHp3o6mpztoBn\n" +
          "4u11RovTwddIf4qnmn5dwQJBAPc0MfodGAhy5wvLILlQ3V3yYnBZVTZzoBa2yn/Z0BjXCxx8MA+/\n" +
          "dlAAIfRDOF8hFju1MHy29aQvieFf1CdS6ScCQQCa6ck7tTghia+EFnUzgBkVWFkXOD9YsEEjyeF9\n" +
          "4Uvo5wj+lh4Jm3+cxdgTKj+weVyrBBsqNvxOMlUOT7gM0CzTAkEAl+5tdPJirfaoyBfNAfiQRUhO\n" +
          "dgyBkdjYoH0x0gg1nL62JoixJUygU6TxOWYDBHyaZJIEvfHY4VMSZAD4rD6J6wJAay/L/zY6qmn8\n" +
          "OabYXVQLBwvkSP6wRgteZwbusQzMW1BQlucDzZ38RFtYUJpxCwhOKD5lFWaKWQjWdVqPfL4l5QJA\n" +
          "ZxQGf+OJkWk5oeORHSP1/0edqPXKKIsOtlt6cQl+8u18kThNZRhcoCswAVjUZxGgHbsdT0n/d+le\n" +
          "0tteBJ6jhQ==\n";
  static String encryptedTextString =
      "3NGmDOKDZr507ILep04MC9ejxiGUpkQPGZ4irzThTIqvkwHiirOqRE222sTr6sSexKj6pbFr4+vZ\n" +
          "17ZscGzqCw==\n";
  static String encryptedSecretKeyString =
      "SBA6eq42/HXVyqqsHcNq61bxiSWnsoVSHPM1nIIENvj+GULX2+WkI5jRgvzaQirUmQ+FBzaSa4Xx\n" +
          "4QWFjRtdMWxk0n4X+cJd5XHnYORzFB2iCn9hB++NGjEuW8asOMS6ua7vY0UEDbrNbsB17o5b8AGn\n" +
          "0mTxjQ6Os789gmHGW+M=\n";

  public static void main(String args[]) {

    try {

      // 1. Get private key
      PKCS8EncodedKeySpec privateSpec = new PKCS8EncodedKeySpec(Base64.decode(privateKey, Base64.DEFAULT));
      KeyFactory keyFactory = KeyFactory.getInstance("RSA");
      PrivateKey privateKey = keyFactory.generatePrivate(privateSpec);

      // 2. Decrypt encrypted secret key using private key
      Cipher cipher1 = Cipher.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding");
      cipher1.init(Cipher.DECRYPT_MODE, privateKey);
      byte[] secretKeyBytes = cipher1.doFinal(Base64.decode(encryptedSecretKeyString, Base64.DEFAULT));
      SecretKey secretKey = new SecretKeySpec(secretKeyBytes, 0, secretKeyBytes.length, "AES");

      // 3. Decrypt encrypted text using secret key
      byte[] raw = secretKey.getEncoded();
      SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
      cipher.init(Cipher.DECRYPT_MODE, skeySpec, new IvParameterSpec(new byte[16]));
      byte[] original = cipher.doFinal(Base64.decode(encryptedTextString, Base64.DEFAULT));
      String text = new String(original, Charset.forName("UTF-8"));

      // 4. Print the original text sent by client
      System.out.println("text: " + text);

    } catch (NoSuchAlgorithmException | InvalidKeySpecException | InvalidKeyException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException | InvalidAlgorithmParameterException e) {
      e.printStackTrace();
    }
  }
}
