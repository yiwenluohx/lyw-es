package com.study.elastic;

import com.alibaba.fastjson.JSONArray;
import com.study.elastic.entity.GeneSnpData;
import com.study.elastic.service.IGeneSnpDataService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author luohx
 * @version 1.0.0
 * @date: 2023/6/30 下午5:16
 * @menu
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationRunner.class)
public class GeneSnpDataTest {

    @Resource
    private IGeneSnpDataService geneSnpDataService;

    @Test
    public void createIndexTest() {
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

    @Test
    public void searchTest() {
        String index = "test_index";
        Pageable pageable = PageRequest.of(1, 1);
        List<GeneSnpData> list = geneSnpDataService.search(index, pageable, null, GeneSnpData.class,
                null, new String[]{}, new String[]{}, "position", true);
        System.out.println(" = = = >>> list=" + JSONArray.toJSONString(list));
    }

}
