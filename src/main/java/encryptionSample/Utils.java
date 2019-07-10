package encryptionSample;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public final class Utils {

  private Utils() {
    // no op
  }

  public static String readFile(String filePath) {
    try {
      return new String(Files.readAllBytes(new File(ClassLoader.getSystemClassLoader().getResource(filePath).getFile()).toPath()));
    } catch (IOException e) {
      return null;
    }
  }

}
