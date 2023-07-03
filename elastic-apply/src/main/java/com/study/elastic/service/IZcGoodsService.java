package com.study.elastic.service;

import com.study.elastic.entity.ZcGoodsWithPageResDto;
import com.study.elastic.param.ZcSelfGoodsSearchParam;

/**
 * 质采商品es操作service
 *
 * @author luohx
 * @version 1.0.0
 * @date: 2023/7/3 下午2:16
 * @menu 质采商品es操作service
 */
public interface IZcGoodsService extends IBaseService<ZcGoodsWithPageResDto> {


    /**
     * 查询质采自有商品（带分页）
     *
     * @param param 参数
     * @return {@link ZcGoodsWithPageResDto}
     */
    ZcGoodsWithPageResDto queryZcSelfGoodsWithPage(ZcSelfGoodsSearchParam param);
}
