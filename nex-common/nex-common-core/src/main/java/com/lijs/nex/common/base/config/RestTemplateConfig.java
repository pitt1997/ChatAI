package com.lijs.nex.common.base.config;

import com.lijs.nex.common.base.config.httpclient.HttpClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    private final HttpClientFactory httpClientFactory = new HttpClientFactory();

    /**
     * 支持超时配置的 RestTemplate
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate(new OkHttp3ClientHttpRequestFactory(
                httpClientFactory.createHttpClient(5, 300)));
    }

}
