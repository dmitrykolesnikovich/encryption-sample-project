package encryptionSample;

import java.security.KeyPair;
import java.security.KeyPairGenerator;

public class GenerateRsaKeyPair {

  public static void main(String[] args) throws Throwable {
    // 1. generate RSA public key and private key
    KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
    keyPairGenerator.initialize(1024); // key length
    KeyPair keyPair = keyPairGenerator.genKeyPair();
    String privateKeyString = Base64.encodeToString(keyPair.getPrivate().getEncoded(), Base64.DEFAULT);
    String publicKeyString = Base64.encodeToString(keyPair.getPublic().getEncoded(), Base64.DEFAULT);

    // 2. print result
    System.out.println("privateKey: '" + privateKeyString + "'");
    System.out.println("publicKey: '" + publicKeyString + "'");
  }

}
