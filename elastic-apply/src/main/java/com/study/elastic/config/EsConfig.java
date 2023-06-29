package com.study.es;

import lombok.Data;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;

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
