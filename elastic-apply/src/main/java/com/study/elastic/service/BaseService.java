package com.study.elastic.service;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.admin.indices.alias.Alias;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.open.OpenIndexRequest;
import org.elasticsearch.action.admin.indices.open.OpenIndexResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.client.indices.CloseIndexRequest;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.collapse.CollapseBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Pageable;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

/**
 * es基础抽象操作service
 *
 * @author luohx
 * @version 1.0.0
 * @date: 2023/6/30 下午1:55
 * @menu es基础抽象操作service
 */
public abstract class BaseService<T> implements IBaseService<T> {

    private Logger logger = LoggerFactory.getLogger(BaseService.class);

    @Resource
    RestHighLevelClient restLevelClient;

    @Override
    public void createIndex(String index, Long max) {
        // 设置setting (setting定义可参照 IndexMetadata 类)
        CreateIndexRequest request = new CreateIndexRequest(index).settings(Settings.builder()
                // 分片数
                .put("index.number_of_shards", 10)
                // 副本数
                .put("index.number_of_replicas", 1)
                // 设置单次查询最大量, 默认10000
                .put("index.max_result_window", max == 0 ? 10000 : max)
                // 将数据路由到一组shard上面，设置index.routing_partition_size，默认值是1，即只路由到1个shard
                // 可以将其设置为大于1且小于索引shard总数的某个值，就可以路由到一组shard了。值越大，数据越均匀
                .put("index.routing_partition_size", 2)
        );
        try {
            // 设置mapping参数
            request.mapping(XContentFactory.jsonBuilder()
                    // 强制设置当前索引的(增删改查)操作时, 必须添加路由参数, 否则报错
                    // 根据业务场景可以选择注释掉
                    .startObject()
                    .startObject("_routing")
                    .field("required", true)
                    .endObject()
                    .endObject());
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 设置别名并设置创建索引超时时长2分钟
        request.alias(new Alias(index + "_alias")).setTimeout(TimeValue.timeValueMinutes(2));
        // 同步请求
        try {
            CreateIndexResponse createIndexResponse = restLevelClient.indices().create(request, RequestOptions.DEFAULT);
            // 处理响应
            boolean acknowledged = createIndexResponse.isAcknowledged();
            boolean shardsAcknowledged = createIndexResponse.isShardsAcknowledged();
            logger.info(" = = = >>> 索引库{}创建 : {}", index, acknowledged + "-" + shardsAcknowledged);
        } catch (IOException e) {
            logger.info(" = = = >>> 索引{}创建异常:" + e.getMessage(), index);
        }
    }

    @Override
    public boolean deleteIndex(String index) {
        if (!isExistIndex(index)) return false;
        AcknowledgedResponse delete = null;
        try {
            delete = restLevelClient.indices().delete(new DeleteIndexRequest(index), RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        boolean acknowledged = delete.isAcknowledged();
        logger.info(" = = = >>> 删除索引库-{} : {}", index, acknowledged);
        return acknowledged;
    }

    @Override
    public boolean isExistIndex(String index) {
        //从主节点返回本地索引信息状态;以适合人类的格式返回;是否返回每个索引的所有默认配置
        GetIndexRequest request = new GetIndexRequest(index).local(false).humanReadable(true).includeDefaults(false);
        boolean exists = false;
        try {
            exists = restLevelClient.indices().exists(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info(" = = = >>> 判断索引库 {} 是否存在 : {}", index, exists);
        return exists;
    }

    @Override
    public void closeIndex(String index) {
        AcknowledgedResponse close = null;
        try {
            close = restLevelClient.indices().close(new CloseIndexRequest(index), RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info(" = = = >>> 关闭索引库-{} : {}", index, close.isAcknowledged());
    }

    @Override
    public void openIndex(String index) {
        OpenIndexResponse open = null;
        try {
            open = restLevelClient.indices().open(new OpenIndexRequest(index), RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info(" = = = >>> 开启索引库-{} : {}", index, open.isAcknowledged());
    }

    @Override
    public Long searchCount(String index, BoolQueryBuilder boolQueryBuilder) {
        long start = System.currentTimeMillis();
        logger.info(" = = = >>> 查询索引库 {} 总条数", index);
        CountRequest countRequest = new CountRequest().indices(index);
        if (boolQueryBuilder != null) {
            countRequest.query(boolQueryBuilder);
        }
        CountResponse count = null;
        try {
            count = restLevelClient.count(countRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info(" = = = >>> 索引库 {} 的总条数: {} , 耗时: {}/秒", index,
                count.getCount(), (System.currentTimeMillis() - start) / 1000);
        return count.getCount();
    }

    @Override
    public List<T> search(String index, Pageable pageable, String routing, Class<T> clazz, BoolQueryBuilder boolQueryBuilder,
                          String[] showField, String[] shieldField, String orderField, boolean bl) {
        //查询开始时间
        long start_time = System.currentTimeMillis();
        logger.info(" = = = >>> 索引库 {} 条件查询", index);
        List<T> list = new ArrayList<>();
        //创建指定索引库请求
        SearchRequest request = new SearchRequest().indices(index);
        //指定路由查询数据
        if (StringUtils.isNotBlank(routing)) {
            request.routing(routing);
        }
        SearchSourceBuilder ssbuilder = new SearchSourceBuilder();
        if (pageable != null && !pageable.isUnpaged()) {
            // 开始页数
            Integer start = (pageable.getPageNumber() == 0 ? 1 : pageable.getPageNumber()) - 1;
            // 这里要自己计算出起始页是第多少条，from是指从多少条开始截取(包含第from这条)
            int from = start * pageable.getPageSize();
            // 查全部数据
            ssbuilder.from(from).size(pageable.getPageSize())
                    // 如果不写或者写false时, 当总记录数超过10000时会返回总数10000
                    // 配置为true就会返回真实条数(前提是真实条数小于创建索引时的最大值, 否则返回创建索引时的最大值)
                    .trackTotalHits(true);
        } else {
            ssbuilder.size(20000).trackTotalHits(true);
        }
        //排序
        if (StringUtils.isNotEmpty(orderField)) {
            ssbuilder.sort(orderField, bl ? SortOrder.ASC : SortOrder.DESC);
        }
        // 指定查询字段和排除字段, 并添加判断条件
        ssbuilder.fetchSource(showField, shieldField).query(boolQueryBuilder);
        // 请求的查询json
        request.source(ssbuilder);
        // 开始查询数据
        SearchResponse response = null;
        try {
            // 获取查询的返回值
            response = restLevelClient.search(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 返回值
        SearchHit[] searchHits = response.getHits().getHits();
        for (SearchHit hit : searchHits) {
            // 获取每个JSON对象字符串
            String jsonStr = hit.getSourceAsString();
            list.add(JSONObject.parseObject(jsonStr, clazz));
        }
        logger.info(" = = = >>> 索引库 {} 条件查询的条数: {} , 耗时: {}/秒", index,
                list.size(), (System.currentTimeMillis() - start_time) / 1000);
        return list;
    }

    @Override
    public List<T> searchByGroup(String index, String routing, Class<T> clazz, BoolQueryBuilder boolQueryBuilder,
                                 String[] showField, String[] shieldField, String groupField, String sortField,
                                 boolean bl) {
        //查询开始时间
        long start_time = System.currentTimeMillis();
        logger.info(" = = = >>> 索引库 {} 条件查询", index);
        List<T> list = new ArrayList<>();
        //创建指定索引库请求
        SearchRequest request = new SearchRequest().indices(index);
        // 指定路由查询数据
        if (StringUtils.isNotBlank(routing)) {
            request.routing(routing);
        }
        // 创建请求参数，指定查询字段和排除字段，添加判断条件
        SearchSourceBuilder ssb = new SearchSourceBuilder().fetchSource(showField, shieldField).query(boolQueryBuilder);
        // 分组排序
        TermsAggregationBuilder partJobAggBuilder = AggregationBuilders.terms("terms")
                .field(groupField + ".keyword");
        partJobAggBuilder.subAggregation(bl ? AggregationBuilders.min("min").field(sortField) : AggregationBuilders.max("max").field(sortField));
        ssb.aggregation(partJobAggBuilder);
        // 请求的查询json
        request.source(ssb);
        // 开始查询数据
        SearchResponse response = null;
        try {
            response = restLevelClient.search(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 返回值
        SearchHit[] searchHits = response.getHits().getHits();
        for (SearchHit hit : searchHits) {
            // 获取每个JSON对象字符串
            String jsonStr = hit.getSourceAsString();
            list.add(JSONObject.parseObject(jsonStr, clazz));
        }
        logger.info(" = = = >>> 索引库 {} 分组排序查询的条数: {} , 耗时: {}/秒", index,
                list.size(), (System.currentTimeMillis() - start_time) / 1000);
        return list;
    }

    @Override
    public JSONObject searchByGroupNum(String index, BoolQueryBuilder boolQueryBuilder, String groupField) {
        //查询开始时间
        long start_time = System.currentTimeMillis();
        logger.info(" = = = >>> 索引库 {} 条件查询", index);
        //创建指定索引库请求
        SearchRequest request = new SearchRequest().indices(index);
        // 创建请求参数,添加判断条件
        SearchSourceBuilder ssb = new SearchSourceBuilder().query(boolQueryBuilder);
        //根据单个字段进行分组统计，统计出的列别名叫sum
        ssb.aggregation(AggregationBuilders.terms("sum").field(groupField + ".keyword"));
        // 请求的查询json
        request.source(ssb);
        // 开始查询数据
        SearchResponse response = null;
        try {
            response = restLevelClient.search(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 得到这个分组的数据集合
        Terms terms = response.getAggregations().get("sum");
        JSONObject json = new JSONObject();
        for (int i = 0; i < terms.getBuckets().size(); i++) {
            //分组后的值
            String key = terms.getBuckets().get(i).getKey().toString();
            // 值的数量
            Long sum = terms.getBuckets().get(i).getDocCount();
            json.put(key, sum);
            logger.info(" = = = >>> key:{} count:{}", terms.getBuckets().get(i).getKey(), terms.getBuckets().get(i).getDocCount());
        }
        logger.info(" = = = >>> 索引库 {} 分组计数查询的条数: {} , 耗时: {}/秒", index,
                json.size(), (System.currentTimeMillis() - start_time) / 1000);
        return json;
    }

    @Override
    public List<T> searchByDistinct(String index, Class<T> clazz, BoolQueryBuilder boolQueryBuilder,
                                    String[] showField, String[] shieldField, String distinctField) {
        //查询开始时间
        long start_time = System.currentTimeMillis();
        logger.info(" = = = >>> 索引库 {} 条件查询", index);
        List<T> list = new ArrayList<>();
        //1.创建指定索引库请求
        SearchRequest request = new SearchRequest().indices(index);
        // 创建请求参数
        SearchSourceBuilder ssb = new SearchSourceBuilder();
        // 指定查询字段和排除字段,添加判断条件,去重
        ssb.fetchSource(showField, shieldField).query(boolQueryBuilder).collapse(new CollapseBuilder(distinctField)).trackTotalHits(true);
        // 请求的查询json
        request.source(ssb);
        // 开始查询数据
        SearchResponse response = null;
        try {
            response = restLevelClient.search(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 返回值
        SearchHit[] searchHits = response.getHits().getHits();
        for (SearchHit hit : searchHits) {
            // 获取每个JSON对象字符串
            list.add(JSONObject.parseObject(hit.getSourceAsString(), clazz));
        }
        logger.info(" = = = >>> 索引库 {} 去重查询的条数: {} , 耗时: {}/秒", index,
                list.size(), (System.currentTimeMillis() - start_time) / 1000);
        return list;
    }

    @Transient
    @Override
    public void saveByObject(T t, String docId, String index, String routing) {
        //查询开始时间
        long start = System.currentTimeMillis();
        logger.info(" = = = >>> 索引库 {} 数据存储", index);
        // 当ID重复时, 新数据就会覆盖掉旧数据, 也就是修改
        // 设置ID主键
        IndexRequest request = new IndexRequest(index).id(docId);
        // 指定路由保存数据
        if (StringUtils.isNotBlank(routing)) {
            request.routing(routing);
        }
        // 传入JSON字符串
        request.source(JSONObject.toJSONString(t), XContentType.JSON);
        try {
            restLevelClient.index(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info(" = = = >>> 索引库 {} 存储耗时: {}/秒", index,
                (System.currentTimeMillis() - start) / 1000);
    }

    @Transient
    @Override
    public void save(Map<String, Object> map, String index, String routing) {
        //查询开始时间
        long start = System.currentTimeMillis();
        logger.info(" = = = >>> 索引库 {} 数据存储", index);
        // 当ID重复时, 新数据就会覆盖掉旧数据, 也就是修改
        IndexRequest request = new IndexRequest(index).id(map.get("id").toString());
        // 指定路由保存数据
        if (StringUtils.isNotBlank(routing)) {
            request.routing(routing);
        }
        // 传入JSON字符串
        request.source(JSONObject.toJSONString(map), XContentType.JSON);
        try {
            restLevelClient.index(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info(" = = = >>> 索引库 {} 存储耗时: {}/秒", index,
                (System.currentTimeMillis() - start) / 1000);
    }

    @Override
    public Long deleteAllByIndices(String indices) {
        long start = System.currentTimeMillis();
        logger.info(" = = = >>> 索引库 {} 删除全部数据", indices);
        DeleteByQueryRequest request = new DeleteByQueryRequest(indices);
        // 异常继续执行
        request.setConflicts("proceed");
        request.setQuery(new BoolQueryBuilder().filter(QueryBuilders.boolQuery()));
        BulkByScrollResponse response = null;
        try {
            response = restLevelClient.deleteByQuery(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info(" = = = >>> 索引库 {} 删除全部数据, 条数: {}, 耗时: {}/秒", indices,
                response.getDeleted(), (System.currentTimeMillis() - start) / 1000);
        return response.getDeleted();
    }

    @Override
    public Long deleteByCondition(String indices, String routing, BoolQueryBuilder boolQueryBuilder) {
        long start = System.currentTimeMillis();
        logger.info(" = = = >>> 索引库 {} 条件删除", indices);
        DeleteByQueryRequest request = new DeleteByQueryRequest(indices);
        if (StringUtils.isNotBlank(routing)) {
            request.setRouting(routing);
        }
        // 异常继续执行
        request.setConflicts("proceed");
        request.setQuery(boolQueryBuilder);
        long deleted = 0L;
        try {
            BulkByScrollResponse response = restLevelClient.deleteByQuery(request, RequestOptions.DEFAULT);
            deleted = response.getDeleted();
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info(" = = = >>> 索引库 {} 条件删除, 条数: {}, 耗时: {}/秒", indices,
                deleted, (System.currentTimeMillis() - start) / 1000);
        return deleted;
    }

    /**
     * 时间+8小时
     *
     * @param date 时间
     */
    public Date getUTCTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR, 8);
        return cal.getTime();
    }
}
