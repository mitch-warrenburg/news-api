package com.labelbox.news.newsapi;

import static com.labelbox.news.newsapi.util.KeyFileUtil.writeGcpKeyFile;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;

/** @author Mitch Warrenburg */
@EnableCaching
@EnableFeignClients
@SpringBootApplication
public class NewsApiApplication {

  public static void main(String[] args) {
    writeGcpKeyFile();
    SpringApplication.run(NewsApiApplication.class, args);
  }
}
