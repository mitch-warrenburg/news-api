package com.labelbox.news.newsapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.labelbox.news.newsapi.enumeration.Country;
import com.labelbox.news.newsapi.enumeration.Language;
import com.labelbox.news.newsapi.enumeration.NewsTopic;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

/** @author Mitch Warrenburg */
@Value
@Builder
@Jacksonized
public class NewsArticle {

  String title;
  String publishedDate;
  String link;
  String cleanUrl;
  String excerpt;
  String summary;
  String rights;
  String authors;
  String media;
  String twitterAccount;
  NewsTopic topic;
  Country country;
  Language language;

  int rank;
  boolean isOpinion;

  @JsonProperty("_id")
  String id;
}
