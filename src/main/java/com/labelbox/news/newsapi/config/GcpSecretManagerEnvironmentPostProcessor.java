package com.labelbox.news.newsapi.config;

import static java.lang.Boolean.parseBoolean;
import static org.apache.commons.lang3.BooleanUtils.FALSE;

import com.google.protobuf.ByteString;
import lombok.val;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.stereotype.Component;

/** @author Mitch Warrenburg */

/* Note: For deployment purposes only */
@Component
public class GcpSecretManagerEnvironmentPostProcessor implements EnvironmentPostProcessor {

  private static final String SECRET_MANAGER_ENABLED_PROPERTY =
      "spring.cloud.gcp.secretmanager.enabled";

  @Override
  public void postProcessEnvironment(
      ConfigurableEnvironment environment, SpringApplication application) {

    val isSecretManagerEnabled =
        parseBoolean(environment.getProperty(SECRET_MANAGER_ENABLED_PROPERTY, FALSE));

    if (isSecretManagerEnabled) {
      environment
          .getConversionService()
          .addConverter((Converter<ByteString, String>) ByteString::toStringUtf8);

      environment
          .getConversionService()
          .addConverter((Converter<ByteString, byte[]>) ByteString::toByteArray);
    }
  }
}
