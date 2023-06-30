package com.study.elastic.controller;

import com.study.elastic.entity.GeneSnpData;
import com.study.elastic.service.IGeneSnpDataService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
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
            Map<String, Object> map = new HashMap<>();
            // 当ID一致时, 新数据会覆盖旧数据, 也就是修改
            map.put("id", UUID.randomUUID().toString());
            map.put("file_id", 80 + i);
            map.put("crop_id", "1");
            map.put("position", 10606360L + i);
            map.put("create_user_id", "6");
            map.put("create_time", new Date());
            geneSnpDataService.save(map, index, null);
        }
    }

    @GetMapping("/search")
    public ResponseEntity searchTest(@RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex, @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        String index = "test_index";
        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        List<GeneSnpData> list = geneSnpDataService.search(index, pageable, null, GeneSnpData.class,
                null, new String[]{}, new String[]{}, "position", true);
        return ResponseEntity.ok(list);
    }

}
