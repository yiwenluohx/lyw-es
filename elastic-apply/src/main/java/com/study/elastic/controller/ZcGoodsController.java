package com.study.elastic.controller;

import com.study.elastic.entity.ZcGoodsWithPageResDto;
import com.study.elastic.entity.ZcSelfGoodsModel;
import com.study.elastic.param.ZcSelfGoodsSearchParam;
import com.study.elastic.service.IZcGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 质采商品controller
 *
 * @author luohx
 * @version 1.0.0
 * @date: 2023/7/3 下午1:57
 * @menu 质采商品controller
 */
@RequestMapping("/es/goods")
@RestController
public class ZcGoodsController {

    @Autowired
    private IZcGoodsService zcGoodsService;

    @PostMapping("/queryZcSelfGoods")
    public ResponseEntity<ZcGoodsWithPageResDto<ZcSelfGoodsModel>> queryZcSelfGoods(ZcSelfGoodsSearchParam param) {
        ZcGoodsWithPageResDto resDto = zcGoodsService.queryZcSelfGoodsWithPage(param);
        return ResponseEntity.ok(resDto);
    }

}
