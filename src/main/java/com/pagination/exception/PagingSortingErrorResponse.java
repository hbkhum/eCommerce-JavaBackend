package com.pagination.exception;

public class PagingSortingErrorResponse {
    private int errorCode;
    private String message;

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
    public int getErrorCode() {
        return errorCode;
    }

    public void setMessage(String message) {this.message=message; }
    public String getNessage() {
        return message;
    }
}
