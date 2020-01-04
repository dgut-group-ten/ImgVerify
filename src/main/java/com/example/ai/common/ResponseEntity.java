package com.example.ai.common;

import java.io.Serializable;

public class ResponseEntity<T> implements Serializable {
    private String message;
    private T data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public ResponseEntity() {
    }

    public ResponseEntity message(String data) {
        this.setMessage(data);
        return this;
    }

    public ResponseEntity data(T data) {
        this.setData(data);
        return this;
    }
}
