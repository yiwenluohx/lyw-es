package com.study.elastic.utils;

import com.alibaba.fastjson.JSONObject;
import com.study.elastic.entity.GeneSnpData;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.admin.indices.alias.Alias;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.open.OpenIndexRequest;
import org.elasticsearch.action.admin.indices.open.OpenIndexResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
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
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

/**
 * elasticsearch工具类
 *
 * @author luohx
 * @version 1.0.0
 * @date: 2023/6/29 下午5:55
 * @menu elasticsearch工具类
 */
@Component
public class ElasticsearchUtils {
    // 日志输出
    private Logger logger = LoggerFactory.getLogger(ElasticsearchUtils.class);

    @Resource
    RestHighLevelClient restHighLevelClient;

    /**
     * 创建索引库
     *
     * @param index 索引库名称
     * @param max   单次查询最大量
     */
    public void createIndex(String index, Long max) {
        CreateIndexRequest request = new CreateIndexRequest(index);
        // 设置setting (setting定义可参照 IndexMetadata 类)
        request.settings(Settings.builder()
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
        // 设置别名
        request.alias(new Alias(index + "_alias"));
        // 设置创建索引超时时长2分钟
        request.setTimeout(TimeValue.timeValueMinutes(2));
        // 同步请求
        try {
            CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
            // 处理响应
            boolean acknowledged = createIndexResponse.isAcknowledged();
            boolean shardsAcknowledged = createIndexResponse.isShardsAcknowledged();
            logger.info(" = = = >>> 索引库{}创建 : {}", index, acknowledged + "-" + shardsAcknowledged);
        } catch (IOException e) {
            logger.info(" = = = >>> 索引{}创建异常:" + e.getMessage(), index);
        }
    }

    /**
     * 删除索引库
     */
    public boolean deleteIndex(String index) {
        boolean acknowledged = isExistIndex(index);
        if (acknowledged) {
            // 创建删除索引请求
            DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(index);
            // 执行
            AcknowledgedResponse delete = null;
            try {
                delete = restHighLevelClient.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // 得到相应
            acknowledged = delete.isAcknowledged();
        }
        logger.info(" = = = >>> 删除索引库-{} : {}", index, acknowledged);
        return acknowledged;
    }

    /**
     * 判断索引库是否存在
     */
    public boolean isExistIndex(String index) {
        GetIndexRequest request = new GetIndexRequest(index);
        //参数
        //从主节点返回本地索引信息状态
        request.local(false);
        //以适合人类的格式返回
        request.humanReadable(true);
        //是否返回每个索引的所有默认配置
        request.includeDefaults(false);
        boolean exists = false;
        try {
            exists = restHighLevelClient.indices().exists(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info(" = = = >>> 判断索引库 {} 是否存在 : {}", index, exists);
        return exists;
    }

    /**
     * 关闭索引
     */
    public void closeIndex(String index) {
        CloseIndexRequest request = new CloseIndexRequest(index);
        AcknowledgedResponse close = null;
        try {
            close = restHighLevelClient.indices().close(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        boolean acknowledged = close.isAcknowledged();
        logger.info(" = = = >>> 关闭索引库-{} : {}", index, acknowledged);
    }

    /**
     * 开启索引
     */
    public void testOpenIndex(String index) {
        OpenIndexRequest request = new OpenIndexRequest(index);
        OpenIndexResponse open = null;
        try {
            open = restHighLevelClient.indices().open(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        boolean acknowledged = open.isAcknowledged();
        logger.info(" = = = >>> 开启索引库-{} : {}", index, acknowledged);
    }

    /**
     * 查询索引库条数
     *
     * @param index            索引库名称
     * @param boolQueryBuilder 条件对象
     */
    public Long searchCount(String index, BoolQueryBuilder boolQueryBuilder) {
        long start = System.currentTimeMillis();
        logger.info(" = = = >>> 查询索引库 {} 总条数", index);
        CountRequest countRequest = new CountRequest();
        countRequest.indices(index);
        if (boolQueryBuilder != null) {
            countRequest.query(boolQueryBuilder);
        }
        CountResponse count = null;
        try {
            count = restHighLevelClient.count(countRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info(" = = = >>> 索引库 {} 的总条数: {} , 耗时: {}/秒", index,
                count.getCount(), (System.currentTimeMillis() - start) / 1000);
        return count.getCount();
    }

    /**
     * 查询数据
     *
     * @param index            索引库名称
     * @param pageable         分页对象
     * @param routing          路由
     * @param clazz            转换对象类型
     * @param boolQueryBuilder 条件对象
     * @param showField        指定查询字段 可传空数组
     * @param shieldField      指定排除字段 可传空数组
     * @param orderField       排序字段
     * @param bl               true.正序 false.倒序
     * @param <T>
     * @return
     */
    public <T> List searchData(String index, Pageable pageable, String routing, Class<T> clazz, BoolQueryBuilder boolQueryBuilder,
                               String[] showField, String[] shieldField, String orderField, boolean bl) {
        long start_time = System.currentTimeMillis();
        logger.info(" = = = >>> 索引库 {} 条件查询", index);
        List<T> list = new ArrayList<>();
        //1.创建请求
        SearchRequest request = new SearchRequest();
        // 指定索引库
        request.indices(index);
        // 指定路由查询数据
        if (routing != null && routing != "") {
            request.routing(routing);
        }
        // 创建请求参数
        SearchSourceBuilder ssb = new SearchSourceBuilder();
        if (pageable != null && !pageable.isUnpaged()) {
            // 开始页数
            Integer start = (pageable.getPageNumber() == 0 ? 1 : pageable.getPageNumber()) - 1;
            // 这里要自己计算出起始页是第多少条，from是指从多少条开始截取(包含第from这条)
            int from = start * pageable.getPageSize();
            // 查全部数据
            ssb.from(from).size(pageable.getPageSize())
                    // 如果不写或者写false时, 当总记录数超过10000时会返回总数10000
                    // 配置为true就会返回真实条数(前提是真实条数小于创建索引时的最大值, 否则返回创建索引时的最大值)
                    .trackTotalHits(true);
        } else {
            ssb.size(20000).trackTotalHits(true);
        }
        //排序
        if (StringUtils.isNotEmpty(orderField)) {
            if (bl) {
                // 正序
                ssb.sort(orderField, SortOrder.ASC);
            } else {
                // 倒序
                ssb.sort(orderField, SortOrder.DESC);
            }
        }
        // 指定查询字段和排除字段
        ssb.fetchSource(showField, shieldField);
        // 添加判断条件
        ssb.query(boolQueryBuilder);
        // 请求的查询json
        request.source(ssb);
        // 开始查询数据
        SearchResponse response = null;
        try {
            // 获取查询的返回值
            response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
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

    /**
     * 分组排序
     *
     * @param index            索引库名称
     * @param routing          路由
     * @param clazz            转换对象类型
     * @param boolQueryBuilder 条件对象
     * @param showField        指定查询字段 可传空数组
     * @param shieldField      指定查询字段 可传空数组
     * @param groupField       分组字段 字段类型必须是字符串
     * @param sortField        排序字段
     * @param bl               排序规则 true.ack   false.desc
     * @param <T>
     * @return
     */
    public <T> List searchDataByGroup(String index, String routing, Class<T> clazz, BoolQueryBuilder boolQueryBuilder,
                                      String[] showField, String[] shieldField, String groupField, String sortField,
                                      boolean bl) {
        long start_time = System.currentTimeMillis();
        logger.info(" = = = >>> 索引库 {} 条件查询", index);
        List<T> list = new ArrayList<>();
        //1.创建请求
        SearchRequest request = new SearchRequest();
        // 指定索引库
        request.indices(index);
        // 指定路由查询数据
        if (routing != null && routing != "") {
            request.routing(routing);
        }
        // 创建请求参数
        SearchSourceBuilder ssb = new SearchSourceBuilder();
        // 指定查询字段和排除字段
        ssb.fetchSource(showField, shieldField);
        // 添加判断条件
        ssb.query(boolQueryBuilder);
        // 分组排序
        TermsAggregationBuilder partJobAggBuilder = AggregationBuilders.terms("terms")
                .field(groupField + ".keyword");
        if (bl) {
            // 从小到大
            partJobAggBuilder.subAggregation(AggregationBuilders.min("min").field(sortField));
        } else {
            // 从大到小
            partJobAggBuilder.subAggregation(AggregationBuilders.max("max").field(sortField));
        }
        ssb.aggregation(partJobAggBuilder);
        // 请求的查询json
        request.source(ssb);
        // 开始查询数据
        SearchResponse response = null;
        try {
            response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
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

    /**
     * 分组计数
     *
     * @param index            索引库名称
     * @param boolQueryBuilder 条件对象
     * @param groupField       分组字段 字段类型必须是字符串
     * @return
     */
    public JSONObject searchDataByGroupNum(String index, BoolQueryBuilder boolQueryBuilder, String groupField) {
        long start_time = System.currentTimeMillis();
        logger.info(" = = = >>> 索引库 {} 条件查询", index);
        //1.创建请求
        SearchRequest request = new SearchRequest();
        // 指定索引库
        request.indices(index);
        // 创建请求参数
        SearchSourceBuilder ssb = new SearchSourceBuilder();
        // 添加判断条件
        ssb.query(boolQueryBuilder);
        //根据单个字段进行分组统计，统计出的列别名叫sum
        TermsAggregationBuilder termsBuilder = AggregationBuilders.terms("sum").field(groupField + ".keyword");
        ssb.aggregation(termsBuilder);
        // 请求的查询json
        request.source(ssb);
        // 开始查询数据
        SearchResponse response = null;
        try {
            response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
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

    /**
     * 数据去重
     *
     * @param index            索引库的名称
     * @param clazz            转换对象类型
     * @param boolQueryBuilder 条件对象
     * @param showField        指定查询字段 可传空数组
     * @param shieldField      指定查询字段 可传空数组
     * @param distinctField    去重字段
     * @param <T>
     * @return
     */
    public <T> List searchDataByDistinct(String index, Class<T> clazz, BoolQueryBuilder boolQueryBuilder,
                                         String[] showField, String[] shieldField, String distinctField) {
        long start_time = System.currentTimeMillis();
        logger.info(" = = = >>> 索引库 {} 条件查询", index);
        List<T> list = new ArrayList<>();
        //1.创建请求
        SearchRequest request = new SearchRequest();
        // 指定索引库
        request.indices(index);
        // 创建请求参数
        SearchSourceBuilder ssb = new SearchSourceBuilder();
        // 指定查询字段和排除字段
        ssb.fetchSource(showField, shieldField);
        // 添加判断条件
        ssb.query(boolQueryBuilder);
        // 去重
        ssb.collapse(new CollapseBuilder(distinctField)).trackTotalHits(true);
        // 请求的查询json
        request.source(ssb);
        // 开始查询数据
        SearchResponse response = null;
        try {
            response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
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
        logger.info(" = = = >>> 索引库 {} 去重查询的条数: {} , 耗时: {}/秒", index,
                list.size(), (System.currentTimeMillis() - start_time) / 1000);
        return list;
    }

    /**
     * 保存、修改数据（固定字段对象）
     *
     * @param fileEntity 实体对象
     * @param index      索引库名称
     * @param routing    路由
     * @throws IOException
     */
    @Transient
    public void saveDataByObject(GeneSnpData fileEntity, String index, String routing) {
        long start = System.currentTimeMillis();
        logger.info(" = = = >>> 索引库 {} 数据存储", index);
        // 当ID重复时, 新数据就会覆盖掉旧数据, 也就是修改
        // 设置ID主键
        IndexRequest request = new IndexRequest(index).id(fileEntity.getId().toString());
        // 指定路由保存数据
        if (routing != null && routing != "") {
            request.routing(routing);
        }
        // 传入JSON字符串
        request.source(JSONObject.toJSONString(fileEntity), XContentType.JSON);
        IndexResponse response = null;
        try {
            response = restHighLevelClient.index(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info(" = = = >>> 索引库 {} 存储耗时: {}/秒", index,
                (System.currentTimeMillis() - start) / 1000);
    }

    /**
     * 保存、修改数据（不固定字段对象）
     *
     * @param map     数据 ( id:必填字段 )
     * @param index   索引库名称
     * @param routing 路由
     * @throws IOException
     */
    @Transient
    public void saveData(Map<String, Object> map, String index, String routing) {
        long start = System.currentTimeMillis();
        logger.info(" = = = >>> 索引库 {} 数据存储", index);
        // 当ID重复时, 新数据就会覆盖掉旧数据, 也就是修改
        // 设置ID主键
        IndexRequest request = new IndexRequest(index).id(map.get("id").toString());
        // 指定路由保存数据
        if (routing != null && routing != "") {
            request.routing(routing);
        }
        // 传入JSON字符串
        request.source(JSONObject.toJSONString(map), XContentType.JSON);
        IndexResponse response = null;
        try {
            response = restHighLevelClient.index(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info(" = = = >>> 索引库 {} 存储耗时: {}/秒", index,
                (System.currentTimeMillis() - start) / 1000);
    }

    /**
     * 删除索引库内所有数据
     *
     * @param indices 索引库名称
     * @return
     * @throws IOException
     */
    public Long deleteAllDataByIndices(String indices) {
        long start = System.currentTimeMillis();
        logger.info(" = = = >>> 索引库 {} 删除全部数据", indices);
        DeleteByQueryRequest request = new DeleteByQueryRequest(indices);
        // 异常继续执行
        request.setConflicts("proceed");
        request.setQuery(new BoolQueryBuilder().filter(QueryBuilders.boolQuery()));
        BulkByScrollResponse response = null;
        try {
            response = restHighLevelClient.deleteByQuery(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info(" = = = >>> 索引库 {} 删除全部数据, 条数: {}, 耗时: {}/秒", indices,
                response.getDeleted(), (System.currentTimeMillis() - start) / 1000);
        return response.getDeleted();
    }

    /**
     * 条件删除数据
     *
     * @param indices          索引库名称
     * @param routing          路由
     * @param boolQueryBuilder 条件对象
     * @return
     * @throws IOException
     */
    public Long deleteDataByCondition(String indices, String routing, BoolQueryBuilder boolQueryBuilder) {
        long start = System.currentTimeMillis();
        logger.info(" = = = >>> 索引库 {} 条件删除", indices);
        long deleted = 0L;
        DeleteByQueryRequest request = new DeleteByQueryRequest(indices);
        if (routing != null && routing != "") {
            request.setRouting(routing);
        }
        // 异常继续执行
        request.setConflicts("proceed");
        request.setQuery(boolQueryBuilder);
        try {
            BulkByScrollResponse response = restHighLevelClient.deleteByQuery(request, RequestOptions.DEFAULT);
            deleted = response.getDeleted();
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info(" = = = >>> 索引库 {} 条件删除, 条数: {}, 耗时: {}/秒", indices,
                deleted, (System.currentTimeMillis() - start) / 1000);
        return deleted;
    }

    /**
     * 自定义判断条件（GeneSnpData 固定字段对象）
     *
     * @return
     */
    public BoolQueryBuilder judgeConditionByGeneSnpData(GeneSnpData fileEntity) {
        logger.info(" = = = >>> 拼接 gene_snp_data 查询条件");
        // 查询条件对象
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        /**
         * ID
         */
        if (StringUtils.isNotBlank(fileEntity.getId())) {
            boolQueryBuilder.filter(QueryBuilders.matchQuery("id.keyword", fileEntity.getId()));
        }
        /**
         * 文件ID
         */
        if (StringUtils.isNotBlank(fileEntity.getFile_id())) {
            boolQueryBuilder.filter(QueryBuilders.matchQuery("file_id.keyword", fileEntity.getFile_id()));
        }
//        /**
//         * 所属染色体 模糊查询
//         */
//        if (StringUtils.isNotBlank(geneContrastResult.getChr_ind())) {
//            boolQueryBuilder.filter(QueryBuilders.wildcardQuery("chr_ind.keyword", "*" + geneContrastResult.getChr_ind() + "*"));
//        }
//        /**
//         * 位置
//         */
//        if (fileEntity.getChr_position() != null) {
//            boolQueryBuilder.filter(QueryBuilders.matchQuery("position", fileEntity.getPosition()));
//        }
        /**
         * 位置 范围查询条件
         */
        if (fileEntity.getBeginPosition() != null && fileEntity.getEndPosition() != null) {
            boolQueryBuilder.filter(QueryBuilders
                    .rangeQuery("position")
                    // 大于等于 开始位置
                    .gte(fileEntity.getBeginPosition())
                    // 小于等于 结束位置
                    .lte(fileEntity.getEndPosition()));
        }
        /**
         * 创建时间（时间范围检索）
         */
        if (fileEntity.getStartTime() != null
                && fileEntity.getEndTime() != null) {
            boolQueryBuilder.filter(QueryBuilders
                    //传入时间，格式 2020-01-02T03:17:37.638Z
                    .rangeQuery("create_time")
                    //因为是FieldType.Date修饰的时间会转为UTC(-8小时), 所以需要+8小时 getUTCTime()函数
                    // 大于等于 开始时间
                    .from(getUTCTime(fileEntity.getStartTime()))
                    // 小于等于 结束时间
                    .to(getUTCTime(fileEntity.getEndTime())));
        }
        logger.info(" = = = >>> gene_snp_data 查询条件: {}", boolQueryBuilder.toString()
                .replace(" ", "").replace("\n", ""));
        return boolQueryBuilder;
    }

    /**
     * 自定义判断条件（不固定字段对象）
     *
     * @return
     */
    public BoolQueryBuilder judgeConditionByMap(Map<String, Object> map) {
        logger.info(" = = = >>> 拼接 map 查询条件");
        // 查询条件对象
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        /**
         * ID
         */
        if (map.get("id") != null && StringUtils.isNotBlank(map.get("id").toString())) {
            boolQueryBuilder.filter(QueryBuilders.matchQuery("id.keyword", map.get("id").toString()));
        }
        /**
         * 文件ID
         */
        if (map.get("file_id") != null && StringUtils.isNotBlank(map.get("file_id").toString())) {
            boolQueryBuilder.filter(QueryBuilders.matchQuery("file_id.keyword", map.get("file_id").toString()));
        }
//        /**
//         * 所属染色体 模糊查询
//         */
//        if (StringUtils.isNotBlank(geneContrastResult.getChr_ind())) {
//            boolQueryBuilder.filter(QueryBuilders.wildcardQuery("chr_ind.keyword", "*" + geneContrastResult.getChr_ind() + "*"));
//        }
        /**
         * 创建时间（时间范围检索）
         */
        if (map.get("startTime") != null
                && map.get("endTime") != null) {
            boolQueryBuilder.filter(QueryBuilders
                    //传入时间，格式 2020-01-02T03:17:37.638Z
                    .rangeQuery("create_time")
                    .from(getUTCTime((Date) map.get("startTime")))
                    .to(getUTCTime((Date) map.get("endTime"))));
        }
        logger.info(" = = = >>> map 查询条件: {}", boolQueryBuilder.toString()
                .replace(" ", "").replace("\n", ""));
        return boolQueryBuilder;
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
