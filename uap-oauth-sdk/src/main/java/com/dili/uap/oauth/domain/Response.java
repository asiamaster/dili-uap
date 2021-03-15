package com.dili.uap.oauth.domain;

public class Response {

    private int code;
    private String message;
    private Object data;

    public Response(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public Response(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public static Response error(String msg) {
        return new Response(500, msg);
    }

    public static Response success(String msg, Object data) {
        return new Response(200, msg, data);
    }
}
