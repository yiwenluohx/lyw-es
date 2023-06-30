package com.study.elastic.service;

import com.alibaba.fastjson.JSONObject;
import com.study.elastic.entity.GeneSnpData;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * es基础操作
 *
 * @author luohx
 * @version 1.0.0
 * @date: 2023/6/30 上午10:56
 * @menu es基础操作
 */
public interface IBaseService<T> {

    /**
     * 创建索引
     *
     * @param index 索引库名称
     * @param max   单次查询最大量
     */
    void createIndex(String index, Long max);

    /**
     * 删除索引
     *
     * @param index 索引库名称
     * @return boolean
     */
    boolean deleteIndex(String index);

    /**
     * 判断索引库是否存在
     *
     * @param index 索引库名称
     * @return boolean
     */
    boolean isExistIndex(String index);

    /**
     * 关闭索引
     *
     * @param index 索引库名称
     */
    void closeIndex(String index);

    /**
     * 开启索引
     *
     * @param index 索引库名称
     */
    void openIndex(String index);

    /**
     * 查询索引库条数
     *
     * @param index            索引库名称
     * @param boolQueryBuilder 条件对象
     * @return {@link Long}
     */
    Long searchCount(String index, BoolQueryBuilder boolQueryBuilder);

    /**
     * 查询数据
     *
     * @param index            索引库名称
     * @param pageable         分页对象
     * @param routing          路由
     * @param boolQueryBuilder 条件对象
     * @param showField        指定查询字段 可传空数组
     * @param shieldField      指定排除字段 可传空数组
     * @param orderField       排序字段
     * @param bl               true.正序 false.倒序
     * @return {@link List}
     */
    List<T> search(String index, Pageable pageable, String routing, BoolQueryBuilder boolQueryBuilder,
                   String[] showField, String[] shieldField, String orderField, boolean bl);

    /**
     * 分组排序
     *
     * @param index            索引库名称
     * @param routing          路由
     * @param boolQueryBuilder 条件对象
     * @param showField        指定查询字段 可传空数组
     * @param shieldField      指定查询字段 可传空数组
     * @param groupField       分组字段 字段类型必须是字符串
     * @param sortField        排序字段
     * @param bl               排序规则 true.ack   false.desc
     * @return {@link List}
     */
    List<T> searchByGroup(String index, String routing, BoolQueryBuilder boolQueryBuilder,
                          String[] showField, String[] shieldField, String groupField, String sortField,
                          boolean bl);

    /**
     * 分组计数
     *
     * @param index            索引库名称
     * @param boolQueryBuilder 条件对象
     * @param groupField       分组字段 字段类型必须是字符串
     * @return {@link JSONObject}
     */
    JSONObject searchByGroupNum(String index, BoolQueryBuilder boolQueryBuilder, String groupField);

    /**
     * 数据去重
     *
     * @param index            索引库的名称
     * @param boolQueryBuilder 条件对象
     * @param showField        指定查询字段 可传空数组
     * @param shieldField      指定查询字段 可传空数组
     * @param distinctField    去重字段
     * @return {@link List}<{@link T}>
     */
    List<T> searchByDistinct(String index, BoolQueryBuilder boolQueryBuilder,
                             String[] showField, String[] shieldField, String distinctField);

    /**
     * 保存、修改数据（固定字段对象）
     *
     * @param t       实体对象
     * @param index   索引库名称
     * @param routing 路由
     */
    void saveByObject(T t, String index, String routing);

    /**
     * 保存、修改数据（不固定字段对象）
     *
     * @param map     数据 ( id:必填字段 )
     * @param index   索引库名称
     * @param routing 路由
     */
    void save(Map<String, Object> map, String index, String routing);

    /**
     * 删除索引库内所有数据
     *
     * @param indices 索引库名称
     * @return {@link Long}
     */
    Long deleteAllByIndices(String indices);

    /**
     * 条件删除数据
     *
     * @param indices          索引库名称
     * @param routing          路由
     * @param boolQueryBuilder 条件对象
     * @return {@link Long}
     */
    Long deleteByCondition(String indices, String routing, BoolQueryBuilder boolQueryBuilder);
}
