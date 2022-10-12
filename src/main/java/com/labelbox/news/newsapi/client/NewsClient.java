package com.labelbox.news.newsapi.client;

import com.labelbox.news.newsapi.enumeration.Country;
import com.labelbox.news.newsapi.enumeration.Language;
import com.labelbox.news.newsapi.enumeration.NewsSearchType;
import com.labelbox.news.newsapi.enumeration.NewsSort;
import com.labelbox.news.newsapi.enumeration.NewsTopic;
import com.labelbox.news.newsapi.model.NewsSearchResponse;
import com.labelbox.news.newsapi.model.NewsSourcesResponse;
import java.time.LocalDate;
import java.util.Collection;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/** @author Mitch Warrenburg */
@Validated
@FeignClient(
    name = "NewsClient",
    url = "${news-catcher-api.url}",
    path = "${news-catcher-api.path}")
public interface NewsClient {

  String NEWS_CACHE_NAME = "news";

  @Valid
  @GetMapping("/search")
  @Cacheable(NEWS_CACHE_NAME)
  NewsSearchResponse searchArticles(
      @RequestParam("q") String query,
      @RequestParam(name = "page", required = false) Integer page,
      @Min(1) @Max(100) @RequestParam(name = "page_size", required = false) Integer pageSize,
      @RequestParam(name = "sort_by", required = false) NewsSort sortBy,
      @RequestParam(name = "topic", required = false) NewsTopic topic,
      @RequestParam(name = "search_in", required = false) NewsSearchType searchType,
      @RequestParam(name = "sources", required = false) Collection<String> sources,
      @RequestParam(name = "not_sources", required = false) Collection<String> excludeSources,
      @RequestParam(name = "lang", required = false) Collection<Language> languages,
      @RequestParam(name = "countries", required = false) @Nullable Collection<Country> countries,
      @RequestParam(name = "from", required = false) @DateTimeFormat(pattern = "yyyy/MM/dd")
          LocalDate fromDate,
      @RequestParam(name = "to", required = false) @DateTimeFormat(pattern = "yyyy/MM/dd")
          LocalDate toDate);

  @GetMapping("/sources")
  NewsSourcesResponse searchSources(
      @RequestParam(name = "topic", required = false) NewsTopic topic,
      @RequestParam(name = "lang", required = false) Collection<Language> languages,
      @RequestParam(name = "countries", required = false) Collection<Country> countries);
}
