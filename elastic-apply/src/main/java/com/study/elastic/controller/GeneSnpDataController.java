package com.study.elastic.controller;

import com.study.elastic.entity.GeneSnpData;
import com.study.elastic.service.IGeneSnpDataService;
import org.elasticsearch.index.query.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import java.util.*;

/**
 * @author luohx
 * @version 1.0.0
 * @date: 2023/6/30 下午5:59
 * @menu
 */
@RequestMapping("/demo")
@RestController
public class GeneSnpDataController {

    @Resource
    private IGeneSnpDataService geneSnpDataService;

    @GetMapping("/createIndex")
    public void createIndex() {
        String index = "test_index";
        // 自定义测试的文件数据
        for (int i = 0; i < 10; i++) {
//            Map<String, Object> map = new HashMap<>();
//            // 当ID一致时, 新数据会覆盖旧数据, 也就是修改
//            map.put("id", UUID.randomUUID().toString());
//            map.put("file_id", 80 + i);
//            map.put("crop_id", "1");
//            map.put("position", 10606360L + i);
//            map.put("create_user_id", "6");
//            map.put("create_time", new Date());
//            geneSnpDataService.save(map, index, null);
            GeneSnpData snpData = new GeneSnpData();
            String id = UUID.randomUUID().toString();
            snpData.setId(id);
            snpData.setFile_id(String.valueOf(80 + i));
            String str = "测试" + (80 + i);
            if ((80 + i) % 2 == 0) {
                str = "测试一下：" + (80 + i);
            }
            snpData.setFile_name(str);
            snpData.setCrop_id("1");
            snpData.setPosition(10606360L + i);
            snpData.setCreate_time(new Date());
            geneSnpDataService.saveByObject(snpData, id, index, null);
        }
    }

    @GetMapping("/deleteIndex")
    public void deleteIndex(@NotBlank(message = "索引名称不能为空") @RequestParam(value = "indexName") String indexName) {
        geneSnpDataService.deleteIndex(indexName);
    }

    @GetMapping("/search")
    public ResponseEntity searchTest(@RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex, @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        String index = "test_index";
        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        //MultiMatchQueryBuilder.Type.PHRASE    按多字段、短语匹配 （不分词检索）
        boolQueryBuilder.should(QueryBuilders.multiMatchQuery("一下", "file_name").type(MultiMatchQueryBuilder.Type.PHRASE).operator(Operator.AND).slop(0));
        List<GeneSnpData> list = geneSnpDataService.search(index, pageable, null, GeneSnpData.class,
                boolQueryBuilder, new String[]{}, new String[]{}, "position", true);
        return ResponseEntity.ok(list);
    }

}
