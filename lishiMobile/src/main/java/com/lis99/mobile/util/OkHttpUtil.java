package com.lis99.mobile.util;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by yy on 16/8/16.
 */
public class OkHttpUtil {

    private static OkHttpUtil instance;

    private OkHttpClient okHttpClient;

    private OkHttpClient.Builder builder;

    private static final int TIMEOUT = 10;

    public OkHttpUtil() {

            builder = new OkHttpClient.Builder()
                    .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(TIMEOUT, TimeUnit.SECONDS);

        try {
            final TrustManager[] trustAllCerts = new TrustManager[] {
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                    }

                    @Override
                    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                    }

                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new java.security.cert.X509Certificate[]{};
                    }
                }
            };

        // Install the all-trusting trust manager
        SSLContext sslContext = null;
        SSLSocketFactory sslSocketFactory = null;

            sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            sslSocketFactory = sslContext.getSocketFactory();


        builder.sslSocketFactory(sslSocketFactory);

        } catch (Exception e) {
            e.printStackTrace();
        }

        builder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });

//        builder.addInterceptor(new HttpLoggingInterceptor());

        okHttpClient = builder.build();
    }

    public static OkHttpUtil getInstance() {
        if ( instance == null )
        {
            synchronized (OkHttpUtil.class)
            {
                if ( instance == null )
                {
                    instance = new OkHttpUtil();
                }
            }
        }
        return instance;
    }


    private String getHttp (String url ) throws IOException {
        Request.Builder requestB = new Request.Builder().url(url);
        requestB.method("GET", null);
        Request request = requestB.build();
        Call call = okHttpClient.newCall(request);
        Response response = call.execute();
        if ( !response.isSuccessful()) throw new IOException("Not isSuccessful="+response);
//        if ( null != response.cacheResponse() ) return response.cacheResponse().toString();
        return response.body() == null ? "" : response.body().toString();
    }


    private String postHttp (String url, Map<String, Object> map ) throws IOException {
        FormBody.Builder fbuilder = new FormBody.Builder();

        if ( map != null && !map.isEmpty() )
        {
            Set<String> keys = map.keySet();

            for ( String key : keys )
            {
                String value = String.valueOf(map.get(key));
                fbuilder.add(key, value);
            }
        }

        RequestBody requestBody = fbuilder.build();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        Response response = okHttpClient.newCall(request).execute();

        if ( !response.isSuccessful()) throw new IOException("Not isSuccessful="+response);
//        if ( null != response.cacheResponse() ) return response.body().toString();
        return response.body() == null ? "" : response.body().toString();

    }


    public String get (String url ) throws IOException {
        return getHttp(url);
    }

    public String post (String url, Map<String, Object> map ) throws IOException {
        return postHttp(url, map);
    }


















}
