package io.github.biezhi.wechat.utils;

import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;

import javax.net.ssl.*;

/**
 * OkHttp SSL 工具类
 */
@Slf4j
public class OkHttpUtils {

    /**
     * Setting testMode configuration. If set as testMode, the connection will skip certification check
     *
     * @param builder
     * @return
     */
    public static OkHttpClient.Builder configureToIgnoreCertificate(OkHttpClient.Builder builder) {
        log.warn("Ignore Ssl Certificate");
        try {
            /*Create a trust manager that does not validate certificate chains*/
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            /*Install the all-trusting trust manager*/
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            /*Create an ssl socket factory with our all-trusting manager*/
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
        } catch (Exception e) {
            log.warn("Exception while configuring IgnoreSslCertificate" + e, e);
        }
        return builder;
    }

}