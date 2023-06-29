package com.study.es;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;

/**
 * @Author: luohx
 * @Description: 创建索引
 * @Date: 2021/7/23 16:32
 */
public class ESTest_Index_Create {
    public static void main(String[] args) throws Exception {
        RestHighLevelClient esClient = new RestHighLevelClient(
                RestClient.builder(new HttpHost("49.232.217.148", 9200, "http"))
        );
        CreateIndexRequest request = new CreateIndexRequest("user");
        //配置settings，分片、副本等信息
        request.settings(Settings.builder().put("index.number_of_shards", 3).put("index.number_of_replicas", 2));
        //配置字段类型。字段类型可以通过JSON字符串、Map以及XContentBuilder三种方式构建
        request.mapping("{\"properties\":{\"title\": {\"type\": \"text\"}}}", XContentType.JSON);
        CreateIndexResponse indexResponse = esClient.indices().create(request, RequestOptions.DEFAULT);
        //响应状态
        boolean acknowledged = indexResponse.isAcknowledged();
        System.out.println("索引操作："+ acknowledged);
        esClient.close();
    }
}
