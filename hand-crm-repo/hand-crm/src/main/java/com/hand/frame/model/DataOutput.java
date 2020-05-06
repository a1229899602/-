package com.hand.frame.model;

import com.hand.frame.common.CommonStatus;

public class DataOutput<T> extends BaseOutput {
    private T result;

    public DataOutput() {
        this(CommonStatus.SUCCESS);
    }

    public DataOutput(OutputStatus status) {
        super(status);
        this.result = null;
    }

    public DataOutput(T result) {
        if (result instanceof OutputStatus) {
            this.setCode(((OutputStatus) result).getCode());
            this.setMessage(((OutputStatus) result).getMessage());
        } else {
            this.result = result;
        }

    }

    public DataOutput(String message, T result) {
        super(message);
        this.result = result;
    }

    public DataOutput(OutputStatus status, T result) {
        super(status);
        this.result = result;
    }

    public DataOutput(OutputStatus status, String message, T result) {
        super(status, message);
        this.result = result;
    }

    public DataOutput(Integer status, String message, T result) {
        super(status, message);
        this.result = result;
    }

    public DataOutput(Integer status, String message) {
        super(status, message);
    }

    public T getResult() {
        return this.result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public T safeGetData() {
        this.ensureSuccess();
        return this.result;
    }
}
