package com.labelbox.news.newsapi;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.test.util.AssertionErrors.assertTrue;

@SpringBootTest(properties = "feign.client.config.newsclient.defaultrequestheaders.x-api-key=test-key")
class NewsApiApplicationTests {

    @Test
    void contextLoads() {
        assertTrue("Failed to load the application context.", true);
    }

}
