package com.study.elastic.config;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: luohx
 * @Description:
 * @Date: 2023/6/29 19:04
 */
@Configuration
public class EsConfig {
    public static final RequestOptions COMMON_OPTIONS;

    @Value("${spring.data.elasticsearch.client.reactive.endpoints}")
    private String urls;
    @Value("${spring.data.elasticsearch.client.reactive.username}")
    private String account;
    @Value("${spring.data.elasticsearch.client.reactive.password}")
    private String password;

    static {
        RequestOptions.Builder builder = RequestOptions.DEFAULT.toBuilder();
        COMMON_OPTIONS = builder.build();
    }

    // 注册到IOC中
    @Bean("restHighLevelClient")
    public RestHighLevelClient restHighLevelClient() {
        RestClientBuilder builder = null;
        String ipAddr = null;
        String[] urlArr;
        Integer port = null;
        if (!StringUtils.isBlank(urls)) {
            String[] urlsArr = urls.split(",");
            for (int i = 0; i < urlsArr.length; i++) {
                String url = urlsArr[i];
                urlArr = url.split(":");
                ipAddr = urlArr[0];
                port = (urlArr[1] == null ? 0 : Integer.parseInt(urlArr[1]));
                builder = RestClient.builder(new HttpHost(ipAddr, port, "http"));
            }
        }
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(account, password));
        builder.setHttpClientConfigCallback(f -> f.setDefaultCredentialsProvider(credentialsProvider));
        RestHighLevelClient restClient = new RestHighLevelClient(builder);
        return restClient;
    }
}
