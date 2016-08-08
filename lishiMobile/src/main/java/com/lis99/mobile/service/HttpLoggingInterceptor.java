package com.lis99.mobile.service;

import com.lis99.mobile.util.Common;

import java.io.IOException;
import java.util.Locale;

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
        okhttp3.Response response = chain.proceed(chain.request());
        long t2 = System.nanoTime();
        Common.log(String.format(Locale.getDefault(), "Received response for %s in %.1fms",
                response.request().url(), (t2 - t1) / 1e6d));
        okhttp3.MediaType mediaType = response.body().contentType();
        String content = response.body().string();
        Common.log("request:" + request.toString());
//        Common.log("request:" + request.body().toString());
        Common.log("response body:" + content);
        return response.newBuilder()
                .body(okhttp3.ResponseBody.create(mediaType, content))
                .build();
    }
}
