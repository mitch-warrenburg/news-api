package com.labelbox.news.newsapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.labelbox.news.newsapi.client.NewsClient;
import com.labelbox.news.newsapi.enumeration.Country;
import com.labelbox.news.newsapi.enumeration.Language;
import com.labelbox.news.newsapi.enumeration.NewsTopic;
import com.labelbox.news.newsapi.model.ErrorResponse;
import java.util.Set;
import javax.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.HttpClientErrorException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.GATEWAY_TIMEOUT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest({ NewsController.class, NewsControllerAdvice.class })
class NewsControllerAdviceTest {

    @MockBean
    NewsClient client;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("given a ConstraintViolationException is thrown by a controller, "
            + "it returns an ErrorResponse with an appropriate message for the exception, "
            + "a 'Bad Request' status "
            + "and the corresponding 400 status code")
    void handleBadRequest()
            throws Exception {

        ConstraintViolationException exception = new ConstraintViolationException("validation failure", Set.of());
        ErrorResponse expectedResponse = ErrorResponse.builder()
                .code(400)
                .status("Bad Request")
                .message("ConstraintViolationException: validation failure")
                .build();

        String expectedResponseJson = objectMapper.writeValueAsString(expectedResponse);

        when(client.searchSources(any(), any(), any())).thenThrow(exception);

        mockMvc.perform(get("/api/v1/news/sources")
                        .param("topic", "science")
                        .param("countries", "US")
                        .param("languages", "en"))
                .andDo(log())
                .andExpect(status().isBadRequest())
                .andExpect(content().json(expectedResponseJson));

        verify(client).searchSources(NewsTopic.science, Set.of(Language.en), Set.of(Country.US));
    }

    @Test
    @DisplayName("given an unexpected Exception is thrown by a controller, "
            + "it returns an ErrorResponse with an appropriate message for the exception, "
            + "an 'Internal Server Error' status "
            + "and the corresponding 500 status code")
    void handleInternalServerError()
            throws Exception {

        HttpClientErrorException exception = new HttpClientErrorException(GATEWAY_TIMEOUT);
        ErrorResponse expectedResponse = ErrorResponse.builder()
                .code(500)
                .status("Internal Server Error")
                .message("HttpClientErrorException: 504 GATEWAY_TIMEOUT")
                .build();

        String expectedResponseJson = objectMapper.writeValueAsString(expectedResponse);

        when(client.searchSources(any(), any(), any())).thenThrow(exception);

        mockMvc.perform(get("/api/v1/news/sources")
                        .param("topic", "science")
                        .param("countries", "US")
                        .param("languages", "en"))
                .andDo(log())
                .andExpect(status().isInternalServerError())
                .andExpect(content().json(expectedResponseJson));

        verify(client).searchSources(NewsTopic.science, Set.of(Language.en), Set.of(Country.US));
    }

}