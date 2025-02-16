package org.sotil.kuarkus.demo.domain.exceptions;

public class CustomerException extends RuntimeException {
  private final int statusCode;

  public CustomerException(String message, int statusCode) {
    super(message);
    this.statusCode = statusCode;
  }

  public int getStatusCode() {
    return statusCode;
  }
}
