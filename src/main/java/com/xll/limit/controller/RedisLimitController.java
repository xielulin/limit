package com.xll.limit.controller;

import com.xll.limit.aop.FlowLimit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xielulin
 * @create 2021-03-07 19:53
 * @desc
 **/
@RestController
public class RedisLimitController {

    private static final Logger logger = LoggerFactory.getLogger(RedisLimitController.class);

    @FlowLimit(permit = 1,key = "limitkey1")
    @GetMapping("/limit")
    public String limit(){
        return "ok";
    }

}
