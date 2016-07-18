package com.lis99.mobile.service;

import java.io.IOException;

/**
 * Created by zhangjie on 7/7/16.
 */
public class ApiException extends IOException {
    public final int code;
    public final String msg;

    public ApiException(int code) {
        this.code = code;
        this.msg = null;
    }

    public ApiException(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public String getMessage() {
        return msg;
    }

    public int getCode() {
        return code;
    }
}

