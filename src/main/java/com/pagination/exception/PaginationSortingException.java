package com.pagination.exception;

public class PaginationSortingException extends RuntimeException {

    private static final long serialVersionUID = -123L;
    private String errorMessage;
    @Override
    public String getMessage() {
        return errorMessage;
    }
    public PaginationSortingException() {
        super();
    }
    public PaginationSortingException(String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
    }
}
