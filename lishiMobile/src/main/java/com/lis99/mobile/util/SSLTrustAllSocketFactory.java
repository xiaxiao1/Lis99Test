package com.lis99.mobile.util;

import org.apache.http.conn.ssl.SSLSocketFactory;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


/**
 * Created by yy on 16/5/19.
 *
 *  继承证书验证， 自定义证书验证验证步骤
 *
 */
public class SSLTrustAllSocketFactory extends SSLSocketFactory {

    private static final String TAG = "SSLTrustAllSocketFactory";
    private SSLContext mCtx;

    public class SSLTrustAllManager implements X509TrustManager {

        public void checkClientTrusted(X509Certificate[] arg0, String arg1)
                throws CertificateException {

            Common.log("ClientTrusted="+arg1);

        }

        public void checkServerTrusted(X509Certificate[] arg0, String arg1)
                throws CertificateException {

            Common.log("ServerTrusted="+arg1);

        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

    }

    public SSLTrustAllSocketFactory(KeyStore truststore)
            throws Throwable {
        super(truststore);
        try {
            mCtx = SSLContext.getInstance("TLS");
            mCtx.init(null, new TrustManager[] { new SSLTrustAllManager() },
                    null);
            setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        } catch (Exception ex) {
        }
    }

    @Override
    public Socket createSocket(Socket socket, String host,
                               int port, boolean autoClose)
            throws IOException, UnknownHostException {
        return mCtx.getSocketFactory().createSocket(socket, host, port, autoClose);
    }

    @Override
    public Socket createSocket() throws IOException {
        return mCtx.getSocketFactory().createSocket();
    }

    public static SSLSocketFactory getSocketFactory() {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);
            SSLSocketFactory factory = new SSLTrustAllSocketFactory(trustStore);
            return factory;
        } catch (Throwable e) {
            Common.log(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

}