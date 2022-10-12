package com.labelbox.news.newsapi.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.labelbox.news.newsapi.client.NewsClient;
import com.labelbox.news.newsapi.enumeration.Country;
import com.labelbox.news.newsapi.enumeration.Language;
import com.labelbox.news.newsapi.enumeration.NewsSearchType;
import com.labelbox.news.newsapi.enumeration.NewsSort;
import com.labelbox.news.newsapi.enumeration.NewsTopic;
import com.labelbox.news.newsapi.model.NewsSearchResponse;
import com.labelbox.news.newsapi.model.NewsSourcesResponse;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** @author Mitch Warrenburg */
@RestController
@AllArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/v1/news", produces = APPLICATION_JSON_VALUE)
public class NewsController {

  private final NewsClient client;

  @GetMapping("/sources")
  @Tag(name = "News Sources", description = "Get domain names for news sources.")
  @Parameter(name = "topic", description = "Limit results by topic.")
  @Parameter(
      name = "languages",
      example = "[\"en\", \"es\"]",
      description =
          "Filter results by language (comma-delimited). Accepts ISO ISO 639-1 language codes "
              + "(https://en.wikipedia.org/wiki/ISO_639-1).")
  @Parameter(
      name = "countries",
      example = "[\"US\",\"CA\"]",
      description =
          "Filter results by country (comma-delimited). Accepts ISO 3166-1 country codes "
              + "(https://en .wikipedia.org/wiki/ISO_3166-1_alpha-2).")
  public NewsSourcesResponse searchSources(
      @RequestParam(required = false) NewsTopic topic,
      @RequestParam(required = false) @Nullable Collection<Country> countries,
      @RequestParam(required = false, defaultValue = "en") Set<Language> languages) {
    return client.searchSources(topic, languages, countries);
  }

  @GetMapping("/search")
  @Tag(name = "News Article Search", description = "Search for news articles.")
  @Parameter(name = "pageSize", description = "The number of results to return per page (1-100).")
  @Parameter(
      name = "searchIn",
      description = "Limit results where query matches either `title` or `summary`.")
  @Parameter(
      name = "q",
      example = "`ukraine` `putin`",
      description = "The keyword to query (space-delimited).")
  @Parameter(
      name = "topic",
      description = "Limit results by topic (Not all news articles are assigned with a topic).")
  @Parameter(
      name = "sources",
      example = "[\"npr.org\"]",
      description = "Filter results by the domain name of the news source.")
  @Parameter(
      name = "excludeSources",
      example = "[\"foxnews.com\"]",
      description = "Exclude results by the domain name of the news source (comma-delimited).")
  @Parameter(
      name = "toDate",
      example = "2022/10/12",
      description =
          "Point in time to end the search. The default timezone is UTC. Defaults to the past week.")
  @Parameter(
      name = "sortBy",
      schema =
          @Schema(
              defaultValue = "relevancy",
              allowableValues = {"date", "rank", "relevancy"}),
      description = "Sort results by `date`, `rank` or `relevancy`.")
  @Parameter(
      name = "fromDate",
      example = "2022/10/01",
      description =
          "Point in time to start the search. The default timezone is UTC. Defaults to the past week."
              + "  Must be within the last 14 days or less.")
  @Parameter(
      name = "languages",
      example = "[\"en\", \"es\"]",
      description =
          "Filter results by language (comma-delimited). Accepts ISO ISO 639-1 language codes "
              + "(https://en.wikipedia.org/wiki/ISO_639-1).")
  @Parameter(
      name = "countries",
      example = "[\"US\",\"CA\"]",
      description =
          "Filter results by country (comma-delimited). Accepts ISO 3166-1 country codes "
              + "(https://en .wikipedia.org/wiki/ISO_3166-1_alpha-2).")
  public NewsSearchResponse searchArticles(
      @RequestParam("q") String query,
      @RequestParam(required = false) Integer page,
      @RequestParam(required = false) Integer pageSize,
      @RequestParam(required = false) NewsSort sortBy,
      @RequestParam(required = false) NewsTopic topic,
      @RequestParam(required = false) NewsSearchType searchIn,
      @RequestParam(required = false) Set<String> sources,
      @RequestParam(required = false) Set<String> excludeSources,
      @RequestParam(required = false) Set<Language> languages,
      @RequestParam(required = false) Set<Country> countries,
      @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy/MM/dd") LocalDate fromDate,
      @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy/MM/dd") LocalDate toDate) {
    return client.searchArticles(
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
