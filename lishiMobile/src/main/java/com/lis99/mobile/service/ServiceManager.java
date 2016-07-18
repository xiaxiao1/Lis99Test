package com.lis99.mobile.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by zhangjie on 7/7/16.
 */

public final class ServiceManager {

    private final static Map<String, Object> serviceInstances = new HashMap();
//    超时时间
    private static long TIME_OUT = 20;

    private static Retrofit retrofit;
    private static Retrofit retrofit2;
    private static Gson gson;

    static {
        gson = new GsonBuilder()
                .registerTypeAdapterFactory(new ApiTypeAdapterFactory("data"))
                .create();
        retrofit = new Retrofit.Builder()
                .baseUrl("https://apis.lis99.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(getUnsafeOkHttpClient())
                .build();
        retrofit2 = new Retrofit.Builder()
                .baseUrl("http://api.lis99.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }


    private static OkHttpClient getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
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
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            builder.connectTimeout(TIME_OUT, TimeUnit.SECONDS);
            builder.readTimeout(TIME_OUT, TimeUnit.SECONDS);
            builder.writeTimeout(TIME_OUT, TimeUnit.SECONDS);

            OkHttpClient okHttpClient = builder.build();
            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T getHttpApiService(Class<T> clazz) {
        T service;

        if ((service = (T) serviceInstances.get(clazz.getCanonicalName())) != null) {
            return service;
        }

        service = retrofit2.create(clazz);
        serviceInstances.put(clazz.getCanonicalName(), service);
        return service;
    }

    public static <T> T getHttpsApiService(Class<T> clazz) {
        T service;

        if ((service = (T) serviceInstances.get(clazz.getCanonicalName())) != null) {
            return service;
        }

        service = retrofit.create(clazz);
        serviceInstances.put(clazz.getCanonicalName(), service);
        return service;
    }
}
