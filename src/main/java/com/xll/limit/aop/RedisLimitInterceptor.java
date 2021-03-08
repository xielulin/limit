package com.xll.limit.aop;

import com.google.common.collect.ImmutableList;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

import java.lang.reflect.Method;

/**
 * @author xielulin
 * @create 2021-03-07 19:40
 * @desc
 **/
@Aspect
@Configuration
public class RedisLimitInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(RedisLimitInterceptor.class);

   /* @Autowired
    private RedisTemplate redisTemplate;*/

    @Autowired
    private RedisTemplate redisTemplate;


    @Around("execution(public * *(..))&&@annotation(com.xll.limit.aop.FlowLimit)")
    public Object interceptor(ProceedingJoinPoint point){

        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        FlowLimit annotation = method.getAnnotation(FlowLimit.class);
        int permit = annotation.permit();

        ImmutableList<String> keys = ImmutableList.of(annotation.key());
        logger.info(String.format("欲取出的令牌数:%d key:%s ",permit,keys.get(0)));

        logger.info("开始尝试取出令牌...");

        try {
            //先获取系统时间
            logger.info("尝试获取当前时间...");
            DefaultRedisScript<Number> redisScript = new DefaultRedisScript<Number>();
            redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("script/redis_currentTimeMillis")));
            redisScript.setResultType(Number.class);
            Number currentTimeMillies = (Number) redisTemplate.execute(redisScript,keys);
            logger.info(String.format("当前时间:%d",currentTimeMillies));


            redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("script/redis_acquire")));
            
            //请求令牌
            System.out.printf(keys.get(0));
            //Boolean aBoolean = redisTemplate.hasKey(keys.get(0));
            Number reqResult = (Number) redisTemplate.execute(redisScript,keys,annotation.permit(),currentTimeMillies.intValue());
           // redisTemplate
            logger.info(String.format("请求结果:%d",reqResult.intValue()));

            if(reqResult.intValue()==1){
                logger.info("请求成功");
                return point.proceed();
            }
            else{
                logger.info("限流了");
                throw new RuntimeException("超限限流");
            }

        }catch (Throwable e){
            if(e instanceof RuntimeException){
                e.printStackTrace();
                throw new RuntimeException(e.getLocalizedMessage());
            }
            throw new RuntimeException("服务器错误");
        }
    }

}
