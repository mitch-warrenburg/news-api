package com.labelbox.news.newsapi.util;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.isNull;
import static lombok.AccessLevel.PRIVATE;
import static org.apache.commons.io.FileUtils.touch;
import static org.apache.commons.io.FileUtils.writeStringToFile;

import java.io.File;
import java.util.Base64;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.val;
import org.springframework.context.ApplicationContextException;

/** @author Mitch Warrenburg */

/* Note: For deployment purposes only */

@NoArgsConstructor(access = PRIVATE)
public final class KeyFileUtil {

  private static final String GCP_CREDENTIAL_KEY_FILE_NAME = "/tmp/key.json";
  private static final String GCP_CREDENTIAL_ENV_VAR_NAME =
      "ENCODED_GOOGLE_APPLICATION_CREDENTIALS";

  @SneakyThrows
  public static void writeGcpKeyFile() {

    val encodedFileContents = System.getenv(GCP_CREDENTIAL_ENV_VAR_NAME);

    if (isNull(encodedFileContents)) {
      throw new ApplicationContextException(GCP_CREDENTIAL_ENV_VAR_NAME);
    }

    val fileContents = new String(Base64.getDecoder().decode(encodedFileContents));
    val keyFile = new File(GCP_CREDENTIAL_KEY_FILE_NAME);

    touch(keyFile);
    writeStringToFile(keyFile, fileContents, UTF_8);
  }
}
