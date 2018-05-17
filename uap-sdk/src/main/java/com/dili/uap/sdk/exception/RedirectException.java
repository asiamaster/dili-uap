package com.dili.uap.sdk.exception;

/**
 * Created by Administrator on 2016/10/19.
 */
public class RedirectException extends RuntimeException {
    public String path;

    public RedirectException(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
