package com.offcn.user;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserStartApplication.class)
public class ScwUserApplicationTest {

    Logger log = LoggerFactory.getLogger(ScwUserApplicationTest.class);

    @Autowired
    private StringRedisTemplate stringRedisTemplate; //存入字符串 和 RedisTemplare 不同

    @Autowired
    private RedisTemplate redisTemplate;
    @Test
    public void contextLoads(){
        redisTemplate.opsForValue().set("msg1","欢迎来优就业学习");
        //stringRedisTemplate.opsForValue().set("msg","欢迎来优就业学习！");
        log.debug("操作成功");
    }
}
