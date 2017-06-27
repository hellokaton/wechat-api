package io.github.biezhi.wechat.robot;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;

import java.util.concurrent.TimeUnit;

/**
 * @author biezhi
 *         18/06/2017
 */
public abstract class BaseTest {

    OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build();

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

}
