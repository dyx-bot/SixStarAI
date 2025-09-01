package com.example.appbacked.controller;

import com.example.appbacked.entity.User;
import com.example.appbacked.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@CrossOrigin(origins = "http://localhost:*",
        allowCredentials = "true")


@RestController
@RequestMapping("/api")
public class LoginController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody User user) {
        boolean success = userService.login(user);

        Map<String, Object> response = new HashMap<>();
        if (success) {
            // 登录成功，返回用户信息
            response.put("success", true);
            response.put("message", "登录成功");
            response.put("userId", user.getId());
            response.put("username", user.getUsername()); // userService 已填充
            response.put("avatar", user.getAvatar());     // userService 已填充
            response.put("grade", user.getGrade());
            response.put("college", user.getCollege());
        } else {
            response.put("success", false);
            response.put("message", "ID 或密码错误");
            // 失败时不返回用户信息
        }

        return response;
    }
}