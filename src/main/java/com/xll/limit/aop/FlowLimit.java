package com.xll.limit.aop;

import java.lang.annotation.*;

/**
 * @author xielulin
 * @create 2021-03-07 19:38
 * @desc 注解
 **/
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface FlowLimit {
    /**
     * 请求令牌数
     * */
    int permit() default 1;

    /**
     * 请求的key
     * */
    String key();
}
