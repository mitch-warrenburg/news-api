package com.labelbox.news.newsapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.labelbox.news.newsapi.client.NewsClient;
import com.labelbox.news.newsapi.enumeration.Country;
import com.labelbox.news.newsapi.enumeration.Language;
import com.labelbox.news.newsapi.enumeration.NewsSearchType;
import com.labelbox.news.newsapi.enumeration.NewsSort;
import com.labelbox.news.newsapi.enumeration.NewsTopic;
import com.labelbox.news.newsapi.model.NewsArticle;
import com.labelbox.news.newsapi.model.NewsSearchResponse;
import com.labelbox.news.newsapi.model.NewsSourcesResponse;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static java.lang.String.join;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.stream.Collectors.joining;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NewsController.class)
@ExtendWith(MockitoExtension.class)
class NewsControllerTest {

    @MockBean
    NewsClient client;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName(
            "given a GET request to /api/v1/news/sources, "
                    + "it deserializes the provided parameters as expected, "
                    + "and delegates to the NewsClient "
                    + "and returns the resulting NewsSourcesResponse and a 200 status code")
    void getSources()
            throws Exception {

        NewsTopic topic = NewsTopic.science;
        Set<Country> countries = Set.of(Country.US, Country.CA);
        Set<Language> languages = Set.of(Language.en, Language.es);
        List<String> sources = List.of("bloomberg.com", "theguardian.com");

        NewsSourcesResponse response = NewsSourcesResponse.builder()
                .sources(sources)
                .build();

        String expectedResponseJson = objectMapper.writeValueAsString(response);

        when(client.searchSources(any(NewsTopic.class), anyCollection(), anyCollection()))
                .thenReturn(response);

        mockMvc.perform(get("/api/v1/news/sources")
                        .param("topic", topic.name())
                        .param("countries", countries.stream().map(Country::name).collect(joining(",")))
                        .param("languages", languages.stream().map(Language::name).collect(joining(","))))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(content().json(expectedResponseJson));

        verify(client).searchSources(topic, languages, countries);
    }

    @Test
    @DisplayName(
            "given a GET request to /api/v1/news/search, "
                    + "it deserializes the provided parameters as expected, "
                    + "and delegates to the NewsClient "
                    + "and returns the resulting NewsSearchResponse and a 200 status code")
    void searchArticles()
            throws Exception {

        String query = "query";
        Integer page = 1;
        Integer pageSize = 50;
        NewsSort sortBy = NewsSort.date;
        NewsTopic topic = NewsTopic.tech;
        NewsSearchType searchIn = NewsSearchType.summary;
        Set<String> sources = Set.of("npr.org", "cnn.com");
        Set<String> excludeSources = Set.of("foxnews.com");
        Set<Language> languages = Set.of(Language.en, Language.es);
        Set<Country> countries = Set.of(Country.US, Country.CA);
        LocalDate fromDate = LocalDate.of(2022, 10, 5);
        LocalDate toDate = LocalDate.of(2022, 10, 12);

        NewsArticle article = NewsArticle.builder()
                .id("id")
                .link("link")
                .media("media")
                .authors("authors")
                .cleanUrl("cleanUrl")
                .publishedDate("2022/10/10")
                .rank(999)
                .excerpt("excerpt")
                .title("title")
                .isOpinion(true)
                .rights("cnn.com")
                .country(Country.US)
                .language(Language.en)
                .topic(NewsTopic.tech)
                .twitterAccount("@someInterestingNewsOutlet")
                .build();

        List<NewsArticle> articles = List.of(article);
        NewsSearchResponse response = NewsSearchResponse.builder()
                .status("ok")
                .page(1)
                .pageSize(50)
                .totalPages(4)
                .totalHits(200)
                .articles(articles)
                .build();

        String expectedResponseJson = objectMapper.writeValueAsString(response);

        when(client
                .searchArticles(anyString(),
                        anyInt(),
                        anyInt(),
                        any(NewsSort.class),
                        any(NewsTopic.class),
                        any(NewsSearchType.class),
                        anyCollection(),
                        anyCollection(),
                        anyCollection(),
                        anyCollection(),
                        any(LocalDate.class),
                        any(LocalDate.class)))
                .thenReturn(response);

        mockMvc.perform(get("/api/v1/news/search")
                        .param("q", query)
                        .param("page", page.toString())
                        .param("pageSize", pageSize.toString())
                        .param("topic", topic.name())
                        .param("sortBy", sortBy.name())
                        .param("searchIn", searchIn.name())
                        .param("sources", join(",", sources))
                        .param("excludeSources", join(",", excludeSources))
                        .param("toDate", toDate.format(ofPattern("yyyy/MM/dd")))
                        .param("fromDate", fromDate.format(ofPattern("yyyy/MM/dd")))
                        .param("languages", languages.stream().map(Language::name).collect(joining(",")))
                        .param("countries", countries.stream().map(Country::name).collect(joining(","))))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(content().json(expectedResponseJson));

        verify(client).searchArticles(
                query,
                page,
                pageSize,
                sortBy,
                topic,
                searchIn,
                sources,
                excludeSources,
                languages,
                countries,
                fromDate,
                toDate);
    }

}