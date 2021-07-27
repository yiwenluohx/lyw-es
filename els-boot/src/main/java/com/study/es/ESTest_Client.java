package com.study.es;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

/**
 * @Author: luohx
 * @Description: 创建链接
 * @Date: 2021/7/23 16:05
 */
public class ESTest_Client {
    public static void main(String[] args) throws Exception{
        RestHighLevelClient esClient = new RestHighLevelClient(
                RestClient.builder(new HttpHost("49.232.217.148", 9200, "http"))
        );


        esClient.close();
    }
}
