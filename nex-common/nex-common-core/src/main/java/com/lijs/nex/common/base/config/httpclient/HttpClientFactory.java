package com.lijs.nex.common.base.config.httpclient;

import okhttp3.OkHttpClient;

import java.util.concurrent.TimeUnit;

/**
 * 负责创建和提供 OkHttpClient 对象
 *
 * @author ljs
 * @date 2024-11-28
 * @description
 */
public class HttpClientFactory {

    private volatile OkHttpClient httpClient;

    /**
     * 根据超时参数创建 HttpClient
     *
     * @param connectTimeout 连接超时（秒）
     * @param readTimeout    读取超时（秒）
     * @return OkHttpClient 实例
     */
    public OkHttpClient createHttpClient(int connectTimeout, int readTimeout) {
        if (httpClient == null) {
            synchronized (HttpClientFactory.class) {
                if (httpClient == null) {
                    httpClient = OkHttpBuilderProvider.trustAllCertificatesBuilder()
                            .connectTimeout(connectTimeout, TimeUnit.SECONDS)
                            .readTimeout(readTimeout, TimeUnit.SECONDS)
                            .build();
                }
            }
        }
        return httpClient;
    }

}
