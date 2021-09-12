package com.mobiquity.exception;

public class APIException extends Exception {

  public APIException(String message, Exception e) {
    super(message, e);
    System.out.println(message);
  }

  public APIException(String message) {
    super(message);
    System.out.println(message);
  }
}