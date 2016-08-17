package com.lis99.mobile.engine.base;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yy on 16/8/17.
 */
public class MyException extends Exception {

    private String message;
    private int code;

    public static final int TIMEOUT = 1;

    private static Map<Integer, String> map;

    static {
        map = new HashMap<>();
        map.put(TIMEOUT, "连接超时");
    }

    /**
     * Constructs a new {@code Exception} that includes the current stack trace.
     */
    public MyException(String message, int code) {
        this.message = message;
        this.code = code;
    }

    @Override
    public String getMessage()
    {
        if (map.containsKey(code))
        {
            return map.get(code);
        }
        return message;
    }

    public int getCode() {
        return code;
    }
}
