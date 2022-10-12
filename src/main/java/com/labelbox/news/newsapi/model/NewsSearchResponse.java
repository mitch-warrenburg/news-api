package com.labelbox.news.newsapi.model;

import java.util.List;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

/** @author Mitch Warrenburg */
@Value
@Builder
@Jacksonized
public class NewsSearchResponse {

  String status;
  int page;
  int pageSize;
  int totalHits;
  int totalPages;
  List<NewsArticle> articles;
}
