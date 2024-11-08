package com.nhnacademy.ssacthree_shop_api.elastic.exception;

public class DataSyncException extends RuntimeException {
  public DataSyncException(String message, Throwable cause) {
    super(message, cause);
  }
}