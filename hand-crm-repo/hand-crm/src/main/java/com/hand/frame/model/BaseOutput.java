package com.hand.frame.model;


import com.hand.frame.common.CommonStatus;

import java.io.Serializable;

public class BaseOutput implements Serializable {
    private String message;
    private Integer code;

    public BaseOutput() {
        this(CommonStatus.SUCCESS);
    }

    public BaseOutput(String message) {
        this(CommonStatus.SUCCESS, message);
    }

    public BaseOutput(OutputStatus status) {
        this(status.getCode(), status.getMessage());
    }

    public BaseOutput(OutputStatus status, String message) {
        this(status.getCode(), message);
    }

    public BaseOutput(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return this.code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void ensureSuccess() {
        if (!CommonStatus.SUCCESS.getCode().equals(this.getCode())) {
            //throw new RemoteCallFailException(this.status, this.message);
        }
    }
}
