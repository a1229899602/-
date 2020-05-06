package com.hand.frame.model;

public class OutputStatus {
    private Integer code;
    private String message;

    public OutputStatus(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }
}
