package com.study.elastic.service;

import com.study.elastic.entity.ZcGoodsWithPageResDto;
import com.study.elastic.param.ZcSelfGoodsSearchParam;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.search.MatchQueryParser;
import org.springframework.stereotype.Service;

/**
 * 质采商品es操作service实现
 *
 * @author luohx
 * @version 1.0.0
 * @date: 2023/7/3 下午2:19
 * @menu 质采商品es操作service实现
 */
@Service("zcGoodsService")
public class ZcGoodsServiceImpl extends BaseService<ZcGoodsWithPageResDto> implements IZcGoodsService {

    @Override
    public ZcGoodsWithPageResDto queryZcSelfGoodsWithPage(ZcSelfGoodsSearchParam param) {
        //构建自定义查询条件
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        if(StringUtils.isNotBlank(param.getKeywords())){
            /**
             * should 至少满足一个条件就返回
             * must  必须满足条件才返回
             * must_not  对must取反
             */
            boolQueryBuilder.should(QueryBuilders.multiMatchQuery(param.getKeywords().trim(), "goodsName").type(MultiMatchQueryBuilder.Type.PHRASE).operator(
            Operator.AND).slop(0));


        }

        return null;
    }
}
