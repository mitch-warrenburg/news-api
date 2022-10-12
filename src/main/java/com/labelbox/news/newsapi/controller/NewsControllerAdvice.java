package com.labelbox.news.newsapi.controller;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import com.labelbox.news.newsapi.model.ErrorResponse;
import javax.validation.ConstraintViolationException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ResponseBody
@RestController
@ControllerAdvice
@RestControllerAdvice
public class NewsControllerAdvice extends ResponseEntityExceptionHandler {

  @ResponseStatus(BAD_REQUEST)
  @ExceptionHandler({ConstraintViolationException.class})
  public ErrorResponse handleBadRequest(ConstraintViolationException exception) {
    return ErrorResponse.builder()
        .code(BAD_REQUEST.value())
        .status(BAD_REQUEST.getReasonPhrase())
        .message(ExceptionUtils.getMessage(exception))
        .build();
  }

  @ResponseStatus(INTERNAL_SERVER_ERROR)
  @ExceptionHandler({Exception.class})
  public ErrorResponse handleInternalServerError(Exception exception) {
    return ErrorResponse.builder()
        .code(INTERNAL_SERVER_ERROR.value())
        .status(INTERNAL_SERVER_ERROR.getReasonPhrase())
        .message(ExceptionUtils.getMessage(exception))
        .build();
  }
}
