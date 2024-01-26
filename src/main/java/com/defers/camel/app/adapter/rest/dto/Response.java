package com.defers.camel.app.adapter.rest.dto;

public class Response {
    private String message;
    private String error;

    public Response() {}

    public Response(String message, String error) {
        this.message = message;
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "Response{" + "message='" + message + '\'' + ", error='" + error + '\'' + '}';
    }
}
