package com.lis99.mobile.service;

import com.lis99.mobile.util.Common;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by yy on 16/7/28.
 */
public class HttpLoggingInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        long t1 = System.nanoTime();
        Response response = chain.proceed(request);
        long t2 = System.nanoTime();

        double time = (t2 - t1) / 1e6d;

        if (request.method().equals("GET")) {
            Common.log(String.format("GET ", request.url(), time, request.headers(), response.code(), response.headers(), response.body().charStream()));
        } else if (request.method().equals("POST")) {
            Common.log(String.format("POST ", request.url(), time, request.headers(), request.body(), response.code(), response.headers(), response.body().charStream()));
        } else if (request.method().equals("PUT")) {
            Common.log(String.format("PUT " , request.url(), time, request.headers(), request.body().toString(), response.code(), response.headers(), response.body().charStream()));
        } else if (request.method().equals("DELETE")) {
            Common.log(String.format("DELETE " , request.url(), time, request.headers(), response.code(), response.headers()));
        }

        return response;
    }
}
