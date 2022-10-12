package com.labelbox.news.newsapi.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

/** @author Mitch Warrenburg */
@Value
@Builder
@Jacksonized
public class ErrorResponse {
  int code;
  String status;

  @Schema(example = "An exception was encountered: SomeException.class")
  String message;
}
