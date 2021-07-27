package com.study.es;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetIndexResponse;

/**
 * @Author: luohx
 * @Description: 查看索引
 * @Date: 2021/7/23 17:10
 */
public class ESTest_Index_Search {
    public static void main(String[] args) throws Exception {
        RestHighLevelClient esClient = new RestHighLevelClient(
                RestClient.builder(new HttpHost("49.232.217.148", 9200, "http"))
        );
        GetIndexRequest request = new GetIndexRequest("user");
        GetIndexResponse indexResponse = esClient.indices().get(request, RequestOptions.DEFAULT);
        //响应状态
        System.out.println(indexResponse.getAliases());
        System.out.println(indexResponse.getMappings());
        System.out.println(indexResponse.getSettings());
        esClient.close();
    }
}

