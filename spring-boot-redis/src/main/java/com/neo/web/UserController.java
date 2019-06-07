package com.neo.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.neo.model.User;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
public class UserController {

    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping("/getUser")
    @Cacheable(value = "user-key")
    public User getUser() {
        User user = new User("aa@126.com", "aa", "aa123456", "aa", "123");
        System.out.println("若下面没出现“无缓存的时候调用”字样且能打印出数据表示测试成功");
        return user;
    }


    @RequestMapping("/uid")
    String uid(HttpSession session) {
        UUID uid = (UUID) session.getAttribute("uid");
        if (uid == null) {
            uid = UUID.randomUUID();
        }
        session.setAttribute("uid", uid);
        return session.getId();
    }

    //记录用户上一次登录时间,存到redis中
    @RequestMapping("/login/{id}")
    String login(@PathVariable Integer id) {
        String response = "用户ID为" + id + ",上一次登录时间:";
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String userLoginDateTime = (String) valueOperations.get("user::" + id);
        String loginDateTime = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(new Date());
        System.out.println(loginDateTime);
        valueOperations.set("user::" + id, loginDateTime);
        if (!StringUtils.isEmpty(userLoginDateTime)) {
            response = response + userLoginDateTime;
        } else {
            response = response + loginDateTime;
        }

        return response;
    }
}