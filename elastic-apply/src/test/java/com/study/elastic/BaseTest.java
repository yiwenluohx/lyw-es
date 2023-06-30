package com.study.elastic;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author luohx
 * @version 1.0.0
 * @date: 2023/6/30 下午5:07
 * @menu
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationRunner.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BaseTest {

    /**
     * 日志
     */
    protected static final Logger log = LoggerFactory.getLogger(BaseTest.class);

    /**
     * 执行开始时间
     */
    private Long startTime;

    @Before
    public void before() {
        this.setStartTime(System.currentTimeMillis());
        log.info("====================> 测试开始执行 <====================");
    }

    @After
    public void after() {
        log.info("====================> 测试执行完成，耗时：{}ms <====================",System.currentTimeMillis() - this.getStartTime());
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }
}
